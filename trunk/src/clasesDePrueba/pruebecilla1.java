package clasesDePrueba;

import java.io.IOException;
import java.util.ArrayList;
import Prueba.Cine;
import Prueba.ProcesadorCinesGDO;
import Prueba.ProvinciasGDO.Provincia;

public class pruebecilla1 {

	public static void main(String[] args) throws IOException {

		ProcesadorCinesGDO a=new ProcesadorCinesGDO();
		ArrayList<Cine> cines=a.getCines(Provincia.avila);
		for (Cine cine: cines)
			System.out.println(cine);
	/*	ProcesadorCarteleraGDO carte= new ProcesadorCarteleraGDO();
		ArrayList<Pelicula> pelis=carte.getPeliculas(Provincia.avila);
		for (Pelicula peli : pelis){
			System.out.println(peli);
		}*/
	}
}
