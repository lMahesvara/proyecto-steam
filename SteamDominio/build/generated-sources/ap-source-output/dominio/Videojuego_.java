package dominio;

import dominio.DetalleCompra;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.9.v20210604-rNA", date="2022-06-24T17:44:14")
@StaticMetamodel(Videojuego.class)
public class Videojuego_ { 

    public static volatile SingularAttribute<Videojuego, Float> precio;
    public static volatile ListAttribute<Videojuego, DetalleCompra> compras;
    public static volatile SingularAttribute<Videojuego, String> desarrolladora;
    public static volatile SingularAttribute<Videojuego, Long> id;
    public static volatile SingularAttribute<Videojuego, Integer> stock;
    public static volatile SingularAttribute<Videojuego, String> nombre;

}