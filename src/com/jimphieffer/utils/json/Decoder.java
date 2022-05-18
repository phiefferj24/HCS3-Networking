package com.jimphieffer.utils.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class Decoder {
    protected final String json;
    protected final HashMap<Class<?>, Json.AssignmentMethod<?>> assignmentMethods = new HashMap<>();
    public Object[] objects = new Object[0];

    public Decoder(String json) {
        this.json = json;
    }

    private void decode() {
        try {
            JsonNode arrayNode = new ObjectMapper().readTree(json);
            if (!arrayNode.isArray()) {
                System.err.println("Error: Invalid json format: JSON is not an array");
                return;
            }
            Object[] objects = new Object[arrayNode.size()];
            for (int i = 0; i < arrayNode.size(); i++) {
                ObjectNode parentNode = (ObjectNode) arrayNode.get(i);
                String type = parentNode.get("type").asText();
                ObjectNode objectNode = (ObjectNode) parentNode.get("object");
                Class<?> classType = Class.forName(type);
                Method[] methods = classType.getMethods();
                Constructor<?>[] constructors = classType.getConstructors();
                boolean hasConstructor = false;
                for (Constructor<?> constructor : constructors) {
                    if (constructor.getParameterCount() == 0) {
                        hasConstructor = true;
                        try {
                            Object object = constructor.newInstance();
                            for (Method method : methods) {
                                if (method.getName().startsWith("set") && method.getParameterCount() == 1 && method.getReturnType() == void.class) {
                                    String propertyName = method.getName().substring(3).toLowerCase();
                                    if (objectNode.has(propertyName)) {
                                        JsonNodeType nodeType = objectNode.get(propertyName).getNodeType();
                                        switch (nodeType) {
                                            case BOOLEAN:
                                                if (method.getParameterTypes()[0].isAssignableFrom(Boolean.class))
                                                    method.invoke(object, objectNode.get(propertyName).asBoolean());
                                                break;
                                            case NUMBER:
                                                switch (method.getParameterTypes()[0].getName()) {
                                                    case "int", "java.lang.Integer" -> {
                                                        method.invoke(object, objectNode.get(propertyName).asInt());
                                                    }
                                                    case "long", "java.lang.Long" -> {
                                                        method.invoke(object, objectNode.get(propertyName).asLong());
                                                    }
                                                    case "float", "java.lang.Float" -> {
                                                        method.invoke(object, (float) objectNode.get(propertyName).asDouble());
                                                    }
                                                    case "double", "java.lang.Double" -> {
                                                        method.invoke(object, objectNode.get(propertyName).asDouble());
                                                    }
                                                }
                                                break;
                                            case STRING:
                                                if (method.getParameterTypes()[0].isAssignableFrom(String.class))
                                                    method.invoke(object, objectNode.get(propertyName).asText());
                                                break;
                                            case OBJECT:
                                                Class<?> varType = Class.forName(objectNode.get(propertyName).get("type").asText());
                                                if (method.getParameterTypes()[0].isAssignableFrom(varType)) {
                                                    if (assignmentMethods.containsKey(varType)) {
                                                        method.invoke(object, assignmentMethods.get(varType).from(objectNode.get(propertyName).get("value").textValue()));
                                                    } else {
                                                        System.err.println("Error: No assignment method found for type " + varType.getName());
                                                    }
                                                }
                                                break;
                                        }
                                    }
                                }
                            }
                            objects[i] = object;
                            break;
                        } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
                            System.err.println("Error: Cannot create instance of " + type + ": " + e);
                        }
                    }
                }
                if (!hasConstructor) {
                    System.err.println("Error: No constructor found for " + type);
                }
            }
            this.objects = objects;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Class not found: " + e);
        }
    }

    public <T> void addAssignmentMethod(Class<T> classObject, Json.AssignmentMethod<T> method) {
        assignmentMethods.put(classObject, method);
    }

    public Object[] getObjects() {
        if (objects == null || objects.length == 0) {
            decode();
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
            decode();
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
            decode();
        }
        ArrayList<T> list = new ArrayList<>();
        for (Object object : objects) {
            if (type.isAssignableFrom(object.getClass())) list.add((T) object);
        }
        return list.toArray((T[]) Array.newInstance(type, list.size()));
    }

    /**
     * @return A list of all objects with their properties represented as key-value pairs
     */
    public HashMap<String, HashMap<String, Object>[]> getPropertyMaps() {
        try {
            JsonNode arrayNode = new ObjectMapper().readTree(json);
            HashMap<String, HashMap<String, Object>[]> propertyMaps = new HashMap<>();
            for (int i = 0; i < arrayNode.size(); i++) {
                HashMap<String, Object>[] innerPropertyMaps = new HashMap[arrayNode.size()];
                JsonNode node = arrayNode.get(i);
                HashMap<String, Object> propertyMap = new HashMap<>();
                String className = node.get("type").asText();
                JsonNode vnode = node.get("object");
                for (Iterator<String> it = vnode.fieldNames(); it.hasNext(); ) {
                    String key = it.next();
                    JsonNode value = vnode.get(key);
                    if (value.isObject()) {
                        Class<?> valueType = Class.forName(value.get("type").asText());
                        JsonNode valueNode = value.get("value");
                        if (assignmentMethods.containsKey(valueType)) {
                            propertyMap.put(key, assignmentMethods.get(valueType).from(valueNode.asText()));
                        } else {
                            System.err.println("Error: No assignment method found for type " + valueType.getName());
                        }
                    } else {
                        switch (value.getNodeType()) {
                            case NUMBER -> propertyMap.put(key, value.asDouble());
                            case BOOLEAN -> propertyMap.put(key, value.asBoolean());
                            case STRING -> propertyMap.put(key, value.asText());
                            default -> System.err.println("Error: Unknown node type " + value.getNodeType());
                        }
                    }
                    innerPropertyMaps[i] = propertyMap;
                }
                propertyMaps.put(className, innerPropertyMaps);
            }
            return propertyMaps;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error: Cannot read JSON: " + e);
        }
        return null;
    }
}
