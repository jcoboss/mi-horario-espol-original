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
import java.util.Objects;

/**
 *
 * @author Josue
 */
public class LlaveSemana implements Serializable{
    
    private Date inicioSemana;
    private Date finSemana;
    public final static DateFormat dateFormatFecha = new SimpleDateFormat("dd/MM/yyyy");
    
    public LlaveSemana() {
    }
    
    public LlaveSemana(Date inicioSemana, Date finSemana) {
        this.inicioSemana = inicioSemana;
        this.finSemana = finSemana;
    }
    
    @Override
    public boolean equals(Object obj){
        
        if(obj!=null && obj instanceof LlaveSemana ){
            LlaveSemana ll=(LlaveSemana)obj;
            if(this.inicioSemana.equals(ll.getInicioSemana())&&
                    this.finSemana.equals(ll.getFinSemana())){
                return true;
            }
        }
        return false;
        
    }
    
    @Override
    public String toString(){
        return "Desde: "+dateFormatFecha.format(inicioSemana)
                +"Hasta: "+dateFormatFecha.format(finSemana);
    }
    
    @Override
    public int hashCode(){
        int hash=4;
        hash=hash*Objects.hashCode(inicioSemana);
        hash=hash*Objects.hashCode(finSemana);
        return hash;
        
    }
    
    public Date getInicioSemana() {
        return inicioSemana;
    }

    public void setInicioSemana(Date inicioSemana) {
        this.inicioSemana = inicioSemana;
    }

    public Date getFinSemana() {
        return finSemana;
    }

    public void setFinSemana(Date finSemana) {
        this.finSemana = finSemana;
    }
    
    
    
}
