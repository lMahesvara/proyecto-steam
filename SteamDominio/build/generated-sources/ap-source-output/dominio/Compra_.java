package dominio;

import dominio.DetalleCompra;
import dominio.Usuario;
import java.util.Calendar;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.9.v20210604-rNA", date="2022-06-24T17:44:14")
@StaticMetamodel(Compra.class)
public class Compra_ { 

    public static volatile SingularAttribute<Compra, Calendar> fechaCompra;
    public static volatile SingularAttribute<Compra, Float> total;
    public static volatile SingularAttribute<Compra, Usuario> usuario;
    public static volatile SingularAttribute<Compra, Long> id;
    public static volatile ListAttribute<Compra, DetalleCompra> videojuegos;

}