package extractores.peliculas;

import java.util.ArrayList;

import tads.Pelicula;
import tads.ProvinciasGDO.Provincia;


/**
 * Interfaz para una clase que descargue la cartelera de una página web.
 *
 */
public interface ProcesadorCartelera {

	public ArrayList<Pelicula> getPeliculas(Provincia provincia);
}
