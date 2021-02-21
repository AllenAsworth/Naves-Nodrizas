package pantallas.niveles.nivel2;

import java.awt.*;
import java.awt.event.*;
import principal.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import pantallas.niveles.nivel3.PantallaNivel3;
import java.io.*;

/**
 * @author Iván Gil Martín
 */
public class PantallaFinalGanada implements Pantalla {

    //FONDO DE PANTALLA FINAL GANADA
    private BufferedImage fondo;

    //FUENTE DE PANTALLA FINAL GANADA
    private Font fuenteGanada;

    //COLOR DE LETRA 
    private Color colorLetra;

    //ATRIBUTO QUE REDIMENSIONA EL FONDO DE PANTALLA FINAL GANADA
    private Image redimension;

    //REFERENCIA A PANELJUEGO NECESARIA PARA CAMBIAR DE PANTALLA
    private PanelJuego p;

    //ATRIBUTO QUE INICIA LA MUSICA
    private Clip clip;

    //ATRIBUTO QUE ESTABLECE EL ARCHIVO DE LA MUSICA
    private AudioInputStream audioInputStream;

    /**
     * CONSTRUCTOR DE LA CLASE PANTALLAFINALGANADA
     * @param p
     */
    public PantallaFinalGanada(PanelJuego p){
        this.p = p;
        fuenteGanada = new Font("Arial", Font.BOLD, 40);
        colorLetra = Color.BLACK;
    }

    /**
     * INICIALIZA LA PANTALLA
     */
    @Override
    public void inicializarPantalla() {
        //CARGAMOS LA IMAGEN DEL FONDO
        fondo = null;
        try{
            fondo = ImageIO.read(new File("Imagenes/pantalla_ganada.jpg"));
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }

        //LO REDIMENSIONAMOS
        redimensionarFondo();

        //REPRODUCIMOS EL AUDIO
        if (audioInputStream == null){
            reproducirAudio();
        }
    }

    /**
     * REPRODUCE LA MUSICA SELECCIONADA
     */
    private void reproducirAudio() {
        try{
            audioInputStream = AudioSystem.getAudioInputStream(new File("Sonidos/victoria.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex){
            ex.printStackTrace();
        }
    }

    /**
     * REDIMENSIONA EL FONDO DE PANTALLAFINALGANADA
     */
    private void redimensionarFondo() {
        redimension = fondo.getScaledInstance(p.getWidth(), p.getHeight(), Image.SCALE_SMOOTH);
    }

    /**
     * PINTA LA PANTALLA
     */
    @Override
    public void pintarPantalla(Graphics g) {
        //RELLENAMOS EL FONDO
        rellenarFondo(g);

        //ESTABLECEMOS EL COLOR Y FUENTE DE LETRA Y LA PINTAMOS
        g.setColor(colorLetra);
        g.setFont(fuenteGanada);
        g.drawString("HAS GANADO",(p.getWidth() / 2) - 100,150);

        g.setColor(colorLetra);
        g.setFont(fuenteGanada);
        g.drawString("Haga clic para el tercer nivel", 0, 300);
    }

    /**
     * PINTA EL FONDO DE PANTALLAFINALGANADA
     * @param g
     */
    private void rellenarFondo(Graphics g) {
        g.drawImage(redimension, 0, 0, null);
    }

    /**
     * EJECUTA LAS ACCIONES NECESARIAS PARA QUE FUNCIONE
     */
    @Override
    public void ejecutarFrame() {
        //DORMIMOS UNOS MS EL HILO
        try{
            Thread.sleep(250);
        }
        catch (InterruptedException ie){
            ie.printStackTrace();
        }

        //CAMBIAMOS EL COLOR DE LETRA CADA ESOS MS DORMIDOS
        colorLetra = colorLetra == Color.BLACK ? Color.LIGHT_GRAY : Color.BLACK;
    }

    /**
     * REALIZA LAS ACCIONES NECESARIAS AL PULSAR EL RATON
     */
    @Override
    public void pulsarRaton(MouseEvent e) {
        pararCancion();
        PantallaNivel3 pantallaNivel3 = new PantallaNivel3(p);
        pantallaNivel3.inicializarPantalla();
        p.cambiarPantalla(pantallaNivel3);
    }

    /**
     * PARA LA MUSICA CUANDO CAMBIA DE PANTALLA
     */
    private void pararCancion() {
        clip.stop();
        clip.close();
        clip = null;
    }

    /**
     * REALIZA LAS ACCIONES NECESARIAS AL MOVER EL RATON (EN ESTE CASO NO ES NECESARIO HACER NADA)
     */
    @Override
    public void moverRaton(MouseEvent e) {}

    /**
     * REDIMENSIONA EL FONDO DE PANTALLAFINALGANADA
     */
    @Override
    public void redimensionarPantalla(ComponentEvent e) {
        redimensionarFondo();
    }
}
