/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ScrippsGenomeAdviserUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

/**
 *
 * @author gerikson
 */
public class ReadFile extends JFrame implements Runnable {
    
    public JFrame frame;
    public Container content;
    public JProgressBar progressBar;
    public Border border;
    
    public File file;
    public static ArrayList<ScrippsGenomeAdviserUI.Reader> arrayOfLines = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
    public static String fileName;
    public Header head;
    public static int fileLines;
    public static boolean status = false;
    public double perc2 = 0;
    
            
    public ReadFile(File f){
        
        file = f;
       
         frame = new JFrame("File Loading");
         content = frame.getContentPane();
         progressBar = new JProgressBar();
         border = BorderFactory.createTitledBorder("Entire file loading in memory...");
         progressBar.setBorder(border);
         content.add(progressBar, BorderLayout.NORTH);
         frame.setSize(300, 100);
         frame.setVisible(true);
          
         
    }
  
        @Override
     public void run() {
        try {
            int datacount = 0;
            
            Interface newfile = new Interface();    
            fileName =  file.getName();
            BufferedReader bReader;
           
               bReader = new BufferedReader(new FileReader(file));
                    
               long len = file.length();
                    double y = (double) len;
                    double ing  = 60000000.0;
                    double x = (double) ing/len;
                    double perc = (double) (x*100);
                   String line = null;
                   
              //     if (Interface.vcfStatus == false) {
            try {
                    line = bReader.readLine();
                } catch (IOException ex) {
                    Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null, ex);
                }

                   for (int i = 0; i < 1; i++) {
                       head = new Header(line);
                   }
                try {
                    /*
                     * Check to see if this file was has already a comments column and an index
                     * from the old dataset, remove the index and don't add "N/A" at the begin
                     */
                     if (Interface.analysedFile) {
                            while((line = bReader.readLine()) != null) {
                          
                         datacount++;
                         String nt;

                            String[] tempLine = line.split("\t");
                            nt = Integer.toString(datacount).concat("\t");
                            for (int st = 0; st<Interface.indexColumnNumber; st++) {
                                nt = nt.concat(tempLine[st]).concat("\t");
                            } 
                            for (int st2 = Interface.indexColumnNumber+1; st2<tempLine.length; st2++) {
                                nt = nt.concat(tempLine[st2]).concat("\t");
                            }
                            ScrippsGenomeAdviserUI.Reader ob1 = new ScrippsGenomeAdviserUI.Reader(nt);
                            arrayOfLines.add(ob1);
                  
                           if (datacount%100000 == 0){
                               
                              // Loading.frame.dispose();
                               System.out.print(datacount + "\t");
                               DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                               Calendar cal = Calendar.getInstance();
                               System.out.println(dateFormat.format(cal.getTime()));
                               perc2 = perc2 + perc;
                               int b = (int) perc2;  
                                progressBar.setValue(b);
                                progressBar.setStringPainted(true);
                           }
                         
                      }
                     } else {
                    while((line = bReader.readLine()) != null) {
                          
                         datacount++;
                         
                           //adding an index plus first element needs to be an empty space for future adition of imported genotypes
                         //Index is needed in the table view so i will know which 
                         //line to edit in the dataset
                         String  current_line = "N/A" + "\t" + datacount + "\t" + line;
                     
                           ScrippsGenomeAdviserUI.Reader ob1 = new ScrippsGenomeAdviserUI.Reader(current_line);
                           arrayOfLines.add(ob1);
                  
                           if (datacount%100000 == 0){
                               
                              // Loading.frame.dispose();
                               System.out.print(datacount + "\t");
                               DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                               Calendar cal = Calendar.getInstance();
                               System.out.println(dateFormat.format(cal.getTime()));
                               perc2 = perc2 + perc;
                               int b = (int) perc2;  
                                progressBar.setValue(b);
                                progressBar.setStringPainted(true);
                           }
                         
                      }
                     }
                } catch (IOException ex) {
                    Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null, ex);
                }

               //Trim the array in case if big files are loaded
               arrayOfLines.trimToSize(); 
               progressBar.setValue(100);
               progressBar.setStringPainted(true);             
               fileLines = datacount;
               System.out.println("Number of variants in file is: " + datacount);
               ShowTable.FileNumberRows = fileLines;
               status = true;
               //insert the main array into the arrayOfArray list
               ShowTable.arrayOfArrays.add(arrayOfLines); 
               FilterFunctions.filterName.add("Main Array");
               FilterFunctions.currentArray = 0;
               ShowTable.tableStatus = 17;
               this.frame.dispose();      

          //      }
                     
                 
                    
                   // if is a vcf file
      /*             else if (Interface.vcfStatus == true) {
                try {
                    line = bReader.readLine();
                } catch (IOException ex) {
                    Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null, ex);
                }
//                   for (int i = 0; i < Interface.vcfCount+2; i++) { 
//                     //get rid of the first lines
//                    if (line.startsWith("##", 0)) {     
//                  
//                        break;
//             
//                     }
//                    
//                    else if (line.startsWith("#", 0)) {
//                        String l = line.replace("#", "");
//                        head = new Header(l); 
//                        break;
//                    }
//                   }
                try {
                    while((line = bReader.readLine()) != null) {
                       if (line.startsWith("##", 0)) {     
                  
                        continue;
             
                     }
                    
                    else if (line.startsWith("#", 0)) {
                        String l = line.replace("#", "");
                        head = new Header(l); 
                        
                        continue;
                        
                    } else {
                                                  
                          datacount++;
                           String nt = Integer.toString(datacount);
                           //adding an index
                          nt = datacount + "\t" + line;
                        ScrippsGenomeAdviserUI.Reader ob1 = new ScrippsGenomeAdviserUI.Reader(nt);
                        arrayOfLines.add(ob1);
               
           //              
           //            /**
           //             * Test function to see how the program works
           //             */   
              /*           if (datacount%100000 == 0){
                             
                            // Loading.frame.dispose();
                             System.out.print(datacount + "\t");
                             DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                             Calendar cal = Calendar.getInstance();
                             System.out.println(dateFormat.format(cal.getTime()));
                             perc2 = perc2 + perc;
                             int b = (int) perc2;  
                              progressBar.setValue(b);
                              progressBar.setStringPainted(true);
                         }
                       
                    }
                }
                }catch (IOException ex) {
                    Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null, ex);
                }
     

               System.out.println("Entire file stored in memory!");
               progressBar.setValue(100);
               progressBar.setStringPainted(true);             
               fileLines = datacount;
               ShowTable.FileNumberRows = fileLines;
               status = true;
               this.frame.dispose();
                    

                   } */
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null, ex);
        }

}
}