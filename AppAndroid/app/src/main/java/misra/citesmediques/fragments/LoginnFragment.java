package misra.citesmediques.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import misra.citesmediques.R;
import misra.citesmediques.ViewModel;
import misra.citesmediques.databinding.FragmentLoginnBinding;
import misra.citesmediques.model.Cita;
import misra.citesmediques.model.CitaFormat;
import misra.citesmediques.utils.JsonUtils;


public class LoginnFragment extends Fragment implements View.OnClickListener, TextWatcher {
    public int codiPersona;
    String host = "192.168.73.1";
    int port = 9999;
    FragmentLoginnBinding binding;
    ViewModel viewModel;

    public LoginnFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel =
                new ViewModelProvider(this).get(ViewModel.class);
        binding = FragmentLoginnBinding.inflate(inflater,container, false);
        binding.btnLogin.setOnClickListener(this);
        binding.username.addTextChangedListener(this);
        binding.password.addTextChangedListener(this);
        return binding.getRoot();

    }

    @Override
    public void onClick(View v) {
        String username = binding.username.getText().toString();
        String password = binding.password.getText().toString();

        // Generar el JSON con la función "login" y los parámetros de username y password
        String json = JsonUtils.generarJSON("login", username, password);

        new AsyncTask<String, Void, List<CitaFormat>>() {
            @Override
            protected List<CitaFormat> doInBackground(String... params) {
                List<CitaFormat> result = null;
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
                        JSONObject citaObject = jsonArray.getJSONObject(i);
                        int codi = citaObject.getInt("codi");
                        String data = citaObject.getString("data");
                        String horaStr = citaObject.getString("hora");
                        Time hora = Time.valueOf(horaStr);
                        String especialitat = citaObject.getString("especialitat");
                        String metge = citaObject.getString("metge");
                        codiPersona = citaObject.getInt("codiPersona");
                        //viewModel.setCodiPersona(codiPersona);
                        CitaFormat cita = new CitaFormat(codi, data, hora, especialitat, metge);
                        result.add(cita);
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
            protected void onPostExecute(List<CitaFormat> result) {
                if (result == null || result.isEmpty()) {
                    // Manejar el error de inicio de sesión aquí
                    Toast.makeText(requireContext(), "Inicio de sesión incorrecto", Toast.LENGTH_SHORT).show();
                } else {

                    NavController navController = NavHostFragment.findNavController(requireParentFragment());
                    // Pasar la lista de citas a la siguiente pantalla o fragmento
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("citas", new ArrayList<>(result));
                    bundle.putInt("codiPersona", codiPersona);
                    navController.navigate(R.id.action_loginnFragment2_to_listaCitesFragment2, bundle);
                }
                System.out.println("El resultado es: " + result);
            }
        }.execute(json);
    }

    public int getCodiPersona() {
        return codiPersona;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        binding.btnLogin.setEnabled(true);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}