package Prueba;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

public class ProcesadorCarteleraSalir implements ProcesadorCartelera {

	@Override
	public ArrayList<Pelicula> getPeliculas() {
		ArrayList<URL> direcciones=getDirecciones();
		ArrayList<Pelicula> pelis=new ArrayList<Pelicula>();
		for (URL direccion : direcciones){
			Pelicula peli=getPelicula(direccion);
			if (peli!=null)
				pelis.add(peli);
		}
		return pelis;
		
	}
	
	
	private ArrayList<URL> getDirecciones(){
		URL direccion;
		Source source=null;
		try {
			direccion = new URL("http://www.salir.com/madrid/cartelera.html");
			source=new Source(direccion);
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		ArrayList <URL> direcciones=new ArrayList<URL>();
		//System.out.print();
		List<? extends Segment> segments=source.getFirstElementByClass("peliculas sortable").getAllElements("td\nclass=\"titulo\"");
		for (Segment segment : segments) {
			String dire=segment.getAllElements("a").get(0).getAttributeValue("href");
			if (dire!=null){
				try {
					direcciones.add(new URL((dire)));
				} catch (MalformedURLException e) {
				}
			}
		}
		return direcciones;
	}
	
	private Pelicula getPelicula(URL direccion){
		//class:mainContent extended
		//id:main-content-container
		Source source=null;
		try {
			source = new Source(direccion);
		} catch (IOException e) {
			return null;
		}
		String titulo=source.getElementById("main-content").getFirstElement("h2").getTextExtractor().toString();
		List <Element> atributos=source.getAllElementsByClass("peliInfo").get(0).getAllElements("li");
		Pelicula a=new Pelicula(titulo);
		/*for (Element elemento : atributos) {
			System.out.println(elemento.getContent().);
		}*/
		return a;
	}
	

}
