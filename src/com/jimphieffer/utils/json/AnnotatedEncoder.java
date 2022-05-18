package com.jimphieffer.utils.json;

import com.fasterxml.jackson.databind.node.*;
import com.jimphieffer.utils.json.annotations.*;

import java.lang.reflect.*;
import java.util.*;

public class AnnotatedEncoder extends Encoder {
    public void addAnnotatedObject(Object object) {
        Class<?> objectClass = object.getClass();
        Method[] methods = objectClass.getMethods();
        Field[] fields = objectClass.getFields();
        Constructor<?>[] constructors = objectClass.getConstructors();
        ObjectNode parentNode = node.addObject();
        parentNode.put("type", objectClass.getName());
        ObjectNode objectNode = parentNode.putObject("object");
        for(Constructor<?> constructor : constructors) {
            if(!constructor.isAnnotationPresent(JsonDefaultConstructor.class)) {
                continue;
            }
            JsonDefaultConstructor defaultConstructor = constructor.getAnnotation(JsonDefaultConstructor.class);
            Arrays.stream(defaultConstructor.names()).forEach(name -> {
                try {
                    String finalName = name;
                    Field field = Arrays.stream(fields).filter(f -> f.getName().equalsIgnoreCase(finalName)).findFirst().orElse(null);
                    if(field != null && field.canAccess(object) && !field.isAnnotationPresent(JsonIgnore.class)) {
                        Class<?> fieldType = field.getType();
                        switch (fieldType.getName()) {
                            case "int", "java.lang.Integer" -> objectNode.put(name.toLowerCase(), field.getInt(object));
                            case "long", "java.lang.Long" -> objectNode.put(name.toLowerCase(), field.getLong(object));
                            case "float", "java.lang.Float" -> objectNode.put(name.toLowerCase(), field.getFloat(object));
                            case "double", "java.lang.Double" -> objectNode.put(name.toLowerCase(), field.getDouble(object));
                            case "boolean", "java.lang.Boolean" -> objectNode.put(name.toLowerCase(), field.getBoolean(object));
                            case "java.lang.String" -> objectNode.put(name.toLowerCase(), field.get(object).toString());
                            default -> {
                                ObjectNode innerNode = objectNode.putObject(name.toLowerCase());
                                innerNode.put("type", fieldType.getName());
                                innerNode.put("value", field.get(object).toString());
                            }
                        }
                    } else {
                        Method method = Arrays.stream(methods).filter(m -> m.getName().toLowerCase().endsWith(finalName.toLowerCase()) && m.getName().toLowerCase().startsWith("get")).findFirst().orElse(null);
                        if(method != null && method.canAccess(object) && !method.isAnnotationPresent(JsonIgnore.class) && method.getReturnType() != void.class && method.getReturnType() != Void.class && method.getParameterCount() == 0) {
                            Class<?> methodType = method.getReturnType();
                            if(method.isAnnotationPresent(JsonEquivalent.class)) {
                                JsonEquivalent equivalent = method.getAnnotation(JsonEquivalent.class);
                                name = equivalent.value();
                            }
                            switch (methodType.getName()) {
                                case "int", "java.lang.Integer" -> objectNode.put(name.toLowerCase(), (Integer)method.invoke(object));
                                case "long", "java.lang.Long" -> objectNode.put(name.toLowerCase(), (Long)method.invoke(object));
                                case "float", "java.lang.Float" -> objectNode.put(name.toLowerCase(), (Float)method.invoke(object));
                                case "double", "java.lang.Double" -> objectNode.put(name.toLowerCase(), (Double)method.invoke(object));
                                case "boolean", "java.lang.Boolean" -> objectNode.put(name.toLowerCase(), (Boolean)method.invoke(object));
                                case "java.lang.String" -> objectNode.put(name.toLowerCase(), (String)method.invoke(object));
                                default -> {
                                    ObjectNode innerNode = objectNode.putObject(name.toLowerCase());
                                    innerNode.put("type", methodType.getName());
                                    innerNode.put("value", method.invoke(object).toString());
                                }
                            }
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    System.err.println("Error: could not get property " + name + " from " + objectClass.getName() + ": " + e.getMessage());
                }
            });
        }
    }
}
