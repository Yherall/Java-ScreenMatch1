package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.DatosEpisodio;
import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporadas;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class EjemploAnteriorPrincipal {
    private Scanner teclado = new Scanner(System.in);//Se declara un atributo privada
    private ConsumoAPI consumoApi = new ConsumoAPI();//Creamos una nueva instancia de ConsumoAPI
    private final String URL_BASE = "https://www.omdbapi.com/?t=";//Se agrega una variable de tipo Constante, al escribir "FINAL" se define de tal manera
    private final String API_KEY = "&apikey=d2673cc4";//Se agrega una variable de tipo Constante, al escribir "FINAL" se define de tal manera
    private ConvierteDatos conversor = new ConvierteDatos();

    public void muestraMenu() {
        System.out.println("Ingrese el nombre de la serie que desea buscar"); //se enviá un mensaje al usuario
        var nombreSerie = teclado.nextLine();//Se crea una variable para guardar lo escrito por el usuario en teclado
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);//Se usa REPLACE para reemplazar el espacio entre lo escrito para agregar un caracter "+"
        var datos = conversor.obtenerDatos(json, DatosSerie.class);

        //BUSCA LOS DATOS DE TODAS LAS TEMPORADAS
        List<DatosTemporadas> temporadas = new ArrayList<>();// Se Realiza una lista con todas las temporadas que contiene la serie
        //Busca los datos generales de las Series
        for (int i = 1; i <= datos.totalTemporadas(); i++) { //Se Realiza un FOR para que vaya contando hasta la cantidad maxima de temporadas determinada por la serie
            json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&Season=" + i + API_KEY);//SE COLOCA "i" PARA QUE CAMBIE DE TEMPORADA DEPENDIENDO DEL LOOP QUE RECORRA EL FOR
            var datosTemporadas = conversor.obtenerDatos(json, DatosTemporadas.class);//Se crea una variable que guardara el JSON que estara convertido en DATOS TEMPORADA
            temporadas.add(datosTemporadas);// Se agregarán los datos de cada una de las temporadas en la lista creada llamada "TEMPORADAS"

            //SE REGISTRA UN HISTORIAL DE BÚSQUEDA DE SERIES CON SUS TEMPORADAS EN UN ARCHIVO DE TEXTO
            try {
                FileWriter archivoTexto = new FileWriter("Series_Temporadas.txt", true); // true para agregar al final del archivo
                archivoTexto.write("Temporada " + datosTemporadas.numero() + ":\n");
                for (DatosEpisodio episodio : datosTemporadas.episodios()) {
                    archivoTexto.write("- Episodio " + episodio.numeroEpisodio() + " - " + episodio.titulo() + "\n");
                }
                archivoTexto.write("\n");
                archivoTexto.close();
            } catch (IOException e) {
                System.out.println("Ocurrió un error al escribir en el archivo: " + e.getMessage());
            }

        }
//        temporadas.forEach(System.out::println);//MANDAREMOS A IMPRIMIR LO QUE TENDREMOS DENTRO DE LA LISTA
        System.out.println(datos);

        //MOSTRAR SOLO EL TÍTULO DE LOS EPISODIOS PARA LAS TEMPORADAS
//        for (int i = 0; i < datos.totalTemporadas(); i++) {
//            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        //Se realiza la siguiente expresion lambda
        //Se realiza un loop o ciclo a la lista "TEMPORADAS" y luego tomara de la lista, los EPISODIOS que contenga
        //De la lista de episodios tambien se realizara un loop y mandaremos a imprimir el TÍTULO de los EPISODIOS
        //temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //CONVERTIR TODAS LAS INFROMACIONES A UNA LISTA DEL TIPO DatosEpisodios
        //STREAM = realiza un flujo de datos de la lista temporadas
        //FLATMAP = Transforma la lista temporadas que indica sus episodios en una lista solo de episodios
        //COLLECT = se encargara de realizar una nueva lista derivada de la transformacion de la lista temporadas a episodios que se guardara en datosEpisodios
        List<DatosEpisodio> datosEpisodios = temporadas.stream().flatMap(t -> t.episodios().stream()).collect(Collectors.toList());

        //TOP 5 DE MEJORES EPISODIOS

//        System.out.println("TOP 5 EPISODIOS");

        //STREAM = realiza un flujo de la lista datosEpisodios
        //FILTER = se filtrará por la evaluacion basándonos en el equals ignore case
        //EQUALS IGNORE CASE = excluira todas las evaluaciones que contengan "N/A"
        //SORTED = ordena la lista haciendo una comparacion con las evaluaciones de los episodios
        //COMPARATOR = compara la lista episodios con base en la evaluacion
        //REVERSED = ordenará los datos de mayor a menor basándonos en el sorted
        //LIMIT = limita la cantidad de episodios que se mostraran
        //FOREACH = realiza un loop para mostrar el top 5 de los episodios

//        datosEpisodios.stream().filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primer Filtro (N/A)" + e)) //NO PERMITE VERIFICAR PASO A PASO CADA UNA DE LAS OPERACIONES
//                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
//                .peek(e -> System.out.println("Segundo Filtro Ordenacion (M>m)" + e))//NO PERMITE VERIFICAR PASO A PASO CADA UNA DE LAS OPERACIONES
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("Tercer Filtro Mayuscula (m>M)" + e))//NO PERMITE VERIFICAR PASO A PASO CADA UNA DE LAS OPERACIONES
//                .limit(5)
//                .forEach(System.out::println);

        //CONVIRTIENDO LOS DATOS A UNA LISTA DE TIPO EPISODIOS
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream().map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        //episodios.forEach(System.out::println);

        //BUSQUEDA DE EPISODIOS A PARTIR DE X AÑO
//        System.out.println("Indique desde que año desea ver los episodios: ");
//        var fecha = teclado.nextInt();//Se guarda en fecha el entero que escribira el usuario
//        teclado.nextLine();
//        LocalDate fechaBusqueda = LocalDate.of(fecha,1,1);//Varaible de fehcaBusqueda que solo modificara el año
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");//Formato de fecha que yo deseo

//        episodios.stream()
//                .filter(e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                ", Episodio: " + e.getTitulo() +
//                                ", Fecha de Lanzamiento: " + e.getFechaDeLanzamiento().format(dtf)));

        //BUSCA EPISODIOS POR PARTE DEL TITULO
//        System.out.println("Por favor escriba el titulo del episodio que desea ver: ");
//        var pedazoTitulo = teclado.nextLine();//Se realiza una variable nueva del teclado para guardar lo escrito en ella
//        //Se escribe OPTIONAL más una variable local para asignar la busqueda findFirst ya que buscara pero no mostrara nada
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))//Dentro del filtro convertimos los datos en MAYUSCULA para que ambos parametros coincidan
//                .findFirst();
//        if (episodioBuscado.isPresent()){ //Se realiza un if para ver si esta presente algo dentro del episodio buscado
//            System.out.println("Episodio Encontrado");// imprime en pantalla el mensaje
//            System.out.println("Los Datos Son: " + episodioBuscado.get());// imprime en pantalla los datos del episodio
//        }else {
//            System.out.println("Episodio No Encontrado"); //Si no encuentra el episodio deja el siguiente mensaje
//        }

        //MAP<INTEGER, DOUBLE> se usa para transformar una lista de episodios en un map de entero y double, por lo que se consigue el número de temporadas y evaluacion
        //FILTER se filtra la lista para episodios que tengan una evaluacion mayor a 0.0
        //COLLECTORS.GROUPINBY para este caso agrupa los episodios por temporada
        //COLLECTOR.AVERAGINGDOUBLE para que luego se calcule el promedio de las evaluaciones de los episodios por temporada. (QUEDA DENTRO DE COLLECTORS.GROUPINBY)
        Map<Integer, Double> evaluacionesPorTemporada = episodios.stream()
                .filter(episodio -> episodio.getEvaluacion() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println(evaluacionesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(episodio -> episodio.getEvaluacion() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println("Media De Evaluaciones: " + est.getAverage());
        System.out.println("Episodio Mejor Evaluado: " + est.getMax());
        System.out.println("Episodio Peor Evaluado: " + est.getMin());
    }
}
