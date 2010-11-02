package Prueba;

public class Pelicula {
	
	private String titulo;
	private int anio;
	private String dirWeb;
	private String director;
	//TODO: meter todos los campos que nos interesen
	
	public Pelicula (String tit){
		this.titulo=tit;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public String getDirWeb() {
		return dirWeb;
	}

	public void setDirWeb(String dirWeb) {
		this.dirWeb = dirWeb;
	}

	public String getTitulo() {
		return titulo;
	}

	public int getAnio() {
		return anio;
	}
	
	public String getDirector() {
		return director;
	}
	
	@Override
	public String toString() {
		return "Pelicula [titulo=" + titulo + "]";
	}

}
