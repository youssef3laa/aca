package com.asset.appwork.util;

import com.asset.appwork.model.Person;
import com.asset.appwork.model.Unit;
import com.asset.appwork.model.User;
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
    public static class UnitCreationDeserializer extends StdDeserializer<Unit> {

        public UnitCreationDeserializer() {
            this(null);
        }

        public UnitCreationDeserializer(Class<?> cls) {
            super(cls);
        }

        @Override
        public Unit deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode root = parser.getCodec().readTree(parser);
            Unit unit = new Unit();
            unit.setName(String.format("%s-%s", root.get("typeCode").asText(), root.get("code").asText()));
            unit.setNameAr(root.get("name").asText());
            unit.setUnitCode(root.get("code").asText());
            unit.setUnitTypeCode(root.get("typeCode").asText());

            unit.setDescription(String.format("%s-%s", root.get("parentTypeCode").asText(), root.get("parentCode").asText()));

            return unit;
        }

    }
    public static class UnitCreationSerializer extends StdSerializer<Unit> {

        public UnitCreationSerializer() {
            this(null);
        }

        public UnitCreationSerializer(Class<Unit> t) {
            super(t);
        }

        @Override
        public void serialize(Unit unit, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("name", unit.getName());
            jsonGenerator.writeStringField("nameAr", unit.getNameAr());
            jsonGenerator.writeStringField("unitTypeCode", unit.getUnitTypeCode());
            jsonGenerator.writeStringField("unitCode", unit.getUnitCode());
            jsonGenerator.writeEndObject();
        }
    }

    public static class UnitRenamingDeserializer extends StdDeserializer<Unit> {

        public UnitRenamingDeserializer() {
            this(null);
        }

        public UnitRenamingDeserializer(Class<?> cls) {
            super(cls);
        }

        @Override
        public Unit deserialize(JsonParser parser, DeserializationContext context) throws IOException  {
            JsonNode root = parser.getCodec().readTree(parser);
            Unit unit = new Unit();
            unit.setNameAr(root.get("newName").asText());
            unit.setUnitCode(root.get("newCode").asText());
            unit.setUnitTypeCode(root.get("typeCode").asText());

            unit.setDescription(root.get("code").asText());

            return unit;
        }

    }
    public static class UnitRenamingSerializer extends StdSerializer<Unit> {

        public UnitRenamingSerializer() {
            this(null);
        }

        public UnitRenamingSerializer(Class<Unit> t) {
            super(t);
        }

        @Override
        public void serialize(Unit unit, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("name", unit.getName());
            jsonGenerator.writeStringField("nameAr", unit.getNameAr());
            jsonGenerator.writeStringField("unitCode", unit.getUnitCode());
            jsonGenerator.writeEndObject();
        }
    }

    public static class UnitChangingParentDeserializer extends StdDeserializer<Unit> {

        public UnitChangingParentDeserializer() {
            this(null);
        }

        public UnitChangingParentDeserializer(Class<?> cls) {
            super(cls);
        }

        @Override
        public Unit deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode root = parser.getCodec().readTree(parser);
            Unit unit = new Unit();
            unit.setUnitCode(root.get("code").asText());
            unit.setUnitTypeCode(root.get("typeCode").asText());

            unit.setDescription(String.format("%s-%s-%s", root.get("newParentTypeCode").asText(), root.get("newParentCode").asText(), root.get("newCode").asText()));

            return unit;
        }

    }
    public static class UnitChangingParentSerializer extends StdSerializer<Unit> {

        public UnitChangingParentSerializer() {
            this(null);
        }

        public UnitChangingParentSerializer(Class<Unit> t) {
            super(t);
        }

        @Override
        public void serialize(Unit unit, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("unitCode", unit.getUnitCode());
            jsonGenerator.writeEndObject();
        }
    }

    public static class UnitParentAndChangingUnitTypeCodeDeserializer extends StdDeserializer<Unit> {

        public UnitParentAndChangingUnitTypeCodeDeserializer() {
            this(null);
        }

        public UnitParentAndChangingUnitTypeCodeDeserializer(Class<?> cls) {
            super(cls);
        }

        @Override
        public Unit deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode root = parser.getCodec().readTree(parser);
            Unit unit = new Unit();
            unit.setUnitCode(root.get("code").asText());
            unit.setUnitTypeCode(root.get("typeCode").asText());

            unit.setDescription(String.format("%s-%s-%s-%s", root.get("newParentTypeCode").asText(), root.get("newParentCode").asText(), root.get("newTypeCode").asText(), root.get("newCode").asText()));

            return unit;
        }

    }
    public static class UnitParentAndChangingUnitTypeCodeSerializer extends StdSerializer<Unit> {

        public UnitParentAndChangingUnitTypeCodeSerializer() {
            this(null);
        }

        public UnitParentAndChangingUnitTypeCodeSerializer(Class<Unit> t) {
            super(t);
        }

        @Override
        public void serialize(Unit unit, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("unitTypeCode", unit.getUnitTypeCode());
            jsonGenerator.writeStringField("unitCode", unit.getUnitCode());
            jsonGenerator.writeEndObject();
        }
    }

    public static class UserUpdateDeserializer extends StdDeserializer<User> {

        public UserUpdateDeserializer() {
            this(null);
        }

        public UserUpdateDeserializer(Class<?> cls) {
            super(cls);
        }

        @Override
        public User deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode root = parser.getCodec().readTree(parser);
            User user = new User();
            Person person = new Person();
            user.setDescription(Integer.toString(root.get("id").asInt()));
            user.setDisplayName(root.get("displayName").asText());
            person.setTitle(root.get("grade").asText());

            person.setNotes(String.format("%s-%s-%s", root.get("newTypeCode").asText(), root.get("newUnitTypeCode").asText(), root.get("newUnitCode").asText()));

            user.setPerson(person);

            return user;
        }

    }
    public static class UserUpdateSerializer extends StdSerializer<User> {

        public UserUpdateSerializer() {
            this(null);
        }

        public UserUpdateSerializer(Class<User> t) {
            super(t);
        }

        @Override
        public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
            jsonGenerator.writeStartObject();
//            jsonGenerator.writeStringField("unitTypeCode", user.getUnitTypeCode());
//            jsonGenerator.writeStringField("unitCode", user.getUnitCode());
            jsonGenerator.writeEndObject();
        }
    }
}