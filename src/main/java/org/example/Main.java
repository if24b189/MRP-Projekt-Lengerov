package org.example;

import org.example.server.Server;

public class Main {
    public static void main(String[] args) {
        try {
            new Server().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
