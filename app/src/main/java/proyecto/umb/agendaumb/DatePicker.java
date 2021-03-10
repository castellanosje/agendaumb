package proyecto.umb.agendaumb;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePicker extends DialogFragment {
    //creamos la clase DatePicker para extender el funcionamiento de un dialogfragment con la utilidad de calendar

    private DatePickerDialog.OnDateSetListener listener;

    public static DatePicker newInstance(DatePickerDialog.OnDateSetListener listener) {
        //Creamos una nueva instancia de la clase
        DatePicker fragment = new DatePicker();
        //le asignamos el OnDateSetListener
        fragment.setListener(listener);
        //retornamos la instancia con el listenner asignado
        return fragment;
    }

    //metodo que asigna el OnDateSetListener a una instancia
    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    @NonNull
    //hook para desplegar el calendario al momento de crearse el cuadro de dialogo
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //instanciamos la utilidad del calendario en c
        final Calendar c = Calendar.getInstance();
        //consultamos año
        int year = c.get(Calendar.YEAR);
        //consultamos mes
        int month = c.get(Calendar.MONTH);
        //consultamos dia
        int day = c.get(Calendar.DAY_OF_MONTH);

        //construimos una nueva instancia de datepicker pasándole los parámetros
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }


//referencia
//https://programacionymas.com/blog/como-pedir-fecha-android-usando-date-picker
}
