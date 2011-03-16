package gui;

import extractores.cines.ProcesadorCinesGDO;
import extractores.peliculas.ProcesadorCarteleraGDO;
import tads.ModoFuncionamiento;
import tads.ParamsConexionBD;
import tads.ProvinciasGDO;
import tads.ProvinciasGDO.Provincia;
import utils.BD;

public class Logica implements Runnable {

	private static boolean interrumpir;

	private InterfazExtractor interfaz;
	
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
		case PELI:  					
					//Actualiza desde esa provincia
					for (ProvinciasGDO.Provincia provIterada : arrProv) {
						if (!interrumpir){
							interfaz.informa(provIterada.ordinal(), 0);
							params=new ParamsConexionBD("userSSII","passSSII","jdbc:mysql://localhost:3306/ssii");
							bd.actualizaPeliculas(procCarte, params, provIterada, false);
						}
					}
		break;
		case CINE:						
					//Actualiza desde esa provincia
					for (ProvinciasGDO.Provincia provIterada : arrProv) {
						if (provIterada.ordinal()>=provIterada.ordinal() && (!interrumpir)){
							interfaz.informa(provIterada.ordinal(), 1);
							params=new ParamsConexionBD("userSSII","passSSII","jdbc:mysql://localhost:3306/ssii");
							bd.actualizaCines(procCines, params, provIterada, false);
						}
					}
			break;
		case SESION: 					
					//Actualiza desde esa provincia
					for (ProvinciasGDO.Provincia provIterada : arrProv) {
						if (provIterada.ordinal()>=provIterada.ordinal() && (!interrumpir)){
							interfaz.informa(provIterada.ordinal(), 2);
							//TODO ¡¡Falta Todo!!!
						}
					}
			break;
		case PROVINCIA:							
						for (ProvinciasGDO.Provincia provIterada : arrProv) {
							if (provIterada.ordinal()>=provIterada.ordinal() && (!interrumpir)){
								interfaz.informa(provIterada.ordinal(),0);
								params=new ParamsConexionBD("userSSII","passSSII","jdbc:mysql://localhost:3306/ssii");
								bd.actualizaPeliculas(procCarte, params, provIterada, false);
							}
							if (provIterada.ordinal()>=provIterada.ordinal() && (!interrumpir)){
								interfaz.informa(provIterada.ordinal(),1);
								params=new ParamsConexionBD("userSSII","passSSII","jdbc:mysql://localhost:3306/ssii");
								bd.actualizaCines(procCines, params, provIterada, false);
							}
						}
			break;
		default:break;
		}
		if (interrumpir){
			interrumpir=false;
			//Aquí tengo que guardar la info sobre la ejecución
			this.interfaz.finalizaProceso(false);
		}
		else this.interfaz.finalizaProceso(true);
	}
	
	
	public void interrumpe(){
		interrumpir=true;
		procCarte.paralo();
		procCines.paralo();
	}
	
	public boolean consultaEjecucionPendiente(){
		return ((procCarte.getProvinciaRestantes()!=null)||(procCines.getProvinciaRestantes()!=null));
	}

}
