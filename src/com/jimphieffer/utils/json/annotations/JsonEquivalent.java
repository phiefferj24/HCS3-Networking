package com.jimphieffer.utils.json.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface JsonEquivalent {
    String name() default "";
}