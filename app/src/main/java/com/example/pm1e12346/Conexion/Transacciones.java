package com.example.pm1e12346.Conexion;


import java.sql.Blob;

public class Transacciones {

    public static final String NameDatabase = "PM01DB";

    public static String tblPaises = "Paises";
    public static final String codigo ="cod";
    public static final String p_pais ="pais";

    public static final String CreateTablePaises = "CREATE TABLE " + tblPaises + "(cod INTEGER PRIMARY KEY,"+"pais TEXT )";
    public static final String DropTablePaises = "DROP TABLE " + tblPaises;

    public static final String tablaContactos = "contactos";

    public static final String id = "id";
    public static final String nombreCompleto = "nombreCompleto";
    public static final String telefono = "telefono";
    public static final String nota = "nota";
    public static final String foto = "foto";
    public static final String pais = "pais";

    public static final String createTableContact = "CREATE TABLE " + tablaContactos +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nombreCompleto TEXT, telefono INTEGER, nota TEXT, foto BLOB, pais TEXT)";

    public static final String dropTableContact = "DROP TABLE IF EXIST" + tablaContactos;
}
