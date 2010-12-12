package clasesDePrueba;

import java.util.ArrayList;

import tads.Cine;
import tads.ParamsConexionBD;
import tads.Pelicula;
import tads.ProvinciasGDO;
import tads.Proyeccion;
import utils.BD;
import extractores.cines.ProcesadorCinesGDO;
import extractores.peliculas.ProcesadorCarteleraGDO;

public class prueba2 {

	public static void main(String args[]){
		BD bd= new BD();
		ParamsConexionBD p=new ParamsConexionBD("root", "ssiipass", "jdbc:mysql://localhost:3306/mysql");
		ParamsConexionBD p2=new ParamsConexionBD("rootNuevo","passNuevo","");
		//Crea administrador
		bd.creaNuevoAdministrador(p,p2);
		//Crea la estructura de la bbdd. Si ya existía, la borra y vuelve a crearla
		bd.creaTablas(p);
		//Crea usuario que interactuará con la bbdd
		bd.creaUserDerechos(p, "userSSII", "passSSII");
		p=new ParamsConexionBD("userSSII","passSSII", "jdbc:mysql://localhost:3306/ssii");
		//Introduce las provincias 
		bd.introduceProvincias(p);

		//Introduce películas de álava
		bd.actualizaPeliculas(p, ProvinciasGDO.Provincia.alava,false);
		
		//Introduce cines de álava
		bd.actualizaCines(p, ProvinciasGDO.Provincia.alava,false);
		
		//Actualiza una provincia completamente, peliculas, cines y pases.
		bd.actualizaProvincia(p, ProvinciasGDO.Provincia.burgos,false);
	}
}
