package uk.ac.cam.cl.alpha.workout.shared;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public enum Serializer {
    ;

    public static byte[] serialize(Object objectToBytes) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutput objectOutput = new ObjectOutputStream(outputStream)) {
            objectOutput.writeObject(objectToBytes);
            return outputStream.toByteArray();
        }
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
             ObjectInput objectInput = new ObjectInputStream(inputStream)) {
            return objectInput.readObject();
        }
    }
}