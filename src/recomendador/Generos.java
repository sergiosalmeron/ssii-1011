package recomendador;


public enum Generos {
	
	Accion, Animacion, Aventuras, Comedia, Documental, Drama, Fantasia, Romantica, Terror, Thriller, CienciaFiccion;
	

public static Generos getGenero(int i){
		switch (i){
			case 0:  return Accion;
			case 1:  return Animacion;
			case 2:  return Aventuras;
			case 3:  return Comedia;	
			case 4:  return Documental;
			case 5:  return Drama;
			case 6:  return Fantasia;
			case 7:  return Romantica;
			case 8:  return Terror;
			case 9:  return Thriller;
			case 10:  return CienciaFiccion;
			default: return null;
		}
	}


}