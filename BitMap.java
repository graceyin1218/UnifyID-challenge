import java.awt.image.BufferedImage;

import javax.swing.*;

public class BitMap {

    // will start start with Java's Random number generator before using up my random.org quota
    
    public static void main(String[] args) {
        
        // according to java Color documentation:
        // "Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are blue"
        // this is convenient -- we can just request 32 bit integers from random.org and insert 
        
        int[] RGBs = new int[128*128];
        
        // used temporarily so I wouldn't waste my quota :)
        for (int i = 0; i < 128*128; i++) {
             RGBs[i] = (int)((long)(Math.random()*Long.MAX_VALUE) - Long.MAX_VALUE/2);
        }
        
        // create a BufferedImage and fill in the pixels
        BufferedImage img = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
        
        int counter = 0;
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getWidth(); y++) {
                img.setRGB(x, y, RGBs[counter]);
                counter++;
            }
        }
        
        // display! :D
        
        final JFrame frame = new JFrame("BitMap!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JLabel(new ImageIcon(img)));
        frame.pack();
        frame.setVisible(true);
    }

}
