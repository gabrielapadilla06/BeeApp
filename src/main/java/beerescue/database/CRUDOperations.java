package beerescue.database;

import java.util.List;

public interface CRUDOperations<T> {
    void insertar(T entity);
    boolean actualizar(T entity);
    boolean eliminar(int id);
    T obtenerPorId(int id);
    List<T> obtenerTodos();
}