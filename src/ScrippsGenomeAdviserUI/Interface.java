/**
 * Interface for the SG_Adviser UI
 * Galina Erikson
 * Research Associate - Translational Science Research Institute
 * Version_1.6_7
 * February 18, 2014
 * Added a different file chooser that would be more native to mac
 * Notifications if the user tries to upload files other then SG_Adviser
 * Notification if the length of the line is not the same as the header
 * Fixed an issue with page count
 * 
 */
 
package ScrippsGenomeAdviserUI;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList; 
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import org.scripps.cnvViewer.CnvViewerInterface;
import org.scripps.utils.CnvHeader;

public class Interface extends javax.swing.JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFrame frame;
    public CnvViewerInterface openCnv;
    public static javax.swing.JPanel jPanel1;
    public  ArrayList<ScrippsGenomeAdviserUI.Reader> arrayOfLines;
    public String fileName;
    static int fileLines;
    public Header head;
    public static String tempHeadForPrefilter = "";
    public static File file;
    //public static String headString;
    //First fifty lines made public for future access by UNDO button
    public static Interface fiftyLines;
    //Thread management variable;
    public static ExecutorService threadExecutor;
    //Is this a vcf file?
    //public static boolean vcfStatus = false;
    //public static int vcfCount = 0;
    
    /*
     * counter telling the stats module if the file is already loaded in memory 
     * or we cant to calculate the stats before file is loaded 1 = before file is loaded
     * 17 = file is loaded
     */
    public static int statistics = 0;
    
    //This file already was analysed using the UI
    public static boolean analysedFile = false;
    public static int indexColumnNumber; 
    static private String selectedString(ItemSelectable is) {
    Object selected[] = is.getSelectedObjects();
    return ((selected.length == 0) ? "null" : (String) selected[0]);
  }
    
   
public Interface() {
         fileChooser = new javax.swing.JFileChooser();
         //JFrame.setDefaultLookAndFeelDecorated(true);
         frame = new JFrame(); 
    }


    private JPanel initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ScrippsGenomeAdviserUI/ScrippsProgram.jpg"))); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1010, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.setBackground(new Color(0xa5d4f2));
        pack();
        return jPanel1;

    }
      

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {                                     
    System.exit(0);       
    }                                    

    public String checkFileType(File file)
    {
    	        BufferedReader bReader;
				String fileType = "";
    	        int lineCount = 1;
    	        String line = "";
    	 
    	        try
				{
    	        	bReader = new BufferedReader(new FileReader(file));
					while((line = bReader.readLine()) != null)
					{
						
						if(lineCount == 2)
						{
							if(line.contains("gain") || line.contains("loss"))
							{
								System.out.println("CNV ADVISER opens");
								fileType = "CNV";
							}
							else
							{
								System.out.println("SNP ADVISER opens");
								fileType = "SNP";
							}
							
							break;
						}
						lineCount++;
					 }
					bReader.close();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return fileType;
    	    }
    	
    	
    
public void openFile() {   
    //variable the checks if this is a sg_adviser file
    boolean adviser_file = false;
    fiftyLines = new Interface();
    String fileType = "";
    int returnVal = fileChooser.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        file = fileChooser.getSelectedFile();
        fileType = checkFileType(file);
        if(fileType.equals("SNP"))
        {
        		fiftyLines.fileName =  file.getName();
            Long siz = file.getTotalSpace();
            System.out.println("File size is: " + siz);
            System.out.println("SG-Adviser UI_1.6_8");
            String s = fiftyLines.fileName;
            boolean match;
            BufferedReader bReader;
            try {
                bReader = new BufferedReader(
                new FileReader(file));        
                String line = null;
                fiftyLines.arrayOfLines = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
                for(int i=0;i<1;i++) {  
                		line = bReader.readLine();
                    String header = "";
                    /*
                     * Check to see if this is a SG-ADviser line:
                     */
                    if (line.startsWith("Haplotype") || line.startsWith("Chromosome")) {   
                        adviser_file = true;     
                        /*
                         * Check to see if the line ends in comments (did it come out of UI already)
                         */
                        if (line.contains("Comments")) {
	                          header = "Index".concat("\t");
	                          analysedFile = true;
	                          String [] tempL = line.split("\t");
	                          for (int ij =0; ij<tempL.length; ij++) {
	                              if (tempL[ij].contains("Index")) {
	                                  indexColumnNumber = ij;
	                              } else {
	                                  header = header.concat(tempL[ij]).concat("\t");
	                              }
                              }
                        } else {
                              header = "Imported_Genotypes" + "\t" + "Index" + "\t" + line;
                        }     
                        fiftyLines.head = new Header(header);
                   }        
               }
            
            // If this is a sg_adviser file continue
            if (adviser_file) {
                long fileInBytes = file.length();     
                System.out.println("Filelenght is: " + fileInBytes);  
                double fileinGB = fileInBytes/1000000000;
                System.out.println("File lenght in gb is " + fileinGB);

                if (fileinGB > 3) {
                    Prefilter(fileinGB); 

                }  else {
                for (int line_count=1; line_count<1001; line_count++) 
                {

                            String current_line = null;             
                            current_line = bReader.readLine();
                           
                            if (current_line == null)
                            {
                                ShowTable.onlyPage = 1;
                                /*
                                 * Less then 1000 lines, remove next/previous pages
                                 */

                                break;
                            } 
                            else 
                            {

                            	
                            	//if user tries to load a previously annotated file, get rid of the old index
                                if (analysedFile) {
                                    String[] tempLine = current_line.split("\t");
                                    String nt = Integer.toString(line_count).concat("\t");
                                    for (int old_index = 0; old_index<indexColumnNumber; old_index++) {
                                        nt = nt.concat(tempLine[old_index]).concat("\t");
                                    } 
                                    for (int st2 = indexColumnNumber+1; st2<tempLine.length; st2++) {
                                        nt = nt.concat(tempLine[st2]).concat("\t");
                                    }
                                    ScrippsGenomeAdviserUI.Reader ob1 = new ScrippsGenomeAdviserUI.Reader(nt);
                                    fiftyLines.arrayOfLines.add(ob1);
                                } else {
                                    current_line = "N/A" + "\t" + line_count + "\t" + current_line;
                                    
                                    // Insert current line with index into the Reader object
                                    ScrippsGenomeAdviserUI.Reader ob1 = new ScrippsGenomeAdviserUI.Reader(current_line);
                                   
                                    fiftyLines.arrayOfLines.add(ob1);
                                }
                       }
                }

                //Save the original object for future reference (Undo button)
                ShowTable first_table = new ShowTable();
                first_table.intoVector(fiftyLines);

                ReadFile rf = new ReadFile(file);
                threadExecutor = Executors.newFixedThreadPool(1);
                threadExecutor.execute(rf);
                threadExecutor.shutdown();
                this.frame.dispose();   
                }
            } else {
                    Interface demo1 = new Interface();  
                    demo1.frame.setContentPane(demo1.createContentPane());
                    //Display the window.
                    demo1.frame.setSize(500, 200);
                    demo1.frame.setLocationRelativeTo(null);
                    demo1.frame.add(new JLabel("This is not a SG ADVISER annotated file, please select a correct file! "));
                    demo1.frame.add(new JLabel("                                                                       "));
                    demo1.frame.add(new JLabel("  Go to http://genomics.scripps.edu/ADVISER/Result_Desc.jsp to see     "));
                    demo1.frame.add(new JLabel("                                                                       "));
                    demo1.frame.add(new JLabel("             example of a SG ADVISER annotated file.                   "));
                    demo1.frame.setVisible(true); 
            }
            } catch (IOException ex) {
                Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
            } 

        }
        else
        {
        	 openCnv = new CnvViewerInterface();
        	 openCnv.startCnvViewerInterface(file);
        	 this.frame.dispose();  
        }
     
        }
        

}

public void noFilter() throws FileNotFoundException, IOException {
           int datacount = 0; 
           BufferedReader bReader;
      
           bReader = new BufferedReader(new FileReader(file));
                    
                    String line = null;
                    fiftyLines.arrayOfLines = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
                    for(int i=0;i<1;i++) {  
                       line = bReader.readLine();
                       String l = "Imported_Genotypes" + "\t" + "Index" + "\t" + line;
                       fiftyLines.head = new Header(l);

                   }
                    
          for (int in=1; in<1001; in++) {
                    
                    
                    String n = null;             
                    n = bReader.readLine();
                    if (n == null){
                        break;
                    } else {
                    
                    String nt; 
                    nt = "N/A" + "\t" + in + "\t" + n;
                    ScrippsGenomeAdviserUI.Reader ob1 = new ScrippsGenomeAdviserUI.Reader(nt);
                            fiftyLines.arrayOfLines.add(ob1);
                            
               }
        }

        //Save the original object for future reference (Undo button)
         ShowTable nt = new ShowTable();
         nt.intoVector(fiftyLines);
         ReadFile rf = new ReadFile(file);
         threadExecutor = Executors.newFixedThreadPool(1);
         threadExecutor.execute(rf);
         threadExecutor.shutdown();
         this.frame.dispose();   
 }
        

    


public void StatsFile() throws IOException {
    statistics = 1;
    String [] head; 
    String  line = null;
    int returnVal = fileChooser.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        file = fileChooser.getSelectedFile();
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
        Statistics stats = new Statistics(line, "Calculating Statistics");
        threadExecutor = Executors.newFixedThreadPool(1);
        threadExecutor.execute(stats);
        threadExecutor.shutdown();
        
}
    
}
public void Prefilter(double fileSize) {
		String line = null;
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
                       tempHeadForPrefilter = line;
                       ShowTable.columnNames = line.split("\t");  
                   }
              
          
      final Interface demo2 = new Interface();  
      demo2.frame.setContentPane(demo2.createContentPane());    
      demo2.frame.setTitle("Advance Filter");
      JLabel label1 = new JLabel("The file size you are trying to upload is " + fileSize + "GB.");
      demo2.frame.add(label1);
      double heapSpace = fileSize*2 + 1;
      JLabel label2 = new JLabel("To successfully view and analyze this file  you will need at least " + heapSpace + "GB RAM memory.");
      demo2.frame.add(label2);
      JLabel label3 = new JLabel("We advise you to prefilter the file by using our custom filter.");
      demo2.frame.add(label3);
      JButton yes = new JButton("YES");
      JButton no = new JButton("NO");
      JLabel label4 = new JLabel("Do you want to prefilter the file using the Coding and Splice variants filter?");
      demo2.frame.add(label4);
      demo2.frame.add(yes);
      demo2.frame.add(no);
      demo2.frame.setSize(650, 200);
      demo2.frame.setLocationRelativeTo(null);
      demo2.frame.setVisible(true); 
      yes.addActionListener(new ActionListener() {
          @Override
      public void actionPerformed(ActionEvent e) {
                 ShowTable.tableStatus = 19;   
                 FilterFunctions rf2 = new FilterFunctions("Coding and Splice Variants");
                 threadExecutor = Executors.newFixedThreadPool(1);
                 threadExecutor.execute(rf2);
                 threadExecutor.shutdown();   
                 demo2.frame.dispose();
                 
          }
      });    
      
     no.addActionListener(new ActionListener() {
          @Override
      public void actionPerformed(ActionEvent e) {
             try {
                 noFilter();
                 demo2.dispose();
             } catch (FileNotFoundException ex) {
                 Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
             } catch (IOException ex) {
                 Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
             }
          }
      });  
     
}


public void OpenActionPerformed(java.awt.event.ActionEvent evt) {  
		
          openFile();
     
}  

public void StatsActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            StatsFile();
        } catch (IOException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
}

public void HelpActionPerformed(java.awt.event.ActionEvent evt) {  
    
            new HelpMenu().setVisible(true);
     
}   


public JPanel createContentPane(){
        
        JPanel totalGUI = new JPanel();
        totalGUI.setBackground(new Color(202, 225, 250));
        totalGUI.setMinimumSize(new Dimension(300, 300));
        totalGUI.setPreferredSize(new Dimension(300, 300));
        totalGUI.setMaximumSize(new Dimension(300, 00));
        totalGUI.setOpaque(true);
        return totalGUI;
}

/**
 * Creates the Menu Bar
 * @return JMenuBar
 */
 public JMenuBar createMenuBar() {
     

      JMenuBar menubar = new JMenuBar();
      
      JMenu file = new JMenu("File");
      JMenu Statistics = new JMenu ("Statistics");
      JMenu help =new JMenu("Help");

      
      menubar.add(file);
      menubar.add(Statistics);
      menubar.add(help);
      
      JMenuItem open = new JMenuItem("Open");
      JMenuItem exit = new JMenuItem("Exit");
      JMenuItem stats = new JMenuItem("Statistics");
      JMenuItem helpMe = new JMenuItem("Help");
         
      file.add(open);
      file.add(exit); 
      Statistics.add(stats);
      help.add(helpMe);
      
      
      exit.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
               ExitActionPerformed(evt);
            }
      });
      
      open.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenActionPerformed(evt);
            }
        });
      
      stats.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                StatsActionPerformed(evt);
            }
        });
            

      helpMe.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                HelpActionPerformed(evt);
            }
        });
            
      return menubar; 
    }


private static void createAndShowGUI() {

        Interface demo = new Interface();
        demo.frame.setContentPane(demo.initComponents());
        demo.frame.setJMenuBar(demo.createMenuBar());
        demo.frame.setTitle("Scripps Genome ADVISER");
        demo.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        demo.frame.pack();
        demo.frame.setVisible(true);
        demo.frame.setLocationRelativeTo(null);
    }


public static void main(String[] args) {
        
        try {
           
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException ex");
        } catch (InstantiationException ex) {
            System.out.println("InstantiationException ex");
        } catch (IllegalAccessException ex) {
            System.out.println("IllegalAccessException ex");
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            System.out.println("UnsupportedLookAndFeelException");
        }
        //</editor-fold>


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
           @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
      // Variables declaration - do not modify
    public static javax.swing.JFileChooser fileChooser;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration

}

