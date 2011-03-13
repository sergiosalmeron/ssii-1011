package recomendador;

import testTemp.Temperamento;

public class PorcentajeTemperamento {
	
	private Temperamento temp;
	private double porcentaje;
	
	
	public PorcentajeTemperamento(Temperamento temp, double porcentaje) {
		this.temp = temp;
		this.porcentaje = porcentaje;
	}
	
	
	public Temperamento getTemp() {
		return temp;
	}
	public double getPorcentaje() {
		return porcentaje;
	}

	
}
