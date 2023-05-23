package misra.citesmediques.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Objects;

public class CitaFormat implements Serializable {
    private int codi;
    private String data;
    private Time hora;
    private String especialitat;
    private String nomMetge;

    protected CitaFormat(){

    }

    public CitaFormat(int codi, String data, Time hora, String especialitat, String nomMetge) {
        this.codi = codi;
        this.data = data;
        this.hora = hora;
        this.especialitat = especialitat;
        this.nomMetge = nomMetge;
    }

    public int getCodi() {
        return codi;
    }

    public void setCodi(int codi) {
        this.codi = codi;
    }

    public String getData() {
        return data;
    }

    public Time getHora() {
        return hora;
    }

    public String getEspecialitat() {
        return especialitat;
    }

    public String getNomMetge() {
        return nomMetge;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public void setEspecialitat(String especialitat) {
        this.especialitat = especialitat;
    }

    public void setNomMetge(String nomMetge) {
        this.nomMetge = nomMetge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CitaFormat)) return false;
        CitaFormat that = (CitaFormat) o;
        return getData().equals(that.getData()) && getHora().equals(that.getHora()) && Objects.equals(getEspecialitat(), that.getEspecialitat()) && Objects.equals(getNomMetge(), that.getNomMetge());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getData(), getHora(), getEspecialitat(), getNomMetge());
    }
}
