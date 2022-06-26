package implementaciones;

import dominio.Compra;
import dominio.Usuario;
import dominio.Videojuego;
import excepciones.PersistenciaException;
import interfaces.IComprasDAO;
import interfaces.IConexionBD;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;

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
            throw new PersistenciaException("No fue posible consultar la compra");
        }
    }

    @Override
    public List<Compra> consultarTodos(Calendar fechaInicio, Calendar fechaFin, Usuario user) throws PersistenciaException {
        List<Compra> compras = new LinkedList<>();
        try {
            EntityManager entityManager = this.conexionBD.crearConexion();
            if(user == null){
                Query query = entityManager.createQuery("SELECT c FROM Compra c WHERE  c.fechaCompra BETWEEN :fechaInicio AND :fechaFin");
                query.setParameter("fechaInicio", fechaInicio);
                query.setParameter("fechaFin", fechaFin);
                return (List<Compra>)query.getResultList();
            }
            Query query = entityManager.createQuery("SELECT c FROM Compra c WHERE c.usuario = :usuario AND c.fechaCompra BETWEEN :fechaInicio AND :fechaFin");
            query.setParameter("usuario", user);
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
            compras = (List<Compra>)query.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(VideojuegosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No fue posible consultar las compras");
        }
        return compras;
    }
    
}
