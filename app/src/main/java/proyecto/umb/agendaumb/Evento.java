package proyecto.umb.agendaumb;

/*creamos la clase evento para modelar los eventos en el flujo de nuestro algoritmo*/
public class Evento {
    /*definimos sus propiedades y el tipo*/
    private Integer id;
    private String fecha;
    private String asunto;
    private String actividad;

    /*creamos el metodo constructor que llena las propiedades por medio de parámetros*/
    public Evento(Integer id, String fecha, String asunto, String actividad) {
        this.id = id;
        this.fecha = fecha.toString().trim();
        this.asunto = asunto.toString().trim();
        this.actividad = actividad.toString().trim();
    }

    /*definimos métodos para consultar cada una de las propiedades del objeto abstracto*/
    public Integer getId() {
        return this.id;
    }

    public String getFecha() {
        return this.fecha;
    }

    public String getAsunto() {
        return this.asunto;
    }

    public String getActividad() {
        return this.actividad;
    }
}

//referencia
//https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView