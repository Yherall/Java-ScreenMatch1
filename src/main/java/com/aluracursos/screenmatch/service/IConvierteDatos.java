/*
SE REALIZA UNA INTERFAZ QUE POR NOMBRE SIEMPRE LLEVARA UNA i AL PRINCIPIO DEL NOMBRE PARA DETERMINAR QUE ES UNA INTERFAZ

 */
package com.aluracursos.screenmatch.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
    // Se realiza un metodo generico "obtenerDatos" que recibira un JSON
}
