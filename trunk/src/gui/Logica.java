package gui;

import java.util.ArrayList;

import tads.Cine;
import tads.Pelicula;
import tads.ProvinciasGDO.Provincia;
import extractores.cines.ProcesadorCinesGDO;
import extractores.peliculas.ProcesadorCarteleraGDO;

public class Logica implements Runnable {

	private static boolean interrumpir;

	private static InterfazExtractor interfaz;

	
	public Logica (InterfazExtractor interfaz){
		this.interfaz=interfaz;
	}
	
	@Override
	public void run() {
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
	
	
	public void interrumpe(){
		interrumpir=true;
	}

}
