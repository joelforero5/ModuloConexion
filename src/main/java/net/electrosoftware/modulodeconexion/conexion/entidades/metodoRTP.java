package net.electrosoftware.modulodeconexion.conexion.entidades;

public class metodoRTP {
    String nombreMetodo, nombreTabla;
    String tituloDatos = "";

    public metodoRTP(String nombreMetodo, String nombreTabla) {
        this.nombreMetodo = nombreMetodo;
        this.nombreTabla = nombreTabla;
    }

    public metodoRTP(String nombreMetodo, String nombreTabla, String tituloDatos) {
        this.nombreMetodo = nombreMetodo;
        this.nombreTabla = nombreTabla;
        this.tituloDatos = tituloDatos;
    }

    public String getTituloDatos() {
        return tituloDatos;
    }

    public void setTituloDatos(String tituloDatos) {
        this.tituloDatos = tituloDatos;
    }

    public String getNombreMetodo() {
        return nombreMetodo;
    }

    public void setNombreMetodo(String nombreMetodo) {
        this.nombreMetodo = nombreMetodo;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }
}