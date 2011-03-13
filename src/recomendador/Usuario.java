package recomendador;

public class Usuario {
	
	private String nombre;
	private double artesano;
	private double idealista;
	private double guardian;
	private double racional;
	
	
	public Usuario(String nombre, double artesano, double idealista, double guardian, double racional) {
		this.nombre = nombre;
		this.artesano = artesano;
		this.idealista = idealista;
		this.guardian = guardian;
		this.racional = racional;
	}


	public double getPorcentajeArtesano() {
		return artesano;
	}


	public double getPorcentajeIdealista() {
		return idealista;
	}


	public double getPorcentajeGuardian() {
		return guardian;
	}


	public double getPorcentajeRacional() {
		return racional;
	}
	
	

}
