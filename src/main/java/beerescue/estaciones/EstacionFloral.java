package beerescue.estaciones;

public class EstacionFloral extends Estacion {
    private String flores;
    private int numPlantas;

    // Constructores

    public EstacionFloral(int codigo, String ubicacion, java.util.Date fechaInstalacion, java.util.Date fechaUltimaVisita, EstadoEstacion estadoOperativo, boolean tieneProblemas, int idDueno, String flores, int numPlantas) {

        super(codigo, ubicacion, fechaInstalacion, fechaUltimaVisita, estadoOperativo, TipoEstacion.FLORAL, tieneProblemas, idDueno);
        this.flores = flores;
        this.numPlantas = numPlantas;
    }

    public EstacionFloral(String ubicacion, java.util.Date fechaInstalacion, java.util.Date fechaUltimaVisita, EstadoEstacion estadoOperativo,boolean tieneProblemas, int idDueno, String flores, int numPlantas) {

        super(ubicacion, fechaInstalacion, fechaUltimaVisita, estadoOperativo,TipoEstacion.FLORAL, tieneProblemas, idDueno);
        this.flores = flores;
        this.numPlantas = numPlantas;
    }

    // Getters y Setters
    public String getFlores() {
        return flores;
    }

    public int getNumPlantas() {
        return numPlantas;
    }


    @Override
    public String toString() {
        return "EstacionFloral{" +
                "codigo=" + getId() +
                ", ubicacion='" + getUbicacion() + '\'' +
                ", fechaInstalacion=" + getFechaInstalacion() +
                ", fechaUltimaVisita=" + getFechaUltimaVisita() +
                ", estadoOperativo=" + getEstadoOperativo() +
                ", tipo=" + getTipo() +
                ", tieneProblemas=" + isTieneProblemas() +
                ", idDueno=" + getIdDueno() +
                ", flores='" + flores + '\'' +
                ", numPlantas=" + numPlantas +
                '}';
    }
}
