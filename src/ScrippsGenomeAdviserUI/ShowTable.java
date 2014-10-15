package ScrippsGenomeAdviserUI;
/**
 * Class that prints out the table.  Swing multithreading for sorting.
 * Table Status 17 - entire file loaded in memory
 * Table Status 23 - merge sort
 *                if (ShowTable.tableStatus == 3) {CodingVar();}
                  if (ShowTable.tableStatus == 4 || ShowTable.tableStatus == 12 || ShowTable.tableStatus == 13) {SpliceVar();}
                  if (ShowTable.tableStatus == 5) {KnownDisease();}
                  if (ShowTable.tableStatus == 6) {PredClinical();}
                  if (ShowTable.tableStatus == 7) {PredResearch();}
                  if (ShowTable.tableStatus == 8) {CancerGenes();}
                  if (ShowTable.tableStatus == 9) {Pharmacogenetic();}
                  if (ShowTable.tableStatus == 10) {TruncatedVariants();}
                  if (ShowTable.tableStatus == 11) {NondbSNP();}
                  if (ShowTable.tableStatus == 14) {AdvanceFilterAlgorithm();}
                  if (ShowTable.tableStatus == 15) {FilterFunction();}
                  if (ShowTable.tableStatus == 16) {chromPosition();}
                  if (ShowTable.tableStatus == 18) {AdvanceFilterAlgorithmOR();}
                  if (ShowTable.tableStatus == 19) {
                  try {
                     prefilter();
                  } catch (IOException ex) {
                     Logger.getLogger(FilterFunctions.class.getName()).log(Level.SEVERE, null, ex);
            }
}                 if (ShowTable.tableStatus == 20) {ValidValues();}  
                  if (ShowTable.tableStatus == 21) {IDIOMprefilter();}    
                  if (ShowTable.tableStatus == 22) {geneList();}  
                  * 
 */




import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.table.TableColumn;




/**
 *
 * @author gerikson
 */

public class ShowTable extends javax.swing.JFrame implements Runnable {
    
    public static Object[][] dataFilter;
    public static JFrame frame;
    public static JPanel panel;
    public static JLabel messageLabel;
    public static String filename;

    //header storage
    public static Vector<String> h;
    public static String[] columns = null;
      
 
    private final JFileChooser fileChooser;
    //for save to table
    public static JFileChooser fc;
    public static String sortSelection;

    public static int FileNumberRows = 0;
    public static int nextPage = 0;
    public static int page = 1;
    public static int pageFilter = 0;
    //for next/previous pages: 1 - entire file view, 2 - sort view, 3 - 16 filter views, 17 - vcf fille loaded
    public static int tableStatus = 1;
    public static int nextPageSort = 500;
    public static int pageSort = 1;
    public static int arraySize = 500;
    
    //ShowTable thread management
    public static ExecutorService threadExecutor;
    public static boolean threadStat = false; 
    public static int sortStat = 0;
    
    
    //for frequency selection
    public static Double frequencySelection = -1.0;
    public static Double frequencySelection2 = -1.0;
    public static Double frequencySelection3 = -1.0;
    
     //variables for multiple filter algorithm
     public static String sort1 = "";
     public static String sort2 = "";
     public static String sort3 = "";
    
     public static String text1 = null;
     public static String text2 = null;
     public static String text3 = null;
     public static String options = "AND";
     // for next previous button to deactivate them or what should we write on them depending if it's first/last or only page
     public static int onlyPage = 0;

     //for next/previous pages
     public static int nextIterator = 0;
     public static String[] columnNames;
     public static JTextField filterText;
     public static String text;
     
     public static ArrayList<ScrippsGenomeAdviserUI.Reader> FilteredArray;
     public static ArrayList<ArrayList<ScrippsGenomeAdviserUI.Reader>> arrayOfArrays = new ArrayList<ArrayList<ScrippsGenomeAdviserUI.Reader>>();
    //want array is the present view, 0 - if undoData button was never pressed
     public static int arrayIndex = 0;

     //for filter index
     public static int filterCounter = 0;

     
    //jframe resize 
    private static boolean componentShown = false;
    public static int frameHeight = 737;
    public static int frameWidth = 1139;
     
    public ShowTable(){
        fileChooser = new javax.swing.JFileChooser();
        frame = new JFrame();
        panel = new JPanel();
        
    }



    public void intoVector(Interface ob){
        
           tableStatus =1;
  
           ArrayList<ScrippsGenomeAdviserUI.Reader> arrayOfLines = ob.arrayOfLines;
           
           Header head = ob.head; 
           filename =   ob.fileName;
           //make sure there is no header predefined already
           if (h == null) {
           h = new Vector<String>();
           h = head.headers;
           if (!h.lastElement().equals("Comments")) {
                h.add("Comments");
           }
           columns = h.toArray(new String[h.size()]);
           }
           
           
           String lines;
           int lineNumber;
           int r;
           lineNumber = arrayOfLines.size();
           
           /**
            * If the file contains more less then 1000 lines print entire file else print 500 lines
            */
           if (lineNumber > 1000){
           r = 1000;
           }else {
               r = lineNumber;
           }
           
           //System.out.println("Column lenght is: " + columns.length);
         //initialize dataFilter
           dataFilter  = new Object[r][columns.length];
           
         for(int j=0; j<r; j++){   
              lines = arrayOfLines.get(j).fileRow;
              String[] r2 = lines.split("\t");
              int size1 = r2.length;
           
           //Make sure this file has as many columns as the header
           if (columns.length >= size1) {
              for (int in = 0; in<size1; in++){                  
                   dataFilter[j][in] = r2[in];
                    }
              // In case if there we are loading a file with already a 
              //comment column, no need to ad another space
                if (size1 < columns.length) {
                dataFilter[j][size1] = "";
                }
           } else  {
               System.out.println("This looks like a corupted file! Lenght of this line is: " + size1 + " but we should have only " + columns.length + " columns.");
               System.out.println(lines);
           }
         }

                 
         String title;
         title = "Scripps Genome ADVISER File: "; 
         createContentTable(dataFilter, columns, title + filename); 

      }
    
    
public static void into2DArray(ArrayList<ScrippsGenomeAdviserUI.Reader> af, int counter){
         
         Object[][] dataFilter;
         dataFilter =  new Object[counter][columns.length];
            for(int j=0; j<counter; j++){   
    
             String  lines = af.get(j).fileRow;
              String[] r2 = lines.split("\t");
              int size1 = r2.length;

              //Make sure this file has as many columns as the header
              if (columns.length >= size1) {
                    for (int c = 0; c<size1; c++) {
                        dataFilter[j][c] = r2[c];
                    }     
                    //if there is no comment fill that cell with null
                    if (columns.length > size1) {
                        dataFilter[j][size1] = ""; 

                    } 
              } else  {
                 System.out.println("This looks like a corupted file! Lenght of this line is: " + size1 + " but we should have only " + columns.length + " columns.");
                 System.out.println(lines);
           }
           }   

             createContentTable(dataFilter, columns, filename );
    }
    
public static void into2DArrayFilterData (ArrayList<ScrippsGenomeAdviserUI.Reader> af, int counter) {
    
         Object[][] dataFilter;
         dataFilter =  new Object[counter][columns.length];
              
        for(int j=0; j<counter; j++){   
             filterCounter++;   
             String  lines = af.get(j).fileRow;
              String[] line_as_array = lines.split("\t");
              int lenght_of_line = line_as_array.length;
              
              //Make sure this file has as many columns as the header
              if (columns.length >= lenght_of_line) {             
                    //if there is no comment fill that cell with null
                    for (int c = 0; c<lenght_of_line; c++) {
                        dataFilter[j][c] = line_as_array[c];
                    }

                    if (columns.length > lenght_of_line) {
                        dataFilter[j][lenght_of_line] = ""; 
                    }
              } else  {
                 System.out.println("This looks like a corupted file! Lenght of this line is: " + lenght_of_line + " but we should have only " + columns.length + " columns.");
                 System.out.println(lines);
           }
            }
 
        String filter_name = FilterFunctions.filterName.get(FilterFunctions.currentArray);
        createContentTable(dataFilter, columns, "Filtered file by  " + filter_name);
}

public static void createContentTable(final Object[][] data, String[] headArray, String title){ 
	
    columns = headArray;  
    columnNames = headArray;
    final JTable table = new JTable(data, headArray );
    table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
    table.setAutoCreateRowSorter(true);
    // Set the height of all rows to 32 pixels high,
    // regardless if any heights were assigned to particular rows
    table.setRowHeight(25);
    
    table.getTableHeader().setToolTipText(
    "Click to sort; Shift-Click to sort in reverse order");
   
    // Disable autoCreateColumnsFromModel otherwise all the column customizations
    // and adjustments will be lost when the model data is sorted
    table.setAutoCreateColumnsFromModel(false); 
    final ShowTable demo = new ShowTable();
    
    //Functionality to edit comments columns and the ability to save it       
    int vColIndex = columns.length - 1;
    TableColumn col = table.getColumnModel().getColumn(vColIndex);
    TableEditor mt = new TableEditor();
    col.setCellEditor(mt);


    panel.setLayout(new BorderLayout());
    demo.frame.setJMenuBar(demo.createMenuBar());
    demo.frame.add(new JScrollPane( table ));
                                                                                                                                      
 	//Insert the column combobox for filter
    JComboBox column = new JComboBox(columns);
    JLabel lOne = new JLabel("  Select column: ");


    column.addActionListener(new java.awt.event.ActionListener() {
    		@Override
    		public void actionPerformed(java.awt.event.ActionEvent evt) {
               sortSelection = FilterFileActionPerformed(evt);
        }

    private String FilterFileActionPerformed(ActionEvent evt) {
             JComboBox cb = (JComboBox)evt.getSource();
             String selection = (String)cb.getSelectedItem();
             return selection;

    }

    }); 

    filterText = new JTextField("Please enter the filter criteria!"); 
    JButton button = new JButton("Filter file");
    JLabel emptyspace = new JLabel(" ");
    JLabel emptyspace1 = new JLabel(" ");

    JLabel button2 = new JLabel( "                                                                                                   ");   
    JLabel button1 = new JLabel("                                                                                                                               ");
    //get the current array size 
    int ij = ShowTable.arrayOfArrays.size();
    if (ij >0) { 
    		ArrayList<ScrippsGenomeAdviserUI.Reader> tempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
    		tempArray = ShowTable.arrayOfArrays.get(FilterFunctions.currentArray);
    		int arrayLenght = tempArray.size();
    		int finalPage;
    		int pag = arrayLenght/1000;  
    	if (pag < 1) {
    		pag = 1;
    		finalPage = 1;
    	} else {
    		//adding one to the final page due to the fact that in most cases it won't be exact division by 1000
    		finalPage = pag + 1;
    	}
 
    	//If this is the first page, page count needs to start at 1, just another error check
    	if (onlyPage == 3) {
        FilterFunctions.pageNumber = 1;
        button2 = new JLabel( "1000 lines per page / Page " + FilterFunctions.pageNumber + " / "  + finalPage + " pages                                 ");     
    } else {
        button2 = new JLabel( "1000 lines per page / Page " + FilterFunctions.pageNumber + " / "  + finalPage + " pages                                 ");     
     }
    } 
 
 
    JLabel undo = new JLabel("Undo/Redo:");
    JButton advanceFilter = new JButton("Advanced Filter");
    advanceFilter.addActionListener(new java.awt.event.ActionListener() {
      @Override
       public void actionPerformed(java.awt.event.ActionEvent evt) {    
              advanceFilterActionPerformed(evt);
        }

    private void advanceFilterActionPerformed(ActionEvent evt) {
                InitGlobalVar();
                tableStatus = 14;  
        if (threadStat == false) {
                 AdvanceFilter();    
                    } else {
                threadExecutor.shutdownNow();
         //       heapSortAlgorithm.frame.dispose();
         //       heapSortAlgorithm.ArrayforSort = null;
                      AdvanceFilter();
              }
    }
    });
      
    JButton firstPage = new JButton("|<<");

    //if this is the main array, no actions aloud
    if (FilterFunctions.currentArray == 0) {
    		firstPage.setEnabled(false);
    } else {
    		firstPage.addActionListener(new java.awt.event.ActionListener() {
    			@Override
    			public void actionPerformed(java.awt.event.ActionEvent evt) {
        
    	  onlyPage = 0;
    	  UndoActionPerformed(evt);
          
        }

    	});
    }
    JButton prevPage = new JButton("<");
    //if this is the main array, no actions alowed
    if (FilterFunctions.currentArray == 0) {
    		prevPage.setEnabled(false);
    } else {
    		prevPage.addActionListener(new java.awt.event.ActionListener() {
    			@Override
    			public void actionPerformed(java.awt.event.ActionEvent evt) {
    			onlyPage = 0;
    			UndoDataActionPerformed(evt);
        }

    		});
    }

    JButton nextPage = new JButton(">");
    //if this is the last filtered array, no actions alowed
    if (FilterFunctions.currentArray == ShowTable.arrayOfArrays.size() - 1) {
    		nextPage.setEnabled(false);
    } else {
    		nextPage.addActionListener(new java.awt.event.ActionListener() {
    			@Override
    		public void actionPerformed(java.awt.event.ActionEvent evt) {  
    		onlyPage = 0;
    		NextFilterActionPerformed(evt);  
        }
    		});
    }

    JButton lastPage = new JButton(">>|");

    //if this is the last filtered array, no actions alowed
    if (FilterFunctions.currentArray == ShowTable.arrayOfArrays.size() - 1) {
    		lastPage.setEnabled(false);
    } else {
    		lastPage.addActionListener(new java.awt.event.ActionListener() {
    			@Override
    			public void actionPerformed(java.awt.event.ActionEvent evt) {
    			onlyPage = 0;
    			LastFilterActionPerformed(evt);
        }
    		});
    }

    Border bored = BorderFactory.createRaisedBevelBorder();
    button.setBorder(bored);
    advanceFilter.setBorder(bored);
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
    	  		text = "";
    	  		text = filterText.getText();
    	  		String st = "Please enter the filter criteria!";
    	  if (text==st) {
    		  text = "";
    	  }
    if (text.length() > 0) {
        tableStatus = 15;
        InitGlobalVar();
        //kill the sort thread if it's hapening
        if (threadStat == false) {
                FilteredArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
             FilterFunctions rf2 = new FilterFunctions(sortSelection);
             threadExecutor = Executors.newFixedThreadPool(1);
             threadExecutor.execute(rf2);
             threadExecutor.shutdown(); 
                    } else {
                threadExecutor.shutdownNow();

                FilteredArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
                FilterFunctions rf2 = new FilterFunctions(sortSelection);
                threadExecutor = Executors.newFixedThreadPool(1);
                threadExecutor.execute(rf2);
                threadExecutor.shutdown(); 
              }           
     
    		}
      }
    });
  
    	JToolBar toolBar2 = new JToolBar();
    toolBar2.add(lOne);
    toolBar2.add(column);
    toolBar2.add(filterText);
    toolBar2.add(button);
    toolBar2.add(emptyspace);
    toolBar2.add(advanceFilter);
    toolBar2.add(emptyspace1);
    toolBar2.add(undo);
    firstPage.setToolTipText("Shows original data");
    toolBar2.add(firstPage);
    prevPage.setToolTipText("Shows previous filtered array");
    toolBar2.add(prevPage);
    	nextPage.setToolTipText("Shows next filtered array");
	toolBar2.add(nextPage);
	lastPage.setToolTipText("Shows last filtered array");
	toolBar2.add(lastPage);
	demo.frame.add(toolBar2, BorderLayout.NORTH);

	if(onlyPage == 0) {
		Next = new JButton("Next Page");
		Previous = new JButton("First Page!");
		Previous.setEnabled(false);
		onlyPage = 4; 
	    
	} else if (onlyPage == 1){
		Next = new JButton("Single Page!");
		Previous = new JButton("              ");
		Next.setEnabled(false);
		Previous.setEnabled(false);
	
	} else if (onlyPage == 2){
		Next = new JButton("Last Page!");
		Previous = new JButton("Previous Page");
		Next.setEnabled(false);
		Previous.setEnabled(true); // added by Neha
		onlyPage = 4;
	
	} else if (onlyPage == 3){
		Next = new JButton("Next Page");
		Previous = new JButton("First Page!");
		Previous.setEnabled(false);
		onlyPage = 4;
	} else if (onlyPage == 4)
	{
		Next = new JButton("Next Page");
		Previous = new JButton("Previous Page");
		Next.setEnabled(true);
		Previous.setEnabled(true);
	
	}
 
	Next.addActionListener(new java.awt.event.ActionListener() {
	
	       @Override
	       public void actionPerformed(java.awt.event.ActionEvent evt) {
	             try {
	                  Next.setEnabled(false);
	                  NextPage();
	             } catch (InterruptedException ex) {
	                 Logger.getLogger(ShowTable.class.getName()).log(Level.SEVERE, null, ex);
	             }
	        }

	});
	Previous.addActionListener(new java.awt.event.ActionListener() {
	
	       @Override
	       public void actionPerformed(java.awt.event.ActionEvent evt) {
	
	                Previous.setEnabled(false);
	                PreviousPage();
	
	        }

	});
	     
    JToolBar toolBar = new JToolBar();
    toolBar.add(button1);
    toolBar.add(button2);
    
    if (onlyPage != 1) {
        toolBar.add(Previous);
        toolBar.add(Next);
    } else {
        JLabel button3 = new JLabel( "          ");   
        JLabel button4 = new JLabel( "Single page");  
        toolBar.add(button3);
        toolBar.add(button4);
        onlyPage = 0;
    }
    
    
    demo.frame.add(toolBar, java.awt.BorderLayout.SOUTH);    
    demo.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    demo.frame.pack();
    demo.frame.setSize(frameWidth, frameHeight);
    demo.frame.setVisible( true ); 
    demo.frame.setTitle(title);
    demo.frame.setLocationRelativeTo(null);
    //compenent to determine user resize
    ShowTable.frame.addComponentListener(new ComponentAdapter() {
    @Override
	public void componentResized(ComponentEvent e) {
        if(componentShown) {
         //  System.out.println("Component RESIZED");
           frameHeight = ShowTable.frame.getHeight();
           frameWidth = ShowTable.frame.getWidth();
         //  System.out.println("Frame height is: " + Integer.toString(frameHeight));
         //  System.out.println("Frame width is: " + Integer.toString(frameWidth));
        }  
    }

    @Override
	public void componentShown(ComponentEvent e) {
        componentShown = true;
    }
});
   
}  
/**
    public static void createContentTable(final Object[][] data, String[] headArray, String title){ 
        columns = headArray;  
        columnNames = headArray;
        final JTable table = new JTable(data, headArray );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        table.setAutoCreateRowSorter(true);
        // Set the height of all rows to 32 pixels high,
        // regardless if any heights were assigned to particular rows
        table.setRowHeight(25);
        
        table.getTableHeader().setToolTipText(
        "Click to sort; Shift-Click to sort in reverse order");
       
        // Disable autoCreateColumnsFromModel otherwise all the column customizations
        // and adjustments will be lost when the model data is sorted
        table.setAutoCreateColumnsFromModel(false); 
        
        //Functionality to edit comments columns and the ability to save it       
        int vColIndex = columns.length - 1;
        TableColumn col = table.getColumnModel().getColumn(vColIndex);
        TableEditor mt = new TableEditor();
        col.setCellEditor(mt);
   

        panel.setLayout(new BorderLayout());
        ShowTable.frame.setJMenuBar(ShowTable.createMenuBar());
        ShowTable.frame.add(new JScrollPane( table ));
                                                                                                                                          
      //Insert the column combobox for filter
     JComboBox column = new JComboBox(columns);
     JLabel lOne = new JLabel("  Select column: ");


    column.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                   sortSelection = FilterFileActionPerformed(evt);
            
            }

        private String FilterFileActionPerformed(ActionEvent evt) {
                 JComboBox cb = (JComboBox)evt.getSource();
                 String selection = (String)cb.getSelectedItem();
                 return selection;
   
        }

        }); 
    
    filterText = new JTextField("Please enter the filter criteria!"); 
    JButton button = new JButton("Filter file");
    JLabel emptyspace = new JLabel(" ");
    JLabel emptyspace1 = new JLabel(" ");
    
    JLabel button2 = new JLabel( "                                                                                                   ");   
    JLabel button1 = new JLabel("                                                                                                                               ");
    //get the current array size 
    int ij = ShowTable.arrayOfArrays.size();
     if (ij >0) { 
     ArrayList<ScrippsGenomeAdviserUI.Reader> tempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
     tempArray = ShowTable.arrayOfArrays.get(FilterFunctions.currentArray);
     int arrayLenght = tempArray.size();
     int finalPage;
     int pag = arrayLenght/1000;  
     if (pag < 1) {
         pag = 1;
         finalPage = 1;
     } else {
         //adding one to the final page due to the fact that in most cases it won't be exact division by 1000
         finalPage = pag + 1;
     }
     
     //If this is the first page, page count needs to start at 1, just another error check
     if (onlyPage == 3) {
            FilterFunctions.pageNumber = 1;
            button2 = new JLabel( "1000 lines per page / Page " + FilterFunctions.pageNumber + " / "  + finalPage + " pages                                 ");     
        } else {
            button2 = new JLabel( "1000 lines per page / Page " + FilterFunctions.pageNumber + " / "  + finalPage + " pages                                 ");     
         }
     } 
     
     
    JLabel undo = new JLabel("Undo/Redo:");
    JButton advanceFilter = new JButton("Advanced Filter");
    advanceFilter.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
            
                  advanceFilterActionPerformed(evt);
              
            }

        private void advanceFilterActionPerformed(ActionEvent evt) {
                    InitGlobalVar();
                    tableStatus = 14;  
            if (threadStat == false) {
                     AdvanceFilter();    
                        } else {
                    threadExecutor.shutdownNow();
             //       heapSortAlgorithm.frame.dispose();
             //       heapSortAlgorithm.ArrayforSort = null;
                          AdvanceFilter();

                  }

        }
      });
          
    JButton firstPage = new JButton("|<<");
   
    //if this is the main array, no actions aloud
    if (FilterFunctions.currentArray == 0) {
       firstPage.setEnabled(false);
   } else {
    firstPage.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
            
        	  onlyPage = 0;
        	  UndoActionPerformed(evt);
              
            }

      });
   }
    JButton prevPage = new JButton("<");
    
    //if this is the main array, no actions alowed
    if (FilterFunctions.currentArray == 0) {
       prevPage.setEnabled(false);
   } else {
    prevPage.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
        	  
        	  onlyPage = 0;
              UndoDataActionPerformed(evt);
              
            }

      });
    }
    
    JButton nextPage = new JButton(">");
    
    //if this is the last filtered array, no actions alowed
    if (FilterFunctions.currentArray == ShowTable.arrayOfArrays.size() - 1) {
       nextPage.setEnabled(false);
   } else {
       nextPage.addActionListener(new java.awt.event.ActionListener() {
          @Override
       public void actionPerformed(java.awt.event.ActionEvent evt) {
              
        	  onlyPage = 0;
        	  NextFilterActionPerformed(evt);
              
            }

      });
    }
    
    JButton lastPage = new JButton(">>|");
    
    //if this is the last filtered array, no actions alowed
    if (FilterFunctions.currentArray == ShowTable.arrayOfArrays.size() - 1) {
       lastPage.setEnabled(false);
   } else {
       lastPage.addActionListener(new java.awt.event.ActionListener() {
          @Override
       public void actionPerformed(java.awt.event.ActionEvent evt) {
              
        	  onlyPage = 0;
        	  LastFilterActionPerformed(evt);
              
            }

      });
    }

    Border bored = BorderFactory.createRaisedBevelBorder();
    button.setBorder(bored);
    advanceFilter.setBorder(bored);
     
      button.addActionListener(new ActionListener() {
          @Override
      public void actionPerformed(ActionEvent e) {
        text = "";
        text = filterText.getText();
        String st = "Please enter the filter criteria!";
        if (text==st) {
          text = "";
      }
        if (text.length() > 0) {
            tableStatus = 15;
            InitGlobalVar();
            //kill the sort thread if it's hapening
            if (threadStat == false) {
                    FilteredArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
                 FilterFunctions rf2 = new FilterFunctions(sortSelection);
                 threadExecutor = Executors.newFixedThreadPool(1);
                 threadExecutor.execute(rf2);
                 threadExecutor.shutdown(); 
                        } else {
                    threadExecutor.shutdownNow();

                    FilteredArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
                    FilterFunctions rf2 = new FilterFunctions(sortSelection);
                    threadExecutor = Executors.newFixedThreadPool(1);
                    threadExecutor.execute(rf2);
                    threadExecutor.shutdown(); 
                  }           
         
        }
      }
    });
      
    JToolBar toolBar2 = new JToolBar();
    toolBar2.add(lOne);
    toolBar2.add(column);
    toolBar2.add(filterText);
    toolBar2.add(button);
    toolBar2.add(emptyspace);
    toolBar2.add(advanceFilter);
    toolBar2.add(emptyspace1);
    toolBar2.add(undo);
    firstPage.setToolTipText("Shows original data");
    toolBar2.add(firstPage);
    prevPage.setToolTipText("Shows previous filtered array");
    toolBar2.add(prevPage);
    nextPage.setToolTipText("Shows next filtered array");
    toolBar2.add(nextPage);
    lastPage.setToolTipText("Shows last filtered array");
    toolBar2.add(lastPage);
    ShowTable.frame.add(toolBar2, BorderLayout.NORTH);
    
    if(onlyPage == 0) {
    	Next = new JButton("Next Page");
		Previous = new JButton("First Page!");
		Previous.setEnabled(false);
		onlyPage = 4; 
        
    } else if (onlyPage == 1){
    	Next = new JButton("Single Page!");
		Previous = new JButton("              ");
		Next.setEnabled(false);
		Previous.setEnabled(false);
    
    } else if (onlyPage == 2){
    	Next = new JButton("Last Page!");
		Previous = new JButton("Previous Page");
		Next.setEnabled(false);
		Previous.setEnabled(true); // added by Neha
		onlyPage = 4;
    
    } else if (onlyPage == 3){
    	Next = new JButton("Next Page");
		Previous = new JButton("First Page!");
		Previous.setEnabled(false);
		onlyPage = 4;
    } else if (onlyPage == 4)
	{
		Next = new JButton("Next Page");
		Previous = new JButton("Previous Page");
		Next.setEnabled(true);
		Previous.setEnabled(true);

	}
     
    Next.addActionListener(new java.awt.event.ActionListener() {
  
           @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                 try {
                      Next.setEnabled(false);
                      NextPage();
                 } catch (InterruptedException ex) {
                     Logger.getLogger(ShowTable.class.getName()).log(Level.SEVERE, null, ex);
                 }
            }
 
     
    });
    Previous.addActionListener(new java.awt.event.ActionListener() {
  
           @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {

                    Previous.setEnabled(false);
                    PreviousPage();

            }
 
     
    });
         
        JToolBar toolBar = new JToolBar();
        toolBar.add(button1);
        toolBar.add(button2);
        
        if (onlyPage != 1) {
            toolBar.add(Previous);
            toolBar.add(Next);
        } else {
            JLabel button3 = new JLabel( "          ");   
            JLabel button4 = new JLabel( "Single page");  
            toolBar.add(button3);
            toolBar.add(button4);
            onlyPage = 0;
        }
        
        
        ShowTable.frame.add(toolBar, java.awt.BorderLayout.SOUTH);    
        ShowTable.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ShowTable.frame.pack();
        ShowTable.frame.setSize(frameWidth, frameHeight);
        ShowTable.frame.setVisible( true ); 
        ShowTable.frame.setTitle(title);
        ShowTable.frame.setLocationRelativeTo(null);
        //compenent to determine user resize
        ShowTable.frame.addComponentListener(new ComponentAdapter() {
        @Override
		public void componentResized(ComponentEvent e) {
            if(componentShown) {
             //  System.out.println("Component RESIZED");
               frameHeight = ShowTable.frame.getHeight();
               frameWidth = ShowTable.frame.getWidth();
             //  System.out.println("Frame height is: " + Integer.toString(frameHeight));
             //  System.out.println("Frame width is: " + Integer.toString(frameWidth));
            }  
        }

        @Override
		public void componentShown(ComponentEvent e) {
            componentShown = true;
        }
    });
       
}

*/
    
public static void PreviousPage() {
    FilterFunctions.pageNumber--;
    //System.out.println("Table status is: " + tableStatus);
    
    //if tableStatus == 23 this is the sorted table
    if (tableStatus == 23) {
          MergeSort.PreviousPage();
    }
        // if this is the main array after the entire array was loaded
    else if (tableStatus == 17 || tableStatus == -1) {
        //System.out.println("table Status: " + tableStatus);
        nextIterator = nextIterator - 1000;
        ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
        int end;
        if (nextIterator <= 0) {
            nextIterator = 0;
            //this is the first page
            onlyPage = 3;
        } else { end = nextIterator + 1000; }
        
        for (int i = nextIterator; i < nextIterator + 1000; i++) {
            TempArray.add(ReadFile.arrayOfLines.get(i));
        }    
             frame.dispose();   
             into2DArray(TempArray, TempArray.size());
    }
    
    // if tableStatus >2 this is the filtered table
    else if (tableStatus > 2 ) {     
      //if this is the last page change the filterCounter to reflect the number of
       if (onlyPage == 2) {
           filterCounter = filterCounter - 1000 - (FilteredArray.size() - nextIterator);
       } else {
       filterCounter = filterCounter - 2000;}
       
        nextIterator = nextIterator - 1000;
      //  previousPageIndex = previousPageIndex - 1000;
         
        ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
        
        //making sure next iterator never goes below 0 as well checking for first page
        int end;
        if (nextIterator  <= 0) {
            nextIterator = 0;
            end = nextIterator + 1000;
            onlyPage = 3;
        } else { 
            end = nextIterator + 1000; 
            }
        
        for (int i = nextIterator; i < end; i++) {
            TempArray.add(ShowTable.FilteredArray.get(i));
        }
        
         frame.dispose();
         if (FilterFunctions.currentArray == 0) {
             into2DArray(TempArray, TempArray.size());
         } else {
         into2DArrayFilterData(TempArray, TempArray.size());   
        }
      }
        
      
//this is the main table, in case if it wasn't entirelly loaded in memory which is almost imposible
if (tableStatus == 1)
    {
    String answer;
       if (page <= 1) {
      //     answer = "first";
        //   return answer;
       } 
       
   if(page > 2) {
        
        page = page - 1;
        
        nextPage = nextPage - 1000;

        ShowTable.frame.dispose();  
        Interface previousP = new Interface();
        previousP.fileName =  Interface.file.getName(); 
        BufferedReader bReader;
        try {
            bReader = new BufferedReader(
            new FileReader(Interface.file));
            String line = null;
            previousP.arrayOfLines = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
            int datacount = 0; 
            
        for (int in=0; in < nextPage + 1001; in++) {
                    
                    datacount = datacount + 1;
                    String n =null;             
                    n = bReader.readLine(); 
                    if (n == null){
                        break;
                    }
                    if (in == 0 && nextPage == 0) {
                        continue;
                    }

                    
                    if (in >= nextPage ) {                                            
                    String nt = Integer.toString(datacount - 1);
                    /*
                     * If this is a file that was previously analized using the UI
                     */  
                    if (Interface.analysedFile) {
                            String[] tempLine = n.split("\t");
                            String n2 = nt.concat("\t");
                            for (int st = 0; st<Interface.indexColumnNumber; st++) {
                                n2 = n2.concat(tempLine[st]).concat("\t");
                            } 
                            for (int st2 = Interface.indexColumnNumber+1; st2<tempLine.length; st2++) {
                                n2 = n2.concat(tempLine[st2]).concat("\t");
                            } 
                                ScrippsGenomeAdviserUI.Reader ob1 = new ScrippsGenomeAdviserUI.Reader(n2);
                                previousP.arrayOfLines.add(ob1);
                                //System.out.println("We are in next Page");
                            } else {
                                //Add "N/A" to begin of line since the file was not read entirely into memory yet
                                nt ="N/A" + "\t" + nt + "\t" + n;
                                ScrippsGenomeAdviserUI.Reader ob1 = new ScrippsGenomeAdviserUI.Reader(nt);
                                previousP.arrayOfLines.add(ob1);               
                       }
                       }
        }

     
     
        ShowTable nt = new ShowTable();
         nt.intoVector(previousP);
          } catch (IOException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        } 

     

       } 
    }

 //if this is the sorted table    
if (tableStatus == 2) {
                //String answer;

       if (pageSort > 1) {
            System.out.println("Page is: " + pageSort);
                pageSort = pageSort - 1;

                if (arraySize == 1000){
              //  nextPageSort = nextPageSort - 1000;
                nextPageSort = nextPageSort - 1000;
                } else {
                    nextPageSort = nextPageSort - 1000 - arraySize;
                }
                
                Object[][] SortedData;  
             
                SortedData  = new Object[1000][columns.length];
                
         for (int i = nextPageSort; i<nextPageSort + 1000; i++){

             int b = (int) heapSortAlgorithm.ArrayforSort[i][0];
             String lin  = ReadFile.arrayOfLines.get(b).fileRow;;

             String[] r = lin.split("\t");
               int size = r.length;
    
               for (int in = 1; in<size + 1; in++){                

                    SortedData[i-nextPageSort][in] = r[in];
                    
               
               }
    }

          ShowTable.frame.dispose();
          createContentTable(SortedData, columns, "Sorted " + ReadFile.fileName + " by column: " + heapSortAlgorithm.selection + " Page " + pageSort); 
 
    } 
             
}
      
}


          

public static void NextPage() throws InterruptedException {
    System.out.println("Table status is: " + tableStatus);
    FilterFunctions.pageNumber ++;
    //check to see if curent form is the sort form
     if (tableStatus == 23) {
          
             MergeSort.SortNext();
    
    }   
     else  if (tableStatus > 2 && tableStatus != 17 ) {
        nextIterator = nextIterator + 1000;
         
        ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
        
        //
        int end;
        if (nextIterator + 1000 >= FilteredArray.size()) {
            end = FilteredArray.size();
            //this is the last page
            onlyPage = 2;
        } else { end = nextIterator + 1000; }
        
        for (int i = nextIterator; i < end; i++) {
            TempArray.add(ShowTable.FilteredArray.get(i));
        }
        
         frame.dispose();
         if (FilterFunctions.currentArray == 0) {
             into2DArray(TempArray, TempArray.size());
         } else {
         into2DArrayFilterData(TempArray, TempArray.size());
         }
    }

    // if this is the main array after the entire array was loaded
    else if (tableStatus == 17 || tableStatus == -1) {
        nextIterator = nextIterator + 1000;
        ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
        int end;
        if (nextIterator + 1000 >= ReadFile.arrayOfLines.size()) {
            end = ReadFile.arrayOfLines.size();
            //this is the last page
            onlyPage = 2;
        } else { end = nextIterator + 1000; }
        
        for (int i = nextIterator; i < end; i++) {
            TempArray.add(ReadFile.arrayOfLines.get(i));
        }    
             frame.dispose();   
             into2DArray(TempArray, TempArray.size());
    }
           

// this is the main array before the entire file was loaded in memory    
else if (tableStatus == 1) {
   
    
        nextPage = nextPage + 1000;
        Interface nextP = new Interface();
        nextP.fileName =  Interface.file.getName(); 
        BufferedReader bReader;
        try {
            bReader = new BufferedReader(
            new FileReader(Interface.file));
            String line = null;
            nextP.arrayOfLines = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
            int datacount = 0; 
                     
      for (int in=0; in<nextPage + 1001; in++) {
                    
                    datacount = datacount + 1;
                    String n =null;             
                    n = bReader.readLine(); 
                    //if the end of file, set the next Page counter as the number of lines
                    if (n == null){
                        nextPage = datacount - 1;
                        //set the number of rows in that file
                        FileNumberRows = datacount;
                        break;
                    }
                    
                    if (in >= nextPage) {
                        
                       String nt = Integer.toString(in);
                         //if this file cwas previously analysed
                         if (Interface.analysedFile) {
                            String[] tempLine = n.split("\t");
                            String n2 = nt.concat("\t");
                            for (int st = 0; st<Interface.indexColumnNumber; st++) {
                                n2 = n2.concat(tempLine[st]).concat("\t");
                            } 
                            for (int st2 = Interface.indexColumnNumber+1; st2<tempLine.length; st2++) {
                                n2 = n2.concat(tempLine[st2]).concat("\t");
                            }
                            ScrippsGenomeAdviserUI.Reader ob1 = new ScrippsGenomeAdviserUI.Reader(n2);
                            nextP.arrayOfLines.add(ob1);
                            System.out.println("We are in next Page");
                            } else {
                 
                    // Add "N/A" at the begin of the line since we are reading from the phisical file and "N/A" was not added yet
                    nt = "N/A"+"\t"+ nt + "\t" + n;
                    ScrippsGenomeAdviserUI.Reader ob1 = new ScrippsGenomeAdviserUI.Reader(nt);
                    nextP.arrayOfLines.add(ob1);               
                         }
                  }
         }
     
//Make sure there is a next page to print out, if there is no more next page, don't do anything
 if (nextP.arrayOfLines.size() != 0) {
             page = page+1;
         ShowTable.frame.dispose();
         ShowTable nt = new ShowTable();
         nt.intoVector(nextP);
        }
 } catch (IOException ex) {
      Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
 } 
  
}  
}


public static JMenuBar createMenuBar() {
     

      JMenuBar menubar = new JMenuBar();
      
      JMenu file = new JMenu("File");
      JMenu edit = new JMenu("Edit");
      JMenu data = new JMenu("Sort");
      JMenu filters = new JMenu("Custom Filters");
      JMenu UndoData = new JMenu("Undo"); 
      JMenu statistics = new JMenu("Statistics");
      JMenu save = new JMenu("Save");
      JMenu help =new JMenu("Help");
      
      menubar.add(file);
      
      menubar.add(data);
      menubar.add(filters);
      menubar.add(save);
      menubar.add(statistics);
      menubar.add(help);
      
      JMenuItem exit = new JMenuItem("Exit Application");
      JMenuItem openVCF = new JMenuItem("Load & compare VCF file");
      JMenuItem openCG = new JMenuItem("Load & compare Complete Genomics file");
      JMenuItem sort = new JMenuItem("Sort");
      JMenuItem advanceFilter = new JMenuItem("Advance filter");
      JMenuItem saveAs = new JMenuItem("Save as...");
      JMenuItem undo =  new  JMenuItem("Original data");
      
      //Custom Filters 
      JMenuItem codingVar = new JMenuItem("Coding Variants");
      JMenuItem spliceVar = new JMenuItem("Coding & Splice Variants");
       JMenuItem codingVarFreq = new JMenuItem("Coding & Splice Variants with Frequency");
      JMenuItem knownDisease = new JMenuItem("Known Disease");
      JMenu predictedDisease = new JMenu("Known and Predicted Disease-Causing Variants");
      JMenuItem predClinical = new JMenuItem("Clinical");
      JMenuItem predResearch = new JMenuItem("Research");
      
      JMenuItem cancerGenes = new JMenuItem("Cancer Genes");
      JMenuItem pharmacogenetic = new JMenuItem("Pharmacogenetic");
      JMenuItem truncatedVariants = new JMenuItem("Truncating Variants");
      JMenuItem nondbSNP = new JMenuItem("Novel Variants");
      JMenuItem nondbSNPFreq = new JMenuItem("Non dbSNP Coding & Splice Variants with Frequency");
      JMenuItem chromPos = new JMenuItem("Chromosome Position");
      JMenuItem validValueFilter = new JMenuItem("Existing Valid Values");
      JMenuItem previousFilter = new JMenuItem("Previous step");
      JMenuItem stats = new JMenuItem("Statistics");
      JMenuItem helpMe = new JMenuItem("Help");
      JMenuItem idiomFilter = new JMenuItem("IDIOM filter");
     // JMenuItem geneList = new JMenuItem("Gene list filter"); 
      
      predictedDisease.add(predClinical);
      predictedDisease.add(predResearch);
      
      codingVar.setToolTipText("All variants impacting the protein coding sequence of a gene: i.e. all possible coding impacts except synonymous variants." );
      spliceVar.setToolTipText("Coding Variants plus variants annotated to damaged splice donor and acceptor sites.");
      codingVarFreq.setToolTipText("Coding and Splice Variants plus a user defined frequency threshold in as observed in the 1000 Genomes, 69 publically available Complete Genomics genomes and the Scripps Wellderly population.");
      knownDisease.setToolTipText("'1' values of the ADVISER-Score columns.");
      predClinical.setToolTipText("All entries in the column 'ADVISER Score Clinical/Disease Entry/Explanation' receiving a modified ADVISER categorization of 1, 2, or 2*. See http://genomics.scripps.edu/ADVISER/ADVISER_score.jsp for ADVISER scoring criteria.");
      predResearch.setToolTipText("All entries in the column 'ADVISER Score Research/Disease Entry/Explanation' receiving a modified ADVISER categorization of 1, 2 and 2*. See http://genomics.scripps.edu/ADVISER/ADVISER_score.jsp for ADVISER scoring criteria.");
      cancerGenes.setToolTipText("All genes annotated as cancer genes by either the Sanger Cancer Gene Census, Memorial Sloan Kettering Cancer Center or Atlas Oncology.");
      pharmacogenetic.setToolTipText("All variants curated by PharmGKB.");
      truncatedVariants.setToolTipText("All Frameshift and Nonsense variants.");
      nondbSNP.setToolTipText("Potential novel variants – i.e. variants not catalogued in dbSNP.");
      nondbSNPFreq.setToolTipText("Non dbSNP Coding & Splice Variants with Frequency.");
      chromPos.setToolTipText("All variants within a user defined region.");
      validValueFilter.setToolTipText("All variants that have valid values in a specific column except '-' and 'N/A'");
      idiomFilter.setToolTipText("All De-novo variants; Homozygous in child only and Compound Heterozygous variants.");
      //geneList.setToolTipText("Filters the variants present in a provided list of genes.");        
              
      filters.add(codingVar);
      filters.add(spliceVar);
      filters.add(codingVarFreq);
      filters.add(knownDisease);
      filters.add(predictedDisease);
      filters.add(cancerGenes);
      filters.add(pharmacogenetic);
      filters.add(truncatedVariants);
      filters.add(nondbSNP);
      filters.add(chromPos);
      filters.add(validValueFilter);
      filters.add(idiomFilter);
     // filters.add(geneList);
      statistics.add(stats);
      UndoData.add(previousFilter);
      UndoData.add(undo);
      
      
      file.add(exit);
      file.add(openVCF);
      file.add(openCG);
      data.add(sort);
      help.add(helpMe);
      save.add(saveAs);
    stats.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
            try {
                StatisticsActionPerformed(evt);
            } catch (IOException ex) {
                Logger.getLogger(ShowTable.class.getName()).log(Level.SEVERE, null, ex);
            }
            }

      });
    
    openCG.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
            try {
                OpenCGActionPerformed(evt);
            } catch (IOException ex) {
                Logger.getLogger(ShowTable.class.getName()).log(Level.SEVERE, null, ex);
            }
              
            }

      });
      openVCF.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
            
                  OpenVCFActionPerformed(evt);
              
            }

      });
      
     previousFilter.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
            
                  UndoDataActionPerformed(evt);
              
            }
               });
            
      undo.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
            
                  UndoActionPerformed(evt);
              
            }
               });
      helpMe.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                HelpActionPerformed(evt);
            }
      });
      
      
     /* 
      advanceFilter.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
            
                  advanceFilterActionPerformed(evt);
              
            }

        private void advanceFilterActionPerformed(ActionEvent evt) {
                    InitGlobalVar();
                    tableStatus = 14;  
            if (threadStat == false) {
                     AdvanceFilter();    
                        } else {
                    threadExecutor.shutdownNow();
                    heapSortAlgorithm.frame.dispose();
                    heapSortAlgorithm.ArrayforSort = null;
                          AdvanceFilter();

                  }

        }
      });
      */

      saveAs.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
              try {
                  SaveActionPerformed(evt);
              } catch (IOException ex) {
                  Logger.getLogger(FilterFunctions.class.getName()).log(Level.SEVERE, null, ex);
              }
            }
      });
      
      exit.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
               ExitActionPerformed(evt);
            }
});
      
     codingVar.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
              tableStatus = 3;              
              InitGlobalVar();
                   //for Next/previous pages need to know which filter hapened
             //if sort thread exists, kill it!
                if (threadStat == false) {
                      CodingVarActionPerformed(evt);
                        } else {
                        	try {
                        		threadExecutor.shutdownNow();
                        		MergeSort.frame.dispose();
                        		MergeSort.ArrayforSort = null;
                        	} catch(Exception ex) {
                        		System.out.println("");
                        	}
                    CodingVarActionPerformed(evt);
                  }
            }
});
     
     spliceVar.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                        //for Next/previous pages need to know which filter hapened
                        tableStatus = 4;
                        InitGlobalVar();
                        frequencySelection = -1.0;
                        frequencySelection2 = -1.0;
                        frequencySelection3 = -1.0;               
              //if sort thread exists, kill it!  
                    if (threadStat == false) {
                        SpliceVarActionPerformed(evt);    
                        } else {
                        	try {
                        		threadExecutor.shutdownNow();
                        		MergeSort.frame.dispose();
                        	}  catch( Exception ex) {
                        		System.out.println("");
                        	}
                        SpliceVarActionPerformed(evt);

                  }

            }
    });
     
    knownDisease.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                 InitGlobalVar();   
                 tableStatus = 5;
              //if sort thread exists, kill it!
                if (threadStat == false) {
                     KnownDiseaseActionPerformed(evt);
                        } else {
                        		try {
                        			threadExecutor.shutdownNow();
                        			MergeSort.frame.dispose();
                        			MergeSort.ArrayforSort = null;
                            	}  catch( Exception ex) {
                            		System.out.println("");
                            	}
                      KnownDiseaseActionPerformed(evt);
                  }

            }
    });
          
    
    predClinical.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                    tableStatus = 6; 
                    InitGlobalVar();
              //if sort thread exists, kill it!
                if (threadStat == false) {
                    PredClinicalActionPerformed(evt);
                        } else {
                        	try {
		                    threadExecutor.shutdownNow();
		                  //  heapSortAlgorithm.frame.dispose();
		                  //  heapSortAlgorithm.ArrayforSort = null;
		                      MergeSort.frame.dispose();
		                      MergeSort.ArrayforSort = null;
                        	}  catch( Exception ex) {
                        		System.out.println("");
                        	}
                    PredClinicalActionPerformed(evt);
                  }
            }
    });
    
    predResearch.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                    tableStatus = 7;    
                    InitGlobalVar();
              //if sort thread exists, kill it!
                if (threadStat == false) {
                    PredResearchActionPerformed(evt);
                        } else {
                        		try {
		                    threadExecutor.shutdownNow();
		                 //   heapSortAlgorithm.frame.dispose();
		                 //   heapSortAlgorithm.ArrayforSort = null;
		                    MergeSort.frame.dispose();
		                    MergeSort.ArrayforSort = null;
                          	}  catch( Exception ex) {
                            		System.out.println("");
                            	}
                    PredResearchActionPerformed(evt);
                  }
            }
    });
    
    cancerGenes.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                    tableStatus = 8; 
                    InitGlobalVar();
              //if sort thread exists, kill it!
                if (threadStat == false) {
                    CancerGenesActionPerformed(evt);
                        } else {
                        	try {
                        		threadExecutor.shutdownNow();
                        		//   heapSortAlgorithm.frame.dispose();
                        		//   heapSortAlgorithm.ArrayforSort = null;
                        		MergeSort.frame.dispose();
                        		MergeSort.ArrayforSort = null;
                         }  catch( Exception ex) {
                        		System.out.println("");
                        	}
                    CancerGenesActionPerformed(evt);
                  }

            }
    });
    
   pharmacogenetic.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
              tableStatus = 9; 
              InitGlobalVar();
              //if sort thread exists, kill it!
                if (threadStat == false) {
                    PharmacogeneticActionPerformed(evt);
                        } else {
                        		try {
			                    threadExecutor.shutdownNow();
			                    //heapSortAlgorithm.frame.dispose();
			                    //heapSortAlgorithm.ArrayforSort = null;
			                    MergeSort.frame.dispose();
			                    MergeSort.ArrayforSort = null;
                             }  catch( Exception ex) {
                            		System.out.println("");
                            	}
                    PharmacogeneticActionPerformed(evt);
                  }
            }
    });
   
   truncatedVariants.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                    tableStatus = 10; 
                    InitGlobalVar();
              //if sort thread exists, kill it!
                if (threadStat == false) {
                    TruncatedVariantsActionPerformed(evt);
                        } else {
                        		try {
			                    threadExecutor.shutdownNow();
			                    //heapSortAlgorithm.frame.dispose();
			                    //heapSortAlgorithm.ArrayforSort = null;
			                    MergeSort.frame.dispose();
			                    MergeSort.ArrayforSort = null;
                           	}  catch( Exception ex) {
                            		System.out.println("");
                            	}
                    TruncatedVariantsActionPerformed(evt);
                  }

            }
    });
      
   nondbSNP.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                    tableStatus = 11; 
                    InitGlobalVar();
              //if sort thread exists, kill it!
                if (threadStat == false) {
                    NondbSNPActionPerformed(evt);
                        } else {
                        	try {
		                    threadExecutor.shutdownNow();
		         //           heapSortAlgorithm.frame.dispose();
		           //         heapSortAlgorithm.ArrayforSort = null;
		                    MergeSort.frame.dispose();
		                    MergeSort.ArrayforSort = null;
                       	}  catch( Exception ex) {
                        		System.out.println("");
                        	}
                    NondbSNPActionPerformed(evt);
                  }

            }
    });
   
   codingVarFreq.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                    InitGlobalVar();
                    tableStatus =12; 
                    frequencySelection = -1.0;
                    frequencySelection2 = -1.0;
                    frequencySelection3 = -1.0;  
              //if sort thread exists, kill it!
                if (threadStat == false) {
                    CodingVarFreqActionPerformed(evt);
                        } else {
                        		try {
			                    threadExecutor.shutdownNow();
			                    //heapSortAlgorithm.frame.dispose();
			                    //heapSortAlgorithm.ArrayforSort = null;
			                    MergeSort.frame.dispose();
			                    MergeSort.ArrayforSort = null;
                           	}  catch( Exception ex) {
                            		System.out.println("");
                            	}
                    CodingVarFreqActionPerformed(evt);
                  }
            }
    });
   
   nondbSNPFreq.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                    tableStatus = 13;
                    InitGlobalVar();
                    frequencySelection = -1.0;
                    frequencySelection2 = -1.0;
                    frequencySelection3 = -1.0;  
              //if sort thread exists, kill it!
                if (threadStat == false) {
                    CodingVarFreqActionPerformed(evt);
                        } else {
                        		try {
			                    threadExecutor.shutdownNow();
			          //          heapSortAlgorithm.frame.dispose();
			            //        heapSortAlgorithm.ArrayforSort = null;
			                    MergeSort.frame.dispose();
			                    MergeSort.ArrayforSort = null;
                           	}  catch( Exception ex) {
                            		System.out.println("");
                            	}
                    CodingVarFreqActionPerformed(evt);
                  }
            }
    });
   
     chromPos.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                    tableStatus = 16; 
                    InitGlobalVar();
              //if sort thread exists, kill it!
                if (threadStat == false) {
                    ChromosomePosition(evt);
                        } else {
                        		try {
			                    threadExecutor.shutdownNow();
			               //     heapSortAlgorithm.frame.dispose();
			               //     heapSortAlgorithm.ArrayforSort = null;
			                    MergeSort.frame.dispose();
			                    MergeSort.ArrayforSort = null;
                           	}  catch( Exception ex) {
                            		System.out.println("");
                            	}
                    ChromosomePosition(evt);
                  }

            }
    });
     
        idiomFilter.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                    tableStatus = 21; 
                    InitGlobalVar();
              //if sort thread exists, kill it!
                if (threadStat == false) {               
                    IDIOM(evt);
                        } else {
                        		try {
		                        		threadExecutor.shutdownNow();
		            //        heapSortAlgorithm.frame.dispose();
		             //       heapSortAlgorithm.ArrayforSort = null;
		                  MergeSort.frame.dispose();
		                   MergeSort.ArrayforSort = null;
                          	}  catch( Exception ex) {
                            		System.out.println("");
                            	}
                    IDIOM(evt);
                  }

            }
    });
          
     validValueFilter.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                    tableStatus = 18; 
                    onlyPage = 0;
                    InitGlobalVar();
              //if sort thread exists, kill it!
                if (threadStat == false) {
                    ValidValFilter(evt);
                        } else {
                    threadExecutor.shutdownNow();
               //     heapSortAlgorithm.frame.dispose();
                //    heapSortAlgorithm.ArrayforSort = null;
                    MergeSort.frame.dispose();
                    MergeSort.ArrayforSort = null;
                    ValidValFilter(evt);
                  }

            }
    });
     
     /*
      geneList.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                    System.out.println("Gene list");
                    tableStatus = 22; 
                    InitGlobalVar();
              //if sort thread exists, kill it!
                if (threadStat == false) {               
                    gList(evt);
                        } else {
                    threadExecutor.shutdownNow();
            //        heapSortAlgorithm.frame.dispose();
              //      heapSortAlgorithm.ArrayforSort = null;
                    MergeSort.frame.dispose();
                    MergeSort.ArrayforSort = null;
                    gList(evt);
                  }

            }
    });
     */

      sort.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                FilterFunctions.pageNumber = 1;
               SortActionPerformed(evt);

            }
      });

      return menubar; 
    }

public static void HelpActionPerformed(java.awt.event.ActionEvent evt) {  
    
            new HelpMenu().setVisible(true);
     
}   

public static void InitGlobalVar() {
    //for next/previous filters
    if (FilterFunctions.currentArray != arrayOfArrays.size() - 1) {
        for ( int i = (FilterFunctions.currentArray + 1); i < arrayOfArrays.size(); i++) {
            //if it's not the last filtered array empty all arrays after
            arrayOfArrays.remove(i);
            FilterFunctions.filterName.remove(i);
            
        }
        
    } 
    //counts the number of raws in array for filter index
    filterCounter = 0;
    //counter for next page view
    nextIterator = 0;
    //counts how many pages were filtered to be able to label and disable buttons for single pages, first and last pages
    pageFilter= 0;
    //new array is initialized and/or old one is cleared if new filter criteria is started
    FilteredArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();

}
          
public static void UndoActionPerformed(java.awt.event.ActionEvent evt) {
    
     FilteredArray = arrayOfArrays.get(0);

     //change the curent array number
     FilterFunctions.currentArray = 0;
     FilterFunctions.pageNumber = 1;
       ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
          int end;
          if (FilteredArray.size() > 1000) 
          { 
        	  onlyPage = 0;
        	  end = 1000;
        	  }
          else {end = FilteredArray.size(); onlyPage = 1;}
          
          for (int i = 0; i < end; i++) {
              TempArray.add(FilteredArray.get(i));
          }
                              
                          frame.dispose();
                 
                             // into2DArray(TempArray, TempArray.size());
                 into2DArrayFilterData(TempArray, TempArray.size());
}

public static void NextFilterActionPerformed(java.awt.event.ActionEvent evt) {
   nextStep(); 
}

//funtion that displays next filtered array if any
public static void nextStep() {
     filterCounter = 0;


if (FilterFunctions.currentArray < arrayOfArrays.size() - 1) {
     FilterFunctions.pageNumber = 1;
     FilteredArray = arrayOfArrays.get(FilterFunctions.currentArray + 1);
     //change the curent array number
     FilterFunctions.currentArray = FilterFunctions.currentArray + 1;
       ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
          int end;
          if (FilteredArray.size() > 1000) 
          {
        	  onlyPage = 0;
        	  end = 1000;
        	  }
          else {end = FilteredArray.size(); onlyPage = 1;}
          
          for (int i = 0; i < end; i++) {
              TempArray.add(FilteredArray.get(i));
          }
                              
                          frame.dispose();
                         into2DArrayFilterData(TempArray, TempArray.size());
                          
}
}

public static void LastFilterActionPerformed(java.awt.event.ActionEvent evt) {
    
    lastFilter();

}

public static void lastFilter(){
      

if (FilterFunctions.currentArray < arrayOfArrays.size() - 1) {
     FilterFunctions.pageNumber = 1;
     filterCounter = 0;
     FilteredArray = arrayOfArrays.get(arrayOfArrays.size() - 1);
     //change the curent array number
     FilterFunctions.currentArray = arrayOfArrays.size() - 1;
       ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
          int end;
          if (FilteredArray.size() > 1000) 
          { 
        	  onlyPage = 0;
        	  end = 1000;
        	  }
          else {end = FilteredArray.size(); onlyPage = 1;}
          
          for (int i = 0; i < end; i++) {
              TempArray.add(FilteredArray.get(i));
          }
                              
                          frame.dispose();
                         into2DArrayFilterData(TempArray, TempArray.size());
                          
}
}

public static void UndoDataActionPerformed(java.awt.event.ActionEvent evt) {
    
    previousStep();


}

public static void previousStep() {
    filterCounter = 0;

if (FilterFunctions.currentArray > 0) {
     FilterFunctions.pageNumber = 1;
     FilteredArray = arrayOfArrays.get(FilterFunctions.currentArray - 1);
     //clear the last element
   //  arrayOfArrays.get(FilterFunctions.currentArray).clear();
     //change the curent array number
     FilterFunctions.currentArray = FilterFunctions.currentArray - 1;
       ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
          int end;
          if (FilteredArray.size() > 1000) 
          { 
        	  onlyPage = 0;
        	  end = 1000;
        	  }
          else {end = FilteredArray.size(); onlyPage = 1;}
          
          for (int i = 0; i < end; i++) {
              TempArray.add(FilteredArray.get(i));
          }
                              
                          frame.dispose();
                          if (FilterFunctions.currentArray == 0) {
                              into2DArray(TempArray, TempArray.size());
                          } else {
                         into2DArrayFilterData(TempArray, TempArray.size());
                          }
} 
}    

public static void OpenVCFActionPerformed(java.awt.event.ActionEvent evt){
       OpenVCF nt = new OpenVCF();
       //nt.openFile();
       threadExecutor = Executors.newFixedThreadPool(1);
       threadStat = true;
       threadExecutor.execute(nt);
       threadExecutor.shutdown();

}

public static void StatisticsActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
        Interface.statistics = 2;
        String head = "";
        for (int i=0; i<columns.length; i++) {
            head = head.concat(columns[i]).concat("\t");
        }
        Statistics stats = new Statistics(head, "Calculating Statistics...");
        threadExecutor = Executors.newFixedThreadPool(1);
        threadExecutor.execute(stats);
        threadExecutor.shutdown();
} 

public static void OpenCGActionPerformed(java.awt.event.ActionEvent evt) throws IOException{
       OpenCG nt = new OpenCG();
       nt.openFile();

}

//filters by 1
public static void CodingVarActionPerformed(java.awt.event.ActionEvent evt) { 
   FilterFunctions rf2 = new FilterFunctions("Coding Variants");
   threadExecutor = Executors.newFixedThreadPool(1);
   threadExecutor.execute(rf2);
   threadExecutor.shutdown();
}


  
//Filter by Coding_Variants. If exists coding_variants -. check Splice Variants. Print existing coding and splice variants.

public static void SpliceVarActionPerformed(java.awt.event.ActionEvent evt) {
   FilterFunctions rf2 = new FilterFunctions("Coding and Splice Variants");
   threadExecutor = Executors.newFixedThreadPool(1);
   threadExecutor.execute(rf2);
   threadExecutor.shutdown();       
}



//Known disease causing variants "1"s only
public static void  KnownDiseaseActionPerformed(java.awt.event.ActionEvent evt) {
      FilterFunctions rf2 = new FilterFunctions("Known disease");
      threadExecutor = Executors.newFixedThreadPool(1);
      threadExecutor.execute(rf2);
      threadExecutor.shutdown();  
}

public static void  PredClinicalActionPerformed(java.awt.event.ActionEvent evt) {
      FilterFunctions rf2 = new FilterFunctions("ADVISER_clinical");
      threadExecutor = Executors.newFixedThreadPool(1);
      threadExecutor.execute(rf2);
      threadExecutor.shutdown();  
   
}

public static void  PredResearchActionPerformed(java.awt.event.ActionEvent evt) {  
      FilterFunctions rf2 = new FilterFunctions("ADVISER_research");
      threadExecutor = Executors.newFixedThreadPool(1);
      threadExecutor.execute(rf2);
      threadExecutor.shutdown(); 
}


public static void  CancerGenesActionPerformed(java.awt.event.ActionEvent evt) {
      FilterFunctions rf2 = new FilterFunctions("Cancer Genes");
      threadExecutor = Executors.newFixedThreadPool(1);
      threadExecutor.execute(rf2);
      threadExecutor.shutdown(); 
}

public static void  PharmacogeneticActionPerformed(java.awt.event.ActionEvent evt) { 
      FilterFunctions rf2 = new FilterFunctions("PharmGKB");
      threadExecutor = Executors.newFixedThreadPool(1);
      threadExecutor.execute(rf2);
      threadExecutor.shutdown(); 
}


public static void  TruncatedVariantsActionPerformed(java.awt.event.ActionEvent evt) {
      FilterFunctions rf2 = new FilterFunctions("Truncating Variants");
      threadExecutor = Executors.newFixedThreadPool(1);
      threadExecutor.execute(rf2);
      threadExecutor.shutdown(); 
}


public static void  NondbSNPActionPerformed(java.awt.event.ActionEvent evt) { 
      FilterFunctions rf2 = new FilterFunctions("NondbSNP");
      threadExecutor = Executors.newFixedThreadPool(1);
      threadExecutor.execute(rf2);
      threadExecutor.shutdown(); 
}


public static  void SaveActionPerformed(ActionEvent evt) throws IOException {
                       
      SaveFilteredArray rf = new SaveFilteredArray();
      threadExecutor = Executors.newFixedThreadPool(1);
      threadStat = true;
      threadExecutor.execute(rf);
      threadExecutor.shutdown();

}
   
public static void CodingVarFreqActionPerformed(java.awt.event.ActionEvent evt) {
         final Interface demo1 = new Interface();  
         demo1.frame.setContentPane(demo1.createContentPane());
             
        //Display the window.
        demo1.frame.setSize(400, 200);


        demo1.frame.setLocationRelativeTo(null);
        demo1.frame.setVisible(true); 
        demo1.frame.setTitle("Enter Frequency");

        Double[] a = {0.0, 0.007, 0.008, 0.009, 0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
        JComboBox columns = new JComboBox(a);
        JComboBox columns2 = new JComboBox(a);
        JComboBox columns3 = new JComboBox(a);

        demo1.frame.add(new JLabel("Please select the frequency limit of 1000Genomes:"));
        demo1.frame.add(columns);
        demo1.frame.add(new JLabel("Please select the frequency limit of CG_69:"));
        demo1.frame.add(columns2);
        demo1.frame.add(new JLabel("Please select the frequency limit of CG_WELLDERLY:"));
        demo1.frame.add(columns3);
        
        final JButton sortFile = new JButton("Filter");
        demo1.frame.add(sortFile);        
        columns.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                    
              JComboBox cb = (JComboBox)evt.getSource();
              frequencySelection = (Double)cb.getSelectedItem();
      
            }

        });
        
       columns2.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                    
              JComboBox cb = (JComboBox)evt.getSource();
              frequencySelection2 = (Double)cb.getSelectedItem();
      
            }

        });
       
        columns3.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                    
              JComboBox cb = (JComboBox)evt.getSource();
              frequencySelection3 = (Double)cb.getSelectedItem();
      
            }

        });
        
        sortFile.addActionListener(new java.awt.event.ActionListener() {
  
           @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
               SpliceVarActionPerformed(evt);
               demo1.frame.dispose();
            }
 
     
    });

}

public static void ChromosomePosition(java.awt.event.ActionEvent evt) {
  ChromPosFilter fr = new ChromPosFilter();
  String[] a = {"", ""};
  ChromPosFilter.main(a);
}

public static void IDIOM(java.awt.event.ActionEvent evt) {
      IDIOMmultipleChildren fr = new IDIOMmultipleChildren(); 
      String[] a = {"", ""};
      IDIOMmultipleChildren.main(a);
  
}

public static void gList(java.awt.event.ActionEvent evt) {
      System.out.println("Gene list filter");    
      FilterFunctions rf2 = new FilterFunctions("Gene List");
      threadExecutor = Executors.newFixedThreadPool(1);
      threadExecutor.execute(rf2);
      threadExecutor.shutdown(); 
  
}


public static void ValidValFilter(java.awt.event.ActionEvent evt) {

         tableStatus = 20;   
         final Interface demo1 = new Interface();  
         demo1.frame.setContentPane(demo1.createContentPane());
             
        //Display the window.
        demo1.frame.setSize(400, 200);


        demo1.frame.setLocationRelativeTo(null);
        demo1.frame.setVisible(true); 
        demo1.frame.setTitle("Valid Values Filter");
        JComboBox col = new JComboBox(columnNames);
        demo1.frame.add(new JLabel("Please select the column you want to filter out the '-' and 'N/A':"));
        demo1.frame.add(col);
        final JButton filterFile = new JButton("  Filter!  ");
        demo1.frame.add(filterFile);        
        col.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                   FilterFunctions.ValidValColumn = SortFileActionPerformed(evt);
                   System.out.println("Selection is: " + FilterFunctions.ValidValColumn);
                } catch (IOException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
 
       //Start filtering once the button is pressed
       filterFile.addActionListener(new java.awt.event.ActionListener() {
                @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                          FilterFunctions rf2 = new FilterFunctions("Valid Values");
                          threadExecutor = Executors.newFixedThreadPool(1);
                          threadExecutor.execute(rf2);
                          threadExecutor.shutdown(); 
                          demo1.frame.dispose(); 
                }
 });
          

}


public static void AdvanceFilter() {
 
                  sort1 = null;
                  sort2 = null;
                  sort3 = null; 
                  
                  text1 = null;
                  text2 = null;
                  text3 = null;
                  FilteredArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
                  tableStatus = 14; 
                  onlyPage = 0;
                         
                  final Interface demo2 = new Interface();  
                  demo2.frame.setContentPane(demo2.createContentPane());    

                  demo2.frame.setTitle("Advance Filter");
                  
                  
                  JLabel label1 = new JLabel("Filter crideria 1: ");
                  demo2.frame.add(label1);
                  JComboBox col1 = new JComboBox(columns);
                  demo2.frame.add(col1);
                  col1.addActionListener(new java.awt.event.ActionListener() {
      
                      @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                  sort1 = FilterFileActionPerformed(evt);
            
            }

        private String FilterFileActionPerformed(ActionEvent evt) {
                 JComboBox cb = (JComboBox)evt.getSource();
                 String selection = (String)cb.getSelectedItem();
                 return selection;
   
        }}); 
                  final JTextField filterText1 = new JTextField("Please enter the 1st filter criteria!"); 
                  demo2.frame.add(filterText1);
                  
                  JLabel label2 = new JLabel("Filter crideria 2: ");
                  demo2.frame.add(label2);
                  JComboBox col2 = new JComboBox(columns);
                  demo2.frame.add(col2);
                  
                  col2.addActionListener(new java.awt.event.ActionListener() {
      
                      @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                  sort2 = FilterFileActionPerformed(evt);
            
            }

        private String FilterFileActionPerformed(ActionEvent evt) {
                 JComboBox cb = (JComboBox)evt.getSource();
                 String selection = (String)cb.getSelectedItem();
                 return selection;
   
        }}); 
                  
                  final JTextField filterText2 = new JTextField("Please enter the 2nd filter criteria!"); 
                  demo2.frame.add(filterText2);
                  
                  JLabel label3 = new JLabel("Filter crideria 3: ");
                  demo2.frame.add(label3);
                  JComboBox col3 = new JComboBox(columns);
                  
                  col3.addActionListener(new java.awt.event.ActionListener() {
      
                      @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                  sort3 = FilterFileActionPerformed(evt);
            
            }

        private String FilterFileActionPerformed(ActionEvent evt) {
                 JComboBox cb = (JComboBox)evt.getSource();
                 String selection = (String)cb.getSelectedItem();
                 return selection;
   
        }}); 
                  demo2.frame.add(col3);
                  final JTextField filterText3 = new JTextField("Please enter the 3rd filter criteria!"); 
                  demo2.frame.add(filterText3);
                  
                  JLabel logicalOp = new JLabel("Logical operator: ");
                  demo2.frame.add(logicalOp);
                  String[] something = {"AND", "OR"};
                  JComboBox optionBox = new JComboBox(something);
                  demo2.frame.add(optionBox);
                  optionBox.addActionListener(new java.awt.event.ActionListener() {
      
                      @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                  options = FilterFileActionPerformed(evt);
            
            }

        private String FilterFileActionPerformed(ActionEvent evt) {
                 JComboBox cb = (JComboBox)evt.getSource();
                 String selection = (String)cb.getSelectedItem();
                 return selection;
   
        }}); 
                  
                  
                //  JButton button = new JButton("Filter file");
                  advanceFilter = new JButton("Filter file");
                  demo2.frame.add(advanceFilter);
                  //Display the window.
                  demo2.frame.setSize(650, 200);
                  demo2.frame.setLocationRelativeTo(null);
                  demo2.frame.setVisible(true); 
                  advanceFilter.addActionListener(new ActionListener() {
          @Override
      public void actionPerformed(ActionEvent e) {

              
        text1 = filterText1.getText();
        text2 = filterText2.getText(); 
        text3 = filterText3.getText();
        
        System.out.println("Filter criteria 1 is: " + text1);
        String st = "Please enter the 1st filter criteria!";
        if (text1 == st) {
          text1 = "";
      }
        if (text1.length() > 0) {
           
                 if (options.equals("AND")) {
                 FilterFunctions rf2 = new FilterFunctions("Advanced filter (AND operator)");
                 threadExecutor = Executors.newFixedThreadPool(1);
                 threadExecutor.execute(rf2);
                 threadExecutor.shutdown(); 
                 demo2.frame.dispose();
                 } else {
                     tableStatus = 18;
                     FilterFunctions rf2 = new FilterFunctions("Advanced filter (OR operator)");
                     threadExecutor = Executors.newFixedThreadPool(1);
                     threadExecutor.execute(rf2);
                     threadExecutor.shutdown(); 
                     demo2.frame.dispose();
                 }
            
        } else {
            
           demo2.frame.dispose();
        }
      }
    });

   }

 
private static void ExitActionPerformed(java.awt.event.ActionEvent evt) {                                     
    System.exit(0);       
}



public static void SortActionPerformed(java.awt.event.ActionEvent evt) {
         tableStatus = -10; 
         /*
         final JFrame frame = new JFrame();
         JPanel panel = new JPanel();
         frame.setContentPane(panel);
         frame.setSize(400, 200);
         frame.setLocationRelativeTo(null);
         frame.setVisible(true); 
         frame.setTitle("Sort");
         JComboBox col = new JComboBox(columns);
         frame.add(new JLabel("Please select the column you want to sort by:"));
         frame.add(col);
         final JButton sortFile = new JButton("  Sort!  ");
         frame.add(sortFile);
         */
         final Interface demo1 = new Interface();  
         demo1.frame.setContentPane(demo1.createContentPane());
             
        //Display the window.
        demo1.frame.setSize(400, 200);


        demo1.frame.setLocationRelativeTo(null);
        demo1.frame.setTitle("Sort");
        JComboBox col = new JComboBox(columns);
        demo1.frame.add(new JLabel("Please select the column you want to sort by:"));
        demo1.frame.add(col);
        final JButton sortFile = new JButton("  Sort!  ");
        demo1.frame.add(sortFile);
        demo1.frame.setVisible(true); 
        col.addActionListener(new java.awt.event.ActionListener() {
          @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                   sortSelection = SortFileActionPerformed(evt);
                   System.out.println("Selection is: " + sortSelection);
                } catch (IOException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
 
 //Start sorting once the button is pressed
 sortFile.addActionListener(new java.awt.event.ActionListener() {
                
                 ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
                 ArrayList<ScrippsGenomeAdviserUI.Reader> TempArrayOne = ShowTable.arrayOfArrays.get(FilterFunctions.currentArray);
                 
           @Override
           public void actionPerformed(java.awt.event.ActionEvent evt) {
                    System.out.println("Size of array is: "+ TempArrayOne.size());
                    //make sure entire file is loaded before sorting starts
                    if (Interface.threadExecutor.isTerminated()) {
                    sortFile.setEnabled(false);
                    demo1.frame.dispose();
                   
                 //check to see if another sorting is happening
                  if (threadStat == false) {
                         //   System.out.println("This is the first sort");
                     //  heapSortAlgorithm rf = new heapSortAlgorithm(ReadFile.arrayOfLines, ReadFile.fileLines, sortSelection, h);
                      if (sortStat == 1) {
                       threadExecutor.shutdownNow(); 
                   //    heapSortAlgorithm.frame.dispose();
                  //     heapSortAlgorithm.ArrayforSort = null;
 
                       //make sure the previous thread was terminated 
                      while (!threadExecutor.isTerminated()) {
                           System.out.println("Previous sort is being terminated");
                       }
                      }

               //       if (threadExecutor.isTerminated()) {
                       sortStat =0;
                       System.out.println("This is the first thread");
                     //  heapSortAlgorithm rf = new heapSortAlgorithm(TempArrayOne, TempArrayOne.size(), sortSelection);
                       MergeSort rf = new MergeSort(TempArrayOne, TempArrayOne.size(), sortSelection);
                       threadExecutor = Executors.newFixedThreadPool(1);
                       threadStat = true;
                       threadExecutor.execute(rf);
                       threadExecutor.shutdown();
                       demo1.frame.dispose();
                 //     }
                      
                  } else if (threadStat == true){
                            System.out.println("This is the second thread");
                       threadExecutor.shutdownNow();
               //        heapSortAlgorithm.frame.dispose();
               //        heapSortAlgorithm.ArrayforSort = null;
                       MergeSort.frame.dispose();
                       MergeSort.ArrayforSort = null;
                       while (!threadExecutor.isTerminated()) {
                           System.out.println("Previous sort is being terminated");
                       }
                       if (threadExecutor.isTerminated()) {
                  //     heapSortAlgorithm rf2 = new heapSortAlgorithm(TempArrayOne, TempArrayOne.size(), sortSelection);     
                       MergeSort rf2 = new MergeSort(TempArrayOne, TempArrayOne.size(), sortSelection);
                  //     threadStat = false;
                  //     sortStat=1;
                       threadExecutor = Executors.newFixedThreadPool(1);
                       threadExecutor.execute(rf2);
                       threadExecutor.shutdown();
                       frame.dispose();
                       }
                        }
                    }
           }
 
     
    });

}
       
public static String SortFileActionPerformed(java.awt.event.ActionEvent evt) throws IOException{
        
        JComboBox cb = (JComboBox)evt.getSource();
        sortSelection = (String)cb.getSelectedItem();
        return sortSelection;
   
    }


    @Override
    public void run() {

    }

      
    private static javax.swing.JButton Previous;
    private static javax.swing.JButton Next;
    private static javax.swing.JButton advanceFilter;
}


