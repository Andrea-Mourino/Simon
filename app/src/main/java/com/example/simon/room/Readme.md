# Documentación Técnica: Implementación de Persistencia con Room

Este módulo del proyecto Simon gestiona la persistencia de datos de manera local utilizando la librería Room Persistence Library, proporcionando una capa de abstracción sobre SQLite para un manejo de datos más robusto y eficiente.

## Arquitectura de la Solución
La implementación sigue el patrón de diseño recomendado por Android, dividiendo la responsabilidad en cuatro componentes principales:

1. Data Entity (RecordEntity):
   Representa la estructura de la tabla en la base de datos.

   - Campos:

     - id: Clave primaria autoincremental.

     - record: Valor entero que almacena la ronda máxima alcanzada.

     - fecha: Almacenada como un tipo Long (Unix Timestamp) para garantizar la compatibilidad con SQLite.

2. Data Access Object (RecordDao): 
   Define la interfaz de comunicación con la base de datos mediante anotaciones SQL.

    - getRecord(): Recupera el último registro insertado utilizando una consulta ordenada por ID de forma descendente.

    - insert(): Inserta un nuevo registro en la tabla de récords.

3. Database Controller (AppDatabase):
   Funciona como el punto de acceso principal para la conexión. Hereda de RoomDatabase y es el encargado de proveer las instancias de los DAOs.

4. Logic Controller (RoomController):
   Actúa como un adaptador entre la lógica de la aplicación y la base de datos. Implementa la interfaz InterfazConexion para desacoplar la fuente de datos de la lógica de negocio.

## Detalles de Implementación
Gestión de Tipos de Datos:

Dado que SQLite no soporta nativamente el tipo LocalDate de Java/Kotlin, se ha implementado una lógica de conversión manual en el RoomController:

- Escritura: Se convierte LocalDate a Long mediante toEpochSecond().

- Lectura: Se reconstruye el objeto LocalDate usando Instant y ZoneId.


## Cómo funciona el flujo de datos
1. Inicio: Al iniciar el ViewModel, se solicita el récord actual a través de obtenerRecord().

2. Validación: Si no existen datos previos, Room devuelve un valor nulo que el controlador gestiona retornando un objeto Record inicial (0).

3. Actualización: Cuando el jugador supera su marca, se invoca actualizarRecord(), realizando una inserción atómica en la base de datos.