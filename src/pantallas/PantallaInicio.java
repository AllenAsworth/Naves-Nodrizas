package pantallas;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;
import pantallas.niveles.nivel1.PantallaNivel1;
import principal.*;

/**
 * @author Iván Gil Martín
 */
public class PantallaInicio implements Pantalla {
    //PONEMOS COLOR DE INICIO
    private Color colorInicio;

    //PONEMOS COLOR DE LAS LETRAS
    private Color colorLetra;

    // INICIAMOS LA FUENTE GRANDE
    private Font fuenteGrande;

    //REFERENCIA A PANEL DE JUEGO
    private PanelJuego p;

    //ATRIBUTO QUE INICIA LA MUSICA
    private Clip clip;

    //ATRIBUTO QUE RECOGE EL ARCHIVO PARA LA MUSICA
    private AudioInputStream audioInputStream;

    //ATRIBUTOS DE RESOLUCION
    int anchoPantalla = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
	int altoPantalla = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;

    /**
     * CONSTRUCTOR DE LA CLASE PANTALLAINICIO
     * @param p
     */
    public PantallaInicio(PanelJuego p){
        this.p = p;
        colorLetra = Color.WHITE;
        colorInicio = Color.BLACK;
        fuenteGrande = new Font("Arial", Font.BOLD, 30);
    }

    /**
     * INICIALIZA LA PANTALLA (EN ESTE CASO NO ES NECESARIO INICIALIZAR NADA)
     */
    @Override
    public void inicializarPantalla() {
        if (audioInputStream == null){
            reproducirAudio();
        }
    }

    /**
     * PINTA LA PANTALLA DE INICIO
     */
    @Override
    public void pintarPantalla(Graphics g) {
        //ESTABLECEMOS COLOR PARA FONDO DE LA PANTALLA
        g.setColor(colorInicio);
        g.fillRect(0, 0, p.getWidth(), p.getHeight());

        //ESTABLECEMOS COLOR Y POSICION PARA EL TITULO DEL JUEGO
        g.setColor(colorLetra);
        g.setFont(fuenteGrande);
        g.drawString("NAVES NODRIZAS", (anchoPantalla / 2) - (p.getWidth() / 2), (altoPantalla / 2) - (p.getHeight() / 2));

        //ESTABLECEMOS COLOR Y POSICION PARA EL MENSAJE
        g.setColor(colorLetra);
        g.setFont(fuenteGrande);
        g.drawString("Haga clic para jugar", 100, 200);
    }

    /**
     * REEPRODUCE EL AUDIO SELECCIONADO
     */
    private void reproducirAudio() {
        try{
            audioInputStream = AudioSystem.getAudioInputStream(new File("Sonidos/Inicio.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex){
            ex.printStackTrace();
        }
    }

    /**
     * EJECUTA LAS ACCIONES NECESARIAS PARA LA PANTALLA DE INICIO
     */
    @Override
    public void ejecutarFrame() {
        //INICIAMOS EL HILO
        try{
            Thread.sleep(250);
        }
        catch (InterruptedException ie){
            ie.printStackTrace();
        }

        //VAMOS CAMBIANDO EL COLOR DE LAS LETRAS DE LA PANTALLA DE INICIO
        colorLetra = colorLetra == Color.WHITE ? Color.LIGHT_GRAY : Color.WHITE;
    }
    

    /**
     * CUANDO PULSAMOS EL RATON EN LA PANTALLA, PASA DIRECTAMENTE AL JUEGO
     */
    @Override
    public void pulsarRaton(MouseEvent e) {
        pararCancion();
        //CAMBIAMOS LA PANTALLA
        PantallaNivel1 pantallaJuego = new PantallaNivel1(p);
        pantallaJuego.inicializarPantalla();
        p.cambiarPantalla(pantallaJuego);
        
    }

    /**
     * PARA LA CANCION CUANDO CAMBIAMOS DE PANTALLA
     */
    private void pararCancion() {
        clip.stop();
        clip.close();
        clip = null;
    }

    /**
     * REALIZA ACCIONES CUANDO MUEVE EL RATON (EN ESTE CASO NO ES NECESARIO HACER NADA)
     */
    @Override
    public void moverRaton(MouseEvent e) {}

    /**
     * REALIZA ACCIONES CUANDO REDIMENSIONA LA PANTALLA (EN ESTE CASO NO ES NECESARIO HACER NADA)
     */
    @Override
    public void redimensionarPantalla(ComponentEvent e) {}
}
