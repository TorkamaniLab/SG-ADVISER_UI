/*
 * Store the objects of parsed lines not found in vcf
 */
package ScrippsGenomeAdviserUI;

/**
 *
 * @author gerikson
 */
public class LostVCF {
    public int chr = 0;
    public int begin = 0;
    public int end = 0;
    public String Vartype = "";
    public String ref = "";
    public String var = "";
    public String importedGenotypes = "";
    public String[] lin;
    public int beginLeng = 0;
    
    public LostVCF(int chrom, int beg, int en, String varTyp, String re, String va, String importedGenotype, String NAgen, String[] li) {
          chr = chrom;
          begin = beg;
          end = en;
          Vartype = varTyp;
          ref = re;
          var = va;
          importedGenotypes = importedGenotype;
          lin = li;
          beginLeng = NAgen.length() + 2;
    }
}
