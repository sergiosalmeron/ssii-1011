package gui;

import extractores.cines.ProcesadorCinesGDO;
import extractores.peliculas.ProcesadorCarteleraGDO;

import java.util.ArrayList;

import tads.Cine;
import tads.ModoFuncionamiento;
import tads.ParamsConexionBD;
import tads.Pelicula;
import tads.ProvinciasGDO;
import tads.ProvinciasGDO.Provincia;
import utils.BD;

public class Logica implements Runnable {

	private static boolean interrumpir;

	private static InterfazExtractor interfaz;
	
	private ProcesadorCarteleraGDO procCarte;
	private ProcesadorCinesGDO procCines;
	private ParamsConexionBD params;
	private final BD bd=new BD();


	
	public Logica (InterfazExtractor interfaz){
		this.interfaz=interfaz;
		procCarte= new ProcesadorCarteleraGDO();
		procCines= new ProcesadorCinesGDO();
		interrumpir=false;

	}
	
	@Override
	public void run() {
		Provincia[] arrProv;///=Provincia.values();
		
		ModoFuncionamiento modo=this.interfaz.getFuncionamiento();
		boolean actualizar=this.interfaz.isActualizar();
		//String prov=this.interfaz.getProvinciaSeleccionada();
		///Provincia provincia=ProvinciasGDO.getProvincia(this.interfaz.getNombreProvinciaSeleccionada());
		Provincia provincia=this.interfaz.getProvinciaSeleccionada();
		
		if (provincia==null)
			arrProv=Provincia.values();
		else{
			arrProv=new Provincia[1];
			arrProv[0]=provincia;
		}
		
		switch (modo) {
		case PELI:  ArrayList<Pelicula> pelis=new ArrayList<Pelicula>();
					
					//Actualiza desde esa provincia
					for (ProvinciasGDO.Provincia provIterada : arrProv) {
						/*TODO arreglar las condiciones, porque la parte derecha no puede ser provIterada, 
						/* sino a partir de la que quiero iterar
						 */ 
						if (provIterada.ordinal()>=provIterada.ordinal() && (!interrumpir)){
							interfaz.informa(provincia.ordinal(), 0);
							//TODO Falta la actualización propiamente dicha
							/*pelis.addAll(procCarte.getPeliculas(provincia));
							params=new ParamsConexionBD("userSSII","passSSII","jdbc:mysql://localhost:3306/ssii");
							bd.actualizaPeliculas(params, provIterada, false);*/
							params=new ParamsConexionBD("userSSII","passSSII","jdbc:mysql://localhost:3306/ssii");
							bd.actualizaPeliculas(params, provIterada, false);
						}
					}
		break;
		case CINE:	ArrayList<Cine> cines=new ArrayList<Cine>();
					
					//Actualiza desde esa provincia
					for (ProvinciasGDO.Provincia provIterada : arrProv) {
						/*TODO arreglar las condiciones, porque la parte derecha no puede ser provIterada, 
						/* sino a partir de la que quiero iterar
						 */
						if (provIterada.ordinal()>=provIterada.ordinal() && (!interrumpir)){
							interfaz.informa(provincia.ordinal(), 1);
							//TODO Falta la actualización propiamente dicha
							//cines.addAll(procCines.getCines(provincia));
							params=new ParamsConexionBD("userSSII","passSSII","jdbc:mysql://localhost:3306/ssii");
							bd.actualizaCines(params, provIterada, false);
						}
					}
			break;
		case SESION:ArrayList<Cine> cinesSesion=new ArrayList<Cine>();
					
					//Actualiza desde esa provincia
					for (ProvinciasGDO.Provincia provIterada : arrProv) {
						/*TODO arreglar las condiciones, porque la parte derecha no puede ser provIterada, 
						/* sino a partir de la que quiero iterar
						 */
						if (provIterada.ordinal()>=provIterada.ordinal() && (!interrumpir)){
							interfaz.informa(provincia.ordinal(), 2);
							//TODO ¡¡Falta Todo!!!
						}
					}
			break;
		case PROVINCIA:	pelis=new ArrayList<Pelicula>();
						cines=new ArrayList<Cine>();
						
						for (ProvinciasGDO.Provincia provIterada : arrProv) {
							//Actualiza desde esa provincia
							/*TODO arreglar las condiciones, porque la parte derecha no puede ser provIterada, 
							/* sino a partir de la que quiero iterar
							 */
							if (provIterada.ordinal()>=provIterada.ordinal() && (!interrumpir)){
								interfaz.informa(provincia.ordinal(),0);
								//TODO Falta la actualización propiamente dicha
								//pelis.addAll(procCarte.getPeliculas(provincia));
								params=new ParamsConexionBD("userSSII","passSSII","jdbc:mysql://localhost:3306/ssii");
								bd.actualizaProvincia(params, provIterada, false);
							}
						}
		break;
		default:break;
		}
		this.interfaz.finalizaProceso(true);
	}
	
	
	public void interrumpe(){
		interrumpir=true;
		procCarte.paralo();
		procCines.paralo();
		this.interfaz.finalizaProceso(false);
	}

}
