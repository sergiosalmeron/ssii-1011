/**
 * StopWordsDetectorSpanish.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 20/06/2007
 */
package jcolibri.extensions.textual.IE.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.extensions.textual.IE.IEutils;
import jcolibri.extensions.textual.IE.gate.GatePhrasesExtractor;
import jcolibri.extensions.textual.IE.representation.IEText;
import jcolibri.extensions.textual.IE.representation.Token;
import jcolibri.util.AttributeUtils;
import jcolibri.util.ProgressController;


/**
 * Removes stop words (workds without relevant meaning) and punctuation symbols.
 * It uses a built-in list and modifies the "isStopWord" flag of the tokens.
 * <p>
 * The first version was developed at: Robert Gordon University - Aberdeen & Facultad Informática,
 * Universidad Complutense de Madrid (GAIA)
 * </p>
 * @author Juan A. Recio-Garcia
 * @version 2.0
 */
public class StopWordsDetectorSpanish
{

    /**
     * Performs the algorithm in the given attributes of a collection of cases.
     * These attributes must be IEText objects.
     */
    public static void detectStopWords(Collection<CBRCase> cases, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(StopWordsDetectorSpanish.class).info("Detecting stop words.");
	ProgressController.init(StopWordsDetectorSpanish.class, "Detecting stop words ...", cases.size());
	for(CBRCase c: cases)
	{
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, c);
		detectStopWords((IEText)o);
	    }
	    ProgressController.step(GatePhrasesExtractor.class);
	}
	ProgressController.finish(GatePhrasesExtractor.class);
    }

    /**
     * Performs the algorithm in the given attributes of a query.
     * These attributes must be IEText objects.
     */
    public static void detectStopWords(CBRQuery query, Collection<Attribute> attributes)
    {
	    org.apache.commons.logging.LogFactory.getLog(StopWordsDetectorSpanish.class).info("Detecting stop words.");
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, query);
		detectStopWords((IEText)o);
	    }
    }
    
    /**
     * Performs the algorithm in all the attributes of a collection of cases
     * These attributes must be IEText objects.
     */
    public static void detectStopWords(Collection<CBRCase> cases)
    {
	org.apache.commons.logging.LogFactory.getLog(StopWordsDetectorSpanish.class).info("Detecting stop words.");
	ProgressController.init(StopWordsDetectorSpanish.class, "Detecting stop words ...", cases.size());
	for(CBRCase c: cases)
	{
	    Collection<IEText> texts = IEutils.getTexts(c);
	    for(IEText t : texts)
		detectStopWords(t);
	    ProgressController.step(GatePhrasesExtractor.class);
	}
	ProgressController.finish(GatePhrasesExtractor.class);
    }
    
    /**
     * Performs the algorithm in all the attributes of a query
     * These attributes must be IEText objects.
     */
    public static void detectStopWords(CBRQuery query)
    {	 
	org.apache.commons.logging.LogFactory.getLog(StopWordsDetectorSpanish.class).info("Detecting stop words.");
	Collection<IEText> texts = IEutils.getTexts(query);
        for(IEText t : texts)
            detectStopWords(t);
    }
    
    /**
     * Performs the algorithm in a given IEText object
     */
    public static void detectStopWords(IEText text)
    {
    	Set<String> stopWordSet = getStopWordSet();
		for(Token t: text.getAllTokens())
		{
		    String word = t.getRawContent().toLowerCase();
		    if(stopWordSet.contains(word))
			t.setStopWord(true);
		}
    }
        
    private static Set<String> stopWordSet = null;
    private static Set<String> getStopWordSet() {
		if(stopWordSet != null)
			return stopWordSet;
		
    	stopWordSet = new HashSet<String>(Arrays.asList(puntuationStopWords));
    	
    	try {
			BufferedReader br = new BufferedReader(new InputStreamReader(jcolibri.util.FileIO.openFile("jcolibri/extensions/textual/lucene/spanish/stopwords-spanish.txt")));
			String line = br.readLine();
			while(line != null)
			{
				stopWordSet.add(line.trim());
				line = br.readLine();
			}
		} catch (IOException e) {
			org.apache.commons.logging.LogFactory.getLog(StopWordsDetector.class).error(e);
		}
		return stopWordSet;
	}

	/**
     * Puntuation Stop words list
     */
    static String[] puntuationStopWords = { 
		",", ";", ".", ":", "_", "{", "}", "[", "]", "+", "*", "¡", "¿", "?", "=", ")", "(", "/", "&", "%", "$", "·"
    		};



}
