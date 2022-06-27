package implementaciones;

import dominio.Compra;
import dominio.Usuario;
import excepciones.PersistenciaException;
import interfaces.IConexionBD;
import interfaces.IUsuariosDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class UsuariosDAO implements IUsuariosDAO{

    private final IConexionBD conexionBD;

    public UsuariosDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }
    
    @Override
    public void agregar(Usuario usuario) throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No fue posible agregar el usuario");
        }
    }

    @Override
    public Usuario consultar(Long id) throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            Usuario usuario = em.find(Usuario.class, id);
            return usuario;
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No se encontró el usuario");
        }
    }

    @Override
    public void actualizar(Usuario usuario) throws PersistenciaException {
        try {
            EntityManager entityManager = this.conexionBD.crearConexion();
            entityManager.getTransaction().begin();
            Usuario usuarioGuardado = entityManager.find(Usuario.class, usuario.getId());
            if(usuarioGuardado == null){
                throw new PersistenciaException("No se encontró el usuario a actualizar");
            }
            usuarioGuardado.setNombre(usuario.getNombre());
            usuarioGuardado.setTelefono(usuario.getTelefono());
            if(usuario.getCompras() != null){
                for(Compra compra: usuario.getCompras()){
                usuarioGuardado.addCompra(compra);
                }
            }
            
            entityManager.persist(usuarioGuardado);
            entityManager.getTransaction().commit();
        } catch(PersistenciaException ex){
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(VideojuegosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No fue posible actualizar el videojuego");
        }
    }

    @Override
    public void eliminar(Long id) throws PersistenciaException {
        try {
            EntityManager entityManager = this.conexionBD.crearConexion();
            entityManager.getTransaction().begin();
            Usuario usuarioGuardado = entityManager.find(Usuario.class, id);
            if(usuarioGuardado == null){
                throw new PersistenciaException("No se encontró el usuario a eliminar");
            }
            if(usuarioGuardado.getCompras() != null && !usuarioGuardado.getCompras().isEmpty()){
                throw new PersistenciaException("No se puede eliminar un usuario que ya realizó una compra");
            }
            entityManager.remove(usuarioGuardado);
            entityManager.getTransaction().commit();
        }catch(PersistenciaException e){
            throw e;
        }
        catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No fue posible eliminar el usuario");
        }
    }

    @Override
    public List<Usuario> consultarTodos() throws PersistenciaException {
        try {
            EntityManager entityManager = this.conexionBD.crearConexion();
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Usuario> criteria = builder.createQuery(Usuario.class);
            Root<Usuario> root = criteria.from(Usuario.class);
            
            CriteriaQuery<Usuario> select = criteria.select(root);
            
            TypedQuery<Usuario> query = entityManager.createQuery(select);
            
            return query.getResultList();
                   
                    
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No fue posible eliminar el usuario");
        }
    }
    
}
