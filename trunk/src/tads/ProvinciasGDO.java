package tads;

public class ProvinciasGDO {

	public static enum Provincia{
		acoruna,
		alava,
		albacete,	
		alicante,
		almeria,
		asturias,	
		avila,
		badajoz,	
		barcelona,	
		burgos,	
		caceres,
		cadiz,
		cantabria,	
		castellon,
		ciudadreal,	
		cordoba,
		cuenca,	
		girona,	
		granada,	
		guadalajara,	
		guipuzcoa,
		huelva,	
		huesca,
		illesbalears,
		jaen,
		larioja,
		laspalmas,
		leon,
		lleida,	
		lugo,	
		madrid,	
		malaga,
		murcia,	
		navarra,	
		ourense,	
		palencia,	
		pontevedra,	
		salamanca,	
		santacruzdetenerife,
		segovia,	
		sevilla,	
		soria,	
		tarragona,	
		teruel,	
		toledo,	
		valencia,
		valladolid,	
		vizcaya,	
		zamora,	
		zaragoza
	}
	
	public static String getCodigo(Provincia pro){
		switch (pro){
			case acoruna:  return "a-coruna";
			case alicante:  return "alicante-alacant";
			case castellon:  return "castellon-castello";
			case ciudadreal:  return "ciudad-real";	
			case illesbalears:  return "illes-balears";
			case larioja:  return "la-rioja";
			case laspalmas:  return "las-palmas";
			case santacruzdetenerife:  return "santa-cruz-de-tenerife";
			case valencia:  return "valencia-valencia";
			default: return pro.toString();
		}
	}
	
	public static String getNombre(Provincia pro){
		switch (pro){
		
		case acoruna:  return "ACoru�a";
		case alava:  return "�lava";
		case albacete:  return "Albacete";
		case alicante:  return "Alicante/Alacant";
		case almeria:  return "Almer�a";
		case asturias:  return "Asturias";
		case avila:  return "�vila";
		case badajoz:  return "Badajoz";
		case barcelona:  return "Barcelona";
		case burgos:  return "Burgos";
		case caceres:  return "C�ceres";
		case cadiz:  return "C�diz";
		case cantabria:  return "Cantabria";
		case castellon:  return "Castell�n/Castell�";
		case ciudadreal:  return "CiudadReal";
		case cordoba:  return "C�rdoba";
		case cuenca:  return "Cuenca";
		case girona:  return "Girona";
		case granada:  return "Granada";
		case guadalajara:  return "Guadalajara";
		case guipuzcoa:  return "Guip�zcoa";
		case huelva:  return "Huelva";
		case huesca:  return "Huesca";
		case illesbalears:  return "Illes Balears";
		case jaen:  return "Ja�n";
		case larioja:  return "La Rioja";
		case laspalmas:  return "Las Palmas";
		case leon:  return "Le�n";
		case lleida:  return "Lleida";
		case lugo:  return "Lugo";
		case madrid:  return "Madrid";
		case malaga:  return "M�laga";
		case murcia:  return "Murcia";
		case navarra:  return "Navarra";
		case ourense:  return "Ourense";
		case palencia:  return "Palencia";
		case pontevedra:  return "Pontevedra";
		case salamanca:  return "Salamanca";
		case santacruzdetenerife:  return "Santa Cruz de Tenerife";
		case segovia:  return "Segovia";
		case sevilla:  return "Sevilla";
		case soria:  return "Soria";
		case tarragona:  return "Tarragona";
		case teruel:  return "Teruel";
		case toledo:  return "Toledo";
		case valencia:  return "Valencia/Val�ncia";
		case valladolid:  return "Valladolid";
		case vizcaya:  return "Vizcaya";
		case zamora:  return "Zamora";
		case zaragoza:  return "Zaragoza";
		default: return pro.toString();
		
		}
	}
	
}
