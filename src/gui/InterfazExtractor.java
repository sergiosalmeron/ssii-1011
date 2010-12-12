package gui;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


import com.sun.corba.se.spi.orbutil.fsm.Action;

import Prueba.ProvinciasGDO;
import Prueba.ProvinciasGDO.Provincia;

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
		this.setSize(250, 200);
		this.setVisible(true);
	}
	
	private JPanel construyeInterfaz(){
		JPanel panel=new JPanel();
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
		
		
		
		
		return panel;
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
			JOptionPane.showMessageDialog(this, "La ejecución a finalizado correctamente");
		else
			JOptionPane.showMessageDialog(this, "La ejecución ha sido interrumpida");

		this.inicio.setEnabled(true);
		this.parada.setEnabled(false);
	}
	
	

}
