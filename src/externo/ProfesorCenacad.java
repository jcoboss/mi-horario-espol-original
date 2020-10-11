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
public class ProfesorCenacad {
    
     private String nombres="";
     private String apellidos="";
     private String link="";
     private String nombreCompleto="";

    public ProfesorCenacad() {
    }

    public ProfesorCenacad(String apellidos,String nombres, String link){
        this.apellidos=apellidos;
        this.nombres=nombres;
        this.link=link;
        this.nombreCompleto=apellidos+" "+nombres;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String toString(){
        return nombreCompleto;
    }
    
}
