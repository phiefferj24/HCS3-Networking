package com.jimphieffer.utils.json;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.jimphieffer.utils.json.annotations.*;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AnnotatedDecoder extends Decoder {
    public AnnotatedDecoder(String json) {
        super(json);
    }

    private void decodeAnnotated() {
        try {
            JsonNode arrayNode = new ObjectMapper().readTree(json);
            if (!arrayNode.isArray()) {
                System.err.println("Error: Invalid json format: JSON is not an array");
                return;
            }
            Object[] objects = new Object[arrayNode.size()];
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode node = arrayNode.get(i);
                Class<?> type = Class.forName(node.get("type").asText());
                ObjectNode objectNode = (ObjectNode) node.get("object");
                Constructor<?>[] constructors = type.getConstructors();
                for(Constructor<?> constructor : constructors) {
                    if(!constructor.isAnnotationPresent(JsonDefaultConstructor.class)) {
                        continue;
                    }
                    JsonDefaultConstructor annotation = constructor.getAnnotation(JsonDefaultConstructor.class);
                    String[] parameterNames = annotation.names();
                    Class<?>[] parameterTypes = constructor.getParameterTypes();
                    if(parameterNames.length != parameterTypes.length) {
                        System.err.println("Error: JsonDefaultConstructor annotation " + Arrays.toString(parameterNames) + " does not match constructor parameters " + Arrays.toString(parameterTypes));
                        continue;
                    }
                    Object[] parameters = new Object[parameterTypes.length];
                    for (int j = 0; j < parameterNames.length; j++) {
                        switch(parameterTypes[j].getName()) {
                            case "int", "java.lang.Integer" -> parameters[j] = objectNode.get(parameterNames[j]).asInt();
                            case "long", "java.lang.Long" -> parameters[j] = objectNode.get(parameterNames[j]).asLong();
                            case "double", "java.lang.Double" -> parameters[j] = objectNode.get(parameterNames[j]).asDouble();
                            case "float", "java.lang.Float" -> parameters[j] = (float)objectNode.get(parameterNames[j]).asDouble();
                            case "boolean", "java.lang.Boolean" -> parameters[j] = objectNode.get(parameterNames[j]).asBoolean();
                            case "java.lang.String" -> parameters[j] = objectNode.get(parameterNames[j]).asText();
                            default -> {
                                ObjectNode parameterNode = (ObjectNode) objectNode.get(parameterNames[j]);
                                if(!assignmentMethods.containsKey(parameterTypes[j])) {
                                    System.err.println("Error: No assignment method for " + parameterTypes[j].getName());
                                } else if (!parameterNode.get("type").asText().equals(parameterTypes[j].getName())) {
                                    System.err.println("Error: Parameter type mismatch: " + parameterTypes[j].getName() + " != " + parameterNode.get("type").asText());
                                } else {
                                    parameters[j] = assignmentMethods.get(parameterTypes[j]).from(parameterNode.get("value").textValue());
                                }
                            }
                        }
                    }
                    objects[i] = constructor.newInstance(parameters);
                }
            }
            this.objects = objects;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Class not found: " + e);
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
            System.err.println("Error: Could not construct object: " + e);
        }
    }
    public Object[] getObjects() {
        if (objects == null || objects.length == 0) {
            decodeAnnotated();
        }
        return objects;
    }

    /**
     * @param type The type of objects to get
     * @return An array of objects of the given type
     */
    @SuppressWarnings("unchecked")
    public <T> T[] getObjects(Class<T> type) {
        if (objects == null || objects.length == 0) {
            decodeAnnotated();
        }
        ArrayList<T> list = new ArrayList<>();
        for (Object object : objects) {
            if (type.equals(object.getClass())) list.add((T) object);
        }
        return list.toArray((T[]) Array.newInstance(type, list.size()));
    }

    /**
     * @param type The type of objects to get along with all subclasses
     * @return An array of objects of the given type and all subclasses
     */
    @SuppressWarnings("unchecked")
    public <T> T[] getDerivativeObjects(Class<T> type) {
        if (objects == null || objects.length == 0) {
            decodeAnnotated();
        }
        ArrayList<T> list = new ArrayList<>();
        for (Object object : objects) {
            if (type.isAssignableFrom(object.getClass())) list.add((T) object);
        }
        return list.toArray((T[]) Array.newInstance(type, list.size()));
    }
}
