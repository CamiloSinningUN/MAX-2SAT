package max.pkg2sat;

import java.util.ArrayList;

/**
 * Clase con los metodos necesarios para resolver MAX 2SAT
 *
 * @author camil
 */
public class Solver {

    private static String[][] Resultado; // Variable que almacena el resultado de ser hallado

    /**
     * getter de Resultado
     *
     * @return
     */
    public static String[][] getResultado() {
        return Resultado;
    }
    
    /**
     * Formula llamada para resolver una formula dada
     *
     * @param formula
     * @param k
     * @return
     */
    public static boolean resolverBF(ArrayList<Clausula> formula, int k) {

        //Se almacenan las variables de la formula en un Arraylist
        ArrayList<String> variablesTemp = new ArrayList<>();
        for (Clausula c : formula) {
            for (Variable v : c.misVariables) {
                if (!variablesTemp.contains(v.nombre)) {
                    variablesTemp.add(v.nombre);
                }
            }
        }

        //Se crea una matriz temporal con las variables
        String limite = ""; //Maxima combinación a la que se puede llegar
        String[][] variables = new String[variablesTemp.size()][2];
        for (int i = 0; i < variables.length; i++) {
            variables[i][0] = variablesTemp.get(i);
            variables[i][1] = "0";
            limite = limite + "1";
        }

        //Se prueban todas las posibles combinaciones para reolver la formula
        while (!Comprobar(formula, variables, k)) {

            //Se genera el numero y se sale del ciclo en caso de que no haya respuestas
            String num = "";
            for (int i = 0; i < variables.length; i++) {
                num = num + variables[i][1];
            }
            if (num.equals(limite)) {
                break;
            }

            //Se genera nueva combinación
            String nNum = sumarBinario(num, "1");
            int length = variables.length;
            int i = 0;
            int j = 0;
            while (i < variables.length) {
                if (length > nNum.length()) {
                    variables[i][1] = "0";
                    length--;
                } else {
                    variables[i][1] = nNum.toCharArray()[j] + "";
                    j++;
                }
                i++;
            }
        }
        
        //Devuelve el resultado correspondiente
        if (Comprobar(formula, variables, k)) {
            Resultado = variables;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Comprueba si cierta combinación booleana solucion MAX 2SAT
     * @param formula
     * @param variables
     * @param k
     * @return
     */
    public static boolean Comprobar(ArrayList<Clausula> formula, String[][] variables, int k) {
        boolean sirve = false;
        int contSatC = 0;
        boolean satC;
        
        for (Clausula clausula : formula) {
            satC = false;
            
            for (Variable v : clausula.misVariables) {
                boolean vTemp = false;
                int i = 0;
                //Se busca la varible para saber que valor asignarle
                while (!variables[i][0].equals(v.nombre)) {
                    i++;
                }
                
                if(variables[i][1].equals("0")){
                    vTemp = false;
                }else{
                    vTemp = true;
                }
                
                if(!v.signo){
                    vTemp = !vTemp;
                }

                if (vTemp) {
                    satC = true;
                }
            }
            if (satC) {
                contSatC++;
            }
        }
        
        if (contSatC >= k) {
            sirve = true;
        }
        return sirve;
    }

    /**
     * Suma dos numeros binarios
     *
     * @param numero0
     * @param numero1
     * @return
     */
    public static String sumarBinario(String numero0, String numero1) {
        int number0 = Integer.parseInt(numero0, 2);
        int number1 = Integer.parseInt(numero1, 2);

        int sum = number0 + number1;
        return Integer.toBinaryString(sum);
    }
}
