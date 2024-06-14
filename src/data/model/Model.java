package data.model;

import java.util.List;

import api.DataType;

public final class Model {
    public static final List<DataType> FECHA_DATA_TYPES = List.of(
        DataType.INTEGER,
        DataType.DATE,
        DataType.INTEGER,
        DataType.INTEGER,
        DataType.INTEGER,
        DataType.INTEGER
    );

    public static final List<DataType> PRODUCTO_DATA_TYPES = List.of(
        DataType.INTEGER,
        DataType.STRING,
        DataType.STRING,
        DataType.STRING
    );

    public static final List<DataType> PUNTO_VENTA_DATA_TYPES = List.of(
        DataType.INTEGER,
        DataType.STRING,
        DataType.STRING,
        DataType.STRING,
        DataType.STRING,
        DataType.STRING
    );

    public static final List<DataType> VENTAS_DATA_TYPES = List.of(
        DataType.INTEGER,
        DataType.INTEGER,
        DataType.INTEGER,
        DataType.INTEGER,
        DataType.FLOAT,
        DataType.FLOAT,
        DataType.FLOAT
    );
}
