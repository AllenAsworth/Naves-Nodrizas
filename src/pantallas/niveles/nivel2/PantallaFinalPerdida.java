package pantallas.niveles.nivel2;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.SwingUtilities;
import pantallas.niveles.nivel1.PantallaNivel1;
import principal.*;
import java.io.*;

/**
 * @author Iván Gil Martín
 */
public class PantallaFinalPerdida implements Pantalla {

    //FONDO DE PANTALLA FINAL PERDIDA
    private BufferedImage fondo;

    //FUENTE DE LA PANTALLA FINAL PERDIDA
    private Font fuentePierde;

    //REFERENCIA A PANEL DE JUEGO NECESARIA PARA CAMBIAR DE PANTALLA
    private PanelJuego p;

    //ATRIBUTO QUE REDIMENSIONA LA IMAGEN DEL FONDO DE PANTALLA FINAL PERDIDA
    private Image redimension;

    //COLOR DE LA LETRA DE LA PANTALLA FINAL PERDIDA
    private Color colorLetra;

    //ATRIBUTO QUE INICIA LA MUSICA
    private Clip clip;

    //ATRIBUTO QUE ESTABLECE EL ARCHIVO DE LA MUSICA
    private AudioInputStream audioInputStream;

    /**
     * CONSTRUCTOR DE LA CLASE PANTALLAFINALPERDIDA
     * @param p
     */
    public PantallaFinalPerdida(PanelJuego p){
        this.p = p;
        fuentePierde = new Font("Arial", Font.BOLD, 40);
        colorLetra = Color.WHITE;
    }

    /**
     * INICIALIZA LA PANTALLA
     */
    @Override
    public void inicializarPantalla() {
        //ESTABLECEMOS EL FONDO
        fondo = null;
        try{
            fondo = ImageIO.read(new File("Imagenes/pantalla_perdido.jpg"));
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
     * REPRODUCE LA CANCION SELECCIONADA
     */
    private void reproducirAudio() {
        try{
            audioInputStream = AudioSystem.getAudioInputStream(new File("Sonidos/derrota.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex){
            ex.printStackTrace();
        }
    }

    /**
     * REDIMENSIONA EL FONDO
     */
    private void redimensionarFondo() {
        redimension = fondo.getScaledInstance(p.getWidth(), p.getHeight(), Image.SCALE_SMOOTH);
    }

    /**
     * PINTA LA PANTALLA
     */
    @Override
    public void pintarPantalla(Graphics g) {
        //PINTAMOS EL FONDO
        rellenarFondo(g);

        //ESTABLECEMOS EL COLOR Y LA FUENTE DE LA LETRA Y LA PINTAMOS
        g.setColor(colorLetra);
        g.setFont(fuentePierde);
        g.drawString("HAS PERDIDO",(p.getWidth() / 2) - 100, 150);
    }

    /**
     * RELLENA EL FONDO DE LA PANTALLA FINAL PERDIDA
     * @param g
     */
    private void rellenarFondo(Graphics g) {
        g.drawImage(redimension, 0, 0, null);
    }

    /**
     * EJECUTA LAS ACCIONES NECESARIAS PARA ESTA PANTALLA
     */
    @Override
    public void ejecutarFrame() {
        //DORMIMOS DURANTE UNOS MS EL HILO
        try{
            Thread.sleep(250);
        }
        catch (InterruptedException ie){
            ie.printStackTrace();
        }

        //CAMBIAMOS EL COLOR DE LA LETRA CADA ESOS MS QUE PASAN
        colorLetra = colorLetra == Color.WHITE ? Color.LIGHT_GRAY : Color.WHITE;
    }
    

    /**
     * REALIZA LAS ACCIONES NECESARIAS AL PULSAR EL RATON
     */
    @Override
    public void pulsarRaton(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)){
            pararCancion();
            PantallaNivel1 pantallaNivel1 = new PantallaNivel1(p);
            pantallaNivel1.inicializarPantalla();
            p.cambiarPantalla(pantallaNivel1);
        }
        else{
            System.exit(0);
        }
    }

    /**
     * PARA LA CANCION CUANDO CAMBIA DE PANTALLA
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
     * REDIMENSIONA EL FONDO DE LA PANTALLA FINAL PERDIDA
     */
    @Override
    public void redimensionarPantalla(ComponentEvent e) {
        redimensionarFondo();
    }
}
