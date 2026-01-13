package beerescue.estaciones;

public class EstacionHidrica extends Estacion {
    private double capacidadAgua;
    private double nivelActual;

    // Constructores

    public EstacionHidrica(int codigo, String ubicacion, java.util.Date fechaInstalacion, java.util.Date fechaUltimaVisita, EstadoEstacion estadoOperativo, boolean tieneProblemas, int idDueno, double capacidadAgua, double nivelActual) {

        super(codigo, ubicacion, fechaInstalacion, fechaUltimaVisita, estadoOperativo,TipoEstacion.HIDRICA, tieneProblemas, idDueno);
        this.capacidadAgua = capacidadAgua;
        this.nivelActual = nivelActual;
    }

    public EstacionHidrica(String ubicacion, java.util.Date fechaInstalacion, java.util.Date fechaUltimaVisita, EstadoEstacion estadoOperativo, boolean tieneProblemas, int idDueno, double capacidadAgua, double nivelActual) {

        super(ubicacion, fechaInstalacion, fechaUltimaVisita, estadoOperativo,TipoEstacion.HIDRICA, tieneProblemas, idDueno);
        this.capacidadAgua = capacidadAgua;
        this.nivelActual = nivelActual;
    }

    // Getters y Setters
    public double getCapacidadAgua() {
        return capacidadAgua;
    }

    public double getNivelActual() {
        return nivelActual;
    }

    public void setNivelActual(double nivelActual) {
        this.nivelActual = nivelActual;
    }

 
    @Override
    public String toString() {
        return "EstacionHidrica{" +
                "codigo=" + getId() +
                ", ubicacion='" + getUbicacion() + '\'' +
                ", fechaInstalacion=" + getFechaInstalacion() +
                ", fechaUltimaVisita=" + getFechaUltimaVisita() +
                ", estadoOperativo=" + getEstadoOperativo() +
                ", tipo=" + getTipo() +
                ", tieneProblemas=" + isTieneProblemas() +
                ", idDueno=" + getIdDueno() +
                ", capacidadAgua=" + capacidadAgua +
                ", nivelActual=" + nivelActual +
                '}';
    }

}
