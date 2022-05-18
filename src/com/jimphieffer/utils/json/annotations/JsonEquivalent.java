package com.jimphieffer.utils.json.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonEquivalent {
    String value() default "";
}