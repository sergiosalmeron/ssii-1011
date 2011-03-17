package gui;

import extractores.cines.ProcesadorCinesGDO;
import extractores.peliculas.ProcesadorCarteleraGDO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
	private static final String config="enlaces/config.txt";


	
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


	public void guardaParamsFuncionamiento(ModoFuncionamiento funcionamiento,
			Provincia prov) {
		
		System.out.println("Guardando configuración de la ejecución parada...");
		File fic = new File(config);
		boolean ok=true;
		if (fic.exists()){
			fic.delete();
		}try {
			fic.createNewFile();
			if (fic.exists()){
				BufferedWriter bw = new BufferedWriter(new FileWriter(fic));
				if (prov!=null)
					bw.write(prov.toString()+"\n");
				else
					bw.write("todas\n");
				bw.write(funcionamiento.toString()+"\n");
				bw.close();
			}
		} catch (IOException e) {
			ok=false;
			System.err.println("Error exportanto la configuración de la ejecucion parada.");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ok)
			System.out.println("Finalizado con éxito el proceso de guardado de la configuración de la ejecución parada");
		else
			System.out.println("No se pudo guardar la configuración de la ejecución parada");
	}
		

	/**
	 * Pone en la interfaz el modo de funcionamiento en que estaba la aplicación cuando se paró una ejecución,
	 * e informa si se estaba ejecutando sobre una provincia o sobre todas.
	 * @return Cierto si estaba ejecutandose sobre todas las provincias, falso en caso contrario.
	 */
	public boolean cargaParamsFuncionamiento(){
		File fic = new File(config);
		boolean todas=false;
		if (fic.exists()){
			try {
				BufferedReader bf = new BufferedReader(new FileReader(fic));
				String modo;
				String provincia;
				provincia=bf.readLine();
				if (provincia.equalsIgnoreCase("todas"))
					todas=true;
				modo=bf.readLine();
				
				if (modo.equalsIgnoreCase("PELI"))
					interfaz.setPeli(true);
				else if (modo.equalsIgnoreCase("CINE"))
					interfaz.setCine(true);
				else if (modo.equalsIgnoreCase("SESION"))
					interfaz.setSesion(true);
				else if (modo.equalsIgnoreCase("PROVINCIA"))
					interfaz.setProvincia(true);
				
				bf.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return todas;
	}

}
