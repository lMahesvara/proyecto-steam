package interfaces;

import dominio.Usuario;
import excepciones.PersistenciaException;

public interface IUsuariosDAO {
    public void agregar(Usuario usuario) throws PersistenciaException;
    public Usuario consultar(Long id) throws PersistenciaException;
    public void actualizar(Usuario usuario) throws PersistenciaException;
    public void eliminar(Usuario usuario) throws PersistenciaException;
}
