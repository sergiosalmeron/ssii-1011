package recomendador;

import java.util.Hashtable;

import testTemp.Temperamento;

public class TablaGeneroTemperamento3 {
	
private Hashtable<String, PorcentajeTemperamento[]> tabla;
	
	public TablaGeneroTemperamento3(){
		tabla=new Hashtable<String, PorcentajeTemperamento[]>();
		meteGenero(Generos.Accion,0.1878,0.0743,0.0540,0.0202);
		meteGenero(Generos.Animacion,0.1266,0.0411,0.1028,0.1012);
		meteGenero(Generos.Aventuras,0.2086,0.0583,0.0831,0.0488);
		meteGenero(Generos.Comedia,0.0826,0.09,0.1184,0.0707);
		meteGenero(Generos.Documental,0.0203,0.1509,0.0717,0.1710);
		meteGenero(Generos.Drama,0.0191,0.1520,0.1309,0.099);
		meteGenero(Generos.Fantasía,0.12,0.0816,0.0982,0.0668);
		meteGenero(Generos.Romántica,0.0348,0.0972,0.1870,0.0636);
		meteGenero(Generos.Terror,0.1156,0.0617,0.0303,0.054);
		meteGenero(Generos.Thriller,0.0464,0.1446,0.0618,0.0887);
		meteGenero(Generos.CienciaFiccion,0.0382,0.0483,0.0618,0.2159);
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
