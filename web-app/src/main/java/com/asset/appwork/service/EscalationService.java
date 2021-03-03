package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Escalation;
import com.asset.appwork.model.Lookup;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.EscalationRepository;
import com.asset.appwork.repository.LookupRepository;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EscalationService {

    @Autowired
    EscalationRepository escalationRepository;

    @Autowired
    LookupRepository lookupRepository;

    @Autowired
    Environment env;

    public Escalation createEscalation(Account account, String props) throws AppworkException, JsonProcessingException {
        Long createdId = new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, env.getProperty("aca.general.solution")), "ACA_Entity_Escalation")
                .create(toEscalationPlatformString(fromEscalationString(props)));
        return updateEscalation(createdId, props);
    }

    public Escalation getEscalation(Long id) throws AppworkException {
        return escalationRepository.findById(id).orElseThrow(
                () -> new AppworkException("Could not get Escalation Entity of id " + id, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public Escalation updateEscalation(Long id, String props) throws AppworkException, JsonProcessingException {
        Escalation escalation = getEscalation(id);
        Escalation newEscalation = fromEscalationString(props);
        escalation.setDuration(newEscalation.getDuration());
        escalation.setJobType(newEscalation.getJobType());
        return escalationRepository.save(escalation);
    }

    public void deleteEscalation(Long id) {
        escalationRepository.deleteById(id);
    }

    public Lookup getJobTitleByKey(String key) throws AppworkException {
        return lookupRepository.findByCategoryAndKey("jobType", key).orElseThrow(
                () -> new AppworkException("Could not get Lookup Entity of category: jobType and key: " + key, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public Escalation fromEscalationString(String json) throws JsonProcessingException, AppworkException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Escalation.class, new EscalationDeserializer());
        mapper.registerModule(module);
        try {
            return mapper.readValue(json, Escalation.class);
        } catch (NullPointerException e) {
            throw new AppworkException("Unknown Properties in JSON", ResponseCode.BAD_REQUEST);
        }
    }

    public String toEscalationPlatformString(Escalation escalation) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Escalation.class, new EscalationSerializer());
        mapper.registerModule(module);
        return mapper.writeValueAsString(escalation);
    }

    private class EscalationDeserializer extends StdDeserializer<Escalation> {

        public EscalationDeserializer() {
            this(null);
        }

        public EscalationDeserializer(Class<?> cls) {
            super(cls);
        }

        @SneakyThrows
        @Override
        public Escalation deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode root = parser.getCodec().readTree(parser);
            Escalation escalation = new Escalation();
            escalation.setDuration(root.get("duration").asInt());
            escalation.setJobType(getJobTitleByKey(root.get("jobTitle").asText()));
            return escalation;
        }

    }

    private class EscalationSerializer extends StdSerializer<Escalation> {

        public EscalationSerializer() {
            this(null);
        }

        public EscalationSerializer(Class<Escalation> t) {
            super(t);
        }

        @Override
        public void serialize(Escalation escalation, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("duration", escalation.getDuration());
            jsonGenerator.writeEndObject();
        }
    }
}
