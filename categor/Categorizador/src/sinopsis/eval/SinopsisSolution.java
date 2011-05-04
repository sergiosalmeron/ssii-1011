package sinopsis.eval;


import java.util.Collection;

import jcolibri.cbrcore.Attribute;
import jcolibri.extensions.classification.ClassificationSolution;

public class SinopsisSolution implements jcolibri.cbrcore.CaseComponent, ClassificationSolution{

	private String  caseId;
	private Collection<String>  generos;
	
	public String toString()
	{
		return "("+caseId+";"+generos+")";
	}

	
	public String getCaseId() {
		return caseId;
	}


	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}


	public Collection<String> getGeneros() {
		return generos;
	}


	public void setGeneros(Collection<String> generos) {
		this.generos = generos;
	}
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("caseId", this.getClass());
	}

	@Override
	public Object getClassification() {
		return caseId;
	}


}
