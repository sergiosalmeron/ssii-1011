package clasesDePrueba;

import java.io.IOException;
import java.util.ArrayList;

import Prueba.BD;
import Prueba.Cine;
import Prueba.ProcesadorCinesGDO;
import Prueba.ProvinciasGDO.Provincia;

public class pruebaJuan {

	public static void main(String[] args) throws IOException {
		//ProcesadorCarteleraGDO a=new ProcesadorCarteleraGDO();
		//;
		//URL direccion=ExtracTor.getURL("http://www.salir.com/madrid/cartelera.html");
		
		
		//Introduce las provincias
		/*BD bd=new BD();
		bd.introduceProvincias();*/
		
		//Introduce los cines
		/*ProcesadorCinesGDO a=new ProcesadorCinesGDO();
		ArrayList<Cine> cines=a.getCines(Provincia.avila);
		BD bd=new BD();
		bd.introduceCine(cines);*/
		

		
		//Introduce las películas de una provincia (por ejemplo, ávila)
		
		/*ProcesadorCarteleraSalir a=new ProcesadorCarteleraSalir();
		a.getPeliculas(null);
		URL direccion=new URL("http://www.salir.com/madrid/cartelera.html");
		Source source=new Source(direccion);*/
		/*List<? extends Segment> segments=source.getAllElements("td\nclass=\"titulo\"");
		for (Segment segment : segments) {
			System.out.println(segment.getAllElements("a").get(0).getAttributeValue("href"));
			System.out.println("--");
		}*/
		/*BD bd=new BD();
		bd.introducePelicula(a.getPeliculas(Provincia.avila));*/
		
		
				
		//Prueba individual de introducción de película con título a tratar.
		
		/*Pelicula p=new Pelicula("Ga'Hoole: La leyenda de los guardianes (Legend of the Guardians: The Owls of Ga'Hoole)");
		p.setAnio(0);
		p.setGenero("null");
		p.setDuracion(0);
		p.setCalificacion(0);
		p.setNacionalidad("null");
		ArrayList<Pelicula> ap=new ArrayList<Pelicula>();
		ap.add(p);
		BD bd=new BD();
		bd.introducePelicula(ap);*/
		
	}

}
