package interfaces;

import dominio.Usuario;
import excepciones.PersistenciaException;
import java.util.List;

public interface IUsuariosDAO {
    public void agregar(Usuario usuario) throws PersistenciaException;
    public Usuario consultar(Long id) throws PersistenciaException;
    public List<Usuario> consultarTodos() throws PersistenciaException;
    public void actualizar(Usuario usuario) throws PersistenciaException;
    public void eliminar(Long id) throws PersistenciaException;
}
