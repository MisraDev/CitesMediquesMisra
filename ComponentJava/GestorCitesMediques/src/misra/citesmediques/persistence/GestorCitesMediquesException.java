/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misra.citesmediques.persistence;

/**
 *
 * @author misra
 */
public class GestorCitesMediquesException extends RuntimeException{
    
    public GestorCitesMediquesException(String message) {
        super(message);
    }

    public GestorCitesMediquesException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
