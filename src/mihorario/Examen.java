/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mihorario;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Josue
 */
public class Examen implements Serializable {
    
    private Date fecha;
    private Date horaInicio;
    private Date horaFin;
    private String aula;
    private String detalleAula;
    public  ExamenCategoria cat;
    
    public final static DateFormat dateFormatFecha = new SimpleDateFormat("dd/MM/yyyy");
    public final static DateFormat dateFormatHora = new SimpleDateFormat("HH:mm");
   
    
            

    public Examen() {
    }

    public Examen(Date fecha, Date horaInicio, Date horaFin,ExamenCategoria cat) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.cat=cat;
    }
    
    

    public Examen(Date fecha, Date horaInicio, Date horafin, String aula, String detalleAula) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horafin;
        this.aula = aula;
        this.detalleAula = detalleAula;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
        return "Examen{" + "fecha=" + fecha + ", horaInicio=" + horaInicio + ", horaFin=" + horaFin + ", cat=" + cat + '}';
    }
    
    public String getInfo(){
        String retorno="";
        String catString=cat.toString();
        String fechaString=dateFormatFecha.format(fecha);
        String horaIString=dateFormatHora.format(horaInicio);
        String horaFString=dateFormatHora.format(horaFin);
        
        retorno=catString+"|"+fechaString+"-"+horaIString+" a "+horaFString;
        return retorno;
    }
    
    
    
    
    
}
