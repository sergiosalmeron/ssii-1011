package recomendador;

public class PorcentajeGenero {
	
	private Generos genero;
	private double porcentaje;
	
	
	public PorcentajeGenero(Generos genero, double porcentaje) {
		super();
		this.genero = genero;
		this.porcentaje = porcentaje;
	}
	public Generos getGenero() {
		return genero;
	}
	public double getPorcentaje() {
		return porcentaje;
	}
	
	
	

}
