package com.example.pm01app1.configuracion;

public class Transacciones {
    // Nombre de la base de datos
    public static final String NameDB = "PM01DB";
    // Tabla de clientes
    public static final String tabla_clientes = "Clientes";

    // Campos de la tabla clientes
    public static final String id = "id";
    public static final String nombres = "nombres";
    public static final String apellidos = "apellidos";
    public static final String correo = "correo";

    // DDL
    public static final String CreateTableClient =
            "CREATE TABLE Clientes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombres TEXT, " +
                    "apellidos TEXT, " +
                    "correo TEXT)";

    public static  final String DropTableClient = "DROP TABLE IF EXISTS Clientes";

    // DML
    public static final String SelectTableClient = "SELECT * FROM " + tabla_clientes;
}

