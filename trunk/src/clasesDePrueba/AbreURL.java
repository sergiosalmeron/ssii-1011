package clasesDePrueba;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AbreURL {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//Realizamos conexión con un URL
			URL url = new URL("http://madrid.salir.com/cartelera.html");
			URLConnection connection = url.openConnection();
			
			//Creamos un buffer para el flujo de entrada
			InputStream s = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s));
			
			//Recorremos las líneas y las mostramos
			String line = "";
			while((line = bufferedReader.readLine())!=null){
			System.out.println(line);
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
