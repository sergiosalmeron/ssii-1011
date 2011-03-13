package recomendador;

public class Principal {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Recomendador recomendador=new Recomendador();
		PeliGeneros p=new PeliGeneros("Todo sobre mi madre");
		p.addGenero(Generos.Thriller, 0.1);
		p.addGenero (Generos.Fantasía, 0.2);
		p.addGenero(Generos.Aventuras, 0.7);
		
		Usuario u=new Usuario("Aleix", 0.3125, 0.375, 0.0625, 0.25);
		
		
		double punt=recomendador.getPuntuación(u, p)*100;
		
		System.out.println(punt);

	}

}
