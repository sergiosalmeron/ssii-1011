package Prueba;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ExtractorWeb {

	public static String downloadURL(URL url) {
		URLConnection connection = null;
		try {
			connection = url.openConnection();
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			
		} catch (IOException e1) {
			return null;
		}

		String line;
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.err.println(e.getLocalizedMessage());
			return null;
		}

		return builder.toString();
	}
	
	public static String downloadURLFirefox(URL url) {
		URLConnection connection = null;
		try {
			connection = url.openConnection();
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; es-ES; rv:1.9.0.10) Gecko/2009042316 Firefox/3.0.10");

		} catch (IOException e1) {
			return null;
		}

		String line;
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.err.println(e.getLocalizedMessage());
			return null;
		}

		return builder.toString();
	}
	
}
