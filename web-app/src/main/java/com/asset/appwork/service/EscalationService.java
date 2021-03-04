package com.asset.appwork.service;

import com.asset.appwork.dto.Account;
import com.asset.appwork.dto.EscalationDTO;
import com.asset.appwork.enums.ResponseCode;
import com.asset.appwork.exception.AppworkException;
import com.asset.appwork.model.Escalation;
import com.asset.appwork.model.Lookup;
import com.asset.appwork.platform.rest.Entity;
import com.asset.appwork.repository.EscalationRepository;
import com.asset.appwork.repository.LookupRepository;
import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class EscalationService {

    @Autowired
    EscalationRepository escalationRepository;

    @Autowired
    LookupRepository lookupRepository;

    @Autowired
    Environment env;

    public EscalationDTO createEscalation(Account account, String props) throws AppworkException, JsonProcessingException {
        return getEscalationDTO(
                new Entity(account, SystemUtil.generateRestAPIBaseUrl(env, env.getProperty("aca.general.solution")), "ACA_Entity_Escalation")
                .create(props)
        );
    }

    public Escalation getEscalation(Long id) throws AppworkException {
        return escalationRepository.findById(id).orElseThrow(
                () -> new AppworkException("Could not get Escalation Entity of id " + id, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public EscalationDTO getEscalationDTO(Long id) throws AppworkException {
        return escalationToDTO(escalationRepository.findById(id).orElseThrow(
                () -> new AppworkException("Could not get Escalation Entity of id " + id, ResponseCode.READ_ENTITY_FAILURE)
        ));
    }

    public EscalationDTO updateEscalation(Long id, Escalation newEscalation) throws AppworkException, JsonProcessingException {
        Escalation escalation = getEscalation(id);
        escalation.setDuration(newEscalation.getDuration());
        escalation.setExtension(newEscalation.getExtension());
        escalation.setJobType(newEscalation.getJobType());
        escalation.setUnitType(newEscalation.getUnitType());
        escalationRepository.save(escalation);
        return getEscalationDTO(id);
    }

    public void deleteEscalation(Long id) {
        escalationRepository.deleteById(id);
    }

    public List<EscalationDTO> getAllEscalation() throws AppworkException {
        List<EscalationDTO> escalationDTOList = new ArrayList<>();
        escalationRepository.findAll().forEach(escalation -> {
            try {
                escalationDTOList.add(escalationToDTO(escalation));
            } catch (AppworkException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        });
        return escalationDTOList;
    }

    public List<EscalationDTO> getAllEscalationJobTypes() throws AppworkException {
        List<EscalationDTO> escalationDTOList = new ArrayList<>();
        Page<Lookup> jobTypes = lookupRepository.findCategoryValues("jobType", "", PageRequest.of(0, Integer.MAX_VALUE, Sort.by("id")));
        jobTypes.forEach(jobType -> {
            try {
                escalationDTOList.add(JobTypeToEscalationDTO(jobType));
            } catch (AppworkException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        });
        return escalationDTOList;
    }

//    public Page<Lookup> getAllEscalationJobTypes(int page, int size) throws AppworkException {
//        Page<Lookup> lookupPage = lookupRepository.findCategoryValues("jobType", "", PageRequest.of(page, size, Sort.by("id")));
//        lookupPage.getContent().forEach(jobType -> {
//            AtomicReference<Integer> duration = new AtomicReference<>(0);
//            escalationRepository.findByJobTypeKey(Integer.parseInt(jobType.getKey())).ifPresent((escalation -> duration.set(escalation.getDuration())));
//            jobType.setType(duration.get());
//        });
//        return lookupPage;
//    }

    public Lookup getJobTitleByKey(Integer key) throws AppworkException {
        return lookupRepository.findByCategoryAndKey("jobType", key.toString()).orElseThrow(
                () -> new AppworkException("Could not get Lookup Entity of category: jobType and key: " + key, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public Lookup getUnitTypeByKey(Integer key) throws AppworkException {
        return lookupRepository.findByCategoryAndKey("unitType", key.toString()).orElseThrow(
                () -> new AppworkException("Could not get Lookup Entity of category: unitType and key: " + key, ResponseCode.READ_ENTITY_FAILURE)
        );
    }

    public EscalationDTO escalationToDTO(Escalation escalation) throws AppworkException {
        EscalationDTO escalationDTO = new EscalationDTO();
        escalationDTO.setId(escalation.getId());
        escalationDTO.setDuration(escalation.getDuration());
        escalationDTO.setExtension(escalation.getExtension());
        escalationDTO.setJobType(getJobTitleByKey(escalation.getJobType()));
        escalationDTO.setUnitType(getUnitTypeByKey(escalation.getUnitType()));
        return escalationDTO;
    }

    public EscalationDTO JobTypeToEscalationDTO(Lookup jobType) throws AppworkException {
        AtomicReference<EscalationDTO> escalationDTO = new AtomicReference<>();
        Optional<Escalation> escalation = escalationRepository.findByJobType(Integer.parseInt(jobType.getKey()));

        escalation.ifPresentOrElse(esc ->  {
            try {
                escalationDTO.set(escalationToDTO(esc));
            } catch (AppworkException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }, () -> {
            EscalationDTO escDTO = new EscalationDTO();
            escDTO.setDuration(0);
            escDTO.setExtension(0);
            escDTO.setJobType(jobType);

            escalationDTO.set(escDTO);
        });
        return escalationDTO.get();
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
            escalation.setExtension(root.get("extension").asInt());
            escalation.setJobType(root.get("jobType").asInt());
            escalation.setUnitType(root.get("unitType").asInt());
            return escalation;
        }
    }
}
