package max.pkg2sat;

import java.util.ArrayList;

public class SolverMejorado {

    /**
     * Formula llamada para resolver una formula usanod aproximación aleatoria
     *
     * @param formula
     * @param k
     * @return
     */
    public static int [] resolver(ArrayList<Clausula> formula, int k) {

        //Se almacenan las variables de la formula en un Arraylist
        ArrayList<Variable> clausulasTemp = new ArrayList<>();
        ArrayList<String> variablesTemp = new ArrayList<>();
        for (Clausula c : formula) {
            for (Variable v : c.misVariables) {
                clausulasTemp.add(v);
                if (!variablesTemp.contains(v.nombre)) {
                    variablesTemp.add(v.nombre);
                }
            }
        }

        int[] variables = new int[variablesTemp.size()];
        return Mejorado(variables, clausulasTemp, variablesTemp);
    }

    public static int[] Mejorado(int[] variablesVal, ArrayList<Variable> clausulas, ArrayList<String> variables) {
        int[] respuesta = new int[variablesVal.length + 1];
        int[] variableCont = new int[variablesVal.length * 2];
        
        for (Variable clausula : clausulas) {
            int j = 0;
            for (String variable : variables) {
                if (clausula.nombre.equals(variable)) {
                    if (clausula.signo) {
                        variableCont[j * 2]++;

                    } else if (!clausula.signo) {
                        variableCont[j * 2 + 1]++;
                    }
                    break;
                }
                j++;
            }
        }

        int cont = 0;     
        for (int i = 0; i < variableCont.length; i = i + 2) {
            if (variableCont[i] > variableCont[i + 1]) {
                variablesVal[cont] = 1;
                cont++;
            } else {
                variablesVal[cont] = -1;
                cont++;
            }
        }

        clausulas = setValores(clausulas, variablesVal, variables);
        for (int i = 0; i < variablesVal.length; i++) {
            respuesta[i] = variablesVal[i];
        }
        respuesta[respuesta.length - 1] = contValores(clausulas);
        return respuesta;
    }

    public static int contValores(ArrayList<Variable> clausulas) {
        int cont = 0;
        for (int i = 0; i < clausulas.size(); i = i + 2) {
            if (clausulas.get(i).signo || clausulas.get(i + 1).signo) {
                cont++;
            }
        }
        return cont;
    }

    public static ArrayList<Variable> setValores(ArrayList<Variable> clausulas, int[] variablesVal, ArrayList<String> variables) {
        ArrayList<Variable> clausulasTemp = new ArrayList<>();
        for (Variable clausula : clausulas) {
            clausulasTemp.add(clausula);
        }

        for (int i = 0; i < variablesVal.length; i++) {
            for (int j = 0; j < clausulas.size(); j++) {
                if (clausulas.get(j).nombre.equals(variables.get(i))) {
                    Variable temp = clausulas.get(j);
                    if (variablesVal[i] == -1) {
                        //
                        temp.signo = !temp.signo;
                    }
                    clausulasTemp.set(j, temp);
                }
            }
        }

        return clausulasTemp;
    }

}
