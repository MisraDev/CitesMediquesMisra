/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misra.citesmediques;

/**
 *
 * @author misra
 */
public class PersonaException extends RuntimeException {
    
    public PersonaException(String message) {
        super(message);
    }

    public PersonaException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
