package Prueba;

import java.util.ArrayList;

import Prueba.ProvinciasGDO.Provincia;

/**
 * Interfaz para una clase que descargue la cartelera de una página web.
 *
 */
public interface ProcesadorCartelera {

	public ArrayList<Pelicula> getPeliculas(Provincia provincia);
}
