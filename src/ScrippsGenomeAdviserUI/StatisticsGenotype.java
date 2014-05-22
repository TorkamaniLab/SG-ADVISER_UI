/*
 * Calculate the Stats per genotype for file with multiple genotypes
 */
package ScrippsGenomeAdviserUI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author gerikson
 */
public class StatisticsGenotype {
    
    public int[] Acmg1_count;
    public String[] header;
    public String genotypes;
    
    public StatisticsGenotype(String path, String genoPath) throws FileNotFoundException, IOException {
            BufferedReader reader = new BufferedReader( new FileReader (genoPath));
            String         line = null;
            
            while( ( line = reader.readLine() ) != null ) {
                genotypes = line;
            }
       
        //get header
        String[] tempGen = genotypes.split("\t");
        Acmg1_count = new int[tempGen.length];
      //  readFile(path);
    }
    
     public int getHeaderIndex(String var) {
        int index = 0;
        for (int i = 0; i<header.length; i++) {
            if (header[i].contains(var)) {
                index = i;
                break;
            }
        }
        
        return index;
    }
    
    public void readFile(String filePath) throws FileNotFoundException, IOException{
            BufferedReader reader = new BufferedReader( new FileReader (filePath));
            String         line = null;
            
            while( ( line = reader.readLine() ) != null ) {
                String[] tempLine = line.split("\t");
                countACMGperGenotype(tempLine);
            }
            
            
         WriteToFile(filePath);
    }
    
    public void WriteToFile (String filePath) throws IOException {
                   BufferedWriter outfile = new BufferedWriter(
                                 new FileWriter("ACMG_stats.txt", true));         
              outfile.write("\t" + "\t".concat(" ACMG STATISTICS ").concat(filePath) +'\n');        
              outfile.write(genotypes + "\n");
              for(int i = 0; i < Acmg1_count.length; i++) {
              outfile.write(Acmg1_count[i] + "\t");    
        }  
              outfile.close();
    }
    
    public void countACMGperGenotype(String[] spLine) {
            
            if (spLine[getHeaderIndex("ACMG_Score_Clinical")].contains("1~")){
    			//extract the genotype
    			String value = spLine[getHeaderIndex("Notes")];
    			value = value.replace("Genotype=", "");
    			String[] spValue = value.split(":");
    			for (int i =0; i<spValue.length; i++) {
                            if (spValue[i].contains("1/1") || spValue[i].contains("1|1") 
                                    || spValue[i].contains("1/0") || spValue[i].contains("1|0")) {
                                int count = Acmg1_count[i];
                                count ++;
                                Acmg1_count[i] = count;
                            }
                        }
        }
    }
    public static void main(String[] args) throws IOException {
           System.out.println("Usage javac Stats.java full/path/to/file /header/file");
           StatisticsGenotype ob1 = new StatisticsGenotype(args[0], args[1]);
           ob1.readFile(args[0]);
    }
}
