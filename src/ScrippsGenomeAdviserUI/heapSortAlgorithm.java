/*
 * Heap Sort Algorithm
 */
package ScrippsGenomeAdviserUI;


import java.awt.BorderLayout;
import java.awt.Container;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.border.Border;


/**
 *
 * @author gerikson
 */
public class heapSortAlgorithm extends JFrame implements Runnable {
    
    public static String selection;
   //headers with comment column in array
 //   public static String[] headArray;
    public static double[][] ArrayforSort;
    public static Object[][] SortedData; 
    public static ArrayList<ScrippsGenomeAdviserUI.Reader> arrayOfLines;
    public static int dataLines;
//    public static Vector<String> headers;
    public static int datacount = 0;
    public static String threadName;
    //progress bar variable
    public static JFrame frame;
    public static Container content;
    public static JProgressBar progressBar;
    public static Border border;
    public static double perc2 = 0;
    public static int last = 0;
    public static int begin = 0; 
    public static int nextPage = 1;
    //variable to see if we printed the last page already
    public static boolean tr = false;
    
    
    public heapSortAlgorithm(ArrayList<ScrippsGenomeAdviserUI.Reader> al, int d, String sel){
        arrayOfLines = al;
        dataLines = d;
        selection = sel;
       // ArrayforSort = null;
        ArrayforSort = new double[dataLines][2];
        
         frame = new JFrame("Sorting");
         content = frame.getContentPane();
         progressBar = new JProgressBar();
         border = BorderFactory.createTitledBorder("Sorting by column " + sel + " ...");
         progressBar.setBorder(border);
         content.add(progressBar, BorderLayout.NORTH);
         frame.setSize(300, 100);
         frame.setVisible(true);
    }


    public static void inputData(ArrayList<ScrippsGenomeAdviserUI.Reader> arrayOfLines, int dataL, String sel) {
      
        //The number of the column to be sorted
        int columnNumber = 0;
       // int CG_69 = 0;
       // ArrayList<String> frequency = new ArrayList<String>();
      //  Set<String> CG_frequency;
         
        System.out.println("Thread active count: " + Thread.activeCount());
        System.out.println("current fread is: " + Thread.currentThread());

        boolean match;
        nextPage = 1;
        begin = 0;
        last = 0;
        //Counter for parser
        int readCount = 0;
        
        //array to be sorted, 2 columns 1 is column number and second is value that needs sorting

      //  ArrayforSort = null;
     //  ArrayforSort = new double[dataLines][2];
        
       
                    double y = (double) dataLines;
                    double ing  = 100000.0;
                    double x = (double) ing/dataLines;
                    double perc = (double) (x*100);

         
        //Extracting the row number to sort by

         for (int s = 0; s < ShowTable.columns.length; s++ ) {             
            if (selection == ShowTable.columns[s]) {
                 columnNumber = s;
                }
          //  if (headArray[s].contains("CG_69")) {
            //    CG_69 = s;
           // }
         }
         
           for(int j=0; j<dataLines; j++) {
                           readCount++; 
            
            if ((readCount%100000) == 0){
                             
                            // Loading.frame.dispose();
                             System.out.print("Copying in small array" + readCount + "\t");
                             DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                             Calendar cal = Calendar.getInstance();
                             System.out.println(dateFormat.format(cal.getTime()));
                             perc2 = perc2 + perc;
                             int b = (int) perc2 - 2;  
                             progressBar.setValue(b);
                             progressBar.setStringPainted(true);
             }
                
               //Get the column number 
               double c;
               c = Double.parseDouble(Integer.toString(j));
               //undefined values need to be last
               double b = 999999999;
               
               String lines = arrayOfLines.get(j).fileRow;
               String[] datavalue = lines.split("\t");
               String col;
               
               if (columnNumber > datavalue.length - 1){
                  
                   col = "999999999";
                   
               } else {
               
                   col = datavalue[columnNumber];
               }
               
               
             //  frequency.add(datavalue[CG_69]);
               
               
               // check var type column first
               if (sel.equalsIgnoreCase("VarType")) {
                   if ("del".equals(col) ) {b = 1;}
                   else if ("delins".equals(col)) {b = 2;}
                   else if ("ins".equals(col)) {b = 3;}
                   else if ("snp".equals(col)) {b = 4;}
               } 
               //parse Chromosome
               else if (sel.equalsIgnoreCase("Chromosome")) {
                   col = col.replaceAll("chr", "");
                   if (col.equalsIgnoreCase("X")) {b=100;}
                   else if(col.equalsIgnoreCase("Y")) {b=200;}
                   else {b = Integer.parseInt(col);}
               }
               
               //parse all other columns
               else {
               String[] ACMG = col.split("///");
               String ac1 = ACMG[0];
              
               String ac;
               if (match = ac1.contains("~")) {
                   String[] ac2 = ac1.split("~");
                   ac = ac2[0];
               } else {
               ac = ac1.substring(0, Math.min(ac1.length(), 9));
            //   System.out.println(ac);
               }
               
               //if ac is a number this should work
               try {
                b = Double.parseDouble(ac);
         
               } 
               // else catch exception and transform ac
               catch( Exception ex) { 
                ac = ac.replaceAll("\\s","");
                

          //converting string to ASCII
            match = ac.contains("N/A");
            if (ac != "-" && ac != null && ac !="" && match == false) {
            
              //take only first 2 characters
            ac = ac.substring(0, Math.min(ac.length(), 3));
            String n = "";    
            for (int i = 0; i < ac.length(); i++){
            char ch = ac.charAt(i);  
            //here is the trick from char to ASCII
            int jh = (int) ch;
            String n1 = Integer.toString(jh); 
            n = n + n1;
            }
            b = Integer.parseInt(n);
          } 
             
           if (ac.equals("-")) {
                 b = 999999999;//.0;
               //  System.out.println(b);
            } 
     

               }
              // }
               }
         //      System.out.println("This is what will be sorted: ");
          //     System.out.println(b);
               ArrayforSort[j][0] = c;
               ArrayforSort[j][1] = b;   
           }
           
           datacount = 0;
         
          //the uniques sorting... 
        //  CG_frequency = new TreeSet<String>(frequency);  
       //   CG_Object = CG_frequency.toArray();
          
        //  System.out.printf("Unique values sorted: %s%n", CG_frequency);

           //do the sorting!
            for(int i=ArrayforSort.length; i> 0; i--){
          
            datacount++; 
            fnSortHeap(ArrayforSort, i-1);
           

               if (datacount == 500){
                             
                             System.out.print("Sorting hapening" + datacount + "\t");
                             DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                             Calendar cal = Calendar.getInstance();
                             System.out.println(dateFormat.format(cal.getTime()));

                             String lin;
                             SortedData  = new Object[500][ShowTable.columns.length];
                             begin =  dataLines - 1;
                             last = dataLines - 501;
                             for (int ij = ArrayforSort.length - 1; ij > ArrayforSort.length - 501; ij--){
                                    int value = (int) ArrayforSort[ij][0];   
                                    lin = arrayOfLines.get(value).fileRow;
                                    String[] r = lin.split("\t");
                                    int size = r.length;
                            for (int in = 0; in<size; in++){                  
                                    SortedData[ArrayforSort.length - 1 - ij][in] = r[in];
                             
                            }
                     }
                             
         progressBar.setValue(100);
         progressBar.setStringPainted(true); 
         ShowTable.frame.dispose(); 
         ShowTable.createContentTable(SortedData, ShowTable.columns, "Sorted File " + ReadFile.fileName + " by column: " + selection); 
         //delete FilterTable frame if exists
       /*  if (FilterTable.DEBUG == true) {
             FilterTable.frame.dispose();
         }
         
         */
         ShowTable.tableStatus = 2;
         heapSortAlgorithm.frame.dispose(); 
       //  heapSortTwo.frame.dispose();
             }
                  if (datacount%1000 == 0){

                             System.out.print("Sorting hapening" + datacount + "\t");
                             DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                             Calendar cal = Calendar.getInstance();
                             System.out.println(dateFormat.format(cal.getTime()));

                 
                  }
  }  

    if (datacount < 500) {
                             ShowTable.onlyPage=1;
                             System.out.print("Sorting hapening" + datacount + "\t");
                             DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                             Calendar cal = Calendar.getInstance();
                             System.out.println(dateFormat.format(cal.getTime()));

                             String lin;
                             SortedData  = new Object[datacount][ShowTable.columns.length];
                             begin =  dataLines - 1;
                             last = dataLines - 501;
                             for (int ij = ArrayforSort.length - 1; ij > ArrayforSort.length - datacount+1; ij--){
                                    int value = (int) ArrayforSort[ij][0];   
                                    lin = arrayOfLines.get(value).fileRow;
                                    String[] r = lin.split("\t");
                                    int size = r.length;
                            for (int in = 0; in<size; in++){                  
                                    SortedData[ArrayforSort.length - 1 - ij][in] = r[in];
                             
                            }
                     }
                             
         progressBar.setValue(100);
         progressBar.setStringPainted(true); 
         ShowTable.frame.dispose(); 
         ShowTable.createContentTable(SortedData, ShowTable.columns, "Sorted File " + ReadFile.fileName + " by column: " + selection); 
         //delete FilterTable frame if exists
      /*   if (FilterTable.DEBUG == true) {
             FilterTable.frame.dispose();
         }
        */ 
         
         ShowTable.tableStatus = 2;
         heapSortAlgorithm.frame.dispose(); 
     //    heapSortTwo.frame.dispose(); 
    } 
}   

    
    //Previous page
    public static void PreviousPage(){

        
        begin = begin + 500;
        System.out.println("Begin is: " + begin);
        
        last = last + 500;
        
        System.out.println("Last is: " + last);
        
        int end;
        //make sure we didn't reach the end
        if (begin >= ArrayforSort.length) {
            begin = ArrayforSort.length;
            last = begin - 500;
            end = last;
           // ShowTable.onlyPage = 3; 
        } else { //last = begin - 500; 
            end = last;}
        
             String lin;
             Object[][] SortedDat;
             SortedDat  = new Object[500][ShowTable.columns.length];
             for (int i = begin; i > end; i--){
             int value = (int) ArrayforSort[i][0];  
             lin = arrayOfLines.get(value).fileRow;
             String[] r = lin.split("\t");
               int size = r.length;
               for (int in = 0; in<size; in++){                  
                    SortedDat[begin - i][in] = r[in];
               }
              }
              
 
         ShowTable.frame.dispose();
         ShowTable.createContentTable(SortedDat, ShowTable.columns, "Sorted File " + ReadFile.fileName + " by column: " + selection); 
  
    }
 
   //next page sort
    public static void SortNext() throws InterruptedException {
        
        //check to make sure next page was sorted already
        if (datacount > (dataLines - last + 500)) {
        
        begin = last;
        last = last - 500;
         System.out.println("Begin is: " + begin);
          System.out.println("Last is: " + last);
         
             String lin;
             Object[][] SortedDat;
             SortedDat  = new Object[500][ShowTable.columns.length];
             for (int i = begin; i > last; i--){
             int value = (int) ArrayforSort[i][0];  
             lin = arrayOfLines.get(value).fileRow;
             String[] r = lin.split("\t");
               int size = r.length;
               for (int in = 0; in<size; in++){                  
                    SortedDat[begin - i][in] = r[in];
               }
              }
              
 
         ShowTable.frame.dispose(); 
         ShowTable.createContentTable(SortedDat, ShowTable.columns, "Sorted File " + ReadFile.fileName + " by column: " + selection); 
  
    } else if (datacount < (dataLines - last + 500) && last > 0 && last - 500 > 0) {
            JFrame fr = new JFrame("Next Page sorting");
            JLabel lab = new JLabel("Please check back in a few seconds!");
            fr.add(lab);
            Thread.sleep(4000);
            fr.dispose();
            SortNext();
        }
   
   // If this is the last page... 
    else {
        
        if (tr == false) {        
        begin = last;

            
            
             String lin;
             Object[][] SortedDat;
             SortedDat  = new Object[begin][ShowTable.columns.length];
             for (int i = begin; i > 0; i--){
             int value = (int) ArrayforSort[i][0];  
             lin = arrayOfLines.get(value).fileRow;
             String[] r = lin.split("\t");
               int size = r.length;
               for (int in = 0; in<size; in++){                  
                    SortedDat[begin - i][in] = r[in];
               }
              }
              
 
         ShowTable.frame.dispose(); 
      //   ShowTable.createContentTable(SortedDat, headArray, "Sorted File " + ReadFile.fileName + " by column: " + selection + " Last Page!"); 
          ShowTable.createContentTable(SortedDat, ShowTable.columns, "Sorted File " + ReadFile.fileName + " by column: " + selection); 
         tr = true;
        }
        
    }
        
    }
    
    
//sorting happening here
  public static void fnSortHeap(double array[][], int arr_ubound){

  int i, o;
  int lChild, rChild, mChild, root;//, 
  double temp, temp1;
  root = (arr_ubound-1)/2;


  for(i=root;i>=0;i--){
      
        lChild = (2*i)+1;
        rChild = (2*i)+2;
        if((lChild <= arr_ubound) && (rChild <= arr_ubound)){

        //  if(array[rChild][1] >= array[lChild][1]) {
            if(array[rChild][1] < array[lChild][1]) {
                mChild = rChild;
            }
         else {
                mChild = lChild;
            }
         }
        else{
            if(rChild > arr_ubound) {
                mChild = lChild;
            }
               else {
                mChild = rChild;
            }
        }
   

   if(array[i][1] >= array[mChild][1]){

          temp = array[i][1];
          temp1 = array[i][0];
          array[i][1] = array[mChild][1];
          array[i][0] = array[mChild][0];
          array[mChild][1] = temp;
          array[mChild][0] = temp1;
  }

  }
  
  temp = array[0][1];
  temp1 = array[0][0];
  array[0][1] = array[arr_ubound][1];
  array[0][0] = array[arr_ubound][0];
  array[arr_ubound][1] = temp;
  array[arr_ubound][0] = temp1;

  }
   
    @Override
    public void run() {
      //  inputData(arrayOfLines, dataLines, selection, headers);
        
            final String orgName = Thread.currentThread().getName();
            threadName = Thread.currentThread().getName();
            Thread.currentThread().setName(orgName + "firstThread");
            try {
                perc2=0;
           //     ArrayforSort = null; 
             //   ShowTable.threadStat = true;
            //     List<Runnable> a = (List<Runnable>) ShowTable.threadExecutor.shutdownNow();
                  inputData(arrayOfLines, dataLines, selection);
            } finally {
                Thread.currentThread().setName(orgName);
            }
        }

    }
   



    
