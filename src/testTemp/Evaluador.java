package testTemp;

import java.util.ArrayList;

public class Evaluador {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Se deben pasar las respuestas como args separadas por espacio "0 0 1 3 2 1 1 2 1 0 3 ... 3"
		if (args.length==16){
			TestTemperamento test= new TestTemperamento();
			ArrayList<Pregunta> preguntas=test.getPreguntas();
			int i=0;
			for (Pregunta p : preguntas) {
				//System.out.println("-----------Pr"+i+"---------------");
				//System.out.println(p.toString());
				//aux=keyboard.nextInt();
				p.setRespuestaElegida(Integer.parseInt(args[i]));
				i++;
			}
		}
		

	}

}
