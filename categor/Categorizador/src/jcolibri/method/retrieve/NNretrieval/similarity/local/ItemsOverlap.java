package jcolibri.method.retrieve.NNretrieval.similarity.local;

import java.util.Collection;

import jcolibri.exception.NoApplicableSimilarityFunctionException;
import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

/**
 * Overlap Coefficient Similarity for Collections
 * <p>
 * This function computes: |intersection(o1,o2)| / min(|o1|,|o2|).

 * @author Juan Antonio Recio García
 * @version 2.0
 *
 */
public class ItemsOverlap implements LocalSimilarityFunction {

	/* (non-Javadoc)
	 * @see jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction#compute(java.lang.Object, java.lang.Object)
	 */
	public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException {
		if ((caseObject == null) || (queryObject == null))
			return 0;
		if (!(caseObject instanceof Collection))
			throw new jcolibri.exception.NoApplicableSimilarityFunctionException(this.getClass(), caseObject.getClass());
		if (!(queryObject instanceof Collection))
			throw new jcolibri.exception.NoApplicableSimilarityFunctionException(this.getClass(), queryObject.getClass());
		
		Collection<?> caseSet  = (Collection<?>) caseObject;
		Collection<?> querySet = (Collection<?>) queryObject;
		
		
		double minSize = Math.min(caseSet.size(), querySet.size());
		if(minSize == 0)
			return 0;
		
		caseSet.retainAll(querySet);
		double intersectionSize = caseSet.size();
		
		return intersectionSize / minSize;
		
	}

	/* (non-Javadoc)
	 * @see jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction#isApplicable(java.lang.Object, java.lang.Object)
	 */
	public boolean isApplicable(Object caseObject, Object queryObject) {
		if((caseObject==null)&&(queryObject==null))
			return true;
		else if(caseObject==null)
			return queryObject instanceof Collection;
		else if(queryObject==null)
			return caseObject instanceof Collection;
		else
			return (caseObject instanceof Collection)&&(queryObject instanceof Collection);
	}

}
