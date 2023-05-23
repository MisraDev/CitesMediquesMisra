/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misra.citesmediques;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author misra
 */
public class Metge extends Persona implements Serializable{
    
    private int codiEmpleat;
    //ManytoMany
    private List<Especialitats> especialitatsMetge = new ArrayList();
                                
    
    
    protected Metge(){}
    
    public Metge(int codiEmpleat){
        this.codiEmpleat = codiEmpleat;
    }

    public void setCodiEmpleat(int codiEmpleat) {
        this.codiEmpleat = codiEmpleat;
    }
    
    
    public long getCodiEmpleat() {
        return codiEmpleat;
    }

    public Iterator<Especialitats> iteEspecialitats() {
        return Collections.unmodifiableCollection(especialitatsMetge).iterator();
    }

    public boolean addEspecialitat(Especialitats e) {
        if (e==null) {
            throw new RuntimeException("Intent d'afegir especialitat null");
        }
        if (especialitatsMetge.contains(e)) {
            return false;
        } else {
            especialitatsMetge.add(e);
            return true;
        }
        
    }

    public boolean removeEspecialitat(Especialitats e) {
        if (e==null) {
            throw new RuntimeException("Intent d'eliminar autor null");
        }
        return especialitatsMetge.remove(e);
    }
    /*
    public Iterator<EntradaHorari> iteAgenda() {
        return Collections.unmodifiableCollection(agenda).iterator();
    }

    public boolean addAgenda(EntradaHorari e) {
        if (e==null) {
            throw new RuntimeException("Intent d'afegir agenda null");
        }
        if (agenda.contains(e)) {
            return false;
        } else {
            agenda.add(e);
            return true;
        }
    }

    public boolean removeAgenda(EntradaHorari e) {
        if (e==null) {
            throw new RuntimeException("Intent d'eliminar agenda null");
        }
        return agenda.remove(e);
    }
*/

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + this.codiEmpleat;
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
        final Metge other = (Metge) obj;
        return this.codiEmpleat == other.codiEmpleat;
    }

    

    
    

    @Override
    public String toString() {
        return "Metge{" + "codiEmpleat=" + codiEmpleat + ", especialitatMetge=" + especialitatsMetge + '}';
    }
    /*
    public Iterator<Cita> iteCitesMetge() {
        return Collections.unmodifiableCollection(obteCites).iterator();
    }

    public boolean addCitaMetge(Cita e) {
        if (e==null) {
            throw new RuntimeException("Intent d'afegir especialitat null");
        }
        if (obteCites.contains(e)) {
            return false;
        } else {
            obteCites.add(e);
            return true;
        }
    }

    public boolean removeCitaMetge(Cita e) {
        if (e==null) {
            throw new RuntimeException("Intent d'eliminar autor null");
        }
        return obteCites.remove(e);
    }
    
    */
    
    
    
}
