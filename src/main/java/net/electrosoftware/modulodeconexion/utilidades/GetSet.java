package net.electrosoftware.modulodeconexion.utilidades;

import net.electrosoftware.modulodeconexion.conexion.rtp.eventos.ConexionService;

public class GetSet {
    private static ConexionService conexionService;
    private static Conexion conConfig = new Conexion();
    private static String cedencialesErroneas = null;
    private static String imei;
    private static String usuario;


    public static ConexionService getConexionService() {
        return conexionService;
    }

    public static void setConexionService(ConexionService conexionService) {
        GetSet.conexionService = conexionService;
    }

    public static Conexion getConConfig() {
        return conConfig;
    }

    public static void setConConfig(Conexion conConfig) {
        GetSet.conConfig = conConfig;
    }

    public static String getCedencialesErroneas() {
        return cedencialesErroneas;
    }

    public static void setCedencialesErroneas(String cedencialesErroneas) {
        GetSet.cedencialesErroneas = cedencialesErroneas;
    }

    public static String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public static String getUsuario() {
        return usuario;
    }

    public static void setUsuario(String usuario) {
        GetSet.usuario = usuario;
    }
}
