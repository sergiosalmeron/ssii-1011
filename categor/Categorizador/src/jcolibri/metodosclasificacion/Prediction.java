package jcolibri.metodosclasificacion;

import java.util.Collection;

import jcolibri.method.retrieve.RetrievalResult;

/**
 * Bean encargado de representar las predicciones. 
 * Una predicci�n se compone de un objeto que almacena la clase y un n�mero entre [0,1] con la confianza en dicha predicci�n.
 * @author GAIA - Juan A. Recio Garc�a
 */
public class Prediction {
	
	/**
	 * Clase de la predicci�n
	 */
	Object Classification;
	/**
	 * Confianza de la predicci�n
	 */
	double confidence;
	
	Collection<RetrievalResult> nns;
	
	
	
	public Collection<RetrievalResult> getNns() {
		return nns;
	}
	public void setNns(Collection<RetrievalResult> nns) {
		this.nns = nns;
	}
	public Object getClassification() {
		return Classification;
	}
	public void setClassification(Object _class) {
		this.Classification = _class;
	}
	public double getConfidence() {
		return confidence;
	}
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	/**
	 * Constructora.
	 */
	public Prediction(Object _class, double confidence) {
		super();
		this.Classification = _class;
		this.confidence = confidence;
	}
	
	
	
}
