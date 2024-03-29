package extractores.peliculas;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import tads.ParamsConexionBD;
import tads.Pelicula;
import tads.ProvinciasGDO;
import tads.ProvinciasGDO.Provincia;
import utils.BD;
import utils.ConecTor;



/**
 * Esta clase es la que procesa la cartelera de la web de la gu�a del ocio.
 * 
 *
 */
public class ProcesadorCarteleraGDO implements ProcesadorCartelera{
	
	private boolean usandoTor;
	private boolean stop;
	
	private BD bd;
	private ParamsConexionBD p;
	
	private static final String urlBase="http://www.guiadelocio.com/";
	private static final String toDos="enlaces/pelis.txt";
	private static final String dirImgs="carteles/";
	

	
	/**
	 * Constructora por defecto. No usa conexiones TOR con ConecTor
	 */
	public ProcesadorCarteleraGDO() {
		super();
		this.usandoTor = false;
		this.stop=false;
	}
	
	public void setBBDD(BD bd, ParamsConexionBD p){
		this.bd = bd;
		this.p = p;
	}
	
	public void usaTor(boolean torr){
		this.usandoTor=torr;
	}

	
	/**
	 * Constructora
	 * @param usandoTor Indica si las conexiones se realizar�n con TOR (true) o sin �l (false)
	 */
//	public ProcesadorCarteleraGDO(boolean usandoTor) {
//		super();
//		this.usandoTor = usandoTor;
//		this.stop=false;
//	}
//
//	/**
//	 * Constructora
//	 * @param bd La bbdd
//	 * @param p los par�metros de conexi�n a la bbdd
//	 */
//	public ProcesadorCarteleraGDO(BD bd, ParamsConexionBD p) {
//		super();
//		this.usandoTor = false;
//		this.bd = bd;
//		this.p = p;
//		this.stop=false;
//	}
//	
//	/**
//	 * Constructora
//	 * @param usandoTor Indica si las conexiones se realizar�n con TOR (true) o sin �l (false)
//	 * @param bd La bbdd
//	 * @param p los par�metros de conexi�n a la bbdd
//	 */
//	public ProcesadorCarteleraGDO(boolean usandoTor, BD bd, ParamsConexionBD p) {
//		super();
//		this.usandoTor = usandoTor;
//		this.bd = bd;
//		this.p = p;
//		this.stop=false;
//	}

	//Las pel�culas est�n limitadas por las etiquetas:
	//Etiqueta Inicio: <div class="gridType06 ftl clear">
	//Etiqueta Fin (primera etiqueta abierta tras el cierre de la etiqueta de inicio): <div id="footer" class="clear clr">
	
	/**
	 * M�todo de obtenci�n de las pel�culas proyectadas en una provincia.
	 */
	public ArrayList<Pelicula> getPeliculas(Provincia provincia) {
		
		ArrayList<URL> direcciones=getDirecciones(provincia);
		ArrayList<Pelicula> pelis=new ArrayList<Pelicula>();
		int i=0;
		while ((i<direcciones.size())&&(!stop)){
			URL direccion=direcciones.get(i);
			Pelicula peli=getPelicula(direccion);
			if (peli!=null)
				pelis.add(peli);
			i++;
		}
		if (stop)
			creaToDos(direcciones, i, provincia);
		stop=false;
		return pelis;
	}
	
	public ArrayList<Pelicula> getPeliculasRestantes(Provincia provincia) {
		
		ArrayList<URL> direcciones=cargaToDos();
		ArrayList<Pelicula> pelis=new ArrayList<Pelicula>();
		int i=0;
		while ((i<direcciones.size())&&(!stop)){
			URL direccion=direcciones.get(i);
			Pelicula peli=getPelicula(direccion);
			if (peli!=null)
				pelis.add(peli);
			i++;
		}
		if (stop)
			creaToDos(direcciones, i, provincia);
		stop=false;
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
				direccion = ConecTor.getURL(urlBase+"cartelera/"+ProvinciasGDO.getCodigo(provincia)+"?vista=3");
			else
				direccion = new URL(urlBase+"cartelera/"+ProvinciasGDO.getCodigo(provincia)+"?vista=3");
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
				dire="http://www.guiadelocio.com"+dire;
				if (consultarPeli(dire)){//si la peli ya esta en la bbdd, no la a�adimos
					try {
						if (usandoTor)
							direcciones.add(ConecTor.getURL(dire));
						else
							direcciones.add(new URL(dire));
					} catch (MalformedURLException e) {
					}
				}
			}
		}
		return direcciones;
	}
	
	/**
	 * M�todo para saber si una peli debe ser consultada en internet. Si la peli ya esta en la bbdd, la peli no se consultar� de nuevo
	 * @param direccion URL de la peli en GDO
	 * @return true si la peli no consta en la bbdd y false eoc
	 */
	private boolean consultarPeli(String direccion){
		boolean hayBD=(this.bd!=null)&&(this.p!=null);
		if (!hayBD)
			return true;
		else{
			return !this.bd.existePelicula(p, direccion);
		}
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
		
		peli.setRutaImg(getImagen(peli.getTitulo(), source));
		
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
		if (e!=null){
			String sino=e.getTextExtractor().toString();
			peli.setSinopsis(sino);
		}
	}
	
	private String getImagen(String ident, Source source){
		String urlImg = source.getFirstElementByClass("ftl shadow").getFirstElement("img").getAttributeValue("src");// .getElementById("main-content").getFirstElement("h2").getTextExtractor().toString();
		String[] aaa=urlImg.split("/");
		String nombreFichero=aaa[aaa.length-1];
		try{
			downloadImagen(urlBase+urlImg, nombreFichero);
			return dirImgs+nombreFichero;
		}catch (Exception e){
			return "";
		}		
	}
	
	private void downloadImagen(String urlIm, String titulo) throws IOException {
			URL url=null;
			if (usandoTor)
				url = ConecTor.getURL(urlIm);
			else
				url = new URL(urlIm);
			URLConnection urlCon = (URLConnection) url.openConnection();
			InputStream is = urlCon.getInputStream();
			FileOutputStream fos = new FileOutputStream(dirImgs+titulo);
			byte[] array = new byte[1000]; // buffer temporal de lectura.
			int leido = is.read(array);
			while (leido > 0) {
				fos.write(array, 0, leido);
				leido = is.read(array);
			}
			is.close();
			fos.close();
	}
	
	
	public void paralo(){
		stop=true;
	}
	
	private void creaToDos(ArrayList<URL> quereceres, int inicio, ProvinciasGDO.Provincia prov){
		System.out.println("Se van a guardar las pel�culas no procesadas...");
		boolean ok=true;
		File fic = new File(toDos);
		if (fic.exists()){
			fic.delete();
		}
		try {
			fic.createNewFile();
			if (fic.exists()){
				BufferedWriter bw = new BufferedWriter(new FileWriter(fic));
				bw.write(prov+"\n");
				for (int i=inicio;i<quereceres.size();i++){
					URL toDo=quereceres.get(i);
					bw.write(toDo+"\n");
				}
				bw.close();
			}
		} catch (IOException e) {
			ok=false;
			System.err.println("Error exportanto los enlaces de pel�culas");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ok)
			System.out.println("Finalizado con �xito el proceso de guardado de las pel�culas por procesar");
		else
			System.out.println("No se pudieron guardar las pel�culas por procesar");
	}
	
	private ArrayList<URL>  cargaToDos(){
		ArrayList<URL> quereceres=new ArrayList<URL>();
		File fic = new File(toDos);
		if (fic.exists()){
			try {
				BufferedReader bf = new BufferedReader(new FileReader(fic));
				String enlace;
				while ((enlace = bf.readLine())!=null) {
					if (enlace.contains("http://www")){
						quereceres.add(new URL(enlace));
					}
				} 
				bf.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return quereceres;
	}
	
	public Provincia getProvinciaRestantes(){
		Provincia resultado=null;
		File fic = new File(toDos);
		if (fic.exists()){
			try {
				BufferedReader bf = new BufferedReader(new FileReader(fic));
				String linea;
				if ((linea = bf.readLine())!=null)
					resultado=ProvinciasGDO.getProvincia(linea);
				bf.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		return resultado;
	}
	
	


	

}
