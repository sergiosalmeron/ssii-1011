package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import extractores.peliculas.ProcesadorCarteleraGDO;

import tads.Cine;
import tads.Pelicula;
import tads.ProvinciasGDO;
import tads.ProvinciasGDO.Provincia;



public class BD {
	
	private String url; //Url del servidor MySQL
	private Connection con; //Conexión con el server
	private Statement stmt; //Instrucción a ejecutar
	private static String driver="com.mysql.jdbc.Driver";
	
	private static String userRoot="userPrueba";
	private static String passRoot="passPrueba";
	
	private String userDerechos;
	private String passDerechos;

	/**
	 * Conecta a la base de datos ssii con el usuario y contraseña seleccionados. Establece 
	 * la url y la conexión.
	 * @param user String con nombre de usuario
	 * @param pass String con contraseña del usuario
	 */
	public void conecta(String user, String pass){
	    //Crea la conexión
		url="jdbc:mysql://localhost:3306/ssii";
		con = dameConexion(user,pass);
		System.out.println("Usuario "+user+" conectado correctamente a "+url);
	}
	
	/**
	 * Singleton para gestionar el atributo conexión
	 * @param user Nombre de usuario que conecta
	 * @param pass Pass del usuario que conecta
	 * @return Conexión con la url
	 */
	//TODO Tengo dudas de qué pasará si estoy conectado con user limitado e intento conectar con otro.
	private Connection dameConexion(String user, String pass){
		Connection conexion=null;
		if (con==null)
			try{
				conexion = DriverManager.getConnection(url,user,pass);
			}catch (SQLException e) {
				// TODO: handle exception
				System.err.println("Error al obtener la conexión del usuario "+user);
				System.err.println(e.getMessage().toString());
			}	
		else 
			conexion=con;
		return conexion;
	}
	
	/**
	 * Cierra la conexión (si existe), estableciendola a null.
	 */
	public void desconecta(){
		try {
	    	//Cierra la conexión
			if (con!=null){
				con.close();
				con=null;
				System.out.println("desconectado correctamente");
			}
			else
				System.out.println("No había una conexión realizada");
		}
		catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Petada al desconectar");
		}	
	}
	
	/**
	 * Ejecuta una consulta y devuelve su resultado en un ResultSet. Tiene que haber una conexión creada.
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
	 * en caso de que no exista. Cambia la ruta (URL).
	 * @param root String con el nombre de usuario del administrador
	 * @param pass String con la contraseña de root
	 * @param newUser String con el login del nuevo usuario
	 * @param newPass String con la contraseña del nuevo usuario
	 */
	public void creaUserDerechos(String root,String pass, String newUser, String newPass){
		System.out.println("Creando usuario con derechos para la base de datos SSII");
		url="jdbc:mysql://localhost:3306/mysql";
		con=dameConexion(root,pass);
		try{
			stmt=con.createStatement();
			String consulta="GRANT SELECT,INSERT,UPDATE,DELETE," +
            "CREATE,DROP ON SSII.* TO '"+newUser+"'@'localhost'"+
            "IDENTIFIED BY '"+newPass+"';";
			stmt.executeUpdate(consulta);
			System.out.println("Usuario creado");
			desconecta();
		}catch (SQLException e){
			e.printStackTrace();
		}
		
	}
	
	/*public void creaUserDerechos(String root,String pass, String newUser, String newPass){
		
		System.out.println("Creando usuario con derechos para la base de datos SSII");
		
		String nuevoUser=newUser;
		String nuevoPass=newPass;
	    try {
	    	url="jdbc:mysql://localhost:3306/mysql";
	    	//Crea la conexión
			con =
			      DriverManager.getConnection(
			                  url,root, pass);
			
			System.out.println("URL: " + url);
		    System.out.println("Conexión: " + con);
		    
		    stmt=con.createStatement();
		    
		    //Crea nuevo usuario 
		    stmt.executeUpdate(
		            "GRANT SELECT,INSERT,UPDATE,DELETE," +
		            "CREATE,DROP " +
		            "ON SSII.* TO '"+nuevoUser+"'@'localhost'"+
		            "IDENTIFIED BY '"+nuevoPass+"';");
		    
		    System.out.println("Usuario creado");
		    con.close();
		    con=null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	
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
	 * Introduce una película en la base de datos. Precondición: La conexión está creada.
	 * @param peli Película a introducir
	 * @return True en caso de introducirse de forma correcta. False en otro caso.
	 */
	private boolean introducePelicula(Pelicula peli){
		boolean exito=false;
		String titulo=peli.getTitulo();
		//Trato el string, por si tiene caracteres especiales como '
		//TODO función que arregle todos los caracteres especiales. http://www.javapractices.com/topic/TopicAction.do?Id=96
		titulo=titulo.replaceAll("\'", "\\\\'");
		String anyo=((Integer)peli.getAnio()).toString();
		String genero=peli.getGenero();
		//String duracion=((Integer)peli.getDuracion()).toString();
		int duracion=peli.getDuracion(); 
		String durString=trataDuracion(duracion);
		//String calificacion=peli.getCalificacion();
		String calificacion="prueba";
		//TODO Hacer la calificación para introducirla correctamente en la bbdd.
		String nacionalidad=peli.getNacionalidad();
		String consulta="INSERT INTO pelicula (Titulo,Año,Genero,Duracion,Calificacion,Nacionalidad)" +
				" VALUES ('"+titulo+"','"+anyo+"','"+genero+"','"+durString+"','"+calificacion+"','"+nacionalidad+"');";
		System.out.println(consulta);
		try{
			stmt=con.createStatement();
			stmt.executeUpdate(consulta);
			exito=true;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error al insertar la película "+titulo+" del año "+anyo);
		}
		return exito;
	}
	

	
	private String trataDuracion(int duracion) {
		Integer horas=duracion/60;
		Integer minutos=duracion%60;
		String s=horas.toString()+":"+minutos.toString();
		return s;
	}

	/**
	 * Introduce un cine en la base de datos. Precondición: La conexión está creada.
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
		//No hago un while porque sólo hay una provincia con ese nombre
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
	 * Introduce las películas que se pasan por el array a la base de datos.
	 * @param peliculas Lista de películas a introducir.
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
	

	
	//Carga el driver, y crea un usuario con privilegios si no existía.
	public BD(){
		//Carga el driver
		cargaDriverBD(driver);
		
		//conecta con derechos de root
		//conecta("root","");
		//Realmente se debería hacer llamando a conecta, y luego ejecuta. TODO
		creaUserDerechos("root", "", userRoot, passRoot);
	}
	
	
	
	public static void main(String args[]){
		BD bd=new BD();
		//TODO meter tratamiento de usar o no usar TOR
		ProcesadorCarteleraGDO pr=new ProcesadorCarteleraGDO(false);
		ArrayList<Pelicula> arr= pr.getPeliculas(ProvinciasGDO.Provincia.alava);
		bd.introducePelicula(arr);
	}
	
	/*
	public static void main(String args[]){
	    System.out.println("Prueba de conexión");

	    cargaDriverBD(driver);
		
	    BD prueba=new BD();
	    prueba.desconecta();
	    prueba.creaUserDerechos("root", "", userRoot, passRoot);
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
			    System.out.println("\t id de película= " + valor
			                         + "\t Título = " + tit);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    prueba.desconecta();
	}//main*/


  
  
}//clase