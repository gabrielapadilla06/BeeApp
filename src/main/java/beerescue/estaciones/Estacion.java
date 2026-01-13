package beerescue.estaciones;

import java.util.Date;

public class Estacion {
    private int id;
    private String ubicacion;
    private Date fechaInstalacion;
    private Date fechaUltimaVisita;
    private EstadoEstacion estadoOperativo;
    private TipoEstacion tipo;
    private boolean tieneProblemas;
    private int idDueno;

    // Constructores
    public Estacion() {
    }

    public Estacion(int codigo, String ubicacion, Date fechaInstalacion, Date fechaUltimaVisita, EstadoEstacion estadoOperativo, TipoEstacion tipo, boolean tieneProblemas, int idDueno) {
        this.id = codigo;
        this.ubicacion = ubicacion;
        this.fechaInstalacion = fechaInstalacion;
        this.fechaUltimaVisita = fechaUltimaVisita;
        this.estadoOperativo = estadoOperativo;
        this.tipo = tipo;
        this.tieneProblemas = tieneProblemas;
        this.idDueno = idDueno;
    }

    public Estacion(String ubicacion, Date fechaInstalacion, Date fechaUltimaVisita, EstadoEstacion estadoOperativo, TipoEstacion tipo, boolean tieneProblemas, int idDueno) {
        this.ubicacion = ubicacion;
        this.fechaInstalacion = fechaInstalacion;
        this.fechaUltimaVisita = fechaUltimaVisita;
        this.estadoOperativo = estadoOperativo;
        this.tipo = tipo;
        this.tieneProblemas = tieneProblemas;
        this.idDueno = idDueno;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public Date getFechaInstalacion() {
        return fechaInstalacion;
    }

    public Date getFechaUltimaVisita() {
        return fechaUltimaVisita;
    }
    public EstadoEstacion getEstadoOperativo() {
        return estadoOperativo;
    }

    public TipoEstacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoEstacion tipo) {
        this.tipo = tipo;
    }

    public boolean isTieneProblemas() {
        return tieneProblemas;
    }

    public int getIdDueno() {
        return idDueno;
    }
    
}
