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
		
		//1-Nos bajamos la cartelera
		ProcesadorCarteleraGDO proce=new ProcesadorCarteleraGDO();
		ArrayList<Pelicula> pelis=proce.getPeliculas(null);
		
		//2-Rellenamos la info de las pelis con la IMDB
		IMDBExtractor imdb=new IMDBExtractor();
		pelis=imdb.getInfo(pelis);
	}

}
