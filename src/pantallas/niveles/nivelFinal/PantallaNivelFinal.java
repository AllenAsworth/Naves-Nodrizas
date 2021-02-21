package pantallas.niveles.nivelFinal;

import java.awt.*;
import principal.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.SwingUtilities;

/**
 * PANTALLA NIVEL FINAL
 * @author Iván Gil Martín
 */
public class PantallaNivelFinal implements Pantalla {

    //CONSTANTES DEL ENEMIGO
    private static int LADO_ENEMIGO_HORIZONTAL = 300;
    private static int LADO_ENEMIGO_VERTICAL = 100;
    private static int ORIGEN_ENEMIGO_X = 10;
    private static int ORIGEN_ENEMIGO_Y = 10;
    private static int VELOCIDAD_ENEMIGO = 15;
    private static int VIDA_ENEMIGO = 10;

    //CONSTANTES DE LA NAVE
    private static int ALTO_NAVE = 30;
    private static int ANCHO_NAVE = 30;

    //CONSTANTES DEL DISPARO DE LA NAVE
    private static int ALTO_DISPARO = 40;
    private static int ANCHO_DISPARO = 16;
    private static int VELOCIDAD_DISPARO = -10;

    //FUENTES DE PANTALLAJUEGO
    private Font fuente;

    //ATRIBUTOS DE LA NAVE
    private Sprite nave;
    private Sprite disparo;

    //FONDO DE LA PANTALLA DE JUEGO
    private BufferedImage fondo;
    private Image redimension;

    //REFERENCIA A PANEL DE JUEGO
    private PanelJuego p;

    //NUMERO DE ENEMIGOS EXPLOTADOS
    private int contadorExplotados;

    //REFERENCIA PARA PASAR DE UNA PANTALLA A OTRA
    private int contador;

    //ENEMIGO FINAL
    private Sprite enemigoFinal;

    //ATRIBUTO QUE INICIA LA MUSICA DE FONDO
    private Clip clipFondo;

    //ATRIBUTO QUE INICIA LOS SONIDOS DE DISPARO
    private Clip clip;

    //ATRIBUTO QUE ESTABLECE EL ARCHIVO DE MUSICA DE FONDO
    private AudioInputStream audioInputStreamFondo;

    //ATRIBUTO QUE ESTABLECE EL ARCHIVO DE LOS SONIDOS DE DISPARO
    private AudioInputStream audioInputStream;

    //ATRIBUTO QUE HACE QUE NO SE REPITA LA CANCION
    private boolean puesta;

    /**
     * CONSTRUCTOR DE LA CLASE PANTALLAJUEGO
     * @param p
     */
    public PantallaNivelFinal(PanelJuego p){
        this.p = p;
        fuente = new Font("Arial", Font.BOLD, 40);
        contador = 0;
        puesta = true;
        contadorExplotados = contadorExplotados + pantallas.niveles.nivel3.PantallaNivel3.contadorExplotados;
    }

    /**
     * INICIALIZA LA PANTALLA
     */
    @Override
    public void inicializarPantalla() {
        crearEnemigo();      

        //CARGAMOS LA IMAGEN DE FONDO
        cargarFondo();

        //REDIMENSIONAMOS LA IMAGEN DE FONDO DEL PANEL DE JUEGO
        redimensionarFondo();

        //SI ESTA PUESTA, SE REPRODUCE EL SONIDO DE FONDO
        if (puesta){
            reproducirAudio();
        }
    }

    /**
     * CARGAMOS EL FONDO
     */
    private void cargarFondo(){
        fondo = null;
        try {
            fondo = ImageIO.read(new File("imagenes/galaxia.jpg"));
        } 
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * CREA EL CUADRADO EN EL PANEL DEL JUEGO
     */
    public void crearEnemigo() {
        //INICIALIZAMOS EL SPRITE ENEMIGO
        enemigoFinal = new Sprite("Imagenes/enemigo.png", LADO_ENEMIGO_HORIZONTAL, LADO_ENEMIGO_VERTICAL, ORIGEN_ENEMIGO_X, ORIGEN_ENEMIGO_Y);
        enemigoFinal.setVelocidadesAlternativas(VELOCIDAD_ENEMIGO);
    }

    /**
     * PINTA LA PANTALLA DE JUEGO
     */
    @Override
    public void pintarPantalla(Graphics g) {
        // RELLENAMOS EL FONDO
        rellenarFondo(g);

        //PINTAMOS LA PUNTUACION
        pintarPuntuacion(g);

        //ESTAMPAMOS EL ENEMIGO
        enemigoFinal.estampar(g);

        //ESTAMPAMOS LA NAVE EN CASO DE DAR CLICK DERECHO
        if (nave != null){
            nave.estampar(g);
        }

        //ESTAMPAMOS EL DISPARO DESPUES DE CREAR LA NAVE
        if (disparo != null){
            if (audioInputStream == null){
                reproducirAudioDisparo();
            }
            disparo.estampar(g);
            disparo.moverDisparo(p.getHeight());
            if (disparo.getPosY() + disparo.getAlto() < 0){
                disparo = null;
            }
        }
    }

    /**
     * PINTAMOS LA PUNTUACION
     * @param g
     */
    private void pintarPuntuacion(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(fuente);
        g.drawString(Integer.toString(contadorExplotados), p.getWidth() - 100, p.getHeight() - 300);
    }

    /**
     * REPRODUCE EL AUDIO DE DISPARO
     */
    private void reproducirAudioDisparo() {
        try{
            audioInputStream = AudioSystem.getAudioInputStream(new File("Sonidos/disparo.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex){
            ex.printStackTrace();
        }
    }

    /**
     * REPRODUCE EL AUDIO DE FONDO
     */
    private void reproducirAudio() {
        try{
            audioInputStreamFondo = AudioSystem.getAudioInputStream(new File("Sonidos/enemigo_final.wav").getAbsoluteFile());
            clipFondo = AudioSystem.getClip();
            clipFondo.open(audioInputStreamFondo);
            clipFondo.start();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex){
            ex.printStackTrace();
        }
    }

    /**
     * RELLENA EL FONDO DEL COMPONENTE
     * @param g
     */
    private void rellenarFondo(Graphics g){
        //REDIMENSIONAMOS Y PINTAMOS EL FONDO
        g.drawImage(redimension, 0, 0, null);
    }

    /**
     * COMPRUEBA LAS COLISIONES DE LOS SPRITES Y LOS ELIMINA
     */
    private void comprobarColisionEnemigo() {
        //COMPROBAMOS QUE SE CHOCAN, SI SE CHCOCAN, SE BORRAN
        if (disparo != null){
            if (enemigoFinal.colisiona(disparo)){
                disparo = null;
                audioInputStream = null;
                contadorExplotados++;
                contador++;
            }
        }
    }

    /**
     * COMPRUEBA SI LA NAVE COLISIONA CON ALGUNO DE LOS ENEMIGOS
     */
    private void comprobarColisionNave(){
        //COMPROBAMOS QUE SE CHOCAN, SI SE CHCOCAN, SE BORRAN Y SE PASA A LA PANTALLAFINALPERDIDA
        if (nave != null){
            if (enemigoFinal.colisiona(nave)){
                nave = null;
                audioInputStreamFondo = null;
                pararSonidoFondo();
                
                PantallaFinalPerdida pantallaFinalPerdida = new PantallaFinalPerdida(p);
                pantallaFinalPerdida.inicializarPantalla();
                p.cambiarPantalla(pantallaFinalPerdida);
            }
        }
    }

    /**
     * PARA LOS SONIDOS DE DISPAROS CUANDO CAMBIAMOS DE PANTALLA
     */
    private void pararSonido() {
        clip.stop();
        clip.close();
        clip = null;
    }

    /**
     * PARA LA MUSICA DE FONDO CUANDO CAMBIAMOS DE PANTALLA
     */
    private void pararSonidoFondo() {
        clipFondo.stop();
        clipFondo.close();
        puesta = false;
        clipFondo = null;
    }

    /**
     * EJECUTA LAS ACCIONES NECESARIAS PARA LA PANTALLA DE JUEGO
     */
    @Override
    public void ejecutarFrame() {
        //DORMIMOS POR UN TIEMPO EL HILO
        try{
            Thread.sleep(25);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        //COMPROBAMOS LA COLISION DEL ENEMIGO CON EL DISPARO
        comprobarColisionEnemigo();

        //COMPROBAMOS LA COLISION DE LA NAVE CON EL ENEMIGO
        comprobarColisionNave();

        //COMPROBAMOS SI EL ENEMIGO SIGUE VIVO O NO
        comprobarSiEnemigoSigueVivo();

        //MOVEMOS LOS ENEMIGOS
        enemigoFinal.moverEnemigos(p.getWidth(), p.getHeight());
    }

    /**
     * COMPRUEBA SI AUN SIGUE VIVO EL ENEMIGO, SI NO, SE PASA A LA PANTALLAFINALGANADA
     */
     private void comprobarSiEnemigoSigueVivo() {
        if (contador == VIDA_ENEMIGO){
            //PARAMOS EL SONIDO
            pararSonido();
            pararSonidoFondo();

            //PASAMOS A LA PANTALLA DE VICTORIA
            PantallaFinalGanada pantallaFinalGanada = new PantallaFinalGanada(p);
            pantallaFinalGanada.inicializarPantalla();
            p.cambiarPantalla(pantallaFinalGanada);
        }
    }

    /**
     * REALIZA LAS ACCIONES NECESARIAS AL PULSAR EL RATON
     */
    @Override
    public void pulsarRaton(MouseEvent e) {
        //SI NO HAY NAVE CREADA Y LE DAMOS CLIC DERECHO AL RATON, CREAMOS LA NAVE
        if (SwingUtilities.isRightMouseButton(e) && nave == null){
            nave = new Sprite("Imagenes/nave.png", ALTO_NAVE, ANCHO_NAVE, e.getX() - ANCHO_NAVE / 2, e.getY() - ALTO_NAVE / 2);
        }
        else{
            //SI YA ESTA CREADA LA NAVE Y LE DAMOS CLIC IZQUIERDO AL RATON, CREAMOS EL DISPARO DE LA NAVE
            if (SwingUtilities.isLeftMouseButton(e) && nave != null){
                disparo = new Sprite(Color.GREEN, ANCHO_DISPARO, ALTO_DISPARO, e.getX() - ANCHO_NAVE / 2, e.getY() - ALTO_NAVE / 2);
                disparo.setVelocidadDisparo(VELOCIDAD_DISPARO);
            }
        }
    }

    /**
     * REALIZA LAS ACCIONES NECESARIAS AL MOVER EL RATON
     */
    @Override
    public void moverRaton(MouseEvent e) {
        //SI YA ESTA CREADA LA NAVE, SE MOVERÁ DONDE ESTE EL RATÓN POSICIONADO
        if (nave != null){
            nave.setPosX(e.getX() - nave.getAncho() / 2);
            nave.setPosY(e.getY() - nave.getAlto() / 2);
        }
    }

    /**
     * REALIZA LAS ACCIONES NECESARIAS PARA REDIMENSIONAR LA PANTALLA DE JUEGO (EN ESTE CASO NO HACE FALTA HACER NADA)
     */
    @Override
    public void redimensionarPantalla(ComponentEvent e) {}

    /**
     * REDIMENSIONA EL FONDO DE LA PANTALLA DEL JUEGO
     */
    private void redimensionarFondo(){
        redimension = fondo.getScaledInstance(p.getWidth(), p.getHeight(), Image.SCALE_SMOOTH);
    }
}
