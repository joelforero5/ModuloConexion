package net.electrosoftware.modulodeconexion.conexion.rtp.eventos;

import android.util.Log;

/*import net.electrosoftware.energismovil.clasesbases.Conexion;
import net.electrosoftware.energismovil.clasesbases.GetSet;
import net.electrosoftware.energismovil.clasesbases.SessionManager;
import net.electrosoftware.energismovil.clasesbases.fotoBmEnvio;*/

import net.electrosoftware.modulodeconexion.clasesbases.fotoBmEnvio;
import net.electrosoftware.modulodeconexion.utilidades.Conexion;
import net.electrosoftware.modulodeconexion.utilidades.GetSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class RtpRequest implements Runnable {

    private static final int BUFFER_SIZE = 1024;

    JSONObject jsonRespuesta;
    String Rta = "OK";
    JSONArray parametros;
    String metodo;
    String tipoMetodo;
    fotoBmEnvio foto;
    int INTENTOS = 3;
    boolean reintento = false;
    JSONArray jsonCantidades = new JSONArray();


    public RtpRequest(String metodo, String tipoMetodo, fotoBmEnvio foto, JSONArray parametros) {
        this.metodo = metodo;
        this.foto = foto;
        this.parametros = parametros;
        this.tipoMetodo = tipoMetodo;
    }

    public String getMetodo() {
        return metodo;
    }

    @Override
    public void run() {
        Conexion con = GetSet.getConConfig();
        //con.cargarCon();
        int numIntento = 0;
        do {
            try {
                enviarPeticion(con);
                evaluarRta(con);
            } catch (Exception e) {
                if (e.getMessage().indexOf("For input string") >= 0) {
                    Rta = "No se pudo establecer conexión con el RTP, configuración no establecida.";
                } else if (e.getMessage().indexOf("Unable to resolve host") >= 0) {
                    Rta = "No se pudo establecer conexión con el RTP, host inaccesible.";
                } else if (e.getMessage().indexOf("port out of range") >= 0) {
                    Rta = "No se pudo establecer conexión con el RTP, puerto fuera de rango.";
                } else if (e.getMessage().indexOf("connect timed out") >= 0) {
                    Rta = "No se pudo establecer conexión con el RTP, tiempo agotado.";
                } else {
                    Rta = "Error de Conexión: " + e.getMessage();
                }
            }

            verificarDatosNulos();

            if (Rta.contains("isConnected failed: ETIMEDOUT (Connection timed out)")
                    || Rta.contains("Datos nulos")
                    || Rta.contains("Connection reset")) {

                numIntento++;

            } else {
                numIntento = INTENTOS;
            }
        } while (numIntento < INTENTOS);
    }

    private void enviarPeticion(Conexion con) throws Exception {

        if(foto == null){
            foto = new fotoBmEnvio("", 0, null);
        }

        byte[] buffer = new byte[BUFFER_SIZE];

        String IP = con.getIpRTP();
        String DEFAULT_PORT = con.getPortRTP();

        /* Open a connection */
        InetAddress addr = InetAddress.getByName(IP);
        SocketAddress sockaddr = new InetSocketAddress(addr, Integer.parseInt(DEFAULT_PORT));
        Socket socket = new Socket();
        socket.setTcpNoDelay(true);
        socket.setTrafficClass(0x10);
        socket.setSoTimeout(1800000);
        socket.connect(sockaddr, 1800000);

        OutputStream output = socket.getOutputStream();

        JSONArray configuracion = con.getConfiguracion();

        //SessionManager session = GetSet.getSession();

        /*String imei = session.getImei();
        String usuario = session.getUsuario();*/
        String imei = GetSet.getImei();
        String usuario = GetSet.getUsuario();
        int CIA = -1;
        int GT = -1;

        JSONObject value = new JSONObject();
        value.put("CONFIGURACION", configuracion);
        value.put("METODO", new JSONObject().put("METODO", metodo).put("PARAMETROS", parametros).put("TIPO", tipoMetodo));
        value.put("REMITENTE", new JSONObject().put("IMEI", imei).put("USUARIO", usuario).put("CIA", CIA).put("GT", GT));

        String json = value.toString();
        //Log.e("JSON",json);
        byte[] bytesJson = json.getBytes("UTF-8");
        int tamanoJson = bytesJson.length;

        String string = "HEADER\n" + foto.getTamanoFoto() + "\n" + foto.getNombreFoto() + "\n";
        string = string + tamanoJson + "\n";
        byte[] bytesHeaders = string.getBytes("UTF-8");
        int tamanoHeaders = bytesHeaders.length;
        System.arraycopy(bytesHeaders, 0, buffer, 0, tamanoHeaders);
        output.write(buffer);

        byte[] bufferJson = new byte[tamanoJson];
        System.arraycopy(bytesJson, 0, bufferJson, 0, tamanoJson);
        output.write(bufferJson);

        if (foto.getTamanoFoto() > 0) {
            buffer = new byte[BUFFER_SIZE];
            ByteArrayInputStream rdr = new ByteArrayInputStream(foto.getBmFoto());
            int count;
            while ((count = rdr.read(buffer, 0, buffer.length)) > 0) {
                output.write(buffer, 0, count);
            }
            rdr.close();
        }

        /* Flush the output to commit */
        output.flush();
        //recibe respuesta del servidor y formatea a String
        Log.i("I/TCP Client", "Received data from server");
        java.io.InputStream stream2 = socket.getInputStream();

        //Se convierte el InputStream en un BufferedReader para poder recorrer linea por linea
        BufferedReader in = new BufferedReader(new InputStreamReader(stream2, "UTF-8"));
        String linea;
        StringBuilder responseStrBuilder = new StringBuilder();
        while ((linea = in.readLine()) != null) {
            responseStrBuilder.append(linea.trim());
        }

        jsonRespuesta = new JSONObject(responseStrBuilder.toString());

        stream2.close();
        output.close();
        socket.close();
        in.close();
    }

    private void evaluarRta(Conexion con) throws Exception {
        if (!jsonRespuesta.getJSONObject("TXT").getString("RTA").equals("OK")) {
            if (con.getConfiguracion() == null) {
                Rta = "No se pudo establecer conexión a la base de datos, configuración no establecida.";
            } else {
                String causa = jsonRespuesta.getJSONObject("TXT").getString("CAUSA");
                if (causa == null) {
                    Rta = "Error de respuesta del RTP: causa NULL";
                } else if (causa.indexOf("Comunicaci?n de Oracle: fallo al conectar al servidor o al analizar la cadena de conexi?n") >= 0) {
                    Rta = "No se pudo establecer conexión a la base de datos, error con la IP o el puerto.";
                    GetSet.setCedencialesErroneas(Rta);
                } else if (causa.indexOf("ORA-12514") >= 0) {
                    Rta = "No se pudo establecer conexión a la base de datos, error con la BD.";
                    GetSet.setCedencialesErroneas(Rta);
                } else if (causa.indexOf("ORA-12545") >= 0) {
                    Rta = "No se pudo establecer conexión a la base de datos, error con la IP.";
                    GetSet.setCedencialesErroneas(Rta);
                } else if (causa.indexOf("ORA-01017") >= 0) {
                    Rta = "No se pudo establecer conexión a la base de datos, error en el usuario o la contraseña.";
                    GetSet.setCedencialesErroneas(Rta);
                } else if (causa.indexOf("ORA-12541") >= 0) {
                    Rta = "No se pudo establecer conexión a la base de datos, error de credenciales.";
                    GetSet.setCedencialesErroneas(Rta);
                } else if (causa.indexOf("Referencia a objeto no establecida como instancia de un objeto. , Metodo:") >= 0 ||
                        causa.indexOf("Transporte de Red: fallo de conexi?n a la direcci?n de transporte TCP") >= 0) {
                    Rta = "No se pudo establecer conexión a la base de datos.";
                } else if (causa.indexOf("ORA-00001") != 0) {
                    Rta = "Error de respuesta del RTP: " + causa;
                }
                    /*else{
                        if (causa.indexOf("Unexpected end of content") == 0 &&
                                causa.indexOf("Unterminated string.") == 0) {
                            Log.e("error controlado rta RTP", causa);
                        }
                    }*/
            }

        } else {
            Rta = "OK";
        }

    }

    private void verificarDatosNulos() {
        try {
            JSONArray DATOS = new JSONArray();
            DATOS = jsonRespuesta.getJSONObject("TXT").optJSONArray("DATOS");
        } catch (Exception e) {
            Rta = "Datos nulos";
            reintento = true;
        }
    }

    public String getRta() {
        return Rta;
    }

    public JSONObject getJsonRespuesta() {
        return jsonRespuesta;
    }
}