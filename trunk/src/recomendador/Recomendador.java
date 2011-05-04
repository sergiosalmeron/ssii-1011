package recomendador;

import testTemp.Temperamento;

public class Recomendador {
	
	private TablaGeneroTemperamento3 tabla;
	
	public Recomendador(){
		tabla=new TablaGeneroTemperamento3();
	}
	
	public double getPuntuacion(Usuario u, PeliGeneros p){
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
		return acum*100;
		
	}
	
	public double getPuntuacionAnimo(Usuario u, PeliGeneros p, EstadosAnimo e) {
		double puntuacion = 0;
		double limite = 0.15;
		switch (e) {
		case Alegre:
			if (p.getCantidadGenero(Generos.Comedia) > limite)
				puntuacion =modificaPunt(puntuacion, u, p, p.getCantidadGenero(Generos.Comedia));
			break;
		case Triste:
			if (p.getCantidadGenero(Generos.Drama) > limite)
				puntuacion =modificaPunt(puntuacion, u, p, p.getCantidadGenero(Generos.Drama));
			break;
		case Sorprendido:
			if (p.getCantidadGenero(Generos.Accion)>limite || p.getCantidadGenero(Generos.Aventuras)>limite){
				puntuacion =modificaPunt(puntuacion, u, p, p.getCantidadGenero(Generos.Accion));
				puntuacion =modificaPunt(puntuacion, u, p, p.getCantidadGenero(Generos.Aventuras));
			}
			break;
		case Asustado:
			if (p.getCantidadGenero(Generos.Terror)>limite || p.getCantidadGenero(Generos.Thriller)>limite){
				puntuacion =modificaPunt(puntuacion, u, p, p.getCantidadGenero(Generos.Terror));
				puntuacion =modificaPunt(puntuacion, u, p, p.getCantidadGenero(Generos.Thriller));
			}
			break;
		case Relajado:
			if (p.getCantidadGenero(Generos.Documental)>limite || p.getCantidadGenero(Generos.Romantica)>limite){
				puntuacion =modificaPunt(puntuacion, u, p, p.getCantidadGenero(Generos.Documental));
				puntuacion =modificaPunt(puntuacion, u, p, p.getCantidadGenero(Generos.Romantica));
			}
			break;

		}
		return puntuacion;
	}

	private double modificaPunt(double punt,Usuario u, PeliGeneros p, double cantidadGenero) {
		double factor=1.2;
		if (punt==0)
			punt=getPuntuacion(u,p);
		
		return punt+(cantidadGenero*factor);
		
	}

	
	
}
