package pantallas.niveles.nivel1;

import java.awt.*;
import java.util.ArrayList;
import principal.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.SwingUtilities;

/**
 * PANTALLA NIVEL 1
 * @author Iván Gil Martín
 */
public class PantallaNivel1 implements Pantalla {

    //CONSTANTES DE LOS ENEMIGOS
    private static int LADO_ENEMIGOS = 40;
    private static int NUM_ENEMIGOS = 6;
    private static int ORIGEN_ENEMIGOS_X = 10;
    private static int ORIGEN_ENEMIGOS_Y = 10;
    private static int VELOCIDAD_ENEMIGOS = 5;

    //CONSTANTES DE LA NAVE
    private static int ALTO_NAVE = 30;
    private static int ANCHO_NAVE = 30;

    //CONSTANTES DEL DISPARO DE LA NAVE
    private static int ALTO_DISPARO = 40;
    private static int ANCHO_DISPARO = 16;
    private static int VELOCIDAD_DISPARO = -10;

    //FUENTES DE PANTALLAJUEGO
    private Font fuente;

    //ARRAYLIST PARA ALMACENAR ENEMIGOS
    private ArrayList<Sprite> enemigos;

    //ATRIBUTOS DE LA NAVE
    private Sprite nave;
    private Sprite disparo;

    //FONDO DE LA PANTALLA DE JUEGO
    private BufferedImage fondo;
    private Image redimension;

    //REFERENCIA A PANEL DE JUEGO
    private PanelJuego p;

    //NUMERO DE ENEMIGOS EXPLOTADOS
    public static int contadorExplotados;

    //REFERENCIA PARA PASAR DE UNA PANTALLA A OTRA
    private int contador;

    //ATRIBUTO QUE INICIA LA MUSICA
    private Clip clip;

    //ATRIBUTO QUE ESTABLECE EL ARCHIVO DE MUSICA
    private AudioInputStream audioInputStream;

    /**
     * CONSTRUCTOR DE LA CLASE PANTALLAJUEGO
     * @param p
     */
    public PantallaNivel1(PanelJuego p){
        this.p = p;
        fuente = new Font("Arial", Font.BOLD, 40);
        contador = 0;
        contadorExplotados = 0;
    }

    /**
     * INICIALIZA LA PANTALLA
     */
    @Override
    public void inicializarPantalla() {
        //INICIALIZAMOS EL ARRAYLIST DE ENEMIGOS
        enemigos = new ArrayList<>();

        //LOS CREAMOS Y LOS METEMOS EN EL ARRAYLIST
        for (int i = 0; i < NUM_ENEMIGOS; i++) {
            crearEnemigo();
        }        

        //CARGAMOS LA IMAGEN DE FONDO
        cargarFondo();

        //REDIMENSIONAMOS LA IMAGEN DE FONDO DEL PANEL DE JUEGO
        redimensionarFondo();
    }

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
        //CREAMOS EL OBJETO DE TIPO SPRITE
        Sprite nuevo = new Sprite("Imagenes/enemigo.png", LADO_ENEMIGOS, LADO_ENEMIGOS, ORIGEN_ENEMIGOS_X, ORIGEN_ENEMIGOS_Y);
        nuevo.setVelocidadesAlternativas(VELOCIDAD_ENEMIGOS);

        //LO AÑADIMOS AL ARRAYLIST
        enemigos.add(nuevo);
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

        //ESTAMPAMOS LOS SPRITES
        for (int i = 0; i < enemigos.size(); i++) {
            enemigos.get(i).estampar(g);
        }

        //ESTAMPAMOS LA NAVE EN CASO DE DAR CLICK DERECHO
        if (nave != null){
            nave.estampar(g);
        }

        //ESTAMPAMOS EL DISPARO DESPUES DE CREAR LA NAVE
        if (disparo != null){
            if (audioInputStream == null){
                reproducirAudio();
            }
            disparo.estampar(g);
            disparo.moverDisparo(p.getHeight());
            if (disparo.getPosY() + disparo.getAlto() < 0){
                disparo = null;
            }
        }
    }

    private void pintarPuntuacion(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(fuente);
        g.drawString(Integer.toString(contadorExplotados), p.getWidth() - 100, p.getHeight() - 300);
    }

    /**
     * REPRODUCE EL AUDIO SELECCIONADO
     */
    private void reproducirAudio() {
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
     * RELLENA EL FONDO DEL COMPONENTE
     * 
     * @param g
     */
    private void rellenarFondo(Graphics g){
        //REDIMENSIONAMOS Y PINTAMOS EL FONDO
        g.drawImage(redimension, 0, 0, null);
    }

    /**
     * COMPRUEBA LAS COLISIONES DE LOS SPRITES Y LOS ELIMINA
     */
    private void comprobarColisionesEnemigos() {
        //COMPROBAMOS QUE SE CHOCAN, SI SE CHCOCAN, SE BORRAN
        for (int i = 0; i < enemigos.size(); i++) {
            if (disparo != null){
                if (enemigos.get(i).colisiona(disparo)){
                    disparo = null;
                    audioInputStream = null;
                    enemigos.remove(i);
                    contadorExplotados++;
                    contador++;
                }
            }
        }
    }

    /**
     * COMPRUEBA SI LA NAVE COLISIONA CON ALGUNO DE LOS ENEMIGOS
     */
    private void comprobarColisionesNave(){
        //COMPROBAMOS QUE SE CHOCAN, SI SE CHCOCAN, SE BORRAN Y SE PASA A LA PANTALLAFINALPERDIDA
        for (int i = 0; i < enemigos.size(); i++) {
            if (nave != null){
                if (enemigos.get(i).colisiona(nave)){
                    nave = null;
                    if (audioInputStream != null){
                        pararSonido();
                    }
                    PantallaFinalPerdida pantallaFinalPerdida = new PantallaFinalPerdida(p);
                    pantallaFinalPerdida.inicializarPantalla();
                    p.cambiarPantalla(pantallaFinalPerdida);
                }
            }
        }
    }

    /**
     * PARA LA MUSICA CUANDO CAMBIAMOS DE PANTALLA
     */
    private void pararSonido() {
        clip.stop();
        clip.close();
        clip = null;
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

        //COMPROBAMOS LAS COLISIONES DE LOS ENEMIGOS CON EL DISPARO
        comprobarColisionesEnemigos();

        //COMPROBAMOS LAS COLISIONES DE LOS ENEMIGOS CON LA NAVE
        comprobarColisionesNave();

        //COMPROBAMOS SI QUEDAN ENEMIGOS POR EXPLOTAR
        comprobarSiQuedanEnemigos();

        //MOVEMOS LOS ENEMIGOS
        for (int i = 0; i < enemigos.size(); i++) {
            enemigos.get(i).moverEnemigos(p.getWidth(), p.getHeight());
        }
    }

    /**
     * COMPRUEBA SI AUN QUEDAN ENEMIGOS POR EXPLOTAR, SI NO QUEDAN, SE PASA A LA PANTALLAFINALGANADA
     */
     private void comprobarSiQuedanEnemigos() {
        if (contador == NUM_ENEMIGOS){
            //PARAMOS EL SONIDO
            pararSonido();

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
