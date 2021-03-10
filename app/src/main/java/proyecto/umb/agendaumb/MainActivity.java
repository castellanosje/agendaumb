package proyecto.umb.agendaumb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;

import proyecto.umb.agendaumb.Listar_eventos;
import proyecto.umb.agendaumb.R;


public class MainActivity extends AppCompatActivity {
    //definimos una variable de tipo Timer para guardar tiempo en milisegundos
    Timer t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //full screen y ocultar barra
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //arrancamos el hilo de la actividad principal
        Thread bienvenida = new Thread() {
            public void run() {
                try {
                    /*creamos un loop para exponer la vista durante unos segundos*/
                    int time = 0;
                    while(time < 10000){
                        sleep(100);
                        time = time+100;
                    }
                    /*una vez concluido el bucle lanzamos el intent que lita los eventos de la base de datos*/
                    Intent i = new Intent(MainActivity.this, Listar_eventos.class);
                    startActivity(i);

                } catch (InterruptedException e) {
                    //si hay errores notifique
                    e.printStackTrace();
                }
                finally {
                    //si por alguna razon el codigo llega hasta aquí cierre la app
                    finish();
                }
            }
        };
        /*lanzamos el hilo*/
        bienvenida.start();
    }
}


