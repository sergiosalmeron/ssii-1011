package sinopsis;

public class GeneroYEntero {
	
	private String genero;
	private double peso;
	
	
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	
	public GeneroYEntero(String genero){
		this.genero=genero;
		this.peso=1;
	}
	
	public GeneroYEntero(String genero, double peso){
		this.genero=genero;
		this.peso=peso;
	}
	
	public String toString(){
		return ("cat "+ this.genero+ " peso " + this.peso);
	}
	
	

}
