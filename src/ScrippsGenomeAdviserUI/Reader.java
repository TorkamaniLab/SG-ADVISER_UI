/*
 * To change this template, choose Tools
 | Templates
 * and open the template in the editor.
 */
package ScrippsGenomeAdviserUI;

import java.io.IOException;

/**
 *
 * @author gerikson
 */
public class Reader {
       
    public String fileRow;
    public String Comments; 
    public static int lineNumber;
    public static int VCFcompare;
    
    // Initialize the class constructor
    public Reader (String row) {
        this.fileRow = row;
    }
    
    public void InsertComment(String s) {
        this.Comments = s;
    }
    

}

