package principal;

import java.awt.EventQueue;

/**
 * Ejercicio 
 * @author iván Gil Martín
 */
public class Principal {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
					ventanaPrincipal.inicializar();
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	
	}
}
