/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package externo;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Josue
 */
public class Diccionario {
    
    public static double coincidencias(String palabra_1,String palabra_2){
        double resultado=0;
        palabra_1=palabra_1.toLowerCase();
        palabra_2=palabra_2.toLowerCase();
        ArrayList<Double> coincidencias=new ArrayList<>();
        if (palabra_1.length()>palabra_2.length()){
            
            for(String vocablo:subconjuntos(palabra_2)){
                if (palabra_1.contains(vocablo)){
                    double n=vocablo.length();
                    double m=palabra_1.length();
                    coincidencias.add(
                            n/m);
                }
            }
        }else if (palabra_1.length()<palabra_2.length()){
            for(String vocablo:subconjuntos(palabra_1)){
                if (palabra_2.contains(vocablo)){
                    double n=vocablo.length();
                    double m=palabra_2.length();
                    coincidencias.add(
                            n/m);
                }
            }
        }else if (palabra_1.length()==palabra_2.length()){
            for(String vocablo:subconjuntos(palabra_1)){
                if (palabra_2.contains(vocablo)){
                    double n=vocablo.length();
                    double m=palabra_2.length();
                    coincidencias.add(
                            n/m);
                }
            }
        }
        return maximo(coincidencias);
    }
    
    
    
    public static ArrayList<String> subconjuntos(String palabra){
        
        ArrayList<String> subs=new ArrayList<>();
       for(int n=0;n<palabra.length();n++){
           palabra.toLowerCase();
            subs.add(String.valueOf(palabra.charAt(n)));
            for(int k=n+1;k<palabra.length();k++){
                subs.add(String.valueOf(palabra.substring(n,k+1)));
            }
        }
        return subs;
    } 
    
    public static double maximo(ArrayList<Double> lista){
        double retorno=0;
        for(double numero:lista){
            if(numero>retorno){
                retorno=numero;
            }
        }
        
        return retorno;
    }
}
