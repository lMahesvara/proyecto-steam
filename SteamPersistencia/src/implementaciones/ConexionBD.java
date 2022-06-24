
package implementaciones;

import interfaces.IConexionBD;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConexionBD implements IConexionBD{

    @Override
    public EntityManager crearConexion() throws Exception {
        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("SteamPU");
        EntityManager entityManager = emFactory.createEntityManager();
        return entityManager;
    }
    
}
