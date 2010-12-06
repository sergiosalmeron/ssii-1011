package Prueba;

import java.util.ArrayList;

import Prueba.ProvinciasGDO.Provincia;

public class Cine {
	
	//plantear la opción de crear la clase "pases" (que contenga peli, cine, día de la semaa y hora) si se piensa guardar tambien la hora de cada pase
	//en ese caso, el arraylist seria de pases y no de pelis
	private String dirWebGDO;
	private String nombre;
	private Provincia provincia;
	private int numSalas;
	private String tfno;
	private String webOficial;
	private String direccion;
	private String zona;
	private String parking;
	private String urlCompras;
	
	
	
	public String getUrlCompras() {
		return urlCompras;
	}


	public void setUrlCompras(String urlCompras) {
		this.urlCompras = urlCompras;
	}


	public String getParking() {
		return parking;
	}


	public void setParking(String parking) {
		this.parking = parking;
	}


	public String getZona() {
		return zona;
	}


	public void setZona(String zona) {
		this.zona = zona;
	}


	public Cine(String dirWebGDO, String nombre) {
		super();
		this.dirWebGDO = dirWebGDO;
		this.nombre = nombre;
	}


	public String getDirWebGDO() {
		return dirWebGDO;
	}


	public Provincia getProvincia() {
		return provincia;
	}
	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}
	public int getNumSalas() {
		return numSalas;
	}
	public void setNumSalas(int numSalas) {
		this.numSalas = numSalas;
	}
	public String getTfno() {
		return tfno;
	}
	public void setTfno(String tfno) {
		this.tfno = tfno;
	}
	public String getWebOficial() {
		return webOficial;
	}
	public void setWebOficial(String webOficial) {
		this.webOficial = webOficial;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getNombre() {
		return nombre;
	}
	
	
	
	
	

}
