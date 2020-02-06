package com.example.testapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;

enum Serializer {
    ;

    static byte[] serialize(Object objToWrite) {
        byte[] output;

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(objToWrite);
            out.flush();
            output = bos.toByteArray();
        } catch (IOException e) {
            // Can always write to local byte array.
            throw new UncheckedIOException(e);
        }

        return output;
    }
}
