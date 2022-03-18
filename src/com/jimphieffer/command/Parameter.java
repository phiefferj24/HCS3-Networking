package com.jimphieffer.command;

public class Parameter<Type> {
    private Type value;
    public Parameter(Type value) {
        this.value = value;
    }
    public Type getValue() {
        return value;
    }
}
