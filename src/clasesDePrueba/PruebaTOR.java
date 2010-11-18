package clasesDePrueba;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.silvertunnel.netlib.adapter.url.NetlibURLStreamHandlerFactory;
import org.silvertunnel.netlib.api.NetFactory;
import org.silvertunnel.netlib.api.NetLayer;
import org.silvertunnel.netlib.api.NetLayerIDs;

public class PruebaTOR {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//preparamos la conectividad TOR
			NetLayer lowerNetLayer = NetFactory.getInstance().getNetLayerById(NetLayerIDs.TOR); 
			lowerNetLayer.waitUntilReady();

			 // Preparamos la factor�a de URL's
			NetlibURLStreamHandlerFactory factory = new NetlibURLStreamHandlerFactory(false);
			// Asociamos nuestro TOR a la factor�a (se puede asociar a multiples factor�as)
			factory.setNetLayerForHttpHttpsFtp(lowerNetLayer);
			
			// Direcci�n URL que necesitamos
			String urlStr =  "http://madrid.salir.com/cartelera.html";
			// Encaminamos hacia hacia la factor�a
			URLStreamHandler handler = factory.createURLStreamHandler("http");
			URL context = null;
			// Y por fin creamos nuestra URL
			URL url = new URL(context, urlStr, handler);
			
			
			//(c�digo independiente de TOR) Realizamos la conezi�n
			URLConnection connection = url.openConnection();
			
			//Creamos un buffer para el flujo de entrada
			InputStream s = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s));
			
			//Recorremos las l�neas y las mostramos
			String line = "";
			while((line = bufferedReader.readLine())!=null){
			System.out.println(line);
			}
			

			bufferedReader.close();
			s.close();
			System.exit(0);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    

			

	}

}
