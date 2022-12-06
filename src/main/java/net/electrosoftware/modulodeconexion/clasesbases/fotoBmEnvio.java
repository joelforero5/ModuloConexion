package net.electrosoftware.modulodeconexion.clasesbases;

public class fotoBmEnvio {
    String nombreFoto;
    int tamanoFoto;
    byte[] bmFoto;

    public fotoBmEnvio(String nombreFoto, int tamanoFoto, byte[] bmFoto) {
        this.nombreFoto = nombreFoto;
        this.tamanoFoto = tamanoFoto;
        this.bmFoto = bmFoto;
    }

    public String getNombreFoto() {
        return nombreFoto;
    }

    public void setNombreFoto(String nombreFoto) {
        this.nombreFoto = nombreFoto;
    }

    public int getTamanoFoto() {
        return tamanoFoto;
    }

    public void setTamanoFoto(int tamanoFoto) {
        this.tamanoFoto = tamanoFoto;
    }

    public byte[] getBmFoto() {
        return bmFoto;
    }

    public void setBmFoto(byte[] bmFoto) {
        this.bmFoto = bmFoto;
    }

}
