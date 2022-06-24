package implementaciones;

import dominio.Compra;
import excepciones.PersistenciaException;
import interfaces.IComprasDAO;
import interfaces.IConexionBD;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class ComprasDAO implements IComprasDAO{

    private IConexionBD conexionBD;

    public ComprasDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }
    
    @Override
    public void agregar(Compra compra) throws PersistenciaException {
        try {
            EntityManager entityManager = this.conexionBD.crearConexion();
            entityManager.getTransaction().begin();
            entityManager.persist(compra);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            Logger.getLogger(ComprasDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No fue posible agregar la compra");
        }
    }

    @Override
    public Compra consultar(Long id) throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            Compra compra = em.find(Compra.class, id);
            return compra;
        } catch (Exception ex) {
            Logger.getLogger(ComprasDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No se encontró la compra");
        }
    }
    
}
