package clasesDePrueba;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

import Prueba.ExtracTor;
import Prueba.ExtractorWeb;
import Prueba.ProcesadorCarteleraSalir;

public class pruebecilla1 {

	public static void main(String[] args) throws IOException {
		URL direccion=ExtracTor.getURL("http://www.salir.com/madrid/cartelera.html");
		ProcesadorCarteleraSalir a=new ProcesadorCarteleraSalir();
		a.getPeliculas();
		//URL direccion=new URL("http://www.salir.com/madrid/cartelera.html");
		Source source=new Source(direccion);
		System.out.println("-------");
		System.out.println("-------");
		System.out.println("-------");
		System.out.println("-------");
		List<? extends Segment> segments=source.getAllElements("td\nclass=\"titulo\"");
		for (Segment segment : segments) {
			System.out.println(segment.getAllElements("a").get(0).getAttributeValue("href"));
			System.out.println("--");
		}
	}
}

//td class="teaserleftodd"><