package Prueba;

import java.util.ArrayList;

public class Cine {
	
	//plantear la opción de crear la clase "pases" (que contenga peli, cine, día de la semaa y hora) si se piensa guardar tambien la hora de cada pase
	//en ese caso, el arraylist seria de pases y no de pelis
	private ArrayList<Pelicula> pelis;
	private String nombre;
	// localidad como enumerado????
	private String localidad;
	
	
	public Cine(String nombre, String localidad) {
		this.nombre = nombre;
		this.localidad = localidad;
		pelis= new ArrayList<Pelicula>();
	}
	
	public Pelicula getPeli(int numPeli) {
		if (numPeli<pelis.size() && numPeli>0)
			return pelis.get(numPeli);
		else
			return null;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getLocalidad() {
		return localidad;
	}
	
	public void addPeli(Pelicula peli){
		pelis.add(peli);
	}
	

}
