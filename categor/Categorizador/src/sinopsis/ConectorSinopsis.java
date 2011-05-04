package sinopsis;


import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import jcolibri.cbrcore.CBRCase;
import jcolibri.cbrcore.CaseBaseFilter;
import jcolibri.exception.InitializingException;
import jcolibri.extensions.textual.IE.opennlp.IETextOpenNLP;
import sinopsis.eval.SinopsisSolution;

public class ConectorSinopsis implements jcolibri.cbrcore.Connector{

	private String solutionClassName;
	
	@Override
	public void close() {
        System.exit(0);	
	}

	@Override
	public void deleteCases(Collection<CBRCase> cases) {
        throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void initFromXMLfile(URL file) throws InitializingException {
		solutionClassName = "SinopsisSolution"; 
	}

	@Override
	public Collection<CBRCase> retrieveAllCases() {
ArrayList<CBRCase> cases = new ArrayList<CBRCase>();
    	
    	try {

    		BufferedReader br = new BufferedReader(
    				new FileReader("peliculas.txt"));
    		String line = "";
    		DescripcionSinopsis desc = new DescripcionSinopsis();
    		/*<!--Acción-->
    		   <pelicula>
    		   Origen
    		   <genero>
    		   Acción
    		   Thriller
    		<sinopsis>*/
    		//String cat="";
    		ArrayList<String> cat=new ArrayList<String>();
    		String text="";
    		line = br.readLine();
    		while ((line!=null))
    		{
    			if(line.equals("<pelicula>")){
    				line = br.readLine();//saltar etiqueta
    				desc.setPeli(line);//peli
    				line = br.readLine();//saltar titulo
    				while(!((line=br.readLine()).equals("<sinopsis>"))){
    					//cat= cat+line+'\n'; //leer, linea a linea, cada categoria
    					cat.add(line);
    				}
    				desc.setGeneros(cat);
    				text=cat.toString();
    				while(!((line=br.readLine()).equals("<pelicula>")))
    				{
    					text=text+line;
    				}
    				desc.setTexto(new IETextOpenNLP(text));
    				text="";
    			}
    			CBRCase newCase = new CBRCase();
    			newCase.setDescription(desc);
    			
    			if(solutionClassName != null){
    				SinopsisSolution sol = new SinopsisSolution();
    				sol.setGeneros(desc.getGeneros());
    				sol.setCaseId(desc.getGeneros().toString());
    				newCase.setSolution(sol);
    				sol = null;
    			}
    			cases.add(newCase);
    			cat=new ArrayList<String>();
    			desc = new DescripcionSinopsis();
    		}
    	} catch (Exception e) {
    		System.err.println(e.getMessage());
    	}
    	return cases;
		
	}

	@Override
	public Collection<CBRCase> retrieveSomeCases(CaseBaseFilter filter) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void storeCases(Collection<CBRCase> cases) {
        throw new UnsupportedOperationException("Not supported yet.");	
	}
	
	public static void main(String[] args) {
	      ConectorSinopsis cc =new ConectorSinopsis();
	      for(CBRCase c: cc.retrieveAllCases())
	            System.out.println(c);
	}

}
