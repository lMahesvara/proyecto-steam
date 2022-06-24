package interfaces;

import dominio.Videojuego;
import excepciones.PersistenciaException;
import java.util.List;

public interface IVideojuegosDAO {
    public void agregar(Videojuego videojuego) throws PersistenciaException;
    public Videojuego consultar(Long id) throws PersistenciaException;
    public Videojuego consultar(String nombre) throws PersistenciaException;
    public List<Videojuego> consultarTodos() throws PersistenciaException;
    public void actualizar(Videojuego videojuego) throws PersistenciaException;
    public void eliminar(Long id) throws PersistenciaException;
}
