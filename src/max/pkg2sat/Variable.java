package max.pkg2sat;
/**
 * Clase que representa las variables de la formula booleana
 * @author camil
 */
public class Variable {    
    boolean signo; //true no negado y false es negado
    String nombre;

    public Variable(boolean signo, String nombre) {
        this.signo = signo;
        this.nombre = nombre;
    }
}
