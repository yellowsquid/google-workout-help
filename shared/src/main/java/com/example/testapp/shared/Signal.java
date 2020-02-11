package com.example.testapp.shared;

import java.io.Serializable;

public enum Signal implements Serializable {
    START,
    STOP,
    PAUSE,
    RESUME;
    private static final long serialVersionUID = 2L;
}