/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package misra.citesmediques;

/**
 *
 * @author misra
 */
public class MetgeException extends RuntimeException{
    public MetgeException(String message) {
        super(message);
    }

    public MetgeException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
