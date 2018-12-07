
package dev.ruben.farmacia.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.OneToMany;

/**
 *
 * @author rubensegura
 */

@Entity
@Table(name = "FARMACIA")
public class Farmacia implements Serializable {
   
    @Id
    @Column (name = "FARMACIA_ID")
    private Long id;
    
    @Column (name = "NOMBRE")
    private String nombre;
    
    @OneToMany(mappedBy = "farmacia", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Medicamento> listaMedicamentos=new ArrayList<>(); 

    
    public Farmacia() {
        
    }

    public Farmacia(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    public List<Medicamento> getListaMedicamentos() {
        return listaMedicamentos;
    }

    public void setListaMedicamentos(List<Medicamento> listaMedicamentos) {
        this.listaMedicamentos = listaMedicamentos;
    }

    @Override
    public String toString() {
        return "Farmacia{" + "id=" + id + ", nombre=" + nombre + ", listaMedicamentos=" + listaMedicamentos + '}';
    }
    
    
    
}
