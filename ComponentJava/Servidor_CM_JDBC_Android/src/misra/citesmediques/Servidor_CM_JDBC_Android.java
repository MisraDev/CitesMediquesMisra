/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package misra.citesmediques;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import misra.citesmediques.persistence.IGestorCitesMediques;




/**
 *
 * @author misra
 */
public class Servidor_CM_JDBC_Android implements Runnable {
    private static final int PORT = 9999;
    private ServerSocket serverSocket;
    private boolean running;
    private IGestorCitesMediques cp;

    public void iniciarServidor() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("El servidor está ejecutándose...");
            running = true;
            new Thread(this).start();
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }

    public void detenerServidor() {
        try {
            running = false;
            serverSocket.close();
            System.out.println("El servidor se ha detenido.");
        } catch (IOException e) {
            System.out.println("Error al detener el servidor: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        
        String nomFitxer = nomFitxer = "infoCapa.properties";
        
        Properties props = new Properties();
        try {
            props.load(new FileReader(nomFitxer));
        } catch (FileNotFoundException ex) {
            System.out.println("No es troba fitxer de propietats " + nomFitxer);
            infoError(ex);
            System.out.println("Avortem aplicació");
            return;
        } catch (IOException ex) {
            System.out.println("Error en carregar fitxer de propietats " + nomFitxer);
            infoError(ex);
            System.out.println("Avortem aplicació");
            return;
        }

        String nomCapa = props.getProperty("nomCapa");
        if (nomCapa == null || nomCapa.equals("")) {
            System.out.println("Fitxer de propietats " + nomFitxer + " no conté propietat nomCapa");
            System.out.println("Avortem aplicació");
            return;
        }
        String nomFitxerConfiguracioCapa = props.getProperty("nomFitxerConfiguracioCapa");
        if (nomFitxerConfiguracioCapa == null || nomFitxerConfiguracioCapa.equals("")) {
            System.out.println("Fitxer de propietats " + nomFitxer + " no conté propietat nomFitxerConfiguracioCapa");
            System.out.println("Avortem aplicació");
            return;
        }

        
        try {
            Class compo = Class.forName(nomCapa);
            Constructor c = compo.getConstructor(String.class);
            cp = (IGestorCitesMediques) c.newInstance(nomFitxerConfiguracioCapa);
            System.out.println("Connexio amb la base de dades per JDBC establerta");
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            System.out.println("Problema en crear capa de persistència");
            infoError(ex);
            return;
        }
        
        Persona p = cp.getPersona(4);
        List<Persona> lP = cp.getMetgesPerEspecialitatJDBC(3);
        for(Persona per: lP){
            System.out.println(""+per.getNom());
        }
        List<Time> hores = cp.getForatsDisponibles(1, 4, "2023-05-12");
        //System.out.println(""+p.getNom());
        for(Time time: hores){
            System.out.println(""+time);
        }
        List<Especialitats> esp = cp.getEspecialitats();
        for(Especialitats espe: esp){
            System.out.println(""+espe.getNomEspecialitat());
        }
        Date fecha = new Date(System.currentTimeMillis());
        System.out.println(fecha.toString());
        
        
        while (running) {
            try (Socket socket = serverSocket.accept();
            BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                
                String json = lector.readLine();
                JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
                String tipoFuncion = jsonObject.get("tipo").getAsString();


                // Obtener los parámetros del JSON
                JsonElement parametrosElement = jsonObject.get("parametros");
                JsonObject parametrosJson = parametrosElement.isJsonObject() ? parametrosElement.getAsJsonObject() : null;


                switch(tipoFuncion){
                    case "login":
                        escritor.write(ferlogin(parametrosJson));
                        break;
                    case "anular visita":
                        escritor.write(anularVisita(parametrosJson));
                        break;
                    case "get especialitats":
                        escritor.write(getEspecialitats());
                        break;
                    case "metge per especialitat":
                        escritor.write(metgePerEspecialitat(parametrosJson));
                        break;
                    case "Hores disponibles":
                        escritor.write(horesDisponibles(parametrosJson));
                        break;
                    case "Concertar cita":
                        escritor.write(ConcertarCita(parametrosJson));
                        break;
                }

                escritor.newLine();
                escritor.flush();
            } catch (IOException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        
        
    }

    
    
    private static void infoError(Throwable aux) {
        do {
            if (aux.getMessage() != null) {
                System.out.println("\t" + aux.getMessage());
            }
            aux = aux.getCause();
        } while (aux != null);
    }

   

    private String ferlogin(JsonObject parametrosJson) {
        String user = parametrosJson.get("param0").getAsString();
        String passw = parametrosJson.get("param1").getAsString();
        int codiPersona = cp.loginPersona(user, passw);
        String json = null;
        if(codiPersona == 0){
            json="";
            
        } else if(codiPersona>0){
            List<CitaFormat> citas = new ArrayList(cp.getCitesPersonaF(codiPersona));
            
            JsonArray citasArray = new JsonArray();
            
            // Agregar cada cita al JSON array
            for (CitaFormat cita : citas) {
                JsonObject citaObject = new JsonObject();
                citaObject.addProperty("codi", cita.getCodi());
                citaObject.addProperty("data", cita.getData());
                citaObject.addProperty("hora", cita.getHora().toString());
                citaObject.addProperty("especialitat", cita.getEspecialitat());
                citaObject.addProperty("metge", cita.getNomMetge());
                citaObject.addProperty("codiPersona", codiPersona);
                citasArray.add(citaObject);
            }

            // Construir el JSON array
            json = citasArray.toString();
            System.out.println(json);
        }
        return json;
    }


    private String anularVisita(JsonObject parametrosJson) {
        int codiCita = parametrosJson.get("param0").getAsInt();
        if(cp.anularCita(codiCita)){
            cp.commit();
            System.out.println("cita anulada amb codi: "+codiCita);
            return "y";
        }
        System.out.println("cita no anulada");
        return "n";
    }

    private String getEspecialitats() {
        List<Especialitats> esp = new ArrayList(cp.getEspecialitats());
        JsonArray espeArray = new JsonArray();
            
            // Agregar cada cita al JSON array
            for (Especialitats e : esp) {
                JsonObject espObject = new JsonObject();
                espObject.addProperty("codi", e.getCodi());
                espObject.addProperty("nom", e.getNomEspecialitat());
                espeArray.add(espObject);
            }

            // Construir el JSON array
        return espeArray.toString();
    }

    private String metgePerEspecialitat(JsonObject parametrosJson) {
        int codiEsp = parametrosJson.get("param0").getAsInt();
        List<Persona> p = new ArrayList();
        p = cp.getMetgesPerEspecialitatJDBC(codiEsp);
        String json = "";
        if(p!=null){
            JsonArray perArray = new JsonArray();
            
            // Agregar cada cita al JSON array
            for (Persona per : p) {
                JsonObject perObject = new JsonObject();
                perObject.addProperty("codi", per.getCodi());
                perObject.addProperty("nif", per.getNif());
                perObject.addProperty("nom", per.getNom());
                perObject.addProperty("cognom1", per.getCognom1());
                perObject.addProperty("cognom2", per.getCognom2());
                perObject.addProperty("adreca", per.getAdreca());
                perObject.addProperty("poblacio", per.getPoblacio());
                perObject.addProperty("sexe", per.getSexe());
                perObject.addProperty("login", per.getLogin());
                perObject.addProperty("passw", per.getPassword());
                perObject.addProperty("esmetge", per.isEsMetge());
                
                perArray.add(perObject);
            }
        

            // Construir el JSON array
            json = perArray.toString();
            
        }    
            
        return json;
    }

    private String horesDisponibles(JsonObject parametrosJson) {
        int codiesp = parametrosJson.get("param0").getAsInt();
        int codimetge = parametrosJson.get("param1").getAsInt();
        String data = parametrosJson.get("param2").getAsString();
        
        List<Time> llistaHoresDis;
        llistaHoresDis = cp.getForatsDisponibles(codiesp, codimetge, data);
        
        String json = "";
        if (llistaHoresDis!=null&&!llistaHoresDis.isEmpty()) {
            JsonArray horesArray = new JsonArray();

            // Agregar cada hora al JSON array
            for (Time hora : llistaHoresDis) {
                JsonObject horaObject = new JsonObject();
                horaObject.addProperty("hora", hora.toString());
                horesArray.add(horaObject);
            }

            // Construir el JSON array
            json = horesArray.toString();
        } else {
            return json;
        }
            
        return json;
    }

    private String ConcertarCita(JsonObject parametrosJson) {
        
        int codiPer = parametrosJson.get("param0").getAsInt();
        int codimetge = parametrosJson.get("param1").getAsInt();
        int codiEsp = parametrosJson.get("param2").getAsInt();
        String strdataHora = parametrosJson.get("param3").getAsString();
        Timestamp timestamp = Timestamp.valueOf(strdataHora);
        String json = null;
        if(cp.concertarCita(codiPer, codimetge, codiEsp, timestamp)){
            cp.commit();
            List<CitaFormat> citas = new ArrayList(cp.getCitesPersonaF(codiPer));
            
            JsonArray citasArray = new JsonArray();
            
            // Agregar cada cita al JSON array
            for (CitaFormat cita : citas) {
                JsonObject citaObject = new JsonObject();
                citaObject.addProperty("codi", cita.getCodi());
                citaObject.addProperty("data", cita.getData());
                citaObject.addProperty("hora", cita.getHora().toString());
                citaObject.addProperty("especialitat", cita.getEspecialitat());
                citaObject.addProperty("metge", cita.getNomMetge());
                citaObject.addProperty("codiPersona", codiPer);
                citasArray.add(citaObject);
            }

            // Construir el JSON array
            json = citasArray.toString();
            System.out.println(json);
        }
        return json;
    }
}
