/**
 * Interface for the SG_Adviser UI
 * Galina Erikson
 * Research Associate - Translational Science Research Institute
 * Version_1.6_8
 * May 29, 2014
 */

package org.scripps.cnvViewer;

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

import org.scripps.utils.CnvFilterFunctions;
import org.scripps.utils.CnvHeader;
import org.scripps.utils.CnvHelpMenu;
import org.scripps.utils.CnvReadFile;
import org.scripps.utils.CnvReader;
import org.scripps.utils.CnvShowTable;
import org.scripps.utils.CnvStatistics;

public class CnvViewerInterface extends javax.swing.JFrame
{

	private static final long serialVersionUID = 1L;
	public JFrame frame;
	public static javax.swing.JPanel jPanel1;
	public ArrayList<CnvReader> arrayOfLines;
	public String fileName;
	static int fileLines;
	public CnvHeader head;
	public static String tempHeadForPrefilter = "";
	public static File cnvFile;

	// First fifty lines made public for future access by UNDO button
	public static CnvViewerInterface viewFiftyLines;

	// Thread management variable;
	public static ExecutorService threadExecutor;

	/*
	 * counter telling the stats module if the file is already loaded in memory
	 * or we can't calculate the stats before file is loaded 1 = before file is
	 * loaded 17 = file is loaded
	 */
	public static int statistics = 0;

	// This file already was analysed using the UI
	public static boolean cnvAnalysedFile = false;
	public static int indexColumnNumber;

	public CnvViewerInterface()
	{
		fileChooser = new javax.swing.JFileChooser();
		// JFrame.setDefaultLookAndFeelDecorated(true);
		frame = new JFrame();
	}

	private JPanel initComponents()
	{

		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/images/ScrippsProgram.jpg"))); // NOI18N

		org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				jPanel1Layout
						.createSequentialGroup()
						.add(jLabel1,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								1010,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.add(0, 0, Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(jLabel1,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				org.jdesktop.layout.GroupLayout.TRAILING,
				layout.createSequentialGroup()
						.add(jPanel1,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				org.jdesktop.layout.GroupLayout.TRAILING, jPanel1,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		jPanel1.setBackground(new Color(0xa5d4f2));
		pack();
		return jPanel1;

	}

	private void ExitActionPerformed(java.awt.event.ActionEvent evt)
	{
		System.exit(0);
	}

	public void startCnvViewerInterface(File file)
	{
		boolean adviser_file = false;
		BufferedReader bReader;
		try
		{
			bReader = new BufferedReader(new FileReader(file));
			String line = null;
			viewFiftyLines = new CnvViewerInterface();
			viewFiftyLines.arrayOfLines = new ArrayList<CnvReader>();
			for (int i = 0; i < 1; i++)
			{
				line = bReader.readLine();
				String l = "";
				/*
				 * Check to see if this is a SG-ADviser line:
				 */

				if (line.startsWith("Haplotype")
						|| line.startsWith("Chromosome"))
				{

					adviser_file = true;
					/*
					 * Check to see if the line ends in comments (did it
					 * come out of UI already)
					 */
					if (line.contains("Comments"))
					{
						l = "Index".concat("\t");
						cnvAnalysedFile = true;
						String[] tempL = line.split("\t");
						for (int ij = 0; ij < tempL.length; ij++)
						{
							if (tempL[ij].contains("Index"))
							{
								indexColumnNumber = ij;
							} else
							{
								l = l.concat(tempL[ij]).concat("\t");
							}
						}
					} else
					{
						l =  "Index" + "\t" + line;
								
					}

					viewFiftyLines.head = new CnvHeader(l);
				}

			}

			// If this is a sg_adviser file continue
			if (adviser_file)
			{
				long fileInBytes = file.length();
				System.out.println("Filelenght is: " + fileInBytes);
				double fileinGB = fileInBytes / 1000000000;
				System.out.println("File lenght in gb is " + fileinGB);

				if (fileinGB > 3)
				{
					Prefilter(fileinGB);

				} else
				{

					for (int in = 1; in < 1001; in++)
					{

						String fileLine = null;
						fileLine = bReader.readLine();
						if (fileLine == null)
						{
							CnvShowTable.onlyPage = 1;
							/*
							 * Less then 1000 lines, remove next/previous
							 * pages
							 */

							break;
						} else
						{


							// if user tries to load a previously annotated
							// file, get rid of the old index
							if (cnvAnalysedFile)
							{

								String[] tempLine = fileLine.split("\t");
								String nt = Integer.toString(in).concat(
										"\t");
								for (int st = 0; st < indexColumnNumber; st++)
								{
									nt = nt.concat(tempLine[st]).concat(
											"\t");
								}
								for (int st2 = indexColumnNumber + 1; st2 < tempLine.length; st2++)
								{
									nt = nt.concat(tempLine[st2]).concat(
											"\t");
								}
								CnvReader ob1 = new CnvReader(nt);
								viewFiftyLines.arrayOfLines.add(ob1);
							} else
							{

								String nt;
								nt = in + "\t" + fileLine;
								CnvReader ob1 = new CnvReader(nt);
								viewFiftyLines.arrayOfLines.add(ob1);
							}
							
						}
					}

					// Save the original object for future reference (Undo
					// button)
					CnvShowTable nt = new CnvShowTable();
					nt.intoVector(viewFiftyLines);

					CnvReadFile rf = new CnvReadFile(file);
					threadExecutor = Executors.newFixedThreadPool(1);
					threadExecutor.execute(rf);
					threadExecutor.shutdown();
					this.frame.dispose();
				}
			} else
			{
				CnvViewerInterface demo1 = new CnvViewerInterface();
				demo1.frame.setContentPane(demo1.createContentPane());
				// Display the window.
				demo1.frame.setSize(500, 200);
				demo1.frame.setLocationRelativeTo(null);
				demo1.frame
						.add(new JLabel(
								"This is not a SG ADVISER annotated file, please select a correct file! "));
				demo1.frame
						.add(new JLabel(
								"                                                                       "));
				demo1.frame
						.add(new JLabel(
								"  Go to http://genomics.scripps.edu/ADVISER/Result_Desc.jsp to see     "));
				demo1.frame
						.add(new JLabel(
								"                                                                       "));
				demo1.frame
						.add(new JLabel(
								"             example of a SG ADVISER annotated file.                   "));
				demo1.frame.setVisible(true);
			
			}
		} catch (IOException ex)
		{
			Logger.getLogger(CnvViewerInterface.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}
	public void OpenFile()
	{

		// variable the checks if this is a sg_adviser file
		boolean adviser_file = false;
		viewFiftyLines = new CnvViewerInterface();
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			cnvFile = fileChooser.getSelectedFile();
			viewFiftyLines.fileName = cnvFile.getName();
			Long siz = cnvFile.getTotalSpace();
			System.out.println("File size is: " + siz);
			System.out.println("SG-Adviser UI_1.6_7");
			BufferedReader bReader;
			try
			{
				bReader = new BufferedReader(new FileReader(cnvFile));

				String line = null;
				viewFiftyLines.arrayOfLines = new ArrayList<CnvReader>();
				for (int i = 0; i < 1; i++)
				{
					line = bReader.readLine();
					String l = "";
					/*
					 * Check to see if this is a SG-ADviser line:
					 */

					if (line.startsWith("Haplotype")
							|| line.startsWith("Chromosome"))
					{

						adviser_file = true;
						/*
						 * Check to see if the line ends in comments (did it
						 * come out of UI already)
						 */
						if (line.contains("Comments"))
						{
							l = "Index".concat("\t");
							cnvAnalysedFile = true;
							String[] tempL = line.split("\t");
							for (int ij = 0; ij < tempL.length; ij++)
							{
								if (tempL[ij].contains("Index"))
								{
									indexColumnNumber = ij;
								} else
								{
									l = l.concat(tempL[ij]).concat("\t");
								}
							}
						} else
						{
							l =  "Index" + "\t" + line;
									
						}

						viewFiftyLines.head = new CnvHeader(l);
					}

				}

				// If this is a sg_adviser file continue
				if (adviser_file)
				{
					long fileInBytes = cnvFile.length();
					System.out.println("Filelenght is: " + fileInBytes);
					double fileinGB = fileInBytes / 1000000000;
					System.out.println("File lenght in gb is " + fileinGB);

					if (fileinGB > 3)
					{
						Prefilter(fileinGB);

					} else
					{

						for (int in = 1; in < 1001; in++)
						{

							String n = null;
							n = bReader.readLine();
							if (n == null)
							{
								CnvShowTable.onlyPage = 1;
								/*
								 * Less then 1000 lines, remove next/previous
								 * pages
								 */

								break;
							} else
							{

								// if user tries to load a previously annotated
								// file, get rid of the old index
								if (cnvAnalysedFile)
								{
									String[] tempLine = n.split("\t");
									String nt = Integer.toString(in).concat(
											"\t");
									for (int st = 0; st < indexColumnNumber; st++)
									{
										nt = nt.concat(tempLine[st]).concat(
												"\t");
									}
									for (int st2 = indexColumnNumber + 1; st2 < tempLine.length; st2++)
									{
										nt = nt.concat(tempLine[st2]).concat(
												"\t");
									}
									CnvReader ob1 = new CnvReader(nt);
									viewFiftyLines.arrayOfLines.add(ob1);
								} else
								{
									String nt;
									nt = in + "\t" + n;
									CnvReader ob1 = new CnvReader(nt);
									viewFiftyLines.arrayOfLines.add(ob1);
								}
							}
						}

						// Save the original object for future reference (Undo
						// button)
						CnvShowTable nt = new CnvShowTable();
						nt.intoVector(viewFiftyLines);

						CnvReadFile rf = new CnvReadFile(cnvFile);
						threadExecutor = Executors.newFixedThreadPool(1);
						threadExecutor.execute(rf);
						threadExecutor.shutdown();
						this.frame.dispose();
					}
				} else
				{
					CnvViewerInterface demo1 = new CnvViewerInterface();
					demo1.frame.setContentPane(demo1.createContentPane());
					// Display the window.
					demo1.frame.setSize(500, 200);
					demo1.frame.setLocationRelativeTo(null);
					demo1.frame
							.add(new JLabel(
									"This is not a SG ADVISER CNV annotated file, please select a correct file! "));
					demo1.frame
							.add(new JLabel(
									"                                                                       "));
					demo1.frame
							.add(new JLabel(
									"  Go to http://genomics.scripps.edu/ADVISER/Result_Desc_CNV.jsp to see     "));
					demo1.frame
							.add(new JLabel(
									"                                                                       "));
					demo1.frame
							.add(new JLabel(
									"             example of a SG ADVISER CNV annotated file.                   "));
					demo1.frame.setVisible(true);
					
				}
			} catch (IOException ex)
			{
				Logger.getLogger(CnvViewerInterface.class.getName()).log(
						Level.SEVERE, null, ex);
			}

		}

	}

	public void noFilter() throws FileNotFoundException, IOException
	{
		BufferedReader bReader;

		bReader = new BufferedReader(new FileReader(cnvFile));

		String line = null;
		viewFiftyLines.arrayOfLines = new ArrayList<CnvReader>();
		for (int i = 0; i < 1; i++)
		{
			line = bReader.readLine();
			String l =  "Index" + "\t" + line; 
			viewFiftyLines.head = new CnvHeader(l);

		}

		for (int in = 1; in < 1001; in++)
		{

			String n = null;
			n = bReader.readLine();
			if (n == null)
			{
				break;
			} else
			{

				String nt;
				nt = in + "\t" + n; 
				CnvReader ob1 = new CnvReader(nt);
				viewFiftyLines.arrayOfLines.add(ob1);

			}
		}
		bReader.close();

		// Save the original object for future reference (Undo button)
		CnvShowTable nt = new CnvShowTable();
		nt.intoVector(viewFiftyLines);
		CnvReadFile rf = new CnvReadFile(cnvFile);
		threadExecutor = Executors.newFixedThreadPool(1);
		threadExecutor.execute(rf);
		threadExecutor.shutdown();
		this.frame.dispose();
	}

	public void StatsFile() throws IOException
	{
		statistics = 1;
		String line = null;
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			cnvFile = fileChooser.getSelectedFile();
			BufferedReader bReader = null;
			try
			{
				bReader = new BufferedReader(new FileReader(cnvFile));
			} catch (IOException ex)
			{
				Logger.getLogger(CnvViewerInterface.class.getName()).log(
						Level.SEVERE, null, ex);
			}
			try
			{
				line = bReader.readLine();
			} catch (IOException ex)
			{
				Logger.getLogger(CnvReadFile.class.getName()).log(Level.SEVERE,
						null, ex);
			}

			for (int i = 0; i < 1; i++)
			{
			}
			CnvStatistics stats = new CnvStatistics(line,
					"Calculating Statistics");
			threadExecutor = Executors.newFixedThreadPool(1);
			threadExecutor.execute(stats);
			threadExecutor.shutdown();

		}

	}

	public void Prefilter(double fileSize)
	{
		String line = null;

		BufferedReader bReader = null;
		try
		{
			bReader = new BufferedReader(new FileReader(cnvFile));
		} catch (IOException ex)
		{
			Logger.getLogger(CnvViewerInterface.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		try
		{
			line = bReader.readLine();
		} catch (IOException ex)
		{
			Logger.getLogger(CnvReadFile.class.getName()).log(Level.SEVERE,
					null, ex);
		}

		for (int i = 0; i < 1; i++)
		{
			tempHeadForPrefilter = line;
			CnvShowTable.columnNames = line.split("\t");
		}

		final CnvViewerInterface demo2 = new CnvViewerInterface();
		demo2.frame.setContentPane(demo2.createContentPane());
		demo2.frame.setTitle("Advance Filter");
		JLabel label1 = new JLabel("The file size you are trying to upload is "
				+ fileSize + "GB.");
		demo2.frame.add(label1);
		double heapSpace = fileSize * 2 + 1;
		JLabel label2 = new JLabel(
				"To successfully view and analyze this file  you will need at least "
						+ heapSpace + "GB RAM memory.");
		demo2.frame.add(label2);
		JLabel label3 = new JLabel(
				"We advise you to prefilter the file by using our custom filter.");
		demo2.frame.add(label3);
		JButton yes = new JButton("YES");
		JButton no = new JButton("NO");
		JLabel label4 = new JLabel(
				"Do you want to prefilter the file using the Coding variants filter?");
		demo2.frame.add(label4);
		demo2.frame.add(yes);
		demo2.frame.add(no);
		demo2.frame.setSize(650, 200);
		demo2.frame.setLocationRelativeTo(null);
		demo2.frame.setVisible(true);
		yes.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				CnvShowTable.tableStatus = 19;
				CnvFilterFunctions rf2 = new CnvFilterFunctions(
						"Coding Variants");
				threadExecutor = Executors.newFixedThreadPool(1);
				threadExecutor.execute(rf2);
				threadExecutor.shutdown();
				demo2.frame.dispose();

			}
		});

		no.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					noFilter();
					demo2.dispose();
				} catch (FileNotFoundException ex)
				{
					Logger.getLogger(CnvViewerInterface.class.getName()).log(
							Level.SEVERE, null, ex);
				} catch (IOException ex)
				{
					Logger.getLogger(CnvViewerInterface.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		});

	}

	public void OpenActionPerformed(java.awt.event.ActionEvent evt)
	{

		// Prefilter();
		OpenFile();

	}

	public void StatsActionPerformed(java.awt.event.ActionEvent evt)
	{
		try
		{
			StatsFile();
		} catch (IOException ex)
		{
			Logger.getLogger(CnvViewerInterface.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	public void HelpActionPerformed(java.awt.event.ActionEvent evt)
	{

		new CnvHelpMenu().setVisible(true);

	}

	public JPanel createContentPane()
	{

		JPanel totalGUI = new JPanel();
		totalGUI.setBackground(new Color(202, 225, 250));
		totalGUI.setMinimumSize(new Dimension(300, 300));
		totalGUI.setPreferredSize(new Dimension(300, 300));
		totalGUI.setMaximumSize(new Dimension(300, 00));
		totalGUI.setOpaque(true);
		return totalGUI;
	}

	public JMenuBar createMenuBar()
	{

		JMenuBar menubar = new JMenuBar();

		JMenu file = new JMenu("File");
		JMenu Statistics = new JMenu("Statistics");
		JMenu help = new JMenu("Help");

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

		exit.addActionListener(new java.awt.event.ActionListener()
		{
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				ExitActionPerformed(evt);
			}
		});

		open.addActionListener(new java.awt.event.ActionListener()
		{
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				OpenActionPerformed(evt);
			}
		});

		stats.addActionListener(new java.awt.event.ActionListener()
		{
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				StatsActionPerformed(evt);
			}
		});

		helpMe.addActionListener(new java.awt.event.ActionListener()
		{
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				HelpActionPerformed(evt);
			}
		});

		return menubar;
	}

	private static void createAndShowGUI()
	{

		CnvViewerInterface demo = new CnvViewerInterface();
		demo.frame.setContentPane(demo.initComponents());
		demo.frame.setJMenuBar(demo.createMenuBar());
		demo.frame.setTitle("Scripps Genome ADVISER");
		demo.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		demo.frame.pack();
		demo.frame.setVisible(true);
		demo.frame.setLocationRelativeTo(null);
	}

	public static void main(String[] args)
	{

		try
		{

			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels())
			{
				if ("Nimbus".equals(info.getName()))
				{
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}

		} catch (ClassNotFoundException ex)
		{
			System.out.println("ClassNotFoundException ex");
		} catch (InstantiationException ex)
		{
			System.out.println("InstantiationException ex");
		} catch (IllegalAccessException ex)
		{
			System.out.println("IllegalAccessException ex");

		} catch (javax.swing.UnsupportedLookAndFeelException ex)
		{
			System.out.println("UnsupportedLookAndFeelException");

		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				createAndShowGUI();
			}
		});
	}

	// Variables declaration - do not modify
	public static javax.swing.JFileChooser fileChooser;
	private javax.swing.JLabel jLabel1;
	// End of variables declaration

}
