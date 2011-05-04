package jcolibri.metodosclasificacion;

import java.util.Collection;

import jcolibri.method.retrieve.RetrievalResult;

/**
 * Intefaz a implementar por cada método de clasificación.
 * Un método de clasificación recibe una lista de documentos ordenados según su similitud a la consulta y realiza una predicción de su categoria de acuerdo a ellos.
 * 
 * @author GAIA - Juan A. Recio García
 *
 */
public interface ClassificationMethod {
	
	/**
	 * Realiza una predicción a partir de una lista de de documentos ordenados según su similitud a la consulta.
	 * @param cases lista de casos ordenados según su similitud a la consulta
	 * @return la predicción.
	 */
	public Prediction getPredictedClass(Collection<RetrievalResult> cases);
	
}
