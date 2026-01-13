package beerescue.database;

import beerescue.usuarios.*;

import java.util.ArrayList;
import java.util.List;

public class UtilidadUsuarios implements CRUDOperations<Usuario> {
    
    //insertar usuario
    @Override
    public void insertar(Usuario u) {
        String sql = "INSERT INTO usuario (nombre, usuario, contrasena, rol) VALUES (?, ?, ?, ?)";
        Object[] params = {
                u.getNombre(),
                u.getUsuario(),
                u.getContrasena(),
                u.getRol().name()
        };
        UtilsDB.insertarDatos(sql, params);
    }

    @Override
    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuario SET nombre = ?, usuario = ?, contrasena = ?, rol = ? WHERE id = ?";
        Object[] params = {
                u.getNombre(),
                u.getUsuario(),
                u.getContrasena(),
                u.getRol().name(),
                u.getId()
        };
        return UtilsDB.actualizarDatos(sql, params);
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuario WHERE id = ?";
        Object[] params = {id};
        return UtilsDB.eliminarDatos(sql, params);
    }

    @Override
    public Usuario obtenerPorId(int id) {
        return null;
    }

    @Override
    public List<Usuario> obtenerTodos() {
        return new ArrayList<>();
    }
}

