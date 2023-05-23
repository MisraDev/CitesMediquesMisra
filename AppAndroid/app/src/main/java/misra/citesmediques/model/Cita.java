package misra.citesmediques.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class Cita implements Serializable {

    //id
    private int codiCita;
    //clau secunadaria composta
    private Date dataCita;
    private String infrome;
    //ManyToONe
    private Persona persona;
    //manyToOne
    //Clau secundaria composta
    private Persona metge;
    //clauSecundariacomostta
    private Especialitats especialitat;

    private boolean esOberta;

    protected Cita(){}

    public Cita(Date dataCita, Persona persona, Persona metge, Especialitats especialitat, boolean esOberta) {
        this.dataCita = dataCita;
        this.persona = persona;
        this.metge = metge;
        this.especialitat = especialitat;
        this.esOberta = esOberta;
    }

    public Cita(Date dataCita, Persona persona, Persona metge, Especialitats especialitat, String informe, boolean esOberta) {
        this.dataCita = dataCita;
        this.infrome = informe;
        this.persona = persona;
        this.metge = metge;
        this.especialitat = especialitat;
        this.esOberta = esOberta;
    }

    public void setCodiCita(int codiCíta) {
        this.codiCita = codiCíta;
    }



    public int getCodiCita() {
        return codiCita;
    }

    public Date getDataCita() {
        return dataCita;
    }

    public String getInfrome() {
        return infrome;
    }

    public Especialitats getEspecialitat() {
        return especialitat;
    }

    public boolean isEsOberta() {
        return esOberta;
    }

    public void setEspecialitat(Especialitats especialitat) {
        if(!this.esOberta){
            throw new RuntimeException("No es pot modificar l'especialitat"
                    + "perqeula vita es Tancada");
        }else{
            this.especialitat = especialitat;

        }

    }

    public void setEsOberta(boolean esOberta) {
        if(this.esOberta && !esOberta){
            this.esOberta = esOberta;
        }else if(!this.esOberta && esOberta){
            throw new RuntimeException("No es pot obrir cita"
                    + "perqeu ja es Tancada");
        }

    }



    public void setDataCita(Date dataCita) {
        this.dataCita = dataCita;
    }

    public void setInfrome(String infrome) {
        if(!this.esOberta){
            throw new RuntimeException("No es pot modificar l'infrome"
                    + "perqeula cita es Tancada");
        }else{
            this.infrome = infrome;

        }

    }

    public Persona getPersona() {
        return persona;
    }

    public Persona getMetge() {
        return metge;
    }

    public void setPersona(Persona persona) {
        System.out.println("Entro setPersona");
        if (persona == null) {
            throw new RuntimeException("En una cita, la persona es obligatoria");
        }
        //       persona.addCitaPersona(this);
        this.persona = persona;
    }

    public void setMetge(Persona metge) {
        System.out.println("Entro setMetge");
        if (metge == null) {
            throw new RuntimeException("En una cita, el Metge és obligatòri");
        }
        //metge.addCitaMetge(this);
        this.metge = metge;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.codiCita;
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
        if (this.codiCita != other.codiCita) {
            return false;
        }
        return Objects.equals(this.dataCita, other.dataCita);
    }

    @Override
    public String toString() {
        return "Cita{" + "codiC\u00edta=" + codiCita + ", dataCita=" + dataCita + ", infrome=" + infrome + '}';
    }



}

