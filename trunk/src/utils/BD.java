package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import tads.Cine;
import tads.ParamsConexionBD;
import tads.Pelicula;
import tads.ProvinciasGDO;
import tads.ProvinciasGDO.Provincia;
import extractores.peliculas.ProcesadorCarteleraGDO;



public class BD {
	
	private Connection con; //Conexión con el server
	private Statement stmt; //Instrucción a ejecutar
	private static String driver="com.mysql.jdbc.Driver";

	
	/**
	 * Singleton para gestionar el atributo conexión. Si ya había una conexión, se cierra
	 * y se abre una nueva con los parámetros pasados.
	 * @param p Parámetros de conexión
	 * @return Conexión con la BBDD
	 */
	private Connection dameConexion(ParamsConexionBD p){
		Connection conexion=null;
		try{
			if (con!=null)
				desconecta();
			conexion = DriverManager.getConnection(p.getUrl(),p.getUser(),p.getPass());
			con=conexion;
		}catch (SQLException e) {
				// TODO: handle exception
				System.err.println("Error al obtener la conexión del usuario "+p.getUser());
				System.err.println(e.getMessage().toString());
		}	
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
			System.err.println("Error al desconectar");
		}	
	}
	
	
	/**
	 * Ejecuta una consulta y devuelve su resultado en un ResultSet.
	 * @param consulta String con la consulta a ejecutar
	 * @param con Conexión que realiza la consulta
	 * @return Resultset con los datos de la consulta.
	 */
	private ResultSet ejecuta(String consulta, Connection con) {
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
	 * Crea un usuario con privilegios de seleccionar, insertar, actualizar y
	 * borrar registros en la base de datos.
	 * No puede crear ni eliminar tablas.
	 * @param p Parámetros de conexión a la base de datos. Debe tener permiso de crear y otorgar derechos a otros usuarios.
	 * @param newUser String con el login del nuevo usuario
	 * @param newPass String con la contraseña del nuevo usuario
	 */
	public void creaUserDerechos(ParamsConexionBD p, String newUser, String newPass){
		System.out.println("Creando usuario con derechos para la base de datos SSII");
		Connection con=dameConexion(p);
		try{
			stmt=con.createStatement();
			String consulta="GRANT SELECT,INSERT,UPDATE,DELETE" +
            " ON SSII.* TO '"+newUser+"'@'localhost'"+
            "IDENTIFIED BY '"+newPass+"';";
			stmt.executeUpdate(consulta);
			System.out.println("Usuario creado");
			desconecta();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Carga el driver de la BBDD
	 * @param nombreDriver Nombre del driver a cargar
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
	
	
	private String trataCadena(String cadena){
		String s=cadena;
		s=s.replaceAll("\'", "\\\\'");
		s=s.replaceAll("\"", "\\\\'");
		return s;
	}
	
	
	/**
	 * Introduce una película en la base de datos. Precondición: La conexión está creada.
	 * @param peli Película a introducir
	 * @return True en caso de introducirse de forma correcta. False en otro caso.
	 */
	private boolean introducePelicula(Pelicula peli, Connection con){
		boolean exito=false;
		String titulo=peli.getTitulo();
		//Trato el string, por si tiene caracteres especiales como '
		//TODO función que arregle todos los caracteres especiales. http://www.javapractices.com/topic/TopicAction.do?Id=96
		titulo=trataCadena(titulo);
		
		/*if (titulo.equals("Flamenco, flamenco"));
			String s="hola";
			String veamos=peli.getSinopsis();
			System.out.println(veamos);
		return true;*/
		
		String anyo=((Integer)peli.getAnio()).toString();
		String genero=peli.getGenero();
		String durString=trataDuracion(peli.getDuracion());
		//String calificacion=peli.getCalificacion();
		String calificacion="prueba";
		//TODO Hacer la calificación para introducirla correctamente en la bbdd.
		String nacionalidad=peli.getNacionalidad();
		String consulta="INSERT INTO pelicula (Titulo,Anyo,Genero,Duracion,Calificacion,Nacionalidad)" +
				" VALUES ('"+titulo+"','"+anyo+"','"+genero+"','"+durString+"','"+calificacion+"','"+nacionalidad+"');";
		//System.out.println(consulta);
		try{
			stmt=con.createStatement();
			stmt.executeUpdate(consulta);
		//	exito=true;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error al insertar la película "+titulo+" del año "+anyo);
		}
		
		
		//Saco el valor de ID que se le acaba de dar a la pelicula
		consulta="SELECT last_insert_id() from `ssii`.`pelicula`";
		System.out.println(consulta);
		ResultSet rs=ejecuta(consulta,con);
		int codPelicula=0;
	    try {
			rs.next();
			codPelicula= rs.getInt(1);
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error al obtener el campo código de la película recién introducida, con título "+titulo+" del año "+anyo);
		}
		
		System.out.println(codPelicula);
		
		//Introduzco la descripción
		String descripcion=trataCadena(peli.getSinopsis());
		consulta="INSERT INTO Descripcion (IDPelicula, Descripcion)" +
		" VALUES ('"+codPelicula+"','"+descripcion+"');";
		try{
			stmt=con.createStatement();
			System.out.println(consulta);
			stmt.executeUpdate(consulta);
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error al insertar la sinopsis de la película "+titulo);
		}
		
		//Introduzco los directores
		String[] directores=peli.getDirector().split(", ");
		consulta="INSERT INTO Dirigen (IDPelicula, Director)" +
		" VALUES ('"+codPelicula+"','";
		try {
			for (int i=0;i<directores.length;i++){
				String consultaAux=consulta+directores[i]+"');";
				System.out.println(consultaAux);
				con.createStatement().executeUpdate(consultaAux);
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error al insertar el director de la película "+titulo);
		}
		
		
		//Introduzco los actores
		String actoresAux=peli.getInterpretes().replaceAll(" y ", ", ");
		String[] actores=actoresAux.split(", ");
		
		consulta="INSERT INTO Actuan (IDPelicula, Actor)" +
		" VALUES ('"+codPelicula+"','";
		try {
			for (int i=0;i<actores.length;i++){
				String consultaAux=consulta+actores[i]+"');";
				System.out.println(consultaAux);
				con.createStatement().executeUpdate(consultaAux);
			}
			exito=true;
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error al insertar los actores de la película "+titulo);
		}
		return exito;
	}
	
	/**
	 * Pasa un entero de duración (en minutos) a un String de tipo HH:MM
	 * @param duracion Duración en minutos a transformar
	 * @return String con formato HH:MM con la duración pasada por parámetro 
	 */
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
	private boolean introduceCine(Cine cine, Connection con){
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
		ResultSet rs=ejecuta(consultaID,con);
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
	 * Introduce las provincias en la base de datos. No tiene en cuenta si ya estaban introducidas,
	 * con lo que dos llamas al método producirían duplicidades. Controlarlo.
	 * @return True si las introduce correctamente.
	 */
	//TODO controlar duplicidades
	public boolean introduceProvincias(ParamsConexionBD p){
		System.out.println("Introduciendo las provincias");
		Connection con=dameConexion(p);
		boolean exito=false;
		Provincia[] provincias=Provincia.values();
		String consulta;
		for (Provincia prov : provincias) {
			String s=ProvinciasGDO.getNombre(prov);
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
	public void introducePelicula(ArrayList<Pelicula> peliculas, ParamsConexionBD p){
		System.out.println("Prueba de carga de peliculas");
		Connection con=dameConexion(p);
		boolean b=true;
		for (Pelicula pelicula : peliculas) {
			b=introducePelicula(pelicula,con) && b;
		}
		desconecta();
		System.out.println(b);
	}
	
	
	/**
	 * Introduce los cines que se pasan por el array a la base de datos.
	 * @param peliculas Lista de cines a introducir.
	 */
	public void introduceCine(ArrayList<Cine> cines, ParamsConexionBD p){
		System.out.println("Prueba de carga de cines");
	    //cargaDriverBD(driver);
		Connection con=dameConexion(p);
		for (Cine cine: cines) {
			introduceCine(cine,con);
		}
		desconecta();
	}
	

	
	//Carga el driver
	public BD(){
		cargaDriverBD(driver);
	}
	
	
	/**
	 * Crea el espacio y las tablas de la base de datos.
	 * Si existian, las borra y vuelve a crearlas.
	 * No hay que haber realizado antes una conexión, pues se hace aquí con 
	 * los datos del parámetro de entrada. También realiza la desconexión.
	 * @param p Parámetros para conectar. Hace falta que el usuario tenga derechos de crear y borrar.
	 */
	public void creaTablas(ParamsConexionBD p){
		String consulta="";
		String textAux="";
		File f= new File("src/bbdd/crea_tablas_FK.sql");
		System.out.println("Creando base de datos desde el archivo "+f.getAbsolutePath().toString());
		BufferedReader entrada=null;
		//Leo el archivo de texto
		try {
			entrada = new BufferedReader( new FileReader( f ) );
			while((textAux=entrada.readLine())!=null)
					consulta=consulta+textAux;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error al leer el archivo" +f.getAbsolutePath().toString());
		}
		//Conecto con permisos de root
		Connection con=dameConexion(p);
		//Separo las consultas
		String[] consultas=consulta.split(";");
		//Y las ejecuto una por una
		try {
			for (int i=0;i<consultas.length;i++)
			con.createStatement().executeUpdate(consultas[i]);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error al crear la base de datos");
		}
		desconecta();
		System.out.println("Base de datos y tablas creadas con éxito");
	}
	
	
	/**
	 * Crea un usuario con derechos totales sobre la base de datos
	 * @param p Parámetros de conexión. El usuario debe tener derechos para crear un usuario.
	 */
	public void creaNuevoAdministrador(ParamsConexionBD p,ParamsConexionBD p2) {
		System.out.println("Creando usuario con derechos de administrador para la base de datos SSII");
		String user=p2.getUser();
		String pass=p2.getPass();
		Connection con=dameConexion(p);
		try{
			stmt=con.createStatement();
			String consulta="GRANT ALL PRIVILEGES"+
            " ON SSII.* TO '"+user+"'@'localhost'"+
            "IDENTIFIED BY '"+pass+"';";
			stmt.executeUpdate(consulta);
			System.out.println("Usuario creado");
			desconecta();
		}catch (SQLException e){
			e.printStackTrace();
		}	
	}



	
  
	public static void main(String args[]){
		BD bd=new BD();
		//TODO meter tratamiento de usar o no usar TOR
		ProcesadorCarteleraGDO pr=new ProcesadorCarteleraGDO(false);
		ArrayList<Pelicula> arr= pr.getPeliculas(ProvinciasGDO.Provincia.alava);
		ParamsConexionBD p=new ParamsConexionBD("userSSII", "passSSII", "jdbc:mysql://localhost:3306/ssii");
		bd.introducePelicula(arr,p);
	}
	
  
}//clase