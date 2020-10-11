/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mihorario;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Josue
 */
public class Clase implements Serializable{
    
    
    private Dias dia;
    private Date horaInicio;
    private Date horaFin;
    private String aula;
    private String detalleAula;
    
    public final static DateFormat dateFormatHora = new SimpleDateFormat("HH:mm");
    
    public Clase(){
        
    }

    public Clase(Dias dia, Date horaInicio, Date horaFin) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }
    
    

    public Clase(Dias dia, Date horaInicio, Date horafin, String aula, String detalleAula) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horafin;
        this.aula = aula;
        this.detalleAula = detalleAula;
    }

    public Dias getDia() {
        return dia;
    }
    
    public String getDiaString(){
        return dia.toString();
    }

    public void setDia(Dias dia) {
        this.dia = dia;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHorafin() {
        return horaFin;
    }

    public void setHorafin(Date horafin) {
        this.horaFin = horafin;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getDetalleAula() {
        return detalleAula;
    }

    public void setDetalleAula(String detalleAula) {
        this.detalleAula = detalleAula;
    }

    @Override
    public String toString() {
        return "Clase{" + "dia=" + dia + ", horaInicio=" + horaInicio + ", horafin=" + horaFin + '}';
    }
    
    public String getInfo(){
        String retorno="";
        
        String diaString=dia.toString();
        String horaIString=dateFormatHora.format(horaInicio);
        String horaFString=dateFormatHora.format(horaFin);
        
        retorno=diaString+"|"+horaIString+" a "+horaFString+"|"+aula;
        
        return retorno;
    }
    
    
    
}
