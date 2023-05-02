/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misra.citesmediques.persistence;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;
import misra.citesmediques.Cita;
import misra.citesmediques.Especialitats;
import misra.citesmediques.Metge;
import misra.citesmediques.Persona;

/**
 *
 * @author misra
 */
public interface IGestorCitesMediques {
    
    /**
     * Tanca la capa de persistència, tancant la connexió amb la BD.
     *
     * @throws EPSednaException si hi ha algun problema en tancar la connexió
     */
    void closeCapa();

    /**
     * Tanca la transacció activa validant els canvis a la BD.
     *
     * @throws EPSednaException si hi ha algun problema
     */
    void commit();

    /**
     * Intenta recuperar persona amb codi indicat per paràmetre.
     *
     * @param codi de l'empleat a recuperar
     * @return L'objecte persona trobat o null si no existeix
     * 
     * 
     */
    Persona getPersona(int codi);
    
    /**
     * Intenta recuperar metge amb codi indicat per paràmetre.
     *
     * @param codi de l'empleat a recuperar
     * @return L'objecte metge trobat o null si no existeix
     * 
     * 
     */
    Metge getMetge(int codi);
    
    /**
     * Intenta recuperar cita amb codi indicat per paràmetre.
     *
     * @param codi de l'empleat a recuperar
     * @return L'objecte cita trobat o null si no existeix
     * 
     * 
     */
    Cita getCita(int codi);
    
    /**
     * Intenta recuperar cita amb codi indicat per paràmetre.
     *
     * @param codi de l'empleat a recuperar
     * @return L'objecte cita trobat o null si no existeix
     * 
     * 
     */
    Especialitats getEspecialitat(int codi);
    
    /**
     * Mira si una cita esta oberta.
     *
     * @param codi de l'empleat a recuperar
     * @return true si la cita es pot modificar
     * 
     * 
     */
    boolean citaOberta(int codi);
    
    /**
     * tanca la cita.
     *
     * @param codi de l'empleat a recuperar
     * @return true si la cita es pot modificar
     * 
     * 
     */
    void tancaCita(int codi);

    /**
     * Redacta o modifica informe citra
     * Si la cita esta tancada o ha pasat un dia desde el dia cita renorna false
     * @return true si l'informe s'ha redactat
     * 
     */
    
    boolean redactaInformeCita(int codiCita, String text);
    
    /**
     * devuelce todas las citas de un metge en concret
     * 
     * @return llista cites
     * 
     */
    List<Cita> getCitesMetge(int codiMetge);
    
    /**
     * devuelce todas las citas de un metge en concret
     * 
     * @return llista cites
     * 
     */
    List<Cita> getCitesPersona(int codiPersona);
    
    /**
     * devuelce todas las citas de un metge en concret
     * 
     * @return llista cites
     * 
     */
    boolean anularCita(int codiPersona, int codiCita);
    
    /**
     * devuelce todas las citas de un metge en concret
     * 
     * @return llista cites
     * 
     */
    Cita concertarCita(int codiPersona, int codiMetge, int codiEspecialitat, Date data );
    
    /**
     * devuelce todas las citas de un metge en concret
     * 
     * @return llista cites
     * 
     */
    boolean loginPersona(int codiPersona);
    
    /**
     * devuelce todas las citas de un metge en concret
     * 
     * @return llista cites
     * 
     */
    void rollback();
    
}
