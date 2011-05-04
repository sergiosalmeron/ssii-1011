package recomendador;

public class PeliPuntuacion implements Comparable<PeliPuntuacion>{
	private String titulo;
	private double puntuacion;
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public double getPuntuacion() {
		return puntuacion;
	}
	public void setPuntuacion(double puntuacion) {
		this.puntuacion = puntuacion;
	}
	public PeliPuntuacion(String titulo, double puntuacion) {
		super();
		this.titulo = titulo;
		this.puntuacion = puntuacion;
	}
	
	
	public String toString(){
		return "Titulo: "+titulo+"; Puntuación: "+puntuacion+"\n";
	}
	@Override
	public int compareTo(PeliPuntuacion o) {
        return (puntuacion >o.getPuntuacion() ? -1 : 1);
		
                //firstName.compareTo(n.firstName));

	}
}
