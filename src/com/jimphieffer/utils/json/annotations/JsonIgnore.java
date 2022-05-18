package com.jimphieffer.utils.json.annotations;

import java.lang.annotation.*;

/**
 * Annotation to indicate that a method should be ignored when serializing or deserializing.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface JsonIgnore {
}
