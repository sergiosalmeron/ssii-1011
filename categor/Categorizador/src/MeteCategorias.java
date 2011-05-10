import java.util.ArrayList;

import jcolibri.cbrcore.CBRQuery;
import jcolibri.datatypes.Text;
import jcolibri.evaluation.Evaluator;
import jcolibri.exception.ExecutionException;
import recomendador.PeliGeneros;
import sinopsis.ClasificaSinopsis;
import sinopsis.DescripcionSinopsis;
import recomendador.Generos;
import utils.BD;

public class MeteCategorias {

	public MeteCategorias() {
		ClasificaSinopsis c;
		// String
		// sinopsis="Una mujer de 35 años descubre que tiene una nave espacial del futuro entre las piernas. Mata a su hijo y viaja al pasado con dos pistolas a vengarse de un extraterrestre mortal.";

		c = new ClasificaSinopsis();

		try {
			c.configure();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Precycle
		try {
			c.preCycle();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ArrayList<String> listaPelis = BD.damePeliculas();
		String id, sinopsis;
		ArrayList<PeliGeneros> pG = new ArrayList<PeliGeneros>();
		int puntoCorte;
		String s2;
		for (String peli : listaPelis) {
			puntoCorte = peli.indexOf(">");
			id = peli.substring(0, puntoCorte);
			sinopsis = peli.substring(puntoCorte + 1);
			sinopsis = sinopsis.replaceAll("'", "");
			sinopsis = sinopsis.replaceAll("!", "");
			sinopsis = sinopsis.replaceAll("¡", "");

			CBRQuery query = new CBRQuery();
			DescripcionSinopsis a = new DescripcionSinopsis();
			a.setTexto(new Text(sinopsis));
			query.setDescription(a);
			try {
				c.generoDefecto(id);
				c.cycle(query);
			} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.err.println("Error en la peli " + id);
			}
			s2 = c.getCategoriaAsignada();
			s2 = s2.replace("No se ha encontrado ninguna",
					"Acción 0% Fantasia 0");
			
			//esto es para solventar el problema de un único género al 99% cuando no hay sinopsis
			if (sinopsis.length()<20)
				s2="Accion 0%";
			
			
			trataAsignadas(id, s2, pG);
			// System.out.println("Asignadas: "+s2);
		}

		BD.introduceGenerosPelicula(pG);
		System.out.println(Evaluator.getEvaluationReport());
	}

	private void trataAsignadas(String id, String asignadas,
			ArrayList<PeliGeneros> pg) {
		PeliGeneros p = new PeliGeneros(id);
		int corteGen;
		String genero;
		double porcentaje;
		String[] arrAsignadas = asignadas.split("%");
		for (int i = 0; i < arrAsignadas.length; i++) {
			corteGen = arrAsignadas[i].indexOf(" ");
			// System.out.println(arrAsignadas[i]);
			genero = arrAsignadas[i].substring(0, corteGen);
			porcentaje = Double
					.valueOf(arrAsignadas[i].substring(corteGen + 1));

			if (genero.equalsIgnoreCase("Accion"))
				p.addGenero(Generos.Accion, porcentaje);
			else if (genero.equalsIgnoreCase("Aventuras"))
				p.addGenero(Generos.Aventuras, porcentaje);
			else if (genero.equalsIgnoreCase("CienciaFiccion"))
				p.addGenero(Generos.CienciaFiccion, porcentaje);
			else if (genero.equalsIgnoreCase("Comedia"))
				p.addGenero(Generos.Comedia, porcentaje);
			else if (genero.equalsIgnoreCase("Drama"))
				p.addGenero(Generos.Drama, porcentaje);
			else if (genero.equalsIgnoreCase("Fantasia"))
				p.addGenero(Generos.Fantasia, porcentaje);
			else if (genero.equalsIgnoreCase("Romantica"))
				p.addGenero(Generos.Romantica, porcentaje);
			else if (genero.equalsIgnoreCase("Terror"))
				p.addGenero(Generos.Terror, porcentaje);
			else if (genero.equalsIgnoreCase("Thriller"))
				p.addGenero(Generos.Thriller, porcentaje);
			else if (genero.equalsIgnoreCase("Animacion"))
				p.addGenero(Generos.Animacion, porcentaje);
			else if (genero.equalsIgnoreCase("Documental"))
				p.addGenero(Generos.Documental, porcentaje);

		}
		pg.add(p);

	}

}
