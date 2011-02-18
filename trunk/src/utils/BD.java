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
import tads.Proyeccion;
import tads.ProvinciasGDO.Provincia;
import extractores.cines.ProcesadorCinesGDO;
import extractores.peliculas.ProcesadorCarteleraGDO;



public class BD {
	
	private Connection con; //Conexi�n con el server
	private Statement stmt; //Instrucci�n a ejecutar
	private static String driver="com.mysql.jdbc.Driver";

	
	/**
	 * Singleton para gestionar el atributo conexi�n. Si ya hab�a una conexi�n, se cierra
	 * y se abre una nueva con los par�metros pasados.
	 * @param p Par�metros de conexi�n
	 * @return Conexi�n con la BBDD
	 */
	private Connection dameConexion(ParamsConexionBD p){
		Connection conexion=null;
		try{
			if (con!=null)
				desconecta();
			conexion = DriverManager.getConnection(p.getUrl(),p.getUser(),p.getPass());
//			System.out.println("Conectado con el usuario "+p.getUser());
			con=conexion;
		}catch (SQLException e) {
				System.err.println("Error al obtener la conexi�n del usuario "+p.getUser());
				System.err.println(e.getMessage().toString());
		}	
		return conexion;
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
				//System.out.println("Usuario desconectado");
			}
			else
				System.out.println("No hab�a una conexi�n realizada");
		}
		catch (SQLException e) {
			System.err.println("Error al desconectar");
		}	
	}
	
	
	/**
	 * Ejecuta una consulta y devuelve su resultado en un ResultSet.
	 * @param consulta String con la consulta a ejecutar
	 * @param con Conexi�n que realiza la consulta
	 * @return Resultset con los datos de la consulta.
	 */
	private ResultSet ejecuta(String consulta, Connection con) {
		ResultSet rs=null;
		try {
			stmt=con.createStatement();
			rs=stmt.executeQuery(consulta);
		} catch (SQLException e) {
			System.err.println("Fallo al ejecutar la consulta "+consulta);
			System.err.println(e.getMessage());
		}
		return rs;
	}
	
	
	
	
	/**
	 * Crea un usuario con privilegios de seleccionar, insertar, actualizar y
	 * borrar registros en la base de datos.
	 * No puede crear ni eliminar tablas.
	 * @param p Par�metros de conexi�n a la base de datos. Debe tener permiso de crear y otorgar derechos a otros usuarios.
	 * @param newUser String con el login del nuevo usuario
	 * @param newPass String con la contrase�a del nuevo usuario
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
			System.err.println("Fallo al crear el usuario "+newUser+" con privilegios" +
					"para seleccionar, insertar, actualizar y borrar registros");
			System.err.println(e.getMessage());
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
			e.printStackTrace();
			System.err.println("Error al cargar el driver de la BD");
		}
	}
	
	/**
	 * Trata las cadenas, adecu�ndolas para su inserci�n en la BBDD. Cambia ' por \' y " por \'
	 * @param cadena Cadena a tratar
	 * @return Cadena modificada para su inserci�n en la BBDD, esto es, con caracteres de escape.
	 */
	private String trataCadena(String cadena){
		String s=cadena;
		if (cadena!=null){
			s=s.replaceAll("\'", "\\\\'");
			s=s.replaceAll("\"", "\\\\'");
		}
		return s;
	}
	
	/**
	 * Calcula un string con la calificaci�n que se insertar� en la BBDD
	 * en funci�n del par�metro. Puede ser "Todos los p�blicos" o "Mayores de X a�os", 
	 * siendo X el valor pasado por par�metro.
	 * @param valor Edad 
	 * @return String para poder ser insertada en la BBDD
	 */
	private String calculaCalificacion(int valor){
		String calificacion;
		if (valor==0)
			calificacion="Todos los p�blicos";
		else
			calificacion="Mayores de "+valor+" a�os";
		return calificacion;
	}
	
	
	/**
	 * Introduce una pel�cula en la base de datos. Precondici�n: La conexi�n est� creada.
	 * @param peli Pel�cula a introducir
	 * @param con Conexi�n con la base de datos
	 * @return True en caso de introducirse de forma correcta. False en otro caso.
	 */
	private boolean introducePelicula(Pelicula peli, Connection con){
		boolean exito=false;
		String titulo=peli.getTitulo();
		//Trato el string, por si tiene caracteres especiales como '
		//TODO funci�n que arregle todos los caracteres especiales. http://www.javapractices.com/topic/TopicAction.do?Id=96
		titulo=trataCadena(titulo);
			
		String anyo=((Integer)peli.getAnio()).toString();
		String genero=peli.getGenero();
		String durString=trataDuracion(peli.getDuracion());
		String calificacion=calculaCalificacion(peli.getCalificacion());
		String nacionalidad=peli.getNacionalidad();
		String url=peli.getDirWeb();
		String consulta="INSERT INTO pelicula (Titulo,Anyo,Genero,Duracion,Calificacion,Nacionalidad,Url)" +
				" VALUES ('"+titulo+"','"+anyo+"','"+genero+"','"+durString+"','"+calificacion+"','"+nacionalidad+"','"+url+"');";
//		System.out.println(consulta);
		boolean introducida=false;
		try{
			stmt=con.createStatement();
			stmt.executeUpdate(consulta);
			introducida=true;
		}
		catch (SQLException e) {
			System.err.println("Error al insertar la pel�cula "+titulo+" del a�o "+anyo);
			System.err.println(e.getMessage());
			System.err.println(consulta);
		}
		
		
		//S�lo en el caso de que no haya fallado la inserci�n de la pel�cula en la bbdd, contin�o
		if (introducida){
			//Saco el valor de ID que se le acaba de dar a la pelicula
			consulta="SELECT last_insert_id() from `ssii`.`pelicula`";
//			System.out.println(consulta);
			ResultSet rs=ejecuta(consulta,con);
			int codPelicula=0;
		    try {
				rs.next();
				codPelicula= rs.getInt(1);
			}catch (SQLException e) {
				System.err.println("Error al obtener el campo c�digo de la pel�cula reci�n introducida, con t�tulo "+titulo+" del a�o "+anyo);
				System.err.println(e.getMessage());
				System.err.println(consulta);
			}
			
			
			//Introduzco la descripci�n
			String descripcion=trataCadena(peli.getSinopsis());
			consulta="INSERT INTO Descripcion (IDPelicula, Descripcion)" +
			" VALUES ('"+codPelicula+"','"+descripcion+"');";
			try{
				stmt=con.createStatement();
//				System.out.println(consulta);
				stmt.executeUpdate(consulta);
			}
			catch (SQLException e) {
				System.err.println("Error al insertar la sinopsis de la pel�cula "+titulo);
				System.err.println(e.getMessage());
				System.err.println(consulta);
			}
			
			//Introduzco los directores
			//TODO Estoy suponiendo que existen directores...
			if (peli.getDirector()!=null){
			String[] directores=peli.getDirector().split(", ");
			consulta="INSERT INTO Dirigen (IDPelicula, Director)" +
			" VALUES ('"+codPelicula+"','";
			try {
				for (int i=0;i<directores.length;i++){
					String consultaAux=consulta+directores[i]+"');";
//					System.out.println(consultaAux);
					con.createStatement().executeUpdate(consultaAux);
				}
			}catch (SQLException e) {
				System.err.println("Error al insertar el director de la pel�cula "+titulo);
				System.err.println(e.getMessage());
				System.err.println(consulta);
			}
			}
			
			//Introduzco los actores
			//TODO Estoy suponiendo que existen actores...
			if (peli.getDirector()!=null){
			String actoresAux=peli.getInterpretes();
			String[] actores;
			if (actoresAux!=null){
				actoresAux=actoresAux.replaceAll(" y ", ", ");
				actores=actoresAux.split(", ");
			}
			else	
				actores=new String[0];

			
			consulta="INSERT INTO Actuan (IDPelicula, Actor)" +
			" VALUES ('"+codPelicula+"','";
			try {
				for (int i=0;i<actores.length;i++){
					String consultaAux=consulta+actores[i]+"');";
//					System.out.println(consultaAux);
					con.createStatement().executeUpdate(consultaAux);
				}
				exito=true;
			}catch (SQLException e) {
				System.err.println("Error al insertar los actores de la pel�cula "+titulo);
				System.err.println(e.getMessage());
//				System.err.println(consulta);
			}}
		}
		return exito;
	}
	
	/**
	 * Pasa un entero de duraci�n (en minutos) a un String de tipo HH:MM
	 * @param duracion Duraci�n en minutos a transformar
	 * @return String con formato HH:MM con la duraci�n pasada por par�metro 
	 */
	private String trataDuracion(int duracion) {
		Integer horas=duracion/60;
		Integer minutos=duracion%60;
		String s=horas.toString()+":"+minutos.toString();
		return s;
	}

	
	/**
	 * Introduce un cine en la base de datos. Precondici�n: La conexi�n est� creada.
	 * @param cine Cine a introducir
	 * @param con Conexi�n con la base de datos
	 * @return True en caso de introducirse de forma correcta. False en otro caso.
	 */
	private boolean introduceCine(Cine cine, Connection con){
		boolean exito=false;
		String nombreCine=trataCadena(cine.getNombre());
		String direccion=trataCadena(cine.getDireccion());

		String provincia=ProvinciasGDO.getNombre(cine.getProvincia());
		String ciudad=cine.getCiudad();
		String consultaID="SELECT ID FROM provincia WHERE Nombre='"+provincia+"';";
		String url=cine.getDirWebGDO();
		boolean provCorrecta,ciudadCorrecta;
		
//		System.out.println(consultaID);
		
		//Busco la provincia del cine.
		int codProvincia=0;
		//No hago un while porque s�lo hay una provincia con ese nombre
		ResultSet rs=ejecuta(consultaID,con);
	    try {
			rs.next();
			codProvincia= rs.getInt("ID");
		} catch (SQLException e) {
			System.err.println("Fallo introduciendo el cine "+nombreCine+" mientras se sacaba el ID de la provincia "+provincia);
			System.err.println(e.getMessage());
			System.err.println(consultaID);
		}
		provCorrecta=codProvincia!=0;
		
		//Saco el c�digo de la ciudad
		int codCiudad = buscaOInsertaCiudad(nombreCine, provincia, ciudad, codProvincia, con);
		ciudadCorrecta=codCiudad!=0;
		
		//Me ha dado un valor correcto
		if (provCorrecta&&ciudadCorrecta){
			String consulta="INSERT INTO cine (Nombre,Direccion,IDProvincia,Url,IDCiudad)" +
				" VALUES ('"+nombreCine+"','"+direccion+"','"+codProvincia+"','"+url+"','"+codCiudad+"');";
//			System.out.println(consulta);
			try{
				stmt=con.createStatement();
				stmt.executeUpdate(consulta);
				exito=true;
			}
			catch (SQLException e) {
				System.err.println("Error al insertar el cine "+nombreCine+" de la provincia "+provincia);
				System.err.println(e.getMessage());
				System.err.println(consulta);
			}
		}
		return exito;
	}


	/**
	 * Busca la existencia de la ciudad pasada por par�metro (ciudad) en la provincia con c�digo (codProvincia).
	 * Si la ciudad no existe, se introduce. Se devuelve el ID de la ciudad. Precondici�n: La conexi�n est� creada.
	 * @param nombreCine Nombre del cine sobre el que se busca la ciudad. Utilizado �nicamente para mostrar errores.
	 * @param provincia Nombre de la provincia sobre la que se busca la ciudad. Utilizado �nicamente para mostrar errores.
	 * @param ciudad Nombre de la ciudad buscada.
	 * @param codProvincia C�digo de la provincia en que se encuentra la ciudad.
	 * @param con Conexi�n con la base de datos. 
	 * @return ID de la ciudad (>0). Si devuelve 0, entonces ha habido un error en la inserci�n.
	 */
	private int buscaOInsertaCiudad(String nombreCine,
			String provincia, String ciudad, int codProvincia, Connection con) {
		
		String consultaID;
		ResultSet rs;
		//Busco la ciudad, si existe. Suponemos que no hay m�s de una ciudad con mismo nombre en la misma provincia
		int codCiudad=0;
		String ciudadArreglada=trataCadena(ciudad);
		consultaID="SELECT ID FROM ciudad WHERE Nombre='"+ciudadArreglada+"' AND IDProvincia='"+codProvincia+"';";
		//TODO quitar los system out
		//System.out.println(consultaID);
		rs=ejecuta(consultaID,con);
		try {
			if(rs.next())
				codCiudad=rs.getInt("ID");
		} catch (SQLException e1) {
			System.err.println("Fallo introduciendo el cine "+nombreCine+" mientras se sacaba el ID de la ciudad "+ciudadArreglada);
			System.err.println(e1.getMessage());
			System.err.println(consultaID);
		}
		
		//si codCiudad=0, entonces no he sacado su valor. Si no lo he sacado, entonces no exist�a en la BD.
		//Procedo a insertarlo.
		if (codCiudad==0){
			consultaID="INSERT INTO ciudad (Nombre, IDProvincia) " +
			"VALUES ('"+ciudadArreglada+"','"+codProvincia+"');";
			//TODO quitar los systemout
			//System.out.println(consultaID);
			try{
				stmt=con.createStatement();
				stmt.executeUpdate(consultaID);
				//Ahora est� insertado, saco su ID. Es copypaste del c�digo anterior
				consultaID="SELECT ID FROM ciudad WHERE Nombre='"+ciudadArreglada+"' AND IDProvincia='"+codProvincia+"';";
				//TODO quitar los systemout
				//System.out.println(consultaID);
				rs=ejecuta(consultaID,con);
				try {
					if(rs.next())
						codCiudad=rs.getInt("ID");
				} catch (SQLException e1) {
					System.err.println("Fallo introduciendo el cine "+nombreCine+" mientras se sacaba el ID de la ciudad "+ciudadArreglada+"despu�s de insertarla");
					System.err.println(e1.getMessage());
					System.err.println(consultaID);
				}
			}
			catch (SQLException e) {
				System.err.println("Error al insertar la ciudad "+ciudadArreglada+" del cine "+nombreCine+" de la provincia "+provincia);
				System.err.println(e.getMessage());
				System.err.println(consultaID);
			}
		}
		return codCiudad;
	}
	
	
	/**
	 * Introduce las proyecciones de un cine en la base de datos.  Precondici�n: Las pel�culas existen en la BBDD.
	 * @param cine Cine del que se van a introducir las proyecciones.
	 * @param p Par�metros de conexi�n a la base de datos. Debe tener permiso de selecci�n e inserci�n nuevos registros. 
	 * @return True si se introduce de forma correcta.
	 */
	private boolean introduceProyecciones(Cine cine,ParamsConexionBD p){
		boolean exito=false;
		Connection con;
		ArrayList<Proyeccion> arrProyecciones=cine.getPases();
		
		for (Proyeccion proyeccion : arrProyecciones) {
			//Saco el ID de la pel�cula a la que voy a meter los pases
			String urlPeli=proyeccion.getDirWebPelicula();
			String consultaID="SELECT ID FROM pelicula WHERE Url='"+urlPeli+"';";
//			System.out.println(consultaID);
			int codPeli=0;
			//No hago un while porque s�lo hay una pelicula con esa Url
			con=dameConexion(p);
			ResultSet rs=ejecuta(consultaID,con);
		    try {
				rs.next();
				codPeli= rs.getInt("ID");
			} catch (SQLException e) {
				System.err.println("Fallo al sacar el ID de pelicula mientras se introduc�an las proyecciones del cine "+cine.getNombre()+" de la provincia "+cine.getProvincia());
				System.err.println(e.getMessage());
				System.err.println(consultaID);
			}
//			System.out.println("ID de pel�cula: "+codPeli);
			
			//Saco el ID del cine al que voy a meter los pases
			String urlCine=cine.getDirWebGDO();
			consultaID="SELECT ID FROM cine WHERE Url='"+urlCine+"';";
//			System.out.println(consultaID);
			int codCine=0;
			//No hago un while porque s�lo hay un cine con esa Url
			con=dameConexion(p);
			rs=ejecuta(consultaID,con);
		    try {
				rs.next();
				codCine= rs.getInt("ID");
			} catch (SQLException e) {
				System.err.println("Fallo al sacar el ID del cine mientras se introduc�an " +
						"las proyecciones del cine "+cine.getNombre()+" de la provincia "+cine.getProvincia());
				System.err.println(e.getMessage());
				System.err.println(consultaID);
			}
//			System.out.println("ID de cine: "+codCine);
			
			//E inserto en la BBDD
			if (codCine!=0 && codPeli!=0){
				String consultaInsercion="INSERT INTO sesion (IDPelicula, IDCine, Hora, Dia)"+
				" VALUES ('"+codPeli+"','"+codCine+"','"+proyeccion.getHora()+"','"+proyeccion.getDia()+"');";
//				System.out.println("Consulta de insercion final: "+consultaInsercion);
				try{
					stmt=con.createStatement();
					stmt.executeUpdate(consultaInsercion);
					exito=true;
				}
				catch (SQLException e) {
					System.err.println("Error al insertar las proyecciones del cine "+cine.getNombre()+" de la provincia "+cine.getProvincia()+" de la pelicula con ID:"+codPeli);
					System.err.println(e.getMessage());
					System.err.println(consultaInsercion);
				}
			}
			
		}
		return exito;
	}
	
	
	/**
	 * Introduce las pel�culas que se pasan por el array a la base de datos.
	 * @param peliculas Lista de pel�culas a introducir.
	 * @param p Par�metros de conexi�n a la base de datos. Debe tener permiso de insertar nuevos registros. 
	 */
	private void introducePelicula(ArrayList<Pelicula> peliculas, ParamsConexionBD p){
		//TODO quitar los systemout
		//System.out.println("Prueba de carga de peliculas");
		Connection con=dameConexion(p);
		boolean b=true;
		for (Pelicula pelicula : peliculas) {
			b=introducePelicula(pelicula,con) && b;
		}
		desconecta();
//		System.out.println(b);
	}
	
	
	/**
	 * Introduce los cines que se pasan por el array a la base de datos.
	 * @param peliculas Lista de cines a introducir.
	 * @param p Par�metros de conexi�n a la base de datos. Debe tener permiso de insertar nuevos registros. 
	 */
	private void introduceCine(ArrayList<Cine> cines, ParamsConexionBD p){
		//TODO quitar los systemout
		//System.out.println("Prueba de carga de cines");
		Connection con=dameConexion(p);
		for (Cine cine: cines) {
			introduceCine(cine,con);
		}
		desconecta();
	}
	

	
	//Carga el driver
	/**
	 * Constructora de clase, carga el driver de la BBDD para poder realizar conexiones.
	 */
	public BD(){
		cargaDriverBD(driver);
	}
	
	
	/**
	 * Crea el espacio y las tablas de la base de datos.
	 * Si existian, las borra y vuelve a crearlas.
	 * No hay que haber realizado antes una conexi�n, pues se hace aqu� con 
	 * los datos del par�metro de entrada. Tambi�n realiza la desconexi�n.
	 * @param p Par�metros para conectar. Hace falta que el usuario tenga derechos de crear y borrar.
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
			System.err.println("Error al crear la base de datos");
			System.err.println(e.getMessage());
		}
		System.out.println("Base de datos y tablas creadas con �xito");
		desconecta();
	}
	
	
	/**
	 * Crea un usuario con derechos totales sobre la base de datos
	 * @param p Par�metros de conexi�n. El usuario debe tener derechos para crear
	 * usuarios y otorgarles permisos.
	 * @param p2 Par�metros que contienen el nombre de usuario y contrase�a del nuevo usuario.
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
			System.err.println("Fallo al crear un nuevo administrador ");
			System.err.println(e.getMessage());
		}	
	}

	/**
	 * Introduce las provincias en la base de datos. Utiliza como clave primaria el orden en que est�n
	 * declaradas las provincias en el enumerado, empleando el ordinal. Por tanto, no permite duplicados,
	 * y s�lo acepta 50 filas (tantas como provincias haya declaradas).
	 * @param p Datos con los que se realiza la conexi�n a la BBDD
	 * @return True si las introduce correctamente.
	 */
	public boolean introduceProvincias(ParamsConexionBD p){
		System.out.println("Introduciendo las provincias");
		Connection con=dameConexion(p);
		boolean exito=false;
		Provincia[] provincias=Provincia.values();
		int id;
		String consulta;
		for (Provincia prov : provincias) {
			String s=ProvinciasGDO.getNombre(prov);
			id=prov.ordinal()+1;
			consulta="INSERT IGNORE INTO provincia (ID,Nombre) VALUES ('"+id+"','"+s+"');";
			try{
				stmt=con.createStatement();
				stmt.executeUpdate(consulta);
				exito=true;
			}catch (SQLException e) {
				System.err.println("Error al insertar la provincia "+s);
				System.err.println(e.getMessage());
				System.err.println(consulta);
			}
		}
		desconecta();
		return exito;
	}
	
	/**
	 * Consulta en la BBDD si existe una pel�cula con dicha URL (recordamos que URL es unique en la BBDD).
	 * @param p Par�metros de conexi�n. El usuario debe tener permisos de consulta.
	 * @param url Url de la pel�cula que vamos a consultar
	 * @return Cierto si la pel�cula existe en la BBDD. Falso en caso contrario.
	 */
	public boolean existePelicula(ParamsConexionBD p, String url){
		boolean existe=false;
		int numPelis=0;
		Connection con=dameConexion(p);
		String consulta="SELECT COUNT(*) FROM PELICULA WHERE URL='"+url+"';";
		try{
			ResultSet rs;
			stmt=con.createStatement();
			rs=stmt.executeQuery(consulta);
			if (rs.next())
				numPelis=rs.getInt(1);
			existe=numPelis!=0;
		}catch (SQLException e) {
			System.err.println("Error al consultar si en la BBDD existe la pel�cula con url "+url);
			System.err.println(e.getMessage());
			System.err.println(consulta);
		}
		return existe;
	}
	

	/**
	 * Actualiza una provincia completamente. Sus peliculas, cines y pases.
	 * Precondici�n: La provincia tiene que existir en la BBDD.
	 * @param p Par�metros de conexi�n. El usuario debe tener permisos de inserci�n.
	 * @param prov Provincia a actualizar
	 * @param usaTor Cierto si debe emplarse Tor en la actualizaci�n.
	 * @see utils.bd#introduceProvincia  IntroduceProvincia
	 */
	public void actualizaProvincia(ParamsConexionBD p, Provincia prov,boolean usaTor){
		System.out.println("El sistema est� actualizando los datos de "+ProvinciasGDO.getNombre(prov));
		actualizaPeliculas(p,prov,usaTor);
		actualizaCines(p, prov,usaTor);
	}

	
	/**
	 * Actualiza los datos de las pel�culas que se proyecten en una provincia
	 * @param p Par�metros de conexi�n. El usuario debe tener permisos de inserci�n.
	 * @param prov Provincia a actualizar
	 * @param usaTor Cierto si debe emplarse Tor en la actualizaci�n.
	 */
	public void actualizaPeliculas(ParamsConexionBD p, Provincia prov, boolean usaTor){
		System.out.println("Procesando las peliculas. Por favor, espere...");
		//ProcesadorCarteleraGDO prPelis=new ProcesadorCarteleraGDO(usaTor);
		ProcesadorCarteleraGDO prPelis=new ProcesadorCarteleraGDO(usaTor, this, p);
		ArrayList<Pelicula> arrPelis= prPelis.getPeliculas(prov);
		introducePelicula(arrPelis, p);
		System.out.println("Fin del procesado de peliculas.");
	}
	
	/**
	 * Actualiza los datos de los cines y sus proyecciones de una provincia.
	 * Precondici�n: La provincia tiene que existir en la BBDD, y las pel�culas que �ste proyecta, tambi�n.
	 * @param p Par�metros de conexi�n. El usuario debe tener permisos de inserci�n.
	 * @param prov Provincia a actualizar
	 * @param usaTor Cierto si debe emplarse Tor en la actualizaci�n.
	 * @see utils.bd#introduceProvincia  IntroduceProvincia
	 */
	public void actualizaCines(ParamsConexionBD p, Provincia prov, boolean usaTor){
		System.out.println("Procesando los cines. Por favor, espere...");
		ProcesadorCinesGDO prCines=new ProcesadorCinesGDO(usaTor);
		ArrayList<Cine> arrCines=prCines.getCines(prov);
		introduceCine(arrCines, p);
		System.out.println("Fin del procesado de cines.");
		System.out.println("Procesando los pases. Por favor, espere...");
		for (Cine cine : arrCines) {
			introduceProyecciones(cine, p);
		}
		System.out.println("Fin del procesado de pases.");
	}
	
  
}//clase