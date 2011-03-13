package recomendador;
import java.util.ArrayList;


public class PeliGeneros {
	
	private ArrayList<PorcentajeGenero> generos;
	private String titulo;
	
	public PeliGeneros(String titulo){
		this.titulo=titulo;
		generos=new ArrayList<PorcentajeGenero>();
	}
	
	public void addGenero(Generos genero,Double porcentaje){
		PorcentajeGenero p=new PorcentajeGenero(genero,porcentaje);
		generos.add(p);
	}
	
	public int getCantidadGeneros(){
		return generos.size();
	}
	
	public double getPorcentaje(int i){
		return generos.get(i).getPorcentaje();
	}
	
	public Generos getGenero(int i){
		return generos.get(i).getGenero();
	}
	
	

}
