package testTemp;
import java.util.ArrayList;


public class TestTemperamento {
	
	private ArrayList<Pregunta> preguntas;
	private int ar=0, id=0, gu=0, ra=0;
	
	public TestTemperamento(){
		//P1
		Pregunta p1= new Pregunta("Prefiero estudiar:");
		p1.addRespuesta("Artes y manualidades.", Temperamento.Artesano);
		p1.addRespuesta("Literatura y humanidades.",Temperamento.Idealista);
		p1.addRespuesta("Negocios y finanzas.", Temperamento.Guardian);
		p1.addRespuesta("Ciencia e ingeniería.", Temperamento.Racional);
		
		//P2
		Pregunta p2= new Pregunta("Me siento mejor conmigo mismo cuando:");
		p2.addRespuesta("Mis acciones son elegantes.", Temperamento.Artesano);
		p2.addRespuesta("Estoy en armonía con alguien.",Temperamento.Idealista);
		p2.addRespuesta("Soy tan confiable como una roca.", Temperamento.Guardian);
		p2.addRespuesta("Utilizo mi ingenio.", Temperamento.Racional);
		
		//P3
		Pregunta p3= new Pregunta("En cuanto a mi estado de ánimo, con más frecuencia estoy o soy:");
		p3.addRespuesta("Emocionado y estimulado.", Temperamento.Artesano);
		p3.addRespuesta("Entusiasmado e inspirado.",Temperamento.Idealista);
		p3.addRespuesta("Precavido y prudente.", Temperamento.Guardian);
		p3.addRespuesta("Tranquilo y distante.", Temperamento.Racional);
		
		//P4
		Pregunta p4= new Pregunta("Siempre insisto en:");
		p4.addRespuesta("Perfeccionar mis habilidades.", Temperamento.Artesano);
		p4.addRespuesta("Ayudar a que otros se afirmen.",Temperamento.Idealista);
		p4.addRespuesta("Ayudar a que otros hagan lo correcto.", Temperamento.Guardian);
		p4.addRespuesta("Explicarme cómo funcionan las cosas.", Temperamento.Racional);
		
		//P5
		Pregunta p5= new Pregunta("Cuando se trata de hacer algo soy:");
		p5.addRespuesta("Práctico y oportunista.", Temperamento.Artesano);
		p5.addRespuesta("Compasivo y altruista.",Temperamento.Idealista);
		p5.addRespuesta("Concienzudo y diligente.", Temperamento.Guardian);
		p5.addRespuesta("Eficiente y pragmático.", Temperamento.Racional);
		
		//P6
		Pregunta p6= new Pregunta("Me respeto más por:");
		p6.addRespuesta("Ser audaz y con espíritu de aventura.", Temperamento.Artesano);
		p6.addRespuesta("Ser bondadoso y con buena voluntad.",Temperamento.Idealista);
		p6.addRespuesta("Realizar buenas obras.", Temperamento.Guardian);
		p6.addRespuesta("Ser autónomo e independiente.", Temperamento.Racional);
		
		//P7
		Pregunta p7= new Pregunta("Me inclino más a confiar en:");
		p7.addRespuesta("Impulsos y caprichos.", Temperamento.Artesano);
		p7.addRespuesta("Intuiciones e insinuaciones.",Temperamento.Idealista);
		p7.addRespuesta("Costumbres y tradiciones.", Temperamento.Guardian);
		p7.addRespuesta("La razón pura y la lógica formal.", Temperamento.Racional);
		
		//P8
		Pregunta p8= new Pregunta("A veces anhelo:");
		p8.addRespuesta("Causar una gran impresión y tener impacto.", Temperamento.Artesano);
		p8.addRespuesta("Perderme en mis sueños románticos.",Temperamento.Idealista);
		p8.addRespuesta("Ser un miembro legítimo y valorado.", Temperamento.Guardian);
		p8.addRespuesta("Hacer un gran descubrimiento.", Temperamento.Racional);
		
		//P9
		Pregunta p9= new Pregunta("En mi vida busco:");
		p9.addRespuesta("Aventuras y emociones.", Temperamento.Artesano);
		p9.addRespuesta("Conocimiento de mi mismo.",Temperamento.Idealista);
		p9.addRespuesta("Protección y seguridad.", Temperamento.Guardian);
		p9.addRespuesta("Métodos de operación eficaces.", Temperamento.Racional);
		
		//P10
		Pregunta p10= new Pregunta("Al mirar al futuro:");
		p10.addRespuesta("Aseguro que ocurrirá algo bueno.", Temperamento.Artesano);
		p10.addRespuesta("Creo en la bondad innata de la gente.",Temperamento.Idealista);
		p10.addRespuesta("Hay que ser sumamente cuidadoso.", Temperamento.Guardian);
		p10.addRespuesta("Es mejor ser cauteloso.", Temperamento.Racional);
		
		//P11
		Pregunta p11= new Pregunta("Si fuera posible, me gustaría convertirme en:");
		p11.addRespuesta("Un artista virtuoso.", Temperamento.Artesano);
		p11.addRespuesta("Un visionario sabio.",Temperamento.Idealista);
		p11.addRespuesta("Un ejecutivo de alto rango.", Temperamento.Guardian);
		p11.addRespuesta("Un genio de la tecnología.", Temperamento.Racional);
		
		//P12
		Pregunta p12= new Pregunta("Funcionaría mejor en un trabajo si me ocupara de:");
		p12.addRespuesta("Herramientas y equipo.", Temperamento.Artesano);
		p12.addRespuesta("Desarrollo de recursos humanos.",Temperamento.Idealista);
		p12.addRespuesta("Materiales y servicios.", Temperamento.Guardian);
		p12.addRespuesta("Sistemas y estructuras.", Temperamento.Racional);
		
		//P13
		Pregunta p13= new Pregunta("Como dirigente de acciones, sobre todo miro:");
		p13.addRespuesta("Las ventajas inmediatas.", Temperamento.Artesano);
		p13.addRespuesta("Las posibilidades futuras.",Temperamento.Idealista);
		p13.addRespuesta("La experiencia pasada.", Temperamento.Guardian);
		p13.addRespuesta("Las condiciones necesarias y suficientes.", Temperamento.Racional);
		
		//P14
		Pregunta p14= new Pregunta("Confío más en mí mismo cuando soy:");
		p14.addRespuesta("Flexible y adaptable.", Temperamento.Artesano);
		p14.addRespuesta("Genuino y auténtico.",Temperamento.Idealista);
		p14.addRespuesta("Respetable y honorable.", Temperamento.Guardian);
		p14.addRespuesta("Resuelto y tesonero.", Temperamento.Racional);
		
		//P15
		Pregunta p15= new Pregunta("Aprecio cuando los demás:");
		p15.addRespuesta("Me sorprenden con su generosidad.", Temperamento.Artesano);
		p15.addRespuesta("Reconocen mi personalidad verdadera.",Temperamento.Idealista);
		p15.addRespuesta("Expresan su gratitud.", Temperamento.Guardian);
		p15.addRespuesta("Preguntan cuáles son mis motivos.", Temperamento.Racional);
		
		//P16
		Pregunta p16= new Pregunta("Cuando pienso en mis desventuras:");
		p16.addRespuesta("Por lo general me río.", Temperamento.Artesano);
		p16.addRespuesta("A menudo me pregunto por qué ocurren.",Temperamento.Idealista);
		p16.addRespuesta("Intento sacar el mejor partido.", Temperamento.Guardian);
		p16.addRespuesta("Las considero desde una perspectiva amplia.", Temperamento.Racional);
		
		preguntas=new ArrayList<Pregunta>();
		preguntas.add(p1);
		preguntas.add(p2);
		preguntas.add(p3);
		preguntas.add(p4);
		preguntas.add(p5);
		preguntas.add(p6);
		preguntas.add(p7);
		preguntas.add(p8);
		preguntas.add(p9);
		preguntas.add(p10);
		preguntas.add(p11);
		preguntas.add(p12);
		preguntas.add(p13);
		preguntas.add(p14);
		preguntas.add(p15);
		preguntas.add(p16);
	}
	
	public ArrayList<Pregunta> getPreguntas(){
		return preguntas;
	}
	
	public String dameResultado(){
		String aux="** Resultado:\n";
		double total=preguntas.size();
		
		for (Pregunta p: preguntas){
			suma(p.dameResultado());
		}
		
		aux=aux+"** - Artesano: "+(ar/total)*100+"%\n";
		aux=aux+"** - Guardian: "+(gu/total)*100+"%\n";
		aux=aux+"** - Idealista: "+(id/total)*100+"%\n";
		aux=aux+"** - Racional: "+(ra/total)*100+"%\n";
		
		return aux;
	}
	
	public ArrayList<Double> dameArrayListResultado(){
		ArrayList<Double> resultado=new ArrayList<Double>();
		double total=preguntas.size();
		for (Pregunta p: preguntas){
			suma(p.dameResultado());
		}
		double a=(ar/total)*100;
		resultado.add(a);
		
		a=(gu/total)*100;
		resultado.add(a);
		
		a=(id/total)*100;
		resultado.add(a);
		
		a=(ra/total)*100;
		resultado.add(a);
		
		return resultado;
	}
	
	private void suma(Temperamento t){
		switch(t){
		case Artesano: ar++; break;
		case Idealista: id++; break;
		case Guardian: gu++; break;
		case Racional: ra++; break;
		}
	}

}
