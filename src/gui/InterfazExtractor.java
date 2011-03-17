package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import com.sun.jmx.trace.Trace;
import com.sun.xml.internal.fastinfoset.tools.PrintTable;

import tads.Cine;
import tads.ModoFuncionamiento;
import tads.ParamsConexionBD;
import tads.ProvinciasGDO;
import tads.ProvinciasGDO.Provincia;
import utils.BD;

public class InterfazExtractor extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JProgressBar progressBar;
	private JLabel indicador,ultActCompleta,fechaUltActCompleta,
			ultProvActualizada,provActualizada,pelis,cines,sesiones;
	private JButton butInicio,butParada,butBorrarBD,butReiniciarBD,butContinua,butCrearBD;
	private TransparentButton butActualizar;
	private int totalExtracciones;
	private ListenerGUI manejador;
	private Logica	logica;
	private Thread thread;
	private JComboBox provinciasCombo;
	private JRadioButton todaProvinciaRB,sesionRB,cineRB,peliRB;
	private ButtonGroup grupo;
	private boolean actualizar;
	private ModoFuncionamiento funcionamiento;

	//Utilizado para saber si se ha parado la ejecución, actualización Parada.
	private boolean actParada;
	

	public void setProvActualizada(JLabel provActualizada) {
		this.provActualizada = provActualizada;
	}

	
	public boolean isActualizar() {
		return actualizar;
	}

	public ModoFuncionamiento getFuncionamiento() {
		return funcionamiento;
	}
	
	public void setFechaUltActCompleta(JLabel fechaUltActCompleta) {
		this.fechaUltActCompleta = fechaUltActCompleta;
	}

	public InterfazExtractor(){
		logica = new Logica(this); 
		thread = new Thread(logica); 
		totalExtracciones=Provincia.values().length;
		manejador=new ListenerGUI(this);
		actualizar=false;
		funcionamiento=ModoFuncionamiento.PELI;
		actParada=logica.consultaEjecucionPendiente();
		this.add(construyeInterfaz());
		this.setMinimumSize(new Dimension(575,275));
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("MoodVie - Extractor de información");
	}
	
	private JPanel construyeInterfaz(){		
		//Creo el Panel Principal
		//JPanel panelPrincipal=new JPanel(new FlowLayout());
		JPanel panelPrincipal=new JPanel(new BorderLayout());
		
		//Creo el panel de la imagen del logo
		JPanel panelImagen = creaPanelImagen();
		//Creo el panel con los contenidos
		JPanel panelInfo = creaPanelContenidos();
	    
		//Añado el panel de la imagen a la izquierda
		panelPrincipal.add(panelImagen,BorderLayout.WEST);
		panelPrincipal.add(panelInfo,BorderLayout.CENTER);
		
		//Bordes y restricciones al panel principal
		panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		panelPrincipal.setMinimumSize(new Dimension(250,150));
		return panelPrincipal;
	}

	private JPanel creaPanelImagen() {
		JPanel panelImagen=new JPanel();
		JLabel etiqueta= new JLabel();
		ImageIcon icono=new ImageIcon("src/gui/imagenes/logo.jpg");
		Image img = icono.getImage();  
		Image newimg = img.getScaledInstance(230, 200,  java.awt.Image.SCALE_SMOOTH);  
		etiqueta.setIcon(new ImageIcon(newimg));
		panelImagen.add(etiqueta);
		return panelImagen;
	}
	
	
	private JPanel creaPanelContenidos() {
		JPanel panelInfo=new JPanel();
		//Panel de Actualizar y de Administrar
		JTabbedPane panelSeleccion=new JTabbedPane();
		panelSeleccion.addTab("Actualizar",creaPanelActualizar());
		panelSeleccion.addTab("Administrar",creaPanelAdministrar());
		panelInfo.add(panelSeleccion);
		return panelInfo;
	}


	private JPanel creaPanelActualizar(){
		JPanel panelActualizar=new JPanel();
		//panelActualizar.setMinimumSize(new Dimension(150,120));
		panelActualizar.setLayout(new BoxLayout(panelActualizar, BoxLayout.Y_AXIS));
		panelActualizar.add(creaPanelRadioYComboActualizar());
		panelActualizar.add(creaPanelProgresoYBotones());
		return panelActualizar;
	}
	
	
	private GridBagConstraints rellenaConstraints(int x,int y, int anchoCelda, int altoCelda, int ancla){
		GridBagConstraints cons=new GridBagConstraints();
		cons.gridx=x;
		cons.gridy=y;
		cons.gridwidth=anchoCelda;
		cons.gridheight=altoCelda;
		cons.anchor=ancla;
		return cons;
	}
	
	private JPanel creaPanelAdministrar(){
		JPanel panelAdministrar=new JPanel();
		panelAdministrar.setLayout(new BoxLayout(panelAdministrar, BoxLayout.Y_AXIS));
		
		JPanel panelInfo=new JPanel();
		GridBagLayout gbl=new GridBagLayout();
		GridBagConstraints cons=new GridBagConstraints();
		panelInfo.setLayout(gbl);
		
		//Creación de etiquetas
		ultActCompleta=new JLabel("Última actualización:");
		cons=rellenaConstraints(1, 1, 1, 1, GridBagConstraints.WEST);
		panelInfo.add(ultActCompleta,cons);
		
		fechaUltActCompleta=new JLabel();
		cons=rellenaConstraints(2,1,1,1,GridBagConstraints.EAST);
		panelInfo.add(fechaUltActCompleta,cons);
		
		ultProvActualizada=new JLabel("Última provincia actualizada:");
		cons=rellenaConstraints(1,2,1,1,GridBagConstraints.WEST);
		cons.ipadx=15;
	    panelInfo.add(ultProvActualizada,cons);
		
	    //TODO GUARDAR ÚLTIMA PROVINCIA ACTUALIZADA
	    provActualizada=new JLabel("PENSAR CÓMO HACER ESTO");
	    cons=rellenaConstraints(2,2,1,1,GridBagConstraints.EAST);
	    cons.ipadx=0;
		panelInfo.add(provActualizada,cons);

		butActualizar=new TransparentButton();
		//Redimensión del icono
		ImageIcon icono=new ImageIcon("src/gui/imagenes/reload.png");
		Image img = icono.getImage();  
		Image newimg = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH);
		butActualizar.setIcon(new ImageIcon(newimg));
		icono=new ImageIcon("src/gui/imagenes/reload_over.jpg");
		img=icono.getImage();
		newimg= img.getScaledInstance(20,20, java.awt.Image.SCALE_SMOOTH);
		butActualizar.setRolloverIcon(new ImageIcon(newimg));
		butActualizar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			actualizaCampos();
			}
		});
		cons=rellenaConstraints(3,2,1,2,GridBagConstraints.WEST);
		cons.ipadx=0;
		panelInfo.add(butActualizar,cons);
		
		pelis=new JLabel();
		cons=rellenaConstraints(1, 3, 1, 1, GridBagConstraints.WEST);
		panelInfo.add(pelis,cons);		
		cines=new JLabel();
		cons=rellenaConstraints(1, 4, 1, 1, GridBagConstraints.WEST);
		panelInfo.add(cines,cons);
		sesiones=new JLabel();
		cons=rellenaConstraints(1, 5, 1, 1, GridBagConstraints.WEST);
		panelInfo.add(sesiones,cons);
		//Actualización de los campos, rellenando los valores
		actualizaCampos();
		
		//Creación de botones
		JPanel panelBotonesAdm=new JPanel(gbl);
		/*butBorrarBD=new JButton("Borrar BD");
		butBorrarBD.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO Poner un modal para preguntar antes de borrar la BBDD
				eliminaBD();
				actualizaCampos();
			}
		});
		cons=rellenaConstraints(1, 1, 1, 1, GridBagConstraints.WEST);
		panelBotonesAdm.add(butBorrarBD,cons);*/
		
		
		butCrearBD=new JButton("Crear BD");
		butCrearBD.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO Poner un modal para preguntar antes de borrar la BBDD
				final BD bd=new BD();
				final ParamsConexionBD p=new ParamsConexionBD("userSSII","passSSII", "jdbc:mysql://localhost:3306/ssii");
				bd.creaTablas(p);
				System.out.println("¡No hace nada!");
			}
		});
		cons=rellenaConstraints(1, 1, 1, 1, GridBagConstraints.WEST);
		panelBotonesAdm.add(butCrearBD,cons);
		
		butReiniciarBD=new JButton("Eliminar entradas");
		butReiniciarBD.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO Poner un modal para preguntar antes de resetear la BBDD
				reseteaBD();
				actualizaCampos();
			}
		});
		cons=rellenaConstraints(2,1,1,1,GridBagConstraints.EAST);
		panelBotonesAdm.add(butReiniciarBD,cons);
				
		JTextArea area=new JTextArea("Borrar no tiene funcionalidad. Falta introducir los " +
				"modales de confirmación");
		cons=rellenaConstraints(1,2,2,2,GridBagConstraints.CENTER);
		panelBotonesAdm.add(area,cons);
		

		panelAdministrar.add(panelInfo);
		panelAdministrar.add(panelBotonesAdm);
		panelAdministrar.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		return panelAdministrar;
	}
	
	
	private void actualizaCampos(){
		final BD bd=new BD();
		//final ParamsConexionBD p=new ParamsConexionBD("userSSII","passSSII", "jdbc:mysql://localhost:3306/ssii");
		final ParamsConexionBD p=new ParamsConexionBD("userSSII","passSSII", "jdbc:mysql://localhost:3306/ssii");
		actualizaUltFecha(bd.dameFechaUltAct(p));	
		actualizaNumPelis(bd.numPelis(p));
		actualizaNumCines(bd.numCines(p));
		actualizaNumSesiones(bd.numSesiones(p));
	}
	
	private void eliminaBD(){
		final BD bd=new BD();
		final ParamsConexionBD p=new ParamsConexionBD("rootNuevo","passNuevo", "jdbc:mysql://localhost:3306/ssii");
		//TODO no tiene funcionalidad, posiblemente quite el botón
		System.out.println("No tiene funcionalidad");
		//bd.borraBBDD(p);
	}
	
	private void reseteaBD(){
		final BD bd=new BD();
		final ParamsConexionBD p=new ParamsConexionBD("rootNuevo","passNuevo", "jdbc:mysql://localhost:3306/ssii");
		bd.resetBBDD(p);
	}
	
	private void actualizaUltFecha(String fecha){
		fechaUltActCompleta.setText(fecha);	;
	}
	private void actualizaNumPelis(int nPelis){
		pelis.setText("Nº Peliculas: "+nPelis);
	}
	private void actualizaNumCines(int nCines){
		cines.setText("Nº Cines: "+nCines);
	}
	private void actualizaNumSesiones(int nSesiones){
		sesiones.setText("Nº Sesiones: "+nSesiones);
	}
	
	
	/**
	 * @return
	 */
	private JPanel creaPanelProgresoYBotones() {
		JPanel panelProgreso=new JPanel();
		GridBagLayout gb=new GridBagLayout();
		panelProgreso.setLayout(gb);
				
		GridBagConstraints cons=new GridBagConstraints();
	    progressBar = new JProgressBar(0, 2*totalExtracciones);
	    cons=rellenaConstraints(0,0,3,1,GridBagConstraints.NORTH);
	    //cons.ipadx=80;
	    panelProgreso.add(progressBar,cons);
	    	    
		indicador=new JLabel("Pulse 'Inicio' para empezar...");
		cons=rellenaConstraints(0,1,3,2,GridBagConstraints.CENTER);
	    panelProgreso.add(indicador,cons);
		
		butInicio=new JButton();
		butInicio.setText("Inicio");
		butInicio.addActionListener(manejador);
		cons=rellenaConstraints(0,3,1,1,GridBagConstraints.WEST);
	    panelProgreso.add(butInicio,cons);		

		butParada=new JButton();
		butParada.setText("Parada");
		butParada.addActionListener(manejador);
		butParada.setEnabled(false);
		cons=rellenaConstraints(1,3,1,1,GridBagConstraints.CENTER);
	    panelProgreso.add(butParada,cons);
	    
		butContinua=new JButton();
		butContinua.setText("Continua");
		butContinua.addActionListener(manejador);
		//TODO función que me diga si hay o no una ejecución a medias, igualado a actParada.
		butContinua.setEnabled(this.actParada);
		cons=rellenaConstraints(2, 3, 1, 1, GridBagConstraints.EAST);
	    panelProgreso.add(butContinua,cons);
		
	    
		panelProgreso.setBorder(BorderFactory.createEmptyBorder(15, 5, 10, 5));
		return panelProgreso;
	}
	
	

	

	
	
	private JPanel creaPanelRadioYComboActualizar(){
		
		JPanel panelSeleccion=new JPanel();
		GridBagLayout gt=new GridBagLayout();
		panelSeleccion.setLayout(gt);
		
		GridBagConstraints cons=new GridBagConstraints();
		
		peliRB = new JRadioButton("Peliculas",true);
		peliRB.setActionCommand("peli act");
		peliRB.addActionListener(manejador);
		//peliRB.setIcon(new ImageIcon("src/gui/imagenes/movie_nosel.png"));
		//peliRB.setRolloverIcon(new ImageIcon("src/gui/imagenes/movie_nosel.png"));
		//peliRB.setRolloverEnabled(true);
		//peliRB.setRolloverSelectedIcon(new ImageIcon("src/gui/imagenes/movie.png"));
		//peliRB.setSelectedIcon(new ImageIcon("src/gui/imagenes/movie.png"));
		//peliRB.setFocusPainted(false);
		//peliRB.setHorizontalAlignment(AbstractButton.LEFT);
		/*
		imageIcon icono=new ImageIcon("src/gui/imagenes/logo.jpg");
		Image img = icono.getImage();  
		Image newimg = img.getScaledInstance(230, 200,  java.awt.Image.SCALE_SMOOTH);
		 */
	    cineRB = new JRadioButton("Cines",false);
	    cineRB.setActionCommand("cine act");
	    cineRB.addActionListener(manejador);
	    sesionRB = new JRadioButton("Sesiones",false);
	    sesionRB.setActionCommand("sesion act");
	    sesionRB.addActionListener(manejador);
	    //sesionRB.setFocusPainted(false);
	    //sesionRB.setHorizontalAlignment(AbstractButton.RIGHT);
	    
	    todaProvinciaRB = new JRadioButton("Toda la Provincia",false);
	    todaProvinciaRB.setActionCommand("provincia act");
	    todaProvinciaRB.setSelected(true);
	    todaProvinciaRB.addActionListener(manejador);
	    //todaProvinciaRB.setFocusPainted(true);
	    
	  /*  class RBActionListener implements ActionListener {
	        public void actionPerformed(ActionEvent ex) {
	          String choice = radioSeleccionado();
	          System.out.println("ACTION Candidate Selected: " + choice);
	        }
	      }*/
	    
	    
	    
	    grupo=new ButtonGroup();
	    grupo.add(peliRB);
	    grupo.add(cineRB);
	    grupo.add(sesionRB);
	    grupo.add(todaProvinciaRB);
	    
	    
	    cons=rellenaConstraints(1,1,1,1,GridBagConstraints.WEST);
	    panelSeleccion.add(peliRB,cons);
		
	    cons=rellenaConstraints(1,2,1,1,GridBagConstraints.WEST);
	    panelSeleccion.add(cineRB,cons);
	    
	    cons=rellenaConstraints(1,3,1,1,GridBagConstraints.WEST);
	    panelSeleccion.add(sesionRB,cons);
	    
	    cons=rellenaConstraints(1,4,1,1,GridBagConstraints.WEST);
	    panelSeleccion.add(todaProvinciaRB,cons);
	    
	    JLabel labelProv=new JLabel("Selecciona Provincia");
	    cons=rellenaConstraints(2,1,1,1,GridBagConstraints.EAST);
	    panelSeleccion.add(labelProv,cons);
	    
	  //Creo un Array de Strings con todas las provincias;
        Provincia[] provs=Provincia.values();
        Vector<String> provinciasEnumeradas=new Vector<String>();
        provinciasEnumeradas.add("Todas");
        for (Provincia prov : provs)
			provinciasEnumeradas.add(ProvinciasGDO.getNombre(prov));
	    provinciasCombo= new JComboBox(provinciasEnumeradas);
	    cons=rellenaConstraints(2,2,1,2,GridBagConstraints.EAST);
	    //cons.ipadx=25;
	    panelSeleccion.add(provinciasCombo,cons);
	    
	    panelSeleccion.setBorder(BorderFactory.createEtchedBorder());
	    panelSeleccion.setMinimumSize(new Dimension(120,80));
		
		return panelSeleccion;
		
	}
	
	private String radioSeleccionado(){
		return grupo.getSelection().getActionCommand();
	}

	
	public void informa(int provincia, int paso){
		if (paso==0){
			indicador.setText("Procesando las películas de "+ProvinciasGDO.getNombre(Provincia.values()[provincia]));
			progressBar.setValue(provincia);
		}
		if (paso==1){
			indicador.setText("Procesando los cines de "+ProvinciasGDO.getNombre(Provincia.values()[provincia]));
			progressBar.setValue(totalExtracciones+provincia);
		}
		if (paso==2){
			indicador.setText("Procesando las sesiones de "+ProvinciasGDO.getNombre(Provincia.values()[provincia]));
			progressBar.setValue(totalExtracciones+provincia);
		}
		if (paso==10){
			indicador.setText("Pulse 'Inicio' para empezar...");
			progressBar.setValue(0);
		}
		if (paso==11){
			indicador.setText("Hay una ejecución pendiente");
			progressBar.setValue(0);
		}
	}
	
	public void iniciaProceso(){
		this.butInicio.setEnabled(false);
		this.butParada.setEnabled(true);
		this.butContinua.setEnabled(false);
		configuraBotonesInterfaz(false);
		thread = new Thread(logica); 
		thread.setName("Extractor");
		thread.start();
	}
	
	public void interrumpeProceso(){
		logica.interrumpe();
		//finalizaProceso(false);
	}
	
	/**
	 * Configura los botones radiobuttons y comboBox según el valor
	 * pasado por parámetro. Si recibe un true, lo pone en la configuración de inicio por defecto.
	 * @param habilitaNormal Parámetro que indica de qué manera se deben comportar los botones
	 */
	private void configuraBotonesInterfaz(boolean habilitaNormal){		
		this.peliRB.setEnabled(habilitaNormal);
		this.cineRB.setEnabled(habilitaNormal);
		this.sesionRB.setEnabled(habilitaNormal);
		this.todaProvinciaRB.setEnabled(habilitaNormal);
		this.provinciasCombo.setEnabled(habilitaNormal);
	}
	
	
	public void finalizaProceso(boolean ok){
		if (ok){
			JOptionPane.showMessageDialog(this, "La ejecución ha finalizado correctamente");
			configuraBotonesInterfaz(ok);
			this.butInicio.setEnabled(true);
			this.butParada.setEnabled(false);
			this.butContinua.setEnabled(false);
			this.actParada=false;
			informa(0, 10);
		}
		else{
			JOptionPane.showMessageDialog(this, "La ejecución ha sido interrumpida");
			configuraBotonesInterfaz(!ok);
			this.logica.guardaParamsFuncionamiento(getFuncionamiento(),getProvinciaSeleccionada());
			this.butInicio.setEnabled(true);
			this.butParada.setEnabled(false);
			this.butContinua.setEnabled(true);
			this.actParada=true;
			informa(0,11);
		}
		//configuraBotonesInterfaz(ok);
	}
	
	public void continua(){
		System.out.println("Debería estar continuando");
		//Tengo que saber si son todas las provincias o solo la actual y si son cine, cartelera o ambos.
		boolean todas;
		todas=this.logica.cargaParamsFuncionamiento();
		
		switch (this.funcionamiento) {
		case PELI: 
				if (!todas){//Sólo la prov actual, modo pelicula
				}
				else
					//Todas las provincias, modo película
			break;
		case CINE: 
			if (!todas){//Sólo la prov actual, modo cine
			}
			else
				//Todas las provincias, modo cine
		break;
		case SESION: 
			if (!todas){//Sólo la prov actual, modo sesion
			}
			else
				//Todas las provincias, modo sesion
		break;
		case PROVINCIA: 
			if (!todas){//Sólo la prov actual, modo provincia
			}
			else
				//Todas las provincias, modo provincia
		break;
				
		default:
			break;
		}
		//Hay que llamar a los métodos que utilizan el fichero de texto, los get*Restantes.
		//Estos métodos son de los procesadores(cartelera y cines). Hablar con Sergio porque igual
		//hay que usar un helper(capa de cebolla) para ver cómo funcionan exactamente.
		
	}
	

	
	/**
	 * Pone el modo a película, y si es actualizar o reiniciar valores de la provincia
	 * @param actualizar Cierto si quiere actualizar, falso si quiere borrar los valores y volver a cargarlos 
	 */
	public void setPeli(boolean actualizar) {
		this.actualizar=actualizar;
		this.funcionamiento=ModoFuncionamiento.PELI;
	}
	
	/**
	 * Pone el modo a cine, y si es actualizar o reiniciar valores de la provincia
	 * @param actualizar Cierto si quiere actualizar, falso si quiere borrar los valores y volver a cargarlos 
	 */
	public void setCine(boolean actualizar) {
		this.actualizar=actualizar;
		this.funcionamiento=ModoFuncionamiento.CINE;
	}

	/**
	 * Pone el modo a sesión, y si es actualizar o reiniciar valores de la provincia
	 * @param actualizar Cierto si quiere actualizar, falso si quiere borrar los valores y volver a cargarlos 
	 */
	public void setSesion(boolean actualizar) {
		this.actualizar=actualizar;
		this.funcionamiento=ModoFuncionamiento.SESION;
	}

	/**
	 * Pone el modo a provincia, y si es actualizar o reiniciar valores de la provincia
	 * @param actualizar Cierto si quiere actualizar, falso si quiere borrar los valores y volver a cargarlos 
	 */
	public void setProvincia(boolean actualizar) {
		this.actualizar=actualizar;
		this.funcionamiento=ModoFuncionamiento.PROVINCIA;
	}

	/**
	 * Obtiene el código de la provincia seleccionada en el comboBox de provincias
	 * @return Código de la provincia seleccionada
	 */
	public String getNombreProvinciaSeleccionada(){
		return ProvinciasGDO.getCodigo(Provincia.values()[this.provinciasCombo.getSelectedIndex()]);
	}
	
	/**
	 * Obtiene el código de la provincia seleccionada en el comboBox de provincias
	 * @return Código de la provincia seleccionada
	 */
	public Provincia getProvinciaSeleccionada(){
		int num=this.provinciasCombo.getSelectedIndex()-1;
		if (num<0)
			return null;
		else
			return Provincia.values()[num];
	}
	
	
	
	
	

}
