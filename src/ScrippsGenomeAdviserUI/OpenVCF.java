/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ScrippsGenomeAdviserUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

import ScrippsGenomeAdviserUI.ShowTable;


public class OpenVCF implements Runnable {
    public static javax.swing.JFileChooser fileChooser;
    public static Interface VCFfiftyLines;
    public static File file;
    //what line should the comparison start
    private int startCompare = 0;
    //how many lines were compared        
    private int compareCounter = 0;
    //is there VCF data available
    public int VCFAvailable = 0;
    public int parsingError = 0;
    public int VCFnotFound = 0;
    public ArrayList mainArrayNotFound = new ArrayList();
    public ArrayList<LostVCF> VCFobjectsArray = new ArrayList<LostVCF>();
    public static String[] importedGenotypes;
    public int numberGeno = 0;
    
    //Progress Bar
	public static JFrame frame;
	public static Container content;
	public static JProgressBar progressBar;
	public static Border border;
	public static int b;
	
    public OpenVCF(){
        fileChooser = new javax.swing.JFileChooser();
        
		frame = new JFrame("Loading genotypes");
		content = frame.getContentPane();
		progressBar = new JProgressBar();
		border = BorderFactory.createTitledBorder("Loading VCF");
		progressBar.setBorder(border);
		content.add(progressBar, BorderLayout.NORTH);
		frame.setSize(300, 100);
		b = 0;
    }
     
public void openFile() {
      //   System.out.println("In VCF");   
         ShowTable.tableStatus = -1;
         VCFfiftyLines = new Interface();
         String header = "";
         
    int returnVal = fileChooser.showOpenDialog(ShowTable.frame);
    
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        file = fileChooser.getSelectedFile();
        VCFfiftyLines.fileName =  file.getName();
        String s = VCFfiftyLines.fileName;
        boolean match;
        int datacount = 0;
        String NAgen = "";
        //make sure it ends with vcf
        if (match = !s.contains(".vcf")) {
            System.out.println("This file doesn't have a *.vcf extension, is this a vcf file? If so, please rename the file to end with '.vcf' and try again.");
        }
        else if (match = s.contains(".vcf")) {
               BufferedReader bReader;
        try {
            bReader = new BufferedReader(
         new FileReader(file));
                    String line;
                 while((line = bReader.readLine()) != null) {
                    VCFfiftyLines.arrayOfLines = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
                    //get rid of the first lines
                   if (line.startsWith("##", 0)) {   
                 //      vcfCount = vcfCount + 1;
                      // System.out.println("first lines");
            
                    }               
                   else if (line.startsWith("#", 0)) {
                       header = line.replace("#", "");
                       System.out.println("header");
                       VCFfiftyLines.head = new Header(header);
                       String[] t =header.split("\t");
                       numberGeno = t.length - 9;
                       //headString = l;
                       break;
                   }
                    
                 } 

        
        
        //for testing purposes, how many lines were skiped?
         int skipLine = 0;
 		// progress bar calculations

 		int linesLeft =  ReadFile.arrayOfLines.size();
 		int threePerc = linesLeft / 100 * 3;
 		if (threePerc < 1)
 		{
 			threePerc = 1;
 		}
        for (int in=0; in< ReadFile.arrayOfLines.size(); in++) {
                    
                    String n =null;             
                    n = bReader.readLine();
                    if (n == null){
                        break;
                    } else {
                
                    if (datacount % 10000 == 0) {
                          System.out.println("ArrayCounter is: " + in);
                          DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                          Calendar cal = Calendar.getInstance();
                          System.out.println(dateFormat.format(cal.getTime()));
                          
                    }
                    
                    datacount++;
        				
        			// check the percentage, if true increment the progress bar
        			if ((datacount % threePerc) == 0)
        			{
        				frame.setVisible(true);
        				b = b + 3;
        				progressBar.setValue(b);
        				progressBar.setStringPainted(true);
        			}
                    
                    String[] lin = n.split("\t");
                    //Extract chr from vcf
                    String chr = lin[0].replace("chr", "");

                    int t;
                    //if there is "X" or "Y" chromose set them as 100 and 200
                    if (chr.equals("X")) {
                        t = 100;
                    } else if (chr.equals("Y")) {
                        t = 200;
                    } else { 
                         try {
                 t = Integer.parseInt(chr);
         
               } 
               // else catch exception - non acceptable chromosome format
               catch( Exception ex) {        
               System.out.println("Error! Non acceptable chromosome format: " + chr);   
               continue;
               }
                    }      
                    
                    int chrom = t; 
                    int begin = Integer.parseInt(lin[1]) - 1;
                    
                    //Extracting the 0/1s from VCF file
                    String importedGenotypes = ""; 

                                      
                  // just for first line extract the number of genotypes in file, if we can't find the genotypes we need to
                        //add N/A and skip
                    
                    
                    if(datacount == 1) {
                         
                         for (int ti = 9; ti < lin.length; ti++) {
                               
                                if (ti != lin.length - 1) {
                                    NAgen = NAgen + "N/A" + "\t";
                        } else {
                        NAgen = NAgen + "N/A"; 
                        }}
                    }
                    for (int ti = 9; ti < lin.length; ti++) {
                        String[] a = lin[ti].split(":");
                        if (ti != lin.length - 1) {
                            importedGenotypes = importedGenotypes + a[0] + "\t";
                        } else {
                        		importedGenotypes = importedGenotypes + a[0]; 
                        }
                    } 
                    
                    //System.out.println("Imported Genotypes " + importedGenotypes);
                    String varType = null;
                    String ref = lin[3];
                    String variation = lin[4];
                    String[] variat = null;
                    //for the coma separated instances 
                    int attencion = 0;
                    if (variation.contains(",")) {
                        attencion =1;
                        variat =  variation.split(",");
                        //variat.
                    }
                    
                    if (attencion == 1){
                    		for (int v=0; v<variat.length; v++) {
                    			String var = variat[v];
                    			vcfTransform(chrom, ref, var, begin, importedGenotypes, NAgen, lin);
                    		}  
                    } else {
                    			String var = variation;
                    			vcfTransform(chrom, ref, var, begin, importedGenotypes, NAgen, lin);
                    }
                    
        }
        }
        
    //    System.out.println("Comparison done!!!");
   //     System.out.println("Parsing Error: " + parsingError);
   //     System.out.println("VCF not found " + VCFnotFound );
        if (VCFnotFound > 1000) {
            System.out.println("Number of missing variants or variants parsed wrong is " + VCFnotFound + " Please make sure the annotation file is sorted by chromosome!");
        } else {
        		System.out.println("Searching for missing variants");
        		missingVariants();
        }
        
    //Print something
  ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
  int end;
  if (ReadFile.arrayOfLines.size() > 1000) {
	  ShowTable.onlyPage = 0;
	  end = 1000;
      
  } else {end = ReadFile.arrayOfLines.size();}
  
    for (int i=0; i< end; i++){
                TempArray.add(ReadFile.arrayOfLines.get(i));
            } 
			
    			frame.dispose();
            ShowTable.frame.dispose();
            ShowTable.tableStatus = -1;
            //adding the extra columns columns
            String[] headTemp = ShowTable.columns;
            String[] vcfHead = header.split("\t");
            //how many genotypes we have in file
            int numberGenotypes = vcfHead.length - 9;
            ShowTable.columns = new String[headTemp.length+numberGenotypes];
            importedGenotypes = new String[numberGenotypes];
            for (int i=0; i<numberGenotypes; i++) {
                importedGenotypes[i] = "Genotype ".concat(vcfHead[i + 9]);
                ShowTable.columns[i] = "Genotype ".concat(vcfHead[i + 9]);
            }
           
            /*
             * Adding this version of the array with the vcf to the arrayofArrays
             * 
             */
           
            FilterFunctions.filterName.add("main array with genotypes");
            //change the count of current array
            FilterFunctions.currentArray = FilterFunctions.currentArray + 1;
            //Insert newly filtered array                
            ShowTable.arrayOfArrays.add(ReadFile.arrayOfLines); 
              
           /* 
            * This version stores the file with the genotype as the first array, not that good of an ideea
            FilterFunctions.filterName.add(0, "main array with genotypes");
            //change the count of current array
            FilterFunctions.currentArray = 0;
            //Insert newly filtered array                
            ShowTable.arrayOfArrays.add(0, ReadFile.arrayOfLines);
            */
            
            ShowTable.columns[numberGenotypes] = "QUAL~FILTER~INFO";
                System.arraycopy(headTemp, 1, ShowTable.columns, numberGenotypes + 1, headTemp.length - 1);
            
            /*
             * copy the columns into the columnNames for dataIntegrity
             */
                ShowTable.columnNames = new String[ShowTable.columns.length];
                
               for (int j = 0; j < ShowTable.columns.length; j++) {     
                    ShowTable.columnNames[j] = ShowTable.columns[j];
               }
            
            System.out.println("First genotype is: " + ShowTable.columns[0]);
            ShowTable.into2DArray(TempArray, end);
            
              } catch (IOException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        } 
 
        }       
   } 

}
   
//grab the lines that weren't found in VCF file:
/*public void printNotFound(){        
FileWriter out = null;
            try {
                out = new FileWriter("/Users/gerikson/Desktop/notFound.txt", true);

     for (int i=0; i<ReadFile.arrayOfLines.size(); i++ ) {
         String line =  ReadFile.arrayOfLines.get(i).fileRow;
         String l[] = line.split("\t");
         if (l[0].equals("N/A")) {
  
                 out.write(line);
                 out.write("\n");
           

         }
     
     }  
       
                out.close();   
                                 // ReadFile rf = new ReadFile(file);
                                  //threadExecutor = Executors.newFixedThreadPool(1);
                                 // threadExecutor.execute(rf);
                                 //    ShowTable.frame.dispose();                  
//                              //    ShowTable.frame.dispose();
            } catch (IOException ex) {
                System.out.print("Some stupid error");
                //Logger.getLogger(OpenVCF.class.getName()).log(Level.SEVERE, null, ex);
            }
                                    
                       
    }*/
 


public void vcfTransform(int chrom, String ref, String var, int begin, String importedGenotypes, String NAGen, String[] lin) {
                        
                       int end = 0;
                       String varType = null;
                    /*
                     * New parsing
                     */
                     int  start = 0;
                    for (int i=0; i < Math.min(var.length(),ref.length()); i++) {
                        if (var.charAt(i) == ref.charAt(i)) {
                            start = start+1 ;
                                    }            
                        else {
                            break;
                        }
                    }
                 //trim the head 
                    ref = ref.substring(start);
                    var = var.substring(start);
                    begin = begin + start;
                
                    
                //check the tail 
                  for (int i=1; i < Math.min(var.length(),ref.length()); i++) {
                      if (var.substring(var.length()-i-1, var.length()-i).equals(ref.substring(ref.length()-i-1,ref.length()-i))) {
                            end = end+1 ;
                                    }            
                   else {
                            break;
                        }
                  } 
                  
                    if (end != 0) {
                    ref = ref.substring(0, ref.length() - end);
                    var = var.substring(0, var.length() - end);
                    } 
                    
                    //is this a snp 
                   if (ref.length() == 1 && var.length() == 1) {
                       end = begin + 1;
                       varType = "snp";
                   } 
                   
                   else if (ref.length() == 0 && var.length() > 0) {
                       begin = begin + 1;
                       end = begin;
                       varType = "ins";
                       ref = "-";
                   }
                   
                   else if (ref.length() > 0 && var.length() == 0) {
                       varType = "del";
                       end = begin + ref.length();
                       var = "-";
                       
                   }
                   
                   else if (ref.length() > var.length() && (var.startsWith(".") || var.startsWith("-")) && !ref.contains("<")) {
                       int offset = var.length();
                       ref = ref.substring(offset);
                       begin = begin + offset;
                       end = begin + ref.length();
                       varType = "del";
                       var = "-";
                   }
                   else if (ref.length() > 0 && var.length() > 0 && !ref.equals(var)) {
                       ref = ref.replace("-", "");
                       varType = "delins";
                       end = begin + ref.length();
                   }
                        
                 compare(chrom, begin, end, varType, ref, var, importedGenotypes, NAGen, lin) ;    
}

/*
 * Good Working version with the previous parser
 */
/*public void vcfTransform(int chrom, String ref, String var, int begin, String importedGenotypes, String NAGen, String[] lin) {
                        
                       int end = 0;
                       String varType = null;
  
                    //is this a snp 
                   if (ref.length() == var.length() && ref.length() == 1) {
                        varType = "snp";
                        begin = begin - 1;
                        end = begin + 1;
                        
                    } else if (ref.length() == var.length() && ref.length() >1 ) {
                        varType = "snp";
                        begin = begin -  1;
                        end = begin + 1;
                    }
                    
                    //is this a deletion
                    else if (ref.length() > var.length()) {
                        varType = "del";
                        int size  = ref.length() - var.length();
                        end = begin + size;
                        ref = ref.substring(1);
                        var = "-";
                    }
                    // is this insertion
                    else if (ref.length() < var.length()) {
                        var = var.substring(1);
                        ref = "-";        
                        varType = "ins";
                        end = begin;
                    }
                 compare(chrom, begin, end, varType, ref, var, importedGenotypes, NAGen, lin) ;    
}*/

//compare VCF file to pipeline output file
public void compare(int chrom, int begin, int end, String varType, String ref, String var, String importedGenotypes, String NAgen, String[] lin){
                   
        
                    //How many time the line was skiped per this round
                    int runSkip = 0;
                   //compare to the original file 
           for (int i = startCompare; i < ReadFile.arrayOfLines.size(); i++) {
                 //this fixed one issue, make sure starting point is never bellow 0, extra check!
                 if (i<0)  {
                      i = 0;
                  }    
                  String originalLine = ReadFile.arrayOfLines.get(i).fileRow;
                 //make sure this line was not compared yet 
                 if(originalLine.startsWith("N/A")) {
                        String[] spLines = originalLine.split("\t");
                         String origBegin = spLines[3];
                        String m = origBegin.replace("chr", "");
                     if (m.equals("X")) {
                            m = "100";
                        } else if (m.equals("Y")) { m = "200";}
                        
                     
                        if (m.contains("N/A")) {
                      
                              startCompare = i-runSkip + 1;   
                              continue;
                        }
                        
                        int origChrom;
                        try {
                          origChrom = Integer.parseInt(m);   
                        } catch (Exception ex){
                     System.out.println( "exception");
                     continue;
                        }
                        
                        
                        
                        //check if it's same chromosome
                         if (chrom >= origChrom) {
                             if (chrom > origChrom ) {
                                 String newLine = NAgen + "\t" + "N/A" + originalLine.substring(3);
                                 ReadFile.arrayOfLines.get(i).fileRow = newLine;
                                 continue;
                             } else if (chrom == origChrom) {
                            // verify start position 
                             int origStart = Integer.parseInt(spLines[4]);
                            
                            if (begin > origStart || begin == origStart) {
                                if (begin > origStart) {
                                  String newLine = NAgen + "\t" + "N/A" + originalLine.substring(3);
                                  ReadFile.arrayOfLines.get(i).fileRow = newLine;
                                  /*
                                   * Need to store this in an array of not found
                                   */
                            //      System.out.println("original line: " + spLines[4] + "\t" + spLines[5] + "\t" + spLines[6] + "\t" + spLines[7]);
                             //     System.out.println("parsed line: " + begin + "\t"+ end+ "\t" + varType + "\t" + ref + "\t" + var);
                                  parsingError++;
                                  mainArrayNotFound.add(Integer.toString(i));     
                                  runSkip++;
                                  continue;
                        
                             } else if (begin == origStart) {
                                 
                               // verify end
                                 int origEnd = Integer.parseInt(spLines[5]);
                         
                                 if (end > origEnd || end == origEnd) {
                                     if (end > origEnd) {
                                         /*
                                          * Need to add this to the skiped runs, we will come back here
                                          */
                                         runSkip++;
                                         continue;
                                      } else if (end == origEnd) { 
                                     //verify vartype, reference and allele

                                if (varType.equals(spLines[6]) && ref.equals(spLines[7]) && var.equals(spLines[8])) {           
                      
                                         compareCounter++;
                                            
                           if (compareCounter%10000 == 0){
                               
                              // Loading.frame.dispose();
                               System.out.print(compareCounter + "\t");
                               DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                               Calendar cal = Calendar.getInstance();
                               System.out.println(dateFormat.format(cal.getTime()));

                           } 
                                         //add the imported genotype index to the first position of arrayOfLines
                                        String info = extractINFO(varType, lin[7]);
                                        String newLine = importedGenotypes + "\t" + lin[5].concat("~").concat(lin[6]).concat(info) + originalLine.substring(3);
                                        ReadFile.arrayOfLines.get(i).fileRow = newLine;
                                        startCompare = i-runSkip-1;
                                        
                                 
                                            break;

                                     } else {
                                  runSkip++;  
                                  continue;
                                }
                                 }} else {
                                 runSkip++;    
                                 continue;
                                 
                                 }
                            }

                            }else {  

                            
                             /*
                              * Need to store this in an object, compare them again with the list of not found
                              */
                              LostVCF ob1 = new LostVCF(chrom, begin,  end, varType, ref,  var, importedGenotypes, NAgen, lin); 
                             VCFobjectsArray.add(ob1);
                             VCFnotFound++;
                             startCompare = i-runSkip-1;
                             break;
                            }                          
                        } }else {break;}
                    } else {
    
               continue;
        }
             }
    }



public String extractINFO(String var, String IN) {
    String info = "";
    String[] splitINFO = IN.split(";");

    for (int i=0; i<splitINFO.length; i++) {
       if (var.contains("snp")) {
        if (splitINFO[i].contains("QD")) {
            info = info.concat("~").concat(splitINFO[i]);
        } else if (splitINFO[i].contains("MQ=")) {
            info = info.concat("~").concat(splitINFO[i]); 
        } else if (splitINFO[i].contains("DP=")) {
             info = info.concat("~").concat(splitINFO[i]); 
        } else if (splitINFO[i].contains("GQ=")) {
            info = info.concat("~").concat(splitINFO[i]);  
        } else if (splitINFO[i].contains("FS=")) {
            info = info.concat("~").concat(splitINFO[i]);  
        } else if (splitINFO[i].contains("HQ=") || splitINFO[i].contains("HQ=HaplotypeScore=")) {
            info = info.concat("~").concat(splitINFO[i]);
        } else if (splitINFO[i].contains("ReadPosRankSum=")) {
            info = info.concat("~").concat(splitINFO[i]);
        } 
            }
     else {
         if (splitINFO[i].contains("QD")) {
            info = info.concat("~").concat(splitINFO[i]);
        } else if (splitINFO[i].contains("ReadPosRankSum=")) {
            info = info.concat("~").concat(splitINFO[i]); 
        } else if (splitINFO[i].contains("InbreedingCoeff=")) {
             info = info.concat("~").concat(splitINFO[i]); 
        } else if (splitINFO[i].contains("FS=")) {
            info = info.concat("~").concat(splitINFO[i]);  
        }
    }
}
     return info;
}

public void missingVariants() throws IOException{
    //for testing purposes write to files
   /*  FileWriter fstream = new FileWriter("VCFNotFound.txt");
     BufferedWriter out = new BufferedWriter(fstream);
      
     FileWriter fstreamTwo = new FileWriter("ArrayNotFound.txt");
     BufferedWriter out2 = new BufferedWriter(fstreamTwo);
    */ 
    int found = 0;
    
    System.out.println("Not found " + VCFobjectsArray.size());
    for (int i = 0; i < VCFobjectsArray.size(); i++) {
        LostVCF ob1 = VCFobjectsArray.get(i);
        for (int j = 0; j < mainArrayNotFound.size(); j++) {
                //extract the number of line then etract that line from arrayOfLines
                 String iter = mainArrayNotFound.get(j).toString();
                 int s = Integer.parseInt(iter);
                
                 String originalLine;
                 if (s <ReadFile.arrayOfLines.size()) {
                 originalLine = ReadFile.arrayOfLines.get(s).fileRow;
                 } else {
                     break;
                 }
                 //make sure this line was not compared yet 
                 String[] spLines = originalLine.split("\t");
                 String origChrom = spLines[numberGeno+3];
                 String m = origChrom.replace("chr", "");
                     if (m.equals("X")) {
                            m = "100";
                        } else if (m.equals("Y")) { m = "200";
                        
                        }
                    
                     String c = Integer.toString(ob1.chr);

                    if (c.equals(m) && Integer.toString(ob1.begin).equals(spLines[numberGeno +4]) 
                            && Integer.toString(ob1.end).equals(spLines[numberGeno + 5]) 
                            && ob1.Vartype.equals(spLines[numberGeno + 6])  && ob1.ref.equals(spLines[numberGeno + 7]) && ob1.var.equals(spLines[numberGeno + 8])) {
                                        found++;
                                        String info = extractINFO(ob1.Vartype, ob1.lin[7]);
                                        String newLine = ob1.importedGenotypes + "\t" + ob1.lin[5].concat("~").concat(ob1.lin[6]).concat(info) + originalLine.substring(ob1.beginLeng);
                                        ReadFile.arrayOfLines.get(s).fileRow = newLine;
                                        break;
                        
                    }

        }
    }
    
    //System.out.println("Number of variants not found: " + (VCFnotFound - found));
}
	@Override
	public void run()
	{
		openFile();
	}
}

