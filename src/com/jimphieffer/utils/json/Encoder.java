package com.jimphieffer.utils.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jimphieffer.utils.json.annotations.JsonIgnore;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Encoder {
    public ArrayNode node = new ArrayNode(new JsonNodeFactory(false));

    public void addObject(Object object, String... ignoredPropertyNames) {
        addObject(object, Object.class, ignoredPropertyNames);
    }

    public void clearObjects() {
        node = node.removeAll();
    }

    public void addObject(Object object, Class<?> superclass, String... ignoredPropertyNames) {
        List<String> ignoredProperties = new ArrayList<>(Arrays.asList(ignoredPropertyNames));
        ignoredProperties = ignoredProperties.stream().map(String::toLowerCase).collect(Collectors.toList());
        ObjectNode parentNode = node.addObject();
        parentNode.put("type", object.getClass().getName());
        ObjectNode objectNode = parentNode.putObject("object");
        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get") && !method.isAnnotationPresent(JsonIgnore.class)) {
                String propertyName = method.getName().substring(3).toLowerCase();
                if (!ignoredProperties.contains(propertyName)
                        && method.getParameterCount() == 0
                        && superclass.isAssignableFrom(method.getDeclaringClass())) {
                    boolean hasSetter = false;
                    for (Method method2 : methods) {
                        if (method2.getName().startsWith("set") && method2.getName().substring(3).equalsIgnoreCase(propertyName)) {
                            hasSetter = true;
                            break;
                        }
                    }
                    if (!hasSetter) {
                        continue;
                    }
                    try {
                        Object returned = method.invoke(object);
                        if (returned != null) {
                            if (Json.allowedTypes.contains(method.getReturnType())) {
                                if (returned instanceof String) {
                                    objectNode.put(propertyName, (String) returned);
                                } else if (returned instanceof Integer) {
                                    objectNode.put(propertyName, (Integer) returned);
                                } else if (returned instanceof Long) {
                                    objectNode.put(propertyName, (Long) returned);
                                } else if (returned instanceof Double) {
                                    objectNode.put(propertyName, (Double) returned);
                                } else if (returned instanceof Float) {
                                    objectNode.put(propertyName, (Float) returned);
                                } else if (returned instanceof Boolean) {
                                    objectNode.put(propertyName, (Boolean) returned);
                                } else if (returned instanceof Character) {
                                    objectNode.put(propertyName, (Character) returned);
                                }
                            } else {
                                ObjectNode subNode = objectNode.putObject(propertyName);
                                subNode.put("type", returned.getClass().getName());
                                subNode.put("value", returned.toString());
                            }
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        System.err.println("Error: Cannot get class property " + propertyName + ": " + e);
                    }
                }
            }
        }
    }

    public String encode() {
        return node.toString();
    }
}
