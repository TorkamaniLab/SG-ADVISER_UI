/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ScrippsGenomeAdviserUI;

import java.util.Vector;

/**
 *
 * @author gerikson
 */
public class Header {
    
    public Vector<String> headers; //= new Vector<String>();
     
    public Header(String lines){
        this.headers = InitHeader(lines);
        //this.headers.add(h);
    }
    
    public Vector<String> InitHeader(String lines){
       String[] r = lines.split("\t");
       int size = r.length;
        Vector<String> row = new Vector<String>();
       for(int i=0; i<size; i++) {
            row.add(r[i]);
       }

       
       return row;
    }
   
    public Vector<String> getheaders(){
        return headers;
}
    
    
}
