/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.flappybird;

/**
 *
 * @author Utente01
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FlappyBird extends JPanel implements ActionListener {

    int birdY = 250;
    int velocity = 0;
    int gravity = 1;
    
    // Dimensioni uccellino
    int birdSize = 30;
    int birdX = 100;

    int pipeX = 500;
    int pipeWidth = 50;
    int pipeGap = 150;
    int pipeTopHeight = 200;

    Timer timer;
    
    boolean gameStarted = false; 
    boolean gameOver = false;

    public FlappyBird() {
        JFrame frame = new JFrame("Flappy Bird");

        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); 
        frame.add(this);
        
        // KeyBindings per la barra spaziatrice
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("SPACE"), "salto");
        actionMap.put("salto", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gestisciInput(); 
            }
        });

        // Alternativa con il Mouse
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gestisciInput();
            }
        });

        timer = new Timer(20, this);
        timer.start();

        frame.setVisible(true);
    }

    private void gestisciInput() {
        if (!gameStarted) {
            gameStarted = true;
            velocity = -10; 
        } else if (gameOver) {
            // Reset completo della partita
            birdY = 250;
            velocity = 0;
            pipeX = 500;
            pipeTopHeight = 50 + (int)(Math.random() * 200);
            gameOver = false;
            gameStarted = false; 
        } else {
            velocity = -10;
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Sfondo (Cielo)
        g.setColor(Color.cyan);
        g.fillRect(0, 0, 500, 500);

        // Tubo Superiore
        g.setColor(Color.green);
        g.fillRect(pipeX, 0, pipeWidth, pipeTopHeight);
        
        // Tubo Inferiore
        g.fillRect(pipeX, pipeTopHeight + pipeGap, pipeWidth, 500 - pipeTopHeight - pipeGap);

        // Uccellino
        g.setColor(Color.yellow);
        g.fillOval(birdX, birdY, birdSize, birdSize);

        // Schermata Iniziale
        if (!gameStarted && !gameOver) {
            g.setColor(new Color(0, 0, 0, 120)); 
            g.fillRect(0, 0, 500, 500);

            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 35));
            g.drawString("FLAPPY BIRD", 135, 200);
            
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Premi SPAZIO o CLICCA per iniziare", 120, 260);
        }

        // Schermata di Game Over
        if (gameOver) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, 500, 500);

            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("GAME OVER", 130, 200);
            
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Premi SPAZIO o CLICCA per riprovare", 115, 260);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStarted && !gameOver) {
            velocity += gravity;
            birdY += velocity;
            pipeX -= 5;

            if (pipeX < -50) {
                pipeX = 500;
                pipeTopHeight = 50 + (int)(Math.random() * 200);
            }

            // Controllo collisione pavimento/soffitto
            if (birdY > 430 || birdY < 0) {
                gameOver = true;
            }

            // NUOVA LOGICA: Controllo collisioni con le barriere verdi
            checkCollisioni();
        }
        repaint();
    }

    private void checkCollisioni() {
        // Creiamo il rettangolo attorno all'uccellino
        Rectangle rectUccellino = new Rectangle(birdX, birdY, birdSize, birdSize);
        
        // Creiamo il rettangolo per il tubo superiore
        Rectangle rectTuboSopra = new Rectangle(pipeX, 0, pipeWidth, pipeTopHeight);
        
        // Creiamo il rettangolo per il tubo inferiore
        Rectangle rectTuboSotto = new Rectangle(pipeX, pipeTopHeight + pipeGap, pipeWidth, 500 - pipeTopHeight - pipeGap);

        // Se l'uccellino interseca il tubo sopra O il tubo sotto, è Game Over
        if (rectUccellino.intersects(rectTuboSopra) || rectUccellino.intersects(rectTuboSotto)) {
            gameOver = true;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlappyBird());
    }
}                                                 