package com.ecommerce.ecommerce_be.builders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseBuilder {

    public ResponseBuilder() {
    }

    static public String buildResponse(Object object)  {
        String response;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            response = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException jsonProcessingException) {
            System.err.println("JSON MAPPING ERROR DETAILS: " + jsonProcessingException.getMessage());
            response = "Mapping error.";
        }

        return response;
    }
}
