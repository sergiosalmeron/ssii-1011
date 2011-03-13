package recomendador;

import testTemp.Temperamento;

public class Recomendador {
	
	private TablaGeneroTemperamento tabla;
	
	public Recomendador(){
		tabla=new TablaGeneroTemperamento();
	}
	
	public double getPuntuación(Usuario u, PeliGeneros p){
		double artesano=u.getPorcentajeArtesano();
		double idealista=u.getPorcentajeIdealista();
		double guardian=u.getPorcentajeGuardian();
		double racional=u.getPorcentajeRacional();
		
		int total=p.getCantidadGeneros();
		double porcentaje=0;
		double aux=0;
		double acum=0;
		Generos genero=Generos.Accion;
		
		for (int i=0; i<total; i++){
			aux=0;
			porcentaje=p.getPorcentaje(i);
			genero=p.getGenero(i);
			aux=artesano*tabla.getValorTabla(genero, Temperamento.Artesano);
			aux=aux+idealista*tabla.getValorTabla(genero, Temperamento.Idealista);
			aux=aux+guardian*tabla.getValorTabla(genero, Temperamento.Guardian);
			aux=aux+racional*tabla.getValorTabla(genero, Temperamento.Racional);
			acum=acum+porcentaje*aux;
		}
		return acum;
		
	}

}
