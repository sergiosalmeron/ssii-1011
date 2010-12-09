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

import Prueba.DiasSemana.EnumDias;
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
		// <div class="contentSesiones">
		List<? extends Segment> bloques=source.getAllElementsByClass("contentSesiones");
		for (Segment bloque : bloques) {
			List<Element> enlaces=bloque.getAllElements("a");
			String urlPeli=null;
			String urlJueves=null;
			for (Element enlace : enlaces){
				String link=enlace.getAttributeValue("href");
				if (link.indexOf("/cine/archivo")>-1){
					urlPeli="http://www.guiadelocio.com"+link;
				}
				if ((enlace.getTextExtractor().toString().indexOf("jueves")>-1) && (link.indexOf("/ezjscore/call")>-1)){
					urlJueves="http://www.guiadelocio.com"+link;
				}
			}
			
			if ((urlPeli!=null)&&(urlJueves!=null)){
				String[] urlHorarios=generaUrlHorarios(urlJueves);
				addPases(cine, urlPeli, urlHorarios);
			}
			
			
			//String urlPeli=segment.getFirstElement("a").getAttributeValue("href");
			//ArrayList<Proyeccion> urlHoras=generaPases(segment);
		}

		
	}


	private String[] generaUrlHorarios(String urlJueves) {
		String[] resultado=new String[7];
		String[] campos=urlJueves.split("::");
		String urlBase="";
		for (int i=0;i<campos.length-1;i++){
			urlBase=urlBase+campos[i]+"::";
		}
		int valorJueves=Integer.valueOf(campos[campos.length-1]);
		for (int i=1;i<=6;i++){
			resultado[i-1]=urlBase+(valorJueves-((7-i)*86400));
		}
		resultado[6]=urlJueves;
		return resultado;
	}

	private void addPases(Cine cine, String urlPeli, String[] urlHorarios) {
		for (int i=0;i<urlHorarios.length;i++){
			String url= urlHorarios[i];
			Source source=null;
			try {
				URL direccion = new URL(url);
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
						Proyeccion pase=new Proyeccion(urlPeli, getDia(i), strHora);
						cine.addPase(pase);
					}
				}
			}
		}
		
	}
	
	private EnumDias getDia(int i){
		EnumDias resultado=null;
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
	}



}
