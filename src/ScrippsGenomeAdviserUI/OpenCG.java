/*
 * Import and compare Complete Genomics file
 */
package ScrippsGenomeAdviserUI;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 *
 * @author gerikson
 */
public class OpenCG {
    
    public static javax.swing.JFileChooser fileChooser;
    public static Interface CGfiftyLines;
    public static File file;
    //Main file row counter
    int mainFileRowCount = 0;
    String haplotype = "00";
    //error counter;
    int missingLinesAnnotated = 0;
    int missingLinesInput = 0;
    //for variants at the same location
    String previousLine ="";
    String[] lin;
    ArrayList<String> missingLines = new ArrayList<String>();
    int missingAnnotated = 0;
 //   boolean insideLoop = false;
    
public OpenCG(){
        fileChooser = new javax.swing.JFileChooser();
}    
    
public void openFile() throws FileNotFoundException, IOException{
               
      //  CGfiftyLines = new Interface();
        int returnVal = fileChooser.showOpenDialog(ShowTable.frame);
    
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        file = fileChooser.getSelectedFile();
        String s = file.getName();
        System.out.println("The name of the file selected is: " + s);
        boolean match;
        //if it ends with *.bz2 uncompress
        if (match = s.contains(".bz2")) {       
                Interface demo1 = new Interface();  
                demo1.frame.setContentPane(demo1.createContentPane());

               //Display the window.
               demo1.frame.setSize(300, 150);
               demo1.frame.setBackground(Color.white);
               demo1.frame.setLocationRelativeTo(null);
               demo1.frame.setVisible(true); 
               demo1.frame.setTitle("Read Me!");
               javax.swing.JTextArea jTextArea1 = new javax.swing.JTextArea();
               jTextArea1.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
               jTextArea1.setText("    \n    For a shorter wait time please\n    uncompres the '*.bz2' file and \n    upload the uncompressed file!\n");
               jTextArea1.setEditable(false);
               demo1.frame.add(jTextArea1);
               demo1.frame.setVisible(true); 
        //if it doesn't end with *.bz2...        
        } else {
            
                System.out.println("You are attempting to load in a file that is not compressed, ATTN: if the file doesn't end in 'tsv' this might not be a Complete Cenomics file!");   
                BufferedReader bReader;
                try {
                    bReader = new BufferedReader(new FileReader(file));
                            String line = null;
                            boolean masterVar = false;
while((line = bReader.readLine()) != null) {
                      
                            //get rid of the first lines
                           if (line.startsWith("#", 0)) {   
                               System.out.println("first lines");
                               continue;
                            }
                           
                           else if (line.startsWith(">", 0)) {
                               String l = line.replace(">", "");
                               String[] headSplit = l.split("\t");
                               System.out.println("Header is: " + l);
                               //if chromosome is on the 3rd position this is a masterVar file
                               if (headSplit[2].contains("chromosome")) {
                                   System.out.println("This is a master var file.");
                                   masterVar = true;
                               }
                               continue;
                           } else if (line.isEmpty()) {
                              // System.out.println("Line is empty!" + "\t");
                               continue;
                           }  
                           
                            // what is in varType if it's junk we don't have to bother comparing it more...
                           String[] tempLin = line.split("\t");
                           
                            if (tempLin[6].contains("no-call") || tempLin[6].contains("no-ref") 
                                    || tempLin[6].contains("complex") || tempLin[6].contains("ref") 
                                    || tempLin[6].contains("PAR")) {
                                    
                           //check if one gene was already found
                            if (haplotype.equals("01")) {
                                              String anLine = ReadFile.arrayOfLines.get(mainFileRowCount).fileRow;
                                              String mLine = deleteNA(anLine);  
                                              String newLine = haplotype.concat("\t").concat(mLine);
                                              ReadFile.arrayOfLines.get(mainFileRowCount).fileRow = newLine;
                                              mainFileRowCount++;
                                              haplotype = "00";
                                              continue;
                                    } else {
                                        continue;
                                    } 
                                

                            }

                   lin = lineTransform(line);        
                   if (lin.equals(null)) {
                       continue;
                   }
                   
                   //are there any more lines in the
                   if (mainFileRowCount < ReadFile.arrayOfLines.size()) {
                         if (mainFileRowCount%10000 == 0){
                               System.out.print(mainFileRowCount + "\t");
                               DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                               Calendar cal = Calendar.getInstance();
                               System.out.println(dateFormat.format(cal.getTime()));
                               System.out.println("Missing lines in missingLines array: " + missingLines.size());

                           } 
                   boolean answer = compareAlgorithm(line);
                   if (answer) {
                       continue;
                   } else {
                       
                       while (answer!=true) {
                        answer = compareAlgorithm(line);
                       }
                   }
             }
                 //exit the while loop, no more lines in the main array  
                 else {
                       break;
                   }


}
                }catch (IOException ex) {
                    System.out.println("Can't open Complete Genomics file!");
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
                
   //Wahoo, we did it. Print something to screen!
  ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
  int end;
  if (ReadFile.arrayOfLines.size() > 1000) {
      end = 1000;
  } else {end = ReadFile.arrayOfLines.size();}
  
    for (int i=0; i< end; i++){
                TempArray.add(ReadFile.arrayOfLines.get(i));
            }            
            ShowTable.frame.dispose();
            ShowTable.tableStatus = -1;
            ShowTable.into2DArray(TempArray, end);
            
        //display a frame with how many missing lines 
        JTable table = new JTable();
        final Interface demo1 = new Interface();  
        demo1.frame.setContentPane(demo1.createContentPane());
             
        //Display the window.
        demo1.frame.setSize(400, 200);


        demo1.frame.setLocationRelativeTo(null);
        demo1.frame.setTitle("Missing lines");
        String one = "Lines not found in the annotated file is: " + missingLinesAnnotated;
        JLabel label1 = new JLabel(one);
        demo1.frame.add(label1);
        String two = "Lines not found in the input file is: " + missingLinesInput;
        JLabel label2 = new JLabel(two);
        demo1.frame.add(label2);
        demo1.frame.setVisible(true); 
            
        }
}
                
    System.out.println("Line not found int the annotated file is: " + missingLinesAnnotated);
    for (int i=0; i<missingLines.size(); i++) {
        System.out.println(missingLines.get(i));
    }
    System.out.println("Line not found in the input file is: " + missingLinesInput);
}

public String[] lineTransform(String line) {
  
    String[] lin = line.split("\t");   
       if (lin.length < 6) {
                 System.out.println("Small line:");
                 System.out.println(lin);
                 String[] var = null;
                 return var;               
        }  
    //if there is "X" or "Y" chromose set them as 100 and 200
     if (lin[3].equals("chrX")) {
        lin[3] = "100";
      } else if (lin[3].equals("chrY")) {
        lin[3] = "200";
      } //If this is mitocondrial DNA go to the next line
      else if (lin[3].equals("chrM")) {
         //return a null string so we know something went wrong
          String[] var = null;
          return var;                      
      }
      else { 
         try {
             lin[3] = lin[3].replace("chr", "");
             //if it's not an integer for chromosome throw exception and kick us out
                int t = Integer.parseInt(lin[3]);
              } 
                       // else catch exception - non acceptable chromosome format
                       catch( Exception ex) {        
                       System.out.println("Error! Non acceptable chromosome format: " + lin[3]);   
                            String[] var = null;
                            return var; 
                       }
                            }      

                    //change the varType column to reflect my var file
                    if (lin[6].equals("sub")){lin[6] = "delins";}
                    else if (lin[6].equals("del")) {lin[8] = "-";}
                    else if  (lin[6].equals("ins")){lin[7] = "-";}
    
    return lin;
}

public String deleteNA(String mainLine) {
     if (mainLine.startsWith("N/A")) {
          mainLine = mainLine.replace("N/A\t", "");
       }
     
     else if (mainLine.startsWith("\t")) {
        mainLine = mainLine.replaceFirst("\t", "");
    }   
    
    return mainLine;
}

public boolean compareAlgorithm(String line) {
      //check to see if we have enough lines in ReadFile.arrayOfLines
      if (mainFileRowCount < ReadFile.arrayOfLines.size()) {
                            String anLine = ReadFile.arrayOfLines.get(mainFileRowCount).fileRow;
                               
                            String[] mainLine = lineTransform(anLine);
                           
                            if (mainLine.equals(null)) {
                                return true;
                            }
                          //    System.out.println("First good mainLine id is: " + mainLine[1]);
             //now the fun part, compare them
  if (lin[3].equals(mainLine[3]) && lin[4].equals(mainLine[4]) && lin[5].equals(mainLine[5])
          && lin[6].equals(mainLine[6]) && lin[7].equals(mainLine[7]) && lin[8].equals(mainLine[8])) {  
                     //    insideLoop = false;   
                         String mLine = deleteNA(anLine);
                        //check to see if we have a haplotype for this line
                        if (haplotype.equals("00")) {

                              
                               haplotype = "01";
                                //go to the next line of complete genomics to see if it's heterozagous
                                return true;
                            }
                           

                        
                        else if (haplotype.equals("01")) {
                            //we skiped the previous line register this line and go to previous line to verify with next
                            missingAnnotated = 0;
                            haplotype = "11";
                            String newL = haplotype + "\t" + mLine;
                            ReadFile.arrayOfLines.get(mainFileRowCount).fileRow = newL;
                            mainFileRowCount++;
                            haplotype = "00";
                            return true;
                     
                        } 
          } 
   
          //the lines are not equal 
          else {
                           
                          //write out previous line if any
                          if (haplotype.equals("01")) {
                            String mLine = deleteNA(anLine);  
                            String newL = haplotype + "\t" + mLine;
                            ReadFile.arrayOfLines.get(mainFileRowCount).fileRow = newL;
                            haplotype = "00";
                            mainFileRowCount++;
                            missingAnnotated = 0;
                            return false;
                          }
                         else  if (!(missingLines.size() == 0)) {
                                

                                int temporaryPosition = -1;
                                boolean answ = false ;
                                for (int i = 0; i < missingLines.size(); i++) {
                                String[] lineMissing = lineTransform(missingLines.get(i));
                                answ = checkMissingLines(lineMissing);
                                if (answ == true) {
                                    missingAnnotated = 0;
                                    missingLines.remove(i);
                                    missingLines.trimToSize();
                                    break;
                                }
                                }
                                
                                 //if one line from the missing lines was found remove it from the missing line array
                                if (answ == true) {
                            
                                    return true;
                                } else {

                                    if (Integer.parseInt(lin[3]) < Integer.parseInt(mainLine[3])) {
                                         missingLinesInput++;
                                         return true;
                                    } else if (Integer.parseInt(lin[3]) > Integer.parseInt(mainLine[3])) {
                                         //go to the next line in Main Array
                                        System.out.println("Biger chromosome?");
                                         missingLinesInput++;
                                         return false;
                                    }
                      
                                    else{
                                      mainFileRowCount++;
                                      //new thing, this deals with lines missing in the annotated array - go to the next line of the annotated array
                                      missingAnnotated++;
                                       if (missingAnnotated > 5) {   
                                        //just give up, you won't find that line
                                        mainFileRowCount = mainFileRowCount - 5;
                                        missingAnnotated = 0;
                                        System.out.println("Skipping this line: " + line);
                                        missingLinesAnnotated++;
                                        //keep it in the missingArray, highly doubt you will find it
                                        return true;
                                      } 
                                      //end of new thing 
                                      return false;
                                    }
                                }
                               
 
                            }
                          
                          //errors! some lines are not found
                          else {
                           
                              //chromoseome comparison
                              if (Integer.parseInt(lin[3]) < Integer.parseInt(mainLine[3])) {
                                  
                                  System.out.println("Error! Variant not fount in the anotated file!");
                                  System.out.println(line);
                                  
                                  missingLinesInput++;
                                  return true;
                              } else if (Integer.parseInt(lin[3]) > Integer.parseInt(mainLine[3])) {
                                      //go to the next line in Main Array
                                      mainFileRowCount++;
                                      missingLinesInput++;
                                      return false;
                                  }

                              /*
                               * Here is an interesting part, if the begin site is equal and the end, vartype etc is not therefore
                               * we might have the lines only they are sorted in the annotated file and not sorted in the CG file
                               * we need to keep this line in memory so we will be able to compare it on the next run.
                               */    
                      
                              else{
                                 missingLines.add(line);
                                 return true;
 
                          }
                              
                          }
                          
                      }
                            
                  } else {
                      System.out.println("End of array stored in memory");
                      return true;
                  }
        return true;
}

public boolean checkMissingLines(String[] lin) {

                            String anLine = ReadFile.arrayOfLines.get(mainFileRowCount).fileRow;
                               
                            String[] mainLine = lineTransform(anLine);
                           
                            if (mainLine.equals(null)) {
                                return false;
                            }
                        
             //now the fun part, compare them
 if (lin[3].equals(mainLine[3]) && lin[4].equals(mainLine[4]) && lin[5].equals(mainLine[5]) && lin[6].equals(mainLine[6]) && lin[7].equals(mainLine[7]) && lin[8].equals(mainLine[8])) {      
                  
                        String mLine = deleteNA(anLine);
                               haplotype = "01";
                               String newL = haplotype + "\t" + mLine;
                               ReadFile.arrayOfLines.get(mainFileRowCount).fileRow = newL;
                               mainFileRowCount++;
              
                               return true;
 }
  
           //the lines are not equal 
          else {
                           
    return false;
}

}
}
        