package sinopsis;



import java.util.ArrayList;
import java.util.Collection;

import recomendador.PeliGeneros;

import jcolibri.casebase.LinealCaseBase;
import jcolibri.cbraplications.StandardCBRApplication;
import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CBRCaseBase;
import jcolibri.cbrcore.CBRQuery;
import jcolibri.exception.ExecutionException;
import jcolibri.extensions.textual.IE.common.StopWordsDetector;
import jcolibri.extensions.textual.IE.common.StopWordsDetectorSpanish;
import jcolibri.extensions.textual.IE.common.TextStemmer;
import jcolibri.extensions.textual.IE.common.TextStemmerSpanish;
import jcolibri.extensions.textual.IE.opennlp.OpennlpPOStagger;
import jcolibri.extensions.textual.IE.opennlp.OpennlpPOStaggerSpanish;
import jcolibri.extensions.textual.IE.opennlp.OpennlpSplitter;
import jcolibri.extensions.textual.IE.opennlp.OpennlpSplitterSpanish;
import jcolibri.extensions.textual.IE.representation.IEText;
import jcolibri.extensions.textual.IE.representation.Token;
import jcolibri.extensions.textual.lucene.LuceneIndexSpanish;
import jcolibri.method.retrieve.RetrievalResult;
import jcolibri.method.retrieve.NNretrieval.NNConfig;
import jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import jcolibri.method.retrieve.NNretrieval.similarity.local.textual.LuceneTextSimilaritySpanish;
import jcolibri.method.retrieve.selection.SelectCases;

public class ClasificaSinopsis implements StandardCBRApplication{
	
	public  ClasificaSinopsis sinopsis;
	private ConectorSinopsis _connector;
	private LinealCaseBase _caseBase;
	private LuceneIndexSpanish luceneIndex ;
	private String generoAsignado;

	public String getCategoriaAsignada() {
		return generoAsignado;
	}
	



	@Override
	public void configure() throws ExecutionException {
		_connector = new ConectorSinopsis();
		_connector.initFromXMLfile(null);
		_caseBase = new LinealCaseBase();
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		NNConfig simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		
		Attribute textualAttribute = new Attribute("texto", DescripcionSinopsis.class);
		simConfig.addMapping(textualAttribute, new LuceneTextSimilaritySpanish(luceneIndex,query,textualAttribute, true));
		simConfig.addMapping(textualAttribute, new LuceneTextSimilaritySpanish(luceneIndex,query,textualAttribute, true));
		OpennlpSplitter.split(query);
		StopWordsDetector.detectStopWords(query);
		TextStemmer.stem(query);
		OpennlpPOStaggerSpanish.tag(query);
		//OpennlpPOStagger.tag(query);
		extractMainTokens(query);
		
		
		// Execute NN
		Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity (_caseBase.getCases(), query, simConfig);
		// Select k cases
		eval = SelectCases.selectTopKRR(eval, 4);
		
		// Print the retrieval
		System.out.println("Retrieved cases:");
		//ArrayList<String> categorias= new ArrayList<String>();
		ArrayList<GeneroYEntero> categorias=new ArrayList<GeneroYEntero>();
		ArrayList<PeliGeneros> pG=new ArrayList<PeliGeneros>();
		for(RetrievalResult nse: eval)
		{
			String s= nse.get_case().getSolution().toString();
			int inicio=s.indexOf("([");
			int fin=s.indexOf("];");
			s=s.substring(inicio+2,fin);
			System.out.println(s);
			String[] cats=s.split(", ");
			for (String s5: cats){
				GeneroYEntero caux=new GeneroYEntero(s5,nse.getEval());;
				int j=0;
				boolean encontrado=false;
				while (!encontrado && j<categorias.size()){
					if (s5.equals(categorias.get(j).getGenero()))
						encontrado=true;
					else
						j++;
				}
				if (encontrado) 
					categorias.get(j).setPeso(categorias.get(j).getPeso()+caux.getPeso());
				else
					categorias.add(caux);
			}
			double global=0;
			double porcentaje=0;
			//generoAsignado="No se ha encontrado ninguna";
			generoAsignado="Acción 0%";
			for (int i=0;i<categorias.size();i++){
				global=global+categorias.get(i).getPeso();
				}
			if (global>0){
				generoAsignado="";
				for (int i=0;i<categorias.size();i++){
					porcentaje=categorias.get(i).getPeso()*100/global;
					generoAsignado=generoAsignado+categorias.get(i).getGenero()+" "+porcentaje+"%";
					/*if (categorias.get(i).getPeso()>1){
					if (generoAsignado.equals(" No se ha encontrado ninguna"))
						generoAsignado="";
					generoAsignado=generoAsignado+categorias.get(i).getGenero()+"; ";
				System.out.println(categorias.get(i).toString());*/
				}
			}
	
		}

		//System.out.println(generoAsignado);
		
	}

	@Override
	public void postCycle() throws ExecutionException {
		_connector.close();
		
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		_caseBase.init(_connector);
		luceneIndex = jcolibri.method.precycle.LuceneIndexCreatorSpanish.createLuceneIndex(_caseBase);
		Collection<CBRCase> cases = _caseBase.getCases();
		OpennlpSplitterSpanish.split(cases);
		StopWordsDetectorSpanish.detectStopWords(cases);
		TextStemmerSpanish.stem(cases);
		OpennlpPOStaggerSpanish.tag(cases);
		extractMainTokens(cases);
		return _caseBase;
		
	}
	
	public void extractMainTokens(Collection<CBRCase> cases)
	{
		for(CBRCase c: cases)
			extractMainTokens((DescripcionSinopsis)c.getDescription());
	}
	
	public void extractMainTokens(CBRQuery query)
	{
			extractMainTokens((DescripcionSinopsis)query.getDescription());
	}
	
	public void extractMainTokens(DescripcionSinopsis dc)
	{
		ArrayList<String> nombres = new ArrayList<String>();
		ArrayList<String> verbos = new ArrayList<String>();
		
		IEText txt=new IEText(dc.getTexto().toString());
		getMainTokens(txt,nombres, verbos);
		
		dc.setNombres(nombres);
		dc.setVerbos(verbos);
	}
	
	void getMainTokens(IEText text, Collection<String> names, Collection<String> verbs)
	{
		for(Token t: text.getAllTokens())
		{
			if(t.getPostag().startsWith("N"))
				if(t.getStem()!=null)
					names.add(t.getStem());
			if(t.getPostag().startsWith("V"))
				if(t.getStem()!=null)
					verbs.add(t.getStem());
		}
	}

}
