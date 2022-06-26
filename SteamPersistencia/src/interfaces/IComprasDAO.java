package interfaces;

import dominio.Compra;
import dominio.Usuario;
import excepciones.PersistenciaException;
import java.util.Calendar;
import java.util.List;

public interface IComprasDAO {
    public void agregar(Compra compra) throws PersistenciaException;
    public Compra consultar(Long id) throws PersistenciaException;
    public List<Compra> consultarTodos(Calendar fechaInicio, Calendar fechaFin, Usuario user) throws PersistenciaException;
}
