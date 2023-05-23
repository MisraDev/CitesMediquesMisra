/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misra.citesmediques;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.persistence.EmbeddedId;

/**
 *
 * @author misra
 */
public class EntradaHorari implements Serializable {
    
    
    //id 3 id primarias
    private Time hora;
    //id
    
    //id
    //ManyToOne
    private Metge metge;
    //ID
    //Manytoone
    private DiesSetmana diaSetmana;
    
    //ManyToOne
    private Especialitats especialitat;
    
    

    protected EntradaHorari() {
    }

    public EntradaHorari(Time hora, Metge metge, DiesSetmana diaSetmana, Especialitats especialitat) {
        setHora(hora);
        setMetge(metge);
        setDiaSetmana(diaSetmana);
        setEspecialitat(especialitat);
        
    }

    public Time getHora() {
        return hora;
    }

    public Metge getMetge() {
        return metge;
    }

    public DiesSetmana getDiaSetmana() {
        return diaSetmana;
    }

    public Especialitats getEspecialitat() {
        return especialitat;
    }


    public void setHora(Time hora) {
        this.hora = hora;
    }

    public void setMetge(Metge metge) {
        if (metge == null) {
            throw new RuntimeException("metge obligatori");
        }
        this.metge = metge;
    }

    public void setDiaSetmana(DiesSetmana diaSetmana) {
        this.diaSetmana = diaSetmana;
    }

    public void setEspecialitat(Especialitats especialitat) {
        this.especialitat = especialitat;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.hora);
        hash = 53 * hash + Objects.hashCode(this.metge);
        hash = 53 * hash + Objects.hashCode(this.diaSetmana);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EntradaHorari other = (EntradaHorari) obj;
        if (!Objects.equals(this.hora, other.hora)) {
            return false;
        }
        if (!Objects.equals(this.metge, other.metge)) {
            return false;
        }
        return this.diaSetmana == other.diaSetmana;
    }

    @Override
    public String toString() {
        return "EntradaHorari{" + "hora=" + hora + ", metge=" + metge + 
                ", diaSetmana=" + diaSetmana + 
                ", especialitat=" + especialitat + '}';
    }

   
    
    
    
    
}
