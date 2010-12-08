package Prueba;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;

import org.bouncycastle.asn1.eac.EACObjectIdentifiers;

import Prueba.ProvinciasGDO.Provincia;



public class BD {
	
	private String url; //Url del servidor MySQL
	private Connection con; //Conexi�n con el server
	private Statement stmt; //Instrucci�n a ejecutar
	private static String driver="com.mysql.jdbc.Driver";
	

	/**
	 * Conecta a la base de datos ssii con el usuario y contrase�a seleccionados. Establece 
	 * la url y la conexi�n.
	 * @param user String con nombre de usuario
	 * @param pass String con contrase�a del usuario
	 */
	private void conecta(String user, String pass){
		try {
	    	//Crea la conexi�n
			url="jdbc:mysql://localhost:3306/ssii";
			con = DriverManager.getConnection(url,user, pass);
			System.out.println("Usuario "+user+" conectado correctamente a "+url);
		}
		catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Petada al conectar el usuario "+user+" a "+url);
			System.err.println(e.getMessage().toString());
		}	
	}
	
	/**
	 * Cierra la conexi�n (si existe), estableciendola a null.
	 */
	private void desconecta(){
		try {
	    	//Cierra la conexi�n
			if (con!=null){
				con.close();
				con=null;
				System.out.println("desconectado correctamente");
			}
			else
				System.out.println("No hab�a una conexi�n realizada");
		}
		catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Petada al desconectar");
		}	
	}
	
	/**
	 * Ejecuta una consulta y devuelve su resultado en un ResultSet. Tiene que haber una conexi�n creada.
	 * @param consulta String con la consulta a ejecutar
	 * @return Resultset con los datos de la consulta.
	 */
	private ResultSet ejecuta(String consulta) {
		ResultSet rs=null;
		try {
			stmt=con.createStatement();
			rs=stmt.executeQuery(consulta);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	
	/**
	 * Crea un usuario con privilegios de administrador para la base de datos,
	 * en caso de que no exista.
	 * @param root String con el nombre de usuario del administrador
	 * @param pass String con la contrase�a de root
	 */
	private void creaUserDerechos(String root,String pass){
		
		System.out.println("Creando usuario con derechos para la base de datos SSII");
		
		String nuevoUser="userPrueba";
		String nuevoPass="passPrueba";
	    try {
	    	url="jdbc:mysql://localhost:3306/mysql";
	    	//Crea la conexi�n
			con =
			      DriverManager.getConnection(
			                  url,root, pass);
			
			System.out.println("URL: " + url);
		    System.out.println("Conexi�n: " + con);
		    
		    stmt=con.createStatement();
		    
		    //Crea nuevo usuario 
		    stmt.executeUpdate(
		            "GRANT SELECT,INSERT,UPDATE,DELETE," +
		            "CREATE,DROP " +
		            "ON SSII.* TO '"+nuevoUser+"'@'localhost'"+
		            "IDENTIFIED BY '"+nuevoPass+"';");
		    
		    System.out.println("Usuario creado");
		    con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Establece el driver de la BD
	 * @param nombreDriver Nombre del driver que se establecer�
	 */
	private static void cargaDriverBD(String nombreDriver){
		try {
			Class.forName(nombreDriver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error al cargar el driver de la BD");
		}
	}
	
	
	
	
	/**
	 * Introduce una pel�cula en la base de datos. Precondici�n: La conexi�n est� creada.
	 * @param peli Pel�cula a introducir
	 * @return True en caso de introducirse de forma correcta. False en otro caso.
	 */
	private boolean introducePelicula(Pelicula peli){
		boolean exito=false;
		String titulo=peli.getTitulo();
		//Trato el string, por si tiene caracteres especiales como '
		//TODO funci�n que arregle todos los caracteres especiales. http://www.javapractices.com/topic/TopicAction.do?Id=96
		titulo=titulo.replaceAll("\'", "\\\\'");
		String anyo=((Integer)peli.getAnio()).toString();
		String genero=peli.getGenero();
		String duracion=((Integer)peli.getDuracion()).toString();
		//String calificacion=peli.getCalificacion();
		String calificacion="prueba";
		//TODO Hacer la calificaci�n para introducirla correctamente en la bbdd.
		String nacionalidad=peli.getNacionalidad();
		String consulta="INSERT INTO pelicula (Titulo,A�o,Genero,Duracion,Calificacion,Nacionalidad)" +
				" VALUES ('"+titulo+"','"+anyo+"','"+genero+"','"+duracion+"','"+calificacion+"','"+nacionalidad+"');";
		System.out.println(consulta);
		try{
			stmt=con.createStatement();
			stmt.executeUpdate(consulta);
			exito=true;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error al insertar la pel�cula "+titulo+" del a�o "+anyo);
		}
		return exito;
	}
	

	
	/**
	 * Introduce un cine en la base de datos. Precondici�n: La conexi�n est� creada.
	 * @param cine Cine a introducir
	 * @return True en caso de introducirse de forma correcta. False en otro caso.
	 */
	private boolean introduceCine(Cine cine){
		boolean exito=false;
		String nombre=cine.getNombre();
		
		//TODO hay que tratar las barras de escape / y las comas ,
		String direccion=cine.getDireccion();
		
		//ProvinciasGDO p=Provincia.
		String provincia=ProvinciasGDO.getNombre(cine.getProvincia());
		String consultaID="SELECT ID FROM provincia WHERE Nombre='"+provincia+"';";
		System.out.println(consultaID);
		
		int codProvincia=0;
		//No hago un while porque s�lo hay una provincia con ese nombre
		ResultSet rs=ejecuta(consultaID);
	    try {
			rs.next();
			codProvincia= rs.getInt("ID");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Petada al sacar el ID de la provincia "+provincia);
		}
		
		//Me ha dado un valor correcto
		if (codProvincia!=0){
			String consulta="INSERT INTO cine (Nombre,Direccion,Provincia)" +
				" VALUES ('"+nombre+"','"+direccion+"','"+codProvincia+"');";
			System.out.println(consulta);
			try{
				stmt=con.createStatement();
				stmt.executeUpdate(consulta);
				exito=true;
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Error al insertar el cine "+nombre+" de la provincia "+provincia);
			}
		}

		return exito;
	}
	
	
	
	
	/**
	 * Introduce las provincias en la base de datos.
	 * @return True si las introduce correctamente.
	 */
	public boolean introduceProvincias(){
		System.out.println("Prueba de carga de peliculas");
	    cargaDriverBD(driver);
		conecta("userPrueba", "passPrueba");
		boolean exito=false;
		Provincia[] provincias=Provincia.values();
		String consulta;
		for (Provincia p : provincias) {
			String s=ProvinciasGDO.getNombre(p);
			consulta="INSERT INTO provincia (Nombre) VALUES ('"+s+"');";
			System.out.println(consulta);
			try{
			stmt=con.createStatement();
			stmt.executeUpdate(consulta);
			exito=true;
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Error al insertar la provincia "+s);
			}
		}
		desconecta();
		return exito;
	}
	
	
	/**
	 * Introduce las pel�culas que se pasan por el array a la base de datos.
	 * @param peliculas Lista de pel�culas a introducir.
	 */
	public void introducePelicula(ArrayList<Pelicula> peliculas){
		System.out.println("Prueba de carga de peliculas");
	    cargaDriverBD(driver);
		conecta("userPrueba", "passPrueba");
		for (Pelicula pelicula : peliculas) {
			introducePelicula(pelicula);
		}
		desconecta();
	}
	
	
	/**
	 * Introduce los cines que se pasan por el array a la base de datos.
	 * @param peliculas Lista de cines a introducir.
	 */
	public void introduceCine(ArrayList<Cine> cines){
		System.out.println("Prueba de carga de cines");
	    cargaDriverBD(driver);
		conecta("userPrueba", "passPrueba");
		for (Cine cine: cines) {
			introduceCine(cine);
		}
		desconecta();
	}
	

	
	
	public static void main(String args[]){
		//No tener en cuenta
	    System.out.println("Prueba de conexi�n");

	    cargaDriverBD(driver);
		
	    BD prueba=new BD();
	    prueba.desconecta();
	    prueba.creaUserDerechos("root", "ssiipass");
	    prueba.conecta("root", "ssiipass");
	    prueba.desconecta();
	    prueba.desconecta();
	    prueba.conecta("userPrueba", "passPrueba");
	    
	    String consulta="SELECT ID,TITULO FROM SSII.PELICULA;";
	    ResultSet rs=prueba.ejecuta(consulta);
	    try {
			while(rs.next()){
			    int valor= rs.getInt("id");
			    String tit = rs.getString("titulo");
			    System.out.println("\t id de pel�cula= " + valor
			                         + "\t T�tulo = " + tit);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    prueba.desconecta();
	}//main


  
  
}//clase