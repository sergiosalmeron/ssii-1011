package utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import tads.Pelicula;
import utils.ExtractorWeb;


public class IMDBExtractor {
	
	public ArrayList<Pelicula> getInfo(ArrayList<Pelicula> pelis){
		//TODO: IMPORTANTE quitar la línea siguiente, es solo una prueba;!!!!
		this.metodoPrueba(pelis.get(0));
		//TODO: IMPORTANTE quitar la línea anterior, es solo una prueba;!!!!
		for (int i=0;i<pelis.size();i++){
			//rellenaInfo(pelis.get(i));
		}
		return pelis;
	}
	
	private void rellenaInfo(Pelicula peli){
		URL urlPeli=null;
		try {
			urlPeli = new URL("http://www.imdb.es/find?s=tt&q="+peli.getTitulo().replaceAll(" ", "+"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//usamos el metodo que nos hace pasarnos por un navegador firefox para que imdb acepte la consulta
		String resultado=ExtractorWeb.downloadURLFirefox(urlPeli);
		//TODO: A partir de aquí que se pegue otro... xD
	}
	
	/**
	 * ESTE METODO ES DE PRUEBA. DEBE SER ELIMINADO!!
	 * @param peli
	 */
	private void metodoPrueba(Pelicula peli){
		if (peli!=null){
			URL urlPeli=null;
			try {
				urlPeli = new URL("http://www.imdb.es/find?s=tt&q="+peli.getTitulo().replaceAll(" ", "+"));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//usamos el metodo que nos hace pasarnos por un navegador firefox para que imdb acepte la consulta
			String resultado=ExtractorWeb.downloadURLFirefox(urlPeli);
			System.out.println(resultado);
		}
		
	}
	
}
