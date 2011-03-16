package clasesDePrueba;

import tads.ParamsConexionBD;
import tads.ProvinciasGDO;
import tads.ProvinciasGDO.Provincia;
import utils.BD;
import extractores.cines.ProcesadorCinesGDO;
import extractores.peliculas.ProcesadorCarteleraGDO;

public class PruebaFuncionamiento {

	public static void main(String args[]){
		BD bd= new BD();
		//ParamsConexionBD p=new ParamsConexionBD("root", "ssiipass", "jdbc:mysql://localhost:3306/mysql");
		ParamsConexionBD p=new ParamsConexionBD("root", "", "jdbc:mysql://localhost:3306/mysql");
		ParamsConexionBD p2=new ParamsConexionBD("rootNuevo","passNuevo","jdbc:mysql://localhost:3306/ssii");
		//Crea administrador
		bd.creaNuevoAdministrador(p,p2);
		//Crea la estructura de la bbdd. Si ya existía, la borra y vuelve a crearla
		bd.creaTablas(p);
		//Crea usuario que interactuará con la bbdd
		bd.creaUserDerechos(p, "userSSII", "passSSII");
		//ParamsConexionBD p=new ParamsConexionBD("userSSII","passSSII", "jdbc:mysql://localhost:3306/ssii");
		p=new ParamsConexionBD("userSSII","passSSII", "jdbc:mysql://localhost:3306/ssii");
		//Introduce las provincias 
		bd.introduceProvincias(p);

		//Introduce películas de álava
		//bd.actualizaPeliculas(p, ProvinciasGDO.Provincia.alava,false);
		
		//Introduce cines de álava
		//bd.actualizaCines(p, ProvinciasGDO.Provincia.alava,false);
		
		//Actualiza una provincia completamente, peliculas, cines y pases.
		//bd.actualizaProvincia(p, ProvinciasGDO.Provincia.badajoz,false);
		
		
		//Bucle que actualiza todas las provincias
		Provincia[] arrProv=ProvinciasGDO.Provincia.values();
		ProcesadorCinesGDO prCines=new ProcesadorCinesGDO();
		ProcesadorCarteleraGDO prPelis=new ProcesadorCarteleraGDO();
		for (ProvinciasGDO.Provincia prov : arrProv) {
			if (prov.ordinal()>=Provincia.cantabria.ordinal() && prov.ordinal()<=Provincia.zaragoza.ordinal()){
				bd.actualizaProvincia(prPelis, prCines,p, prov, false);
				int a=3;
				a++;
			}
		}
		
	}
}
