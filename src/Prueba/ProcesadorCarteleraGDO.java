package Prueba;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * Esta clase es la que procesa la cartelera de la web de la guía del ocio.
 * 
 *
 */
public class ProcesadorCarteleraGDO implements ProcesadorCartelera{
	
	//Las películas están limitadas por las etiquetas:
	//Etiqueta Inicio: <div class="gridType06 ftl clear">
	//Etiqueta Fin (primera etiqueta abierta tras el cierre de la etiqueta de inicio): <div id="footer" class="clear clr">
	
	public ArrayList<Pelicula> getPeliculas() {
		URL url=null;
		try {
			url = new URL("http://www.guiadelocio.com/cartelera/madrid?vista=3");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String web=ExtractorWeb.downloadURL(url);
		
		int inicio=web.indexOf("gridType06 ftl clear");
		int fin=web.indexOf("footer");
		if ( (inicio>0) && (fin>inicio) )
			return buscaPeliculas(web.substring(inicio, fin));
		else
			return null;
		
	}
	
	private ArrayList<Pelicula> buscaPeliculas (String web){
		ArrayList<Pelicula> pelis= new ArrayList<Pelicula>();
		String tagInicioPeli="titulotexto\">";
		String tagFinPeli="</spam>";
		int inicioTitulo=web.indexOf(tagInicioPeli);
		while (inicioTitulo>0){
			inicioTitulo=inicioTitulo+tagInicioPeli.length();
			int finTitulo=web.indexOf(tagFinPeli, inicioTitulo);
			String titulo=web.substring(inicioTitulo, finTitulo);
			pelis.add(new Pelicula(titulo));
			//TODO: procesar los cines aqui
			inicioTitulo=web.indexOf(tagInicioPeli, finTitulo);
		}
		
		
		
		return pelis;
	}

}
