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
	
	
	
	public String getTitulo() {
		return titulo;
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
	
	public double getCantidadGenero(Generos g){
		double cantidad=0;
		int i=0;
		boolean encontrado=false;
		while (!encontrado && i<generos.size()){
			if (g==generos.get(i).getGenero()){
				cantidad=generos.get(i).getPorcentaje();
				encontrado=true;
			}
			i++;
		}
		return cantidad;
	}
	
	public String toString(){
		String aux=titulo+"-> ";
		for (PorcentajeGenero p: generos){
			aux=aux+p;
		}
		return aux;
	}
	
	

}
