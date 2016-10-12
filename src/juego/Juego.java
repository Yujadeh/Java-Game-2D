/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juego;

import control.Teclado;
import graficos.Pantalla;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Jose
 */

/*Hacemos la clase principal del juego como Canvas*/

public class Juego extends Canvas implements Runnable{
    //Identificador de clase
    private static final long serialVersionUID =1L;
    
    //Creamos la ventana(static para tener una sola en toda la clase)
    private static JFrame ventana;
    //Dimensiones ventana: ancho
    private static final int ANCHO = 800;
    //Dimensiones ventana: ancho
    private static final int LARGO = 600;
    //Nombre ventana
    private static final String NOMBRE = "Juego";
    //APS
    private static int aps = 0;
    private static int fps = 0;
    
    //Variables de control de bucles(volatiles para no generar errores con los threads)
    private static volatile boolean enFuncionamiento = false;
    //Creación de hilos
    private static Thread thread;
    
    //Teclaas
    private static Teclado teclado;
    
    //COORDENADAS
    private static int x =0;
    private static int y =0;
    
    //Pantalla
    private static Pantalla pantalla;
    //Pantalla en blanco
    private static BufferedImage imagen = new BufferedImage(ANCHO,LARGO,BufferedImage.TYPE_INT_RGB);
    private static int [] pixeles = ((DataBufferInt)imagen.getRaster().getDataBuffer()).getData();
    
    
    
    //Constructor(Por defecto privado)
    private Juego(){
        setPreferredSize(new Dimension(ANCHO,LARGO));
        
        teclado = new Teclado();
        addKeyListener(teclado);
        
        pantalla = new Pantalla(ANCHO, LARGO);
        
        ventana = new JFrame(NOMBRE);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setLayout(new BorderLayout());
        ventana.add(this, BorderLayout.CENTER);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

    }
    
    private synchronized void iniciar(){
      enFuncionamiento = true;
      
      thread = new Thread(this, "Graficos");
      thread.start();
    }
    
    private synchronized void detener(){
      enFuncionamiento = false;
      //Evitamos el cuelgue al detener 
        try {
            //Lo hacemos con join() para no detener de golpe el hilo
            thread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void actualizar(){
        
        teclado.actualizar();
        //Vemos las teclas pulsadas
        if(teclado.arriba){
            //System.out.println("arriba");
            y++;
        }
        
        if(teclado.abajo){
            //System.out.println("abajo");
            y--;
        }
        
        if(teclado.izquierda){
            //System.out.println("izquierda");
            x++;
        }
        
        if(teclado.derecha){
            //System.out.println("derecha");
            x--;
        }
        
        aps++;
    }
    
    private void mostrar(){
        BufferStrategy estrategia = getBufferStrategy();
        
        if(estrategia == null){
            createBufferStrategy(3);
            return;
        }
        
        pantalla.limpiar();
        pantalla.mostrar(x, y);
        
        //Copia los arrays de pantalla
        System.arraycopy(pantalla.pixeles, 0, pixeles, 0, pixeles.length);
        
      //  for(int i=0;i < pixeles.length; i++){
        //    pixeles[i] = pantalla.pixeles[i];
        //}
        
        Graphics g = estrategia.getDrawGraphics();
        
        g.drawImage(imagen, 0, 0, getWidth(),getHeight(),null);
        g.dispose();
        
        estrategia.show();
        fps++;
    }
    
    @Override
    public void run() {
        final int NS_POR_SEGUNDO = 1000000000;
        final byte APS_OBJETIVO = 60;
        final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO/APS_OBJETIVO;
        
        long referenciaActualizacion = System.nanoTime();
        long referenciaContador = System.nanoTime();
        
        double tiempoTranscurrido;
        double delta = 0;
        
        requestFocus();
        
        while(enFuncionamiento){
            final long inicioBucle = System.nanoTime();
            
            tiempoTranscurrido = inicioBucle - referenciaActualizacion;
            referenciaActualizacion = inicioBucle;
            
            delta += tiempoTranscurrido / NS_POR_ACTUALIZACION;
            
            while(delta >= 1){
             //Actualizamos información del bucle
               actualizar();
               delta--;
            }

            //Redibujamos los gráficos
            mostrar();
            if(System.nanoTime() - referenciaContador > NS_POR_SEGUNDO){
                ventana.setTitle(NOMBRE + "|| APS: " + aps + "|| FPS: " + fps);
                aps = 0;
                fps = 0;
                referenciaContador = System.nanoTime();
            }
        }
    }
    
    public static void main(String[] args){
        Juego juego = new Juego();
        juego.iniciar();
    }


  
}
