package dominio;

import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "compras")
public class Compra implements Serializable {

    @Id
    @Column(name = "id_compra")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "fecha_compra", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar fechaCompra;
    
    @Column(name = "total", nullable = false)
    private float total;
    
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "compra")
    private List<DetalleCompra> videojuegos;

    public Compra() {
    }

    public Compra(Calendar fechaCompra, float total, Usuario usuario) {
        this.fechaCompra = fechaCompra;
        this.total = total;
        this.usuario = usuario;
    }

    public Compra(Long id, Calendar fechaCompra, float total, Usuario usuario) {
        this.id = id;
        this.fechaCompra = fechaCompra;
        this.total = total;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Calendar fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }    

    public List<DetalleCompra> getVideojuegos() {
        return videojuegos;
    }

    public void setVideojuegos(List<DetalleCompra> videojuegos) {
        this.videojuegos = videojuegos;
    }
    
    public void addVideojuego(DetalleCompra videojuego){
        
        if(videojuego == null){
            throw new IllegalArgumentException("El detalle de la compra no puede ser nulo");
        }
        videojuego.setCompra(this);
        if(this.videojuegos == null){
            this.videojuegos = new LinkedList<>();
        }
        this.videojuegos.add(videojuego);
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
        if (!(object instanceof Compra)) {
            return false;
        }
        Compra other = (Compra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Compra{" + "id=" + id + ", fechaCompra=" + fechaCompra + ", total=" + total + ", usuario=" + usuario + '}';
    }

    
    
}
