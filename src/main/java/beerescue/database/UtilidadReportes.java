package beerescue.database;

import beerescue.reportes.*;

import java.util.Date;
import java.util.List;

public class UtilidadReportes implements CRUDOperations<Incidente> {

    @Override
    public void insertar(Incidente incidente) {
        String sql = "INSERT INTO incidente (fecha, tipo_problema, estado_reporte, severidad, descripcion, codigo_estacion) VALUES (?, ?, ?, ?, ?, ?)";
        Object[] params = {
                new java.sql.Timestamp(incidente.getFecha().getTime()),
                incidente.getTipoProblema().name(),
                incidente.getEstadoReporte().name(),
                incidente.getSeveridad().name(),
                incidente.getDescripcion(),
                incidente.getCodigoEstacion()
        };
        UtilsDB.insertarDatos(sql, params);
    }

    @Override
    public boolean actualizar(Incidente incidente) {
        String sql = "UPDATE incidente SET tipo_problema = ?, estado_reporte = ?, severidad = ?, descripcion = ? WHERE id = ?";
        Object[] params = {
                incidente.getTipoProblema().name(),
                incidente.getEstadoReporte().name(),
                incidente.getSeveridad().name(),
                incidente.getDescripcion(),
                incidente.getId()
        };
        return UtilsDB.actualizarDatos(sql, params);
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM incidente WHERE id = ?";
        Object[] params = {id};
        return UtilsDB.eliminarDatos(sql, params);
    }

    @Override
    public Incidente obtenerPorId(int id) {
        return UtilsDB.obtenerIncidentePorId(id);
    }

    @Override
    public List<Incidente> obtenerTodos() {
        return UtilsDB.obtenerIncidentes();
    }

    // Keep your existing specific methods
    public boolean insertarIncidente(Date fecha, TipoProblema tipo, EstadoReporte estado,
                                     Severidad severidad, String descripcion, int codigoEstacion) {
        Incidente incidente = new Incidente(fecha, tipo, estado, severidad, descripcion, codigoEstacion);
        insertar(incidente);
        return true;
    }

    public boolean actualizarEstadoIncidente(int idIncidente, EstadoReporte nuevoEstado) {
        String sql = "UPDATE incidente SET estado_reporte = ? WHERE id = ?";
        Object[] params = {nuevoEstado.name(), idIncidente};
        return UtilsDB.actualizarDatos(sql, params);
    }

    public void insertarMantenimiento(VisitaMantenimiento m) {
        String sql = "INSERT INTO visita_mantenimiento (fecha, tipo_actividad, observaciones, codigo_estacion) VALUES (?, ?, ?, ?)";
        Object[] params = {
                new java.sql.Date(m.getFecha().getTime()),
                m.getTipoActividad().name(),
                m.getObservaciones(),
                m.getCodigoEstacion()
        };
        UtilsDB.insertarDatos(sql, params);
    }

    public List<VisitaMantenimiento> obtenerMantenimientos() {
        return UtilsDB.obtenerMantenimientos();
    }
}