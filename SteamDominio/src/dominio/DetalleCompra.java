package dominio;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.CascadeOnDelete;

@Entity
@Table(name = "detallescompra")
public class DetalleCompra implements Serializable {

    @Id
    @Column(name = "id_detallecompra")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_copias",nullable = false)
    private int numeroCopias;
    
    @Column(name = "precio",nullable = false)
    private float precio;
    
    @Column(name = "importe",nullable = false)
    private float importe;
    
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_videojuego", nullable = false)
    private Videojuego videojuego;
    
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_compra", nullable = false)
    private Compra compra;

    public DetalleCompra() {
    }

    public DetalleCompra(int numeroCopias, float precio, float importe, Videojuego videojuego, Compra compra) {
        this.numeroCopias = numeroCopias;
        this.precio = precio;
        this.importe = importe;
        this.videojuego = videojuego;
        this.compra = compra;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumeroCopias() {
        return numeroCopias;
    }

    public void setNumeroCopias(int numeroCopias) {
        this.numeroCopias = numeroCopias;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }

    public Videojuego getVideojuego() {
        return videojuego;
    }

    public void setVideojuego(Videojuego videojuego) {
        this.videojuego = videojuego;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleCompra)) {
            return false;
        }
        DetalleCompra other = (DetalleCompra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "detalleCompra{" + "id=" + id + ", numeroCopias=" + numeroCopias + ", precio=" + precio + ", importe=" + importe + ", videojuego=" + videojuego + ", compra=" + compra + '}';
    }

    
    
}
