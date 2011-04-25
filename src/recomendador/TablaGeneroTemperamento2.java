package recomendador;

import java.util.Hashtable;

import testTemp.Temperamento;

public class TablaGeneroTemperamento2 {
	
private Hashtable<String, PorcentajeTemperamento[]> tabla;
	
	public TablaGeneroTemperamento2(){
		tabla=new Hashtable<String, PorcentajeTemperamento[]>();
		meteGenero(Generos.Accion,0.1101,0.0913,0.0741,0.0405);
		meteGenero(Generos.Animacion,0.1055,0.0822,0.1019,0.0946);
		meteGenero(Generos.Aventuras,0.1376,0.0776,0.0926,0.0856);
		meteGenero(Generos.Comedia,0.0872,0.0959,0.1111,0.0901);
		meteGenero(Generos.Documental,0.0459,0.1096,0.1065,0.1261);
		meteGenero(Generos.Drama,0.0505,0.1279,0.1111,0.0991);
		meteGenero(Generos.Fantasía,0.1055,0.0959,0.0972,0.0901);
		meteGenero(Generos.Romántica,0.0688,0.0913,0.1389,0.0811);
		meteGenero(Generos.Terror,0.0963,0.0685,0.037,0.0631);
		meteGenero(Generos.Thriller,0.0917,0.1005,0.0648,0.1036);
		meteGenero(Generos.CienciaFiccion,0.1009,0.0594,0.0648,0.1261);
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



