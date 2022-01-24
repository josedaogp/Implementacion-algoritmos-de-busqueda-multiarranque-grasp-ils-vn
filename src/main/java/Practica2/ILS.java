/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Practica2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author josed
 */
public class ILS {
    LeerFichero lf = new LeerFichero();
    BusquedaLocalMejor ba;
    ArrayList<Integer> ciudades;
    int[][] matrizCostes;
    int CosteInicial;
    int costeTotal;
    int evaluaciones;
    Random aleatorio;

    public ILS(String ruta) {
        aleatorio = new Random(54252);
        evaluaciones = 0;
        costeTotal = 0;
        ba = new BusquedaLocalMejor(ruta);
        try {
            lf.leerfichero(ruta);
            this.ciudades = lf.getCiudades();
            // System.out.println("Ciudades.size: "+this.ciudades.size());
            this.matrizCostes = lf.getMatrizCostes();
        } catch (IOException ex) {
            Logger.getLogger(Greedy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int metodoILS(){
        
        ArrayList<Integer> SolucionInicial = generarSolucionAleatoria(); //la solución inicial será una aleatoria
        //ArrayList<Integer> MejorSolucion = (ArrayList<Integer>) SolucionInicial.clone();
        
        ArrayList<Integer> S = ba.busquedaElMejor(SolucionInicial);
        int costeS = ba.getCosteFinal();
        evaluaciones+=ba.getEvaluaciones();
        ArrayList<Integer> SPrima;
        ArrayList<Integer> SPrima2;
        
        int i=0;
        do {    
            SPrima = mutar(S); //se muta la solución
            SPrima2 = ba.busquedaElMejor(SPrima); //se le aplica la búsqueda local
            int costeSPrima2 = ba.getCosteFinal();
            evaluaciones+= ba.getEvaluaciones();
            
            if (costeSPrima2 < costeS) { //criterio de selección
                S = (ArrayList<Integer>) SPrima2.clone();
                costeS = costeSPrima2;
            }
            
            i++;
        } while (i<49);
        
        return costeS;
    }
    
    private ArrayList<Integer> generarSolucionAleatoria() {
        
        int pos; //posición que se generara aleatoriamente
        
        ArrayList<Integer> conjuntoNodos = (ArrayList<Integer>) this.ciudades.clone(); //nodos candidatos 
        ArrayList<Integer> vecino = new ArrayList<>(); //vecino que se generará
        
         do{            
            pos = aleatorio.nextInt(conjuntoNodos.size()); //se genera una nueva posición
            vecino.add(conjuntoNodos.get(pos)); //se añade a la solución la ciudad correspondiente
            conjuntoNodos.remove(pos);  //se elimina de los candidatos
        }while (!conjuntoNodos.isEmpty()); //se repite mientras queden candidatos por elegir
         
        vecino.add(vecino.get(0)); //para tener en cuenta que vuelve hasta la posición origen de nuevo
        CosteInicial=calcularCosteSolucion(vecino); //se calcula el coste del vecino generado
        
        return vecino;
    }
    
    private int calcularCosteSolucion(ArrayList<Integer> solucion){
        //evaluaciones++;
        int costeDevolver=0;
        for (int i = 0; i < solucion.size()-1; i++) {
            int ciudad1 = solucion.get(i); 
            int ciudad2 = solucion.get(i+1); 
            costeDevolver+=matrizCostes[ciudad1-1][ciudad2-1];
        }
        return costeDevolver;
    }

    private ArrayList<Integer> mutar(ArrayList<Integer> S) { 
        
        int n = ciudades.size();
        int posIniSublista = aleatorio.nextInt(n-n/4); //numero aleatorio entre 0 y n-n/4, ya que la lista no es cíclica
        int posFinSublista = posIniSublista + n/4-1; //se calcula el resto de la lista 
        List<Integer> sublista = S.subList(posIniSublista, posFinSublista+1); //cogemos la sublista para barajarla
        
        Collections.shuffle(sublista); //se barajan aleatoriamente los elementos de la sublista
        
        //Se junta de nuevo la sublista con la primera parte y la última de la solución original
        List<Integer> primeraParte = S.subList(0, posIniSublista);
        List<Integer> ultimaParte = S.subList(posFinSublista, n);
        
        ArrayList<Integer> solucionMutada = new ArrayList<Integer>(primeraParte);
        solucionMutada.addAll(sublista);
        solucionMutada.addAll(ultimaParte);
        
        return solucionMutada;
        
    }
    
    public int getEvaluaciones(){
        return this.evaluaciones;
    }
}
