package utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;

import org.silvertunnel.netlib.adapter.url.NetlibURLStreamHandlerFactory;
import org.silvertunnel.netlib.api.NetFactory;
import org.silvertunnel.netlib.api.NetLayer;
import org.silvertunnel.netlib.api.NetLayerIDs;

public class ConecTor {

	public static URL getURL(String urlStr) throws MalformedURLException {
		//preparamos la conectividad TOR
		NetLayer lowerNetLayer = NetFactory.getInstance().getNetLayerById(NetLayerIDs.TOR); 
		lowerNetLayer.waitUntilReady();

		 // Preparamos la factor�a de URL's
		NetlibURLStreamHandlerFactory factory = new NetlibURLStreamHandlerFactory(false);
		// Asociamos nuestro TOR a la factor�a (se puede asociar a multiples factor�as)
		factory.setNetLayerForHttpHttpsFtp(lowerNetLayer);
		

		// Encaminamos hacia hacia la factor�a
		URLStreamHandler handler = factory.createURLStreamHandler("http");
		URL context = null;
		// Y por fin creamos nuestra URL
		return new URL(context, urlStr, handler);
	}
}
