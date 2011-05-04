package sinopsis;

import java.util.Collection;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;
import jcolibri.datatypes.Text;

public class DescripcionSinopsis implements CaseComponent{
	private Collection<String> nombres;
	private Collection<String> verbos;
	private Collection<String> generos;
	private String peli;
	private Text texto;
	
	
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("peli",this.getClass());
	}
	
	public String toString(){
		if (getVerbos()!=null && getNombres()!=null)
			return("\nTitulo: " + getPeli() +
				"\nGeneros: " + getGeneros() +
				"\nTexto: " + getTexto().toString()+
				"\nVerbos: " +getVerbos().toString()+
				"\nNombres: " +getNombres().toString()+
				"\n********************************************");
		else
			return("\nTitulo: " + getPeli() +
					"\nGeneros: " + getGeneros() +
					"\nTexto: " + getTexto().toString()+
					"\n********************************************");
			
	}

	
	public Collection<String> getNombres() {
		return nombres;
	}


	public void setNombres(Collection<String> nombres) {
		this.nombres = nombres;
	}


	public Collection<String> getVerbos() {
		return verbos;
	}


	public void setVerbos(Collection<String> verbos) {
		this.verbos = verbos;
	}


	public Collection<String> getGeneros() {
		return generos;
	}


	public void setGeneros(Collection<String> generos) {
		this.generos = generos;
	}


	public Text getTexto() {
		return texto;
	}


	public void setTexto(Text texto) {
		this.texto = texto;
	}

	public void setPeli(String peli) {
		this.peli = peli;
	}

	public String getPeli() {
		return peli;
	}
}
