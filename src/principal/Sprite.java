package principal;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.*;

/**
 * Sprite
 * @author Iván Gil Martín
 */
public class Sprite {
    
    //ATRIBUTOS PARA PINTAR EL SPRITE
    private BufferedImage buffer;
    private Color color = Color.BLACK;

    //ATRIBUTOS QUE DETERMINAN EL ANCHO Y ALTO DEL SPRITE
    private int ancho;
    private int alto;

    //ATRIBUTOS QUE DETERMINAN LA POSICION EN PANTALLA DEL SPRITE
    private int posX;
    private int posY;

    //ATRIBUTOS QUE DETERMINAN LA VELOCIDAD EN PANTALLA DEL SPRITE
    private int velX;
    private int velY;

    //ATRIBUTO PARA GUARDAR LA IMAGEN DEL ASTEROIDE
    private BufferedImage asteroide;

    /**
     * CCONSTRUCTORES DE LA CLASE SPRITE
     */
    public Sprite(Color color, int ancho, int alto, int posX, int posY) {
        this.color = color;
        this.ancho = ancho;
        this.alto = alto;
        this.posX = posX;
        this.posY = posY;
        inicializarBuffer();
    }

    public Sprite(String rutaImagen, int ancho, int alto, int posX, int posY) {
        this.ancho = ancho;
        this.alto = alto;
        this.posX = posX;
        this.posY = posY;
        inicializarBuffer(rutaImagen);
    }

    //GETTERS Y SETTERS DE LA CLASE SPRITE
    public BufferedImage getBuffer() {
        return this.buffer;
    }

    public void setBuffer(BufferedImage buffer) {
        this.buffer = buffer;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getAncho() {
        return this.ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return this.alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public int getPosX() {
        return this.posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }


    public int getVelX() {
        return this.velX;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public int getVelY() {
        return this.velY;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

     /**
     * CREA UNA IMAGEN VACIA DEL COLOR DEL BUFFER
     */
    private void inicializarBuffer() {
        buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics g = buffer.getGraphics();

        g.setColor(color);
        g.fillRect(0, 0, ancho, alto);
        g.dispose();
    }

    /**
     * CREA UNA IMAGEN VACIA CON LA RUTA POR PARAMETROS
     */
    private void inicializarBuffer(String rutaImagen) {
        buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        asteroide = null;

        try{
            asteroide = ImageIO.read(new File(rutaImagen));

            Graphics g = buffer.getGraphics();

            g.drawImage(asteroide.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH), 0, 0, null);
            g.dispose();
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * ESTAMPA LA IMAGEN EN EL BUFFER
     */
	public void estampar(Graphics g) {
        g.drawImage(buffer, getPosX(), getPosY(), null);
    }
    
    /**
     * ESTABLECE LAS VELOCIDADES HORIZONTALES Y VERTICALES ALEATORIAMENTE
     * @param num
     */
    public void setVelocidadesAlternativas(int num){
        //CREAMOS EL ATRIBUTO RANDOM
        Random rd = new Random();

        //COMPROBAMOS QUE LAS VELOCIDADES HORIZONTAL Y VERTICAL NO SON CERO
        while (velX == 0){
            setVelX(rd.nextInt(num));
        }

        while(velY == 0){
            setVelY(rd.nextInt(num));
        }
    }

    /**
     * ESTABLECE LA VELOCIDAD DEL DISPARO
     * @param num
     */
    public void setVelocidadDisparo(int num){
        velY = num;
    }

    /**
     * MOVEMOS Y COMPROBAMOS SI LOS CUADRADOS CHOCAN O NO
     */
	public void moverEnemigos(int width, int height) {
        //MOVER EL CUADRADO
        setPosX(posX + velX);
        setPosY(posY + velY);

        //COMPROBAR SI CHOCA CON LOS BORDES
        //POR LA DERECHA
        if (posX + ancho >= width){
            velX = -Math.abs(velX); //FORZAR A QUE LA VELOCIDAD SEA NEGATIVA
        }

        //POR LA IZQUIERDA
        if (posX < 0){
            velX = Math.abs(velX); //FORZAR A QUE LA VELOCIDAD SEA SIEMPRE POSITIVA
        }

        //POR ARRIBA
        if (posY + alto >= height){
            velY = -Math.abs(velY); //FORZAR A QUE LA VELOCIDAD SEA NEGATIVA
        }

        //POR ABAJO
        if (posY < 0){
            velY = Math.abs(velY); //FORZAR A QUE LA VELOCIDAD SEA SIEMPRE POSITIVA
        }
    }

    public void mover(int width) {
        //MOVER EL CUADRADO
        setPosX(posX + velX);

        //COMPROBAR SI CHOCA CON LOS BORDES
        //POR LA DERECHA
        if (posX + ancho >= width){
            velX = -Math.abs(velX); //FORZAR A QUE LA VELOCIDAD SEA NEGATIVA
        }

        //POR LA IZQUIERDA
        if (posX < 0){
            velX = Math.abs(velX); //FORZAR A QUE LA VELOCIDAD SEA SIEMPRE POSITIVA
        }
    }

    /**
     * MUEVE EL DISPARO HACIA ARRIBA
     * @param height
     */
	public void moverDisparo(int height) {
        setPosY(posY + velY);
	}

    public void moverDisparoJefe(int height){
        setPosY(posY - velY);
    }
    
    /**
     * COMPRUEBA SI DOS SPRITES COLISIONAN HORIZONTAL Y VERTICALMENTE
     * @param segundoSprite
     * @return BOOLEANO QUE INDICARÁ SI HAN COLISIONADO O NO
     */
    public boolean colisiona(Sprite segundoSprite){ 
        boolean colisionaEjeX = false;

        if (posX < segundoSprite.getPosX()){
            colisionaEjeX = posX + ancho >= segundoSprite.posX;
        }
        else{
            colisionaEjeX = segundoSprite.getPosX() + segundoSprite.getAncho() >= posX;
        }

        boolean colisionaEjeY = false;

        if (posY < segundoSprite.getPosY()){
            colisionaEjeY = posY + alto >= segundoSprite.posY;
        }
        else{
            colisionaEjeY = segundoSprite.getPosY() + segundoSprite.getAlto() >= posY;
        }

        return colisionaEjeX && colisionaEjeY;
    }
}
