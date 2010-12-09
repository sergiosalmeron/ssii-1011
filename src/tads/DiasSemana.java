package tads;

import tads.ProvinciasGDO.Provincia;

public class DiasSemana {
	
	public enum EnumDias {
		Viernes, S�bado, Domingo, Lunes, Martes, Mi�rcoles, Jueves
	}
		
		public static String getDiaString(EnumDias pro){
			String resul=null;
			switch (pro){
				case Viernes:
					resul="Viernes";
					break;
				case S�bado:
					resul="S�bado";
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
				case Mi�rcoles:
					resul="Mi�rcoles";
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
				if (pro.equalsIgnoreCase("S�bado"))
					return EnumDias.S�bado;
				if (pro.equalsIgnoreCase("Sabado"))
					return EnumDias.S�bado;
				if (pro.equalsIgnoreCase("Domingo"))
					return EnumDias.Domingo;
				if (pro.equalsIgnoreCase("Lunes"))
					return EnumDias.Lunes;
				if (pro.equalsIgnoreCase("Martes"))
					return EnumDias.Martes;
				if (pro.equalsIgnoreCase("Mi�rcoles"))
					return EnumDias.Mi�rcoles;
				if (pro.equalsIgnoreCase("Miercoles"))
					return EnumDias.Mi�rcoles;
				if (pro.equalsIgnoreCase("Jueves"))
					return EnumDias.Jueves;
			}
			return null;
		}


}
