package com.jimphieffer.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

public class Json {
    public static ArrayList<Class<?>> allowedTypes = new ArrayList<>(Arrays.asList(new Class<?>[] {
            String.class,
            int.class, Integer.class,
            double.class, Double.class,
            float.class, Float.class,
            long.class, Long.class,
            boolean.class, Boolean.class,
            char.class, Character.class,
    }));

    public interface AssignmentMethod<T> {
        T from(String value);
    }
}
