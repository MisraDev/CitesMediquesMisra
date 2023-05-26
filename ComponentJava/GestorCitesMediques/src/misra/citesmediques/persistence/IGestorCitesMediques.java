/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misra.citesmediques.persistence;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import misra.citesmediques.Cita;
import misra.citesmediques.CitaFormat;
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
    
    //-----------------------------
    //---------------------JPA-----------------------------
    //---------------------------------------------------------
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
    
    List<Especialitats> getEspecialitatsMetge(int codiMetge);
    
    List<Metge> getMetges();
    
    void addEspecialitatMetge(Especialitats especialitat, int codiMetge);
    
    void removeEspecialitatMetge(Especialitats especialitat, int codiMetge);
    
    //-------------------------
    //-----------------------c#
    //--------------------------------
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
     * Intenta recuperar cita amb codi indicat per paràmetre.
     *
     * @param codi de l'empleat a recuperar
     * @return L'objecte cita trobat o null si no existeix
     * 
     * 
     */
    int getCodiEspecialitatPerNom(String nomEsp);
    
    /**
     * Busca totes les especialitats a la bd.
     *
     * @return llista especialitats
     * 
     * 
     */
    
    List<Especialitats> getEspecialitats();
    
    /**
     * Busca tots els metges per una especialitat.
     *
     * @return llista Metges
     * @param codiEsp de l'especialitat
     * 
     */
    
    List<Metge> getMetgePerEspecialitats(int codiEsp);
    
    /**
     * Mira quines hores hi han disponibles per un metge y especialitat concreta
     *
     * @param codiEsp de l'empleat a recuperar
     * @param codiMetge de l'empleat a recuperar
     * @param data String DAteformat new SimpleDateFormat("yyyy-MM-dd")
     * @return llista de dates disponibles
     * 
     * 
     */
    
    List<Time> getForatsDisponibles(int codiEsp, int codiMetge, String data);
    
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
     * @param codiPersona
     * 
     * @return llista cites
     * 
     */
    List<Cita> getCitesPersona(int codiPersona);
    
    /**
     * devuelce todas las citas de un metge en concret
     * 
     * @param codiPersona
     * 
     * @return llista cites
     * 
     */
    List<CitaFormat> getCitesPersonaF(int codiPersona);
    
    /**
     * devuelce todas las citas de un metge en concret
     * 
     * @return llista cites
     * 
     */
    List<Object> getCitesPersonaAndoid(int codiPersona);
    
    /**
     * devuelce todas las citas de un metge en concret
     * 
     * @return llista cites
     * 
     */
    boolean anularCita( int codiCita);
    
    /**
     * devuelce todas las citas de un metge en concret
     * 
     * @return llista cites
     * 
     */
    boolean concertarCita(int codiPersona, int codiMetge, int codiEspecialitat, Timestamp data );
    
    List<Persona>getMetgesPerEspecialitatJDBC(int codiEsp);
    
    /**
     * fa le login
     * 
     * @return codi perosna en cas de que el login sigui correcte o 0 si no es correcte
     * 
     */
    int loginPersona(String loginPersona, String passwPersona);
    
    /**
     * devuelce todas los dias de la semana disponibles por metge y especialitat
     * 
     * @return llista dies
     * 
     */
    
    List<String> diasSemanaDisponibles(int codiMetge, int codiEspecialitat);
    
    Cita getCitaAndroid(int codiMetge, int codiEsp, Timestamp diaHora);
    
    
    void rollback();
    
}
