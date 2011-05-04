package jcolibri.metodosclasificacion;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jcolibri.extensions.classification.ClassificationSolution;
import jcolibri.method.retrieve.RetrievalResult;

/**
 * Asigna la clase mayoritaria entre los k documentos más próximos.
 * Si existiera empate se asigna la clase con mayor similitud.
 */
public class MajorityVotingMethod implements ClassificationMethod
{

    /**
     * Devuelve la clase mayoritaria entre los k documentos más próximos.
     * Si existe empate se asigna la clase con mayor similitud.
     */
	public Prediction getPredictedClass(Collection<RetrievalResult> cases) {
        Map<Object, Integer> votes = new HashMap<Object, Integer>();
        
        for (RetrievalResult result: cases)
        {
            ClassificationSolution solution = (ClassificationSolution)result.get_case().getSolution();
            
            Object classif = solution.getClassification();
            
            if (votes.containsKey(classif))
            {
                votes.put(classif, votes.get(classif) + 1);
            }
            else
            {
                votes.put(classif, 1);
            }
        }
        
        int highestVoteSoFar = 0;
        Object predictedClass = null;
        for (Map.Entry<Object, Integer> e : votes.entrySet())
        {
            if (e.getValue() >= highestVoteSoFar)
            {
                highestVoteSoFar = e.getValue();
                predictedClass = e.getKey();
            }
        }
        
        if(highestVoteSoFar ==1)
        	predictedClass = ((ClassificationSolution)cases.iterator().next().get_case().getSolution()).getClassification();
        
        return new Prediction(predictedClass, (double)highestVoteSoFar/(double)cases.size());
	}
}
