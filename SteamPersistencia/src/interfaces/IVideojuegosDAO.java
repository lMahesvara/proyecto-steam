package interfaces;

import dominio.Videojuego;
import excepciones.PersistenciaException;

public interface IVideojuegosDAO {
    public void agregar(Videojuego videojuego) throws PersistenciaException;
    public Videojuego consultar(Long id) throws PersistenciaException;
    public void actualizar(Videojuego videojuego) throws PersistenciaException;
    public void eliminar(Videojuego videojuego) throws PersistenciaException;
}
