package com.example.testapp.shared;

import java.io.Serializable;

// TODO: Investigate switching to enum.
public class Signal implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String message;

    public Signal(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}