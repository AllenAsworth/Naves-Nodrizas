package principal;

import java.awt.event.*;
import javax.swing.JPanel;
import pantallas.PantallaInicio;
import java.awt.*;

/**
 * @author Iván Gil Martín
 */
public class PanelJuego extends JPanel implements Runnable, MouseMotionListener, MouseListener, ComponentListener{
    
    //CONSTANTE NECESARIA CUANDO EXTIENDES DE JPANEL
    private static final long serialVersionUID = 1L;

    //INSTANCIA A PANTALLA PARA CARGAR LAS DIFERENTES PANTALLAS DEL JUEGO
    private Pantalla pantallaActual;

    /**
     * CONSTRUCTOR
     */
    public PanelJuego(){
        //INICIALIZAMOS LA PANTALLA DE INICIO
        pantallaActual = new PantallaInicio(this);

        //INICIAMOS EL LISTENER DEL PANEL DEL JUEGO
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addComponentListener(this);

        //INICIAMOS EL HILO
        new Thread(this).start();
    }

    /**
     * PINTA EL COMPONENTE (ESTE METODO SE LLAMA AUTOMATICAMENTE)
     */
    @Override
    public void paintComponent(Graphics g) {
        pantallaActual.pintarPantalla(g);
    }

    /**
     * CAMBIAMOS LA PANTALLA DEL JUEGO DEPENDIENDO DE LO QUE SUCEDA
     * @param siguientePantalla
     */
    public void cambiarPantalla(Pantalla siguientePantalla) {
        pantallaActual.inicializarPantalla();
        pantallaActual = siguientePantalla;
	}
    
    // LISTENERS DE LA CLASE PANELJUEGO
    @Override
    public void mouseClicked(MouseEvent e) {
        pantallaActual.pulsarRaton(e);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    @Override
    public void mouseDragged(MouseEvent e) {}
    
    @Override
    public void mouseMoved(MouseEvent e) {
        pantallaActual.moverRaton(e);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        pantallaActual.redimensionarPantalla(e);
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}

    /**
     * HILO QUE SE EJECUTA EN EL CONSTRUCTOR
     */
    @Override
    public void run() {
        pantallaActual.inicializarPantalla();

        while(true){
            pantallaActual.ejecutarFrame();

            //REPINTO EL CUADRADO Y LO SINCRONIZO
            repaint();
            Toolkit.getDefaultToolkit().sync();
        }
    }
}
