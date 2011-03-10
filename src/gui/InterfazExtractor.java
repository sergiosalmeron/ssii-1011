package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
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

import tads.ModoFuncionamiento;
import tads.ProvinciasGDO;
import tads.ProvinciasGDO.Provincia;

public class InterfazExtractor extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JProgressBar progressBar;
	private JLabel indicador,ultActCompleta,fechaUltActCompleta,ultProvActualizada,provActualizada;
	private JButton inicio,parada,borrarBD,reiniciarBD;
	private int totalExtracciones;
	private ListenerGUI manejador;
	private Logica	logica;
	private Thread thread;
	private JComboBox provinciasCombo;
	private JRadioButton todaProvinciaRB,sesionRB,cineRB,peliRB;
	private ButtonGroup grupo;
	private boolean actualizar;
	private ModoFuncionamiento funcionamiento;
	
	
	
	
	
	public void setProvActualizada(JLabel provActualizada) {
		this.provActualizada = provActualizada;
	}

	
	
	/*public JRadioButton getTodaProvinciaRB() {
		return todaProvinciaRB;
	}

	public JRadioButton getSesionRB() {
		return sesionRB;
	}

	public JRadioButton getCineRB() {
		return cineRB;
	}

	public JRadioButton getPeliRB() {
		return peliRB;
	}*/

	
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
		funcionamiento=ModoFuncionamiento.PROVINCIA;
		this.add(construyeInterfaz());
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setMinimumSize(new Dimension(575,275));
		//this.setSize(250, 200);
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
		//panelPrincipal.add(panelImagen);
		//panelPrincipal.add(panelInfo);
		
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
		
		//panelInfo.setLayout(new BorderLayout());
		//Panel de Actualizar y de Administrar
		JTabbedPane panelSeleccion=new JTabbedPane();
		panelSeleccion.addTab("Actualizar",creaPanelActualizar());
		panelSeleccion.addTab("Administrar",creaPanelAdministrar());
		//panelSeleccion.setMinimumSize(new Dimension(150,120));
		//panelInfo.add(panelSeleccion,BorderLayout.NORTH);
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
		
		//
		ultActCompleta=new JLabel("Última actualización:   ");
		cons=rellenaConstraints(1, 1, 1, 1, cons.WEST);
		panelInfo.add(ultActCompleta,cons);
		
		fechaUltActCompleta=new JLabel("el X-Y-Z a las AA:BB");
		cons=rellenaConstraints(2,1,1,1,cons.EAST);
		panelInfo.add(fechaUltActCompleta,cons);
		
		ultProvActualizada=new JLabel("Última provincia actualizada:  ");
		cons=rellenaConstraints(1,2,1,1,cons.WEST);
	    panelInfo.add(ultProvActualizada,cons);
		
	    provActualizada=new JLabel("Prov inexistente");
	    cons=rellenaConstraints(2,2,1,1,cons.EAST);
		panelInfo.add(provActualizada,cons);
		
		
		JPanel panelBotonesAdm=new JPanel(gbl);
		borrarBD=new JButton("Borrar BD");
		cons=rellenaConstraints(1, 1, 1, 1, cons.WEST);
		panelBotonesAdm.add(borrarBD,cons);
		
		reiniciarBD=new JButton("Eliminar entradas");
		cons=rellenaConstraints(2,1,1,1,cons.EAST);
		panelBotonesAdm.add(reiniciarBD,cons);
				
		JTextArea area=new JTextArea("Nada en este panel tiene aún funcionalidad");
		cons=rellenaConstraints(1,2,2,1,cons.CENTER);
		panelBotonesAdm.add(area,cons);
		
		
		panelAdministrar.add(panelInfo);
		panelAdministrar.add(panelBotonesAdm);
		panelAdministrar.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
		return panelAdministrar;
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
	    cons=rellenaConstraints(1,1,2,1,cons.NORTH);
	    //cons.ipadx=80;
	    panelProgreso.add(progressBar,cons);
	    	    
		indicador=new JLabel("Pulse 'Inicio' para empezar...");
		cons=rellenaConstraints(1,2,2,1,cons.CENTER);
	    panelProgreso.add(indicador,cons);
		
		inicio=new JButton();
		inicio.setText("Inicio");
		inicio.addActionListener(manejador);
		cons=rellenaConstraints(1,3,1,1,cons.CENTER);
	    panelProgreso.add(inicio,cons);		

		parada=new JButton();
		parada.setText("Parada");
		parada.addActionListener(manejador);
		parada.setEnabled(false);
		cons=rellenaConstraints(2,3,1,1,cons.CENTER);
	    panelProgreso.add(parada,cons);
		
	    
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
	    sesionRB = new JRadioButton("Sesiones",false);
	    sesionRB.setActionCommand("sesion act");
	    //sesionRB.setFocusPainted(false);
	    //sesionRB.setHorizontalAlignment(AbstractButton.RIGHT);
	    
	    todaProvinciaRB = new JRadioButton("Toda la Provincia",false);
	    todaProvinciaRB.setActionCommand("provincia act");
	    todaProvinciaRB.setSelected(true);
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
	    
	    
	    cons=rellenaConstraints(1,1,1,1,cons.WEST);
	    panelSeleccion.add(peliRB,cons);
		
	    cons=rellenaConstraints(1,2,1,1,cons.WEST);
	    panelSeleccion.add(cineRB,cons);
	    
	    cons=rellenaConstraints(1,3,1,1,cons.WEST);
	    panelSeleccion.add(sesionRB,cons);
	    
	    cons=rellenaConstraints(1,4,1,1,cons.WEST);
	    panelSeleccion.add(todaProvinciaRB,cons);
	    
	    JLabel labelProv=new JLabel("Selecciona Provincia");
	    cons=rellenaConstraints(2,1,1,1,cons.EAST);
	    panelSeleccion.add(labelProv,cons);
	    
	  //Creo un Array de Strings con todas las provincias;
        Provincia[] provs=Provincia.values();
        Vector<String> provinciasEnumeradas=new Vector<String>();
        provinciasEnumeradas.add("Todas");
        for (Provincia prov : provs)
			provinciasEnumeradas.add(ProvinciasGDO.getNombre(prov));
	    provinciasCombo= new JComboBox(provinciasEnumeradas);
	    cons=rellenaConstraints(2,2,1,2,cons.EAST);
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
			indicador.setText("Procesando los cines de "+Provincia.values()[provincia]);
			progressBar.setValue(totalExtracciones+provincia);
		}
		if (paso==2){
			indicador.setText("Procesando las sesiones de "+Provincia.values()[provincia]);
			progressBar.setValue(totalExtracciones+provincia);
		}
	}
	
	public void iniciaProceso(){
		this.inicio.setEnabled(false);
		this.parada.setEnabled(true);
		thread.setName("Extractor");
		thread.start();
	}
	
	public void interrumpeProceso(){
		logica.interrumpe();
	}
	
	public void finalizaProceso(boolean ok){
		if (ok)
			JOptionPane.showMessageDialog(this, "La ejecución ha finalizado correctamente");
		else
			JOptionPane.showMessageDialog(this, "La ejecución ha sido interrumpida");

		this.inicio.setEnabled(true);
		this.parada.setEnabled(false);
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

	public String getProvinciaSeleccionada(){
		return ProvinciasGDO.getCodigo(Provincia.values()[this.provinciasCombo.getSelectedIndex()]);
	}
	

}
