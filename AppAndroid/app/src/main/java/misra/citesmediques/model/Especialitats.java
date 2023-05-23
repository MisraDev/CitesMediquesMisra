package misra.citesmediques.model;

import java.io.Serializable;
import java.util.Objects;

public class Especialitats implements Serializable {
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int codi;

    private String nomEspecialitat;


    protected Especialitats(){

    }

    public Especialitats(String nomEspecialitat){
        this.nomEspecialitat = nomEspecialitat;
    }

    public void setNomEspecialitat(String nomEspecialitat) {
        this.nomEspecialitat = nomEspecialitat;
    }

    public void setCodi(int codi) {
        this.codi = codi;
    }

    public String toString(){
        return nomEspecialitat;
    }

    public String getNomEspecialitat() {
        return nomEspecialitat;
    }

    public int getCodi() {
        return codi;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.codi;
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
        final Especialitats other = (Especialitats) obj;
        if (this.codi != other.codi) {
            return false;
        }
        return Objects.equals(this.nomEspecialitat, other.nomEspecialitat);
    }
/*
    @Override
    public String toString() {
        return "Especialitats{" + "codi=" + codi + ", nomEspecialitat=" + nomEspecialitat + '}';
    }
*/


}
