package max.pkg2sat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase principal
 *
 * @author camil
 */
public class MAX2SAT {

    public static void main(String[] args) {
        //Se declaran varibles
        Scanner leer = new Scanner(System.in);
        String formula;
        int k;
        ArrayList<Clausula> clausulas;
        //menu 
        System.out.println("Que desea ver:\n"
                + "Ejemplos previos (1)\n"
                + "Solucionar una formula dada (2)");
        int menu = leer.nextInt();
        leer.nextLine();
        if (menu == 1) {
            //Inicialización de variables
            File archiveDonations;
            FileReader read;
            BufferedReader storage;
            String str;
            archiveDonations = new File("ejemplos.txt");
            //Si no existe el archivo se crea
            try {
                read = new FileReader(archiveDonations);
                storage = new BufferedReader(read);
                str = storage.readLine();

                while (str != null) {
                    try {
                        System.out.println(str);
                        str = storage.readLine();
                    } catch (IOException ex) {
                        System.out.println("Error inesperado");
                    }
                }
                storage.close();
                read.close();
            } catch (FileNotFoundException ex) {
                System.out.println("Error inesperado");
            } catch (IOException ex) {
                System.out.println("Error inesperado");
            }
        } else if (menu == 2) {
            //Se solicita la formula booleana
            while (true) {
                System.out.println("Formato: (; = AND) (, = OR) (- = NOT)\n"
                        + "Ej: (¬a1 ∨ a2) ∧ (a2 ∨ ¬b) ∧ ... --> -a1,a2;a2,-b;...\n"
                        + "Dígite la formula:");
                formula = leer.nextLine();
                clausulas = parse(formula);

                System.out.println("\nConfirmar que la formula digitada es: ");
                print(clausulas);

                System.out.println("Dígite:\n"
                        + "Sí (1)\n"
                        + "No (0)\n");
                int confirmar = leer.nextInt();

                if (confirmar == 1) {
                    break;
                }
            }
            System.out.println("\nDigite k (Número de clausulas que quiere se satisfagan como minimo):");
            k = leer.nextInt();

            //Se obtiene el resultado y se muestra
            long InicioInicial = System.nanoTime();
            boolean resultado = Solver.resolverBF(clausulas, k);
            long FinalInicial = System.nanoTime();
            
            long InicioMejorado = System.nanoTime();
            int[] resultadoM = SolverMejorado.resolver(clausulas, k);
            long FinalMejorado = System.nanoTime();

            if (resultado) {
                String[][] resultadoVariables = Solver.getResultado();
                System.out.println("\nExiste un resultado");
                System.out.println("Las asignaciones usadas fueron: ");
                for (int i = 0; i < resultadoVariables.length; i++) {
                    if (resultadoVariables[i][1].equals("0")) {
                        System.out.println(resultadoVariables[i][0] + " = Falso");
                    } else {
                        System.out.println(resultadoVariables[i][0] + " = Verdadero");
                    }
                }
            } else {
                System.out.println("No existe un resultado");
            }
            System.out.println("El tiempo de ejecución para el algoritmos inicial es: " + (FinalInicial - InicioInicial));
            System.out.println("El tiempo de ejecución para el algoritmos mejorado es: " + (FinalMejorado - InicioMejorado));
        }

    }

    /**
     * Combierte el texto ingresado por el usuario a el sistema desarrollado
     *
     * @param formula
     * @return
     */
    public static ArrayList<Clausula> parse(String formula) {

        String[] clausulasTemporal = formula.split(";");
        ArrayList<String[]> clausulas = new ArrayList<>();
        for (String string : clausulasTemporal) {
            clausulas.add(string.split(","));
        }

        ArrayList<Clausula> parsed = new ArrayList<>();
        for (String[] clausula : clausulas) {
            Clausula temp = new Clausula();
            for (int i = 0; i <= 1; i++) {
                Boolean signo;
                String nombre;
                if (clausula[i].contains("-")) {
                    signo = false;
                    nombre = clausula[i].substring(1);
                } else {
                    signo = true;
                    nombre = clausula[i];
                }
                Variable vTemp = new Variable(signo, nombre);
                temp.misVariables.add(vTemp);
            }
            parsed.add(temp);
        }
        return parsed;
    }

    /**
     * Muestra la formula ingresada con formato
     *
     * @param clses
     */
    public static void print(ArrayList<Clausula> clses) {
        System.out.print("U = ");
        for (Clausula clse : clses) {

            if (clse == clses.get(0)) {
                System.out.print("( ");
            } else {
                System.out.print(" and ( ");
            }

            for (Variable v : clse.misVariables) {
                if (v.signo) {
                    System.out.print(v.nombre);
                } else {
                    System.out.print("-" + v.nombre);
                }
                if (v == clse.misVariables.get(0)) {
                    System.out.print(" or ");
                }
            }
            System.out.print(" )");
        }
        System.out.println("");
    }

}
