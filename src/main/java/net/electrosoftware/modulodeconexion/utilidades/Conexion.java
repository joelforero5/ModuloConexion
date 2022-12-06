package net.electrosoftware.modulodeconexion.utilidades;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONObject;

public class Conexion {
    String ipBD;
    String portBD;
    String BD;
    String userBD;
    String passBD;
    String ipRTP;
    String portRTP;
    int maxE;
    int maxN;
    String rutasony;
    String rutaConfig = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ENERGIS";

    String energisCon = "";
    JSONObject conConfigObj = new JSONObject();
    JSONArray configuracion = new JSONArray();
    Context context;

    public Conexion(String ipBD, String portBD, String BD, String userBD, String passBD, String ipRTP, String portRTP, Context context) {
        this.ipBD = ipBD;
        this.portBD = portBD;
        this.BD = BD;
        this.userBD = userBD;
        this.passBD = passBD;
        this.ipRTP = ipRTP;
        this.portRTP = portRTP;
        this.context = context;
    }

    public Conexion() {
    }

    public String getIpBD() {
        return ipBD;
    }

    public void setIpBD(String ipBD) {
        this.ipBD = ipBD;
    }

    public String getPortBD() {
        return portBD;
    }

    public void setPortBD(String portBD) {
        this.portBD = portBD;
    }

    public String getBD() {
        return BD;
    }

    public void setBD(String BD) {
        this.BD = BD;
    }

    public String getUserBD() {
        return userBD;
    }

    public void setUserBD(String userBD) {
        this.userBD = userBD;
    }

    public String getPassBD() {
        return passBD;
    }

    public void setPassBD(String passBD) {
        this.passBD = passBD;
    }

    public String getIpRTP() {
        return ipRTP;
    }

    public void setIpRTP(String ipRTP) {
        this.ipRTP = ipRTP;
    }

    public String getPortRTP() {
        return portRTP;
    }

    public void setPortRTP(String portRTP) {
        this.portRTP = portRTP;
    }

    public int getMaxE() {
        return maxE;
    }

    public void setMaxE(int maxE) {
        this.maxE = maxE;
    }

    public int getMaxN() {
        return maxN;
    }

    public void setMaxN(int maxN) {
        this.maxN = maxN;
    }

    public String getRutasony() {
        return rutasony;
    }

    public void setRutasony(String rutasony) {
        this.rutasony = rutasony;
    }

    public String getRutaConfig() {
        return rutaConfig;
    }

    public void setRutaConfig(String rutaConfig) {
        this.rutaConfig = rutaConfig;
    }

    public String getEnergisCon() {
        return energisCon;
    }

    public void setEnergisCon(String energisCon) {
        this.energisCon = energisCon;
    }

    public JSONObject getConConfigObj() {
        return conConfigObj;
    }

    public void setConConfigObj(JSONObject conConfigObj) {
        this.conConfigObj = conConfigObj;
    }

    public JSONArray getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(JSONArray configuracion) {
        this.configuracion = configuracion;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
