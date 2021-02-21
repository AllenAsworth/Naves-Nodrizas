package principal;

import java.awt.Graphics;
import java.awt.event.*;

/**
 * @author Iván Gil Martín
 */
public interface Pantalla {
    
    /**
     * METODOS FUNDAMENTALES DE UNA PANTALLA
     */
    public void inicializarPantalla();
    public void pintarPantalla(Graphics g);
    public void ejecutarFrame();
    public void pulsarRaton(MouseEvent e);
    public void moverRaton(MouseEvent e);
    public void redimensionarPantalla(ComponentEvent e);
}
