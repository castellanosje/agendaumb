package proyecto.umb.agendaumb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

//creamos el adaptador personalizado para curar los eventos y enviarlos al listview
public class EventAdapter extends ArrayAdapter<Evento> {

    /*creamos variable contenedora para la lista de eventos*/
    private ArrayList<Evento> listaEventos;

    public EventAdapter(@NonNull Context context, int resource, ArrayList<Evento> listaEventos) {
        super(context, 0, listaEventos);
        //asignamos la lista a la propiedad del adaptador listaEventos
        this.listaEventos = listaEventos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // convert View viene vacía porque el adaptador es personalizado así que hay que definirle la vista

        if(convertView == null){
            /*como la vista viene vacía se crea list_format.xml para personalizar la presentación de los items y se expande*/
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_format,
                    parent, false);
        }

        //se crea una instancia del evento que se esta iterando
        Evento evento = getItem(position);

        //se instancian las vistas(a veces las llaman campos) del xml en variables
        TextView asuntoTextView = (TextView) convertView.findViewById(R.id.eventoAsunto);
        TextView actividadTextView = (TextView) convertView.findViewById(R.id.actividad);
        TextView fechaTextView = (TextView) convertView.findViewById(R.id.fecha);
        ImageView deleteBtn = convertView.findViewById(R.id.borrar);

        //se setean los valores  de la vista que se esta generando utilizando el objeto de tipo evento activo
        asuntoTextView.setText(evento.getAsunto());
        actividadTextView.setText(evento.getActividad());
        fechaTextView.setText(evento.getFecha());

        //se guarda el id del evento como tag de la vista boton para mas adelante poder borrarlo en la base de datos apuntando a la id
        deleteBtn.setTag(evento.getId());

        //se asocial un click listener al boton para ejecutar el evento deleteEvent
        deleteBtn.setOnClickListener(v -> {
            //lambda view.onclickListenner que recibe el tag guardado en la vista boton y la pocisión del item de lista que viene desde getView
            deleteEvent(v.getTag().toString(), position);
        });
        //devolvemos el item construido para su inyección en la lista
        return convertView;
    }

    //Definimos el evento deleteEvent que tiene como proposito lanzar una solicitud post al api en php para borrar un evento
    private void deleteEvent(String id, int pos) {
        //definimos la url al api en un ibjeto del tipo URL
        String URL = "http://10.0.2.2:8888/apiumb/delete.php";
        //Creamos una nueva solicitud de tipo post
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL,
                response -> /*estatus 200*/new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run(){
                                //borrar el item
                                Toast.makeText(getContext(), "Evento eliminado", LENGTH_SHORT).show();
                                remove(listaEventos.get(pos));
                                notifyDataSetChanged();

                            }
                        }, 300),
                error -> /*estatus de error 500, 404, 400 etc...*/Toast.makeText(getContext(), "Error al intentar eliminar", Toast.LENGTH_SHORT).show()
        ){
            @Override
            protected Map<String, String> getParams() {
                /*pasamos el parametro de la id para el borrado en mysql*/
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };
        //instanciamos la cola de solicitudes de la clase Volley
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //añadimos la solicitud recien creada a la cola de solicitudes
        requestQueue.add(stringRequest);


    }


}
