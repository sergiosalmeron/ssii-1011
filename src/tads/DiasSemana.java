package tads;

import tads.ProvinciasGDO.Provincia;

public class DiasSemana {
	
	public enum EnumDias {
		Viernes, Sábado, Domingo, Lunes, Martes, Miércoles, Jueves
	}
		
		public static String getDiaString(EnumDias pro){
			String resul=null;
			switch (pro){
				case Viernes:
					resul="Viernes";
					break;
				case Sábado:
					resul="Sábado";
					break;
				case Domingo:
					resul="Domingo";
					break;
				case Lunes:
					resul="Lunes";
					break;
				case Martes:
					resul="Martes";
					break;
				case Miércoles:
					resul="Miércoles";
					break;
				case Jueves:
					resul="Jueves";
					break;
			}
			return resul;
		}
		
		public static EnumDias getDiaEnum(String pro){
			if (pro!=null){
				if (pro.equalsIgnoreCase("Viernes"))
					return EnumDias.Viernes;
				if (pro.equalsIgnoreCase("Sábado"))
					return EnumDias.Sábado;
				if (pro.equalsIgnoreCase("Sabado"))
					return EnumDias.Sábado;
				if (pro.equalsIgnoreCase("Domingo"))
					return EnumDias.Domingo;
				if (pro.equalsIgnoreCase("Lunes"))
					return EnumDias.Lunes;
				if (pro.equalsIgnoreCase("Martes"))
					return EnumDias.Martes;
				if (pro.equalsIgnoreCase("Miércoles"))
					return EnumDias.Miércoles;
				if (pro.equalsIgnoreCase("Miercoles"))
					return EnumDias.Miércoles;
				if (pro.equalsIgnoreCase("Jueves"))
					return EnumDias.Jueves;
			}
			return null;
		}


}
