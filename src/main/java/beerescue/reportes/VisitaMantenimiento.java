package beerescue.reportes;

import java.util.Date;

public class VisitaMantenimiento {
    private int id;
    private Date fecha;
    private TipoActividad tipoActividad;
    private String observaciones;
    private int codigoEstacion;

    // Constructores

    public VisitaMantenimiento(Date fecha, TipoActividad tipoActividad, String observaciones, int codigoEstacion) {
        this.fecha = fecha;
        this.tipoActividad = tipoActividad;
        this.observaciones = observaciones;
        this.codigoEstacion = codigoEstacion;
    }

    @Override
    public String toString() {
        return "Visita de Mantenimiento #" + id + 
               " - Actividad: " + tipoActividad + 
               " - Estación: " + codigoEstacion + 
               " - Fecha: " + fecha + 
               " - Observaciones: " + (observaciones != null ? observaciones : "Sin observaciones");
    }

    public Date getFecha() {
        return fecha;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoActividad getTipoActividad() {
        return tipoActividad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public int getCodigoEstacion() {
        return codigoEstacion;
    }

}
