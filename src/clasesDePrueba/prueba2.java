package clasesDePrueba;

import java.util.ArrayList;

import extractores.peliculas.ProcesadorCarteleraGDO;
import tads.ParamsConexionBD;
import tads.Pelicula;
import tads.ProvinciasGDO;
import utils.BD;

public class prueba2 {

	public static void main(String args[]){
		BD bd= new BD();
		ParamsConexionBD p=new ParamsConexionBD("root", "ssiipass", "jdbc:mysql://localhost:3306/mysql");
		bd.creaNuevoAdministrador(p);
		bd.creaTablas(p);
		bd.creaUserDerechos(p, "userSSII", "passSSII");
		p=new ParamsConexionBD("userSSII","passSSII", "jdbc:mysql://localhost:3306/ssii");
		bd.introduceProvincias(p);

		//Introduce películas de ávila
		ProcesadorCarteleraGDO pr=new ProcesadorCarteleraGDO(false);
		ArrayList<Pelicula> arr= pr.getPeliculas(ProvinciasGDO.Provincia.alava);
		bd.introducePelicula(arr, p);
	}
}
