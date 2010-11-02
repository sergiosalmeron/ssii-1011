package Prueba;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Principal {

	/**
	 * @param args
	 * @throws MalformedURLException 
	 */
	
	public static void main(String[] args) throws MalformedURLException {
	/*	URL url=new URL("http://www.imdb.es/find?q=Amador");
		String resultado=ExtractorWeb.downloadURL(url);
		System.out.println(resultado);
	*/
		
		//System.out.println(resultado);
		
		ProcesadorCarteleraGDO proce=new ProcesadorCarteleraGDO();
		ArrayList<Pelicula> pelis=proce.getPeliculas();
		
		URL urlPeli= new URL("http://www.imdb.es/find?s=tt&q="+pelis.get(2).getTitulo());
		//usamos el metodo que nos hace pasarnos por un navegador firefox para que imdb acepte la consulta
		String resultado=ExtractorWeb.downloadURLFirefox(urlPeli);
		System.out.println(resultado);
	}

}
