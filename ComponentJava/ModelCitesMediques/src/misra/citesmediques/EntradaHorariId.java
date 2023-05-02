/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misra.citesmediques;

import java.sql.Time;
import javax.persistence.Column;

/**
 *
 * @author misra
 */
public class EntradaHorariId {
    
    @Column(name = "id_medico")
    private int idMedico;
    
    @Column(name = "dia_setmana")
    private DiesSetmana diaSemana;
    
    @Column(name = "hora")
    private Time hora;
    
}
