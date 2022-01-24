/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Practica2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author josed
 */
public class VNS {
    LeerFichero lf = new LeerFichero();
    BusquedaLocalMejor ba;
    ArrayList<Integer> ciudades;
    int[][] matrizCostes;
    int CosteInicial;
    int costeTotal;
    int evaluaciones;
    Random aleatorio;

    public VNS(String ruta) {
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
    
    public int metodoVNS(){
        
        ArrayList<Integer> Sact = generarSolucionAleatoria(); //solución inicial aleatoria
        ArrayList<Integer> Svec = new ArrayList<>();
        ArrayList<Integer> SPrima;
        int costeSPrima;
        int costeSact = CosteInicial;
        evaluaciones++;
        //int costeSact = calcularCosteSolucion(Sact);
        
        int k =1;
        int blmax = 50;
        int kmax = 5;
        
        for (int bl = 0; bl < blmax; bl++) {
            if (k>kmax) { //actualización parámetro de entornos
                k=1;
            }
            Svec = mutar(Sact, k); //se muta según el entorno
            SPrima = ba.busquedaElMejor(Svec); //se le aplica la búsqueda local a la solución anterior
            costeSPrima = ba.getCosteFinal();
            evaluaciones+= ba.getEvaluaciones();
            
            if (costeSPrima < costeSact) { //criterio de selección
                Sact = (ArrayList<Integer>) SPrima.clone();
                costeSact = costeSPrima;
                k=1;
            }else k++;
        }
        
        return costeSact;
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
        evaluaciones++;
        int costeDevolver=0;
        for (int i = 0; i < solucion.size()-1; i++) {
            int ciudad1 = solucion.get(i); 
            int ciudad2 = solucion.get(i+1); 
            costeDevolver+=matrizCostes[ciudad1-1][ciudad2-1];
        }
        return costeDevolver;
    }

    private ArrayList<Integer> mutar(ArrayList<Integer> S, int k) {
        
        int n = ciudades.size();
        int s = n/(9-k); //dependiente del numero de entornos
        int posIniSublista = aleatorio.nextInt(n-s); //numero aleatorio entre 0 y s, ya que la lista no es cíclica
        int posFinSublista = posIniSublista + s-1; //se calcula el resto de la lista 
        List<Integer> sublista =  S.subList(posIniSublista, posFinSublista+1); //cogemos la sublista para barajarla
        
        Collections.shuffle(sublista); //se barajan aleatoriamente los elementos de la sublista
        
        //Se junta de nuevo la sublista con la primera parte y la última de la solución original
        List<Integer> primeraParte = S.subList(0, posIniSublista);
        List<Integer> ultimaParte = S.subList(posFinSublista, n);
        
        ArrayList<Integer> solucionMutada = new ArrayList<Integer>(primeraParte);
        solucionMutada.addAll(sublista);
        solucionMutada.addAll(ultimaParte);
        
        return solucionMutada;
        
    }

    public int getEvaluaciones() {
        
        return this.evaluaciones;
    }
    
}
