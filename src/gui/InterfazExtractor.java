package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
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
import javax.swing.JScrollPane;

import tads.ProvinciasGDO;
import tads.ProvinciasGDO.Provincia;

public class InterfazExtractor extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JProgressBar progressBar;
	private JLabel indicador;
	private JButton inicio;
	private JButton parada;
	private int totalExtracciones;
	private ListenerGUI manejador;
	private Logica	logica;
	private Thread thread;
	
	public InterfazExtractor(){
		logica = new Logica(this); 
		thread = new Thread(logica); 
		totalExtracciones=Provincia.values().length;
		manejador=new ListenerGUI(this);
		this.add(construyeInterfaz());
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		//this.setSize(250, 200);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("MoodVie - Extractor de información");
	}
	
	private JPanel construyeInterfaz(){
		/*JPanel panel=new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		
		panel.add(Box.createGlue());
		progressBar = new JProgressBar(0, 2*totalExtracciones);
		progressBar.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(progressBar, Component.CENTER_ALIGNMENT);
		indicador=new JLabel("Pulse 'Inicio' para empezar...");
		panel.add(indicador, Component.CENTER_ALIGNMENT);//  .add(indicador);
		
		
		panel.add(Box.createGlue());
		inicio=new JButton();
		inicio.setText("Inicio");
		inicio.addActionListener(manejador);
		inicio.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(inicio);//, Component.CENTER_ALIGNMENT);		
		
		
		panel.add(Box.createGlue());
		parada=new JButton();
		parada.setText("Parada");
		parada.setAlignmentX(CENTER_ALIGNMENT);
		parada.addActionListener(manejador);
		parada.setEnabled(false);
		panel.add(parada, Component.CENTER_ALIGNMENT);
		
		
		
		
		return panel;*/
		
		
		//Creo el Panel Principal
		JPanel panelPrincipal=new JPanel(new BorderLayout());
		
		
		//Creo el panel de la imagen del logo
		JPanel panelImagen=new JPanel();
		JLabel etiqueta= new JLabel();
		etiqueta.setIcon(new ImageIcon("src/gui/imagenes/logo.jpg"));
		panelImagen.add(etiqueta);
		
		
		//Creo el panel con los contenidos
		JPanel panelInfo=new JPanel();
		//panelInfo.setLayout(new BoxLayout(panelInfo,BoxLayout.Y_AXIS));
		panelInfo.setLayout(new BorderLayout());
				
		//Panel de radiobuttons y comboBox
		JPanel panelSeleccion=creaPanelRadioYCombo();
	    //Barra de progreso, botones de iniciar y parar
	    JPanel panelProgreso = creaPanelProgresoYBotones();
	    
	    
	    //Añado los paneles, combos y botones
	    panelInfo.add(panelSeleccion,BorderLayout.WEST);
	    panelInfo.add(panelProgreso,BorderLayout.SOUTH);
		//panelInfo.add(panelSeleccion);
		//panelInfo.add(panelProgreso);
	    
	    	
		//Añado el panel de la imagen a la izquierda
		panelPrincipal.add(panelImagen,BorderLayout.WEST);
		panelPrincipal.add(panelInfo,BorderLayout.CENTER);
		
		panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 15));
		return panelPrincipal;
	}

	/**
	 * @return
	 */
	private JPanel creaPanelProgresoYBotones() {
		JPanel panelProgreso=new JPanel(new BorderLayout());
	    progressBar = new JProgressBar(0, 2*totalExtracciones);
		progressBar.setAlignmentX(CENTER_ALIGNMENT);
		panelProgreso.add(progressBar,BorderLayout.NORTH);
		indicador=new JLabel("Pulse 'Inicio' para empezar...");
		panelProgreso.add(indicador,BorderLayout.CENTER);
		
		
		JPanel panelBotones=new JPanel(new FlowLayout());
	    inicio=new JButton();
		inicio.setText("Inicio");
		inicio.addActionListener(manejador);
		inicio.setAlignmentX(CENTER_ALIGNMENT);
		panelBotones.add(inicio);		
		
		

		parada=new JButton();
		parada.setText("Parada");
		parada.setAlignmentX(CENTER_ALIGNMENT);
		parada.addActionListener(manejador);
		parada.setEnabled(false);
		
		panelBotones.add(parada);

		
		panelProgreso.add(panelBotones,BorderLayout.SOUTH);
		return panelProgreso;
	}
	
	
	private JPanel creaPanelRadioYCombo(){
		//Panel de radiobuttons y comboBox
		JPanel panelSeleccion=new JPanel(new FlowLayout());
		
		//Creo los radioButton
		JRadioButton peliRB = new JRadioButton();
		peliRB.setText("Películas");
	    peliRB.setSelected(true);
	    JRadioButton cineRB = new JRadioButton();
	    cineRB.setText("Cines");
	    JRadioButton sesionRB = new JRadioButton();
	    sesionRB.setText("Sesiones");
	    JRadioButton todaProvinciaRB = new JRadioButton();
	    todaProvinciaRB.setText("Toda la Provincia");
	    
	    //Agrupo los botones
	    ButtonGroup grupo=new ButtonGroup();
	    grupo.add(peliRB);
	    grupo.add(cineRB);
	    grupo.add(sesionRB);
	    grupo.add(todaProvinciaRB);
	    
	    //Panel donde van los radiobutton
	    JPanel radioPanel = new JPanel();
	    radioPanel.setLayout(new BoxLayout(radioPanel,BoxLayout.Y_AXIS));
        radioPanel.add(peliRB);
        radioPanel.add(cineRB);
        radioPanel.add(sesionRB);
        radioPanel.add(todaProvinciaRB);

	    //Creo un Array de Strings con todas las provincias;
        Provincia[] provs=Provincia.values();
        Vector<String> provinciasEnumeradas=new Vector<String>();
        provinciasEnumeradas.add("Todas");
        for (Provincia prov : provs)
			provinciasEnumeradas.add(ProvinciasGDO.getNombre(prov));
	    JComboBox  provinciasCombo= new JComboBox(provinciasEnumeradas);
	    
	    //Añado el panel de radioButton y el combo de las provincias
	    panelSeleccion.add(radioPanel);
	    panelSeleccion.add(provinciasCombo);
	    
	    return panelSeleccion;
	}
	
	
	
	
	public void Informa(int provincia, int paso){
		if (paso==0){
			indicador.setText("Procesando las películas de "+ProvinciasGDO.getNombre(Provincia.values()[provincia]));
			progressBar.setValue(provincia);
		}
		if (paso==1){
			indicador.setText("Procesando los cines de "+Provincia.values()[provincia]);
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
	
	

}
