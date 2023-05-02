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
import java.util.Objects;

/**
 *
 * @author misra
 */
public abstract class Persona implements Serializable {
    
    private int codì;
    private String Nif;
    private String nom;
    private String cognom1;
    private String cognom2;
    private String adreca;
    private String poblacio;
    private int sexe;
    private String login;
    private String password;
    //One to many
    private List<Cita> cites = new ArrayList();
    
    protected Persona(){}

    public Persona(String Nif, String nom, String cognom1, String cognom2, String adreca, String poblacio, int sexe, String login, String password) {
        this.Nif = Nif;
        this.nom = nom;
        this.cognom1 = cognom1;
        this.cognom2 = cognom2;
        this.adreca = adreca;
        this.poblacio = poblacio;
        this.sexe = sexe;
        this.login = login;
        this.password = password;
    }

    public int getCodì() {
        return codì;
    }

    public String getNif() {
        return Nif;
    }

    public String getNom() {
        return nom;
    }

    public String getCognom1() {
        return cognom1;
    }

    public String getCognom2() {
        return cognom2;
    }

    public String getAdreca() {
        return adreca;
    }

    public String getPoblacio() {
        return poblacio;
    }

    public int getSexe() {
        return sexe;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setNif(String Nif) {
        this.Nif = Nif;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCognom1(String cognom1) {
        this.cognom1 = cognom1;
    }

    public void setCognom2(String cognom2) {
        this.cognom2 = cognom2;
    }

    public void setAdreca(String adreca) {
        this.adreca = adreca;
    }

    public void setPoblacio(String poblacio) {
        this.poblacio = poblacio;
    }

    public void setSexe(int sexe) {
        this.sexe = sexe;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public Iterator<Cita> iteCitesPersona() {
        return Collections.unmodifiableCollection(cites).iterator();
    }

    public boolean addCitaPersona(Cita e) {
        if (e==null) {
            throw new RuntimeException("Intent d'afegir cites null");
        }
        if (cites.contains(e)) {
            return false;
        } else {
            cites.add(e);
            return true;
        }
    }

    public boolean removeCitaPersona(Cita e) {
        if (e==null) {
            throw new RuntimeException("Intent d'eliminar autor null");
        }
        return cites.remove(e);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.codì;
        hash = 89 * hash + Objects.hashCode(this.Nif);
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
        final Persona other = (Persona) obj;
        if (this.codì != other.codì) {
            return false;
        }
        return Objects.equals(this.Nif, other.Nif);
    }

    @Override
    public String toString() {
        return "Persona{" + "cod\u00ec=" + codì + ", Nif=" + Nif + 
                ", nom=" + nom + ", cognom1=" + cognom1 + 
                ", cognom2=" + cognom2 + ", adreca=" + adreca + 
                ", poblacio=" + poblacio + ", sexe=" + sexe + 
                ", login=" + login + ", password=" + password + 
                ", cites=" + cites + '}';
    }
    
    
    
    
    
    
}
