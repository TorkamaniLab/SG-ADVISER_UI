/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ScrippsGenomeAdviserUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
public class Statistics implements Runnable{
        
      private int datacount = 0;  
            // SNP
      private int coding_snp=0;
      private int nonsense_snp=0;
      private int nonsyn_snp=0;
      private int syn_snp = 0;
      private int UTR_snp = 0;
      private int intron_snp = 0;
      private int tot_snp = 0;
      private int cons_snp = 0;
      private int TFBS_snp = 0;
      private int TFBS_snp_tot = 0;
      private int premiRNA_snp_tot = 0;
      private int splice_snp = 0;
      private int splice_snp_tot = 0;
      private int pmotif_snp  = 0;
      private int pmotif_snp_tot = 0;
      private int ESE_snp_del = 0;
      private int ESE_snp_tot = 0;
      private int ESE_snp_ind = 0; 
      private int ESS_snp_del = 0; 
      private int ESS_snp_tot = 0; 
      private int ESS_snp_ind = 0; 
      private int miRNA_snp_del = 0;
      private int miRNA_snp_created = 0;
      private int miRNA_snp_changed = 0;
      private int miRNA_snp_tot = 0;
      private int miRNA_genomic_snp = 0;
      private int trunc_snp = 0;
                    
        
        // INSERTIONS
      private int coding_ins = 0;
      private int fshift_ins = 0;
      private int out_frame_ins = 0;
      private int in_frame_ins = 0;
      private int UTR_ins = 0;
      private int intron_ins = 0;
      private int tot_ins = 0;
      private int cons_ins = 0;
      private int TFBS_ins = 0;
      private int TFBS_ins_tot = 0;
      private int premiRNA_ins_tot = 0;
      private int splice_ins = 0;
      private int splice_ins_tot = 0;
      private int pmotif_ins  = 0;
      private int pmotif_ins_tot = 0;
      private int ESE_ins_del = 0;
      private int ESE_ins_tot = 0;
      private int ESE_ins_ind = 0; 
      private int ESS_ins_del = 0; 
      private int ESS_ins_tot = 0; 
      private int ESS_ins_ind = 0; 
      private int miRNA_ins_del = 0;
      private int miRNA_ins_created = 0;
      private int miRNA_ins_changed = 0;
      private int miRNA_ins_tot = 0;
      private int miRNA_genomic_ins = 0;
      private int trunc_ins = 0;
        
        //DELETIONS
      private int coding_del = 0;
      private int fshift_del = 0;
      private int inter_codon_del = 0;
      private int in_frame_del = 0;
      private int UTR_del = 0;
      private int intron_del = 0;
      private int tot_del = 0;
      private int cons_del = 0;
      private int TFBS_del = 0;
      private int TFBS_del_tot = 0;
      private int premiRNA_del_tot = 0;
      private int splice_del = 0;
      private int splice_del_tot = 0;
      private int pmotif_del  = 0;
      private int pmotif_del_tot = 0;
      private int ESE_del_del = 0;
      private int ESE_del_tot = 0;
      private int ESE_del_ind = 0; 
      private int ESS_del_del = 0; 
      private int ESS_del_tot = 0; 
      private int ESS_del_ind = 0; 
      private int miRNA_del_del = 0;
      private int miRNA_del_created = 0;
      private int miRNA_del_changed = 0;
      private int miRNA_del_tot = 0;
      private int miRNA_genomic_del = 0;
      private int trunc_del = 0;
      
      //INSERT/DELETIONS
      private int coding_rear = 0;
      private int fshift_rear = 0;
      private int in_frame_rear = 0;
      private int UTR_rear = 0;
      private int intron_rear = 0;
      private int tot_rear = 0;
      private int cons_rear = 0;
      private int TFBS_rear = 0;
      private int TFBS_rear_tot = 0;
      private int premiRNA_rear_tot = 0;
      private int splice_rear = 0;
      private int splice_rear_tot = 0;
      private int pmotif_rear  = 0;
      private int pmotif_rear_tot = 0;
      private int ESE_rear_del = 0;
      private int ESE_rear_tot = 0;
      private int ESE_rear_ind = 0; 
      private int ESS_rear_del = 0; 
      private int ESS_rear_tot = 0; 
      private int ESS_rear_ind = 0; 
      private int miRNA_rear_del = 0;
      private int miRNA_rear_created = 0;
      private int miRNA_rear_changed = 0;
      private int miRNA_rear_tot = 0;
      private int miRNA_genomic_rear = 0;
      private int trunc_rear = 0;
            
      private boolean CONS_FOUND = false;
      private int cons_count = 0;
    //  private int TFBS = 0;
      //not sure what this is used for, all of the muRNA counter that are > 0 or value "DELETED"
      private int premiRNA_disrupt = 0;
      private int splice_site = 0;
      private int pmotif_major = 0;
      private int splice_change = 0;
      private int probably_damaging = 0;
      private int possibly_damaging = 0;
      private int nonsense = 0;
      private int nonsyn = 0;
      private int syn = 0;
      private int pmotif = 0;
      private int TOT_DISRUPT = 0;
      private int TOT_DISRUPT_NEW = 0;
      private int TOT_POSIB_DISRUPT = 0;
      private int SIFT_INTOLERANT = 0;
      private int SIFT_TOLERANT = 0;
      private int Condel_deletirious = 0;
      private int Condel_neutral = 0;
      
      boolean temp = false;
      boolean temp_splice = false;
      boolean PFAM = false;
      
      
      private static String[] header;
      private JFrame frame;
      private Container content;
      private JProgressBar progressBar;
      private Border border;
      private static int progBarCalc= 0;
      private ArrayList<String> ACMG1_genotype = new ArrayList<String>();
    
    public Statistics(String head, String s){
        
        datacount = 0;
        header = head.split("\t");
        
        // SNP
        coding_snp=0;
        nonsense_snp=0;
        nonsyn_snp=0;
        syn_snp = 0;
        UTR_snp = 0;
        intron_snp = 0;
        tot_snp = 0;
        cons_snp = 0;
        TFBS_snp = 0;
        TFBS_snp_tot = 0;
        premiRNA_snp_tot = 0;
        splice_snp = 0;
        splice_snp_tot = 0;
        pmotif_snp  = 0;
        pmotif_snp_tot = 0;
        ESE_snp_del = 0;
        ESE_snp_tot = 0;
        ESE_snp_ind = 0; 
        miRNA_snp_del = 0;
        miRNA_snp_created = 0;
        miRNA_snp_changed = 0;
        miRNA_snp_tot = 0;
        miRNA_genomic_snp = 0;
        trunc_snp = 0;
      
        // INSERTIONS
        coding_ins = 0;
        fshift_ins = 0;
        out_frame_ins = 0;
        in_frame_ins = 0;
        UTR_ins = 0;
        intron_ins = 0;
        tot_ins = 0;
        cons_ins = 0;
        TFBS_ins = 0;
        TFBS_ins_tot = 0;
        premiRNA_ins_tot = 0;
        splice_ins = 0;
        splice_ins_tot = 0;
        pmotif_ins  = 0;
        pmotif_ins_tot = 0;
        ESE_ins_del = 0;
        ESE_ins_tot = 0;
        miRNA_ins_del = 0;
        miRNA_ins_created = 0;
        miRNA_ins_changed = 0;
        miRNA_ins_tot = 0;
        miRNA_genomic_ins = 0;
        trunc_ins = 0;
        
        //DELETIONS
        coding_del = 0;
        fshift_del = 0;
        inter_codon_del = 0;
        in_frame_del = 0;
        UTR_del = 0;
        intron_del = 0;
        tot_del = 0;
        TFBS_del = 0;
        TFBS_del_tot = 0;
        premiRNA_del_tot = 0;
        splice_del = 0;
        splice_del_tot = 0;
        pmotif_del  = 0;
        pmotif_del_tot = 0;
        ESE_del_del = 0;
        ESE_del_tot = 0;
        miRNA_del_del = 0;
        miRNA_del_created = 0;
        miRNA_del_changed = 0;
        miRNA_del_tot = 0;
        miRNA_genomic_del = 0;
        trunc_del = 0;
        
        //INSERT/DELETIONS
        coding_rear = 0;
        fshift_rear = 0;
        in_frame_rear = 0;
        UTR_rear = 0;
        intron_rear = 0;
        tot_rear = 0;
        TFBS_rear = 0;
        TFBS_rear_tot = 0;
        premiRNA_rear_tot = 0;
        splice_rear = 0;
        splice_rear_tot = 0;
        pmotif_rear  = 0;
        pmotif_rear_tot = 0;
        ESE_rear_del = 0;
        ESE_rear_tot = 0;
        miRNA_rear_del = 0;
        miRNA_rear_created = 0;
        miRNA_rear_changed = 0;
        miRNA_rear_tot = 0;
        miRNA_genomic_rear = 0;
        trunc_rear = 0;
        
        CONS_FOUND = false;
        cons_count = 0;
   //     TFBS = 0;
        premiRNA_disrupt = 0;
        splice_site = 0;
        pmotif_major = 0;
        splice_change = 0;
        probably_damaging = 0;
        possibly_damaging = 0;
        nonsense = 0;
        nonsyn = 0;
        syn = 0;
        pmotif = 0;
        TOT_DISRUPT = 0;
        TOT_DISRUPT_NEW = 0;
        TOT_POSIB_DISRUPT = 0;
        SIFT_INTOLERANT = 0;
        SIFT_TOLERANT = 0;
        Condel_deletirious = 0;
        Condel_neutral = 0;
        temp = false;
        temp_splice = false;
        PFAM = false;
       
         frame = new JFrame("Statistics");
         content = frame.getContentPane();
         progressBar = new JProgressBar();
         border = BorderFactory.createTitledBorder(s);
         progressBar.setBorder(border);
         content.add(progressBar, BorderLayout.NORTH);
         frame.setSize(300, 100);
         progBarCalc = 0;
    }
    
    private int getHeaderIndex(String var) {
        int index = 0;
        for (int i = 0; i<header.length; i++) {
         //   if (header[i].contains(var)) {
             if (header[i].equals(var)) {
                index = i;
            }
        }
        
        return index;
    }
    
        //get the info in some column
    private String getColum(String var) {
    		String value = "";
            for (int i = 0; i<header.length; i++) {
                if (header[i].contains(var)) {
                    value = header[i];
                    break;
                }   
        }
               return value;    
    }
    
        
    private void ACMG_genotype(String[] spLine) {
    	if (spLine[getHeaderIndex("ACMG_Score_Clinical")].contains("1~")){
                   //extract the genotype
    		 String value = spLine[getHeaderIndex("Notes")];
    		 value = value.replace("Genotype=", "");
    		 String[] spValue = value.split(":");
    		 for (int ij =0; ij <spValue.length; ij++ ) {
                     //if this is homozaigous or heterozaigous
                     if (spValue[ij].equals("1/0") || spValue[ij].equals("1|0") 
                             || spValue[ij].equals("1/1") || spValue[ij].equals("1|1")) {
                             
                             int tempValue = Integer.parseInt(ACMG1_genotype.get(ij));
                             tempValue ++;
                            
                         }
    		}
    }
 
    }           
    /*
     * This method is for when the user tries to calculate the statistics before loading in a file in the UI
     */
    private void calculateStatsFile(File file) throws IOException {
        String line = "";
        String[] head;
     
        boolean match;
        BufferedReader bReader = null;
        try {
            bReader = new BufferedReader(
                    new FileReader(file));
        } catch (IOException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
                   try {
             line = bReader.readLine();
                } catch (IOException ex) {
                    Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null, ex);
                }

                   for (int i = 0; i < 1; i++) {
                       head = line.split("\t");
                   }
                          long fileInBytes = file.length();
                          System.out.println("Filelenght is: " + fileInBytes);
                          //calculating how many lines on average 3 percent would be,
                          //1358 is the size of each line in bytes
                          int threePerc = (int) ((fileInBytes/1358)/100)*3; 
                    
                 while((line = bReader.readLine()) != null) {

                          datacount++;
                          
                          if (datacount % threePerc == 0) {
                                frame.setVisible(true);
                                progBarCalc = progBarCalc + 3;
                                progressBar.setValue(progBarCalc);
                                progressBar.setStringPainted(true);
                                System.out.print(datacount + "\t");
                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                Calendar cal = Calendar.getInstance();
                                System.out.println(dateFormat.format(cal.getTime()));
                          }
         
                          String[] spLine = line.split("\t");
                /*          int[] i  = Polythen(spLine);
                          int b = Coding_Impact(spLine);
                          int c = calcStats(spLine);
                          int d = Sift(spLine);
                          int e = Condel(spLine);
                          int f = Truncation(spLine);
                          /*
                           * if either column a Functional variant I add to total disrupt only once
                           */
                    /*      if (i[0]==1 || b ==1 || c == 1) {
                              TOT_DISRUPT_NEW ++;
                          }
                          
                          if (i[1]==1||d==1 || e == 1 || temp_splice || PFAM || f==1) {
                              TOT_POSIB_DISRUPT++;
                              temp_splice = false;
                              PFAM = false;
                          } */
                          
                          int[] a  = Polythen(spLine);
                          int b = Coding_Impact(spLine);
                          int c = calcStats(spLine);
                          int d = Sift(spLine);
                          int e = Condel(spLine);
                          int f = Truncation(spLine);

                           /*
                           * if either column a Functional variant I add to total disrupt only once
                           */
                          if (a[1]==1||d==1 || e == 1 || temp_splice || PFAM || f==1) {
                              TOT_POSIB_DISRUPT++;
                              temp_splice = false;
                              PFAM = false;
                          }

                          if (a[0]==1 || b ==1 || c == 1) {
                              TOT_DISRUPT_NEW ++;
                          }
        }
        
        writeToFile();
        frame.dispose();
        
        System.out.println("Number of lines is: " + datacount);
        System.out.println("Each line contains raughly: " + fileInBytes/datacount + " bytes");
    }
    
    /*
     * This method is for when the file is loaded in already
     */
    
    private void calculateStatsArray(ArrayList<Reader> TempArray) throws IOException{

     /*   int varType = getHeaderIndex("VarType");
        int codingImpact = getHeaderIndex("Coding_Impact"); */
        String line;
      
        for (int i = 0; i<TempArray.size(); i++) {
            
                            int end = TempArray.size();
               //progress bar calculations
               int lineCount = 0;
               int linesLeft = end;
               int threePerc = (int) (linesLeft/100)*3; 
                if (threePerc < 1) {threePerc = 1; }
               SaveProgress pr = new SaveProgress();

            datacount ++;
            //check the percentage, if true increment the progress bar
            if (datacount % threePerc == 0) {
                    frame.setVisible(true);
                    progBarCalc = progBarCalc + 3;
                    progressBar.setValue(progBarCalc);
                    progressBar.setStringPainted(true);
                System.out.print(i + "\t");
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                System.out.println(dateFormat.format(cal.getTime()));
            }
        
            line = TempArray.get(i).fileRow;
            
            String[] spLine = line.split("\t");
           
            /*
            polythen(spLine);
            Coding_Impact(spLine);
            calcStats(spLine);
            */
                          int[] a  = Polythen(spLine);
                          int b = Coding_Impact(spLine);
                          int c = calcStats(spLine);
                          int d = Sift(spLine);
                          int e = Condel(spLine);
                          int f = Truncation(spLine);

                           /*
                           * if either column a Functional variant I add to total disrupt only once
                           */
                          if (a[1]==1||d==1 || e == 1 || temp_splice || PFAM || f==1) {
                              TOT_POSIB_DISRUPT++;
                              temp_splice = false;
                              PFAM = false;
                          }

                          if (a[0]==1 || b ==1 || c == 1) {
                              TOT_DISRUPT_NEW ++;
                          }
        }
        
        writeToFile();
        frame.dispose();
    }
    
    private int calcStats (String[] spLine) {
            int i = 0;
            /*
             * counting SNPs
             * Nonsense -> Nonsynonymous -> Synonymous -> 5UTR or 3UTR ->Intron
             */
            if (spLine[getHeaderIndex("VarType")].contains("snp")){
                tot_snp++;
                if (spLine[getHeaderIndex("Coding_Impact")].contains("Nonsense")){
                coding_snp++;
                nonsense_snp++;
                } else if (spLine[getHeaderIndex("Coding_Impact")].contains("Nonsynonymous")) {
                coding_snp++;
                nonsyn_snp++;
                }  else if (spLine[getHeaderIndex("Coding_Impact")].contains("Synonymous")) {
                coding_snp++;
                syn_snp++;
                } else if (spLine[getHeaderIndex("Location")].contains("5UTR") || spLine[getHeaderIndex("Coding_Impact")].contains("3UTR")) {
                UTR_snp++;
                } else if (spLine[getHeaderIndex("Location")].contains("Intron")) {
                intron_snp++;
                }
                
                if (Truncation(spLine) == 1){
                trunc_snp ++;
                }
                // Calculate if this is a conserved snp
                if (CONS_FOUND_Fuction(spLine)) {
                    cons_snp++;
                }
                
                //TFBS
                if (TFBS_calc(spLine)) {
                    TFBS_snp_tot++;
                    if (temp) {
                        i = 1;
                        temp = false;
                    }
                }
                
                //miRNA
                if (premiRNA(spLine)) {
                      i = 1;
                    premiRNA_snp_tot++;
                }
                
                //spliceChange
                if  (splicingChange(spLine)) {
                      i = 1;
                    splice_snp++;
                    splice_snp_tot++;
                }
                
                //Protein_Domein_Impact
                if  (Protein_Domains_Impact(spLine)) {
                      i = 1;
                    pmotif_snp_tot++;
                }
                
                if (ESE_sites(spLine) == 1) {
                    ESE_snp_del++; 
                    i =1;
                    ESE_snp_tot++;
                } else if (ESE_sites(spLine) == 2) {
                    ESE_snp_ind++; 
                    i = 1;
                    ESE_snp_tot++;
                }
                
                if (ESS_sites(spLine) == 1) {
                    ESS_snp_del++;
                    i = 1;
                    ESS_snp_tot++;
                } else if (ESS_sites(spLine) == 2) {
                    ESS_snp_ind++;
                    i = 1;
                    ESS_snp_tot++;
                }
                     
                if (miRNA(spLine) == 1) {
                    miRNA_snp_del++;
                    i = 1;
                    miRNA_snp_tot++;
                } else if (miRNA(spLine) == 2) { 
                    miRNA_snp_created++;
                    miRNA_snp_tot++;
                } else if (miRNA(spLine) == 3) { 
                    miRNA_snp_changed++;
                    miRNA_snp_tot++;
                }
                
                if (miRNA_genomic(spLine) == 1) {
                    miRNA_genomic_snp++; 
                }
                
            } 
            
            
            /*
             * DELETIONS/INSERTIONS
             * Frameshift ->In_Frame_Rearrangement ->5UTR or 3UTR ->Intro
             */
            else if (spLine[getHeaderIndex("VarType")].contains("delins")){
                tot_rear++;
                if (spLine[getHeaderIndex("Coding_Impact")].contains("Frameshift")){
                coding_rear++;
                fshift_rear++;
                } else if (spLine[getHeaderIndex("Coding_Impact")].contains("In_Frame_Rearrangement")){
                in_frame_rear++;
                coding_rear++;
                } else if (spLine[getHeaderIndex("Location")].contains("5UTR") || spLine[getHeaderIndex("Location")].contains("3UTR")) {
                UTR_rear++;
                } else if (spLine[getHeaderIndex("Location")].contains("Intron")) {
                intron_rear++;
                }
                
                // Calculate if this is a conserved snp
                if (CONS_FOUND_Fuction(spLine)) {
                    cons_rear++;
                }
                if (TFBS_calc(spLine)) {
                    TFBS_rear_tot++;
                }
                
                if (Truncation(spLine) == 1){
                trunc_rear ++;
                }
                                      
                 //miRNA
                if (premiRNA(spLine)) {
                      i = 1;
                    premiRNA_rear_tot++;
                }
                
                //spliceChange
                if  (splicingChange(spLine)) {
                      i = 1;
                    splice_rear++;
                    splice_rear_tot++;
                }
                
                //Protein_Domain_Impact
                if  (Protein_Domains_Impact(spLine)) {
                      i = 1;
                    pmotif_rear_tot++;
                }
                
                if (ESE_sites(spLine) == 1) {
                    ESE_rear_del++; 
                    ESE_rear_tot++;
                } else if (ESE_sites(spLine) == 2) {
                    ESE_rear_ind++; 
                    ESE_rear_tot++;
                }
                
                if (ESS_sites(spLine) == 1) {
                    ESS_rear_del++; 
                    ESS_rear_tot++;
                } else if (ESS_sites(spLine) == 2) {
                    ESS_rear_ind++; 
                    ESS_rear_tot++;
                }
                if (miRNA(spLine) == 1) {
                    miRNA_rear_del++; 
                    miRNA_rear_tot++;
                } else if (miRNA(spLine) == 2) {
                    miRNA_rear_created++;
                    miRNA_rear_tot++;
                } else if (miRNA(spLine) == 3) { 
                    miRNA_rear_changed++;
                    miRNA_rear_tot++;
                }
                
                if (miRNA_genomic(spLine) == 1) {
                    miRNA_genomic_rear++; 
                }
            }            
            
             /*
             * INSERTIONS
             * Frameshift -> InterCodon_In_Frame_Insertion ->In_Frame_Insertion ->5UTR or 3UTR ->Intron
             */
            
            else if (spLine[getHeaderIndex("VarType")].contains("ins")){
                tot_ins++;
                if (spLine[getHeaderIndex("Coding_Impact")].contains("Frameshift")){
                coding_ins++;
                fshift_ins++;
                } else if (spLine[getHeaderIndex("Coding_Impact")].contains("InterCodon_In_Frame_Insertion")){
                out_frame_ins++;
                coding_ins++;
                } else if (spLine[getHeaderIndex("Coding_Impact")].contains("In_Frame_Insertion")){
                in_frame_ins++;
                coding_ins++;
                } else if (spLine[getHeaderIndex("Location")].contains("5UTR") || spLine[getHeaderIndex("Location")].contains("3UTR")) {
                UTR_ins++;
                } else if (spLine[getHeaderIndex("Location")].contains("Intron")) {
                intron_ins++;
                } 
                
                // Calculate if this is a conserved ins
                if (CONS_FOUND_Fuction(spLine)) {
                    cons_ins++;
                }
                
                if (Truncation(spLine) == 1){
                trunc_ins ++;
                }
                
                if (TFBS_calc(spLine)) {
                    TFBS_ins_tot++;
                }
                
                //miRNA
                if (premiRNA(spLine)) {
                      i = 1;
                    premiRNA_ins_tot++;
                }
                
                //spliceChange
                if  (splicingChange(spLine)) {
                      i = 1;
                    splice_ins++;
                    splice_ins_tot++;
                }
                
                //Protein_Domain_Impact
                if  (Protein_Domains_Impact(spLine)) {
                      i = 1;
                    pmotif_ins_tot++;
                }
                
                if (ESE_sites(spLine) == 1) {
                    ESE_ins_del++; 
                    ESE_ins_tot++;
                } else if (ESE_sites(spLine) == 2) {
                    ESE_ins_ind++; 
                    ESE_ins_tot++;
                }
                if (ESS_sites(spLine) == 1) {
                    ESS_ins_del++; 
                    ESS_ins_tot++;
                } else if (ESS_sites(spLine) == 2) {
                    ESS_ins_ind++; 
                    ESS_ins_tot++;
                }
                if (miRNA(spLine) == 1) {
                    miRNA_ins_del++; 
                    miRNA_ins_tot++;
                } else if (miRNA(spLine) == 2) {
                    miRNA_ins_created++;
                    miRNA_ins_tot++;
                } else if (miRNA(spLine) == 3) { 
                    miRNA_ins_changed++;
                    miRNA_ins_tot++;
                }
                
                 if (miRNA_genomic(spLine) == 1) {
                    miRNA_genomic_ins++; 
                }
            }
            
            /*
            * DELETIONS
            * Frameshift ->In_Frame_Deletion_One_Altered_Codon ->In_Frame_Deletion->5UTR or 3UTR ->Intron
            */
             else if (spLine[getHeaderIndex("VarType")].contains("del")){
                tot_del++;
                if (spLine[getHeaderIndex("Coding_Impact")].contains("Frameshift")){
                coding_del++;
                fshift_del++;
                } else if (spLine[getHeaderIndex("Coding_Impact")].contains("In_Frame_Deletion_One_Altered_Codon")){
                inter_codon_del++;
                coding_del++;
                } else if (spLine[getHeaderIndex("Coding_Impact")].contains("In_Frame_Deletion")){
                in_frame_del++;
                coding_del++;
                } else if (spLine[getHeaderIndex("Location")].contains("5UTR") || spLine[getHeaderIndex("Location")].contains("3UTR")) {
                UTR_del++;
                } else if (spLine[getHeaderIndex("Location")].contains("Intron")) {
                intron_del++;
                } 

                // Calculate if this is a conserved del
                if (CONS_FOUND_Fuction(spLine)) {
                    cons_del++;
                }
                
                if (TFBS_calc(spLine)) {
                    TFBS_del_tot++;
                }
                if (Truncation(spLine) == 1){
                trunc_del ++;
                }
                 //miRNA
                if (premiRNA(spLine)) {
                    i = 1;
                    premiRNA_del_tot++;
                }
                
                 //spliceChange
                if  (splicingChange(spLine)) {
                      i = 1;
                    splice_del++;
                    splice_del_tot++;
                }
                
                 //Protein_Domain_Impact
                if  (Protein_Domains_Impact(spLine)) {
                      i = 1;
                    pmotif_del_tot++;
                }
                
               if (ESE_sites(spLine) == 1) {
                    ESE_del_del++; 
                    ESE_del_tot++;
                } else if (ESE_sites(spLine) == 2) {
                    ESE_del_ind++; 
                    ESE_del_tot++;
                }
               
                if (ESS_sites(spLine) == 1) {
                    ESS_del_del++; 
                    ESS_del_tot++;
                } else if (ESS_sites(spLine) == 2) {
                    ESS_del_ind++; 
                    ESS_del_tot++;
                }
                if (miRNA(spLine) == 1) {
                    miRNA_del_del++; 
                    miRNA_del_tot++;
                } else if (miRNA(spLine) == 2) { 
                    miRNA_del_created++;
                    miRNA_del_tot++;
                } else if (miRNA(spLine) == 3) { 
                    miRNA_del_changed++;
                    miRNA_del_tot++;
                }
                
                if (miRNA_genomic(spLine) == 1) {
                    miRNA_genomic_del++; 
                }
            }
            
        return i;    
    }
    
    private void writeToFile() throws IOException{
           BufferedWriter outfile = new BufferedWriter(
                                 new FileWriter("Statistics.txt", true)); 
           if (FilterFunctions.filterName.isEmpty()) {
              String fName = Interface.file.getName();        
              outfile.write("\t" + "\t".concat("STATISTICS ").concat(fName) +'\n');        
            } else {   
           outfile.write("\t" + "\t".concat("STATISTICS ").concat(FilterFunctions.filterName.get(FilterFunctions.currentArray)) +'\n');
                  }
           outfile.write("Coding SNPs:\t" + "\t"+ Integer.toString(coding_snp) + "\n");
           outfile.write("Nonsynonymous SNPs:\t" + "\t" + Integer.toString(nonsyn_snp) + "\n");
           outfile.write("Synonymous SNPs:\t" + "\t" + Integer.toString(syn_snp) + "\n");
           outfile.write("Nonsense SNPs:\t" + "\t" + Integer.toString(nonsense_snp) + "\n");
           outfile.write("Untranslated Region SNPs:\t" + "\t" + Integer.toString(UTR_snp) + "\n");
          // outfile.write("Non-coding RNA SNPs:\t" + "\t" + Integer.toString + "\n")
           outfile.write("Intronic SNPs:\t" + "\t" + Integer.toString(intron_snp) + "\n");
           outfile.write("Damaging Truncations SNPs:\t" + "\t" + Integer.toString(trunc_snp) + "\n");
          // outfile.write("Intergenic SNPs:\t" + "\t".join(str(x) for x in ingenic_snp_tracker) + "\n")
           outfile.write("Total SNPs:\t" + "\t" + Integer.toString(tot_snp) + "\n");

           outfile.write("Coding Insertions:\t" + "\t" + Integer.toString(coding_ins) + "\n");
           outfile.write("In-frame Insertions:\t" + "\t" + Integer.toString(in_frame_ins) + "\n");
           outfile.write("Out-of-frame Insertions:\t" + "\t" + Integer.toString(out_frame_ins) + "\n");
           outfile.write("Frameshift Insertions:\t" + "\t" + Integer.toString(fshift_ins) + "\t" + "\n");
           outfile.write("Untranslated region Insertions:\t" + "\t" + Integer.toString(UTR_ins) + "\n");
        //   outfile.write("Non-coding RNA Insertions:\t" + "\t" + Integer.toString() + "\n")
           outfile.write("Intronic Insertions:\t" + "\t" + Integer.toString(intron_ins) + "\n");
           outfile.write("Damaging Truncations Insertions:\t" + "\t" + Integer.toString(trunc_ins) + "\n");
         //  outfile.write("Intergenic Insertions:\t" + "\t" + Integer.toString(ingenic_ins) + "\n");
           outfile.write("Total Insertions:\t" + "\t" + Integer.toString(tot_ins) + "\n");

           outfile.write("Coding Deletions:\t" + "\t" + Integer.toString(coding_del) + "\n");
           outfile.write("In-frame Deletions:\t" + "\t" + Integer.toString(in_frame_del) + "\n");
           outfile.write("Inter-Codon Deletions:\t" + "\t" + Integer.toString(inter_codon_del) + "\n");
           outfile.write("Frameshift Deletions:\t" + "\t" + Integer.toString(fshift_del) + "\n");
           outfile.write("Untranslated region Deletions:\t" + "\t" + Integer.toString(UTR_del) + "\n");
           //outfile.write("Non-coding RNA Deletions:\t" + "\t"+ Integer.toString(noncoding_del) + "\n");
           outfile.write("Intronic Deletions:\t" + "\t" + Integer.toString(intron_del) + "\n");
           outfile.write("Damaging Truncations deletions:\t" + "\t" + Integer.toString(trunc_del) + "\n");
           //outfile.write("Intergenic Deletions:\t" + "\t" + Integer.toString(ingenic_del) + "\n");
           outfile.write("Total Deletions:\t" + "\t" + Integer.toString(tot_del) + "\n");

           outfile.write("Coding rearrangements:\t" + "\t" + Integer.toString(coding_rear) + "\n");
           outfile.write("In-frame rearrangements:\t" + "\t" + Integer.toString(in_frame_rear) + "\n");
           outfile.write("Frameshift rearrangements:\t" + "\t" + Integer.toString(fshift_rear) + "\n");
           outfile.write("Untranslated region rearrangements:\t" + "\t" + Integer.toString(UTR_rear) + "\n");
           //outfile.write("Non-coding RNA rearrangements:\t" + "\t" + Integer.toString(noncoding_rear) + "\n")
           outfile.write("Intronic rearrangements:\t" + "\t" + Integer.toString(intron_rear) + "\n");
           outfile.write("Damaging Truncations rearangements:\t" + "\t" + Integer.toString(trunc_rear) + "\n");
           //outfile.write("Intergenic rearrangements:\t" + "\t" + Integer.toString(ingenic_rear) + "\n");
           outfile.write("Total rearrangements:\t" + "\t" + Integer.toString(tot_rear) + "\n");

           outfile.write("Total number of variants:\t" + "\t" + datacount + "\n");
           
           outfile.write("\n");
           
        /*   outfile.write("\n");
           outfile.write("-------------TOTALS-------------");
           outfile.write("\n"); */
           outfile.write("Nonsense SNPs:\t" + "\t" + Integer.toString(nonsense_snp) + "\n");
           if (coding_snp != 0) {
               double test = (double) nonsense_snp/coding_snp;
                outfile.write("Rate Nonsense SNPs:\t" + "\t" + String.format("%.5g%n", test) + "\n");
           } else {
               outfile.write("Rate:\tN/A\n");
           }
           outfile.write("Frameshift Structural Variants:\t" + "\t" + (fshift_del + fshift_ins + fshift_rear) + "\n");
           int c = coding_ins + coding_del + coding_rear; 
           if (c != 0) { 
                double d = (double) (fshift_del + fshift_ins + fshift_rear)/c;
                outfile.write("Rate:\t" + "\t" + String.format("%.5g%n",d) + "\n");
           } else {
           outfile.write("Rate:\tN/A\n");
           }
           /*
           outfile.write("Frameshift Insertions:\t" + fshift_ins + "\n");
           if (coding_ins != 0) {
               double d = (double) fshift_ins/coding_ins;
            outfile.write("Rate:\t" + String.format("%.5g%n",d) + "\n");
           } else {
            outfile.write("Rate:\tN/A\n");
            }
           outfile.write("Frameshift Deletions:\t" + fshift_del + "\n");
           if (coding_del != 0) {
               double d = (double) fshift_del/coding_del;
               outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           } else {    outfile.write("Rate:\tN/A\n"); }
           outfile.write("Frameshift Rearangements:\t" + fshift_rear + "\n");
           if (coding_rear != 0) {
               double d = (double) fshift_rear/coding_rear;
               outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           } else {    outfile.write("Rate:\tN/A\n"); }
           
           */
           outfile.write("Splicing Change Variants:\t" + "\t" + splice_change + "\n");
           if (splice_site != 0 ) { 
               double d = (double) splice_change/splice_site;
               outfile.write("Rate:\t" + "\t" + String.format("%.5g%n", d) + "\n");
           } else {
               outfile.write("Rate:\tN/A\n");
                   }  
           outfile.write("Probably Damaging nscSNPs:\t" + "\t" + probably_damaging + "\n");
           if (nonsyn != 0) {
               double d = (double) probably_damaging/nonsyn;
               outfile.write("Rate:\t" + "\t" + String.format("%.5g%n", d) + "\n");
           } else {
           outfile.write("Rate:\tN/A\n");
           }
           outfile.write("Possibly Damaging nscSNPs:\t" + "\t" + possibly_damaging + "\n");
           if (nonsyn != 0) {
               double d = (double) possibly_damaging/nonsyn;
               outfile.write("Rate:\t" + "\t" + String.format("%.5g%n", d) + "\n");
           } else {
           outfile.write("Rate:\tN/A\n"); }
           outfile.write("Protein motif damaging Variants:\t" + "\t" + pmotif_major + "\n");
           if (pmotif != 0) {
               double d = (double) pmotif_major/pmotif;
               outfile.write("Rate:\t" + "\t" + String.format("%.5g%n", d) + "\n");
           } else {
           outfile.write("Rate:\tN/A\n");
                   }    
           outfile.write("Protein Impact Prediction(SIFT INTOLERANT) Variants:\t" + "\t" + SIFT_INTOLERANT + "\n");
           outfile.write("Protein Impact Prediction(SIFT TOLERANT) Variants:\t" + "\t" + SIFT_TOLERANT + "\n");
           outfile.write("Protein Impact Prediction(Condel deletirious) Variants:\t" + "\t" + Condel_deletirious + "\n");
           outfile.write("Protein Impact Prediction(Condel neutral) Variants:\t" + "\t" + Condel_neutral + "\n");
           
           outfile.write("TFBS Variants:\t" + "\t" + (TFBS_snp_tot + TFBS_ins_tot + TFBS_del_tot + TFBS_rear_tot) + "\n\n");
           outfile.write("TFBS Disrupting Variants:\t" + "\t" + (TFBS_snp + TFBS_ins + TFBS_del + TFBS_rear) + "\n\n");
           int a = TFBS_snp_tot + TFBS_ins_tot + TFBS_del_tot + TFBS_rear_tot;
           if (a != 0) {
               double d = (double) (TFBS_snp + TFBS_ins + TFBS_del + TFBS_rear)/a;
               outfile.write("Rate:\t" + "\t" + String.format("%.5g%n", d)  + "\n");
           } else {
           outfile.write("Rate:\tN/A\n");
           }
           outfile.write("pre-miRNA Disrupting Variants:\t" + "\t" + premiRNA_disrupt + "\n");
           int tot_number_variants = tot_snp + tot_ins + tot_del + tot_rear;
           if (tot_number_variants != 0) {
               double t =  (double) premiRNA_disrupt/tot_number_variants;
               outfile.write("Rate:\t" + "\t" + String.format("%.5g%n", t) + "\n");
           } else {
           outfile.write("Rate:\tN/A\n");
           }
           
           int miRNA_tot = miRNA_snp_tot + miRNA_del_tot + miRNA_ins_tot + miRNA_rear_tot;
           outfile.write("miRNA-BS Variants:\t" + "\t" + miRNA_tot + "\n");
           outfile.write("miRNA-BS Disrupting Variants:\t" + "\t" + (miRNA_snp_del + miRNA_del_del + miRNA_ins_del + miRNA_rear_del) + "\n");
           if ( miRNA_tot != 0) {
           double t =  (double) (miRNA_snp_del + miRNA_del_del + miRNA_ins_del + miRNA_rear_del)/miRNA_tot;
           outfile.write("Rate:\t" + "\t" + String.format("%.5g%n", t) + "\n");
           } else {
           outfile.write("Rate:\tN/A\n");
           }
           outfile.write("miRNA-BS CREATED Variants:\t" + "\t" + (miRNA_snp_created + miRNA_del_created + miRNA_ins_created + miRNA_rear_created) + "\n");
           if (miRNA_tot!=0) {
           double t =  (double) (miRNA_snp_created + miRNA_del_created + miRNA_ins_created + miRNA_rear_created)/miRNA_tot;
           outfile.write("Rate:\t" + "\t" + String.format("%.5g%n", t) + "\n");
           } else {
           outfile.write("Rate:\tN/A\n");
           } 
           outfile.write("miRNA-BS CHANGED Variants:\t" + "\t" + (miRNA_snp_changed + miRNA_del_changed + miRNA_ins_changed + miRNA_rear_changed) + "\n");
           if (miRNA_tot!=0) {
           double t =  (double) (miRNA_snp_changed + miRNA_del_changed + miRNA_ins_changed + miRNA_rear_changed)/miRNA_tot;
           outfile.write("Rate:\t" + "\t" + String.format("%.5g%n", t) + "\n");
           } else {
           outfile.write("Rate:\tN/A\n");
           } 
           
           outfile.write("miRNA genomic Variants:\t" + "\t" + (miRNA_genomic_snp + miRNA_genomic_del + miRNA_genomic_ins + miRNA_genomic_rear) + "\n\n");
       
           outfile.write("Total Likely Functional Variants:\t" + "\t" + TOT_DISRUPT + "\n");
           if (datacount != 0) {
           double t = (double) TOT_DISRUPT/datacount;    
           outfile.write("Rate:\t" + "\t" + String.format("%.5g%n", t) + "\n");
           } else {
           outfile.write("Rate:\tN/A\n");
           }
           
           outfile.write("Total Potential Functional Variants:\t" + "\t" + TOT_DISRUPT_NEW + "\n");
           if (datacount != 0) {
           double t = (double) TOT_DISRUPT_NEW/datacount;    
           outfile.write("Rate:\t" + "\t" + String.format("%.5g%n", t) + "\n");
           } else {
           outfile.write("Rate:\tN/A\n");
           }
           outfile.write("Total Likely Functional Variants:\t" + "\t" + TOT_POSIB_DISRUPT + "\n");
           if (datacount != 0) {
           double t = (double) TOT_POSIB_DISRUPT/datacount;    
           outfile.write("Rate:\t" + "\t" + String.format("%.5g%n", t) + "\n");
           } else {
           outfile.write("Rate:\tN/A\n");
           }
         
           
           outfile.write("-------------SNPs-------------");
           outfile.write("\n");
           outfile.write("Conserved Element SNPs:\t" + "\t" + cons_snp + "\n\n");
           outfile.write("TFBS SNPs:\t" + "\t" + TFBS_snp_tot + "\n");
           outfile.write("TFBS disrupting SNPs:\t" + "\t" + TFBS_snp + "\n");
           if (TFBS_snp_tot != 0) {
              // System.out.println("TFBS_snp_tot = " + TFBS_snp_tot);
                double d = (double) TFBS_snp/TFBS_snp_tot;
              //  System.out.println("Rate snps is:" + c);
                outfile.write("Rate TFBS SNPs:\t" + String.format("%.5g%n", d) + "\n");
           } else { 
                outfile.write("Rate:\tN/A\n");
            }
           outfile.write("pre-miRNA Disrupting SNPS:\t" + "\t" + premiRNA_snp_tot + "\n");
           if (tot_snp != 0) {
                double d = (double) premiRNA_snp_tot/tot_snp;
                outfile.write("Rate pre-miRNA Disrupting SNPs:\t" + String.format("%.5g%n", d) + "\n");
           } else  { 
                outfile.write("Rate:\tN/A\n");
            }
           
           outfile.write("miRNA-BS SNPs:\t" + "\t" + miRNA_snp_tot + "\n");
           outfile.write("miRNA-BS disrupting SNPs:\t" + "\t" + miRNA_snp_del + "\n");
           if (miRNA_snp_tot != 0) {
                   double d = (double) miRNA_snp_del/miRNA_snp_tot;
                  // outfile.write("Rate:\t" + String.format("%.5g%n", c) + "\n");
                   outfile.write("Rate:\t" + String.format("%f\n", d) + "\n");
           } else {        
                   outfile.write("Rate:\tN/A\n");
            }
           outfile.write("miRNA-BS CREATED SNPs:\t" + "\t" + miRNA_snp_created + "\n");
           if (miRNA_snp_tot != 0) {
                   double d = (double) miRNA_snp_created/miRNA_snp_tot;
                   outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           } else {        
                   outfile.write("Rate:\tN/A\n");
            }
           
           outfile.write("miRNA-BS CHANGED SNPs:\t" + "\t" + miRNA_snp_changed + "\n");
           if (miRNA_snp_tot != 0) {
                   double d = (double) miRNA_snp_changed/miRNA_snp_tot;
                //   outfile.write("Rate:\t" + String.format("%.5g%n", c) + "\n");
                   outfile.write("Rate:\t" + String.format("%f\n", d) + "\n");
           } else {        
                   outfile.write("Rate:\tN/A\n");
            }
           
           outfile.write("miRNA genomic SNPs:\t" + "\t" + miRNA_genomic_snp + "\n\n");
           outfile.write("ESE-BS deletion SNPs:\t" + "\t" + ESE_snp_del + "\n");
           if (ESE_snp_tot != 0) {
                double d = (double) ESE_snp_del/ESE_snp_tot;       
                outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           }else {      
                outfile.write("Rate:\tN/A\n");
           }
           outfile.write("ESE-BS induction SNPs:\t" + "\t" + ESE_snp_ind + "\n");
           if (ESE_snp_tot != 0) {
                   double d = (double) ESE_snp_ind/ESE_snp_tot;
                   outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           } else {        
                   outfile.write("Rate:\tN/A\n");
            }
           outfile.write("ESS-BS deletion SNPs:\t" + "\t" + ESE_snp_del + "\n");
           if (ESS_snp_tot != 0) {
                double d = (double) ESS_snp_del/ESS_snp_tot;       
                outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           }else {      
                outfile.write("Rate:\tN/A\n");
           }
           outfile.write("ESS-BS induction SNPs:\t" + "\t" + ESS_snp_ind + "\n");
           if (ESS_snp_tot != 0) {
                   double d = (double) ESS_snp_ind/ESS_snp_tot;
                   outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           } else {        
                   outfile.write("Rate:\tN/A\n");
            }
           outfile.write("Splicing Change SNPs:\t" + "\t" + splice_snp + "\n");
           if (splice_snp_tot != 0) {
                double d = (double) splice_snp/splice_snp_tot;
                outfile.write("Rate Splicing Change SNPs:\t" + String.format("%.5g%n", d) + "\n");
           } else { 
                outfile.write("Rate:\tN/A\n");
            }
           outfile.write("Protein Motif disrupting SNPs:\t" + "\t" + pmotif_snp + "\n");
           if (pmotif_snp_tot != 0) {
               double d = (double) pmotif_snp/pmotif_snp_tot;
                outfile.write("Rate Protein Motif disrupting SNPs:\t" + String.format("%.5g%n", d) + "\n");
           } else {    
                outfile.write("Rate:\tN/A\n");
            }         
           
           outfile.write("\n");
           outfile.write("-------------Insertions-------------");
           outfile.write("\n");
           outfile.write("Conserved Element Insertions:\t" + "\t" + cons_ins + "\n\n");
           outfile.write("TFBS Insertions:\t" + "\t" + TFBS_ins_tot + "\n");
           outfile.write("TFBS Disruptive Insertions:\t" + "\t" + TFBS_ins + "\n");
           if (TFBS_ins_tot != 0) {
           //     System.out.println("TFBS_ins_tot = " + TFBS_ins_tot);
                double d = (double) TFBS_ins/TFBS_ins_tot;
         //       System.out.println("Rate insertions is:" + c);
                outfile.write("Rate TFBS Insertions:\t" + String.format("%.5g%n", d) + "\n");
           } else { 
                outfile.write("Rate:\tN/A\n");
            }
           outfile.write("pre-miRNA Disrupting Insertions:\t" + "\t" + premiRNA_ins_tot + "\n");
           if (tot_ins != 0) {
                double d = (double) premiRNA_ins_tot/tot_ins;
                outfile.write("Rate pre-miRNA Disrupting Insertions:\t" + String.format("%.5g%n", d) + "\n");
           } else { 
                outfile.write("Rate:\tN/A\n");
           }
           
           outfile.write("miRNA-BS Insertions:\t" + "\t" + miRNA_ins_tot + "\n");
           outfile.write("miRNA-BS disrupting Insertions:\t" + "\t" + miRNA_ins_del + "\n");
           if (miRNA_ins_tot != 0) {
                   double d = (double) miRNA_ins_del/miRNA_ins_tot;
                   outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           } else {        
                   outfile.write("Rate:\tN/A\n");
            }
           outfile.write("miRNA-BS CREATED Insertions:\t" + "\t" + miRNA_ins_created + "\n");
           if (miRNA_ins_tot != 0) {
                   double d = (double) miRNA_ins_created/miRNA_ins_tot;
                   outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           } else {        
                   outfile.write("Rate:\tN/A\n");
            }
           outfile.write("miRNA-BS CHANGED Insertions:\t" + "\t" + miRNA_ins_changed + "\n");
           if (miRNA_ins_tot != 0) {
                   double d = (double) miRNA_ins_changed/miRNA_ins_tot;
                //   outfile.write("Rate:\t" + String.format("%.5g%n", c) + "\n");
                   outfile.write("Rate:\t" + String.format("%f\n", d) + "\n");
           } else {        
                   outfile.write("Rate:\tN/A\n");
            }
           outfile.write("miRNA genomic Insertions:\t" + "\t" + miRNA_genomic_ins + "\n\n");
           outfile.write("ESE-BS deletion Insertions:\t" + "\t" + ESE_ins_del + "\n");
           if (ESE_ins_tot != 0) {
                double d = (double) ESE_ins_del/ESE_ins_tot;       
                outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           }else {      
                outfile.write("Rate:\tN/A\n");
           }
           outfile.write("ESE-BS induction Insertions:\t" + "\t" + ESE_ins_ind + "\n");
           if (ESE_ins_tot != 0) {
                   double d = (double) ESE_ins_ind/ESE_ins_tot;
                   outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           } else {        
                   outfile.write("Rate:\tN/A\n");
            }
           outfile.write("ESS-BS deletion Insertions:\t" + "\t" + ESE_ins_del + "\n");
           if (ESS_ins_tot != 0) {
                double d = (double) ESS_ins_del/ESS_ins_tot;       
                outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           }else {      
                outfile.write("Rate:\tN/A\n");
           }
           outfile.write("ESS-BS induction Insertions:\t" + "\t" + ESS_ins_ind + "\n");
           if (ESS_ins_tot != 0) {
                   double d = (double) ESS_ins_ind/ESS_ins_tot;
                   outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           } else {        
                   outfile.write("Rate:\tN/A\n");
            }
           outfile.write("Splicing Change Inertions:\t" + "\t" + splice_ins + "\n");
           if (splice_ins_tot != 0) {
                double d = (double) splice_ins/splice_ins_tot;
                outfile.write("Rate Splicing Change Insertions:\t" + String.format("%.5g%n", d) + "\n");
           } else { 
                outfile.write("Rate:\tN/A\n");
           }
           outfile.write("Protein Motif disrupting Insertions:\t" + "\t" + pmotif_ins+ "\n");
           if (pmotif_ins_tot != 0){
               double d = (double) pmotif_ins/pmotif_ins_tot;
                outfile.write("Rate Protein Motif disrupting Insertions:\t" + String.format("%.5g%n", d) + "\n");
           } else {    
                outfile.write("Rate:\tN/A\n");
            }        
           
           outfile.write("\n");
           outfile.write("-------------Deletions-------------");
           outfile.write("\n");
           outfile.write("Conserved Element Deletions:\t" + "\t" + cons_del + "\n\n");
           outfile.write("TFBS Deletions:\t" + "\t" + TFBS_del_tot + "\n");
           outfile.write("TFBS Disrupting Deletions:\t" + "\t" + TFBS_del + "\n");
           if (TFBS_del_tot != 0) {
                double d = (double) TFBS_del/TFBS_del_tot;
                outfile.write("Rate TFBS Deletions:\t" + String.format("%.5g%n", d) + "\n");
           } else { 
                outfile.write("Rate:\tN/A\n");
            }
           outfile.write("pre-miRNA Disrupting Deletions:\t" + "\t" + premiRNA_del_tot + "\n");
           if (tot_del != 0) {
                double d = (double) premiRNA_del_tot/tot_del;
                outfile.write("Rate pre-miRNA Disrupting Deletions:\t" + String.format("%.5g%n", d) + "\n");
           } else { 
                outfile.write("Rate:\tN/A\n");
           }
           outfile.write("miRNA-BS Deletions:\t" + "\t" + miRNA_del_tot + "\n");
           outfile.write("miRNA-BS disrupting Deletions:\t" + "\t" + miRNA_del_del + "\n");
           if (miRNA_del_tot != 0) {
                   double d = (double) miRNA_del_del/miRNA_del_tot;
                   outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");       
           } else {        
                   outfile.write("Rate:\tN/A\n");
            }
                  outfile.write("miRNA-BS CREATED Deletions:\t" + "\t" + miRNA_del_created + "\n");
           if (miRNA_del_tot != 0) {
                   double d = (double) miRNA_del_created/miRNA_del_tot;
                   outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           } else {        
                   outfile.write("Rate:\tN/A\n");
            }
           outfile.write("miRNA-BS CHANGED Deletions:\t" + "\t" + miRNA_del_changed + "\n");
           if (miRNA_del_tot != 0) {
                   double d = (double) miRNA_del_changed/miRNA_del_tot;
                //   outfile.write("Rate:\t" + String.format("%.5g%n", c) + "\n");
                   outfile.write("Rate:\t" + String.format("%f\n", d) + "\n");
           } else {        
                   outfile.write("Rate:\tN/A\n");
            }

           outfile.write("miRNA genomic Deletions:\t" + "\t" + miRNA_genomic_del + "\n\n");
           outfile.write("ESE-BS deletion Deletions:\t" + "\t" + ESE_del_del + "\n");
           if (ESE_del_tot != 0) {
                double d = (double) ESE_del_del/ESE_del_tot;       
                outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           }else {      
                outfile.write("Rate:\tN/A\n");
           }
           outfile.write("ESE-BS induction Deletions:\t" + "\t" + ESE_del_ind + "\n");
           if (ESE_del_tot != 0) {
                   double d = (double) ESE_del_ind/ESE_del_tot;
                   outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           } else {        
                   outfile.write("Rate:\tN/A\n");
            }
           outfile.write("ESS-BS deletion Deletions:\t" + "\t" + ESS_del_del + "\n");
           if (ESS_del_tot != 0) {
                double d = (double) ESS_del_del/ESS_del_tot;       
                outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           }else {      
                outfile.write("Rate:\tN/A\n");
           }
            outfile.write("ESS-BS deletion Deletions:\t" + "\t" + ESS_del_ind + "\n");
           if (ESS_del_tot != 0) {
                double d = (double) ESS_del_ind/ESS_del_tot;       
                outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           }else {      
                outfile.write("Rate:\tN/A\n");
           }
           outfile.write("Splicing Change Deletions:\t" + "\t" + splice_del + "\n");
           if (splice_del_tot != 0) {
                double d = (double) splice_del/splice_del_tot;
                outfile.write("Rate Splicing Change Deletions:\t" + String.format("%.5g%n", d) + "\n");
           } else { 
                outfile.write("Rate:\tN/A\n");
           }   
           outfile.write("Protein Motif disrupting Deletions:\t" + "\t" + pmotif_del + "\n");
           if (pmotif_del_tot != 0){
               double d = (double) pmotif_del/pmotif_del_tot;
                outfile.write("Rate Protein Motif disrupting Deletions:\t" + String.format("%.5g%n", d) + "\n");
           } else {    
                outfile.write("Rate:\tN/A\n");
            }        
           
           outfile.write("\n");
           outfile.write("-------------Delins-------------");
           outfile.write("\n");
           outfile.write("Conserved Element Rearrangements:\t" + "\t" + cons_rear + "\n\n");
           outfile.write("TFBS Rearangements:\t" + "\t" + TFBS_rear_tot + "\n");
           outfile.write("TFBS Disrupting Rearangements:\t" + "\t" + TFBS_rear + "\n");
           if (TFBS_rear_tot != 0){
           //     System.out.println("TFBS_rear_tot = " + TFBS_rear_tot);
                double d = (double) TFBS_rear/TFBS_rear_tot;
           //     System.out.println("Rate rearangements is:" + c);
                outfile.write("Rate TFBS Rearangements:\t" + String.format("%.5g%n", d) + "\n");
           } else { 
                outfile.write("Rate:\tN/A\n");
            }
           outfile.write("pre-miRNA Disrupting Rearangements:\t" + "\t" + premiRNA_rear_tot + "\n");
            if (tot_rear != 0){
                double d = (double) premiRNA_rear_tot/tot_rear;
                outfile.write("Rate pre-miRNA Rearangements:\t" + String.format("%.5g%n", d) + "\n");
           } else  { 
                outfile.write("Rate:\tN/A\n");
            }
            
           outfile.write("miRNA-BS Insertions:\t" + "\t" + miRNA_rear_tot + "\n");
           outfile.write("miRNA-BS disrupting Rearangements:\t" + "\t" + miRNA_rear_del + "\n");
           if (miRNA_rear_tot != 0) {
                   double d = (double) miRNA_rear_del/miRNA_rear_tot;
                   outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           } else {        
                   outfile.write("Rate:\tN/A\n");
            }
           outfile.write("miRNA-BS CREATED Rearangements:\t" + "\t" + miRNA_rear_created + "\n");
           if (miRNA_rear_tot !=  0) {
                   double d = (double) miRNA_rear_created/miRNA_rear_tot;
                   outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           } else {        
                   outfile.write("Rate:\tN/A\n");
            }
           outfile.write("miRNA-BS CHANGED Rearangements:\t" + "\t" + miRNA_rear_changed + "\n");
           if (miRNA_rear_tot != 0) {
                   double d = (double) miRNA_rear_changed/miRNA_rear_tot;
                   outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           } else {        
                   outfile.write("Rate:\tN/A\n");
            }
           outfile.write("miRNA genomic Rearangements:\t" + "\t" + miRNA_genomic_rear + "\n\n");
           outfile.write("ESE-BS deletion Rearangements:\t" + "\t" + ESE_rear_del + "\n");
           if (ESE_rear_tot != 0) {
                double d = (double) ESE_rear_del/ESE_rear_tot;       
                outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           }else {      
                outfile.write("Rate:\tN/A\n");
           }
           outfile.write("ESE-BS induction Rearangements:\t" + "\t" + ESE_rear_ind + "\n");
           if (ESE_rear_tot != 0) {
                   double d = (double) ESE_rear_ind/ESE_rear_tot;
                   outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           } else {        
                   outfile.write("Rate:\tN/A\n");
            }
           outfile.write("ESS-BS deletion Rearangements:\t" + "\t" + ESE_rear_del + "\n");
           if (ESS_rear_tot != 0) {
                double d = (double) ESS_rear_del/ESS_rear_tot;       
                outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           }else {      
                outfile.write("Rate:\tN/A\n");
           }
           outfile.write("ESS-BS induction Rearangements:\t" + "\t" + ESS_rear_ind + "\n");
           if (ESS_rear_tot != 0) {
                   double d = (double) ESS_rear_ind/ESS_rear_tot;
                   outfile.write("Rate:\t" + String.format("%.5g%n", d) + "\n");
           } else {        
                   outfile.write("Rate:\tN/A\n");
            } 
           outfile.write("Splicing Change Rearangements:\t" + "\t" + splice_rear + "\n");
           if (splice_rear_tot != 0) {
                double d = (double) splice_rear/splice_rear_tot;
                outfile.write("Rate Splicing Change Rearangements:\t" + String.format("%.5g%n", d) + "\n");
           } else { 
                outfile.write("Rate:\tN/A\n");
           }       
           outfile.write("Protein Motif disrupting Rearangements:\t" + "\t" + pmotif_rear + "\n");
           if (pmotif_rear_tot != 0) {
               double d = (double) pmotif_rear/pmotif_rear_tot;
                outfile.write("Rate Protein Motif disrupting Rearangements:\t" + String.format("%.5g%n", d) + "\n");
           } else {    
                outfile.write("Rate:\tN/A\n");
            }      
           
          
           outfile.close();
           System.out.println("Everything written to file!");
    }
    
    public boolean TFBS_calc(String[] spLine) {
            boolean TF_check = false;
            boolean TF_check_major = false;
            boolean TF_check_delete = false;
            float lowest = 100;
            String[] entry = spLine[getHeaderIndex("TFBS_deltaS")].split("///");
            for (int i=0; i<entry.length; i++) {
                if(entry[i].equals("DELETED")) {
                    TF_check_delete = true;
                    TF_check = true;
                    break;
                  /*  if (CONS_FOUND && spLine[getHeaderIndex("Location")].contains("Upstream")) {
                        lowest = -40;
                     } */ 
                } else if (!entry[i].equals("-")) {
                    try {
                      if (Float.parseFloat(entry[i]) < 0) {
                          TF_check = true;
                          
                      }
                      //something here come back later and figure out
                      if (Float.parseFloat(entry[i]) < -7) {
                          TF_check = true;
                          TF_check_major = true;
                          break;
                      } 
                    }  catch( Exception ex) { 
                        
                    }  
                }
            }
            
        //calculate only the major TFBS variation that are smaller then -7, are upstreap and is a conserved variants;
   //     if (CONS_FOUND && spLine[getHeaderIndex("Location")].contains("Upstream"))  {
            if (TF_check_delete || TF_check_major) {
                TOT_DISRUPT++;
                /*
                 * for the new total disrupt calculations
                 */
                temp = true;
                if (spLine[getHeaderIndex("VarType")].contains("snp")) {
                    TFBS_snp++;
                } if (spLine[getHeaderIndex("VarType")].contains("ins")) {
                    TFBS_ins++;
                } else if (spLine[getHeaderIndex("VarType")].contains("del")) {
                    TFBS_del++;
                } else if (spLine[getHeaderIndex("VarType")].contains("delins")) {
                    TFBS_rear++;
                } 
            }
     //   }
            
        return TF_check;
    }
    
    private boolean CONS_FOUND_Fuction(String[] spLine) {
        CONS_FOUND = false;
       // phastConsElements46way
        if (!spLine[getHeaderIndex("Conserved46way")].equals("-") && !spLine[getHeaderIndex("Conserved46way")].startsWith("N/A")) {
           CONS_FOUND = true;
           cons_count++;
        }
        //phastConsElements46wayPlacental
        else if (!spLine[getHeaderIndex("Conserved46wayPlacental")].equals("-") && !spLine[getHeaderIndex("Conserved46wayPlacental")].startsWith("N/A")) {
           CONS_FOUND = true;
           cons_count++;
        }
       // phastConsElements46wayPrimates
        else if (!spLine[getHeaderIndex("Conserved46wayPrimates")].equals("-") && !spLine[getHeaderIndex("Conserved46wayPrimates")].startsWith("N/A")) {
           CONS_FOUND = true;
           cons_count++;
        }
        
        return CONS_FOUND;
    }
    
    /*
     * Count all of the values in the miRNA_BS_deltaG, that will be the total number of miRNA
     */
    private int miRNA(String[] spLine) {
        //extract the value in the miRNA_BS_deltaG column and make sure it's not empty
   //     String value = spLine[getHeaderIndex("miRNA_BS_deltaG")];
   //     char[] chars = value.toCharArray();
        
        String[] entry = spLine[getHeaderIndex("miRNA_BS_deltaG")].split("///");
        for (int i=0; i<entry.length; i++) {        
   //    for (char c : chars) {
        //        if (Character.isDigit(c) || Character.isLetter(c)) {
                  if ( !entry[i].equals("-")) {
                     double test = 0.0; 
                 
                     try {
                         test =  Double.parseDouble(entry[i]);
                     } catch (Exception e) {
                    //  System.out.println("Can't parse this: " + entry[i]);
                  }
                  if (spLine[getHeaderIndex("miRNA_BS_impact")].contains("DELETED")) {
                     TOT_DISRUPT++; 
                     return 1;
                  } else if (test < 0.0) {
                     TOT_DISRUPT++; 
                     return 1;   
                  } else if (spLine[getHeaderIndex("miRNA_BS_impact")].contains("CREATED")){
                     return 2;
                  }
                return 3;
    
                }
       }        
        return 0;
    }      
    
    
    private int miRNA_genomic(String[] spLine) {
        //extract the value in the miRNA_genomic column and make sure it's not empty
        String value = spLine[getHeaderIndex("miRNA_genomic")];
        char[] chars = value.toCharArray();
            
       for (char c : chars) {
                if (Character.isDigit(c) || Character.isLetter(c)) {
                     return 1;
                }
       }        
        return 0;
    }  
    
    //Coding Impact
    private int Coding_Impact (String[] spLine) {
        int i = 0;
        if (spLine[getHeaderIndex("Coding_Impact")].contains("Nonsense")) {
            TOT_DISRUPT++;
            nonsense++;
            i =1;
        } else if (spLine[getHeaderIndex("Coding_Impact")].contains("Nonsynonymous")) {
            nonsyn++;

        } else if (spLine[getHeaderIndex("Coding_Impact")].contains("Synonymous")) {
            syn++;

        } 
        return i;
    }
    
    //Damaging truncation
    private int Truncation (String[] spLine) {
        int i = 0;
        if (spLine[getHeaderIndex("Trunc_Prediction")].contains("Damaging_Truncation")) {
            i = 1;
        } 
        return i;
    }
    
    //Polythen
    private int[] Polythen (String[] spLine) {
        int[] i = new int[2];
        if (spLine[getHeaderIndex("Protein_Impact_Prediction(Polyphen)")].contains("probably damaging")) {
            TOT_DISRUPT++;
            probably_damaging++;
            i[0] = 1;
            i[1] = 1;
        } else if (spLine[getHeaderIndex("Protein_Impact_Prediction(Polyphen)")].contains("possibly damaging")) {
            TOT_DISRUPT++;
            possibly_damaging++;
            i[0] =1;
        }
        return i;
    }
    
        //Polythen
    private int Sift (String[] spLine) {
        int i =0;
        if (spLine[getHeaderIndex("Protein_Impact_Prediction(SIFT)")].contains("INTOLERANT")) {
            SIFT_INTOLERANT++;
            i = 1;
        } else if (spLine[getHeaderIndex("Protein_Impact_Prediction(SIFT)")].contains("TOLERANT")) {
            SIFT_TOLERANT++;
           // i =1;
        }
        return i;
    }
    
    //Condel
    private int Condel (String[] spLine) {
        int i =0;
        if (spLine[getHeaderIndex("Protein_Impact_Prediction(Condel)")].contains("deleterious")) {
            Condel_deletirious++;
            i = 1;
        } else if (spLine[getHeaderIndex("Protein_Impact_Prediction(Condel)")].contains("neutral")) {
            Condel_neutral++;
           // i =1;
        }
        return i;
    }
    
    //miRNA_seq_genomic
    private boolean premiRNA(String[] spLine) {
        boolean premiRNA_found = false;
        String[] entry = spLine[getHeaderIndex("miRNA_folding_deltaG")].split("///");
        for (int i=0; i<entry.length; i++) {
           if (!entry[i].equals("-") && !entry[i].contains("N/A")) {
               if (entry[i].contains("DELETED") || Float.parseFloat(entry[i]) < 0) {
                   TOT_DISRUPT++;
                   premiRNA_disrupt++;
                   premiRNA_found = true;
                   break;
               }
           } 
        }
        /*
         * check to make sure that premiRNA wasn't found yet, if it wasn't found check the column
         * miRNA_binding_deltaG
         */
        if (!premiRNA_found) {
                    String[] entryTwo = spLine[getHeaderIndex("miRNA_binding_deltaG")].split("///");
            for (int i=0; i<entry.length; i++) {
                if (!entryTwo[i].equals("-") && !entryTwo[i].contains("N/A")) {
                if (entryTwo[i].contains("DELETED") || Float.parseFloat(entry[i]) < 0) {
                   TOT_DISRUPT++;
                   premiRNA_disrupt++;
                   premiRNA_found = true;
                   break;
               }
           } 
        }
        }
        
        return premiRNA_found;
    }
    
    //splicing change
    private boolean splicingChange(String[] spLine) {
      
        boolean splice_found = false;
        boolean no_splice_found = false;
        boolean uncoven_found = false;
        String[] entry = spLine[getHeaderIndex("Splicing_Prediction(MaxENT)")].split("///");
        for (int i=0; i<entry.length; i++) {
            if (entry[i].equals("-")) {
                continue;
            } else if (entry[i].startsWith("Splicing_Change")) {
                temp_splice = true;
                TOT_DISRUPT++;
                splice_site++;
                splice_change++;
                splice_found = true;
            } else if (entry[i].startsWith("No_Splicing_Change")) {
                no_splice_found = true;
            } else if (entry[i].startsWith("Unconventional_Splice_Site")) {
                uncoven_found = true;
            } 
            
        }
        
        if (no_splice_found || uncoven_found) {
            splice_site++;
             if (spLine[getHeaderIndex("VarType")].contains("snp")) {
                    splice_snp_tot++;
                } if (spLine[getHeaderIndex("VarType")].contains("ins")) {
                    splice_ins_tot++;
                } else if (spLine[getHeaderIndex("VarType")].contains("del")) {
                    splice_del_tot++;
                } else if (spLine[getHeaderIndex("VarType")].contains("delins")) {
                    splice_rear_tot++;
                } 
        }
        return splice_found;
    }
    
    private int ESE_sites(String[] spLine) {
        if (spLine[getHeaderIndex("ESE_sites")].contains("DELETED")) {
                TOT_DISRUPT++;
                return 1;
            } else if (spLine[getHeaderIndex("ESE_sites")].contains("CREATED")){
                TOT_DISRUPT++;
                return 2;
            }
        return 0;
    }
    
    
   private int ESS_sites(String[] spLine) {
        if (spLine[getHeaderIndex("ESS_sites")].contains("DELETED")) {
                TOT_DISRUPT++;
                return 1;
            } else if (spLine[getHeaderIndex("ESS_sites")].contains("CREATED")){
                TOT_DISRUPT++;
                return 2;
            }
        return 0;
    }
    
   private boolean Protein_Domains_Impact(String[] spLine) {
            boolean PFAM_found = false;
            boolean PFAM_major_found = false;
            String[] entry = spLine[getHeaderIndex("Protein_Domains_Impact(LogRE)")].split("///");
            for (int i = 0; i < entry.length; i++) {
                if (entry[i].equals("-")) {
                    System.out.println("entry.lenght is: " + entry.length);
                    continue;
                } else {
                    String[] temp = entry[i].split("\\$");
                    for (int j = 0; j < temp.length; j++) {
                        String[] dom = temp[j].split("~");
                        
                        if (dom.length < 2) {
                            break;
                        } else if (dom[1].equals("-")) {
                            break;
                        }
                        float logRE = Float.parseFloat(dom[1]);
                        if (logRE >= 0.0) {
                            PFAM_found = true;
                            pmotif++;
                        } 
                        if (logRE >0.7) {
                            PFAM_major_found = true;
                            PFAM = true;
                           // PFAM_found = true;
                            break;
                        }
                }
            
                }
            
            }
            
           if(PFAM_major_found) {
                pmotif_major ++;
                TOT_DISRUPT++;
                if (spLine[getHeaderIndex("VarType")].contains("snp")) {
                    pmotif_snp ++;
                } else if (spLine[getHeaderIndex("VarType")].contains("ins")) {
                    pmotif_ins ++;
                } else if (spLine[getHeaderIndex("VarType")].contains("del")) {
                    pmotif_del ++;
                } else if (spLine[getHeaderIndex("VarType")].contains("delins")) {
                    pmotif_rear ++;
                }            
            }
            
            return PFAM_major_found;
    }
    
       @Override
    public void run() {
           if (Interface.statistics == 1) {
           try {
                calculateStatsFile(Interface.file);
            } catch (IOException ex) {
                Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
            }
}
           else if (Interface.statistics == 2) {
              //  int last = ShowTable.arrayOfArrays.size();
            try {
                calculateStatsArray(ShowTable.arrayOfArrays.get(FilterFunctions.currentArray));
            } catch (IOException ex) {
                Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, ex);
            }
           }

    }
}
