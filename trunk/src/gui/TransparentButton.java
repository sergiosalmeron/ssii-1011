package gui;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;


class TransparentButton extends JButton {
        public TransparentButton() {
            //super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        }
        
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
            super.paint(g2);
            g2.dispose();
        }

} 
