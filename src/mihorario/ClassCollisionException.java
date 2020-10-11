/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mihorario;

import java.io.Serializable;

/**
 *
 * @author Josue
 */
public class ClassCollisionException extends Exception implements Serializable{

    public ClassCollisionException() {
        
    }
    
    public ClassCollisionException(String message){
        super(message);
        
    }
    
    
    
}
