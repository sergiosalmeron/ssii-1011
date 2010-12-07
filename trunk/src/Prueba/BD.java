package Prueba;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



public class BD {
	
	private String url; //Url del servidor MySQL
	private Connection con; //Conexión con el server
	private Statement stmt; //Instrucción a ejecutar
	private static String driver="com.mysql.jdbc.Driver";
	

	/**
	 * Conecta a la base de datos ssii con el usuario y contraseña seleccionados. Establece 
	 * la url y la conexión.
	 * @param user String con nombre de usuario
	 * @param pass String con contraseña del usuario
	 */
	public void conecta(String user, String pass){
		try {
	    	//Crea la conexión
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
	 * en caso de que no exista.
	 * @param root String con el nombre de usuario del administrador
	 * @param pass String con la contraseña de root
	 */
	public void creaUserDerechos(String root,String pass){
		
		System.out.println("Creando usuario con derechos para la base de datos SSII");
		
		String nuevoUser="userPrueba";
		String nuevoPass="passPrueba";
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private static void cargaDriverBD(String nombreDriver){
		try {
			Class.forName(nombreDriver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error al cargar el driver de la BD");
		}
	}
	
	
	public static void main(String args[]){
	    System.out.println("Prueba de conexión");

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
			    System.out.println("\t id de película= " + valor
			                         + "\t Título = " + tit);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    prueba.desconecta();
	}//main


  
  
}//clase