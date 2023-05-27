/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misra.citesmediques.persistence;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.Query;
import misra.citesmediques.Cita;
import misra.citesmediques.CitaFormat;
import misra.citesmediques.Especialitats;
import misra.citesmediques.Metge;
import misra.citesmediques.Persona;

/**
 *
 * @author misra
 */
public class EPCM_JDBC implements IGestorCitesMediques {
    
    private Connection con;
    
    // Clonsultes parametritzades
    PreparedStatement psLogin = null;    // cercar login i passw
    PreparedStatement psCites = null;
    PreparedStatement psCitesF = null;// retorna totes les cites d'una persona
    PreparedStatement psPers = null;    // especialitats disponibles
    PreparedStatement psMetg = null;  // forats disponibles de metge per especialitat
    PreparedStatement psEspec = null;  // concerta visita per un metge y una especialitat y una data
    PreparedStatement psCita = null;  // Eliminar cap dels empleats que el tenen per cap
    PreparedStatement psDelCita = null;  // Actualitzar cap d'empleats amb un determinat cap
    PreparedStatement psInsertCita = null;  // Comprovar si 
    PreparedStatement psMetgeXEsp = null;  // Comprovar si 
    PreparedStatement psForats = null;  // Comprovar si 
    PreparedStatement psAgenda = null;  // Comprovar si 
    PreparedStatement psDiesSemana = null;  // Comprovar si
    PreparedStatement psCitaMetgeEspDia = null;
    
    /**
     * Constructor que estableix connexió amb el servidor a partir de les dades
     * informades en fitxer de propietats de nom EPJDBC.properties.
     *
     *
     */
    public EPCM_JDBC() {
        this("EPCM_JDBC.properties");
    }

    /**
     * Constructor que estableix connexió amb el servidor a partir de les dades
     * informades en fitxer de propietats, i en cas de ser null cercarà el
     * fitxer de nom EPBaseX.properties.
     *
     * @param nomFitxerPropietats
     * 
     */
    public EPCM_JDBC(String nomFitxerPropietats) {
        if (nomFitxerPropietats == null || nomFitxerPropietats.equals("")) {
            nomFitxerPropietats = "EPJDBC.properties";
        }
        Properties props = new Properties();
        try {
            props.load(new FileReader(nomFitxerPropietats));
        } catch (FileNotFoundException ex) {
            throw new GestorCitesMediquesException("No es troba fitxer de propietats", ex);
        } catch (IOException ex) {
            throw new GestorCitesMediquesException("Error en carregar fitxer de propietats", ex);
        }

        String url = props.getProperty("url");
        String user = props.getProperty("user");
        String password = props.getProperty("password");
        if (url == null || user == null || password == null) {
            throw new GestorCitesMediquesException("El fitxer de propietats no incorpora totes les propietats necessàries: url,user,password");
        }

        try {
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Conexio establerta");
        } catch (SQLException ex) {
            throw new GestorCitesMediquesException("No es pot establir la connexió", ex);
        }
        
        try {
            try {
                con.setAutoCommit(false);
            } catch (SQLException ex) {
                throw new GestorCitesMediquesException("No s'ha pogut desactivar l'autocommit", ex);
            }
            // Creació dels diversos PreparedStatement
            try {
                String cadena = "select per_codi, per_passw from persona "
                        + "where per_login = ?";
                psLogin = con.prepareStatement(cadena);
            } catch (SQLException ex) {
                throw new GestorCitesMediquesException("Error en preparar sentència psLogin", ex);
            }
            try {
                String cadena = "select * from cita c where c.cit_codi_persona = ?";
                psCites = con.prepareStatement(cadena);
            } catch (SQLException ex) {
                throw new GestorCitesMediquesException("Error en preparar sentència psCites", ex);
            }
            try {
                String cadena = "select c.cit_codi as codi,DATE(c.cit_data_hora)as data, TIME(cit_data_hora)as hora, e.esp_nom as especialitat , p.per_nom as nom_metge\n" +
                    "from cita c\n" +
                    "join especialitat e on e.esp_codi = c.cit_codi_esp \n" +
                    "join persona p on p.per_codi  = c.cit_codi_empleat \n" +
                    "where c.cit_codi_persona = ?\n" +
                    "order by 2,3 asc";
                psCitesF = con.prepareStatement(cadena);
            } catch (SQLException ex) {
                throw new GestorCitesMediquesException("Error en preparar sentència psCites", ex);
            }
            try {
                String cadena = "select * from persona where per_codi = ?";
                psPers = con.prepareStatement(cadena);
            } catch (SQLException ex) {
                throw new GestorCitesMediquesException("Error en preparar sentència psPers", ex);
            }
            try {
                String cadena = "select * from metge where met_codi = ?";
                psMetg = con.prepareStatement(cadena);
            } catch (SQLException ex) {
                throw new GestorCitesMediquesException("Error en preparar sentència psMetg", ex);
            }
            try {
                String cadena = "select * from especialitat where esp_codi = ?";
                psEspec = con.prepareStatement(cadena);
            } catch (SQLException ex) {
                throw new GestorCitesMediquesException("Error en preparar sentència psEspec", ex);
            }
            try {
                String cadena = "select * from cita where cit_codi = ?";
                psCita = con.prepareStatement(cadena);
            } catch (SQLException ex) {
                throw new GestorCitesMediquesException("Error en preparar sentència psCita", ex);
            }
            try {
                String cadena = "delete from cita where cit_codi = ?";
                psDelCita = con.prepareStatement(cadena);
            } catch (SQLException ex) {
                throw new GestorCitesMediquesException("Error en preparar sentència psDelCita", ex);
            }
            try {
                String cadena = "INSERT INTO cita "
                        + "(cit_data_hora, cit_codi_persona, cit_codi_empleat,cit_codi_esp, cit_informe, cit_es_oberta) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";
                psInsertCita = con.prepareStatement(cadena);
            } catch (SQLException ex) {
                throw new GestorCitesMediquesException("Error en preparar sentència psUpdCap", ex);
            }
            try {
                String cadena = "select me_met_codi from metge_especialitat where me_esp_codi  = ?";
                psMetgeXEsp = con.prepareStatement(cadena);
            } catch (SQLException ex) {
                throw new GestorCitesMediquesException("Error en preparar sentència psDelCita", ex);
            }
            try {
                String cadena = "select TIME(c.cit_data_hora) as hora_cita \n" +
                                "from cita c\n" +
                                "where c.cit_codi_empleat = ? and DATE(c.cit_data_hora) = ? and c.cit_codi_esp = ?";
                psForats = con.prepareStatement(cadena);
            } catch (SQLException ex) {
                throw new GestorCitesMediquesException("Error en preparar sentència psDelCita", ex);
            }
            try {
                String cadena = "select m.am_hora as agenda from agenda_metges m \n" +
                                "where m.am_dia_setmana = ? and m.am_esp_codi = ? and m.am_me_codi = ?";
                psAgenda = con.prepareStatement(cadena);
            } catch (SQLException ex) {
                throw new GestorCitesMediquesException("Error en preparar sentència psDelCita", ex);
            }
            try {
                String cadena = "select DISTINCT am.am_dia_setmana from agenda_metges am where am.am_me_codi = ? and am.am_esp_codi = ?";
                psDiesSemana = con.prepareStatement(cadena);
            } catch (SQLException ex) {
                throw new GestorCitesMediquesException("Error en preparar sentència psDelCita", ex);
            }
            try {
                String cadena = "select * from cita c where c.cit_codi_empleat = ? and c.cit_codi_esp = ? and c.cit_data_hora = ?";
                psCitaMetgeEspDia = con.prepareStatement(cadena);
            } catch (SQLException ex) {
                throw new GestorCitesMediquesException("Error en preparar sentència psDelCita", ex);
            }
        } catch (Exception ex) {
            try {
                con.close();
            } catch (SQLException ex1) {
            }
        }
    }


    @Override
    public void closeCapa() {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException ex) {
            }
            try {
                con.close();
            } catch (SQLException ex) {
                throw new GestorCitesMediquesException("Error en tancar la connexió", ex);
            } finally {
                con = null;
            }
        }
    }

    @Override
    public void commit() {
        try {
            con.commit();
        } catch (SQLException ex) {
            throw new GestorCitesMediquesException("Error en fer commit", ex);
        }
    }

    @Override
    public Persona getPersona(int codi) {
        Persona p;
        try {
            psPers.setInt(1, codi);
            ResultSet rs = psPers.executeQuery();
            if (!rs.next()) {
                return null;
            }else{
                
                p = new Persona(
                        rs.getString("per_nif"), 
                        rs.getString("per_nom"), 
                        rs.getString("per_cognom1"), 
                        rs.getString("per_cognom2"), 
                        rs.getString("per_adreca"), 
                        rs.getString("per_poblacio"), 
                        rs.getInt("per_sexe"), 
                        rs.getString("per_login"), 
                        rs.getString("per_passw"), 
                        rs.getBoolean("per_es_metge"));
                p.setCodi(rs.getInt("per_codi"));
                return p;
            }
        } catch (SQLException ex) {
            throw new GestorCitesMediquesException("Error al trobar persona", ex);
        }
    }

    @Override
    public Metge getMetge(int codi) {
        /*
        Metge m;
        try {
            psMetg.setInt(1, codi);
            ResultSet rs = psMetg.executeQuery();
            if (!rs.next()) {
                return null;
            }else{
                m = new Metge(
                        rs.getInt("met_codi_empleat"));
                
            }
        } catch (SQLException ex) {
            throw new GestorCitesMediquesException("Error al trobar empleat", ex);
        }
        */
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    @Override
    public List<Especialitats> getEspecialitatsMetge(int codiMetge) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Metge> getMetges() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void addEspecialitatMetge(Especialitats especialitat, int codiMetge) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void removeEspecialitatMetge(Especialitats especialitat, int codiMetge) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Cita getCita(int codi) {
        
        Cita c;
        try {
            psCita.setInt(1, codi);
            ResultSet rs = psCita.executeQuery();
            if (!rs.next()) {
                return null;
            }else{
                c = new Cita(
                        rs.getTimestamp("cit_data_hora"),
                        getPersona(rs.getInt("cit_codi_persona")),
                        getPersona(rs.getInt("cit_codi_empleat")),
                        getEspecialitat(rs.getInt("cit_codi_esp")),
                        rs.getString("cit_informe"),
                        rs.getBoolean("cit_es_oberta"));
                c.setCodiCita(rs.getInt("cit_codi"));
                return c;
            }
        } catch (SQLException ex) {
            throw new GestorCitesMediquesException("Error al trobar cita", ex);
        }
    }

    @Override
    public Especialitats getEspecialitat(int codi) {
        Especialitats e;
        try {
            psEspec.setInt(1, codi);
            ResultSet rs = psEspec.executeQuery();
            if (!rs.next()) {
                return null;
            }else{
                e = new Especialitats(
                        rs.getString("esp_nom"));
                e.setCodi(rs.getInt("esp_codi"));
                return e;
            }
        } catch (SQLException ex) {
            throw new GestorCitesMediquesException("Error al trobar persona", ex);
        }
    }

    @Override
    public List<Especialitats> getEspecialitats() {
        List<Especialitats> llistaEspecialitats = new ArrayList();
        String cadena = "select * from especialitat";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(cadena);
            while (rs.next()) {
                    int codi = rs.getInt("esp_codi");
                    String nomEsp = rs.getString("esp_nom");                
                    Especialitats especialitat = new Especialitats(nomEsp );
                    especialitat.setCodi(codi);
                    llistaEspecialitats.add(especialitat);
                } 
            if(llistaEspecialitats.isEmpty()){
                return null;
            }
            
        return llistaEspecialitats;
        } catch (SQLException ex) {
            throw new GestorCitesMediquesException("Error en recuperar la llista de especialitats", ex);
        }
    }

    @Override
    public List<Metge> getMetgePerEspecialitats(int codiEsp) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

    }

    @Override
    public List<Time> getForatsDisponibles(int codiEsp, int codiMetge, String data) {
        List <Time> horesOcupades = new ArrayList();
        List <Time> horesAgendaDisp = new ArrayList();
        
        try {
            psForats.setInt(1, codiMetge);
            Date sqlDate = Date.valueOf(data);
            
            psForats.setDate(2, sqlDate);
            psForats.setInt(3, codiEsp);
            
            ResultSet rs = psForats.executeQuery();
            while (rs.next()) {
                    Time horaReservada = rs.getTime("hora_cita");
                    horesOcupades.add(horaReservada);
                }
            
            
        } catch (SQLException ex) {
            throw new GestorCitesMediquesException("Error en fer login", ex);
        }
        String dia = retornadiaSetmana(data);
        try {
            psAgenda.setString(1, dia );
            psAgenda.setInt(2, codiEsp);
            psAgenda.setInt(3, codiMetge);
            
            ResultSet rs = psAgenda.executeQuery();
            
            if(!horesOcupades.isEmpty()){
                while (rs.next()) {
                    Time horaDisponibleAgenda = rs.getTime("agenda");
                    /*for(Time horaOcup: horesOcupades){
                        if(!horaOcup.equals(horaDisponibleAgenda)){
                            horesAgendaDisp.add(horaDisponibleAgenda);
                        }
                    } */
                    horesAgendaDisp.add(horaDisponibleAgenda);
                }
            } else {
                while (rs.next()) {
                    Time horaDisponibleAgenda = rs.getTime("agenda");
                    horesAgendaDisp.add(horaDisponibleAgenda); 
                }

            }
            
            if(horesAgendaDisp.isEmpty()){
                return null;
            }
            
            return horesAgendaDisp;
        } catch (SQLException ex) {
            throw new GestorCitesMediquesException("Error en fer login", ex);
        }
    }

    @Override
    public boolean citaOberta(int codi) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void tancaCita(int codi) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean redactaInformeCita(int codiCita, String text) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Cita> getCitesMetge(int codiMetge) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Cita> getCitesPersona(int codiPersona) {
        List<Cita> llistaCites = new ArrayList();
        try {
            psCites.setInt(1, codiPersona);
            ResultSet rs = psCites.executeQuery();
            while (rs.next()) {
                int codi = rs.getInt("cit_codi");
                Timestamp dataHora = rs.getTimestamp("cit_data_hora");
                int codiPersonac = rs.getInt("cit_codi_persona");
                int codiEmpleatc = rs.getInt("cit_codi_empleat");
                int codiEsp = rs.getInt("cit_codi_esp");
                String informe = rs.getString("cit_informe");
                boolean esOberta = rs.getBoolean("cit_es_oberta");
                Cita cita = new Cita( dataHora, getPersona(codiPersonac), getMetge(codiEmpleatc),getEspecialitat(codiEsp), informe, esOberta );
                cita.setCodiCita(codi);
                llistaCites.add(cita);
            } 
            if (llistaCites.isEmpty()){
                return null;
            }
            
        return llistaCites;
        } catch (SQLException ex) {
            throw new GestorCitesMediquesException("Error en recuperar la llista de cites", ex);
        }
    }
    
    @Override
    public List<CitaFormat> getCitesPersonaF(int codiPersona) {
        List<CitaFormat> llistaCites = new ArrayList();
        try {
            psCitesF.setInt(1, codiPersona);
            ResultSet rs = psCitesF.executeQuery();
            while (rs.next()) {
                int codi = rs.getInt("codi");
                Date data = rs.getDate("data");
                Time hora = rs.getTime("hora");
                String especialitat = rs.getString("especialitat");
                String nomMetge = rs.getString("nom_metge");

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dataString = dateFormat.format(data);

                CitaFormat cita = new CitaFormat(codi, dataString, hora, especialitat, nomMetge );
                llistaCites.add(cita);
            }
            if (llistaCites.isEmpty()){
                return null;
            }
            
        return llistaCites;
        } catch (SQLException ex) {
            throw new GestorCitesMediquesException("Error en recuperar la llista de cites formatades", ex);
        }    
    }
    
    @Override
    public List<Object> getCitesPersonaAndoid(int codiPersona) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody

        /*
        List<Object> llistaCites = new ArrayList();
        try {
            psCites.setInt(1, codiPersona);
            ResultSet rs = psCites.executeQuery();
            if (!rs.next()) {
                return null;
            }
            while (rs.next()) {
                String data = rs.getString("data");
                String hora = rs.getString("hora");
                String nomEspecialitat = rs.getString("nom_especialitat");
                String nomMetge = rs.getString("nom_metge");
                Object cita = new Object(data, hora, nomEspecialitat, nomMetge);
            }
        } catch (SQLException ex) {
            throw new GestorCitesMediquesException("Error en fer login", ex);
        }
*/
    }

    @Override
    public boolean anularCita( int codiCita) {
        Cita cita = getCita(codiCita);
        if(cita==null){
            return false;
        }else{
            
            try {
                psDelCita.setInt(1, codiCita);
                psDelCita.executeUpdate();
                return true;
            } catch (SQLException ex) {
                throw new GestorCitesMediquesException("Error en eliminar cita amb codi " + codiCita, ex);
            }
            
        }
    }

    @Override
    public boolean concertarCita(int codiPersona, int codiMetge, int codiEspecialitat, Timestamp data) {
        boolean trobada = false;
        //primero buscamos una cita con la misma persona, metge y data
        try{
            psCitaMetgeEspDia.setInt(1, codiMetge);
            psCitaMetgeEspDia.setInt(2, codiEspecialitat);
            psCitaMetgeEspDia.setTimestamp(3, data);
            ResultSet rs = psCitaMetgeEspDia.executeQuery();
            if(!rs.next()){
                trobada = false;
            } else{
                trobada = true;
            }
        }catch(SQLException e){
        
        }
        
        //Aqui hay que restringir que un medico pueda pedir cita a el mismo
        boolean ok = false;
        if(codiPersona!=codiMetge && !trobada){
            try {
                psInsertCita.setTimestamp(1, data);
                psInsertCita.setInt(2, codiPersona);
                psInsertCita.setInt(3, codiMetge);
                psInsertCita.setInt(4, codiEspecialitat);
                psInsertCita.setString(5, "");
                psInsertCita.setBoolean(6, true);
                psInsertCita.executeUpdate();
                ok = true;
                return ok;
            } catch (SQLException ex) {
                throw new GestorCitesMediquesException("Error en insertar Cita amb codi ", ex);
            }
        }
        return ok;
    }

   

    @Override
    public void rollback() {
        try {
            con.rollback();
        } catch (SQLException ex) {
            throw new GestorCitesMediquesException("Error en fer rollback", ex);
        }
    }

    @Override
    public int loginPersona(String loginPersona, String passwPersona) {
        try {
            psLogin.setString(1, loginPersona);
            ResultSet rs = psLogin.executeQuery();
            if (!rs.next()) {
                return 0;
            } else {
                String passw = rs.getString("per_passw");
                int codiPersona = rs.getInt("per_codi");
                if(passw.equals(passwPersona)){
                    return codiPersona;
                }
                return 0;
            }
        } catch (SQLException ex) {
            throw new GestorCitesMediquesException("Error en fer login", ex);
        }
        
    }

    private String retornadiaSetmana(String data) {
        String result;
        //SimpleDateFormat dateF = new SimpleDateFormat("yyyy-MM-dd");
        Date sqlDate = Date.valueOf(data);
        //Date diaDate = (Date) dateF.parse(data);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sqlDate);
        int diaSetmana = calendar.get(Calendar.DAY_OF_WEEK);
        switch(diaSetmana) {
            case Calendar.MONDAY:
                result = "LUNES";
                //System.out.println("LUNES");
                break;
            case Calendar.TUESDAY:
                result = "MARTES";
                break;
            case Calendar.WEDNESDAY:
                result = "MIERCOLES";
                break;
            case Calendar.THURSDAY:
                result = "JUEVES";
                break;
            case Calendar.FRIDAY:
                result = "VIERNES";
                break;
            case Calendar.SATURDAY:
                result = "SABADO";
                break;
            case Calendar.SUNDAY:
                result = "DOMINGO";
                break;
            default:
                result = "Día no válido";
                break;
        }
        return result;
    }

    @Override
    public int getCodiEspecialitatPerNom(String nomEsp) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Persona> getMetgesPerEspecialitatJDBC(int codiEsp) {
        List<Persona> llistaMetges = new ArrayList();
        try {
            psMetgeXEsp.setInt(1, codiEsp);
            ResultSet rs = psMetgeXEsp.executeQuery();
            while (rs.next()) {
                int codiMetgeEsp = rs.getInt("me_met_codi");
                Persona metge = getPersona(codiMetgeEsp);
                llistaMetges.add(metge);
            }
            if (llistaMetges.isEmpty()) {
                return null;
            }
            
        return llistaMetges;
        } catch (SQLException ex) {
            throw new GestorCitesMediquesException("Error en recuperar metge per especialitat", ex);
        }
    }

    @Override
    public List<String> diasSemanaDisponibles(int codiMetge, int codiEspecialitat) {
        List<String> llistaDiesSemana = new ArrayList();
        try {
            psDiesSemana.setInt(1, codiMetge);
            psDiesSemana.setInt(2, codiEspecialitat);
            ResultSet rs = psDiesSemana.executeQuery();
            while (rs.next()) {
                String diaSemana = rs.getString("am_dia_setmana");
                llistaDiesSemana.add(diaSemana);
            }
            if (llistaDiesSemana.isEmpty()) {
                return null;
            }
            
        return llistaDiesSemana;
        } catch (SQLException ex) {
            throw new GestorCitesMediquesException("Error en recuperar dies de la semana disponibles", ex);
        }
    }

    @Override
    public Cita getCitaAndroid(int codiMetge, int codiEsp, Timestamp diaHora) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    

    

    
    
}
