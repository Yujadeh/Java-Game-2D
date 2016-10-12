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
public final class Sprite {
    private final int lado;
    
    private int x,y;
    
    public int [] pixeles;
    private final HojaSprites hoja;
    
    //Coleccion de Sprites
    public static Sprite asfalto = new Sprite(32, 0, 0, HojaSprites.desierto);
    //Fin de la coleccion
    
    public Sprite(final int lado, final int columna, final int fila, final HojaSprites hoja){
        this.lado = lado;
        
        pixeles = new int[lado*lado];
        
        this.x = columna*lado;
        this.y = fila*lado;
        this.hoja = hoja;
        for(int i=0;i<lado;i++){
            for(int j=0; j<lado;j++){
                pixeles[i+j * lado] = hoja.pixeles[(j + this.x) + (i + this.y)*hoja.getAncho()];
            }
        }
    }
    
}
