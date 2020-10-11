/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mihorario;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author Josue
 */
public class Horario implements Serializable{
    
    private ArrayList<Materia> materias=new ArrayList<>();
    public Dias[] dias=Horario.diasSemana();
    public Date[][] horas;
    private Materia[][] horario;
    
    transient public final static long unDia=86400000;
    
    public Horario(){
        inicializarHorario(7, 20);
    }
    
    public Horario(ArrayList<Materia> materias){
        this.materias=materias;
        inicializarHorario(7, 20);
        
    }
    
    
    public void inicializarHorario(int desde, int hasta){
        horas=Horario.intervalosHoras(desde, hasta);
        horario=new Materia[horas.length][dias.length];
        
    }
    
    
    public boolean hayEspacioClase(Materia materia,Clase clase) {
        int iInterseccion=0;
        int iVacios=0;
        Date inicio=clase.getHoraInicio();
        Date fin=clase.getHorafin();
        Dias dia=clase.getDia();
        for (int i=0;i<horario.length;i++){
            Date[] referencia=horas[i];
            if (Horario.intersecan(referencia, inicio, fin)){
                for (int j=0;j<horario[i].length;j++){
                    if(dias[j]==dia){
                        iInterseccion++;
                        if(horario[i][j]==null){
                        iVacios++;
                        }
                    }
                }
            }
        }
        if ((iInterseccion!=0 && iVacios!=0)&&(iInterseccion==iVacios)){
            return true;
        }else {
            System.out.println("Puede que esten chocando o no hay clases");
            return false;
        }
    }
    
    public boolean hayEspacioExamen(Materia materia,Examen examen)throws ClassCollisionException {
        int iInterseccion=0;
        int iVacios=0;
        Date inicio=examen.getHoraInicio();
        Date fin=examen.getHorafin();
        Date dia=examen.getFecha();
        for (int i=0;i<horario.length;i++){
            Date[] referencia=horas[i];
            if (Horario.intersecan(referencia, inicio, fin)){
                for (int j=0;j<horario[i].length;j++){
                    if(dias[j]==Horario.getDia(dia)){
                        iInterseccion++;
                        if(horario[i][j]==null){
                        iVacios++;
                        }
                    }
                }
            }
        }
        if ((iInterseccion!=0 && iVacios!=0)&&(iInterseccion==iVacios)){
            return true;
        }else {
            System.out.println("Puede que esten chocando o no hay examen");
            //return false;
            throw new ClassCollisionException();
        }
    }
    
    public boolean hayEspacioMateria(Materia materia){
        ArrayList<Clase> clases=materia.getClases();
        int indiceEspacios=0;
        for(Clase clase:clases){
            if(hayEspacioClase(materia, clase)){
                indiceEspacios++;
            }
            
        }//esto lanzara un error en caso de que no hayan clases en esa materia
        if((indiceEspacios!=0 && clases.size()!=0)&&(clases.size()==(indiceEspacios))){
            return true;
        }else{
            System.out.println("Las materia chocan o no hay materias");
            return false;
        }
        
    }
    
    //Deberia llamarse agregarClaseHorario
    public void agregarClase(Materia materia,Clase clase) {
        Date inicio=clase.getHoraInicio();
        Date fin=clase.getHorafin();
        Dias dia=clase.getDia();
        for (int i=0;i<horario.length;i++){
            Date[] referencia=horas[i];
            if (Horario.intersecan(referencia, inicio, fin)){
                for (int j=0;j<horario[i].length;j++){
                    if(dias[j]==dia){
                        if(horario[i][j]==null){
                        horario[i][j]=materia;    
                        }
                        
                    }
                }
            }
        }
    }
    
    public void agregarExamenHorario(Materia materia,Examen examen) {
        Date inicio=examen.getHoraInicio();
        Date fin=examen.getHorafin();
        Date dia=examen.getFecha();
        for (int i=0;i<horario.length;i++){
            Date[] referencia=horas[i];
            if (Horario.intersecan(referencia, inicio, fin)){
                for (int j=0;j<horario[i].length;j++){
                    if(dias[j]==Horario.getDia(dia)){
                        //System.out.println(dias[j]+dia.toString());
                        //System.out.println(Arrays.deepToString(Dias.values()));
                        
                        if(horario[i][j]==null){
                        horario[i][j]=materia;    
                        }
                        
                    }
                }
            }
        }
    }
   
    
    public void agregarMateriaHorario(Materia materia) {
        
        ArrayList<Clase> clases=materia.getClases();
        for(Clase clase:clases){
            agregarClase(materia,clase);
        }
        
    }
    //Valida que todas las clases de todas las materias ingresadas entran
    //Se debe usar solo cuando se ingresa un paralelo teorico y uno practico 
    //a la misma vez, es decir dentro de un arreglo, usualmente los provisto por un 
    //objeto ScrapFile
    public void hayEspacioMateriasClases(ArrayList<Materia> arregloMaterias) throws ClassCollisionException{
        int iespacioMateria=0;
        for(Materia m:arregloMaterias){
            if(hayEspacioMateria(m)){
                iespacioMateria++;
            }
        }
        
        if(!(iespacioMateria==arregloMaterias.size())){
            throw new ClassCollisionException();
        }
        
    }
    
    public void actualizarHorario() {
        inicializarHorario(7,20);
        if (!(materias.isEmpty())){
            System.out.println("Materias actuales:");
            for(Materia m:materias){
                System.out.println(m);
                agregarMateriaHorario(m);
            }
        }System.out.println("\n");
    }

    public boolean isEmpty(){
        int numCeldas=0;
        int numVacios=0;
        for(int i =0;i<horario.length;i++){
            for(int j=0;j<horario[i].length;j++){
                numCeldas++;
                if(horario[i][j]==null){
                    numVacios++;
                }
            }
        }
        if(numCeldas==numVacios && numCeldas!=0 && numVacios!=0){
            return true;
        }
        return false;
    }
    
    public ArrayList<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(ArrayList<Materia> materias) {
        this.materias = materias;
    }

    
    public Materia[][] getHorario() {
        return horario;
    }

    public void setHorario(Materia[][] horario) {
        this.horario = horario;
    }

    public Dias[] getDias() {
        return dias;
    }

    public Date[][] getHoras() {
        return horas;
    }
    
    /**
     * La variable desde es un entero que representa la hora de inicio
     * la variable hasta es un entero , va desde 00 hasta 24.
     * @param desde
     * @param hasta
     * @return Date[]
     */
    public static Date[] crearHoras(int desde, int hasta){
        int diferencia =hasta-desde;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        
        Date [] c1=new Date[2*diferencia+1];
        try{
            String fuente=(desde-1)+":30";
        Date d0=dateFormat.parse(fuente);
        int i=0;
        
        for(Date dia:c1){
            
            if(i%2==0){
                int nuevaHora=d0.getHours()+1;
                d0.setHours(nuevaHora);
                int nuevoMin=0;
                d0.setMinutes(nuevoMin);
            }else if (i%2!=0){
                int nuevoMin=30;
                d0.setMinutes(nuevoMin);
            }
            Date d1=(Date)d0.clone();//para compensar error de referencia
            //System.out.println(d1);
            c1[i]=d1;
            i++;
            }
        }catch(ParseException e){
            System.out.println("Error");
        }
        //System.out.println(Arrays.deepToString(c1));
        //System.out.println(25/2);
        return c1;
    }
    /**
     * Retorna un arreglo de arreglos que contiene las horas con intervalo de 30 min
     * @param desde
     * @param hasta
     * @return 
     */
    public static Date[][] intervalosHoras(int desde, int hasta){
        
        Date [] horas=crearHoras(desde,hasta);
        Date [][] matriz=new Date[horas.length-1][2];
        
        for (int i=0;i<matriz.length;i++){
            for(int y=0;y<matriz[i].length;y++)
            matriz[i][y]=horas[i+y];
            
        }
        //System.out.println(Arrays.deepToString(matriz));
        //System.out.println(matriz.length);
        return matriz;
    }
    
    public static Dias[] diasSemana(){
        
        Dias[] dias=Dias.values();
        return dias;
    }
    
    public static boolean intersecan(Date[] horas,Date inicio,Date fin){
        
        if ((inicio.before(horas[1])||inicio.equals(horas[0]))&&
                ((fin.after(horas[0])||fin.equals(horas[1])))){
            return true;
        }
        
        return false;
    }
    
     public static boolean intersecan(LlaveSemana intervalo,Date date){
       
         
       if ((date.after(intervalo.getInicioSemana())||date.equals(intervalo.getInicioSemana()))&&
                ((date.before(intervalo.getFinSemana())||date.equals(intervalo.getFinSemana())))){
            return true;
        }
        
        return false;
       
   }
   
    public static Dias getDia(Date date){
        Dias dia=null;
        int diaInt=date.getDay();
        if(diaInt!=0 ){
            dia= Dias.values()[diaInt-1]; 
        }else if (diaInt==0){
            System.out.println("Es domingo");
        }
        return dia;
    }
    //Date[] intervalo,Date fecha
    public static LlaveSemana IFSemana(Date dia)throws ParseException{
        
        //Date dia=formato.parse("03/02/2019");
        //seteo de la media noche
        dia.setHours(0);
        dia.setMinutes(0);
        dia.setSeconds(0);
        // declaracion del inicio y final de semana
        Date inicioSemana=new Date(dia.getTime()-(dia.getDay()*unDia));
        Date finSemana=new Date(dia.getTime()+((6-dia.getDay())*unDia));
        //muestra por pantalla resultados
        //System.out.println("inicio: "+inicioSemana);
        //System.out.println("dia : "+dia);
        //System.out.println("fin: "+finSemana);
        
        //System.out.println(Arrays.deepToString(IF));
        return new LlaveSemana(inicioSemana,finSemana);
    }
    
}
