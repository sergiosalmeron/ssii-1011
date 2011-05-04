package recomendador;
import java.util.Hashtable;

import testTemp.Temperamento;


public class TablaGeneroTemperamento {
	
	private Hashtable<String, PorcentajeTemperamento[]> tabla;
	
	public TablaGeneroTemperamento(){
		tabla=new Hashtable<String, PorcentajeTemperamento[]>();
		meteGenero(Generos.Accion,0.45,0.2167,0.2167,0.1167);
		meteGenero(Generos.Animacion,0.3167,0.1333,0.3,0.25);
		meteGenero(Generos.Aventuras,0.4,0.2,0.2667,0.1333);
		meteGenero(Generos.Comedia,0.25,0.25,0.3167,0.1833);
		meteGenero(Generos.Documental,0.1167,0.3667,0.2,0.3167);
		meteGenero(Generos.Drama,0.1,0.3167,0.35,0.2333);
		meteGenero(Generos.Fantasia,0.3,0.2267,0.3,0.1733);
		meteGenero(Generos.Romantica,0.1333,0.2833,0.4,0.1833);
		meteGenero(Generos.Terror,0.3167,0.24,0.2433,0.2);
		meteGenero(Generos.Thriller,0.1333,0.3833,0.2833,0.2);
		meteGenero(Generos.CienciaFiccion,0.1,0.2167,0.2833,0.4);
	}
	
	private void meteGenero(Generos genero,double artesano, double idealista, double guardian, double racional){
		PorcentajeTemperamento[] v=new PorcentajeTemperamento[4];	
		v[0]=new PorcentajeTemperamento(Temperamento.Artesano,artesano);
		v[1]=new PorcentajeTemperamento(Temperamento.Idealista,idealista);
		v[2]=new PorcentajeTemperamento(Temperamento.Guardian,guardian);
		v[3]=new PorcentajeTemperamento(Temperamento.Racional,racional);
		tabla.put(genero.toString(), v);
	}
	
	public double getValorTabla(Generos genero, Temperamento temperamento){
		double valor=getPorcentajeTemperamentoTabla((PorcentajeTemperamento[]) tabla.get(genero.toString()), temperamento);
		return valor;
	}

	private double getPorcentajeTemperamentoTabla(PorcentajeTemperamento[] temps, Temperamento temperamento) {
		double valor=0;
		switch (temperamento){
		case Artesano: valor=temps[0].getPorcentaje();break;
		case Idealista: valor=temps[1].getPorcentaje();break;
		case Guardian: valor=temps[2].getPorcentaje();break;
		case Racional:valor=temps[3].getPorcentaje();break;
		}
		return valor;
	}



}
