/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misra.citesmediques.persistence;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import misra.citesmediques.Cita;
import misra.citesmediques.Especialitats;
import misra.citesmediques.Metge;
import misra.citesmediques.Persona;

/**
 *
 * @author misra
 */
public class EPCM_JPA implements IGestorCitesMediques{
    
    private EntityManager em;

    /**
     * Constructor que estableix connexió amb el servidor a partir de les dades
     * informades en fitxer de propietats de nom EPcM_JPA.properties.
     *
     * @throws GestorCitesMediquesException si hi ha algun problema en el fitxer de
     * configuració
     */
    public EPCM_JPA() {
        this("EPJPA.properties");
    }

    /**
     * Constructor que estableix connexió amb el servidor a partir de les dades
     * informades en fitxer de propietats, i en cas de ser null cercarà el
     * fitxer de nom EPCM_JPA.proprties
     *
     * @param nomFitxerPropietats
     * @throws GestorCitesMediquesException si hi ha algun problema en el fitxer de
     * configuració o si no s'estableix la connexió
     */
    public EPCM_JPA(String nomFitxerPropietats) {
        if (nomFitxerPropietats == null || nomFitxerPropietats.equals("")) {
            nomFitxerPropietats = "EPCM_JPA.properties";
        }
        Properties props = new Properties();
        try {
            props.load(new FileReader(nomFitxerPropietats));
        } catch (FileNotFoundException ex) {
            throw new GestorCitesMediquesException("No es troba fitxer de propietats", ex);
        } catch (IOException ex) {
            throw new GestorCitesMediquesException("Error en carregar fitxer de propietats", ex);
        }
        String up = props.getProperty("up");
        if (up == null) {
            throw new GestorCitesMediquesException("Fitxer de propietats no conté propietat obligatòria <up>");
        }

        EntityManagerFactory emf = null;
        try {
            emf = Persistence.createEntityManagerFactory(up);
            em = emf.createEntityManager();
        } catch (Exception ex) {
            if (emf != null) {
                emf.close();
            }
            throw new GestorCitesMediquesException("Error en crear EntityManagerFactory o EntityManager", ex);
        }
    }

    @Override
    public void closeCapa() {
        if (em != null) {
            EntityManagerFactory emf = null;
            try {
                emf = em.getEntityManagerFactory();
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                em.close();
            } catch (Exception ex) {
                throw new GestorCitesMediquesException("Error en tancar la connexió", ex);
            } finally {
                em = null;
                if (emf != null) {
                    emf.close();
                }
            }
        }    
    }

    @Override
    public void commit() {
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw new GestorCitesMediquesException("Error en fer commit.", ex);
        }    
    }

    @Override
    public Persona getPersona(int codi) {
        if (codi <= 0) {
            throw new GestorCitesMediquesException("Intent de recuperar PERSONA amb codi " + codi + " erroni");
        }
        return em.find(Persona.class, codi);    
    }

    @Override
    public Metge getMetge(int codi) {
        if (codi <= 0) {
            throw new GestorCitesMediquesException("Intent de recuperar Metge amb codi " + codi + " erroni");
        }
        return em.find(Metge.class, codi);      
    }

    @Override
    public Cita getCita(int codi) {
        if (codi <= 0) {
            throw new GestorCitesMediquesException("Intent de recuperar Cita amb codi " + codi + " erroni");
        }
        return em.find(Cita.class, codi);      
    }
    
    @Override
    public Especialitats getEspecialitat(int codi) {
        if (codi <= 0) {
            throw new GestorCitesMediquesException("Intent de recuperar Especialitat amb codi " + codi + " erroni");
        }
        return em.find(Especialitats.class, codi);
    }

    @Override
    public boolean citaOberta(int codi) {
        if (codi <= 0) {
            throw new GestorCitesMediquesException("Intent de recuperar cita amb codi " + codi + " erroni");
        }
        Cita c = em.find(Cita.class, codi);
        if (c.isEsOberta())return true;
        return false;
                      
    }

    @Override
    public void tancaCita(int codi) {
        if (codi <= 0) {
            throw new GestorCitesMediquesException("Intent de recuperar cita amb codi " + codi + " erroni");
        }
        Cita c = em.find(Cita.class, codi);
        if (c.isEsOberta())c.setEsOberta(false);
          
    }

    @Override
    public boolean redactaInformeCita(int codiCita, String text) {
        if (codiCita <= 0) {
            throw new GestorCitesMediquesException("Intent de recuperar cita amb codi " + codiCita + " erroni");
        }
        Cita c = em.find(Cita.class, codiCita);
        if (c.isEsOberta()){
            c.setInfrome(text);
            return true;
        }    
        return false;
    }

    @Override
    public List<Cita> getCitesMetge(int codiMetge) {
        if (codiMetge <= 0) {
            throw new GestorCitesMediquesException("Intent de recuperar metge amb codi " + codiMetge + " erroni");
        }
        Metge m = em.find(Metge.class, codiMetge);
        if (m!=null){
            TypedQuery<Cita> query = em.createQuery(
            "SELECT c FROM Cita c WHERE c.metge.codiMetge = :codiMetge", Cita.class);
        query.setParameter("codiMetge", codiMetge);
        return query.getResultList();
        }    
        return null;    
    }

    @Override
    public List<Cita> getCitesPersona(int codiPersona) {
        if (codiPersona <= 0) {
            throw new GestorCitesMediquesException("Intent de recuperar persona amb codi " + codiPersona + " erroni");
        }
        Persona p = em.find(Metge.class, codiPersona);
        if (p!=null){
            TypedQuery<Cita> query = em.createQuery(
            "SELECT c FROM Cita c WHERE c.persona.codi = :codiPersona", Cita.class);
        query.setParameter("codiPersona", codiPersona);
        return query.getResultList();
        }    
        return null;     
    }

    @Override
    public boolean anularCita(int codiPersona, int codiCita) {
        Persona p = getPersona(codiPersona);
        if (p!=null){
            Iterator<Cita> iteCitas = p.iteCitesPersona();
            while (iteCitas.hasNext()) {
                Cita cita = iteCitas.next();
                if (cita.getCodiCíta() == codiCita){
                    p.removeCitaPersona(cita);
                    System.out.print("Cita eliminada"+cita.getCodiCíta()+" amb data "+cita.getDataCita());
                    return true;
                }
                    
            }
        } else {
            throw new GestorCitesMediquesException("Intent de recuperar persona amb codi " + codiPersona + " erroni");
        }
        return false;
    }


    @Override
    public boolean loginPersona(int codiPersona) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void rollback() {
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            em.getTransaction().rollback();
        } catch (Exception ex) {
            throw new GestorCitesMediquesException("Error en fer rollback.", ex);
        }    
    }

    @Override
    public Cita concertarCita(int codiPersona, int codiMetge, int codiEspecialitat, Date data) {
        Persona persona = getPersona(codiPersona);
        Metge metge = getMetge(codiMetge);
        Especialitats especialitat = getEspecialitat(codiEspecialitat);
        Cita cita = null;
        if (persona!=null){
            Iterator<Cita> iteCitas = persona.iteCitesPersona();
            while (iteCitas.hasNext()) {
                cita = iteCitas.next();
                if (cita.getDataCita()== data && cita.getMetge().equals(metge)){
                    throw new GestorCitesMediquesException("Ja existeix una cita per aquesta data " + data + " i aquest metge"+metge);
                } else {
                    cita = new Cita(data, persona, metge, especialitat, true);
                    em.persist(cita);
                }
                    
            }
        } else {
            throw new GestorCitesMediquesException("Intent de recuperar persona amb codi " + persona + " erroni");
        }
        
        return cita;
    }

    
    
}
