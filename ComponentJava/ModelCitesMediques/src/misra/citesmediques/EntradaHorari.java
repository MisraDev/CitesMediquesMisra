/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misra.citesmediques;

import java.sql.Date;

/**
 *
 * @author misra
 */
public class EntradaHorari {
    
    //id 3 id primarias
    private Date hora;
    //id
    
    //id
    //ManyToMany
    private Metge metge;
    //ID
    //ManytoMany
    private DiesSetmana diaSetmana;
    
    //ManyToOne
    private Especialitats especialitat;
    
}
