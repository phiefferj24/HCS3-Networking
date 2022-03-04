package com.jimphieffer;

public class Message {
    public enum MessageProtocol {
        SEND,
        RELAY
    }

    /**
     * Formats:
     * Connect: SEND/CONNECT:[username]
     * Message: (SEND, RELAY)/MESSAGE:[message]
     */
    public enum MessageType {
        CONNECT,
        MESSAGE,
        DISCONNECT
    }
    public static String encode(String message, MessageProtocol protocol, MessageType type) {
        return protocol.name() + "/" + type.name() + ":" + message;
    }
    public static MessageProtocol getProtocol(String message) {
        for(MessageProtocol m : MessageProtocol.values()) {
            if(message.split("/")[0].equals(m.name())) return m;
        }
        return null;
    }
    public static MessageType getType(String message) {
        for(MessageType m : MessageType.values()) {
            if(message.split("/")[1].split(":")[0].equals(m.name())) return m;
        }
        return null;
    }
    public static String decode(String message) {
        return message.substring(message.indexOf(':') + 1);
    }
}
