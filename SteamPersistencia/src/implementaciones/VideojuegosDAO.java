/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package implementaciones;

import dominio.DetalleCompra;
import dominio.Videojuego;
import excepciones.PersistenciaException;
import interfaces.IConexionBD;
import interfaces.IVideojuegosDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author Erick
 */
public class VideojuegosDAO implements IVideojuegosDAO{

    private final IConexionBD conexionBD;

    public VideojuegosDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }
    
    @Override
    public void agregar(Videojuego videojuego) throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            em.persist(videojuego);
            em.getTransaction().commit();
        } catch (Exception ex) {
            Logger.getLogger(VideojuegosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No fue posible agregar el videojuego");
        }
        
    }
    
    @Override
    public Videojuego consultar(Long id) throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            Videojuego videojuego = em.find(Videojuego.class, id);
            return videojuego;
        } catch (Exception ex) {
            Logger.getLogger(VideojuegosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No se encontró el videojuego");
        }
    }

    @Override
    public void actualizar(Videojuego videojuego) throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            Videojuego videojuegoGuardado = em.find(Videojuego.class, videojuego.getId());
            if(videojuegoGuardado == null){
                throw new PersistenciaException("No se encontró el videojuego a actualizar");
            }
            videojuegoGuardado.setNombre(videojuego.getNombre());
            videojuegoGuardado.setDesarrolladora(videojuego.getDesarrolladora());
            videojuegoGuardado.setStock(videojuego.getStock());
            videojuegoGuardado.setPrecio(videojuego.getPrecio());
            for(DetalleCompra compra: videojuego.getCompras()){
                videojuegoGuardado.addCompra(compra);
            }
            em.persist(videojuegoGuardado);
            em.getTransaction().commit();
        } catch(PersistenciaException ex){
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(VideojuegosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No fue posible actualizar el videojuego");
        }
    }

    @Override
    public void eliminar(Videojuego videojuego) throws PersistenciaException {
        try {
            EntityManager em = this.conexionBD.crearConexion();
            em.getTransaction().begin();
            Videojuego videojuegoGuardado = em.find(Videojuego.class, videojuego.getId());
            if(videojuegoGuardado == null){
                throw new PersistenciaException("No se encontró el videojuego a eliminar");
            }
            em.remove(videojuegoGuardado);
            em.getTransaction().commit();
        } catch (Exception ex) {
            Logger.getLogger(VideojuegosDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("No fue posible eliminar el videojuego");
        }
    }
    
}
