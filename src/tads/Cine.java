package tads;

import java.util.ArrayList;

import tads.ProvinciasGDO.Provincia;


public class Cine {
	
	//La direcci�n web del cine en la p�gina de GDO
	private String dirWebGDO;
	private String nombre;
	private Provincia provincia;
	private int numSalas;
	private String tfno;
	private String webOficial;
	private String ciudad;

	private String direccion;
	private String zona;
	private String parking;
	private String urlCompras;
	//Arraylist con los pases del cine
	private ArrayList<Proyeccion> pases;
	
	
	
	
	
	public String getCiudad() {
		return ciudad;
	}


	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}


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
		this.pases=new ArrayList<Proyeccion>();
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
	
	public ArrayList<Proyeccion> getPases() {
		return pases;
	}


	public boolean addPase(Proyeccion pro){
		if (pro!=null){
			pases.add(pro);
			return true;
		}
		else
			return false;
	}
	
	public Proyeccion getPase(int i){
		if (i<pases.size())
			return pases.get(i);
		else
			return null;
	}
	
	public int getNumPases(){
		return pases.size();
	}
	
	@Override
	public String toString() {
		String resultado= nombre + ":\n";// + pases + "]";
		for (Proyeccion pase : pases){
			resultado=resultado+"  -"+pase+"\n";
		}
		return resultado;
	}
	
	

}
