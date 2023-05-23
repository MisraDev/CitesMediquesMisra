package misra.citesmediques.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import misra.citesmediques.R;
import misra.citesmediques.model.Cita;
import misra.citesmediques.model.CitaFormat;
import misra.citesmediques.utils.JsonUtils;

public class CitasAdapter extends RecyclerView.Adapter<CitasAdapter.ViewHolder> {
    private List<CitaFormat> mCitas;
    private String host = "192.168.73.1";
    private int port = 9999;
    public CitasAdapter(List<CitaFormat> citas){
        mCitas = citas;
    }
    @NonNull
    @Override
    public CitasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View fila = LayoutInflater.from(parent.getContext()).inflate(R.layout.fila, parent, false);
        ViewHolder vh = new ViewHolder(fila);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CitasAdapter.ViewHolder holder, int position) {
        CitaFormat c = mCitas.get(position);

        String fechaString = c.getData();
        String formatoEntrada = "yyyy-MM-dd";
        String formatoSalida = "dd-MM-yyyy";

        SimpleDateFormat sdfEntrada = new SimpleDateFormat(formatoEntrada);
        SimpleDateFormat sdfSalida = new SimpleDateFormat(formatoSalida);
        String fechaFormateada= "";
        try {
            Date fecha = sdfEntrada.parse(fechaString);
            fechaFormateada = sdfSalida.format(fecha);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.txtData.setText(fechaFormateada);
        holder.txvName.setText("Metge: "+c.getNomMetge());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String tiempoFormateado = formatter.format(c.getHora());
        holder.txvHora.setText(tiempoFormateado);
        holder.txvEsp.setText(c.getEspecialitat());
        final int codiCita = c.getCodi();
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Confirmación");
                builder.setMessage("¿Estás seguro que deseas eliminar esta cita?");

                // Agregar botón de confirmación
                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción a realizar al confirmar la eliminación de la cita
                        eliminarCita(codiCita, v);
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

    }

    private void eliminarCita(int codiCita, View v) {
        String json = JsonUtils.generarJSON("anular visita", codiCita);
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String result = null;
                try {
                    Socket socket = new Socket(host, port);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write(params[0]);
                    writer.newLine();
                    writer.flush();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    result = reader.readLine();
                    socket.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                if (result == null) {
                    // Manejar el error de inicio de sesión aquí
                    Toast.makeText(v.getContext(), "No se ha podido eliminar la cita con codigo"+codiCita,
                            Toast.LENGTH_SHORT).show();
                } else if (result.equals("y")) {
                    // Eliminar la cita de la lista
                    for (int i = 0; i < mCitas.size(); i++) {
                        if (mCitas.get(i).getCodi() == codiCita) {
                            mCitas.remove(i);
                            break;
                        }
                    }
                    notifyDataSetChanged();


                    Toast.makeText(v.getContext(), "cita con codigo"+codiCita+" Eliminada",
                            Toast.LENGTH_SHORT).show();
                } else if (result.equals("n")) {
                    Toast.makeText(v.getContext(), "cita con codigo"+codiCita+"No Eliminada",
                            Toast.LENGTH_SHORT).show();
                }
                System.out.println("El resultado es: " + result);
            }
        }.execute(json);

    }

    @Override
    public int getItemCount() {
        return mCitas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        GridLayout grdl;
        TextView txtData;
        TextView txvName;
        Button btnCancel;
        TextView txvEsp;
        TextView txvHora;

        View vieSelected;
        public ViewHolder(@NonNull View fila) {
            super(fila);
            grdl = fila.findViewById(R.id.grdl);
            txtData = fila.findViewById(R.id.txvdata);
            txvName = fila.findViewById(R.id.txvNameMetge);
            btnCancel = fila.findViewById(R.id.btnCancel);

            txvEsp = fila.findViewById(R.id.txtEsp);
            txvHora = fila.findViewById(R.id.txtHora);

            vieSelected = fila.findViewById(R.id.vieSelected);
        }
    }
}
