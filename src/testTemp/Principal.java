package testTemp;
import java.util.ArrayList;
import java.util.Scanner;


public class Principal {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Scanner keyboard = new Scanner(System.in);
		int aux;
		int i=1;
		
		TestTemperamento test= new TestTemperamento();
		ArrayList<Pregunta> preguntas=test.getPreguntas();
		
		for (Pregunta p : preguntas) {
			System.out.println("-----------Pr"+i+"---------------");
			System.out.println(p.toString());
			aux=keyboard.nextInt();
			p.setRespuestaElegida(aux);
			i++;
		}
		
		
		System.out.println(test.dameResultado());
	}

}
