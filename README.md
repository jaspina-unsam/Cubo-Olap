# Cubo OLAP - Trabajo Práctico Integrador

En este repositorio se encuentra la solución del Grupo 11 para la resolución del Cubo OLAP en JAVA 8 sin librerías externas. Para referencias de uso, usar `App.java` o el presente documento, o la documentación de diseño.

## Notas de uso
Se debe construir un Cubo OLAP con sus respectivas dimensiones y hechos. Para esto, utilizar la clase `CubeBuilder`. `CubeBuilder` no necesita parametros de instanciación, pero se apoya en un `DataParser` para la lectura de datos.

> Nota: por el momento, sólo se implementó `CsvParser`

En primer lugar, `CubeBuilder` precisa que se le agreguen dimensiones, una por vez, y una vez leidas las dimensiones, se pueden leer los hechos.

Para la carga de dimensiones, utilizar `CubeBuilder.addDimension()`. Para la carga de hechos, `CubeBuilder.addFacts()`.

Una vez leidos los datos de memoria, se debe crear la instancia del cubo llamando a `CubeBuilder.buildCube()`.

Ya con el cubo creado, se puede realizar toda consulta que se necesite. Para ello, se debe usar `TablePrinter.display()` que es un método sobrecargado, en combinación con las operaciones hechas en el cubo anteriormente (`drillDown()`, `rollUp()`, `dice()`, `slice()`), además de la selección de hecho y medida (`selectFact()`, `selectMeasure()`).

## Valores por defecto

- En caso de no seleccionar un hecho en particular, la primera columna que no es FK en el archivo de hechos será el hecho por defecto. Con los archivos actuales en el repo, `cantidad` es el hecho default.
- Si no se selecciona una medida, la que se toma por defecto es `contar`.

## Ejemplos de uso

### Imprimir información del estado del cubo
```java
Cube cube = builder.buildCube();
System.out.println(cube.toString());
```

`Cube [57851 cells. Dimensions: [puntos_venta, fechas, productos]. Facts: [cantidad, valor_unitario, valor_total, costo]. Measures: [contar, suma, min, max]]`

> Se recomienda usar antes de cualquier consulta para conocer las dimensiones, hechos y medidas disponibles, además de validar que se encuentren todos los datos.
> 
> **IMPORTANTE**: la presente implementación no hace validación de datos. Se confía en el buen uso de la aplicación.

### Reducción total de todas las dimensiones
Para esto se debe invocar al `display()` con una instancia de un cubo.

```java
TablePrinter.display(cube);

// Salida por pantalla
+-------------------+
| cantidad (contar) |
+-------------------+
| 57851.00          |
+-------------------+
```

### Operar al cubo y `display()` para una sola dimensión
En el siguiente ejemplo se toman sólamente los hechos del año 2018, se cambia el hecho y medidad por defecto y se muestra por pantalla:

```java
// Ejemplo para responder la pregunta de negocio ¿Cuál fue el costo total de los productos vendidos durante 2018?

cube = cube.slice("fechas", "2018");    // Mostrar solo 2018
cube.selectFact("costo");               // Cambio el hecho a "costo"
cube.selectMeasure("suma");             // Cambio la medida a "suma"
TablePrinter.display(cube, "fechas");   // Muestro solo la dimensión fechas

// Salida por pantalla
+---------------+--------------+
| anio (fechas) | costo (suma) |
+---------------+--------------+
| 2018          | 23821032.82  |
+---------------+--------------+
```

En este ejemplo, se elije mostrar la dimensión fechas, pero podría haber sido cualquier otra.

### Operar al cubo y `display()` para dos dimensiones
```java
// Ejemplo para responder la pregunta de negocio ¿Cuáles fueron los tickets más bajos en California, Alabama y Arizona (EE.UU.)?

cube.drillDown("puntos_venta"); // Bajo de región a país
cube.drillDown("puntos_venta"); // Bajo de país a provincia
Cube dicedCube = cube.dice("puntos_venta", new String[] { "California", "Alabama", "Arizona" });
TablePrinter.display(dicedCube, "puntos_venta", "fechas");

// Salida por pantalla
provincia (puntos_venta) vs anio (fechas) [valor_total (min)]
+--------------------------+-------+--------+-------+-------+
| provincia (puntos_venta) | 2017  | 2018   | 2019  | 2020  |
+--------------------------+-------+--------+-------+-------+
| Alabama                  | 0.00  | 183.94 | 24.28 | 24.29 |
+--------------------------+-------+--------+-------+-------+
| Arizona                  | 11.40 | 10.38  | 2.74  | 6.85  |
+--------------------------+-------+--------+-------+-------+
| California               | 5.19  | 5.19   | 2.74  | 2.99  |
+--------------------------+-------+--------+-------+-------+
```

Sólo cuando se trata de tablas de dimensión vs dimensión se muestra una linea extra con información de la consulta.

## Notas técnicas
Lista de TO-DOs:

- Cuando se hace `drillDown()`, se debe combinar el nivel activo con sus niveles superiores para responder acertadamente la pregunta de negocio.
    - Workaround: Mostrar una tabla por cada nivel superior deseado. Puede llegar a ser muy engorroso, pero es posible.
- Validación de datos de entrada.
    - Workaround: Se confía en el buen uso de la aplicación.
- Cubo interactivo: posibilidad de interactuar con el cubo por CLI.
    - Workaround: Usar `App.java`
