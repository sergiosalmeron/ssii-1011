package gui;

import extractores.cines.ProcesadorCinesGDO;
import extractores.peliculas.ProcesadorCarteleraGDO;

import java.util.ArrayList;

import tads.Cine;
import tads.ModoFuncionamiento;
import tads.Pelicula;
import tads.ProvinciasGDO;
import tads.ProvinciasGDO.Provincia;

public class Logica implements Runnable {

	private static boolean interrumpir;

	private static InterfazExtractor interfaz;

	
	public Logica (InterfazExtractor interfaz){
		this.interfaz=interfaz;
	}
	
	@Override
	public void run() {
		Provincia[] arrProv=Provincia.values();
		
		ModoFuncionamiento modo=this.interfaz.getFuncionamiento();
		boolean actualizar=this.interfaz.isActualizar();
		//String prov=this.interfaz.getProvinciaSeleccionada();
		Provincia provincia=ProvinciasGDO.getProvincia(this.interfaz.getProvinciaSeleccionada());
		
		switch (modo) {
		case PELI:  ArrayList<Pelicula> pelis=new ArrayList<Pelicula>();
					ProcesadorCarteleraGDO procCarte= new ProcesadorCarteleraGDO();
					
					//Actualiza desde esa provincia
					for (ProvinciasGDO.Provincia provIterada : arrProv) {
						if (provincia.ordinal()>=provIterada.ordinal() && (!interrumpir)){
							interfaz.informa(provincia.ordinal(), 0);
							//TODO Falta la actualización propiamente dicha
							pelis.addAll(procCarte.getPeliculas(provincia));
							//bd.actualizaProvincia(p, prov, false);
						}
					}
		break;
		case CINE:	ArrayList<Cine> cines=new ArrayList<Cine>();
					ProcesadorCinesGDO procCines= new ProcesadorCinesGDO();
					
					//Actualiza desde esa provincia
					for (ProvinciasGDO.Provincia provIterada : arrProv) {
						if (provincia.ordinal()>=provIterada.ordinal() && (!interrumpir)){
							interfaz.informa(provincia.ordinal(), 1);
							//TODO Falta la actualización propiamente dicha
							cines.addAll(procCines.getCines(provincia));
							//bd.actualizaProvincia(p, prov, false);
						}
					}
			break;
		case SESION:ArrayList<Cine> cinesSesion=new ArrayList<Cine>();
					ProcesadorCinesGDO procCinesSesion= new ProcesadorCinesGDO();
					
					//Actualiza desde esa provincia
					for (ProvinciasGDO.Provincia provIterada : arrProv) {
						if (provincia.ordinal()>=provIterada.ordinal() && (!interrumpir)){
							interfaz.informa(provincia.ordinal(), 2);
							//TODO ¡¡Falta Todo!!!
						}
					}
			break;
		case PROVINCIA:	pelis=new ArrayList<Pelicula>();
						cines=new ArrayList<Cine>();
						procCines= new ProcesadorCinesGDO();
						procCarte= new ProcesadorCarteleraGDO();
						
						for (ProvinciasGDO.Provincia provIterada : arrProv) {
							//Actualiza desde esa provincia
							//Peliculas
							if (provincia.ordinal()>=provIterada.ordinal() && (!interrumpir)){
								interfaz.informa(provincia.ordinal(),0);
								//TODO Falta la actualización propiamente dicha
								pelis.addAll(procCarte.getPeliculas(provincia));
								//bd.actualizaProvincia(p, prov, false);
							}
							//Cines (y sesiones!)
							if (provincia.ordinal()>=provIterada.ordinal() && (!interrumpir)){
								interfaz.informa(provincia.ordinal(),1);
								//TODO Falta la actualización propiamente dicha
								cines.addAll(procCines.getCines(provincia));
								//bd.actualizaProvincia(p, prov, false);
							}
						}
		break;
		default:
			break;
		}
		
		/*ArrayList<Cine> cines=new ArrayList<Cine>();
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
			interrumpir=false;*/
	}
	
	
	public void interrumpe(){
		interrumpir=true;
	}

}
