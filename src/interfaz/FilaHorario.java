/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

/**
 *
 * @author Josue
 */

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import mihorario.Materia;

public class FilaHorario implements Serializable{
    
    private String intervaloHora="";
    private String lunes="";
    private String martes="";
    private String miercoles="";
    private String jueves="";
    private String viernes="";
    private String sabado="";
    
    
    public static final DateFormat formato=new SimpleDateFormat("HH:mm");
    
    public FilaHorario(){
    }
    
    public FilaHorario(Date[][] intervaloHora,Materia[] materias,int indice ){
        this.intervaloHora=(setIntervaloHora(intervaloHora,indice));
        for(int i=0;i<materias.length;i++){
            
            if(materias[i]!=null){
                if(i==0){this.lunes=materias[i].toString();}
                if(i==1){this.martes=materias[i].toString();}
                if(i==2){this.miercoles=materias[i].toString();}
                if(i==3){this.jueves=materias[i].toString();}
                if(i==4){this.viernes=materias[i].toString();}
                if(i==5){this.sabado=materias[i].toString();}
            }
            
        }
        
        
        
    }
    
    public  String setIntervaloHora(Date[][] intervaloHora,int indice){
        String hora0=formato.format(intervaloHora[indice][0]);
            String hora1=formato.format(intervaloHora[indice][1]);
            String cadena=hora0+"-"+hora1;
        
        return cadena;
    }

    public String getIntervaloHora() {
        return intervaloHora;
    }

    public String getLunes() {
        return lunes;
    }

    public String getMartes() {
        return martes;
    }

    public String getMiercoles() {
        return miercoles;
    }

    public String getJueves() {
        return jueves;
    }

    public String getViernes() {
        return viernes;
    }

    public String getSabado() {
        return sabado;
    }

   

 
    
    
    
    
    
    
}
