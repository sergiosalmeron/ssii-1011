package extractores.peliculas;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import tads.Pelicula;
import tads.ProvinciasGDO;
import tads.ProvinciasGDO.Provincia;
import utils.ConecTor;


import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;



/**
 * Esta clase es la que procesa la cartelera de la web de la gu�a del ocio.
 * 
 *
 */
public class ProcesadorCarteleraGDO implements ProcesadorCartelera{
	
	private boolean usandoTor;
	/**
	 * Constructora por defecto. No usa conexiones TOR con ConecTor
	 */
	public ProcesadorCarteleraGDO() {
		super();
		this.usandoTor = false;
	}
	
	/**
	 * Constructora
	 * @param usandoTor Indica si las conexiones se realizar�n con TOR (true) o sin �l (false)
	 */
	public ProcesadorCarteleraGDO(boolean usandoTor) {
		super();
		this.usandoTor = usandoTor;
	}


	//Las pel�culas est�n limitadas por las etiquetas:
	//Etiqueta Inicio: <div class="gridType06 ftl clear">
	//Etiqueta Fin (primera etiqueta abierta tras el cierre de la etiqueta de inicio): <div id="footer" class="clear clr">
	
	/**
	 * M�todo de obtenci�n de las pel�culas proyectadas en una provincia.
	 */
	public ArrayList<Pelicula> getPeliculas(Provincia provincia) {
		
		ArrayList<URL> direcciones=getDirecciones(provincia);
		ArrayList<Pelicula> pelis=new ArrayList<Pelicula>();
		for (URL direccion : direcciones){
			Pelicula peli=getPelicula(direccion);
			if (peli!=null)
				pelis.add(peli);
		}
		return pelis;
	}
	
	
	/**
	 * Extrae la url de cada pel�cula que se proyecta en la provincia deseada
	 * @param provincia La provincia de la que se desea obtener las urls de las pel�culas
	 * @return un arraylist de urls
	 */
	private ArrayList<URL> getDirecciones(Provincia provincia){
		//etiqueta padre de <spam class="titulotexto">
		//etiqueta nieta de <p class="texto withBolo01"> con enlace
		URL direccion;
		Source source=null;
		try {
			if (usandoTor)
				direccion = ConecTor.getURL("http://www.guiadelocio.com/cartelera/"+ProvinciasGDO.getCodigo(provincia)+"?vista=3");
			else
				direccion = new URL("http://www.guiadelocio.com/cartelera/"+ProvinciasGDO.getCodigo(provincia)+"?vista=3");
			source=new Source(direccion);
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		ArrayList <URL> direcciones=new ArrayList<URL>();
		List<? extends Segment> segments=source.getAllElementsByClass("texto withBolo01");
		for (Segment segment : segments) {
			String dire=segment.getFirstElement("a").getAttributeValue("href");
			if (dire!=null){
				try {
					if (usandoTor)
						direcciones.add(ConecTor.getURL("http://www.guiadelocio.com"+dire));
					else
						direcciones.add(new URL("http://www.guiadelocio.com"+dire));
					//direcciones.add(ExtracTor.getURL("http://www.guiadelocio.com"+dire));
				} catch (MalformedURLException e) {
				}
			}
		}
		return direcciones;
	}
	
	/**
	 * Crea un objeto pel�cula a partir de la informaci�n obtenida de la web
	 * @param direccion Direcci�n web de la que se obtendr� la informaci�n para generar la pel�cula
	 * @return La pel�cula creada
	 */
	private Pelicula getPelicula(URL direccion){
		Source source=null;
		try {
			source = new Source(direccion);
		} catch (IOException e) {
			return null;
		}
		//T�tulo: contenido en el <h1 class="nobg ftl">
		
		String titulo=source.getFirstElementByClass("nobg ftl").getTextExtractor().toString();// .getElementById("main-content").getFirstElement("h2").getTextExtractor().toString();
		
		//Info:PRIMER elemento contenido en <div class="infoContent">
		
		/*Pelicula peli=new Pelicula(titulo);
		peli.setDirWeb(direccion.toString());
		System.out.println("-----------");
		System.out.println(titulo);*/
		Pelicula peli=creaPelicula(titulo, procesaInfoPelicula(source));
		peli.setDirWeb(direccion.toString());
		setSinopsis(peli, source);
		return peli;
	}
	
	/**
	 * Crea una tabla hash a partir del cuadro que contiene la informaci�n de la peli.
	 * @param fuente la web de la que se extraer� la informaci�n
	 * @return La tabla hash con la informaci�n de la pel�cula
	 */
	private Hashtable<String, String> procesaInfoPelicula(Source fuente){
		List <Element> atributos=fuente.getAllElementsByClass("infoContent").get(0).getAllElements("li");
		//System.out.println("-----------");
		Hashtable<String, String> tabla = new Hashtable<String, String>();
		for (Element atri : atributos){
			String cosa=atri.getTextExtractor().toString();
			int separador=cosa.indexOf(':');
			if ((separador>0) && (separador<cosa.length()-1)){
				String valorAtt=cosa.substring(separador+1, cosa.length()).trim();
				if (valorAtt.endsWith(".")){
					valorAtt=valorAtt.substring(0, valorAtt.length()-1).trim();
				}
				tabla.put(cosa.substring(0, separador), valorAtt);
			}
			//System.out.println(cosa);
		}
		//System.out.println(" ");
		return tabla;
	}
	
	/**
	 * Crea el objeto Pel�cula y lo rellena con la informaci�n de la tabla hash.
	 * @param titulo El t�tulo de la pel�cula
	 * @param tabla La tabla con el resto de informaci�n de la pel�cula
	 * @return El objeto pel�cula con toda la informaci�n
	 */
	private Pelicula creaPelicula(String titulo, Hashtable<String, String> tabla){
		Pelicula peli=new Pelicula(titulo);
		Object aux;
		
		//procesamos el director
		aux=tabla.get("Director");
		if (aux!=null)
			peli.setDirector(aux.toString());
		
		
		int posicion=0;		
		//procesamos la duraci�n
		aux=tabla.get("Duraci�n");
		if (aux!=null){
			posicion=aux.toString().indexOf("min");
			if (posicion>0)
				peli.setDuracion(Integer.valueOf(aux.toString().substring(0,posicion).trim()));
		}
		
		//procesamos los int�rpretes
		aux=tabla.get("Int�rpretes");
		if (aux!=null)
			peli.setInterpretes(aux.toString());
		
		//procesamos la nacionalidad
		aux=tabla.get("Nacionalidad y a�o de producci�n");
		if (aux!=null)
			peli.setNacionalidad(aux.toString());
		
		//procesamos el estreno
		aux=tabla.get("Fecha de estreno");
		if (aux!=null){
			peli.setEstreno(aux.toString());
			try{
				//formato dd / mm / aaaa.
				int anio=Integer.valueOf(aux.toString().split("/")[2].trim());
				peli.setAnio(anio);
			}
			catch (Exception e){
				System.err.println("No se pudo extraer el a�o de la fecha de estreno de la pel�cula "+titulo);
			}
			
			
		}
		
		//procesamos el G�nero
		aux=tabla.get("G�nero");
		if (aux!=null)
			peli.setGenero(aux.toString());
		
		aux=tabla.get("Productora");
		if (aux!=null)
			peli.setProductora(aux.toString());
		
		aux=tabla.get("Guionista");
		if (aux!=null)
			peli.setGuionista(aux.toString());
		
		aux=tabla.get("Distribuidora");
		if (aux!=null)
			peli.setDistribuidora(aux.toString());
		
		aux=tabla.get("Calificaci�n");
		if (aux!=null){
			boolean puesta=false;
			if (aux.toString().indexOf("12")>0){
				peli.setCalificacion(12);
				puesta=true;
			}
			if (aux.toString().indexOf("13")>0){
				peli.setCalificacion(13);
				puesta=true;
			}
			if (aux.toString().indexOf("16")>0){
				peli.setCalificacion(16);
				puesta=true;
			}
			if (aux.toString().indexOf("18")>0){
				peli.setCalificacion(18);
				puesta=true;
			}
			if (aux.toString().indexOf("7")>0){
				peli.setCalificacion(7);
				puesta=true;
			}
			if (aux.toString().indexOf("Pendiente por calificar")>0){
				peli.setCalificacion(-1);
				puesta=true;
			}
			if (!puesta)
				peli.setCalificacion(0);
			
		}
		
		return peli;
	}
	
	/**
	 * M�todo que a�ade la sinopsis a la pel�cula
	 * @param peli La pel�cula
	 * @param pag El c�digo de la p�gina de la peli
	 */
	private void setSinopsis(Pelicula peli, Source pag){
		Element e=pag.getFirstElementByClass("wysiwyg");
		String sino=e.getTextExtractor().toString();
		peli.setSinopsis(sino);
	}

	

}
