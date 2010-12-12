package gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListenerGUI implements ActionListener{

	private InterfazExtractor interfaz;
	
	public ListenerGUI(InterfazExtractor interfaz){
		this.interfaz=interfaz;
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println(e.getActionCommand());
		if (e.getActionCommand().equalsIgnoreCase("Inicio")){
			interfaz.iniciaProceso();
		}
		if (e.getActionCommand().equalsIgnoreCase("Parada")){
			interfaz.interrumpeProceso();
		}
		
	}

}
