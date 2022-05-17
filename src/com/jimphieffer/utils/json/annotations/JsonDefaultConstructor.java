package com.jimphieffer.utils.json.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface JsonDefaultConstructor {
    String[] names() default {};
}
