
package dev.ruben.farmacia.modelo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author rubensegura
 */

@Entity
@Table(name = "MEDICAMENTO")
public class Medicamento implements Serializable {
    
    @Id
    @Column(name = "MEDICAMENTO_ID")
    private Long id;
    
    @Column (name = "NOMBRE")
    private String nombre;
    
    @Column (name = "CANTIDAD")
    private int cantidad;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FARMACIA_ID")
    private Farmacia farmacia;
    
    public Medicamento() {
        
    }

    public Medicamento(Long id, String nombre, int cantidad, Farmacia farmacia) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.farmacia = farmacia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Farmacia getFarmacia() {
        return farmacia;
    }

    public void setFarmacia(Farmacia farmacia) {
        this.farmacia = farmacia;
    }

    @Override
    public String toString() {
        return "Medicamento{" + "id=" + id + ", nombre=" + nombre + ", cantidad=" + cantidad + '}';
    }

}
