package beerescue.database;


import beerescue.estaciones.*;
import beerescue.reportes.*;
import beerescue.usuarios.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtilsDB {
    
    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/beerescue";
        String user = "root";
        String passwd = "sasa";

        Connection conn = null;

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            conn = DriverManager.getConnection(url, user, passwd);
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("Error: MySQL JDBC Driver no encontrado. Asegúrese de que la dependencia esté en el pom.xml");
            e.printStackTrace();
        } catch (SQLException ex) {
            System.err.println("Error al conectar con la base de datos MySQL:");
            ex.printStackTrace();
        } catch (Exception ex) {
            System.err.println("Error inesperado al conectar con la base de datos:");
            ex.printStackTrace();
        }
        return null;
    }

    
    public static void insertarDatos(String sql, Object[] params) {
        Connection conn = getConnection();
        if (conn == null) {
            System.out.println("Error: No se pudo conectar a la base de datos");
            return;
        }

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            
            // Establecer los parámetros
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                System.out.println("Se insertó correctamente el registro");
            } else {
                System.out.println("No se pudo insertar el registro.");
            }

        } catch(Exception ex){
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static boolean actualizarDatos(String sql, Object[] params) {
        Connection conn = getConnection();
        if (conn == null) {
            System.out.println("Error: No se pudo conectar a la base de datos");
            return false;
        }

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            
            // Establecer los parámetros
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                System.out.println("Registro actualizado correctamente");
                return true;
            } else {
                System.out.println("No se encontró el registro a actualizar.");
                return false;
            }

        } catch(Exception ex){
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static boolean eliminarDatos(String sql, Object[] params) {
        Connection conn = getConnection();
        if (conn == null) {
            System.out.println("Error: No se pudo conectar a la base de datos");
            return false;
        }

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            
            // Establecer los parámetros
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                System.out.println("Registro eliminado exitosamente");
                return true;
            } else {
                System.out.println("No se encontró el registro a eliminar.");
                return false;
            }

        } catch(Exception ex){
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static Usuario obtenerUsuarioPorUsuario(String username) {
        String sql = "SELECT id, nombre, usuario, contrasena, rol FROM usuario WHERE usuario = ?";
        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setUsuario(rs.getString("usuario"));
                    usuario.setContrasena(rs.getString("contrasena"));
                    usuario.setRol(RolUsuario.valueOf(rs.getString("rol")));
                    return usuario;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener usuario por nombre de usuario: " + e.getMessage());
        }
        return null;
    }

    public static List<EstacionHidrica> obtenerEstacionesHidricasPorUsuario(int idUsuario) {
        List<EstacionHidrica> estaciones = new ArrayList<>();
        String sql = "SELECT codigo, ubicacion, capacidad_agua, nivel_actual " +
                     "FROM estacion WHERE id_usuario = ? AND tipo = 'HIDRICA' ORDER BY codigo";

        try (Connection conn = UtilsDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                EstacionHidrica estacion = new EstacionHidrica(
                        rs.getInt("codigo"),
                        rs.getString("ubicacion"),
                        null, null, null, false,
                        idUsuario,
                        rs.getDouble("capacidad_agua"),
                        rs.getDouble("nivel_actual")
                );
                estaciones.add(estacion);
            }
        } catch (Exception e) {
            e.printStackTrace(); // or throw a custom exception
        }
        return estaciones;
    }

    public static List<Estacion> obtenerEstacionesPorUsuario(Integer idDueno) {
        List<Estacion> estaciones = new ArrayList<>();
        String sql = "SELECT codigo, ubicacion, fecha_instalacion, fecha_ultima_visita, " +
                     "estado_operativo, tipo, tiene_problemas, id_usuario, flores, numero_plantas, " +
                     "capacidad_agua, nivel_actual FROM estacion";
        if (idDueno != null) {
            sql += " WHERE id_usuario = ? ORDER BY codigo";
        } else {
            sql += " ORDER BY codigo";
        }
    
        try (Connection conn = UtilsDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            if (idDueno != null) {
                ps.setInt(1, idDueno);
            }
    
            ResultSet rs = ps.executeQuery();
    
            while (rs.next()) {
                TipoEstacion tipo = TipoEstacion.valueOf(rs.getString("tipo"));
                Estacion estacion;
    
                if (tipo == TipoEstacion.FLORAL) {
                    estacion = new EstacionFloral(
                            rs.getInt("codigo"),
                            rs.getString("ubicacion"),
                            rs.getDate("fecha_instalacion"),
                            rs.getDate("fecha_ultima_visita"),
                            EstadoEstacion.valueOf(rs.getString("estado_operativo")),
                            rs.getBoolean("tiene_problemas"),
                            rs.getInt("id_usuario"),
                            rs.getString("flores"),
                            rs.getInt("numero_plantas")
                    );
                } else {
                    estacion = new EstacionHidrica(
                            rs.getInt("codigo"),
                            rs.getString("ubicacion"),
                            rs.getDate("fecha_instalacion"),
                            rs.getDate("fecha_ultima_visita"),
                            EstadoEstacion.valueOf(rs.getString("estado_operativo")),
                            rs.getBoolean("tiene_problemas"),
                            rs.getInt("id_usuario"),
                            rs.getDouble("capacidad_agua"),
                            rs.getDouble("nivel_actual")
                    );
                }
    
                estaciones.add(estacion);
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return estaciones;
    }

    public static Incidente obtenerIncidentePorId(int id) {
        Incidente incidente = null;
        String sql = """
                SELECT id, fecha, tipo_problema, estado_reporte, severidad, descripcion, codigo_estacion
                FROM incidente WHERE id = ?
                """;
    
        try (Connection conn = UtilsDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
    
            if (rs.next()) {
                incidente = new Incidente(
                        rs.getTimestamp("fecha"),
                        TipoProblema.valueOf(rs.getString("tipo_problema")),
                        EstadoReporte.valueOf(rs.getString("estado_reporte")),
                        Severidad.valueOf(rs.getString("severidad")),
                        rs.getString("descripcion"),
                        rs.getInt("codigo_estacion")
                );
                incidente.setId(rs.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return incidente;
    }

    public static SolicitudInstalacion obtenerSolicitudPorId(int idSolicitud) {
        String sql = """
                SELECT id, id_usuario, tipo_estacion, ubicacion,
                       estado_solicitud, costo_estimado, fecha_solicitud
                FROM solicitud_instalacion
                WHERE id = ?
                """;
    
        try (Connection conn = UtilsDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setInt(1, idSolicitud);
            ResultSet rs = ps.executeQuery();
    
            if (rs.next()) {
                SolicitudInstalacion solicitud = new SolicitudInstalacion(
                        rs.getInt("id_usuario"),
                        TipoEstacion.valueOf(rs.getString("tipo_estacion")),
                        rs.getString("ubicacion"),
                        EstadoSolicitud.valueOf(rs.getString("estado_solicitud")),
                        rs.getDouble("costo_estimado"),
                        rs.getDate("fecha_solicitud")
                );
                solicitud.setId(rs.getInt("id"));
                return solicitud;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Estacion obtenerEstacionPorCodigo(int codigo) {
        String sql = "SELECT codigo, ubicacion, fecha_instalacion, fecha_ultima_visita, estado_operativo, tipo, " +
                     "tiene_problemas, id_usuario, flores, numero_plantas, capacidad_agua, nivel_actual " +
                     "FROM estacion WHERE codigo = ?";
    
        try (Connection conn = UtilsDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();
    
            if (rs.next()) {
                TipoEstacion tipo = TipoEstacion.valueOf(rs.getString("tipo"));
    
                if (tipo == TipoEstacion.FLORAL) {
                    return new EstacionFloral(
                            rs.getInt("codigo"),
                            rs.getString("ubicacion"),
                            rs.getDate("fecha_instalacion"),
                            rs.getDate("fecha_ultima_visita"),
                            EstadoEstacion.valueOf(rs.getString("estado_operativo")),
                            rs.getBoolean("tiene_problemas"),
                            rs.getInt("id_usuario"),
                            rs.getString("flores"),
                            rs.getInt("numero_plantas")
                    );
                } else {
                    return new EstacionHidrica(
                            rs.getInt("codigo"),
                            rs.getString("ubicacion"),
                            rs.getDate("fecha_instalacion"),
                            rs.getDate("fecha_ultima_visita"),
                            EstadoEstacion.valueOf(rs.getString("estado_operativo")),
                            rs.getBoolean("tiene_problemas"),
                            rs.getInt("id_usuario"),
                            rs.getDouble("capacidad_agua"),
                            rs.getDouble("nivel_actual")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Incidente> obtenerIncidentes() {
        List<Incidente> incidentes = new ArrayList<>();
        String sql = """
                SELECT id, fecha, tipo_problema, estado_reporte,
                       severidad, descripcion, codigo_estacion
                FROM incidente
                ORDER BY fecha DESC
                """;
    
        try (Connection conn = UtilsDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
    
            while (rs.next()) {
                Incidente incidente = new Incidente(
                        rs.getTimestamp("fecha"),
                        TipoProblema.valueOf(rs.getString("tipo_problema")),
                        EstadoReporte.valueOf(rs.getString("estado_reporte")),
                        Severidad.valueOf(rs.getString("severidad")),
                        rs.getString("descripcion"),
                        rs.getInt("codigo_estacion")
                );
                incidente.setId(rs.getInt("id"));
                incidentes.add(incidente);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return incidentes;
    }

    public static List<VisitaMantenimiento> obtenerMantenimientos() {
        List<VisitaMantenimiento> visitas = new ArrayList<>();
        String sql = """
                SELECT id, fecha, tipo_actividad, observaciones, codigo_estacion
                FROM visita_mantenimiento
                ORDER BY fecha DESC
                """;
    
        try (Connection conn = UtilsDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
    
            while (rs.next()) {
                VisitaMantenimiento visita = new VisitaMantenimiento(
                        rs.getTimestamp("fecha"),
                        TipoActividad.valueOf(rs.getString("tipo_actividad")),
                        rs.getString("observaciones"),
                        rs.getInt("codigo_estacion")
                );
                visita.setId(rs.getInt("id"));
                visitas.add(visita);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return visitas;
    }

    public static List<SolicitudInstalacion> obtenerSolicitudes() {
        List<SolicitudInstalacion> solicitudes = new ArrayList<>();
        String sql = """
                SELECT id, id_usuario, tipo_estacion, ubicacion,
                       estado_solicitud, costo_estimado, fecha_solicitud
                FROM solicitud_instalacion
                ORDER BY fecha_solicitud DESC
                """;
    
        try (Connection conn = UtilsDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
    
            while (rs.next()) {
                SolicitudInstalacion solicitud = new SolicitudInstalacion(
                        rs.getInt("id_usuario"),
                        TipoEstacion.valueOf(rs.getString("tipo_estacion")),
                        rs.getString("ubicacion"),
                        EstadoSolicitud.valueOf(rs.getString("estado_solicitud")),
                        rs.getDouble("costo_estimado"),
                        rs.getDate("fecha_solicitud")
                );
                solicitud.setId(rs.getInt("id"));
                solicitudes.add(solicitud);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return solicitudes;
    }
}
