package com.aluracursos.screenmatch.principal;

import java.util.Arrays;
import java.util.List;

public class EjemploStreams {
    public void muestraEjemplo(){
        List<String> nombres = Arrays.asList("Brenda","Luis","Maria Fernanda","Eric","Genesys");
        //STREAM = se realiza un flujo de datos de la lista nombres
        //SORTED = ordenará la lista alfabeticamente
        //LIMIT = limita la cantidad de nombres que apareceran impresos
        //FILTER = filtrara la lista, para que aparezca el primer nombre con la letra inicial Indicada
        //MAP = el nombre filtrado transformara sus letras en Mayuscula
        nombres.stream().sorted().limit(4).filter(n -> n.startsWith("G")).map(n -> n.toUpperCase()).forEach(System.out::println);
    }
}
