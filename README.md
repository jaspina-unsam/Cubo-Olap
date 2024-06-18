# Cubo OLAP - Trabajo Práctico Integrador

En este repositorio se encuentra la solución del Grupo 11 para la resolución del Cubo OLAP en JAVA 8 sin librerías externas.

## Notas de uso
Se debe construir un Cubo OLAP con sus respectivas dimensiones y hechos. Para esto, utilizar la clase `CubeBuilder`. `CubeBuilder` no necesita parametros de instanciación, pero se apoya en un `DataParser` para la lectura de datos.

> Nota: por el momento, sólo se implementó `CsvParser`

En primer lugar, `CubeBuilder` precisa que se le agreguen dimensiones, una por vez, y una vez leidas las dimensiones, se pueden leer los hechos.

Para la carga de dimensiones, utilizar `CubeBuilder.addDimension()`. Para la carga de hechos, `CubeBuilder.addFacts()`.

Una vez leidos los datos de memoria, se debe crear la instancia del cubo llamando a `CubeBuilder.buildCube()`.

Ya con el cubo creado, se puede realizar toda consulta que se necesite.

## Notas técnicas
Hay una serie de validaciones que restan hacer (bastantes), pero la funcionalidad básica, y con un buen uso, la aplicación es capaz de informar correctamente sobre el negocio leido en el dataset.