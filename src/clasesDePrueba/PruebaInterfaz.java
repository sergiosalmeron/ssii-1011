package clasesDePrueba;

import java.io.IOException;
import java.util.ArrayList;

import extractores.cines.ProcesadorCinesGDO;
import extractores.peliculas.ProcesadorCarteleraGDO;
import gui.InterfazExtractor;

import tads.Cine;
import tads.Pelicula;
import tads.ProvinciasGDO.Provincia;

public class PruebaInterfaz {
	
	private static InterfazExtractor interfaz;
	private static boolean interrumpir;

	public static void main(String[] args) throws IOException {

		/*ProcesadorCinesGDO a=new ProcesadorCinesGDO();
		ArrayList<Cine> cines=a.getCines(Provincia.avila);
		for (Cine cine: cines)
			System.out.println(cine);*/
		/*ProcesadorCarteleraGDO carte= new ProcesadorCarteleraGDO();
		ArrayList<Pelicula> pelis=carte.getPeliculas(Provincia.avila);
		for (Pelicula peli : pelis){
			System.out.println(peli);
			System.out.println(peli.getSinopsis());
		}
		*/
		interfaz= new InterfazExtractor();
	}
	
	public static void extrae(){
		Provincia[] provincias=Provincia.values();
		
		ArrayList<Cine> cines=new ArrayList<Cine>();
		ArrayList<Pelicula> pelis= new ArrayList<Pelicula>();
		
		ProcesadorCinesGDO procCines=new ProcesadorCinesGDO();
		ProcesadorCarteleraGDO procCarte= new ProcesadorCarteleraGDO();
		for (int i=0;i<provincias.length;i++){
			if (!interrumpir){
				interfaz.Informa(i, 0);
				pelis.addAll(procCarte.getPeliculas(provincias[i]));	
			}
			
		}
		
		for (int i=0;i<provincias.length;i++){
			if (!interrumpir){
				interfaz.Informa(i, 1);
				cines.addAll(procCines.getCines(provincias[i]));
			}
		}
		
			interfaz.finalizaProceso(!interrumpir);
			interrumpir=false;
	}
	
	public static void interrumpe(){
		interrumpir=true;
	}
	
	
}
