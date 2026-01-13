package beerescue.database;

import beerescue.estaciones.*;

import java.util.List;

public class UtilidadEstaciones implements CRUDOperations<Estacion> {

    @Override
    public void insertar(Estacion e) {
        if (e instanceof EstacionFloral ef) {
            String sql = "INSERT INTO estacion (ubicacion, fecha_instalacion, fecha_ultima_visita, estado_operativo, tipo, tiene_problemas, id_usuario, flores, numero_plantas) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            Object[] params = {
                    ef.getUbicacion(),
                    new java.sql.Date(ef.getFechaInstalacion().getTime()),
                    ef.getFechaUltimaVisita() != null ? new java.sql.Date(ef.getFechaUltimaVisita().getTime()) : null,
                    ef.getEstadoOperativo().name(),
                    ef.getTipo().name(),
                    ef.isTieneProblemas(),
                    ef.getIdDueno(),
                    ef.getFlores(),
                    ef.getNumPlantas()
            };
            UtilsDB.insertarDatos(sql, params);
        } else if (e instanceof EstacionHidrica eh) {
            String sql = "INSERT INTO estacion (ubicacion, fecha_instalacion, fecha_ultima_visita, estado_operativo, tipo, tiene_problemas, id_usuario, capacidad_agua, nivel_actual) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            Object[] params = {
                    eh.getUbicacion(),
                    new java.sql.Date(eh.getFechaInstalacion().getTime()),
                    eh.getFechaUltimaVisita() != null ? new java.sql.Date(eh.getFechaUltimaVisita().getTime()) : null,
                    eh.getEstadoOperativo().name(),
                    eh.getTipo().name(),
                    eh.isTieneProblemas(),
                    eh.getIdDueno(),
                    eh.getCapacidadAgua(),
                    eh.getNivelActual()
            };
            UtilsDB.insertarDatos(sql, params);
        }
    }

    @Override
    public boolean actualizar(Estacion e) {
        if (e instanceof EstacionFloral ef) {
            String sql = "UPDATE estacion SET ubicacion = ?, estado_operativo = ?, tiene_problemas = ?, flores = ?, numero_plantas = ? WHERE codigo = ?";
            Object[] params = {
                    ef.getUbicacion(),
                    ef.getEstadoOperativo().name(),
                    ef.isTieneProblemas(),
                    ef.getFlores(),
                    ef.getNumPlantas(),
                    ef.getId()
            };
            return UtilsDB.actualizarDatos(sql, params);
        } else if (e instanceof EstacionHidrica eh) {
            String sql = "UPDATE estacion SET ubicacion = ?, estado_operativo = ?, tiene_problemas = ?, capacidad_agua = ?, nivel_actual = ? WHERE codigo = ?";
            Object[] params = {
                    eh.getUbicacion(),
                    eh.getEstadoOperativo().name(),
                    eh.isTieneProblemas(),
                    eh.getCapacidadAgua(),
                    eh.getNivelActual(),
                    eh.getId()
            };
            return UtilsDB.actualizarDatos(sql, params);
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM estacion WHERE codigo = ?";
        Object[] params = {id};
        return UtilsDB.eliminarDatos(sql, params);
    }

    @Override
    public Estacion obtenerPorId(int id) {
        return UtilsDB.obtenerEstacionPorCodigo(id);
    }

    @Override
    public List<Estacion> obtenerTodos() {
        return UtilsDB.obtenerEstacionesPorUsuario(null);
    }

    public boolean actualizarNivelAguaEstacion(int idEstacion, double nuevoNivel) {
        String sql = "UPDATE estacion SET nivel_actual = ? WHERE codigo = ? AND tipo = 'HIDRICA'";
        Object[] params = {nuevoNivel, idEstacion};
        return UtilsDB.actualizarDatos(sql, params);
    }

    public void insertarSolicitud(SolicitudInstalacion s) {
        String sql = "INSERT INTO solicitud_instalacion (id_usuario, tipo_estacion, ubicacion, estado_solicitud, costo_estimado, fecha_solicitud) VALUES (?, ?, ?, ?, ?, ?)";
        Object[] params = {
                s.getIdUsuario(),
                s.getTipoEstacion().name(),
                s.getUbicacion(),
                s.getEstadoSolicitud().name(),
                s.getCostoEstimado(),
                new java.sql.Date(s.getFechaSolicitud().getTime())
        };
        UtilsDB.insertarDatos(sql, params);
    }

    public boolean actualizarEstadoSolicitud(int idSolicitud, EstadoSolicitud nuevoEstado) {
        String sql = "UPDATE solicitud_instalacion SET estado_solicitud = ? WHERE id = ?";
        Object[] params = {nuevoEstado.name(), idSolicitud};
        return UtilsDB.actualizarDatos(sql, params);
    }
}