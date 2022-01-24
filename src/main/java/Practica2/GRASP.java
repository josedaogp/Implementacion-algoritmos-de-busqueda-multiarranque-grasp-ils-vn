/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Practica2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author josed
 */
public class GRASP {

    LeerFichero lf = new LeerFichero();
    BusquedaLocalMejor ba;
    ArrayList<Integer> ciudades;
    int[][] matrizCostes;
    int costeTotal;
    int evaluaciones;
    Random aleatorio;

    public GRASP(String ruta) {
        aleatorio = new Random();
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

    public GRASP(ArrayList<Integer> solucionActual, String ruta) {
        evaluaciones = 0;
        costeTotal = 0;
        ba = new BusquedaLocalMejor(ruta);
        try {
            lf.leerfichero(ruta);
            this.matrizCostes = lf.getMatrizCostes();
        } catch (IOException ex) {
            Logger.getLogger(Greedy.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.ciudades = (ArrayList<Integer>) solucionActual.clone();
    }

    public int metodoGRASP() {

        int i = 0;
        ArrayList<Integer> S;
        //int costeS;
        ArrayList<Integer> SPrima;
        int costeSPrima;
        int mejorCoste = Integer.MAX_VALUE;
        
        do {
            S = ConstruccionSolGreedyAleatorizada(); //generar solución greedy aleatoriezada
            //costeS = calcularCosteSolucion(S);
            SPrima = ba.busquedaElMejor(S); //aplicarle la búsqueda local del mejor vecino
            costeSPrima = ba.getCosteFinal(); //calcular el coste de la nueva solución
            evaluaciones+= ba.getEvaluaciones(); //actualizar evaluaciones
            
            if (costeSPrima < mejorCoste) { //criterio de selección
                mejorCoste = costeSPrima;
                //S = (ArrayList<Integer>) SPrima.clone();
                //costeS = costeSPrima;
            }
            i++;
        } while (i < 10);

        return mejorCoste;
    }

    /**
     * Este método hace uso de varias listas de nodos para controlar que en la solución generada no se repiten
     * ciudades. Además, varias listas para calcular las probabilidades a partir de las inversas de las distancias de cada
     * ciudad candidata.
     * @return 
     */
    private ArrayList<Integer> ConstruccionSolGreedyAleatorizada() {

        int n = ciudades.size();
        
        int ciuIni = aleatorio.nextInt(n);

        ArrayList<Integer> S = new ArrayList<>();
        ArrayList<Integer> conjuntoNodos = (ArrayList<Integer>) ciudades.clone();
        ArrayList<Integer> conjuntoNodosLRC;

        S.add(ciuIni); //añadimos la primera ciudad a la solucion
        conjuntoNodos.remove((Object) ciuIni); //quitamos de las restantes esa primera

        
        int i = 0; //insertados
        int l = (int) (0.1 * n); //tamaño LRC

        ArrayList<Integer> LRC = new ArrayList<>();

        boolean encontrado = false; //si se ha encontrado la ciudad con probabilidad
        ArrayList<Integer> distCiu = new ArrayList<>();
        ArrayList<Double> probCiu;
        ArrayList<Double> inversasCiu;

        int ultimaCiudad = ciuIni;
        while (i < n-1) {

            //reiniciamos los arrays y variables necesarias
            conjuntoNodosLRC = (ArrayList<Integer>) conjuntoNodos.clone();
            probCiu = new ArrayList<>(); 
            inversasCiu = new ArrayList<>();
            distCiu = new ArrayList<>();
            LRC = new ArrayList<>();
            encontrado = false;

            int j=0;
            while (j<l && conjuntoNodosLRC.size() > 0) { //Se crea la LRC con las ciudades más cercanas, sin contar las que ya han sido insertadas           
                int insertar = calcularCercana(ultimaCiudad, conjuntoNodosLRC, distCiu); //calcula la ciudad más cercana
                LRC.add(insertar);
                conjuntoNodosLRC.remove((Object) insertar); //para evitar repetidas
                j++;
            }

            double totalprob = 0;
            for (int distciudad : distCiu) { //calcular el total de probabilidades y guardar las inversas de las ciudades
                double inversa = (double) 1 / distciudad;
                inversasCiu.add(inversa);
                totalprob += inversa;
            }

            for (double inversa : inversasCiu) { //calcular probabilidades individuales. En probCiu tendremos las probabilidades ordenadas por sus ciudades
                double prob = (double) inversa / totalprob;
                probCiu.add(prob);
            }

            double probabilidad = aleatorio.nextDouble();
            int k = 0;
            //la primera vez que se calcule el rango será entre cero y la primera probabilidad:
            double prob1 = 0; 
            double prob2 = probCiu.get(k);
            if (probabilidad > prob1 && probabilidad <= prob2) {
                encontrado = true;
            }else k++;
            
            //Si el dado tirado (probabilidad) no pertenece a la primera ciudad, seguir buscando
            while (!encontrado && k<probCiu.size()-1) { //encontrar la ciudad con probabilidad
                prob1 = probCiu.get(k);
                prob2 = probCiu.get(k + 1);
                if (probabilidad > prob1 && probabilidad <= prob2) {
                    encontrado = true;
                }
                k++;
            }

            S.add(LRC.get(k)); //añadir la ciudad a la solución
            conjuntoNodos.remove((Object) LRC.get(k)); //eliminarla del conjunto de nodos disponibles
            ultimaCiudad = LRC.get(k); //volver a empezar por esa ciudad
            i++;
        }

        return S;
    }

    public Integer calcularCercana(int ciuPartir, ArrayList<Integer> conjuntoNodos, ArrayList<Integer> distCiu) {

        int min = Integer.MAX_VALUE;
        Integer aBuscar = -1;

        if (ciuPartir<0) {
                System.out.println("Entra");
            }
        
        for (int ciudad : conjuntoNodos) { //para que coja cada ciudad como objeto
            if (matrizCostes[ciuPartir - 1][ciudad - 1] < min) {
                min = matrizCostes[ciuPartir - 1][ciudad - 1];
                aBuscar = ciudad;
            }
        }

        distCiu.add(min);
        return aBuscar;
    }
    
    private int calcularCosteSolucion(ArrayList<Integer> solucion){
        evaluaciones++;
        int costeDevolver=0;
        for (int i = 0; i < solucion.size()-1; i++) {
            int ciudad1 = solucion.get(i); 
            int ciudad2 = solucion.get(i+1); //System.out.println("ciudad1 + ciudad2: "+ciudad1+" + "+ + ciudad2);
            //System.out.println("Coste: "+mCostes[ciudad1-1][ciudad2-1]);
            costeDevolver+=matrizCostes[ciudad1-1][ciudad2-1];
        }
        //System.out.println("CosteSolucionVecina: "+costeDevolver);
        return costeDevolver;
    }
    
    public int getEvaluaciones(){
        return this.evaluaciones;
    }
}
