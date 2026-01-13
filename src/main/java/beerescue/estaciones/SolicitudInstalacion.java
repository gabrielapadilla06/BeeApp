package beerescue.estaciones;

import java.util.Date;

public class SolicitudInstalacion {
    private int id;
    private int idUsuario;                 // <-- nuevo campo
    private TipoEstacion tipoEstacion;
    private String ubicacion;
    private EstadoSolicitud estadoSolicitud;
    private double costoEstimado;
    private Date fechaSolicitud;

    public SolicitudInstalacion(int idUsuario, TipoEstacion tipoEstacion, String ubicacion, EstadoSolicitud estadoSolicitud, double costoEstimado, Date fechaSolicitud) {
        this.idUsuario = idUsuario;
        this.tipoEstacion = tipoEstacion;
        this.ubicacion = ubicacion;
        this.estadoSolicitud = estadoSolicitud;
        this.costoEstimado = costoEstimado;
        this.fechaSolicitud = fechaSolicitud;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public TipoEstacion getTipoEstacion() {
        return tipoEstacion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public EstadoSolicitud getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public double getCostoEstimado() {
        return costoEstimado;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    @Override
    public String toString() {
        return "SolicitudInstalacion{" +
                "id=" + id +
                ", idUsuario=" + idUsuario +
                ", tipoEstacion=" + tipoEstacion +
                ", ubicacion='" + ubicacion + '\'' +
                ", estadoSolicitud=" + estadoSolicitud +
                ", costoEstimado=" + costoEstimado +
                ", fechaSolicitud=" + fechaSolicitud +
                '}';
    }
}