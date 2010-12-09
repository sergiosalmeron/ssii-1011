package tads;

import tads.DiasSemana.EnumDias;

public class Proyeccion {
	
	private String dirWebPelicula;
	private EnumDias dia;
	private String hora;
	
	public String getDirWebPelicula() {
		return dirWebPelicula;
	}
	
	public EnumDias getDia() {
		return dia;
	}
	
	public String getHora() {
		return hora;
	}
	
	public Proyeccion(String dirWebPelicula, EnumDias dia, String hora) {
		super();
		this.dirWebPelicula = dirWebPelicula;
		this.dia = dia;
		this.hora = hora;
	}
	/*
	public Proyeccion(String dirWebPelicula, String diaYhora) {
		String[] datos=diaYhora.split(" ");
		if (datos.length==2){
			this.dirWebPelicula = dirWebPelicula;
			this.dia = datos[0];
			this.hora = datos[1];
		}
	}*/
	
	@Override
	public String toString() {
		return dia+" "+hora+": "+dirWebPelicula;
	}



}
