package pruebas;

import dominio.Compra;
import dominio.DetalleCompra;
import dominio.Usuario;
import dominio.Videojuego;
import implementaciones.ComprasDAO;
import implementaciones.ConexionBD;
import implementaciones.UsuariosDAO;
import implementaciones.VideojuegosDAO;
import interfaces.IComprasDAO;
import interfaces.IConexionBD;
import interfaces.IUsuariosDAO;
import interfaces.IVideojuegosDAO;
import java.util.GregorianCalendar;

public class Pruebas {

    public static void main(String[] args) {
        IConexionBD conexionBD = new ConexionBD();
        IUsuariosDAO usuariosDAO = new UsuariosDAO(conexionBD);
        IVideojuegosDAO videojuegosDAO = new VideojuegosDAO(conexionBD);
        IComprasDAO comprasDAO = new ComprasDAO(conexionBD);
        try {
            
//            Usuario usuario = usuariosDAO.consultar(4L);
//            usuariosDAO.eliminar(usuario);
            
//            Usuario usuario = usuariosDAO.consultar(4L);
//            usuario.setNombre("Chili");
//            usuario.setTelefono("45123123");
//            usuariosDAO.actualizar(usuario);
            
//            Videojuego videojuego = videojuegosDAO.consultar(4L);
//            videojuegosDAO.eliminar(videojuego);
            
//            Videojuego videojuego = videojuegosDAO.consultar(1L);
//            System.out.println(videojuego);
//            videojuego.getCompras().forEach(compra -> {
//                System.out.println(compra);
//            });
//            videojuego.setNombre("Halo 6");
//            videojuego.setPrecio(1500);
//            videojuegosDAO.actualizar(videojuego);
//            System.out.println(videojuego);
//            videojuego.getCompras().forEach(compra -> {
//                System.out.println(compra);
//            });
            
//            Usuario usuario = usuariosDAO.consultar(1L);
//            //Videojuego videojuego = videojuegosDAO.consultar(1L);
//            Videojuego videojuego2 = videojuegosDAO.consultar(5L);
//            Compra compra = new Compra(new GregorianCalendar(), 500, usuario);
//            DetalleCompra dc1 = new DetalleCompra(2, 100, 200, videojuego2, compra);
//            //DetalleCompra dc2 = new DetalleCompra(1, 300, 300, videojuego, compra);
//            compra.addVideojuego(dc1);
//            //compra.addVideojuego(dc2);
////            
//            comprasDAO.agregar(compra);
//            
//            Videojuego videojuego = videojuegosDAO.consultar(1L);
//            System.out.println(videojuego);
            
//            Videojuego videojuego = new Videojuego("Valorant", "Riot Games", 50, 619);
//            videojuegosDAO.agregar(videojuego);
            
//            Usuario usuario = new Usuario("Shroud", "1234512345");
//            usuariosDAO.agregar(usuario);

            
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    
}
