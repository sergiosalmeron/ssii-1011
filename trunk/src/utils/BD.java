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

import recomendador.Generos;
import recomendador.PeliGeneros;
import recomendador.Usuario;
import tads.Cine;
import tads.ParamsConexionBD;
import tads.Pelicula;
import tads.ProvinciasGDO;
import tads.Proyeccion;
import tads.ProvinciasGDO.Provincia;
import extractores.cines.ProcesadorCinesGDO;
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
//			System.out.println("Conectado con el usuario "+p.getUser());
			con=conexion;
		}catch (SQLException e) {
				System.err.println("Error al obtener la conexión del usuario "+p.getUser());
				System.err.println(e.getMessage().toString());
		}	
		return conexion;
	}

	
	/**
	 * Cierra la conexión (si existe), estableciendola a null.
	 */
	private void desconecta(){
		try {
	    	//Cierra la conexión
			if (con!=null){
				con.close();
				con=null;
				//System.out.println("Usuario desconectado");
			}
			else
				System.out.println("No había una conexión realizada");
		}
		catch (SQLException e) {
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
			System.err.println("Fallo al ejecutar la consulta "+consulta);
			System.err.println(e.getMessage());
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
			System.err.println("Fallo al crear el usuario "+newUser+" con privilegios" +
					"para seleccionar, insertar, actualizar y borrar registros");
			System.err.println(e.getMessage());
		}
	}
	
	
	//TODO Arreglar esto para no tener un método igual
	/**
	 * Crea un usuario con privilegios de seleccionar, insertar, actualizar y
	 * borrar registros en la base de datos.
	 * No puede crear ni eliminar tablas.
	 * @param p Parámetros de conexión a la base de datos. Debe tener permiso de crear y otorgar derechos a otros usuarios.
	 * @param newUser String con el login del nuevo usuario
	 * @param newPass String con la contraseña del nuevo usuario
	 */
	public void creaUserDerechosFB(ParamsConexionBD p, String newUser, String newPass){
		System.out.println("Creando usuario con derechos para la base de datos SSII");
		Connection con=dameConexion(p);
		try{
			stmt=con.createStatement();
			String consulta="GRANT SELECT,INSERT,UPDATE,DELETE" +
            " ON USERSFB.* TO '"+newUser+"'@'localhost'"+
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
	 * Trata las cadenas, adecuándolas para su inserción en la BBDD. Cambia ' por \' y " por \'
	 * @param cadena Cadena a tratar
	 * @return Cadena modificada para su inserción en la BBDD, esto es, con caracteres de escape.
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
	 * Calcula un string con la calificación que se insertará en la BBDD
	 * en función del parámetro. Puede ser "Todos los públicos" o "Mayores de X años", 
	 * siendo X el valor pasado por parámetro.
	 * @param valor Edad 
	 * @return String para poder ser insertada en la BBDD
	 */
	private String calculaCalificacion(int valor){
		String calificacion;
		if (valor==0)
			calificacion="Todos los públicos";
		else
			calificacion="Mayores de "+valor+" años";
		return calificacion;
	}
	
	
	/**
	 * Introduce una película en la base de datos. Precondición: La conexión está creada.
	 * @param peli Película a introducir
	 * @param con Conexión con la base de datos
	 * @return True en caso de introducirse de forma correcta. False en otro caso.
	 */
	private boolean introducePelicula(Pelicula peli, Connection con){
		boolean exito=false;
		String titulo=peli.getTitulo();
		//Trato el string, por si tiene caracteres especiales como '
		//TODO función que arregle todos los caracteres especiales. http://www.javapractices.com/topic/TopicAction.do?Id=96
		titulo=trataCadena(titulo);
			
		String anyo=((Integer)peli.getAnio()).toString();
		String genero=peli.getGenero();
		String durString=trataDuracion(peli.getDuracion());
		String calificacion=calculaCalificacion(peli.getCalificacion());
		String nacionalidad=peli.getNacionalidad();
		String url=peli.getDirWeb();
		String rutaImg=peli.getRutaImg();
//		System.out.println(rutaImg);
		
		String consulta="INSERT INTO pelicula (Titulo,Anyo,Genero,Duracion,Calificacion,Nacionalidad,Url,RutaImg)" +
				" VALUES ('"+titulo+"','"+anyo+"','"+genero+"','"+durString+"','"+calificacion+
				"','"+nacionalidad+"','"+url+"','"+rutaImg+"');";
//		System.out.println(consulta);
		boolean introducida=false;
		try{
			stmt=con.createStatement();
			stmt.executeUpdate(consulta);
			actualizaFechaTabla(con, "pelicula");
			introducida=true;
		}
		catch (SQLException e) {
			System.err.println("Error al insertar la película "+titulo+" del año "+anyo);
			System.err.println(e.getMessage());
			System.err.println(consulta);
		}
		
		
		//Sólo en el caso de que no haya fallado la inserción de la película en la bbdd, continúo
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
				System.err.println("Error al obtener el campo código de la película recién introducida, con título "+titulo+" del año "+anyo);
				System.err.println(e.getMessage());
				System.err.println(consulta);
			}
			
			
			//Introduzco la descripción
			String descripcion=trataCadena(peli.getSinopsis());
			consulta="INSERT INTO Descripcion (IDPelicula, Descripcion)" +
			" VALUES ('"+codPelicula+"','"+descripcion+"');";
			try{
				stmt=con.createStatement();
//				System.out.println(consulta);
				stmt.executeUpdate(consulta);
				actualizaFechaTabla(con, "Descripcion");
			}
			catch (SQLException e) {
				System.err.println("Error al insertar la sinopsis de la película "+titulo);
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
				actualizaFechaTabla(con, "Dirigen");
			}catch (SQLException e) {
				System.err.println("Error al insertar el director de la película "+titulo);
				System.err.println(e.getMessage());
				System.err.println(consulta);
			}
			}
			
			/*TODO Falta por introducir los géneros de la película.
			 * Se haría aquí, al terminar, o justo después de los actores.
			 * Sería interesante hacerlo con un thread aparte, para hacer la llamada al evaluador(JColibri)
			 * en ese hilo, y al volver, introducirlo. Para eso igual hace falta otro usuario.
			 * Dejo escrito el insert.
			 * String consulta="INSERT INTO generopelicula (IDPelicula,Accion,Animacion,
			 * Aventuras,Comedia,Documental,Drama,Fantastica,Romantica,Terror,Thriller,CienciaFiccion)" +
				" VALUES ('"+codPelicula+"','"+accion+"','"+animacion+"','"+aventuras+"','"+comedia+
				"','"+documental+"','"+drama+"','"+fantastica+"','"+romantica+
				"','"+terror+"','"+thriller+"','"+cienciaFiccion+"');";
			 */
			
			
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
					actualizaFechaTabla(con, "Actuan");
					exito=true;
				}catch (SQLException e) {
					System.err.println("Error al insertar los actores de la película "+titulo);
					System.err.println(e.getMessage());
	//				System.err.println(consulta);
				}
			}
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
	 * @param con Conexión con la base de datos
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
		//No hago un while porque sólo hay una provincia con ese nombre
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
		
		//Saco el código de la ciudad
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
				actualizaFechaTabla(con, "cine");
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
	 * Busca la existencia de la ciudad pasada por parámetro (ciudad) en la provincia con código (codProvincia).
	 * Si la ciudad no existe, se introduce. Se devuelve el ID de la ciudad. Precondición: La conexión está creada.
	 * @param nombreCine Nombre del cine sobre el que se busca la ciudad. Utilizado únicamente para mostrar errores.
	 * @param provincia Nombre de la provincia sobre la que se busca la ciudad. Utilizado únicamente para mostrar errores.
	 * @param ciudad Nombre de la ciudad buscada.
	 * @param codProvincia Código de la provincia en que se encuentra la ciudad.
	 * @param con Conexión con la base de datos. 
	 * @return ID de la ciudad (>0). Si devuelve 0, entonces ha habido un error en la inserción.
	 */
	private int buscaOInsertaCiudad(String nombreCine,
			String provincia, String ciudad, int codProvincia, Connection con) {
		
		String consultaID;
		ResultSet rs;
		//Busco la ciudad, si existe. Suponemos que no hay más de una ciudad con mismo nombre en la misma provincia
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
		
		//si codCiudad=0, entonces no he sacado su valor. Si no lo he sacado, entonces no existía en la BD.
		//Procedo a insertarlo.
		if (codCiudad==0){
			consultaID="INSERT INTO ciudad (Nombre, IDProvincia) " +
			"VALUES ('"+ciudadArreglada+"','"+codProvincia+"');";
			//TODO quitar los systemout
			//System.out.println(consultaID);
			try{
				stmt=con.createStatement();
				stmt.executeUpdate(consultaID);
				actualizaFechaTabla(con, "ciudad");
				//Ahora está insertado, saco su ID. Es copypaste del código anterior
				consultaID="SELECT ID FROM ciudad WHERE Nombre='"+ciudadArreglada+"' AND IDProvincia='"+codProvincia+"';";
				//TODO quitar los systemout
				//System.out.println(consultaID);
				rs=ejecuta(consultaID,con);
				try {
					if(rs.next())
						codCiudad=rs.getInt("ID");
				} catch (SQLException e1) {
					System.err.println("Fallo introduciendo el cine "+nombreCine+" mientras se sacaba el ID de la ciudad "+ciudadArreglada+"después de insertarla");
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
	 * Introduce las proyecciones de un cine en la base de datos.  Precondición: Las películas existen en la BBDD.
	 * @param cine Cine del que se van a introducir las proyecciones.
	 * @param p Parámetros de conexión a la base de datos. Debe tener permiso de selección e inserción nuevos registros. 
	 * @return True si se introduce de forma correcta.
	 */
	private boolean introduceProyecciones(Cine cine,ParamsConexionBD p){
		boolean exito=false;
		Connection con;
		ArrayList<Proyeccion> arrProyecciones=cine.getPases();
		con=dameConexion(p);
		//Saco el ID del cine al que voy a meter los pases. Lo hago fuera porque es igual para toads las películas.		
		String urlCine=cine.getDirWebGDO();
		String consultaID="SELECT ID FROM cine WHERE Url='"+urlCine+"';";
//		System.out.println(consultaID);
		int codCine=0;
		//No hago un while porque sólo hay un cine con esa Url
		//con=dameConexion(p);
		ResultSet rs=ejecuta(consultaID,con);
	    try {
			rs.next();
			codCine= rs.getInt("ID");
		} catch (SQLException e) {
			System.err.println("Fallo al sacar el ID del cine mientras se introducían " +
					"las proyecciones del cine "+cine.getNombre()+" de la provincia "+cine.getProvincia());
			System.err.println(e.getMessage());
			System.err.println(consultaID);
		}
//		System.out.println("ID de cine: "+codCine);
		
		/*TODO Ahora debería hacer una transacción que elimine las proyecciones de este cine que estén en
		la BBDD e introduzca a continuación todas estas.*/
		
		for (Proyeccion proyeccion : arrProyecciones) {
			//Saco el ID de la película a la que voy a meter los pases
			String urlPeli=proyeccion.getDirWebPelicula();
			consultaID="SELECT ID FROM pelicula WHERE Url='"+urlPeli+"';";
//			System.out.println(consultaID);
			int codPeli=0;
			//No hago un while porque sólo hay una pelicula con esa Url
			rs=ejecuta(consultaID,con);
		    try {
				rs.next();
				codPeli= rs.getInt("ID");
			} catch (SQLException e) {
				System.err.println("Fallo al sacar el ID de pelicula mientras se introducían las proyecciones del cine "+cine.getNombre()+" de la provincia "+cine.getProvincia());
				System.err.println(e.getMessage());
				System.err.println(consultaID);
			}
//			System.out.println("ID de película: "+codPeli);
			
			
			//E inserto en la BBDD
			if (codCine!=0 && codPeli!=0){
				String consultaInsercion="INSERT INTO sesion (IDPelicula, IDCine, Hora, Dia)"+
				" VALUES ('"+codPeli+"','"+codCine+"','"+proyeccion.getHora()+"','"+proyeccion.getDia()+"');";
//				System.out.println("Consulta de insercion final: "+consultaInsercion);
				try{
					stmt=con.createStatement();
					stmt.executeUpdate(consultaInsercion);
					actualizaFechaTabla(con, "sesion");
					exito=true;
				}
				catch (SQLException e) {
					System.err.println("Error al insertar las proyecciones del cine "+cine.getNombre()+" de la provincia "+cine.getProvincia()+" de la pelicula con ID:"+codPeli);
					System.err.println(e.getMessage());
					System.err.println(consultaInsercion);
				}
			}
			
		}
		desconecta();
		return exito;
	}
	
	
	/**
	 * Introduce las películas que se pasan por el array a la base de datos.
	 * @param peliculas Lista de películas a introducir.
	 * @param p Parámetros de conexión a la base de datos. Debe tener permiso de insertar nuevos registros. 
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
	 * @param p Parámetros de conexión a la base de datos. Debe tener permiso de insertar nuevos registros. 
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
			System.err.println("Error al crear la base de datos");
			System.err.println(e.getMessage());
		}
		System.out.println("Base de datos y tablas creadas con éxito");
		desconecta();
	}

	
	/**
	 * Crea un usuario con derechos totales sobre la base de datos
	 * @param p Parámetros de conexión. El usuario debe tener derechos para crear
	 * usuarios y otorgarles permisos.
	 * @param p2 Parámetros que contienen el nombre de usuario y contraseña del nuevo usuario.
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
			//desconecta();
		}catch (SQLException e){
			System.err.println("Fallo al crear un nuevo administrador ");
			System.err.println(e.getMessage());
		}	
		desconecta();
	}

	/**
	 * Introduce las provincias en la base de datos. Utiliza como clave primaria el orden en que están
	 * declaradas las provincias en el enumerado, empleando el ordinal. Por tanto, no permite duplicados,
	 * y sólo acepta 50 filas (tantas como provincias haya declaradas).
	 * @param p Datos con los que se realiza la conexión a la BBDD
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
				actualizaFechaTabla(con, "provincia");
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
	
	public String dameFechaUltAct(ParamsConexionBD p, String tabla){
		String fecha = null;
		Connection con=dameConexion(p);
		String consulta="SELECT MAX(Fecha) FROM ACTUALIZADO;";
		try{
			ResultSet rs;
			stmt=con.createStatement();
			rs=stmt.executeQuery(consulta);
			if (rs.next())
				fecha=rs.getString(1);
		}catch (SQLException e) {
			System.err.println("Error al consultar la fecha de última actualización de la tabla "+tabla);
			System.err.println(e.getMessage());
			System.err.println(consulta);
		}
		desconecta();
		return fecha;
	}
	
	/**
	 * Función que devuelve cuándo fue actualizada la BBDD por última vez, sin importar qué tabla sea (salvo la tabla Actualizado).
	 * @param p Parámetros de conexión a la base de datos. Debe tener permiso de selección de registros.
	 * @return Fecha de última actualización de la BBDD (pero no sé la tabla).
	 */
	public String dameFechaUltAct(ParamsConexionBD p){
		String fecha = "N/D";
		Connection con=dameConexion(p);
		String consulta="SELECT MAX(Fecha) FROM actualizado;";
		try{
			ResultSet rs;
			stmt=con.createStatement();
			rs=stmt.executeQuery(consulta);
			if (rs.next())
				fecha=rs.getString(1);
		}catch (SQLException e) {
			System.err.println("Error al consultar la fecha de última actualización de la BBDD");
			System.err.println(e.getMessage());
			System.err.println(consulta);
		}
		desconecta();
		return fecha;
	}
	
	/**
	 * Consulta en la BBDD si existe una película con dicha URL (recordamos que URL es unique en la BBDD).
	 * @param p Parámetros de conexión. El usuario debe tener permisos de consulta.
	 * @param url Url de la película que vamos a consultar
	 * @return Cierto si la película existe en la BBDD. Falso en caso contrario.
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
			System.err.println("Error al consultar si en la BBDD existe la película con url "+url);
			System.err.println(e.getMessage());
			System.err.println(consulta);
		}
		desconecta();
		return existe;
	}
	

	/*private void actualizaFechaTabla(ParamsConexionBD p,String tabla){
		Connection con=dameConexion(p);
		String consulta="INSERT INTO ACTUALIZADO(Tabla,Fecha) VALUES('"+
		tabla+"',null) ON DUPLICATE KEY UPDATE fecha=null;";
		try{
			stmt=con.createStatement();
			stmt.executeUpdate(consulta);
		}catch (SQLException e) {
			System.err.println("Error al actualizar la fecha de actualización de la tabla "+tabla);
			System.err.println(e.getMessage());
			System.err.println(consulta);
		}
	}*/
	
	private void actualizaFechaTabla(Connection con,String tabla) throws SQLException {
		//Connection con=dameConexion(p);
		String consulta="INSERT INTO ACTUALIZADO(Tabla,Fecha) VALUES('"+
		tabla+"',null) ON DUPLICATE KEY UPDATE fecha=null;";
		try{
			stmt=con.createStatement();
			stmt.executeUpdate(consulta);
		}catch (SQLException e) {
			System.err.println("Error al actualizar la fecha de actualización de la tabla "+tabla);
			System.err.println(e.getMessage());
			System.err.println(consulta);
			throw e;
		}
	}
	
	
	/**
	 * Actualiza una provincia completamente. Sus peliculas, cines y pases.
	 * Precondición: La provincia tiene que existir en la BBDD.
	 * @param p Parámetros de conexión. El usuario debe tener permisos de inserción.
	 * @param prov Provincia a actualizar
	 * @param usaTor Cierto si debe emplarse Tor en la actualización.
	 * @see utils.bd#introduceProvincia  IntroduceProvincia
	 */
	public void actualizaProvincia(ProcesadorCarteleraGDO prPelis, ProcesadorCinesGDO prCines, ParamsConexionBD p, Provincia prov,boolean usaTor){
		System.out.println("El sistema está actualizando los datos de "+ProvinciasGDO.getNombre(prov));
		actualizaPeliculas(prPelis, p,prov,usaTor);
		actualizaCines(prCines, p, prov,usaTor);
	}

	
	/**
	 * Actualiza los datos de las películas que se proyecten en una provincia
	 * @param p Parámetros de conexión. El usuario debe tener permisos de inserción.
	 * @param prov Provincia a actualizar
	 * @param usaTor Cierto si debe emplarse Tor en la actualización.
	 */
	/*public void actualizaPeliculas(ProcesadorCartelera procesador,ParamsConexionBD p, Provincia prov, boolean usaTor){
		System.out.println("Procesando las peliculas. Por favor, espere...");
		//ProcesadorCarteleraGDO prPelis=new ProcesadorCarteleraGDO(usaTor);
		ProcesadorCarteleraGDO prPelis=new ProcesadorCarteleraGDO(usaTor, this, p);
		ArrayList<Pelicula> arrPelis= prPelis.getPeliculas(prov);
		introducePelicula(arrPelis, p);
		System.out.println("Fin del procesado de peliculas.");
	}*/
	public void actualizaPeliculas(ProcesadorCarteleraGDO prPelis,ParamsConexionBD p, Provincia prov, boolean usaTor){
		System.out.println("Procesando las peliculas. Por favor, espere...");
		//ProcesadorCarteleraGDO prPelis=new ProcesadorCarteleraGDO(usaTor);
		prPelis.setBBDD(this, p);
		prPelis.usaTor(usaTor);
		ArrayList<Pelicula> arrPelis= prPelis.getPeliculas(prov);
		introducePelicula(arrPelis, p);
		System.out.println("Fin del procesado de peliculas.");
	}
	
	/**
	 * Actualiza los datos de los cines y sus proyecciones de una provincia.
	 * Precondición: La provincia tiene que existir en la BBDD, y las películas que éste proyecta, también.
	 * @param p Parámetros de conexión. El usuario debe tener permisos de inserción.
	 * @param prov Provincia a actualizar
	 * @param usaTor Cierto si debe emplarse Tor en la actualización.
	 * @see utils.bd#introduceProvincia  IntroduceProvincia
	 */
	public void actualizaCines(ProcesadorCinesGDO prCines, ParamsConexionBD p, Provincia prov, boolean usaTor){
		System.out.println("Procesando los cines. Por favor, espere...");
		//ProcesadorCinesGDO prCines=new ProcesadorCinesGDO(usaTor);
		prCines.setUsaTor(usaTor);
		ArrayList<Cine> arrCines=prCines.getCines(prov);
		introduceCine(arrCines, p);
		System.out.println("Fin del procesado de cines.");
		System.out.println("Procesando los pases. Por favor, espere...");
		for (Cine cine : arrCines) {
			introduceProyecciones(cine, p);
		}
		System.out.println("Fin del procesado de pases.");
	}
	
	/**
	 * Elimina la BBDD.
	 * Precondición: La BBDD tiene que existir
	 * @param p Parámetros de conexión. El usuario debe tener permisos de eliminación de la BBDD.
	 */
	/*public void borraBBDD(ParamsConexionBD p){
		//String consulta="DROP SCHEMA `ssii`";
		String consulta="DROP TABLE IF EXISTS sesion, cine, actuan, descripcion" +
				", dirigen, ciudad, pelicula, provincia, actualizado;";
		Connection con=dameConexion(p);
		try{
			stmt=con.createStatement();
			stmt.executeUpdate(consulta);
		}catch (SQLException e) {
			System.err.println("Error al eliminar la BBDD");
			System.err.println(e.getMessage());
			System.err.println(consulta);
		}
		desconecta();
	}*/
	
	
	
	
	
	
	/**
	 * Elimina las tablas de la bbdd y vuelve a crearlas, sin registros.
	 * Precondición: La BBDD tiene que existir
	 * @param p Parámetros de conexión. El usuario debe tener permisos de eliminación y creación de tablas.
	 */
	public void resetBBDD(ParamsConexionBD p){
		String consulta="DROP TABLE IF EXISTS sesion, cine, actuan, descripcion" +
				", dirigen, ciudad, pelicula, provincia, actualizado;";
		Connection con=dameConexion(p);
		try{
			stmt=con.createStatement();
			stmt.executeUpdate(consulta);
		}catch (SQLException e) {
			System.err.println("Error al eliminar la BBDD");
			System.err.println(e.getMessage());
			System.err.println(consulta);
		}
		creaTablas(p);
		//desconecta();
	}
	
	
	
	/**
	 * Devuelve el número de cines de la BBDD
	 * Precondición: La BBDD debe existir
	 * @param p Parámetros de conexión. El usuario debe tener permisos de selección.
	 */
	public int numCines(ParamsConexionBD p){
		int numCines=-1;
		Connection con=dameConexion(p);
		String consulta="SELECT COUNT(*) FROM CINE;";
		try{
			ResultSet rs;
			stmt=con.createStatement();
			rs=stmt.executeQuery(consulta);
			if (rs.next())
				numCines=rs.getInt(1);
		}catch (SQLException e) {
			System.err.println("Error al consultar el número de cines existente");
			System.err.println(e.getMessage());
			System.err.println(consulta);
		}
		desconecta();
		return numCines;
	}
	
	/**
	 * Devuelve el número de peliculas de la BBDD
	 * Precondición: La BBDD debe existir
	 * @param p Parámetros de conexión. El usuario debe tener permisos de selección.
	 */
	public int numPelis(ParamsConexionBD p){
		int numPelis=-1;
		Connection con=dameConexion(p);
		String consulta="SELECT COUNT(*) FROM PELICULA;";
		try{
			ResultSet rs;
			stmt=con.createStatement();
			rs=stmt.executeQuery(consulta);
			if (rs.next())
				numPelis=rs.getInt(1);
		}catch (SQLException e) {
			System.err.println("Error al consultar el número de peliculas existente");
			System.err.println(e.getMessage());
			System.err.println(consulta);
		}
		desconecta();
		return numPelis;
	}
	
	
	/**
	 * Devuelve el número de sesiones de la BBDD
	 * Precondición: La BBDD debe existir
	 * @param p Parámetros de conexión. El usuario debe tener permisos de selección.
	 */
	public int numSesiones(ParamsConexionBD p){
		int numSesiones=-1;
		Connection con=dameConexion(p);
		String consulta="SELECT COUNT(*) FROM SESION;";
		try{
			ResultSet rs;
			stmt=con.createStatement();
			rs=stmt.executeQuery(consulta);
			if (rs.next())
				numSesiones=rs.getInt(1);
		}catch (SQLException e) {
			System.err.println("Error al consultar el número de sesiones existente");
			System.err.println(e.getMessage());
			System.err.println(consulta);
		}
		desconecta();
		return numSesiones;
	}
	
	public static ArrayList<String> damePeliculas(){
		//TODO meter provincia
		ParamsConexionBD params=new ParamsConexionBD("userSSII","passSSII", "jdbc:mysql://localhost:3306/ssii");
		BD b=new BD();
		Connection con=b.dameConexion(params);
		//int numProvincia=p.ordinal();
		//String consulta="SELECT d.IDPelicula, d.Descripcion FROM PELICULA p, DESCRIPCION d " +
			//	"WHERE p.ID=d.IDPelicula";
		String consulta="SELECT d.IDPelicula, d.Descripcion FROM ssii.DESCRIPCION d;";
		int IDPeli=0;
		String desc;
		ArrayList<String> arrayDevolucion=new ArrayList<String>();
		try{
			ResultSet rs;
			Statement stmt=con.createStatement();
			rs=stmt.executeQuery(consulta);
			while (rs.next()){
				IDPeli=rs.getInt(1);
				desc=rs.getString(2);
				String pelitotal=new String(IDPeli+">"+desc);
				System.out.println(pelitotal);
				arrayDevolucion.add(pelitotal);
			}
		}catch (SQLException e) {
			System.err.println("Error al consultar el número de sesiones existente");
			System.err.println(e.getMessage());
			System.err.println(consulta);
		}
		b.desconecta();
		return arrayDevolucion;
	}
	
	public static void introduceGenerosPelicula(ArrayList<PeliGeneros> arrPelis){
		ParamsConexionBD params=new ParamsConexionBD("userSSII","passSSII", "jdbc:mysql://localhost:3306/ssii");
		BD b=new BD();
		Connection con=b.dameConexion(params);
		String consulta;
		String idPelicula;
		for (PeliGeneros peliGeneros : arrPelis) {
			int numGenerosPeli=peliGeneros.getCantidadGeneros();
			idPelicula=peliGeneros.getTitulo();
			String generosString="";
			String valuesString="";
			for (int i=0;i<numGenerosPeli;i++){
				generosString=generosString+","+peliGeneros.getGenero(i).toString();
				valuesString=valuesString+","+Double.toString(peliGeneros.getPorcentaje(i));
			}
			//int lastComa=valuesString.lastIndexOf(',');
			valuesString=valuesString.substring(1);
			//lastComa=generosString.lastIndexOf(',');
			generosString=generosString.substring(1);
			consulta="INSERT INTO generopelicula("+generosString+") VALUES ("+
			valuesString+");";
			try{
				Statement stmt;
				stmt=con.createStatement();
				stmt.executeUpdate(consulta);
			}catch (SQLException e) {
				System.err.println("Error al insertar los generos de peliculas");
				System.err.println(e.getMessage());
				System.err.println(consulta);
			}
			
			//System.out.println(consulta);
		}
		b.desconecta();
	}

	
	//Dado un id de usuario, devuelve su id y su temperamento
	public static Usuario dameUsuario(String userID){
		Usuario u;
		ParamsConexionBD params=new ParamsConexionBD("userfb","ssiipass", "jdbc:mysql://localhost:3306/usersfb");
		//ParamsConexionBD params=new ParamsConexionBD("userSSII","passSSII", "jdbc:mysql://localhost:3306/ssii");
		BD b=new BD();
		Connection con=b.dameConexion(params);
		String consulta="SELECT * FROM TEMPERAMENTOS WHERE IDUSER='"+userID+"';";
		try{
			ResultSet rs;
			Statement stmt=con.createStatement();
			rs=stmt.executeQuery(consulta);
			double guardian = 0, artesano = 0,racional = 0,idealista = 0;
			if (rs.next()){
				guardian=rs.getDouble("TempGuardian");
				artesano=rs.getDouble("TempArtesano");
				idealista=rs.getDouble("TempIdealista");
				racional=rs.getDouble("TempRacional");
			}
			u=new Usuario(userID,artesano,idealista,guardian,racional);
		}catch (SQLException e) {
			System.err.println("Error al consultar el temperamento del usuario");
			System.err.println(e.getMessage());
			System.err.println(consulta);
			u=new Usuario("-1",0,0,0,0);
		}
		b.desconecta();
		return u;
	}
	
	
	//Devuelve la tabla de películas con sus género
	public static ArrayList<PeliGeneros> dameTablaGeneros(){
		ArrayList<PeliGeneros> arrPeliculas=new ArrayList<PeliGeneros>();
		ParamsConexionBD params=new ParamsConexionBD("userSSII","passSSII", "jdbc:mysql://localhost:3306/ssii");
		BD b=new BD();
		Connection con=b.dameConexion(params);
		String consulta="SELECT * FROM GENEROPELICULA;";
		String idPeli="";
		double valor;
		Generos genero=null;
		try{
			ResultSet rs;
			Statement stmt=con.createStatement();
			rs=stmt.executeQuery(consulta);
			while (rs.next()){
				idPeli=rs.getString("IDPelicula");
				PeliGeneros p=new PeliGeneros(idPeli);
				for (int i=2;i<13;i++){
					valor=rs.getDouble(i);
					if (valor!=0){
						genero=Generos.getGenero(i-2);
						p.addGenero(genero, valor);
					}
				}
				arrPeliculas.add(p);
			}
		}catch (SQLException e) {
			System.err.println("Error al consultar la tabla de géneros");
			System.err.println(e.getMessage());
			System.err.println(consulta);
		}
		b.desconecta();
		return arrPeliculas;
	}

	
	
	
	
	

}//clase