package misra.citesmediques.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import misra.citesmediques.R;
import misra.citesmediques.ViewModel;
import misra.citesmediques.adapter.CitasAdapter;
import misra.citesmediques.adapter.HoresAdapter;
import misra.citesmediques.databinding.FragmentCrearCitaBinding;
import misra.citesmediques.databinding.FragmentListaCitesBinding;
import misra.citesmediques.model.CitaFormat;
import misra.citesmediques.model.Especialitats;
import misra.citesmediques.model.Persona;
import misra.citesmediques.utils.JsonUtils;


public class CrearCitaFragment extends Fragment implements HoresAdapter.HoraSelectedListener{
    FragmentCrearCitaBinding binding;
    ViewModel viewModel;
    private List<Especialitats> listaEspecialitats = null;
    ArrayAdapter<Especialitats> adapterEsp;
    ArrayAdapter<Persona> adapterMet;
    boolean isFirstSelection = true;
    View mView;
    Especialitats especialitatSeleccionada;
    Persona metgeSeleccionado;
    String dateStringSeleccionada;
    HoresAdapter horesAdapter;


    private int codiPersona;


    private String host = "192.168.73.1";
    private int port = 9999;

    public CrearCitaFragment() {
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
        binding = FragmentCrearCitaBinding.inflate(inflater,container, false);
        mView = inflater.inflate(R.layout.fragment_lista_cites, container, false);
        viewModel =
                new ViewModelProvider(this).get(ViewModel.class);

        Bundle bundle = getArguments();
        if (bundle != null) {
            codiPersona = bundle.getInt("codiPersona");
        }
        binding.metgesSpinner.setEnabled(false);
        recuperarLListaEsp();

        // Obtén la referencia al Spinner en tu código
        Spinner spinnerEsp = binding.especialitatSpinner;
        Spinner spinnerMetg = binding.metgesSpinner;
        // Crea el adaptador para el Spinner


        viewModel.mEspecialitats.observe(getViewLifecycleOwner(), new Observer<List<Especialitats>>() {
            @Override
            public void onChanged(List<Especialitats> especialitats) {
                adapterEsp = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, especialitats);
                adapterEsp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEsp.setAdapter(adapterEsp);

            }
        });

        spinnerEsp.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirstSelection) {
                    isFirstSelection = false;
                    return;
                }
                Especialitats especialitat = (Especialitats) parent.getItemAtPosition(position);
                viewModel.seleccionarEspecialitat(especialitat);
                especialitatSeleccionada = (Especialitats) parent.getItemAtPosition(position);

                //binding.especialitatSpinner.setEnabled(false);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewModel.mEspecialitat.observe(getViewLifecycleOwner(), new Observer<Especialitats>() {
            @Override
            public void onChanged(Especialitats especialitat) {
                binding.metgesSpinner.setEnabled(true);
                viewModel.getMetgePerEspecialitat(especialitat.getCodi(), mView);
            }
        });

        viewModel.mMetges.observe(getViewLifecycleOwner(), new Observer<List<Persona>>() {
            @Override
            public void onChanged(List<Persona> personas) {
                adapterMet = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, personas);
                adapterMet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMetg.setAdapter(adapterMet);

            }
        });

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        binding.datePickerActions.init(year, month, dayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //primer buidem la llista del recycler
                List<Time> timesbuit = new ArrayList<>();
                binding.rcyCitesDisp.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.rcyCitesDisp.setHasFixedSize(true);
                horesAdapter = new HoresAdapter(timesbuit, CrearCitaFragment.this);
                binding.rcyCitesDisp.setAdapter(horesAdapter);
                //després carreguem la llista de hores si hi ha
                metgeSeleccionado = (Persona) spinnerMetg.getSelectedItem();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                dateStringSeleccionada = dateFormat.format(calendar.getTime());
                viewModel.getHoresDisponibles(year, monthOfYear, dayOfMonth, metgeSeleccionado,especialitatSeleccionada, mView);

            }
        });

        viewModel.mHoresDisponibles.observe(getViewLifecycleOwner(), new Observer<List<Time>>() {
            @Override
            public void onChanged(List<Time> times) {
                System.out.println("hores disponobles"+times.get(0));
                binding.rcyCitesDisp.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.rcyCitesDisp.setHasFixedSize(true);
                horesAdapter = new HoresAdapter(times, CrearCitaFragment.this);
                binding.rcyCitesDisp.setAdapter(horesAdapter);
            }
        });



        return binding.getRoot();
    }

    private void recuperarLListaEsp() {
        String json = JsonUtils.generarJSON("get especialitats");
        final List<Especialitats> llistaEsp = new ArrayList<>();
        new AsyncTask<String, Void, List<Especialitats>>() {
            @Override
            protected List<Especialitats> doInBackground(String... params) {
                List<Especialitats> result = null;
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
                        JSONObject espObject = jsonArray.getJSONObject(i);
                        int codi = espObject.getInt("codi");
                        String nom = espObject.getString("nom");

                        Especialitats esp = new Especialitats(nom);
                        esp.setCodi(codi);
                        result.add(esp);
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
            protected void onPostExecute(List<Especialitats> result) {
                if (result == null || result.isEmpty()) {
                    // Manejar el error de inicio de sesión aquí
                    Toast.makeText(requireContext(), "No se recuperaron las Especialidades", Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.recuperarEspecialitats(result);
                    //listaEspecialitats = new ArrayList<>(result);

                }
                System.out.println("El resultado es: " + result);
            }
        }.execute(json);
    }


    @Override
    public void onHoraSeleccionada(Time seleccionada) {
        // Convertir el String de fecha a un objeto java.util.Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date fecha = null;
        try {
            java.util.Date utilDate = dateFormat.parse(dateStringSeleccionada);
            fecha = new java.sql.Date(utilDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //pasasmos a Timestamp
        java.sql.Timestamp timestamp = new java.sql.Timestamp(fecha.getTime() + seleccionada.getTime());
        //luego a string para adjuntarlo a json
        SimpleDateFormat dateFormatt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestampString = dateFormatt.format(timestamp);
        String json = JsonUtils.generarJSON("Concertar cita", codiPersona, metgeSeleccionado.getCodi(), especialitatSeleccionada.getCodi(), timestampString);
        new AsyncTask<String, Void, List<CitaFormat>>() {
            @Override
            protected List<CitaFormat> doInBackground(String... params) {
                List<CitaFormat>  result = null;
                try {
                    Socket socket = new Socket(host, port);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write(params[0]);
                    writer.newLine();
                    writer.flush();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String jsonResponse = reader.readLine();
                    result = new ArrayList<>();
                    // Convertir la cadena JSON en un JSONArray
                    JSONArray jsonArray;
                    if(jsonResponse!= null){
                        jsonArray = new JSONArray(jsonResponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject citaObject = jsonArray.getJSONObject(i);
                            int codi = citaObject.getInt("codi");
                            String data = citaObject.getString("data");
                            String horaStr = citaObject.getString("hora");
                            Time hora = Time.valueOf(horaStr);
                            String especialitat = citaObject.getString("especialitat");
                            String metge = citaObject.getString("metge");
                            CitaFormat cita = new CitaFormat(codi, data, hora, especialitat, metge);
                            result.add(cita);
                        }
                    }


                    // Convertir el JSONArray en una lista de objetos CitaFormat


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
                if (result == null||result.isEmpty()) {
                    // Manejar el error de inicio de sesión aquí
                    Toast.makeText(mView.getContext(), "No se ha podido concertar la cita",
                            Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(mView.getContext(), "cita Concertada",
                            Toast.LENGTH_SHORT).show();
                    NavController navController = NavHostFragment.findNavController(requireParentFragment());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("citas", new ArrayList<>(result));
                    navController.navigate(R.id.action_crearCitaFragment_to_listaCitesFragment2, bundle);
                }
                System.out.println("El resultado es: " + result);
            }
        }.execute(json);



    }
}