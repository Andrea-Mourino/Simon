
# Room explicado 

El **Room** otorga una **capa de abstracción** sobre SQLite.

¿Qué quiere decir esto?
Que Room **usa SQLite por debajo**, pero **nos evita escribir todo a mano**.
Nos ahorra un mucho código y tiempo y es más seguro

---

## Cómo implementar Room

Muy fácil, lo más importante son **3 clases nuevas** que vas a tener que crear:

1. **Entidades**

    * Son básicamente las **tablas** que van a estar en la base de datos.
    * Cada entidad representa una tabla con sus columnas.

2. **DAO** (Data Access Object)

    * Es una **interfaz** donde definimos el **CRUD** y otras operaciones que queramos hacer sobre la base de datos.
    * Aquí ponemos cosas como `insert`, `delete`, `update` y consultas personalizadas.

3. **AppDatabase**

    * Es una **clase abstracta** que **expone los DAOs** a las entidades.
    * Básicamente conecta todo para que podamos usar nuestra base de datos desde la app.

---

## Paso final

Una vez que tenemos las 3 clases:

* Instanciamos la **base de datos** (AppDatabase)
* Obtenemos el **DAO**
* Ahora podemos usar los métodos del DAO para guardar, leer o borrar datos de nuestra app.

