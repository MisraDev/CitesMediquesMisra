/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misra.citesmediques.persistence;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import misra.citesmediques.Cita;
import misra.citesmediques.CitaFormat;
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
    public Metge getMetge(int codi) {
        if (codi <= 0) {
            throw new GestorCitesMediquesException("Intent de recuperar Metge amb codi " + codi + " erroni");
        }
        return em.find(Metge.class, codi);      
    }
    
    @Override
    public List<Metge> getMetges() {
        String consulta = "Select p from Persona p Where p.esMetge = true";
        Query q = em.createQuery(consulta);
        List<Metge> mList = q.getResultList();
        return mList;
        
    }
    
    @Override
    public List<Especialitats> getEspecialitatsMetge(int codiMetge) {
        List<Especialitats> espList = new ArrayList();
        Metge m = getMetge(codiMetge);
        if (m!=null){
            Iterator<Especialitats> iteEspecialitats = m.iteEspecialitats();
            while (iteEspecialitats.hasNext()) {
                Especialitats esp = iteEspecialitats.next();
                espList.add(esp);  
            }
        } else {
            throw new GestorCitesMediquesException("Intent de recuperar metge amb codi " + codiMetge + " erroni");
        }
        return espList;
    }

    @Override
    public void addEspecialitatMetge(Especialitats especialitat, int codiMetge) {
        
       Metge m = getMetge(codiMetge);
       if(m!=null){
           if (!m.addEspecialitat(especialitat)){
                throw new GestorCitesMediquesException("Intent de incloure especialitat al Metge amb codi " + codiMetge + " erroni");
           } else{
               
               System.out.print("Especialitat"+especialitat.getNomEspecialitat()+"introduida");
           }
       }
    }

    @Override
    public void removeEspecialitatMetge(Especialitats especialitat, int codiMetge) {
        Metge m = getMetge(codiMetge);
       if(m!=null){
           if (!m.removeEspecialitat(especialitat)){
                throw new GestorCitesMediquesException("Intent de eliminare especialitat al Metge amb codi " + codiMetge + " erroni");
           } else{
               System.out.print("Especialitat"+especialitat.getNomEspecialitat()+"eliminada");
           }
       }
        
    }

        
    //_-------------------------------------------------Unsuported
    @Override
    public Cita getCita(int codi) {
        /*
        if (codi <= 0) {
            throw new GestorCitesMediquesException("Intent de recuperar Cita amb codi " + codi + " erroni");
        }
        return em.find(Cita.class, codi);    
        */
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }
    
    //
    
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
            em.persist(c);
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
    public boolean anularCita( int codiCita) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }
/*
    
    @Override
    public concertarCita(int codiPersona, int codiMetge, int codiEspecialitat, Date data) {
        /*
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
        
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }
*/

    @Override
    public List<Especialitats> getEspecialitats() {
        String consulta = "Select e from Especialitats e";
        Query q = em.createQuery(consulta);
        List<Especialitats> eList = q.getResultList();
        return eList;
        
    }

    @Override
    public List<Metge> getMetgePerEspecialitats(int codiEsp) {
        TypedQuery<Metge> query = em.createQuery(
            "Select m from Metge m join m.especialitatsMetge e where e.codi = :codiEsp", Metge.class);
        query.setParameter("codiEsp", codiEsp);
        return query.getResultList();
    }


    @Override
    public int loginPersona(String loginPersona, String passwPersona) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Object> getCitesPersonaAndoid(int codiPersona) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    

    @Override
    public List<Time> getForatsDisponibles(int codiEsp, int codiMetge, String data) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getCodiEspecialitatPerNom(String nomEsp) {
        if (nomEsp.equals("")) {
            throw new GestorCitesMediquesException("Intent de recuperar especialitat per nom erroni");
        }
        TypedQuery<Integer> query = em.createQuery(
        "SELECT e.codi FROM Especialitats e WHERE e.nomEspecialitat = :nomEspe", Integer.class);
        query.setParameter("nomEspe", nomEsp);
        Integer result = query.getSingleResult();
        return result != null ? result : 0; 
    }

    @Override
    public List<Persona> getMetgesPerEspecialitatJDBC(int codiEsp) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<CitaFormat> getCitesPersonaF(int codiPersona) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean concertarCita(int codiPersona, int codiMetge, int codiEspecialitat, Timestamp data) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
}
