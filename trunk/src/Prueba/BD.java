package Prueba;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;


public class BD {


	String conexion;
	private static String nombreDriver="org.apache.derby.jdbc.EmbeddedDriver";

	public BD() {
		super();
		//this.conexion = conexion;
		cargaDriverBD(nombreDriver);
		setDBSystemDir();
	}
	
	/*Tiene que tener los drivers y la variable de sistema cargado.
	 * Antes de llamar a esto, siempre hay que invocar a la constructora.
	 * 
	 */ 
	public void creaBD(){
		Connection dbConnection = null;
		String strUrl = "jdbc:derby:BBDDPeliculas;create=true";
		try {
		    dbConnection = DriverManager.getConnection(strUrl);
		} catch (SQLException ex) {
		    ex.printStackTrace();
		}
	}
	
	/*
	 * Obtiene los parámetros de la clase que se pasa, y con 
	 * ellos se crea la tabla en la bbdd.
	 */
	public void creaTabla(Object o){
		Class c=o.getClass();
		Field[] params=c.getDeclaredFields();
		/*Tengo que guardar los nombres y tipos del atributo en
		 *un vector para luego ejecutar el comando de creacion de tabla.
		 */
		for (int i=0;i<params.length;i++){
			System.out.print("Parámetro número "+i+":");
			System.out.print(" Nombre :");
			System.out.print(params[i].getName());
			System.out.print(" Tipo :");
			System.out.println(params[i].getType().getName());
		}
	}
	
	private void cargaDriverBD(String nombreDriver){
		try {
			Class.forName(nombreDriver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error al cargar el driver de la BD");
		}
	}
	
	private void setDBSystemDir() {
	    // Decide on the db system directory: <userhome>/.addressbook/
	    //String userHomeDir = System.getProperty("user.home", ".");
		
		//Obtengo ruta relativa
	    File f= new File("");
	    String ruta=null;
	    try {
			ruta=f.getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error al obtener ruta de la BD");
		}
	    //String systemDir = userHomeDir + "/.addressbook";
		//Establezco la ruta de la BD
		String dirBD= ruta + "/.peliculas"; 

	    // Set the db system directory.
	    //System.setProperty("derby.system.home", systemDir);
		System.setProperty("derby.system.home", dirBD);
	}

	
	
	
}
