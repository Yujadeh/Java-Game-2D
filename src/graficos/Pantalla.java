/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graficos;

/**
 *
 * @author Jose
 */
public class Pantalla {
    private final int ancho;
    private final int alto;
    
    
    public final int [] pixeles;
    
    //temporal
    private final static int LADO_SPRITE = 32;
    private final static int MASCARA_SPRITE = LADO_SPRITE - 1;
    //fin temporal
    
    public Pantalla(final int ancho, final int alto){
        this.ancho = ancho;
        this.alto = alto;
        
        pixeles = new int[alto*ancho];
    }
    
    
    //Metodo limpiar
    /*Se encargará de redibujar toda la pantalla cada vez*/
    public void limpiar(){
        for(int i=0; i<pixeles.length; i++){
            pixeles[i] = 0; //#0 Es negro en hexadecimal
        }
    }
    
    
    public void mostrar(final int compensacionX, final int compensacionY){
        for(int y=0; y<alto; y++){
            int posicionY = y + compensacionY;
            if(posicionY < 0 || posicionY >= alto){
                continue; //Para no salirnos del mapa
            }
            for(int x=0;x < ancho; x++){
                int posicionX = x+compensacionX;
                if(posicionX < 0 || posicionX >= ancho){
                    continue; //Para no salirnos del mapa
                }
                
                //Codigo para redibujar 
                //temporal
                pixeles[posicionX + posicionY*ancho] = Sprite.asfalto.pixeles[(x & MASCARA_SPRITE) + (y & MASCARA_SPRITE)*LADO_SPRITE]; 
            }
        }
    }
    
}
