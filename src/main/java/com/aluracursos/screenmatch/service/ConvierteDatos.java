package com.aluracursos.screenmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IConvierteDatos {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            return objectMapper.readValue(json, clase);
            // READ VALUE nos dice que pueden ocurrir excepciones por lo que agregamos un TRY CATCH
            // Retorna un objeto del tipo objectmapper, leera el valor y transformara el JSON en la "clase"
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
