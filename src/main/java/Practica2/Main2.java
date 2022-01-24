package Practica2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author josed
 */
public class Main2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        GRASP grasp;
        ILS ils;
        VNS vns;

        //ficheros
        ArrayList<String> ficheros = new ArrayList<>();
        ficheros.add("./st70.tsp");
        ficheros.add("./ch130.tsp");
        ficheros.add("./a280.tsp");
//        ficheros.add("./p654.tsp");
//        ficheros.add("./vm1084.tsp");
//        ficheros.add("./vm1748.tsp");

//        //semillas
//        Random aleatorio = new Random();
//        int[] semillas = new int[10];
//        for (int i = 0; i < 10; i++) {
//            semillas[i] = aleatorio.nextInt(999999)+1;
//        }
        File f = new File("ResultadosVNSk1.csv");
        FileWriter fWriter;
        try {
            fWriter = new FileWriter(f);

            //GRASP
            System.out.println("GRASP-----------");
            fWriter.write("GRASP;ST70;ST70;CH130;CH130;A280;A280;");
            fWriter.write("\n");
            fWriter.write("N Ejecucion;Coste;EV;Coste ;Ev;Coste;EV;");

            fWriter.write("\n");
            fWriter.write(1 + ";");
            for (String fichero : ficheros) {
                System.out.println("FICHERO: " + fichero);
                grasp = new GRASP(fichero);
                int coste = grasp.metodoGRASP();
                fWriter.write(coste + ";");
                int evaluaciones = grasp.getEvaluaciones();
                fWriter.write(evaluaciones + ";");

            }
            fWriter.flush();

            System.out.println("FIN GREEDY***********");
            fWriter.write(";;;;;;;;;;;;;;;;;;");
            fWriter.write(";;;;;;;;;;;;;;;;;;");

            //ILS
            System.out.println("ILS-----------");
            fWriter.write("ILS;ST70;ST70;CH130;CH130;A280;A280;");
            fWriter.write("\n");
            fWriter.write("N Ejecucion;Coste;EV;Coste ;Ev;Coste;EV;");

            fWriter.write("\n");
            fWriter.write(1 + ";");
            for (String fichero : ficheros) {
                System.out.println("FICHERO: " + fichero);
                ils = new ILS(fichero);
                int coste = ils.metodoILS();
                fWriter.write(coste + ";");
                int evaluaciones = ils.getEvaluaciones();
                fWriter.write(evaluaciones + ";");

            }
            fWriter.flush();

            System.out.println("FIN ILS***********");
            fWriter.write(";;;;;;;;;;;;;;;;;;");
            fWriter.write(";;;;;;;;;;;;;;;;;;");
            
            //VNS
            System.out.println("VNS-----------");
            fWriter.write("VNS;ST70;ST70;CH130;CH130;A280;A280;");
            fWriter.write("\n");
            fWriter.write("N Ejecucion;Coste;EV;Coste ;Ev;Coste;EV;");

            fWriter.write("\n");
            fWriter.write(1 + ";");
            for (String fichero : ficheros) {
                System.out.println("FICHERO: " + fichero);
                vns = new VNS(fichero);
                int coste = vns.metodoVNS();
                fWriter.write(coste + ";");
                int evaluaciones = vns.getEvaluaciones();
                fWriter.write(evaluaciones + ";");

            }
            fWriter.flush();

            System.out.println("FIN VNS***********");
            fWriter.write(";;;;;;;;;;;;;;;;;;");
            fWriter.write(";;;;;;;;;;;;;;;;;;");
            

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

}
