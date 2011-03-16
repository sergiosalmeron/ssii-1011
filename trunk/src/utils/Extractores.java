package utils;

import tads.ParamsConexionBD;
import extractores.cines.ProcesadorCinesGDO;
import extractores.peliculas.ProcesadorCarteleraGDO;

public class Extractores {

	private static ProcesadorCinesGDO procesadorCines=null;
	private static ProcesadorCarteleraGDO procesadorPelis=null;
	
	//TODO implementar para los parámetros (BD, TOR...)
	public static ProcesadorCinesGDO getInstanceCines(){
		if (procesadorCines==null)
			procesadorCines=new ProcesadorCinesGDO();
		return procesadorCines;
	}
	
	//TODO implementar para los parámetros (BD, TOR...)
	public static ProcesadorCarteleraGDO getInstancePelis(){
		if (procesadorPelis==null)
			procesadorPelis=new ProcesadorCarteleraGDO();
		return procesadorPelis;
	}
	
}
