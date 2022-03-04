package com.jimphieffer.Network.Client;

public interface Client {
    void onMessage(String message);
    String getName();
}
