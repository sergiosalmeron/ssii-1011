package recomendador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import utils.BD;

public class Principal {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Recomendador recomendador=new Recomendador();
		/*PeliGeneros p=new PeliGeneros("Película Documental");
		p.addGenero(Generos.Comedia, 0.6);
		p.addGenero (Generos.Accion, 0.3);
		p.addGenero(Generos.Romantica, 0.1);*/
		
		//Usuario u=new Usuario("Aleix", 0.3125, 0.375, 0.0625, 0.25);
		/*Usuario u=new Usuario("Mr. Patata", 0.01, 0.1, 0.04, 0.85);
		System.out.println(u);
		System.out.println(p);*/
		Usuario u=BD.dameUsuario("1");
		ArrayList<PeliGeneros> arrPeliculas=BD.dameTablaGeneros();
		ArrayList<PeliPuntuacion> arrPuntuaciones=new ArrayList<PeliPuntuacion>();
		PeliPuntuacion arrayPunt[] = new PeliPuntuacion[arrPeliculas.size()];
		double punt;
		int i=0;
		for (PeliGeneros peliGeneros : arrPeliculas) {
			punt=recomendador.getPuntuacion(u, peliGeneros);
			PeliPuntuacion puntuacion=new PeliPuntuacion(peliGeneros.getTitulo(),punt);
			arrPuntuaciones.add(puntuacion);
			arrayPunt[i]=puntuacion;
			i++;
			
		}
		List<PeliPuntuacion> names = Arrays.asList(arrayPunt);
        Collections.sort(names);
        System.out.println(names);
		
		//System.out.println(arrPuntuaciones);
		
//		PeliPuntuacion nameArray[] = {
//	            new PeliPuntuacion("John", "Lennon"),
//	            new Name("Karl", "Marx"),
//	            new Name("Groucho", "Marx"),
//	            new Name("Oscar", "Grouch")
//	        };
	        /*List<Name> names = Arrays.asList(nameArray);
	        Collections.sort(names);
	        System.out.println(names);*/

		
		
		/*System.out.println("Sin estado de ánimo: "+punt);
		punt=recomendador.getPuntuacionAnimo(u, p,EstadosAnimo.Alegre);		
		System.out.println("Alegre: "+punt);
		punt=recomendador.getPuntuacionAnimo(u, p,EstadosAnimo.Triste);		
		System.out.println("Triste: "+punt);
		punt=recomendador.getPuntuacionAnimo(u, p,EstadosAnimo.Sorprendido);		
		System.out.println("Sorprendido: "+punt);
		punt=recomendador.getPuntuacionAnimo(u, p,EstadosAnimo.Relajado);		
		System.out.println("Relajado: "+punt);
		punt=recomendador.getPuntuacionAnimo(u, p,EstadosAnimo.Asustado);		
		System.out.println("Asustado: "+punt);*/

	}

}
