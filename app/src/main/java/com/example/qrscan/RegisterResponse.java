package com.example.qrscan;

import java.util.HashMap;
import java.util.Map;

public class RegisterResponse {
    private String success;
    private final Map<String, Object> additionalProperties = new HashMap<>();

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
