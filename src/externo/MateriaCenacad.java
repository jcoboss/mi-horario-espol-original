/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package externo;

/**
 *
 * @author Josue
 */
public class MateriaCenacad {
    
    String nombre="";
    double promedio=0.0f;
    short año=0;
    
    public MateriaCenacad(){
        
    }
    
    public MateriaCenacad(String nombre, double promedio,short año){
        this.nombre=nombre;
        this.promedio=promedio;
        this.año=año;
        
    }
    
    public String toString(){
        return nombre+" "+promedio+" "+año;
        
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public short getAño() {
        return año;
    }

    public void setAño(short año) {
        this.año = año;
    }
    
    

    
    
}
