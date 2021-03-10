package proyecto.umb.agendaumb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

//definimos la clase que gestionará el despliegue de eventos
public class Listar_eventos extends AppCompatActivity implements View.OnClickListener {

    /*creamos variables contenedoras para las vistas*/
    ListView listView;
    Button agregar;
    /*variable para el dialog de progreso*/
    ProgressDialog pd;
    /*Array list de objetos de tipo Evento*/
    private ArrayList<Evento> listaEventos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //full screen y ocultar barra
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_listar_eventos);


        //inicializamos las variables con las referencias a cada vista
        listView = findViewById(R.id.listView);
        agregar = findViewById(R.id.agregarBtn);
        agregar.setOnClickListener(this);

        //corremos el método downloadJSON para llamar los datos usando el api en php (fetch)
        downloadJSON("http://10.0.2.2:8888/apiumb/api.php");
    }


    public void downloadJSON(final String urlWebService) {
        //extendemos la clase para funcionar de forma asíncrona para hacer fetch
        class DownloadJSON extends AsyncTask<String, String, String> {

            /*hook para antes de iniciar su ejecución*/
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                /*creamos un nuevo diálogo de progreso*/
                pd = new ProgressDialog(Listar_eventos.this);
                pd.setMessage("Cargando los datos...");
                pd.setCancelable(false);
                pd.show();
            }


            @Override
            /*hook para cuando termina la ejecución*/
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                /*intentamos cargar los datos en la listview con el método loadIntoListView*/
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    /*si hay error lo consultamos*/
                    e.getMessage();
                }
                /*al final del proceso ocultamos el cuadro de dialogo, lo dejamos activo unos milisegundos para evitar parpadeos*/
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                //ocultamos el diálogo
                                pd.hide();
                            }
                        }, 500);

            }

            /*hook para correr el codigo de fondo*/
            @Override
            protected String doInBackground(String... strings) {
                /*inicializamos un objeto de tipo HttpURLConnection nulo*/
                HttpURLConnection con = null;
                /*inicializamos  un buffer de lectura*/
                BufferedReader bufferedReader = null;

                /*intentamos el fetch o la conexión*/
                try {
                    //creamos una instancia de objeto de tipo URL
                    URL url = new URL(urlWebService);

                    //llamamos el metodo openConnection de la clase URL y guardamos el resultado(de tipo HttpURLConnection) en con
                    con = (HttpURLConnection) url.openConnection();
                    //desencadeamos el metodo connect de la clase HttpURLConnection
                    con.connect();
                    //capturamos los datos con getInputStream() de HttpURLConnection
                    InputStream stream = con.getInputStream();
                    // inicializamos el buffer de lectura con los datos recibidos curados por medio del método InputStreamReader y los guardamos en bufferedReader
                    bufferedReader = new BufferedReader(new InputStreamReader(stream));

                    //instanciamos el método StringBuffer en buffer
                    StringBuffer buffer = new StringBuffer();
                    //creamos una variable de tipo string vacia
                    String line = "";
                    //loopeamos cada una de las líneas de bufferedReader
                    while ((line = bufferedReader.readLine()) != null) {
                        //si la línea contiene información entonces la agregamos al buffer
                        buffer.append(line+"\n");
                    }
                    //una vez hemos creado el buffer con todas las lineas y los caracteres de salto retornamos una cadena de texto sin espacios en blanco
                    return buffer.toString().trim();

                } catch (MalformedURLException e) {
                    //cuando ha errorres en la solicitud http notificamos
                    e.printStackTrace();
                } catch (IOException e) {
                    //cuando ha errorres en el bloque tru catch en general
                    e.printStackTrace();
                } finally {
                    //una vez ejecutado el try catch

                    if (con != null) {
                        //si la conección capturo datos, desconectela
                        con.disconnect();
                    }
                    try {
                        if (bufferedReader != null) {
                            //si el lector tiene información, cierrelo
                            bufferedReader.close();
                        }
                    } catch (IOException e) {
                        //si hubo errores en el bloque try catch notifique
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }
        //instanciamos el método
        DownloadJSON getJSON = new DownloadJSON();
        //ejecutamos el la instancia
        getJSON.execute();
    }

    //creamos el metodo loadIntoListView
    private void loadIntoListView(String json) throws JSONException {
        //Creamos una instancia de tipo JSONArray con la cadena de texto del parámetro
        JSONArray jsonArray = new JSONArray(json);

        //iteramos el objeto recultante
        for (int i = 0; i < jsonArray.length(); i++) {
            //extraemos cada objeto del arreglo
            JSONObject obj = jsonArray.getJSONObject(i);

            //creamos un nuevo objeto de tipo evento y lo guardamos en la lista de tipo evento usando los valores del objeto iterado
            listaEventos.add(
                    new Evento(
                            Integer.parseInt(obj.getString("ID_AGENDA")) ,
                            obj.getString("FECHA"),
                            obj.getString("ASUNTO"),
                            obj.getString("ACTIVIDAD")
                    )
            );
        }

        //creamos un nuevo eventAdapter que es una clase que extiende el Adapter estandar
        EventAdapter eventAdapter = new EventAdapter(this, R.layout.list_format, listaEventos);
        //asignamos el adaptador a la vista de tipo lista
        listView.setAdapter(eventAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if( id == R.id.agregarBtn ){
            //cargamos la clase Agregar_evento para crear nuevos eventos
            Intent i = new Intent(Listar_eventos.this, Agregar_evento.class);
            startActivity(i);
        }
    }




}