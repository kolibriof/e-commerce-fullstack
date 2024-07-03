package com.ecommerce.ecommerce_be.builders;

import com.ecommerce.ecommerce_be.responses.UsersResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseBuilder {

    public ResponseBuilder() {
    }

    static public String buildResponse(UsersResponse usersResponse)  {
        String response;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            response = objectMapper.writeValueAsString(usersResponse);
        } catch (JsonProcessingException jsonProcessingException) {
            System.err.println("JSON MAPPING ERROR DETAILS: " + jsonProcessingException.getMessage());
            response = "Mapping error.";
        }

        return response;
    }
}
