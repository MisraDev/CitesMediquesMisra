package misra.citesmediques.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import misra.citesmediques.R;
import misra.citesmediques.adapter.CitasAdapter;
import misra.citesmediques.databinding.FragmentListaCitesBinding;
import misra.citesmediques.model.CitaFormat;


public class ListaCitesFragment extends Fragment {
    FragmentListaCitesBinding binding;
    CitasAdapter adapter;
    private List<CitaFormat> listaCitas = null;
    private int codiPersona;



    public ListaCitesFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListaCitesBinding.inflate(inflater,container, false);
        View view = inflater.inflate(R.layout.fragment_lista_cites, container, false);
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if (bundle != null) {

            listaCitas = (List<CitaFormat>) bundle.getSerializable("citas");
            codiPersona = bundle.getInt("codiPersona");
        }
        if(!listaCitas.isEmpty()){
            carregarTotesLesCites();
        }
        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    private void carregarTotesLesCites() {
        //listaCitas.clear();

        binding.rcyCites.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rcyCites.setHasFixedSize(true);
        adapter = new CitasAdapter(listaCitas);
        binding.rcyCites.setAdapter(adapter);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.menu_item1){
            Bundle bundle = new Bundle();
            bundle.putInt("codiPersona", codiPersona);
            NavController navController = NavHostFragment.findNavController(requireParentFragment());
            navController.navigate(R.id.action_listaCitesFragment2_to_crearCitaFragment, bundle);

            System.out.println("boton seleccionado");
            return true;
        }


        return false;
    }


}