package clasesDePrueba;

import java.io.IOException;
import java.util.ArrayList;

import tads.Cine;
import tads.Pelicula;
import tads.ProvinciasGDO.Provincia;
import extractores.cines.ProcesadorCinesGDO;
import extractores.peliculas.ProcesadorCarteleraGDO;

public class OtraClaseDePrueba {

	public static void main(String[] args) throws IOException {
		/*Provincia[] provincias=Provincia.values();
		
		ArrayList<Cine> cines=new ArrayList<Cine>();
		
		ProcesadorCinesGDO procCines=new ProcesadorCinesGDO();
		
		for (int i=0;i<provincias.length;i++){
			//if (!interrumpir){
				//interfaz.Informa(i, 1);
				cines.addAll(procCines.getCines(provincias[i]));
			//}
		}*/
		//ProcesadorCarteleraGDO a=new ProcesadorCarteleraGDO();
		//a.getPeliculasRestantes(null);
		
		ProcesadorCinesGDO a = new ProcesadorCinesGDO();
		//a.getCines(Provincia.granada);
		a.getCinesRestantes(Provincia.granada);
	}
	
}
