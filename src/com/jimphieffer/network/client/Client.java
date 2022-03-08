package com.jimphieffer.network.client;

public interface Client {
    void onMessage(String message);
    String getName();
}
