package proyecto.umb.agendaumb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class Agregar_evento extends AppCompatActivity implements View.OnClickListener{

    /*creamos variables contenedoras para las vistas*/
    Button btnEnviar;
    EditText eventoDateView;
    EditText asuntoView;
    EditText actividadView;
    /*variable para almacenar la cola de solicitudes con la clase Volley*/
    RequestQueue requestQueue;
    //la url al api en php
    private static final String URL1 = "http://10.0.2.2:8888/apiumb/insert.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //full screen y ocultar barra
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_agregar_evento);

        /*inicializamos la reqqueue del volley*/
        requestQueue = Volley.newRequestQueue(this);

        //inicializamos las variables con las referencias a cada vista
        eventoDateView = findViewById(R.id.eventoDate);
        asuntoView = findViewById(R.id.eventoAsunto);
        actividadView = findViewById(R.id.eventoActividad);
        btnEnviar = findViewById(R.id.enviar);


        //bindeamos eventos
        eventoDateView.setOnClickListener(this);
        btnEnviar.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        int id = v.getId();
        /*boton enviar*/
        if( id == R.id.enviar ){
            //$id_agenda=0;
            //$fecha=$_POST['FECHA'];
            //$asunto=$_POST['ASUNTO'];
            //$actividad=$_POST['ACTIVIDAD'];
            //textviewname.getText().toString().trim();
            String fecha = eventoDateView.getText().toString().trim();
            String asunto = asuntoView.getText().toString().trim();
            String actividad = actividadView.getText().toString().trim();

            createEvent(fecha, asunto, actividad);
        }

        /*datepicker*/
        if( id == R.id.eventoDate ){
            showDatePickerDialog(findViewById(R.id.eventoDate));
        }
    }


    /*creamos el metodo para usar el datepicker fragment*/
    private void showDatePickerDialog(final EditText editText) {
    // lambda que reemplaza onDataSet
        //instanciamos un nuevo fragment de tipo datepicker y le pasamos una funcion anonima con los parámetros de la fecha, los que necesita el listenner
        DatePicker newFragment = DatePicker.newInstance((view, year, month, dayOfMonth) -> {
            //curamos el string en una variable
            final String selectedDate = dayOfMonth + " / " + (month+1) + " / " + year;
            //seteamos el string al campo
            editText.setText(selectedDate);
        });

        //NOTA:newfragment es de tipo datepicker una clase que extiende el comportamiento de dialog
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /*enviar evento al api*/
    private void createEvent(final String fecha, final String asunto, final String actividad) {
        //Creamos la string request para añadir a la cola del volley
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL1,
                response -> {
                    //lambda response listener
                    Toast.makeText(Agregar_evento.this, "Evento agregado con éxito!!", Toast.LENGTH_SHORT).show();
                    //es como el timeout de js
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    //de vuelta al listado de eventos
                                    Intent i = new Intent(Agregar_evento.this, Listar_eventos.class);
                                    startActivity(i);
                                }
                            }, 300);
                },
                error -> /*lambda response error listener*/ Toast.makeText(Agregar_evento.this, "Error al intentar guardar el evento!!", Toast.LENGTH_SHORT).show()
        ){
            @Override
            protected Map<String, String> getParams(){
                /*mapeamos los parametros (con un hashmap para encriptar) que van por url en la solicitud de tipo post*/
                Map<String, String> params = new HashMap<>();
                params.put("FECHA", fecha);
                params.put("ASUNTO", asunto);
                params.put("ACTIVIDAD", actividad);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}


