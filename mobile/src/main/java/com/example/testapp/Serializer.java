package com.example.testapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Serializer {

    public Serializer() {

    }

    public static byte[] serialize(Object objToWrite) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(objToWrite);
            out.flush();
            byte[] yourBytes = bos.toByteArray();
        } catch (IOException e) {
            // Can always write to local byte array.
            e.printStackTrace();
        }
        // ignore close exception
        return new byte[0];
    }
}
