package jcolibri.metodosclasificacion;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jcolibri.extensions.classification.ClassificationSolution;
import jcolibri.method.retrieve.RetrievalResult;

/**
 * Votación ponderada: Se suma el peso de cada categoría dependiendo de la similitud del documento a la consulta.  
 * Finalmente se asigna la categoría con mayor peso
 */
public class SimilarityWeightedVotingMethod implements ClassificationMethod
{
    /**
     * Votación ponderada: Se suma el peso de cada categoría dependiendo de la similitud del documento a la consulta.  
     * Finalmente se asigna la categoría con mayor peso
     */
	public Prediction getPredictedClass(Collection<RetrievalResult> cases) 
    {
        Map<Object, Double> votes = new HashMap<Object, Double>();
        
        for(RetrievalResult result: cases)
        {   
            ClassificationSolution solution = (ClassificationSolution)result.get_case().getSolution();
           
            Object solnAttVal = solution.getClassification();
             
            double eval = result.getEval();
            if (votes.containsKey(solnAttVal))
            	votes.put(solnAttVal, votes.get(solnAttVal) + eval);
            else 
            	votes.put(solnAttVal, eval);
        }
        double highestVoteSoFar = 0.0;
        Object predictedClassVal = null;
        for (Map.Entry<Object, Double> e : votes.entrySet())
        {   
        	if (e.getValue() >= highestVoteSoFar)
            {  	
        		highestVoteSoFar = e.getValue();
                predictedClassVal = e.getKey();
            }
        }
        
        return new Prediction(predictedClassVal, highestVoteSoFar/(double)cases.size());
    }

}
