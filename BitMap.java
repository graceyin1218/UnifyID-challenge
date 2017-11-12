import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

import javax.swing.*;

public class BitMap {

    // will start start with Java's Random number generator before using up my random.org quota
    
    public static void main(String[] args) throws IOException {
        
        // according to Java's Color documentation:
        // "Bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are blue"
        
        // this is convenient -- we can just request 32 bit integers from random.org 
        // and use them as rgb values
        
        int[] RGBs = new int[128*128];
        
        // fills RGBs with random numbers from random.org
        getRandomNumbers(RGBs);
        
//        // used temporarily so I wouldn't waste my quota :)
//        for (int i = 0; i < 100*100; i++) {
//             RGBs[i] = (int)((long)(Math.random()*Long.MAX_VALUE) - Long.MAX_VALUE/2);
//        }
        
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
    
    // array must be of length 128*128
    public static void getRandomNumbers(int[] array) throws IOException {
        // get the first 10000 numbers
        
        // oh my goodness this url is so long :(
        URL url = new URL("https://www.random.org/integers/?num=10000&min=-1000000000&max=1000000000&col=1&base=10&format=plain&rnd=new");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        
        int counter = 0;
        
        Scanner in = new Scanner(con.getInputStream());
        while (in.hasNext()) {
            String s = in.next();
            
            array[counter] = Integer.parseInt(s);
            counter++;
        }
        
        in.close();
        con.disconnect();
        
        
        // need 6384 more numbers
        
        URL url2 = new URL("https://www.random.org/integers/?num=6384&min=-1000000000&max=1000000000&col=1&base=10&format=plain&rnd=new");
        HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
        con2.setRequestMethod("GET");
        int status2 = con2.getResponseCode();
        
        in = new Scanner(con2.getInputStream());
        while (in.hasNext()) {
            // just in case something goes wrong
            if (counter >= 128*128) {
                System.out.println("counter : " + counter);
                break;
            }
            String s = in.next();
            
            array[counter] = Integer.parseInt(s);
            counter++;
        }
        
        in.close();
        con2.disconnect();
        
    }

}
