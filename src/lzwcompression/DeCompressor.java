/*
 * DeCompressor.java
 *
 * Created on 16 August 2005, 18:27
 */

import java.io.*;
import java.util.*;

/**
 *
 * @author Mad_Fool
 */
public class DeCompressor {
    
    /** Creates a new instance of DeCompressor */
    public DeCompressor()
    {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        BufferedReader input = null;
        PrintWriter output = null;
        
        try
        {
            //set up reading from source file            
            input = new BufferedReader(new FileReader("inputfile.txt.lzw"));
            
            //set up writing to target file
            //String filename = args[0].substring(0, args[0].length() - 4);
            output = new PrintWriter(new FileOutputStream("outputfile.txt"));
        }
        catch (IOException e)
        {
            System.out.println("Problem with input/output file");
            System.exit(1);
        }
        
        try
        {
            ArrayList phrases = new ArrayList();
            
            for (int i = 0; i < 256; i++)
            {
                phrases.add(Character.toString((char)i));
            }
            
            int phraseNumberCount = 256;
            String readInput = input.readLine();
            int currentPhraseNumber = Integer.parseInt(readInput);
            String lastSeen = (String)phrases.get(currentPhraseNumber);
            output.print(lastSeen);
            output.flush();
            
            readInput = input.readLine();          
            while(readInput != null)
            {
                currentPhraseNumber = Integer.parseInt(readInput);
                
                String toOutput = null;
                
                if (currentPhraseNumber >= phrases.size())
                {
                    toOutput = lastSeen + lastSeen.charAt(0);
                }
                else
                {
                    toOutput = (String)phrases.get(currentPhraseNumber);
                }
                
                //DEBUG:
                System.out.println(toOutput);
                
                output.print(toOutput);
                output.flush();

                phrases.add(new String(lastSeen + toOutput.charAt(0)));                
                lastSeen = toOutput;
                
                readInput = input.readLine();
            }
        }
        catch (Exception e)
        {
            System.out.println("Problem decompressing file");
            System.exit(2);
        }
    } 
}
