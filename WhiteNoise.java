import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class WhiteNoise {
    // I used up my random.org quota for the day... 
    // so I'll just try this with Math.random() and see what happens :)

    /*
       Found this online (thank god :D )
     
       WAV File Specification
       FROM http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
       
       The canonical WAVE format starts with the RIFF header:
       0         4   ChunkID          Contains the letters "RIFF" in ASCII form
                                      (0x52494646 big-endian form).
       4         4   ChunkSize        36 + SubChunk2Size, or more precisely:
                                      4 + (8 + SubChunk1Size) + (8 + SubChunk2Size)
                                      This is the size of the rest of the chunk 
                                      following this number.  This is the size of the 
                                      entire file in bytes minus 8 bytes for the
                                      two fields not included in this count:
                                      ChunkID and ChunkSize.
       8         4   Format           Contains the letters "WAVE"
                                      (0x57415645 big-endian form).
    
       The "WAVE" format consists of two subchunks: "fmt " and "data":
       The "fmt " subchunk describes the sound data's format:
       12        4   Subchunk1ID      Contains the letters "fmt "
                                      (0x666d7420 big-endian form).
       16        4   Subchunk1Size    16 for PCM.  This is the size of the
                                      rest of the Subchunk which follows this number.
       20        2   AudioFormat      PCM = 1 (i.e. Linear quantization)
                                      Values other than 1 indicate some 
                                      form of compression.
       22        2   NumChannels      Mono = 1, Stereo = 2, etc.
       24        4   SampleRate       8000, 44100, etc.
       28        4   ByteRate         == SampleRate * NumChannels * BitsPerSample/8
       32        2   BlockAlign       == NumChannels * BitsPerSample/8
                                      The number of bytes for one sample including
                                      all channels. I wonder what happens when
                                      this number isn't an integer?
       34        2   BitsPerSample    8 bits = 8, 16 bits = 16, etc.
    
       The "data" subchunk contains the size of the data and the actual sound:
       36        4   Subchunk2ID      Contains the letters "data"
                                      (0x64617461 big-endian form).
       40        4   Subchunk2Size    == NumSamples * NumChannels * BitsPerSample/8
                                      This is the number of bytes in the data.
                                      You can also think of this as the size
                                      of the read of the subchunk following this 
                                      number.
       44        *   Data             The actual sound data.
    */
    
    public static void main(String[] args) {
        // let's set... 
        
        // numChannels = 1
        // sampleRate = 8000
        // bitsPerSample = 32 (normal integers)
        // byterate = 8000 * 1 * 32/8
        // blockAlign = 1 * 32/8
        
        // subchunk2size = numSamples * 1 * 32/8
        // numSamples = 3sec * 8000samples/sec
        
        int numSamples = 3 * 8000;
        int subchunk2size = numSamples * 1 * 32/8;
        int size = 44 + subchunk2size;
        
        byte[] bytes = new byte[size];
        
        int counter = 0;
        
        // ChunkID = "RIFF"
        bytes[0] = 'R';
        bytes[1] = 'I';
        bytes[2] = 'F';
        bytes[3] = 'F';
        counter += 4;
        
        // ChunkSize = 36 + SubChunk2Size
        ByteBuffer b4 = ByteBuffer.allocate(4);
        b4.putInt(36 + subchunk2size);
        bytes[counter] = b4.get(0);
        bytes[counter+1] = b4.get(1);
        bytes[counter+2] = b4.get(2);
        bytes[counter+3] = b4.get(3);
        counter += 4;
        
        // Format = "WAVE"
        bytes[counter] = 'W';
        bytes[counter+1] = 'A';
        bytes[counter+2] = 'V';
        bytes[counter+3] = 'E';
        counter += 4;
        
//////////////////////////
        
        // Subchunk1ID = "fmt "
        bytes[counter] = 'f';
        bytes[counter+1] = 'm';
        bytes[counter+2] = 't';
        bytes[counter+3] = ' ';
        counter += 4;
        
        // Subchunk1Size = 16 for PCM
        b4 = ByteBuffer.allocate(4);
        b4.putInt(16);
        bytes[counter+3] = b4.get(0);
        bytes[counter+2] = b4.get(1);
        bytes[counter+1] = b4.get(2);
        bytes[counter] = b4.get(3);
        counter += 4;
        
        // AudioFormat = 1 (LPCM)
        ByteBuffer b2 = ByteBuffer.allocate(2);
        b2.putShort((short)1);
        bytes[counter+1] = b2.get(0);
        bytes[counter] = b2.get(1);
        counter += 2;
        
        // NumChannels = 1 (mono)
        b2 = ByteBuffer.allocate(2);
        b2.putShort((short)1);
        bytes[counter+1] = b2.get(0);
        bytes[counter] = b2.get(1);
        counter += 2;
        
        // SampleRate = 8000
        b4 = ByteBuffer.allocate(4);
        b4.putInt(8000);
        bytes[counter+3] = b4.get(0);
        bytes[counter+2] = b4.get(1);
        bytes[counter+1] = b4.get(2);
        bytes[counter+0] = b4.get(3);
        counter += 4;
        
        // ByteRate = SampleRate * NumChannels * BitsPerSample/8 = 8000 * 1 * 32/8
        b4 = ByteBuffer.allocate(4);
        b4.putInt(8000 * 32/8);
        bytes[counter+3] = b4.get(0);
        bytes[counter+2] = b4.get(1);
        bytes[counter+1] = b4.get(2);
        bytes[counter] = b4.get(3);
        counter += 4;
        
        // BlockAlign = NumChannels * BitsPerSample/8 = 1 * 32/8
        b2 = ByteBuffer.allocate(2);
        b2.putShort((short)4);
        bytes[counter+1] = b2.get(0);
        bytes[counter] = b2.get(1);
        counter += 2;
        
        // BitsPerSample = 32 (for convenience)
        b2 = ByteBuffer.allocate(2);
        b2.putShort((short)32);
        bytes[counter+1] = b2.get(0);
        bytes[counter] = b2.get(1);
        counter += 2;
        
//////////////////////////
        // Subchunk2ID = "data"
        bytes[counter] = 'd';
        bytes[counter+1] = 'a';
        bytes[counter+2] = 't';
        bytes[counter+3] = 'a';
        counter += 4;
        
        // Subchunk2Size = NumSamples * NumChannels * BitsPerSample/8 (already calculated)
        b4 = ByteBuffer.allocate(4);
        b4.putInt(subchunk2size);
        bytes[counter+3] = b4.get(0);
        bytes[counter+2] = b4.get(1);
        bytes[counter+1] = b4.get(2);
        bytes[counter] = b4.get(3);
        counter += 4;
        
        // DATA!!!!
        
        //getRandomNumbers(bytes, counter, numSamples, 4);
        
        // Would replace this part with random.org :/
        for (int i = 0; i < numSamples * 4; i++) {
            if (counter >= size) {
                System.out.println("Bad : size is too big");
                break;
            }
            bytes[counter] = (byte)(Math.random()*Short.MAX_VALUE);
            counter++;
        }
        
        writeWav(bytes);
    }
    
    public static void writeWav(byte[] b) {
        String name = "whitenoise.wav";
        File file = new File(name);
        try {
            if (!file.exists() ) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(name);
            fos.write(b);
            fos.close();
        } 
        catch (IOException e) {
            System.out.println("Bad : " + e);
        }
    }
    
    // assumes that there's enough space in bytes to store numSamples * blockSize
    // more elements.
    // (untested, so this prob has some dumb bugs :/ )
    public static void getRandomNumbers(byte[] bytes, int start, int numSamples, int blockSize) throws IOException {
        int randNeeded = numSamples * blockSize;
        int counter = 0;
        
        while (randNeeded > 10000) {
            URL url = new URL("https://www.random.org/integers/?num=10000&min=-128&max=127&col=1&base=10&format=plain&rnd=new");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            
            Scanner in = new Scanner(con.getInputStream());
            while (in.hasNext()) {
                String s = in.next();
                
                bytes[counter] = Byte.parseByte(s);
                counter++;
            }
            
            in.close();
            con.disconnect();
        }
        
        if (randNeeded > 0) {
            URL url = new URL("https://www.random.org/integers/?num=" + randNeeded + "&min=-128&max=127&col=1&base=10&format=plain&rnd=new");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            
            Scanner in = new Scanner(con.getInputStream());
            while (in.hasNext()) {
                String s = in.next();
                
                bytes[counter] = Byte.parseByte(s);
                counter++;
            }
            
            in.close();
            con.disconnect();
        }
    }
}
