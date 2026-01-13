package beerescue.reportes;

import java.util.Date;

public class Incidente {
    private int id;
    private Date fecha;
    private TipoProblema tipoProblema;
    private EstadoReporte estadoReporte;
    private Severidad severidad;
    private String descripcion;
    private int codigoEstacion;

    // Constructores

    public Incidente(Date fecha, TipoProblema tipoProblema, EstadoReporte estadoReporte, Severidad severidad, String descripcion, int codigoEstacion) {
        this.fecha = fecha;
        this.tipoProblema = tipoProblema;
        this.estadoReporte = estadoReporte;
        this.severidad = severidad;
        this.descripcion = descripcion;
        this.codigoEstacion = codigoEstacion;
    }

    @Override
    public String toString(){
        return "Incidente #" + id + 
               " - Tipo: " + tipoProblema + 
               " - Severidad: " + severidad + 
               " - Estado: " + estadoReporte + 
               " - Estación: " + codigoEstacion + 
               " - Fecha: " + fecha;
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
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public TipoProblema getTipoProblema() {
        return tipoProblema;
    }
    public EstadoReporte getEstadoReporte() {
        return estadoReporte;
    }
    public Severidad getSeveridad() {
        return severidad;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public int getCodigoEstacion() {
        return codigoEstacion;
    }
}
