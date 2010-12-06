package Prueba;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

import Prueba.ProvinciasGDO.Provincia;

public class ProcesadorCinesGDO {
	
	public ArrayList<Cine> getCines(Provincia provincia) {
		
		ArrayList<Cine> cines=getDirecciones(provincia);
		for (Cine cine : cines){
			getCine(cine);
		}
		
		
		return cines;
	}
	
	

	private ArrayList<Cine> getDirecciones(Provincia provincia){
		ArrayList<Cine> cines=new ArrayList<Cine>();
		//<div class="gridType06 ftl clear">
		URL direccion;
		Source source=null;
		try {
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
	
	
	
	
	
	private boolean getCine(Cine cine) {
		Source source=null;
		try {
			source = new Source(new URL(cine.getDirWebGDO()));
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
				direccion=direccion.split(":")[1].trim();
				cine.setDireccion(direccion);
			}
			catch(Exception e){
			}
		}
		
		procesaPases(cine, source);
		
		
		return true;
	}



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
	
	private void procesaPases(Cine cine, Source source) {
		// TODO Auto-generated method stub
		
	}

}
