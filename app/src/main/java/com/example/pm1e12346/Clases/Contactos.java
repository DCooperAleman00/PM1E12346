package com.example.pm1e12346.Clases;

import java.sql.Blob;

public class Contactos {
    private int id;
    private String nombre;
    private int telefono;
    private int codPais;
    private String nota;
    private Blob foto;

    public Blob getImage() {
        return foto;
    }

    public Contactos() {
    }

    public Contactos(int id, String nombre, int telefono, int codPais, String nota, Blob foto) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.codPais = codPais;
        this.nota = nota;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public int getCodPais() {
        return codPais;
    }

    public void setCodPais(int codPais) {
        this.codPais = codPais;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public Blob getFoto() {
        return foto;
    }

    public void setFoto(Blob foto) {
        this.foto = foto;
    }
}
