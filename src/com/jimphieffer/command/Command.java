package com.jimphieffer.command;

public class Command {
    private static final String[] validCommands = {
            // Commands: parameters surrounded with <name:type>, parameters of infinite length surrounded with [name:type]
            // specify name inside brackets
            // types can be number, string, name
            // mark optional with a ? at the end
            "/tp <x:number> <y:number> <deg:number?>",
            "/tp <name:name>",
            "/msg <name:name> [message:string]",
            "/quit",
            "/spawn <entity:string> <x:number?> <y:number?>"
    };
    private Parameter[] parameters;
    public static Command getCommand(String commandString) {
        return null;
    }
    public Command(String command, Parameter... parameters) {
        this.parameters = parameters;
    }
}
