package misra.citesmediques;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import misra.citesmediques.model.CitaFormat;
import misra.citesmediques.model.Especialitats;
import misra.citesmediques.model.Persona;
import misra.citesmediques.utils.JsonUtils;

public class ViewModel extends AndroidViewModel {

    private String host = "192.168.73.1";
    private int port = 9999;


    public MutableLiveData<List<Especialitats>> mEspecialitats = new MutableLiveData<>();
    public MutableLiveData<List<Persona>> mMetges = new MutableLiveData<>();
    public MutableLiveData<List<String>> mDiesSemanaDisponibles = new MutableLiveData<>();
    public MutableLiveData<Especialitats> mEspecialitat = new MutableLiveData<>();
    public MutableLiveData<List<Time>> mHoresDisponibles = new MutableLiveData<>();
    public MutableLiveData<Integer> mCodiPersona = new MutableLiveData<>();

    public ViewModel(@NonNull Application application) {
        super(application);
    }

    public void recuperarEspecialitats(List<Especialitats> lEsp){
        mEspecialitats.setValue(lEsp);
    }

    public void seleccionarEspecialitat(Especialitats esp){
        mEspecialitat.setValue(esp);
    }

    public void setCodiPersona(int codi){
        mCodiPersona.setValue(codi);
    }



    public void getMetgePerEspecialitat(int codi, View view) {
        String json = JsonUtils.generarJSON("metge per especialitat", codi);

        new AsyncTask<String, Void, List<Persona>>() {
            @Override
            protected List<Persona> doInBackground(String... params) {
                List<Persona> result = null;
                try {
                    Socket socket = new Socket(host, port);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write(params[0]);
                    writer.newLine();
                    writer.flush();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String jsonResponse = reader.readLine();

                    // Convertir la cadena JSON en un JSONArray
                    JSONArray jsonArray = new JSONArray(jsonResponse);

                    // Convertir el JSONArray en una lista de objetos CitaFormat
                    result = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject perObject = jsonArray.getJSONObject(i);
                        int codi = perObject.getInt("codi");
                        String nif = perObject.getString("nif");
                        String nom = perObject.getString("nom");
                        String cognom1 = perObject.getString("cognom1");
                        String cognom2 = perObject.getString("cognom2");
                        String adreca = perObject.getString("adreca");
                        String poblacio = perObject.getString("poblacio");
                        int sexe = perObject.getInt("sexe");
                        String login = perObject.getString("login");
                        String passw = perObject.getString("passw");
                        Boolean esmetge = perObject.getBoolean("esmetge");
                        Persona per = new Persona(nif, nom, cognom1, cognom2, adreca, poblacio, sexe, login, passw, esmetge);
                        per.setCodi(codi);
                        result.add(per);
                    }
                    socket.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<Persona> result) {
                if (result == null || result.isEmpty()) {
                    // Manejar el error de inicio de sesión aquí
                    Toast.makeText(view.getContext(), "Actualment no tenim metges per aquesta especialitat: ", Toast.LENGTH_SHORT).show();
                } else {
                    mMetges.setValue(result);

                }
                System.out.println("El resultado es: " + result);
            }
        }.execute(json);
    }

    public void getHoresDisponibles(int year, int monthOfYear, int dayOfMonth, Persona metgeSeleccionado, Especialitats espSeleccionada, View mView) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String formattedDate = dateFormat.format(calendar.getTime());
        String json = JsonUtils.generarJSON("Hores disponibles",
                espSeleccionada.getCodi(),
                metgeSeleccionado.getCodi(), formattedDate);

        new AsyncTask<String, Void, List<Time>>() {
            @Override
            protected List<Time> doInBackground(String... params) {
                List<Time> result = null;
                try {
                    Socket socket = new Socket(host, port);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write(params[0]);
                    writer.newLine();
                    writer.flush();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String jsonResponse = reader.readLine();

                    // Convertir la cadena JSON en un JSONArray
                    if(jsonResponse!=null){
                        JSONArray jsonArray = new JSONArray(jsonResponse);

                        // Convertir el JSONArray en una lista de objetos CitaFormat
                        result = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject perObject = jsonArray.getJSONObject(i);
                            String horaString = perObject.getString("hora");
                            Time hora = Time.valueOf(horaString);
                            result.add(hora);
                        }
                    } else{
                        result = null;
                    }

                    socket.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<Time> result) {
                if (result == null || result.isEmpty()) {
                    // Manejar el error de inicio de sesión aquí
                    Toast.makeText(mView.getContext(), "No hi han hores disponibles", Toast.LENGTH_SHORT).show();
                } else {
                    mHoresDisponibles.setValue(result);

                }
                System.out.println("El resultado es: " + result);
            }
        }.execute(json);
    }

    public void diesSemanaDisponibles(Persona metgeSeleccionado, Especialitats especialitatSeleccionada, View view) {
        String json = JsonUtils.generarJSON("Dies disponibles",
                metgeSeleccionado.getCodi(),
                especialitatSeleccionada.getCodi());

        new AsyncTask<String, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(String... params) {
                List<String> result = null;
                try {
                    Socket socket = new Socket(host, port);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write(params[0]);
                    writer.newLine();
                    writer.flush();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String jsonResponse = reader.readLine();

                    // Convertir la cadena JSON en un JSONArray
                    if(jsonResponse!=null){
                        JSONArray jsonArray = new JSONArray(jsonResponse);

                        // Convertir el JSONArray en una lista de objetos CitaFormat
                        result = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject perObject = jsonArray.getJSONObject(i);
                            String dia = perObject.getString("dia");
                            result.add(dia);
                        }
                    } else{
                        result = null;
                    }

                    socket.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<String> result) {
                if (result == null || result.isEmpty()) {
                    // Manejar el error de inicio de sesión aquí
                    Toast.makeText(view.getContext(), "No hi han dies disponibles", Toast.LENGTH_SHORT).show();
                } else {
                    mDiesSemanaDisponibles.setValue(result);

                }
                System.out.println("El resultado es: " + result);
            }
        }.execute(json);
    }
}
