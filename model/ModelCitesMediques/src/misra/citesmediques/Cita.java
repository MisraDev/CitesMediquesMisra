/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misra.citesmediques;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

/**
 *
 * @author misra
 */
public class Cita implements Serializable {
    
    //id
    private int codiCíta;
    //clau secunadaria composta
    private Date dataCita;
    private String infrome;
    //ManyToONe
    private Persona persona;
    //manyToOne
    //Clau secundaria composta
    private Metge metge;
    //clauSecundariacomostta
    private Especialitats especialitat;
    
    protected Cita(){}

    public Cita(Date dataCita, String infrome) {
        this.dataCita = dataCita;
        this.infrome = infrome;
    }

    public int getCodiCíta() {
        return codiCíta;
    }

    public Date getDataCita() {
        return dataCita;
    }

    public String getInfrome() {
        return infrome;
    }

    public void setDataCita(Date dataCita) {
        this.dataCita = dataCita;
    }

    public void setInfrome(String infrome) {
        this.infrome = infrome;
    }

    public Persona getPersona() {
        return persona;
    }

    public Metge getMetge() {
        return metge;
    }

    public void setPersona(Persona persona) {
        System.out.println("Entro setPersona");
        if (persona == null) {
            throw new CitaException("En una cita, la persona es obligatoria");
        }
        persona.addCita(this);
        this.persona = persona;
    }

    public void setMetge(Metge metge) {
        System.out.println("Entro setMetge");
        if (metge == null) {
            throw new CitaException("En una cita, el Metge és obligatòri");
        }
        metge.addObteCites(this);
        this.metge = metge;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.codiCíta;
        hash = 97 * hash + Objects.hashCode(this.dataCita);
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
        final Cita other = (Cita) obj;
        if (this.codiCíta != other.codiCíta) {
            return false;
        }
        return Objects.equals(this.dataCita, other.dataCita);
    }

    @Override
    public String toString() {
        return "Cita{" + "codiC\u00edta=" + codiCíta + ", dataCita=" + dataCita + ", infrome=" + infrome + '}';
    }
    
    
    
}
