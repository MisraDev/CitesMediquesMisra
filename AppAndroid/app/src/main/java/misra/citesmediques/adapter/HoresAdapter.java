package misra.citesmediques.adapter;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.util.List;

import misra.citesmediques.R;
import misra.citesmediques.model.CitaFormat;

public class HoresAdapter extends RecyclerView.Adapter<HoresAdapter.ViewHolder>{

    private List<Time> mHoras;
    private int posItemSeleccionat = -1;
    private HoraSelectedListener mlistener;

    public HoresAdapter(List<Time> horas, HoraSelectedListener listener){
        mlistener= listener;
        mHoras = horas;
    }

    public interface HoraSelectedListener {
        public void onHoraSeleccionada(Time seleccionada);
    }

    @NonNull
    @Override
    public HoresAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View fila = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila_hores_disp, parent, false);
        HoresAdapter.ViewHolder vh = new HoresAdapter.ViewHolder(fila);

        //Programació del click de la fila
        fila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = vh.getAdapterPosition();
                //mCards.get(pos).setSelected(true);
                int posAntiga = posItemSeleccionat;
                posItemSeleccionat = pos;
                if(posAntiga!=-1) notifyItemChanged(posAntiga);
                notifyItemChanged(pos);
                Time t = mHoras.get(pos);


                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirmación");
                builder.setMessage("¿Estás seguro que deseas concertar esta cita? "+t+"h.");

                // Agregar botón de confirmación
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción a realizar al confirmar la eliminación de la cita
                        System.out.println("hora Confirmada"+t);
                        if(mlistener!=null) mlistener.onHoraSeleccionada(t);

                    }


                });

                // Agregar botón de cancelar
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción a realizar al cancelar la eliminación de la cita
                        dialog.dismiss();
                    }
                });

                // Mostrar el diálogo
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull HoresAdapter.ViewHolder holder, int position) {
        Time t = mHoras.get(position);

        holder.txtHoras.setText(""+t.toString()+" h");

    }



    @Override
    public int getItemCount() {
        return mHoras.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtHoras;
        public ViewHolder(@NonNull View fila) {
            super(fila);

            txtHoras = fila.findViewById(R.id.txtHoras);
        }
    }
}
