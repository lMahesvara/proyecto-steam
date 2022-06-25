package dominio;

import dominio.Compra;
import dominio.Videojuego;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.9.v20210604-rNA", date="2022-06-24T17:44:14")
@StaticMetamodel(DetalleCompra.class)
public class DetalleCompra_ { 

    public static volatile SingularAttribute<DetalleCompra, Compra> compra;
    public static volatile SingularAttribute<DetalleCompra, Float> precio;
    public static volatile SingularAttribute<DetalleCompra, Long> id;
    public static volatile SingularAttribute<DetalleCompra, Videojuego> videojuego;
    public static volatile SingularAttribute<DetalleCompra, Integer> numeroCopias;
    public static volatile SingularAttribute<DetalleCompra, Float> importe;

}