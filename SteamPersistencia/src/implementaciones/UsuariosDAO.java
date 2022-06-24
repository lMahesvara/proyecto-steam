/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package implementaciones;

import dominio.Compra;
import dominio.Usuario;
import excepciones.PersistenciaException;
import interfaces.IConexionBD;
import interfaces.IUsuariosDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

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
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            Usuario usuarioGuardado = em.find(Usuario.class, usuario.getId());
            if(usuarioGuardado == null){
                throw new PersistenciaException("No se encontró el usuario a actualizar");
            }
            usuarioGuardado.setNombre(usuario.getNombre());
            usuarioGuardado.setTelefono(usuario.getTelefono());
            for(Compra compra: usuario.getCompras()){
                usuarioGuardado.addCompra(compra);
            }
            em.persist(usuarioGuardado);
            em.getTransaction().commit();
        } catch(PersistenciaException ex){
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No fue posible actualizar el usuario");
        }
    }

    @Override
    public void eliminar(Usuario usuario) throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            Usuario usuarioGuardado = em.find(Usuario.class, usuario.getId());
            if(usuarioGuardado == null){
                throw new PersistenciaException("No se encontró el usuario a eliminar");
            }
            em.remove(usuarioGuardado);
            em.getTransaction().commit();
        } catch (Exception ex) {
            Logger.getLogger(UsuariosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No fue posible eliminar el usuario");
        }
    }
    
}
