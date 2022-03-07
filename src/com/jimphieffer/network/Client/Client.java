package com.jimphieffer.network.Client;

public interface Client {
    void onMessage(String message);
    String getName();
}
