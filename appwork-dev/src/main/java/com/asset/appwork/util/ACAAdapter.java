package com.asset.appwork.util;

import com.asset.appwork.model.Unit;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ACAAdapter {
    public static class UnitDeserializer extends StdDeserializer<Unit> {

        public UnitDeserializer() {
            this(null);
        }

        public UnitDeserializer(Class<?> cls) {
            super(cls);
        }

        @Override
        public Unit deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
            JsonNode root = parser.getCodec().readTree(parser);
            Unit unit = new Unit();
            unit.setName(String.format("%s-%s", root.at("/typeCode").asText(), root.at("/code").asText()));
            unit.setNameAr(root.at("/name").asText());
            unit.setUnitCode(root.at("/code").asText());
            unit.setUnitTypeCode(root.at("/typeCode").asText());

            unit.setDescription(String.format("%s-%s", root.at("/parentTypeCode").asText(), root.at("/parentCode").asText()));

            return unit;
        }

    }
    public static class UnitSerializer extends StdSerializer<Unit> {

        public UnitSerializer() {
            this(null);
        }

        public UnitSerializer(Class<Unit> t) {
            super(t);
        }

        @Override
        public void serialize(Unit unit, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("name", unit.getName());
            jsonGenerator.writeStringField("nameEn", unit.getNameEn());
            jsonGenerator.writeStringField("nameAr", unit.getNameAr());
            jsonGenerator.writeStringField("unitTypeCode", unit.getUnitTypeCode());
            jsonGenerator.writeStringField("unitCode", unit.getUnitCode());
            jsonGenerator.writeEndObject();
        }
    }
}