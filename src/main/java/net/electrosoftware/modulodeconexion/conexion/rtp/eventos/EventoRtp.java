package net.electrosoftware.modulodeconexion.conexion.rtp.eventos;

import android.os.Handler;
import android.util.Log;

/*import net.electrosoftware.energismovil.clasesbases.SessionManager;
import net.electrosoftware.energismovil.clasesbases.fotoBmEnvio;
import net.electrosoftware.energismovil.conexion.entidades.metodoRTP;
import net.electrosoftware.energismovil.conexion.rtp.ConexionService;
import net.electrosoftware.energismovil.conexion.rtp.RequestControl;
import net.electrosoftware.energismovil.conexion.rtp.RtpRequest;
import net.electrosoftware.energismovil.clasesbases.GetSet;
import net.electrosoftware.energismovil.sqlite.SqliteController;*/
import net.electrosoftware.modulodeconexion.clasesbases.fotoBmEnvio;
import net.electrosoftware.modulodeconexion.conexion.entidades.metodoRTP;
import net.electrosoftware.modulodeconexion.utilidades.GetSet;
import net.electrosoftware.modulodeconexion.utilidades.Utilidades;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventoRtp {
    ExecutorService pool;
    int POOLSIZE = 10;
    int REINTENTOTIME = 5;
    int reintentoCount = 0;
    boolean iniciado = false;
    boolean enviando = false;
    boolean finalizado = false;
    //SqliteController sqlcon;


    JSONArray jaParametros = new JSONArray();
    ArrayList<fotoBmEnvio> listaFotoBmEnvios = new ArrayList<>();
    ArrayList<metodoRTP> listMetodos = new ArrayList<>();
    int codMetodo = 0;

    String Rta;
    JSONArray rtaDatos;


    //RequestControl requestControl;
    Utilidades utilidades;
    public ConexionService conexionService;
    //SessionManager session;

    ArrayList<String> llaves = new ArrayList<>();
    JSONArray ja = new JSONArray();
    public static String proyectoAP;
    static String manejoStockMaterial;

    public boolean isFinalizado() {
        return finalizado;
    }


    final Handler handler = new Handler();

    /**
     * Esta función se encarga de almacenar los parametros necesarios que representan a cada evento.<p>
     * A Continuación se mostrará un ejemplo:
     * <p>
     * try {<p>
     * joLogin = new JSONObject().put("NOMBRE", "LOGIN").put("DATO", LOGIN).put("TIPO", "string");<p>
     * } catch (Exception e) {<p>
     * e.toString();<p>
     * }<p>
     */
    public void cargarJoParametros() {

    }

    /**
     * Esta función se encarga de agregar la lista de  métodos que corresponden a cada evento.<p>
     * A Continuación se mostrará un ejemplo:
     * <p>
     * listMetodos.add(new metodoRTP("getArtefactosElectricos", "ARTEFACTOSELECTRICOS"));
     */
    public void cargarListaMetodos() {
    }

    public boolean isIniciado() {
        return iniciado;
    }

    public void setIniciado(boolean iniciado) {
        this.iniciado = iniciado;
    }

    public void iniciar() {
        iniciado = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (finalizado) {
                    conexionService.setPorcentageProgreso(100);
                    handler.removeCallbacks(this);
                } else {
                    if (!enviando) {
                        conexionService = GetSet.getConexionService();
                        if (GetSet.getConexionService().isCancelled()) {
                            finalizado = true;
                        } else {
                            prepararEnvio();
                            handler.postDelayed(this, 1000);
                        }
                    }
                }
            }

        }, 200);
    }


    /**
     * Esta función se encarga de agregar los argumentos y validaciones a cada uno de los
     * métodos agregados.
     * <p>
     * A Continuación se mostrará un ejemplo:<p>
     * switch (codMetodo) {<p>
     * case 0:<p>
     * //Título descriptivo del método 0<p>
     * conexionService.setFase("Descargando descripcion del metodo");<p>
     * ja.put(joLogin);<p>
     * jaParametros.put(ja);<p>
     * sendRequest("GET", null);<p>
     * if (Rta.equals("OK")) {<p>
     * if (rtaDatos != null && rtaDatos.length() > 0) {<p>
     * //Aquí se agregan los argumentos para el método 0<p>
     * codMetodo++;<p>
     * } else {<p>
     * conexionService.setError("Descripción del error");<p>
     * finalizado = true;<p>
     * }<p>
     * }<p>
     * break;<p>
     * default:<p>
     * //Título descriptivo del método por defecto<p>
     * .<p>
     * .<p>
     * .<p>
     * break;<p>
     * }<p>
     * actualizarProgreso();<p>
     * enviando = false;<p>
     */

    public void prepararEnvio() {
        enviando = true;
        jaParametros = new JSONArray();
        ja = new JSONArray();
    }

    public void sendRequest(String tipoMetodo, ArrayList<fotoBmEnvio> fotos) {
        rtaDatos = new JSONArray();
        //reintentoCount = 0;
        ArrayList<RtpRequest> listRequests = new ArrayList<>();
        Rta = null;
        fotoBmEnvio foto;


        if (jaParametros.length() > 0) {
            reintentoCount = 0;
            do {
                try {

                    pool = Executors.newFixedThreadPool(POOLSIZE);
                    for (int i = 0; i < jaParametros.length(); i++) {
                        foto = null;
                        if (fotos != null && fotos.size() > 0) {
                            foto = fotos.get(i);
                        }
                        RtpRequest rtpRequest = new RtpRequest(listMetodos.get(codMetodo).getNombreMetodo(),
                                tipoMetodo, foto, jaParametros.getJSONArray(i));
                        listRequests.add(rtpRequest);
                        pool.execute(rtpRequest);
                    }


                    pool.shutdown();
                    int timeaout = 60;
                    if (conexionService.getTitle().equals("Subiendo la asignación")) {
                        timeaout = 10;
                    }
                    if (pool.awaitTermination(timeaout, TimeUnit.SECONDS)) {
                        for (RtpRequest rr : listRequests) {
                            if (Rta == null || (!rr.equals("OK"))) {
                                Rta = rr.getRta();
                                if (listRequests.size() > 1) {
                                    rtaDatos.put(rr.getJsonRespuesta().optJSONObject("TXT").optJSONArray("DATOS"));
                                } else {
                                    rtaDatos = rr.getJsonRespuesta().optJSONObject("TXT").optJSONArray("DATOS");
                                }

                            }
                        }
                    } else {
                        Rta = "Tiempo de solicitud agotado";
                    }


                    reintentoCount = REINTENTOTIME + 1;
                } catch (Exception e) {
                    Rta = e.getMessage();

                    //if (requestControl.conexionInternet()) {
                    if (utilidades.conexionInternet()) {
                        reintentoCount++;
                    } else {
                        Rta = "No hay conexión a  internet, verifique su red e intente nuevamente";
                        reintentoCount = REINTENTOTIME + 1;
                    }
                }
            } while (reintentoCount < REINTENTOTIME);
        }

        if (Rta == null) {

            if (jaParametros.length() > 0) {
                conexionService.setError("No fué posible obtener una respuesta");
            } else {
                //Rta = "OK";
            }
        } else if (!Rta.equals("OK")) {
            conexionService.setError(Rta);
        }
        Log.e("sendRequest", listMetodos.get(codMetodo).getNombreMetodo() + ": " + Rta);

    }


    public void actualizarProgreso() {
        int progreso = (codMetodo * 100) / listMetodos.size();
        conexionService.setPorcentageProgreso(progreso);
    }


}