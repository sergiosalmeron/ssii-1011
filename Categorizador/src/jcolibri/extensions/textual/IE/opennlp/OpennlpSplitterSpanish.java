/**
 * OpennlpSplitterSpanish.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-García.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 21/06/2007
 */

package jcolibri.extensions.textual.IE.opennlp;

import java.io.IOException;
import java.util.Collection;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.extensions.textual.IE.IEutils;
import jcolibri.extensions.textual.IE.representation.IEText;
import jcolibri.extensions.textual.IE.representation.Paragraph;
import jcolibri.extensions.textual.IE.representation.Sentence;
import jcolibri.extensions.textual.IE.representation.Token;
import jcolibri.util.AttributeUtils;
import jcolibri.util.ProgressController;
import opennlp.tools.lang.spanish.SentenceDetector;
import opennlp.tools.lang.spanish.Tokenizer;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.tokenize.TokenizerME;

/**
 * Organizes an IETextOpenNLP object in paragraphs, sentences and tokens.
 * This implementation uses maximum entropy algorithms to obtain sentences and tokens.
 * @author Juan A. Recio-Garcia
 * @version 1.0
 *
 */
public class OpennlpSplitterSpanish
{    
    /**
     * Performs the algorithm in the given attributes of a collection of cases.
     * These attributes must be IETextOpenNLP objects.
     */
    public static void split(Collection<CBRCase> cases, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(OpennlpSplitterSpanish.class).info("Splitting OpenNLP text.");
	ProgressController.init(OpennlpSplitterSpanish.class, "Splitting OpenNLP text", cases.size());
	for(CBRCase c: cases)
	{
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, c);
		if(o instanceof IETextOpenNLP)
		    split((IETextOpenNLP)o);
	    }
	    ProgressController.step(OpennlpSplitterSpanish.class);
	}
	ProgressController.finish(OpennlpSplitterSpanish.class);
    }

    /**
     * Performs the algorithm in the given attributes of a query.
     * These attributes must be IETextOpenNLP objects.
     */
    public static void split(CBRQuery query, Collection<Attribute> attributes)
    {
	org.apache.commons.logging.LogFactory.getLog(OpennlpSplitterSpanish.class).info("Splitting OpenNLP text.");
	    for(Attribute a: attributes)
	    {
		Object o = AttributeUtils.findValue(a, query);
		if(o instanceof IETextOpenNLP)
		    split((IETextOpenNLP)o);
	    }
    }
    
    /**
     * Performs the algorithm in all the IETextOpenNLP typed attributes of a collection of cases.
     */
    public static void split(Collection<CBRCase> cases)
    {
	org.apache.commons.logging.LogFactory.getLog(OpennlpSplitterSpanish.class).info("Splitting OpenNLP text.");
	ProgressController.init(OpennlpSplitterSpanish.class, "Splitting OpenNLP text", cases.size());
	for(CBRCase c: cases)
	{
	    Collection<IEText> texts = IEutils.getTexts(c);
	    for(IEText t : texts)
		if(t instanceof IETextOpenNLP)
		    split((IETextOpenNLP)t);
	    ProgressController.step(OpennlpSplitterSpanish.class);
	}
	ProgressController.finish(OpennlpSplitterSpanish.class);
    }
    
    /**
     * Performs the algorithm in all the IETextOpenNLP typed attributes of a query.
     */ 
    public static void split(CBRQuery query)
    {	 
	org.apache.commons.logging.LogFactory.getLog(OpennlpSplitterSpanish.class).info("Splitting OpenNLP text.");
	Collection<IEText> texts = IEutils.getTexts(query);
        for(IEText t : texts)
            if(t instanceof IETextOpenNLP)
        	split((IETextOpenNLP)t);
    }
    
    
    
    public static void split(IETextOpenNLP text)
    {
		try
		{
	    	String content = text.getRAWContent();
	    	for(String parText: content.split("\n{2,}"))
	    	{
	    		Paragraph myPar = new Paragraph(parText);
	    		text.addParagraph(myPar);
	    		
	    		SentenceDetectorME sentenceDetector = getSentenceDetector();
	    		for(String sentText: sentenceDetector.sentDetect(parText))
	    		{
	    			Sentence mySent = new Sentence(sentText);
	    			myPar.addSentence(mySent);
	    			
	    			TokenizerME tokenizer = getTokeniser();
	    			for(String tokenText : tokenizer.tokenize(sentText))
	    				mySent.addToken(new Token(tokenText));
	    		}
	    	}
		} catch (Exception e)
		{
		    org.apache.commons.logging.LogFactory.getLog(OpennlpSplitterSpanish.class).error(e);   
		}
    }
    

    
    private static TokenizerME tokeniser = null;
    private static TokenizerME getTokeniser() throws Exception
    {
	if(tokeniser == null)
	    tokeniser = new Tokenizer("jcolibri/extensions/textual/IE/opennlp/data/SpanishTok.bin.gz");
	return tokeniser;
    }
    
    private static SentenceDetectorME spanishSentenceDetector = null;
    private static SentenceDetectorME getSentenceDetector()
    {
		if(spanishSentenceDetector == null)
			try {
				spanishSentenceDetector = new SentenceDetector("jcolibri/extensions/textual/IE/opennlp/data/SpanishSent.bin.gz");
			} catch (IOException e) {
				
			}
		return spanishSentenceDetector;
    }
    
    
}
