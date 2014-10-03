/*
 * Compressor.java
 *
 * Created on 16 August 2005, 15:10
 */

import java.io.*;
import java.util.*;

/**
 *
 * @author Mad_Fool
 */
public class Compressor
{
    private static class TreeNode
    {
        int phraseNumber;
        char thisCharacter;
        ArrayList children;
    }
    
    /** Creates a new instance of Compressor */
    public Compressor()
    {
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        int shifter = 10;
        System.out.println(shifter);
        shifter = shifter >> 2;
        System.out.println(shifter);
        
        //initialise the tree with the first 255 bits
        TreeNode[] rootNode = new TreeNode[256];
        for (int i = 0; i < 256; i++)
        {
            rootNode[i] = new TreeNode();
            rootNode[i].phraseNumber = i;
            rootNode[i].thisCharacter = (char)i;
            rootNode[i].children = new ArrayList();         
        }
        
        DataInputStream input = null;
        PrintWriter output = null;
        
        try
        {
            //set up reading from source file
            input = new DataInputStream(new FileInputStream(args[0]));
            
            //set up writing to target file
            output = new PrintWriter(new FileOutputStream(args[0] + ".lzw"));
        }
        catch (IOException e)
        {
            System.out.println("Problem with input/output file");
            System.exit(1);
        }
        
        try
        {
            //loop through until file is compressed
            int phraseNumberCount = 256;
            int readInput = input.read();
            TreeNode currentNode = null;
            while(readInput != -1)
            {
                if (currentNode == null)
                {
                    currentNode = rootNode[readInput];
                }
                else
                {
                    TreeNode nextNode = findChildNode(currentNode.children, (char)readInput);
                    if (nextNode != null)
                    {
                        currentNode = nextNode;
                    }
                    else
                    {                        
                        output.println(currentNode.phraseNumber);
                        output.flush();
                        
                        TreeNode newNode = new TreeNode();
                        newNode.phraseNumber = phraseNumberCount++;
                        newNode.thisCharacter = (char)readInput;
                        newNode.children = new ArrayList();
                        currentNode.children.add(newNode);
                        
                        currentNode = rootNode[readInput];
                    }
                }

                readInput = input.read();
            }
            //exception for the final character
            output.println(currentNode.phraseNumber);
            output.flush();
        }
        catch (Exception e)
        {
            System.out.println("Problem compressing file");
            System.exit(2);
        }
    }
    
    private static TreeNode findChildNode(ArrayList children, char input)
    {
        Iterator childrenList = children.iterator();
        while(childrenList.hasNext())
        {
            TreeNode currentChild = (TreeNode)childrenList.next();
            if (currentChild.thisCharacter == input)
            {
                return currentChild;
            }
        }
        
        return null;
    }
}
