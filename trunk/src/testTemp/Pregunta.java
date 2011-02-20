package testTemp;
import java.util.ArrayList;


public class Pregunta {
	
	private String pregunta;
	private ArrayList<Respuesta> respuestas;
	private int respuestaElegida;
	
	public Pregunta(String pregunta){
		this.pregunta=pregunta;
		respuestas=new ArrayList<Respuesta>();
	}
	
	public void addRespuesta(String respuesta, Temperamento temperamento){
		Respuesta r= new Respuesta(respuesta,temperamento);
		respuestas.add(r);
	}
	
	
	public String toString(){
		String aux=pregunta+"\n";
		int i=0;
		for (Respuesta r : respuestas){
			aux=aux+" "+i+")"+" "+r.getRespuesta()+"\n";
			i++;
		}	
		return aux;	
	}
	
	public void setRespuestaElegida(int elegida){
		respuestaElegida=elegida;
	}
	
	public Temperamento dameResultado(){
		return respuestas.get(respuestaElegida).getTemperamento();
	}

}
