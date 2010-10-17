package clasesDePrueba;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class AbreURL {

	private ArrayList<String> a;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//Realizamos conexión con un URL.
			URL url = new URL("http://www.guiadelocio.com/cartelera/madrid?vista=3");
			URLConnection connection = url.openConnection();
			
			//Creamos un buffer para el flujo de entrada.
			InputStream s = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s));
			
			//Recorremos las líneas y las mostramos.
			String line = "";
			while((line = bufferedReader.readLine())!=null){
				//System.out.println(line);
				muestraPeli(line);
				muestraLugar(line);
				muestraCine(line);
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Busca la etiqueta etiqInicio en una linea y muestra por pantalla su contenido
	 * @param linea La línea del html
	 * @return True si la linea contiene la etiqueta buscada, False EOC.
	 */
	private static boolean muestraPeli(String linea){
		//etiqInicio y etiqFin son las etiquetas que contienen UNICAMENTE el título de la peli (etiqFin debe ser la etiqueta de cierre de etiqInicio) 
		String etiqInicio="<spam class=\"titulotexto\">";
		String etiqFin="</spam>";
		int inicio=linea.indexOf(etiqInicio);
		if (inicio>-1){
			inicio=inicio+etiqInicio.length();
			int fin=linea.indexOf(etiqFin, inicio+1);
			if (fin>-1){
				System.out.println(linea.substring(inicio, fin));
				return true;
			}
			
		}
		return false;
	}
	
	/**
	 * Busca la etiqueta etiqInicio en una linea y muestra por pantalla su contenido
	 * @param linea La línea del html
	 * @return True si la linea contiene la etiqueta buscada, False EOC.
	 */
	private static boolean muestraLugar(String linea){
		//etiqInicio y etiqFin son las etiquetas que contienen UNICAMENTE el título de la peli (etiqFin debe ser la etiqueta de cierre de etiqInicio) 
		String etiqInicio="<spam class=\"vistatexto\">";
		String etiqFin="</spam>";
		int inicio=linea.indexOf(etiqInicio);
		if (inicio>-1){
			inicio=inicio+etiqInicio.length();
			int fin=linea.indexOf(etiqFin, inicio+1);
			if (fin>-1){
				System.out.println("***"+linea.substring(inicio, fin));
				return true;
			}
			
		}
		return false;
	}
	
	/**
	 * Busca la etiqueta etiqInicio en una linea y muestra por pantalla su contenido.
	 * Puesto que en este caso la etiqueta de inicio es un enlace (y cada enlace es distinto), se busca la parte común de la etiqueta.
	 * @param linea La línea del html
	 * @return True si la linea contiene la etiqueta buscada, False EOC.
	 */
	private static boolean muestraCine(String linea){
		//etiqInicio y etiqFin son las etiquetas que contienen UNICAMENTE el título de la peli (etiqFin debe ser la etiqueta de cierre de etiqInicio) 
		String etiqInicioStart="<a href=\"/cine/madrid/";
		String etiqInicioFin="\">";
		String etiqFin="</a>";
		int inicio=linea.indexOf(etiqInicioStart);
		if (inicio>-1){
			inicio=linea.indexOf(etiqInicioFin, inicio+1) + etiqInicioFin.length();
			int fin=linea.indexOf(etiqFin, inicio+1);
			if (fin>-1){
				System.out.println("--->"+linea.substring(inicio, fin));
				return true;
			}
			
		}
		return false;
	}
	

}
