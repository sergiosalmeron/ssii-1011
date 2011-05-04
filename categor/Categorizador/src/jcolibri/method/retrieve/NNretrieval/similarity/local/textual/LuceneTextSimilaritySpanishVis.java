/**
 * LuceneTextSimilaritySpanish.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 25/06/2007
 */
package jcolibri.method.retrieve.NNretrieval.similarity.local.textual;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.datatypes.Text;
import jcolibri.exception.NoApplicableSimilarityFunctionException;
import jcolibri.extensions.textual.lucene.LuceneIndexSpanish;
import jcolibri.extensions.textual.lucene.LuceneSearchResult;
import jcolibri.extensions.textual.lucene.LuceneSearcher;
import jcolibri.method.retrieve.NNretrieval.similarity.InContextLocalSimilarityFunction;

/**
 * Computes the similarity between two texts using Lucene.
 * <br>
 * This index manages Spanish texts.
 * <br>
 * It is applicable to any Text object.
 * <br>
 * Requires the previous execution of the method jcolibri.method.precycle.LuceneIndexCreatorSpanish to create
 * a LuceneIndexSpanish.
 * <br>
 * Test 13 shows how to use this similarity measure (in English).
 * 
 * @author Juan A. Recio-Garcia
 * @version 1.0
 * @see jcolibri.datatypes.Text
 * @see jcolibri.method.precycle.LuceneIndexCreatorSpanish
 * @see jcolibri.test.test13.Test13b
 */
public class LuceneTextSimilaritySpanishVis extends InContextLocalSimilarityFunction
{
    LuceneSearchResult lsr = null;
    boolean normalized = false;
    LuceneIndexSpanish index;
    String attribute;
    
    /**
     * Creates a LuceneTextSimilaritySpanish object. This constructor pre-computes the similarity of the query with
     * the textaul attributes of the case (as these attributes are in the index). 
     * @param index Index that contains the attributes of the case
     * @param query query that will be compared
     * @param at textual attribute of the case or query object that is being compared
     * @param normalized if the Lucene result must be normalized to [0..1]
     */
    public LuceneTextSimilaritySpanishVis(LuceneIndexSpanish index, CBRQuery query, Attribute at, boolean normalized)
    {
    	this.normalized = normalized;
		this.index = index;
		this.attribute = at.getName();
    }
    

    /* (non-Javadoc)
     * @see jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction#compute(java.lang.Object, java.lang.Object)
     */
    public double compute(Object caseObject, Object queryObject) throws NoApplicableSimilarityFunctionException
    {
	if ((caseObject == null) || (queryObject == null))
		return 0;
	if (!(caseObject instanceof Text))
		throw new jcolibri.exception.NoApplicableSimilarityFunctionException(this.getClass(), caseObject.getClass());
	if (!(queryObject instanceof Text))
		throw new jcolibri.exception.NoApplicableSimilarityFunctionException(this.getClass(), queryObject.getClass());
	
	Text qs = (Text)queryObject;
	lsr = LuceneSearcher.searchSpanish(index, qs.toString(), attribute);
	return lsr.getDocScore(_case.getID().toString(), normalized);
    }

    /* (non-Javadoc)
     * @see jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction#isApplicable(java.lang.Object, java.lang.Object)
     */
    public boolean isApplicable(Object o1, Object o2)
    {
	if((o1==null)&&(o2==null))
		return true;
	else if(o1==null)
		return o2 instanceof Text;
	else if(o2==null)
		return o1 instanceof Text;
	else
		return (o1 instanceof Text)&&(o2 instanceof Text);
    }

}
