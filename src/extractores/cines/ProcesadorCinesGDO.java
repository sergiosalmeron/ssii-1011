package extractores.cines;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import tads.Cine;
import tads.DiasSemana;
import tads.ProvinciasGDO;
import tads.Proyeccion;
import tads.DiasSemana.EnumDias;
import tads.ProvinciasGDO.Provincia;
import utils.ConecTor;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

/**
 * Clase para la extracción de cines y pases de la página de la guía del ocio.
 * @author SSII
 *
 */
public class ProcesadorCinesGDO {
	//Parámetro consultado para ver si las conexiones se hacen por medio de la clase ConecTor o no
	private boolean usandoTor;
	
	/**
	 * Constructora por defecto. Las conexiones se hacen sin ConecTor
	 */
	public ProcesadorCinesGDO() {
		super();
		this.usandoTor =false;
	}
	/**
	 * Constructor
	 * @param usandoTor Especifica si las conexiones se hacen por medio de la clase ConecTor o no
	 */
	public ProcesadorCinesGDO(boolean usandoTor) {
		super();
		this.usandoTor = usandoTor;
	}

	/**
	 * Método que devuelve un arraylist con los cines (y sus pases) de una provincia.
	 * @param provincia La provincia
	 * @return Los cines
	 */
	public ArrayList<Cine> getCines(Provincia provincia) {
		
		ArrayList<Cine> cines=getDirecciones(provincia);
		for (Cine cine : cines){
			getCine(cine);
		}
		
		
		return cines;
	}
	
	
	/**
	 * A partir de la página principal de cines de la provincia a tratar, genera un arraylist
	 * de cines que solo tienen el nombre del cine y su URL de GDO
	 * @param provincia La provincia
	 * @return Las direcciones web de los cines dentro de GDO
	 */
	private ArrayList<Cine> getDirecciones(Provincia provincia){
		ArrayList<Cine> cines=new ArrayList<Cine>();
		//<div class="gridType06 ftl clear">
		URL direccion;
		Source source=null;
		try {
			if (usandoTor)
				direccion = ConecTor.getURL("http://www.guiadelocio.com/salas/"+ProvinciasGDO.getCodigo(provincia));
			else
				direccion = new URL("http://www.guiadelocio.com/salas/"+ProvinciasGDO.getCodigo(provincia));
			//direccion = ExtracTor.getURL("http://www.guiadelocio.com/cartelera/"+ProvinciasGDO.getCodigo(provincia)+"?vista=3");
			source=new Source(direccion);
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		//ArrayList <URL> direcciones=new ArrayList<URL>();
		List<? extends Segment> segments=source.getAllElementsByClass("moduleType101 borderb clear");
		for (Segment segment : segments) {
			
			String dire=segment.getFirstElement("a").getAttributeValue("href");
			String nombre=segment.getFirstElement("strong").getTextExtractor().toString();
			if ((dire!=null)&&(nombre!=null)){
				dire="http://www.guiadelocio.com"+dire;
				Cine cine=new Cine(dire, nombre);
				cine.setProvincia(provincia);
				cines.add(cine);
			}
		}
		
		return cines;
	}
	
	/**
	 * Método que, a partir de un objeto cine que solo tiene su dirección web de GDO, rellena sus atts
	 * @param cine Un objeto cine con su dirección web de GDO
	 * @return El objeto cine con todos sus atts rellenos
	 */
	private boolean getCine(Cine cine) {
		Source source=null;
		try {
			URL url;
			if (usandoTor)
				url=ConecTor.getURL(cine.getDirWebGDO());
			else
				url=new URL(cine.getDirWebGDO());
			source = new Source(url);
		} catch (MalformedURLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		//<div class="titIcon type11">
		String zona=source.getFirstElementByClass("titIcon type11").getFirstElement("h1").getTextExtractor().toString();
		if (zona!=null){
			try{
			zona=zona.split(",")[1].trim();
			cine.setZona(zona);
			}
			catch(Exception e){
			}
		}
		
		procesaTablaInfo(cine, procesaInfoCine(source));
		
		//moduleType106 clear
		String direccion=source.getFirstElementByClass("moduleType106 clear").getFirstElement("li").getTextExtractor().toString();
		if (direccion!=null){
			try{
				String city;
				String[] stringsDireccion=direccion.split(",");
				city=stringsDireccion[stringsDireccion.length-1].trim();
				direccion=direccion.split(":")[1].trim();
				cine.setDireccion(direccion);
				cine.setCiudad(city);
			}
			catch(Exception e){
			}
		}
		
		procesaPases(cine, source);
		
		
		return true;
	}


	/**
	 * Método que genera una tabla hash con los atributos mostrados de un cine en su web de GDO
	 * @param fuente El código de la página del cine
	 * @return La tabla hash con los atts y sus valores
	 */
	private Hashtable<String, String> procesaInfoCine(Source fuente){
		List <Element> atributos=fuente.getAllElementsByClass("infoContent").get(0).getAllElements("li");
		//System.out.println("-----------");
		Hashtable<String, String> tabla = new Hashtable<String, String>();
		for (Element atri : atributos){
			String cosa=atri.getTextExtractor().toString();
			int separador=cosa.indexOf(':');
			if ((separador>0) && (separador<cosa.length()-1))
				tabla.put(cosa.substring(0, separador), cosa.substring(separador+1, cosa.length()));
			//System.out.println(cosa);
		}
		//System.out.println(" ");
		return tabla;
	}
	/**
	 * Método que rellena los atts de un cine a partir de una tabla hash
	 * @param cine El cine a "rellenar"
	 * @param tabla La tabla con los valores de los atributos del cine
	 */
	private void procesaTablaInfo(Cine cine, Hashtable<String, String> tabla){
		Object aux;
		

		aux=tabla.get("Número de salas");
		if (aux!=null)
			cine.setNumSalas(Integer.valueOf(aux.toString().trim()));
		
		aux=tabla.get("Parking");
		if (aux!=null)
			cine.setParking(aux.toString());
		
		aux=tabla.get("Teléfono");
		if (aux!=null)
			cine.setTfno(aux.toString());
		
		aux=tabla.get("Web");
		if (aux!=null)
			cine.setWebOficial(aux.toString());
		
		aux=tabla.get("Venta de entradas");
		if (aux!=null)
			cine.setUrlCompras(aux.toString());
		
	}
	
	/**
	 * Método que inserta en un cine todos los pases de la semana
	 * @param cine El cine en el que se incluirán los pases
	 * @param source La página web en GDO del cine
	 */
	private void procesaPases(Cine cine, Source source) {
		// <div class="contentSesiones">
		List<? extends Segment> bloques=source.getAllElementsByClass("contentSesiones");
		for (Segment bloque : bloques) {
			List<Element> enlaces=bloque.getAllElements("a");
			String urlPeli=null;
			String urlUltimoDia=null;
			EnumDias ultimoDia=EnumDias.Martes;
			for (Element enlace : enlaces){
				String link=enlace.getAttributeValue("href");
				if (link.indexOf("/cine/archivo")>-1){
					urlPeli="http://www.guiadelocio.com"+link;
				}
				
				String ultimoDiaStr=DiasSemana.getDiaString(ultimoDia).toLowerCase();
				if ((enlace.getTextExtractor().toString().indexOf(ultimoDiaStr)>-1) && (link.indexOf("/ezjscore/call")>-1)){
					urlUltimoDia="http://www.guiadelocio.com"+link;
				}
			}
			
			if ((urlPeli!=null)&&(urlUltimoDia!=null)){
				String[] urlHorarios=generaUrlHorarios(urlUltimoDia);
				addPases(cine, urlPeli, urlHorarios, ultimoDia);
			}
			
			
			//String urlPeli=segment.getFirstElement("a").getAttributeValue("href");
			//ArrayList<Proyeccion> urlHoras=generaPases(segment);
		}

		
	}

	/**
	 * Debido a que los horarios de los pases de cada pelicula y cine están en una dirección web distinta
	 * tenemos que obtener las direcciones de cada uno de los horarios.
	 * @param urlJueves La URL con los horarios de los pases del último día
	 * @return Un arrayList de direcciones web calculadas a partir de la url del jueves
	 */
	private String[] generaUrlHorarios(String urlUltimoDia) {
		String[] resultado=new String[7];
		String[] campos=urlUltimoDia.split("::");
		String urlBase="";
		for (int i=0;i<campos.length-1;i++){
			urlBase=urlBase+campos[i]+"::";
		}
		int valorUltimoDia=Integer.valueOf(campos[campos.length-1]);
		for (int i=1;i<=6;i++){
			resultado[i-1]=urlBase+(valorUltimoDia-((7-i)*86400));
		}
		resultado[6]=urlUltimoDia;
		return resultado;
	}

	/**
	 * Método que, a partir de los horarios de los pases de una película, incluye en el Cine 
	 * los pases.
	 * @param cine El Cine en el que se incluirán los pases
	 * @param urlPeli La url de la película (usada a modo de clave en el pase)
	 * @param urlHorarios Las direcciones con los horarios de los pases de la película esta semana
	 * @param ultimoDia 
	 */
	private void addPases(Cine cine, String urlPeli, String[] urlHorarios, EnumDias ultimoDia) {
		for (int i=0;i<urlHorarios.length;i++){
			String url= urlHorarios[i];
			Source source=null;
			try {
				URL direccion;
				if (usandoTor)
					direccion = ConecTor.getURL(url);
				else
					direccion = new URL(url);
				//URL direccion = ExtracTor.getURL("http://www.guiadelocio.com/cartelera/"+ProvinciasGDO.getCodigo(provincia)+"?vista=3");
				source=new Source(direccion);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (source!=null){
				List<Element> horas=source.getAllElements("li");
				for (Element hora: horas){
					String strHora=hora.getTextExtractor().toString().trim();
					if (strHora.length()>0){
						Proyeccion pase=new Proyeccion(urlPeli, getDia(i, ultimoDia), strHora);
						cine.addPase(pase);
					}
				}
			}
		}
		
	}
	
	/**
	 * 
	 * @param i el número de día de la semana (partiendo de que el viernes es el valor 0)
	 * @return el enumerado del día
	 */
	/*private EnumDias getDia(int i, EnumDias ultimoDia){
		EnumDias resultado=null;
		((ArrayList)EnumDias.values()).indexOf(null);
		switch (i){
			case 0:
				resultado=DiasSemana.getDiaEnum("Viernes");
				break;
			case 1:
				resultado=DiasSemana.getDiaEnum("Sábado");
				break;
			case 2:
				resultado=DiasSemana.getDiaEnum("Domingo");
				break;
			case 3:
				resultado=DiasSemana.getDiaEnum("Lunes");
				break;
			case 4:
				resultado=DiasSemana.getDiaEnum("Martes");
				break;
			case 5:
				resultado=DiasSemana.getDiaEnum("Miércoles");
				break;
			case 6:
				resultado=DiasSemana.getDiaEnum("Jueves");
				break;
		}
		return resultado;
	}*/
	private EnumDias getDia(int i, EnumDias ultimoDia){
		int base=getValorBase(ultimoDia);
		int valor=(base+i+1)%7;
		return EnumDias.values()[valor];
	}
	
	private int getValorBase(EnumDias ultimoDia){
		EnumDias[] arr=EnumDias.values();
		int resultado=0;
		boolean encontrado=false;
		for (EnumDias a:arr){
			if (a==ultimoDia)
				encontrado=true;
			if (!encontrado)
				resultado++;
		}
		return resultado;
	}



}
