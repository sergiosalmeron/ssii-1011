package recomendador;

public class Principal {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Recomendador recomendador=new Recomendador();
		PeliGeneros p=new PeliGeneros("Loca academia de policía");
		p.addGenero(Generos.Comedia, 0.8);
		p.addGenero (Generos.Accion, 0.1);
		p.addGenero(Generos.Documental, 0.1);
		
		//Usuario u=new Usuario("Aleix", 0.3125, 0.375, 0.0625, 0.25);
		Usuario u=new Usuario("Aleix", 0.0, 0.0, 0.1, 0.9);
		System.out.println(u);
		System.out.println(p);
		double punt=recomendador.getPuntuacion(u, p);
		
		System.out.println("Sin estado de ánimo: "+punt);
		punt=recomendador.getPuntuacionAnimo(u, p,EstadosAnimo.Alegre);		
		System.out.println("Alegre: "+punt);
		punt=recomendador.getPuntuacionAnimo(u, p,EstadosAnimo.Triste);		
		System.out.println("Triste: "+punt);
		punt=recomendador.getPuntuacionAnimo(u, p,EstadosAnimo.Sorprendido);		
		System.out.println("Sorprendido: "+punt);
		punt=recomendador.getPuntuacionAnimo(u, p,EstadosAnimo.Relajado);		
		System.out.println("Relajado: "+punt);
		punt=recomendador.getPuntuacionAnimo(u, p,EstadosAnimo.Asustado);		
		System.out.println("Asustado: "+punt);

	}

}
