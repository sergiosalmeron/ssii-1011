package tads;

public class Pelicula {
	
	private String titulo;
	private int anio;
	private int duracion;
	private String dirWeb;
	private String director;
	private String interpretes;
	private String nacionalidad;
	private String estreno;
	private String genero;
	private String productora;
	private String guionista;
	private String distribuidora;
	//Calificación contiene la edad mínima recomendada para ver la pelicula
	//Si es 0: todos los públicos
	//Si es -1: película pendiente de calificar
	private int calificacion;
	
	public int getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(int calificacion) {
		this.calificacion = calificacion;
	}

	public String getProductora() {
		return productora;
	}

	public void setProductora(String productora) {
		this.productora = productora;
	}

	public String getGuionista() {
		return guionista;
	}

	public void setGuionista(String guionista) {
		this.guionista = guionista;
	}

	public String getDistribuidora() {
		return distribuidora;
	}

	public void setDistribuidora(String distribuidora) {
		this.distribuidora = distribuidora;
	}

	public String getInterpretes() {
		return interpretes;
	}

	public void setInterpretes(String interpretes) {
		this.interpretes = interpretes;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getEstreno() {
		return estreno;
	}

	public void setEstreno(String estreno) {
		this.estreno = estreno;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public Pelicula (String tit){
		this.titulo=tit;
	}

	public void setDirector(String director) {
		this.director = director;
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
	
	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	@Override
	public String toString() {
		return "Pelicula [titulo=" + titulo + ", estreno="
				+ estreno + ", genero=" + genero + ", calificacion=" + calificacion + "]";
	}
	


}
