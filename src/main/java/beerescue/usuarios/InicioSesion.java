package beerescue.usuarios;

import beerescue.database.UtilidadUsuarios;
import beerescue.database.UtilsDB;

public class InicioSesion {

    private final UtilidadUsuarios utilidadUsuarios = new UtilidadUsuarios();

    public Usuario loginGUI(String username, String password) {
        Usuario usuario = UtilsDB.obtenerUsuarioPorUsuario(username);
        
        if (usuario == null) {
            return null;
        }
        
        if (!usuario.getContrasena().equals(password)) {
            return null;
        }
        
        return usuario;
    }

    public Usuario signupGUI(String nombre, String username, String password, RolUsuario rol) {
        // Verificar que no haya un nombre de usuario igual
        Usuario usuarioExistente = UtilsDB.obtenerUsuarioPorUsuario(username);
        
        if (usuarioExistente != null) {
            System.out.println("Error: El nombre de usuario ya existe.");
            return null;
        }
        
        // Crear nuevo usuario con los datos proporcionados
        Usuario nuevo = new Usuario(nombre, username, password, rol);
        
        // Insertar en la base de datos
        utilidadUsuarios.insertar(nuevo);
        
        System.out.println("Usuario registrado correctamente.");
        return nuevo;
    }
}