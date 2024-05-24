## Cubo Olap

Estamos trabajando en crear la estructura estructura de datos multidimensional basada en el modelo de Cubo OLAP (On-Line Analytical Processing) para manejo de datasets.


## Pasos que faltarían hacer:

- Modificar las clases de Dimension y Facts de forma más similar a lo subido por el profe para obtener un mejor funcionamiento y mayor claridad
- Realizar las user stories CUBO-002, CUBO-003, y CUBO-004
- Realizar las user stories CUBO-006, CUBO-007, CUBO-008, CUBO-009    (Operaciones Olap)
- Realizar las user stories CUBO-010, CUBO-012, CUBO-013              (Visualización)
- Realizar la user story CUBO-014


## Teoria Operaciones Olap (CUBO-006 - CUBO-009)

- Roll-up: se aplica sobre una dimensión con el objetivo de subir en un nivel de abstracción (ejemplo: en Tiempo podemos hacer un rollup desde días a meses)
- Drill-down: la operación inversa a Rollup, se aplica para ser más específico (ejemplo: en Ubicación podemos hacer un drill-down de provincias a municipios)
- Slice: dada una dimensión y un valor específico, se genera un subcubo con el resto de las dimensiones filtrando los hechos correspondientes a la dimensión y valor de corte (ejemplo: si hacemos un slice sobre Tiempo definiendo el año 2024 como valor, se obtiene un nuevo “cubo” con dimensiones Producto y Ubicación pero sólo con ventas realizadas en 2024)
- Dice: similar a la operación de slice, pero permite seleccionar varias dimensiones de corte y además un conjunto de valores a filtrar en cada una (ejemplo: si hacemos un dice con Tiempo entre 2020 y 2024, y Ubicación de Córdoba o Salta, obtendremos un cubo con los hechos de esos años y esas provincias únicamente)

Preguntas:
> + Roll-up y drill-down deberían ser operaciones solo de Dimension o también de Cubo cuando es dado el nombre de una dimensión?
> + Roll-up y drill-down deberían aceptar un número para cual nivel deberían subir o bajar además de que cuando son llamadas solo bajar o subir uno?
> + Roll-up y drill-down deberían aceptar un nombre de nivel para cual nivel deberían subir o bajar además de que cuando son llamadas solo bajar o subir uno?

Funcionalidad

> dimension.roll-up()
> dimension.roll-up(3)
> dimension.roll-up("meses")
> cube.roll-up("fechas")
> cube.roll-up("fechas", 3)
> cube.roll-up("fechas", "meses")

> dimension.drill-down()
> dimension.drill-down(4)
> dimension.drill-down("dias")
> cube.drill-down("fechas")
> cube.drill-down("fechas", 4)
> cube.drill-down("fechas", "dias")

> cubo.slice("fechas", 2024)
> cubo.slice("punto_venta", Salta)

> cubo.dice("fechas", 2020, 2024)
> cubo.dice("fechas", 2020, "punto_venta", Córdoba)
> cubo.dice("fechas", 2020, 2024, "punto_venta", Salta)
> cubo.dice("fechas", 2020, 2024, "punto_venta", Córdoba, Salta) 

Funcion (en concepto, faltaría tener las otras cosas más completas para probar algo como esto)

public void roll-up() {
	this.columnaKey = columnaKey + 1;
}

public void roll-up(int nivel) {
	this.columnaKey = nivel;
}

public void roll-up(String nivel) {
	for (nombreColumna : columnas)
	this.columnaKey = ;
}


public void drill-down() {
	this.columnaKey = columnaKey - 1;
}

public void drill-down(int nivel) {
	this.columnaKey = nivel;
}

public void drill-down(String nivel) {
	this.columnaKey = ;
}


public Cube slice(String dimension, ) {
	
	return cubo;
}


