/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misra.citesmediques;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author misra
 */
public class Metge extends Persona {
    //id clau primaria
    private int codì;
    //clau secundaria composta
    private long codiEmpleat;
    //OneToMany
    List<Cita>obteCites = new ArrayList();
    //ManytoMany
    private List<Especialitats> especialitatsMetge;
    
    protected Metge(){}
    
    public Metge(long codiEmpleat){
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (int) (this.codiEmpleat ^ (this.codiEmpleat >>> 32));
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
    
    final void addObteCites(Cita cita) {
        obteCites.add(cita);
    }
    
    
    
}
