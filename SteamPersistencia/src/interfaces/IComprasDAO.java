package interfaces;

import dominio.Compra;
import excepciones.PersistenciaException;

public interface IComprasDAO {
    public void agregar(Compra compra) throws PersistenciaException;
    public Compra consultar(Long id) throws PersistenciaException;
}
