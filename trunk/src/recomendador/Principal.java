package recomendador;

public class Principal {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Recomendador recomendador=new Recomendador();
		PeliGeneros p=new PeliGeneros("Todo sobre mi madre");
		p.addGenero(Generos.Comedia, 0.05);
		p.addGenero (Generos.Documental, 0.9);
		p.addGenero(Generos.CienciaFiccion, 0.05);
		
		//Usuario u=new Usuario("Aleix", 0.3125, 0.375, 0.0625, 0.25);
		Usuario u=new Usuario("Aleix", 0.05, 0.05, 0.10, 0.80);
		
		double punt=recomendador.getPuntuación(u, p)*100;
		
		System.out.println(punt);

	}

}
