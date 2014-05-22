/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scripps.utils;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

import org.scripps.cnvViewer.CnvViewerInterface;



/**
 * 
 * @author gerikson
 */
public class CnvFilterFunctions implements Runnable
{
	public static javax.swing.JFileChooser fileChooser;
	public static JFrame frame;
	public static Container content;
	public static JProgressBar progressBar;
	public static Border border;
	public static int progressIndicator;
	public static int pageNumber = 1;

	public static int currentArray = 0;
	// for ability to print current filter table
	public static ArrayList<String> filterName = new ArrayList<String>();
	public static ArrayList<String> geneList = new ArrayList<String>();
	public static String ValidValColumn = "";

	// creating a set for the compound heterozygous filter
	public static Set<String> compoundSet = new HashSet<String>();
	public Map<String, ArrayList<CnvCompoundHeteroObj>> GeneMap = new HashMap<String, ArrayList<CnvCompoundHeteroObj>>();

	public CnvFilterFunctions(String s)
	{
		frame = new JFrame("File filtering");
		content = frame.getContentPane();
		progressBar = new JProgressBar();
		// border = BorderFactory.createTitledBorder("Entire file filtering by "
		// + s);
		border = BorderFactory.createTitledBorder(s);
		progressBar.setBorder(border);
		content.add(progressBar, BorderLayout.NORTH);
		frame.setSize(300, 100);
		progressIndicator = 0;
	}

	public static void CodingVar()
	{

		int colForSort = 0;
		int counterCod = 0;

		// create new temporary array to store just the view for this page
		ArrayList<CnvReader> TempArray = new ArrayList<CnvReader>();
		ArrayList<CnvReader> TempArrayOne = CnvShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// find which is the column named Coding_Impact
		for (int i = 0; i < CnvShowTable.columnNames.length; i++)
		{

			if (CnvShowTable.columnNames[i].contains("Coding_Impact"))
			{
				// System.out.println("We will be selecting by column: ");
				// System.out.println(CnvShowTable.columnNames[i]);
				colForSort = i;
			}
		}

		int end = TempArrayOne.size();
		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = (int) (linesLeft / 100) * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		CnvSaveProgress pr = new CnvSaveProgress();

		for (int i = 0; i < end; i++)
		{

			lineCount++;
			// check the percentage, if true increment the progress bar
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				progressIndicator = progressIndicator + 3;
				progressBar.setValue(progressIndicator);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;

			String[] l = line.split("\t");
			String sortingColumn = l[colForSort];
			if (sortingColumn.toLowerCase().contains("partial")
					|| sortingColumn.toLowerCase().contains("frame")
					|| sortingColumn.toLowerCase().contains("amplified")
					|| sortingColumn.toLowerCase().contains("deletion")
					|| sortingColumn.toLowerCase().contains("deleted"))
			{

				counterCod++;
				CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
			}

		}

		if (counterCod > 1000)
		{
			end = 1000;
			CnvShowTable.onlyPage = 0; 

		} else
		{
			end = CnvShowTable.FilteredArray.size();
			CnvShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(CnvShowTable.FilteredArray.get(i));
		}
		frame.dispose();
		CnvShowTable.frame.dispose();

		currentArray = currentArray + 1;
		filterName.add("Coding Impact");
		CnvShowTable.arrayOfArrays.add(CnvShowTable.FilteredArray);
		CnvShowTable.into2DArrayFilterData(TempArray, end);

	}

	public static void CodingVarFreq()
	{

		int codingImpCol = 0;
		int spliceCol = 0;
		int counter = 0;
		int thousandGenGain = 0;
		int thousandGenLoss = 0;
		int GC69 = 0;
		int gcWellLoss = 0;
		int gcWellGain = 0;
		int dbSNP = 0;
		String[] columnData;
		
		
		ArrayList<CnvReader> TempArray = new ArrayList<CnvReader>();
		ArrayList<CnvReader> TempArrayOne = CnvShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		int end = TempArrayOne.size();

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = (int) (linesLeft / 100) * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		CnvSaveProgress pr = new CnvSaveProgress();

		// find which is the column named Coding_Impact
		for (int i = 0; i < CnvShowTable.columnNames.length; i++)
		{
			if (CnvShowTable.columnNames[i].contains("Coding_Impact"))
			{
				codingImpCol = i;
			}
			else if (CnvShowTable.columnNames[i].contains("1000genomes_Loss"))
			{

				thousandGenLoss = i;
			}

			else if (CnvShowTable.columnNames[i].contains("1000genomes_Gain"))
			{
				thousandGenGain = i;
			}

			else if (CnvShowTable.columnNames[i].contains("Wellderly_Loss"))
			{
				gcWellLoss = i;
			}

			else if (CnvShowTable.columnNames[i].contains("Wellderly_Gain"))
			{
				gcWellGain = i;
			}

			
		}

		for (int i = 0; i < end; i++)
		{
			lineCount++;
			// check the percentage, if true increment the progress bar
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				progressIndicator = progressIndicator + 3;
				progressBar.setValue(progressIndicator);
				progressBar.setStringPainted(true);
			}


			String line = TempArrayOne.get(i).fileRow;
			columnData = line.split("\t");
			String codingImpactVal = columnData[codingImpCol];			
			String thousandGenGainVal = columnData[thousandGenGain];
			String thousandGenLossVal = columnData[thousandGenLoss];
			String wellderlyGainVal = columnData[gcWellGain];
			String wellderlyLossVal = columnData[gcWellLoss];
			
			String[] tokensThousandGenGain = thousandGenGainVal.split("~|/");
			String[] tokensWellderlyGain = wellderlyGainVal.split("~|/");
			String[] tokensThousandGenLoss = thousandGenLossVal.split("~|/");
			String[] tokensWellderlyLoss = wellderlyLossVal.split("~|/");
			
			
			double genGainAlleleFreq; //= new Double(tokensThousandGenGain[3]).doubleValue();
			double wellderlyGainAlleleFreq;// = new Double(tokensWellderlyGain[3]).doubleValue();
			double genLossAlleleFreq;// = new Double(tokensThousandGenLoss[3]).doubleValue();
			double wellderlyLossAlleleFreq; // = new Double(tokensWellderlyLoss[3]).doubleValue();
			
			
			
			//get coding impact result array
			if (codingImpactVal.toLowerCase().contains("partial")
					|| codingImpactVal.toLowerCase().contains("frame")
					|| codingImpactVal.toLowerCase().contains("amplified")
					|| codingImpactVal.toLowerCase().contains("deletion")
					|| codingImpactVal.toLowerCase().contains("deleted"))
			{
				//CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
				//if Cnvs are not in both Wellderly and 1000G then add it to 
				//filtered array because it is a newly discovered CnV
				if (tokensThousandGenGain[1].equals("-")
						&& tokensThousandGenLoss[1].equals("-")
						&& tokensWellderlyGain[1].equals("-")
						&& tokensWellderlyLoss[1].equals("-")
						&& tokensThousandGenGain[2].equals("-")
						&& tokensThousandGenLoss[2].equals("-")
						&& tokensWellderlyGain[2].equals("-")
						&& tokensWellderlyLoss[2].equals("-")
						&& tokensThousandGenGain[3].equals("-")
						&& tokensThousandGenLoss[3].equals("-")
						&& tokensWellderlyGain[3].equals("-")
						&& tokensWellderlyLoss[3].equals("-"))
				{
					CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
					continue;
				}
				//check if wellderly loss has overlap > 80
				
				//check if wellderly gain  has overlap > 80
				
				//check if 1000gen loss  has overlap > 80
				
				//check if 1000gen gain  has overlap > 80
				
				else if (!tokensThousandGenGain[1].equals("-")
						|| !tokensThousandGenGain[2].equals("-")
						|| !tokensThousandGenGain[3].equals("-"))
				{
					if((new Integer(tokensThousandGenGain[1])).intValue() > 80
						&& (new Integer(tokensThousandGenGain[2])).intValue() > 80)
						{
						System.out.println(tokensThousandGenGain[3]);
							genGainAlleleFreq = new Double(tokensThousandGenGain[3]).doubleValue();
							if (genGainAlleleFreq < CnvShowTable.thousandGenFrequencySelection)
								CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
						}
				}
				
				else if( !tokensThousandGenLoss[1].equals("-") 
						|| !tokensThousandGenLoss[2].equals("-")
						|| !tokensThousandGenLoss[3].equals("-"))
				{
					if ((new Integer(tokensThousandGenLoss[1])).intValue() > 80
							&& (new Integer(tokensThousandGenLoss[2])).intValue() > 80)
							{
								genLossAlleleFreq = new Double(tokensThousandGenLoss[3]).doubleValue();
								if (genLossAlleleFreq < CnvShowTable.thousandGenFrequencySelection)
									CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
							}
							
				}
				
				else if( !tokensWellderlyGain[1].equals("-")
						|| !tokensWellderlyGain[2].equals("-")
						|| !tokensWellderlyGain[3].equals("-"))
				{
					if ((new Integer(tokensWellderlyGain[1])).intValue() > 80
						&& (new Integer(tokensWellderlyGain[2])).intValue() > 80)
						{
						wellderlyGainAlleleFreq = new Double(tokensWellderlyGain[3]).doubleValue();
							if (wellderlyGainAlleleFreq < CnvShowTable.wellderlyFrequencySelection)
								CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
						}
				}
				
				else if(!tokensWellderlyLoss[1].equals("-")
						|| !tokensWellderlyLoss[2].equals("-")
						|| !tokensWellderlyLoss[3].equals("-"))
				{
					if((new Integer(tokensWellderlyLoss[1])).intValue() > 80
						&& (new Integer(tokensWellderlyLoss[2])).intValue() > 80)
						{
							
							wellderlyLossAlleleFreq = new Double(tokensWellderlyLoss[3]).doubleValue();
							if (wellderlyLossAlleleFreq < CnvShowTable.wellderlyFrequencySelection)
								CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
						}
				}
				
			}
	
		
			
			// if it's from custom filter 11
			if (CnvShowTable.tableStatus == 13)
			{
				String t = columnData[dbSNP];
				if (!t.equals("-"))
				{
					continue;
				}
			}

			
		}
			if (counter > 1000)
			{
				end = 1000;
				CnvShowTable.onlyPage = 0; 
			} else
			{
				end = CnvShowTable.FilteredArray.size();
				CnvShowTable.onlyPage = 1;
			}
	

		if (CnvShowTable.FilteredArray.size() != 0)
		{
			for (int i = 0; i < end; i++)
			{
				TempArray.add(CnvShowTable.FilteredArray.get(i));
			}
			CnvShowTable.frame.dispose();
			
			if (CnvShowTable.tableStatus == 12)
			{
				filterName.add("Coding Variants plus frequency");
			}
			if (CnvShowTable.tableStatus == 13)
			{
				filterName.add("Non dbSNP, Coding Variants with frequency");
			}
			currentArray = currentArray + 1;
			CnvShowTable.arrayOfArrays.add(CnvShowTable.FilteredArray);
			CnvShowTable.into2DArrayFilterData(TempArray, end);
		}
		frame.dispose();

	

		
		
	}

	

	public static void KnownDisease()
	{
		int colForSort = 0;
		int counter = 0;

		ArrayList<CnvReader> TempArray = new ArrayList<CnvReader>();
		ArrayList<CnvReader> TempArrayOne = CnvShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		int end = TempArrayOne.size();
		// find which is the column named ADVISER_Clinical...
		for (int i = 0; i < CnvShowTable.columnNames.length; i++)
		{
			if (CnvShowTable.columnNames[i].contains("ADVISER_Score"))
			{
				// System.out.println("We will be selecting by column: ");
				// System.out.println(columnNames[i]);
				colForSort = i;
			}
		}

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = (int) (linesLeft / 100) * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		CnvSaveProgress pr = new CnvSaveProgress();

		for (int i = 0; i < end; i++)
		{
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				progressIndicator = progressIndicator + 3;
				progressBar.setValue(progressIndicator);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String sortingColumn = l[colForSort];
			String[] g = sortingColumn.split("~");
			if (g[0].equals("1"))
			{
				counter++;
				CnvShowTable.FilteredArray.add(TempArrayOne.get(i));

			}

		}

		if (counter > 1000)
		{
			end = 1000;
			CnvShowTable.onlyPage = 0; 
		} else
		{
			end = CnvShowTable.FilteredArray.size();
			CnvShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(CnvShowTable.FilteredArray.get(i));
		}
		frame.dispose();
		CnvShowTable.frame.dispose();

		filterName.add("Disease Variants");
		currentArray = currentArray + 1;
		CnvShowTable.arrayOfArrays.add(CnvShowTable.FilteredArray);
		CnvShowTable.into2DArrayFilterData(TempArray, end);
	}

	public static void PredClinical()
	{
		int colForSort = 0;
		int counterClinnical = 0;

		ArrayList<CnvReader> TempArray = new ArrayList<CnvReader>();
		ArrayList<CnvReader> TempArrayOne = CnvShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		int end = TempArrayOne.size();

		// progress bar calculation
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = (int) (linesLeft / 100) * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		CnvSaveProgress pr = new CnvSaveProgress();

		// find which is the column named ADVISER_Clinical...
		for (int i = 0; i < CnvShowTable.columnNames.length; i++)
		{
			if (CnvShowTable.columnNames[i].contains("ADVISER_Score_Clinical"))
			{
				System.out.println("We will be selecting by column: ");
				System.out.println(CnvShowTable.columnNames[i]);
				colForSort = i;
			}
		}

		for (int i = 0; i < end; i++)
		{

			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				progressIndicator = progressIndicator + 3;
				progressBar.setValue(progressIndicator);
				progressBar.setStringPainted(true);
			}
			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String sortingColumn = l[colForSort];
			String[] letter = sortingColumn.split("~");
			String letterOne = letter[0];

			if (letterOne.contains("1") || letterOne.contains("2"))
			{
				counterClinnical++;
				CnvShowTable.FilteredArray.add(TempArrayOne.get(i));

			}

		}
		// verify if we have more then 1000 lines in the filtered array to print
		if (counterClinnical > 1000)
		{
			end = 1000;
			CnvShowTable.onlyPage = 0; 
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = CnvShowTable.FilteredArray.size();
			CnvShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(CnvShowTable.FilteredArray.get(i));
		}
		// dispose the progress bar
		frame.dispose();

		CnvShowTable.frame.dispose();

		// Insert newly filtered array

		filterName.add("Predicted Clinical Variants");
		// change the count of current array
		currentArray = currentArray + 1;
		CnvShowTable.arrayOfArrays.add(CnvShowTable.FilteredArray);
		CnvShowTable.into2DArrayFilterData(TempArray, end);
	}

	public static void PredResearch()
	{
		int colForSort = 0;

		int counterResearch = 0;

		// this array is for representing first 1000 lines
		ArrayList<CnvReader> TempArray = new ArrayList<CnvReader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<CnvReader> TempArrayOne = CnvShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();

		// progress bar calculation
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = (int) (linesLeft / 100) * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		CnvSaveProgress pr = new CnvSaveProgress();

		// find which is the column named ADVISER_Clinical...
		for (int i = 0; i < CnvShowTable.columnNames.length; i++)
		{
			if (CnvShowTable.columnNames[i].contains("ADVISER_Score_Research"))
			{
				System.out.println("We will be selecting by column: ");
				System.out.println(CnvShowTable.columnNames[i]);
				colForSort = i;
			}
		}

		for (int i = 0; i < end; i++)
		{
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				progressIndicator = progressIndicator + 3;
				progressBar.setValue(progressIndicator);
				progressBar.setStringPainted(true);
			}
			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String sortingColumn = l[colForSort];

			String[] letter = sortingColumn.split("~");
			String letterOne = letter[0];

			if (letterOne.contains("1") || letterOne.contains("2"))
			{
				counterResearch++;
				// insert the reference to the found element into the filtered
				// array
				CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
			}

		}

		// verify if we have more then 1000 lines in the filtered array to print
		if (counterResearch > 1000)
		{
			end = 1000;
			CnvShowTable.onlyPage = 0; 
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = CnvShowTable.FilteredArray.size();
			CnvShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(CnvShowTable.FilteredArray.get(i));
		}
		// dispose the progress bar
		frame.dispose();

		CnvShowTable.frame.dispose();

		filterName.add("Predicted Research Variants");
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		CnvShowTable.arrayOfArrays.add(CnvShowTable.FilteredArray);
		CnvShowTable.into2DArrayFilterData(TempArray, end);

	}

	public static void CancerGenes()
	{
		int colForSort = 0;
		int colForSort2 = 0;
		int colForSort3 = 0;
		// int colForSort4 = 0;
		// int colForSort5 = 0;

		int counterResearch = 0;

		// this array is for representing first 1000 lines
		ArrayList<CnvReader> TempArray = new ArrayList<CnvReader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<CnvReader> TempArrayOne = CnvShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();

		// find which is the column named ADVISER_Clinical...
		for (int i = 0; i < CnvShowTable.columnNames.length; i++)
		{

			if (CnvShowTable.columnNames[i].contains("MSKCC_CancerGenes"))
			{
				// System.out.println("We will be selecting by column: ");
				// System.out.println(columnNames[i]);
				colForSort = i;
			}

			if (CnvShowTable.columnNames[i].contains("Atlas_Oncology"))
			{
				// System.out.println("We will be selecting by column: ");
				// System.out.println(columnNames[i]);
				colForSort2 = i;
			}

			if (CnvShowTable.columnNames[i].contains("Sanger_CancerGenes"))
			{
				// System.out.println("We will be selecting by column: ");
				// System.out.println(columnNames[i]);
				colForSort3 = i;
			}

		}

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = (int) (linesLeft / 100) * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		CnvSaveProgress pr = new CnvSaveProgress();

		for (int i = 0; i < end; i++)
		{
			// progress bar
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				progressIndicator = progressIndicator + 3;
				progressBar.setValue(progressIndicator);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String sortingColumn = l[colForSort];
			if (Character.isUpperCase(sortingColumn.charAt(0))
					|| Character.isLetterOrDigit(sortingColumn.charAt(0)))
			{
				counterResearch++;
				// insert the reference to the found element into the filtered
				// array
				CnvShowTable.FilteredArray.add(TempArrayOne.get(i));

			} else if (Character.isUpperCase(l[colForSort2].charAt(0))
					|| Character.isLetterOrDigit(l[colForSort2].charAt(0)))
			{
				counterResearch++;
				// insert the reference to the found element into the filtered
				// array
				CnvShowTable.FilteredArray.add(TempArrayOne.get(i));

			} else if (Character.isUpperCase(l[colForSort3].charAt(0))
					|| Character.isLetterOrDigit(l[colForSort3].charAt(0)))
			{
				counterResearch++;
				// insert the reference to the found element into the filtered
				// array
				CnvShowTable.FilteredArray.add(TempArrayOne.get(i));

			}

		}

		// verify if we have more then 1000 lines in the filtered array to print
		if (counterResearch > 1000)
		{
			end = 1000;
			CnvShowTable.onlyPage = 0;
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = CnvShowTable.FilteredArray.size();
			CnvShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(CnvShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();
		CnvShowTable.frame.dispose();
		filterName.add("Cancer Genes");
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		CnvShowTable.arrayOfArrays.add(CnvShowTable.FilteredArray);
		CnvShowTable.into2DArrayFilterData(TempArray, end);

	}

	public static void Pharmacogenetic()
	{
		int colForSort = 0;
		int counterPharm = 0;

		// this array is for representing first 1000 lines
		ArrayList<CnvReader> TempArray = new ArrayList<CnvReader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<CnvReader> TempArrayOne = CnvShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();
		// find which is the column named ADVISER_Clinical...
		for (int i = 0; i < CnvShowTable.columnNames.length; i++)
		{
			if (CnvShowTable.columnNames[i].contains("PharmGKB"))
			{
				// System.out.println("We will be selecting by column: ");
				// System.out.println(columnNames[i]);
				colForSort = i;
			}
		}
		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = (int) (linesLeft / 100) * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		CnvSaveProgress pr = new CnvSaveProgress();

		for (int i = 0; i < end; i++)
		{
			// progress bar
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				progressIndicator = progressIndicator + 3;
				progressBar.setValue(progressIndicator);
				progressBar.setStringPainted(true);
			}
			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String sortingColumn = l[colForSort];
			if (Character.isUpperCase(sortingColumn.charAt(0))
					|| Character.isLetterOrDigit(sortingColumn.charAt(0)))
			{
				counterPharm++;
				// insert the reference to the found element into the filtered
				// array
				CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
			}
		}
		// verify if we have more then 1000 lines in the filtered array to print
		if (counterPharm > 1000)
		{
			end = 1000;
			CnvShowTable.onlyPage = 0;
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = CnvShowTable.FilteredArray.size();
			CnvShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(CnvShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();
		CnvShowTable.frame.dispose();

		filterName.add("Pharmacogenetic");
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		CnvShowTable.arrayOfArrays.add(CnvShowTable.FilteredArray);
		CnvShowTable.into2DArrayFilterData(TempArray, end);

	}

	public static void TruncatedVariants()
	{
		int sortPharmGKB = 0;
		int sortLocation = 0;
		int sortCodingImpact = 0;
		int counterPharm = 0;

		// this array is for representing first 1000 lines
		ArrayList<CnvReader> TempArray = new ArrayList<CnvReader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<CnvReader> TempArrayOne = CnvShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();
		// find which is the column named ADVISER_Clinical...
		for (int i = 0; i < CnvShowTable.columnNames.length; i++)
		{
			if (CnvShowTable.columnNames[i].contains("PharmGKB"))
			{
				// System.out.println("We will be selecting by column: ");
				// System.out.println(columnNames[i]);
				sortPharmGKB = i;
			}
		}

		// find which is the column named ADVISER_Clinical...
		for (int i = 0; i < CnvShowTable.columnNames.length; i++)
		{
			if (CnvShowTable.columnNames[i].contains("Coding_Impact"))
			{
				// System.out.println("We will be selecting by column: ");
				// System.out.println(columnNames[i]);
				sortCodingImpact = i;
			}
		}

		// find which is the column named Location
		for (int i = 0; i < CnvShowTable.columnNames.length; i++)
		{
			if (CnvShowTable.columnNames[i].contains("Location"))
			{
				// System.out.println("We will be selecting by column: ");
				// System.out.println(columnNames[i]);
				sortLocation = i;
			}
		}

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = (int) (linesLeft / 100) * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		CnvSaveProgress pr = new CnvSaveProgress();

		for (int i = 0; i < end; i++)
		{
			// progress bar
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				progressIndicator = progressIndicator + 3;
				progressBar.setValue(progressIndicator);
				progressBar.setStringPainted(true);
			}
			String line = TempArrayOne.get(i).fileRow;
			String[] columnData = line.split("\t");
			String sortColCoding = columnData[sortCodingImpact];
			String sortColLocation = columnData[sortLocation];
			if (sortColCoding.contains("Frameshift"))
			{
				// insert the reference to the found element into the filtered
				// array
				CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
			}

			else if (sortColLocation.contains("N_Terminal")
					|| sortColLocation.contains("C_Terminal"))
			{
				// split the column into its tokens separated by ///
				CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
			}

			// sortingColumn = columnData [sortPharmGKB];
			// to do
		}
		// verify if we have more then 1000 lines in the filtered array to print
		if (counterPharm > 1000)
		{
			end = 1000;
			CnvShowTable.onlyPage = 0;
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = CnvShowTable.FilteredArray.size();
			CnvShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(CnvShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();
		CnvShowTable.frame.dispose();

		filterName.add("Truncated Variants");
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		CnvShowTable.arrayOfArrays.add(CnvShowTable.FilteredArray);
		CnvShowTable.into2DArrayFilterData(TempArray, end);
	}

	public static void NondbSNP()
	{
		int colForSort = 0;
		int counterPharm = 0;

		// this array is for representing first 1000 lines
		ArrayList<CnvReader> TempArray = new ArrayList<CnvReader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<CnvReader> TempArrayOne = CnvShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();

		// find which is the column named ADVISER_Clinical...
		for (int i = 0; i < CnvShowTable.columnNames.length; i++)
		{
			if (CnvShowTable.columnNames[i].contains("dbSNP_ID"))
			{
				// System.out.println("We will be selecting by column: ");
				// System.out.println(columnNames[i]);
				colForSort = i;
			}
		}

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = (int) (linesLeft / 100) * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		CnvSaveProgress pr = new CnvSaveProgress();

		for (int i = 0; i < end; i++)
		{

			// progress bar
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				progressIndicator = progressIndicator + 3;
				progressBar.setValue(progressIndicator);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String sortingColumn = l[colForSort];
			if (sortingColumn.equals("-"))
			{
				// insert the reference to the found element into the filtered
				// array
				CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
			}

		}
		// verify if we have more then 1000 lines in the filtered array to print
		if (counterPharm > 1000)
		{
			end = 1000;
			CnvShowTable.onlyPage = 0;
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = CnvShowTable.FilteredArray.size();
			CnvShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(CnvShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();
		CnvShowTable.frame.dispose();

		filterName.add("Non dbSNP");
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		CnvShowTable.arrayOfArrays.add(CnvShowTable.FilteredArray);
		CnvShowTable.into2DArrayFilterData(TempArray, end);

	}

	public static void AdvanceFilterAlgorithmOR()
	{
		int colForSort = 0;
		int colForSort2 = 0;
		int colForSort3 = 0;

		int counterResearch = 0;

		// this array is for representing first 1000 lines
		ArrayList<CnvReader> TempArray = new ArrayList<CnvReader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<CnvReader> TempArrayOne = CnvShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();

		// Find the number of the first selection
		for (int i = 0; i < CnvShowTable.columns.length; i++)
		{
			if (CnvShowTable.columns[i] == CnvShowTable.sort1)
			{
				// System.out.println("Found the first column!");
				// System.out.println(columns[i]);
				colForSort = i;
			}
		}

		// Find the number of the second selection
		for (int i = 0; i < CnvShowTable.columns.length; i++)
		{
			if (CnvShowTable.columns[i] == null ? CnvShowTable.sort2 == null
					: CnvShowTable.columns[i].equals(CnvShowTable.sort2))
			{
				// System.out.println(CnvShowTable.columns[i]);
				colForSort2 = i;
			}
		}

		// Find the number of the third selection
		for (int i = 0; i < CnvShowTable.columns.length; i++)
		{
			if (CnvShowTable.columns[i] == null ? CnvShowTable.sort3 == null
					: CnvShowTable.columns[i].equals(CnvShowTable.sort3))
			{
				// System.out.println("Found a third column!");
				// System.out.println(columns[i]);
				colForSort3 = i;
			}
		}

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = (int) (linesLeft / 100) * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		CnvSaveProgress pr = new CnvSaveProgress();

		for (int i = 0; i < end; i++)
		{
			// progress bar
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				progressIndicator = progressIndicator + 3;
				progressBar.setValue(progressIndicator);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String t = l[colForSort];
			// boolean match;
			t = t.replace("\\^\\w+", "");
			CnvShowTable.text1 = CnvShowTable.text1.replace("\\s+?", "");

			String t2 = l[colForSort2];
			t2 = t2.replace("\\^\\w+", "");
			CnvShowTable.text2 = CnvShowTable.text2.replace("\\s+?", "");

			String t3 = l[colForSort3];
			t3 = t3.replace("\\^\\w+", "");
			CnvShowTable.text3 = CnvShowTable.text3.replace("\\s+?", "");

			if (t.contains(CnvShowTable.text1))
			{
				counterResearch++;
				// insert the reference to the found element into the filtered
				// array
				CnvShowTable.FilteredArray.add(TempArrayOne.get(i));

			} else if (t2.contains(CnvShowTable.text2))
			{
				counterResearch++;
				// insert the reference to the found element into the filtered
				// array
				CnvShowTable.FilteredArray.add(TempArrayOne.get(i));

			} else if (t3.contains(CnvShowTable.text3))
			{
				counterResearch++;
				// insert the reference to the found element into the filtered
				// array
				CnvShowTable.FilteredArray.add(TempArrayOne.get(i));

			}

		}

		// verify if we have more then 1000 lines in the filtered array to print
		if (counterResearch > 1000)
		{
			end = 1000;
			CnvShowTable.onlyPage = 0;
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = CnvShowTable.FilteredArray.size();
			CnvShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(CnvShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();
		CnvShowTable.frame.dispose();
		filterName.add(" OR logical operator");
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		CnvShowTable.arrayOfArrays.add(CnvShowTable.FilteredArray);
		CnvShowTable.into2DArrayFilterData(TempArray, end);
	}

	public static void AdvanceFilterAlgorithm()
	{

		ArrayList<CnvReader> TempArray = new ArrayList<CnvReader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<CnvReader> TempArrayOne = CnvShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();

		int counter = 0;
		int colNumber1 = 0;
		int colNumber2 = 0;
		int colNumber3 = 0;

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = (int) (linesLeft / 100) * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		CnvSaveProgress pr = new CnvSaveProgress();

		// Find the number of the first selection
		for (int i = 0; i < CnvShowTable.columns.length; i++)
		{
			if (CnvShowTable.columns[i] == CnvShowTable.sort1)
			{
				// System.out.println("Found the first column!");
				// System.out.println(columns[i]);
				colNumber1 = i;
			}
		}

		// Find the number of the second selection
		for (int i = 0; i < CnvShowTable.columns.length; i++)
		{
			if (CnvShowTable.columns[i] == null ? CnvShowTable.sort2 == null
					: CnvShowTable.columns[i].equals(CnvShowTable.sort2))
			{
				// System.out.println(CnvShowTable.columns[i]);
				colNumber2 = i;
			}
		}

		// Find the number of the third selection
		for (int i = 0; i < CnvShowTable.columns.length; i++)
		{
			if (CnvShowTable.columns[i] == null ? CnvShowTable.sort3 == null
					: CnvShowTable.columns[i].equals(CnvShowTable.sort3))
			{
				// System.out.println("Found a third column!");
				// System.out.println(columns[i]);
				colNumber3 = i;
			}
		}

		String st2 = "Please enter the 2nd filter criteria!";
		if (CnvShowTable.text2.equals(st2))
		{
			CnvShowTable.text2 = "";
		}

		String st3 = "Please enter the 3rd filter criteria!";
		if (CnvShowTable.text3.equals(st3))
		{
			CnvShowTable.text3 = "";
		}

		for (int i = 0; i < end; i++)
		{

			// progress bar
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				progressIndicator = progressIndicator + 3;
				progressBar.setValue(progressIndicator);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String t = l[colNumber1];
			boolean match;
			t = t.replace("\\^\\w+", "");
			CnvShowTable.text1 = CnvShowTable.text1.replace("\\s+?", "");

			if (match = t.toLowerCase().contains(
					CnvShowTable.text1.toLowerCase()))
			{

				if (CnvShowTable.sort2 == null)
				{
					// insert the reference to the found element into the
					// filtered array
					CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
					counter++;
					// else verify the 3nd criteria
				} else
				{
					String t2 = l[colNumber2];
					boolean match2;
					t2 = t2.replace("\\^\\w+", "");
					CnvShowTable.text2 = CnvShowTable.text2
							.replace("\\s+?", "");

					if (match2 = t2.toLowerCase().contains(
							CnvShowTable.text2.toLowerCase()))
					{
						// System.out.println("Found a second match!");
						// if there is no 3rd criteria print
						if (CnvShowTable.text3 == null)
						{
							// insert the reference to the found element into
							// the filtered array
							CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
							counter++;
						} else
						{
							String t3 = l[colNumber3];
							boolean match3;
							t3 = t3.replace("\\^\\w+", "");
							CnvShowTable.text3 = CnvShowTable.text3.replace(
									"\\s+?", "");

							if (match3 = t3.toLowerCase().contains(
									CnvShowTable.text3.toLowerCase()))
							{
								// insert the reference to the found element
								// into the filtered array
								CnvShowTable.FilteredArray.add(TempArrayOne
										.get(i));
								counter++;
							}
						}
					}

				}
			}

		}
		// verify if we have more then 1000 lines in the filtered array to print
		if (counter > 1000)
		{
			end = 1000;
			CnvShowTable.onlyPage = 0;
			
		}

		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = CnvShowTable.FilteredArray.size();
			CnvShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(CnvShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();
		CnvShowTable.frame.dispose();

		filterName.add("Advance Filter (AND logical operator)");
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		CnvShowTable.arrayOfArrays.add(CnvShowTable.FilteredArray);
		CnvShowTable.into2DArrayFilterData(TempArray, end);

	}

	public static void FilterFunction()
	{

		ArrayList<CnvReader> TempArray = new ArrayList<CnvReader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<CnvReader> TempArrayOne = CnvShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = (int) (linesLeft / 100) * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		CnvSaveProgress pr = new CnvSaveProgress();

		int counter = 0;
		int colNumber = 0;
		for (int i = 0; i < CnvShowTable.columns.length; i++)
		{
			if (CnvShowTable.columns[i] == CnvShowTable.sortSelection)
			{
				// System.out.println(CnvShowTable.columns[i]);
				colNumber = i;
			}
		}

		for (int i = 0; i < end; i++)
		{

			// progress bar
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				progressIndicator = progressIndicator + 3;
				progressBar.setValue(progressIndicator);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String t;
			if (l.length <= colNumber)
			{
				t = "";
			} else
			{
				t = l[colNumber];
			}
			boolean match;
			t = t.replace("\\^\\w+", "");
			CnvShowTable.text = CnvShowTable.text.replace("\\s+?", "");

			if (match = t.toLowerCase().contains(
					CnvShowTable.text.toLowerCase()))
			{
				CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
				counter++;
			}

		}

		// verify if we have more then 1000 lines in the filtered array to print
		if (counter > 1000)
		{
			end = 1000;
			CnvShowTable.onlyPage = 0; 
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = CnvShowTable.FilteredArray.size();
			CnvShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(CnvShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();
		CnvShowTable.frame.dispose();

		filterName.add("by column " + CnvShowTable.sortSelection);
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		CnvShowTable.arrayOfArrays.add(CnvShowTable.FilteredArray);
		CnvShowTable.into2DArrayFilterData(TempArray, end);

	}

	/*
	 * Filter out all of the values that are '-' or 'N/A'
	 */
	public static void ValidValues()
	{

		ArrayList<CnvReader> TempArray = new ArrayList<CnvReader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<CnvReader> TempArrayOne = CnvShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = (int) (linesLeft / 100) * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		CnvSaveProgress pr = new CnvSaveProgress();

		int counter = 0;
		int colNumber = 0;
		for (int i = 0; i < CnvShowTable.columns.length; i++)
		{
			if (CnvShowTable.columns[i] == ValidValColumn)
			{
				// System.out.println(CnvShowTable.columns[i]);
				colNumber = i;
			}
		}

		for (int i = 0; i < end; i++)
		{

			// progress bar
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				progressIndicator = progressIndicator + 3;
				progressBar.setValue(progressIndicator);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String t;
			if (l.length <= colNumber)
			{
				t = "";
			} else
			{
				t = l[colNumber];
			}
			boolean match;

			if (match = t.equals("-"))
			{
				continue;

			}

			else if (match = t.equals("N/A"))
			{
				continue;
			}

			char[] chars = t.toCharArray();

			for (char c : chars)
			{
				if (Character.isDigit(c))
				{
					CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
					counter++;
					break;
				} else if (Character.isLetter(c))
				{
					CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
					counter++;
					break;
				}
			}
			continue;

		}

		// verify if we have more then 1000 lines in the filtered array to print
		if (counter > 1000)
		{
			end = 1000;
			CnvShowTable.onlyPage = 0;

		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = CnvShowTable.FilteredArray.size();
			CnvShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(CnvShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();
		CnvShowTable.frame.dispose();

		filterName.add("by column " + CnvShowTable.sortSelection);
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		CnvShowTable.arrayOfArrays.add(CnvShowTable.FilteredArray);
		CnvShowTable.into2DArrayFilterData(TempArray, end);

	}

	public static void chromPosition()
	{
		// dispose ChromPosFilter option page
		CnvChromPosFilter.fr.dispose();
		ArrayList<CnvReader> TempArray = new ArrayList<CnvReader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<CnvReader> TempArrayOne = CnvShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = (int) (linesLeft / 100) * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		CnvSaveProgress pr = new CnvSaveProgress();

		int counter = 0;
		int chromosomeColumn = 0;
		int beginColumn = 0;
		int endColumn = 0;

		// Find the number of the Chromosome column
		for (int i = 0; i < CnvShowTable.columns.length; i++)
		{
			if ("Chromosome".equals(CnvShowTable.columns[i]))
			{
				chromosomeColumn = i;
			}
		}

		// Find the number of the Begin column
		for (int i = 0; i < CnvShowTable.columns.length; i++)
		{
			if ("Begin".equals(CnvShowTable.columns[i]))
			{
				beginColumn = i;
			}
		}

		// Find the number of the End column
		for (int i = 0; i < CnvShowTable.columns.length; i++)
		{
			if ("End".equals(CnvShowTable.columns[i]))
			{
				endColumn = i;
			}
		}

		for (int i = 0; i < end; i++)
		{

			// progress bar
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				progressIndicator = progressIndicator + 3;
				progressBar.setValue(progressIndicator);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String t = l[chromosomeColumn];
			boolean match;
			t = t.replace("\\^\\w+", "");
			CnvChromPosFilter.chrom = CnvChromPosFilter.chrom.replace("\\s+?",
					"");
			/*
			 * In case we have numbers without the 'chr' upfront
			 */
			String chrNochr = CnvChromPosFilter.chrom.substring(3);
			if (t.equals(CnvChromPosFilter.chrom) || t.equals(chrNochr))
			{
				// parse in the number of Begin position
				double t2 = Double.parseDouble(l[beginColumn]);

				if (t2 >= CnvChromPosFilter.startPosition)
				{
					// parse in the number of End position
					double tEnd = Double.parseDouble(l[endColumn]);
					if (tEnd <= CnvChromPosFilter.endPosition)
					{
						CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
						counter++;
					}
				}
			}
		}

		// verify if we have more then 1000 lines in the filtered array to print
		if (counter > 1000)
		{
			end = 1000;
			CnvShowTable.onlyPage = 0;

		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = CnvShowTable.FilteredArray.size();
			CnvShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(CnvShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();

		CnvShowTable.frame.dispose();

		filterName.add("Chromosome Position");
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		CnvShowTable.arrayOfArrays.add(CnvShowTable.FilteredArray);
		CnvShowTable.into2DArrayFilterData(TempArray, end);

	}

	public void prefilter() throws IOException
	{

		int colForSort = 0;
		int spliceCol = 0;

		int lineCount = 0;
		// counter of only the good variants
		int datacount = 0;
		int end = 0;

		ArrayList<CnvReader> TempArray = new ArrayList<CnvReader>();
		CnvShowTable.FilteredArray = new ArrayList<CnvReader>();
		CnvFilterFunctions.filterName.add("Prefilter - Coding Variant");
		// find which is the column named Coding_Impact
		for (int i = 0; i < CnvShowTable.columnNames.length; i++)
		{
			System.out.println("COL NAMES TOTAL NUMBER: "
					+ CnvShowTable.columnNames.length);
			if (CnvShowTable.columnNames[i].contains("Coding_Impact"))
			{
				// System.out.println("We will be selecting by column: ");
				// System.out.println(CnvShowTable.columnNames[i]);
				colForSort = i;
			}

			// find the Splice column number
			else if (CnvShowTable.columnNames[i].contains("Splice_Site"))
			{
				// System.out.println("We will be selecting by column: ");
				// System.out.println(CnvShowTable.columnNames[i]);
				spliceCol = i;
			}

		}
		System.out.println("colForSort: " + colForSort);
		System.out.println("spliceCol: " + spliceCol);
		String line = "";
		String[] head;

		boolean match;
		BufferedReader bReader = null;
		try
		{
			bReader = new BufferedReader(new FileReader(
					CnvViewerInterface.cnvFile));
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

		// for (int i = 0; i < 1; i++)
		// {
		head = line.split("\t");
		// }
		long fileInBytes = CnvViewerInterface.cnvFile.length();
		System.out.println("Filelenght is: " + fileInBytes);
		// calculating how many lines on average 3 percent would be,
		// 1358 is the size of each line in bytes
		int threePerc = (int) ((fileInBytes / 1358) / 100) * 3;
		System.out.println("threePerc is: " + threePerc);
		String[] columnData = null;
		while ((line = bReader.readLine()) != null)
		{
			lineCount++;
			// check the percentage, if true increment the progress bar
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				progressIndicator = progressIndicator + 3;
				progressBar.setValue(progressIndicator);
				progressBar.setStringPainted(true);
			}
			/*
			 * if ((lineCount % 10000) == 0) {
			 * System.out.println("Line count is: " + lineCount); }
			 */

			columnData = line.split("\t");
			String sortingColumn = columnData[colForSort];

			if (sortingColumn.toLowerCase().contains("nonsy")
					|| sortingColumn.toLowerCase().contains("frame"))
			{
				datacount++;
				String nt;
				// adding an index plus first element needs to be an empty space
				// for future addition of imported genotypes
				nt = datacount + "\t" + line; // "N/A" + "\t" +

				CnvReader ob1 = new CnvReader(nt);

				CnvReadFile.arrayOfLines.add(ob1);
				CnvShowTable.FilteredArray.add(ob1);

			} else
			{
				// find the splice site column
				String splice = columnData[spliceCol];
				if (splice.contains("Splice"))
				{
					datacount++;
					String nt;
					// adding an index plus first element needs to be an empty
					// space for future adition of imported genotypes
					nt = datacount + "\t" + line; // "N/A" + "\t" +
					CnvReader ob1 = new CnvReader(nt);
					CnvReadFile.arrayOfLines.add(ob1);
					CnvShowTable.FilteredArray.add(ob1);
				}

			}
		}

		System.out.println("Datacount is: " + datacount);

		/*
		 * variables needed to build that darn table; get rid of some of them
		 */
		System.out.println("tempHeadForPrefilter: "
				+ CnvViewerInterface.tempHeadForPrefilter);
		String newHead = "Data Counter" + "\t"
				+ CnvViewerInterface.tempHeadForPrefilter + "\t" + "Comments";
		CnvShowTable.columnNames = newHead.split("\t");
		System.out.println("COLS IN EACH DATAROW IS: " + columnData.length);
		System.out.println("NEW COL NAMES TOTAL NUMBER: "
				+ CnvShowTable.columnNames.length);
		CnvShowTable.columns = newHead.split("\t");

		if (datacount > 1000)
		{
			end = 1000;
			CnvShowTable.onlyPage = 0;
		} else
		{
			end = CnvReadFile.arrayOfLines.size();
			CnvShowTable.onlyPage = 1;
		}

		for (int j = 0; j < end; j++)
		{
			TempArray.add(CnvReadFile.arrayOfLines.get(j));
		}
		System.out.println("TempArray size: " + TempArray.size());
		frame.dispose();
		// Interface.frame.dispose();

		System.out.println("Before CnvShowTable.arrayOfArrays size: "
				+ CnvShowTable.arrayOfArrays.size());
		CnvShowTable.arrayOfArrays.add(CnvReadFile.arrayOfLines);
		System.out.println("After CnvShowTable.arrayOfArrays size: "
				+ CnvShowTable.arrayOfArrays.size());
		CnvShowTable.into2DArrayFilterData(TempArray, end);

	}

	public int compoundHetero(String aff, String unaffOne, String unaffTwo,
			String line, String[] genes, String end_allele, String gFour,
			String fourAffect, String gFive, String fiveAffect)
	{ // , String TwoNA, String ThreeNA) {

		int datacount = 0;
		int tOne = 0;
		int tTwo = 0;
		int tFour = 0;
		int tFive = 0;

		// check if either of the parents have the genotype 1/1 or 1|1 skip, as
		// that genotype would make then affected as well?

		// parsing the genotype: 1 - variant present, 2 - variant missing (we
		// don't care about heterozygous or homozygous for now)
		if (unaffOne.contains("1") || unaffOne.contains("2")
				|| unaffOne.contains("3") || unaffOne.contains("4")
				|| unaffOne.contains("5"))
		{
			tOne = 1;
		}

		if (unaffTwo.contains("1") || unaffTwo.contains("2")
				|| unaffTwo.contains("3") || unaffTwo.contains("4")
				|| unaffOne.contains("5"))
		{
			tTwo = 1;
		}

		int child = 0;
		if (aff.contains("1") || aff.contains("2") || aff.contains("3")
				|| aff.contains("4") || unaffOne.contains("5"))
		{
			child = 1;
		}

		if (gFour.contains("1") || gFour.contains("2") || gFour.contains("3")
				|| gFour.contains("4") || gFour.contains("5"))
		{
			tFour = 1;
		}

		if (gFive.contains("1") || gFive.contains("2") || gFive.contains("3")
				|| gFive.contains("4") || gFive.contains("5"))
		{
			tFive = 1;
		}
		/*
		 * If the child contains the variant and ONLY one of the parents contain
		 * it
		 */

		if (child == 1 && tOne != tTwo)
		{

			int print = 0;

			// check for 2 more children
			/*
			 * if ((fourAffect.equals("Affected") && tFour != 1) ||
			 * (fiveAffect.equals("Affected") && tFive != 1)) {
			 * System.out.println("Other children were affected!"); return
			 * datacount; }
			 */

			for (int j = 0; j < genes.length; j++)
			{

				String[] gene = genes[j].split("\\(");
				// String[] gene = genes[0].split("\\(");

				/*
				 * check if this gene exists in the Gene Map already
				 */
				if (!GeneMap.containsKey(gene[0]))
				{
					/*
					 * Insert the line in GeneMap
					 */
					CnvCompoundHeteroObj ob1 = new CnvCompoundHeteroObj(tOne,
							tTwo, line, end_allele, gene[0], tFour, tFive,
							fourAffect, fiveAffect);
					ArrayList<CnvCompoundHeteroObj> temp = new ArrayList<CnvCompoundHeteroObj>();
					temp.add(ob1);
					GeneMap.put(gene[0], temp);

				} else if (GeneMap.containsKey(gene[0]))
				{
					/*
					 * Gene was already previously found, we need to print both
					 * variants if it fits our criteria
					 */
					// first check if this gene was not already printed
					ArrayList<CnvCompoundHeteroObj> temp = new ArrayList<CnvCompoundHeteroObj>();
					temp = GeneMap.get(gene[0]);
					// iterate through each element of the arrayList
					for (int i = 0; i < temp.size(); i++)
					{
						CnvCompoundHeteroObj ob3 = temp.get(i);
						// if different parents had this gene
						if ((((ob3.parentOne == 1 && tTwo == 1) || (ob3.parentTwo == 0 && tOne == 0)) && gene[0]
								.equals(ob3.gene))
								|| (((ob3.parentOne == 0 && tTwo == 0) || (ob3.parentTwo == 1 && tOne == 1)) && gene[0]
										.equals(ob3.gene)))
						{

							// check the rest of the children
							// if second child is N/A or
							if ((gFour.equals("N/A") && gFive.equals("N/A"))
									||
									// second child is unaffected and has either
									// one of the variants or neither
									((fourAffect.equals("Unaffected") && (ob3.childTwo != tFour || (ob3.childTwo == 0 && tFour == 0))))
									||
									// second child is affected and has both
									// variants and third child is "N/A"
									((fourAffect.equals("Affected")
											&& ob3.childTwo == 1 && tFour == 1 && gFive
												.equals("N/A")))
									||
									// second child is affected and has both
									// variants and third child child is
									// unaffected and has either one of the
									// variants or only one
									((fourAffect.equals("Affected")
											&& ob3.childTwo == 1 && tFour == 1
											&& fiveAffect.equals("Unaffected") && (ob3.childThree != tFive || (ob3.childThree == 0 && tFive == 0))))
									||
									// both second and third children are
									// affected and have both variants
									((fourAffect.equals("Affected")
											&& ob3.childTwo == 1 && tFour == 1
											&& fiveAffect.equals("Affected")
											&& ob3.childThree == 1 && tFive == 1))
									||
									// second child in unaffected and has either
									// one of the variants or neitherand, third
									// child is affected and has both variants
									((fourAffect.equals("Unaffected") && (ob3.childTwo != tFour || (ob3.childTwo == 0 && tFour == 0)))
											&& fiveAffect.equals("Affected")
											&& ob3.childThree == 1 && tFive == 1))
							{

								if (ob3.printed == 0)
								{

									// print them both
									// Put that line into a Reader object, then
									// insert the Reader object into Filtered
									// array
									compoundSet.add(ob3.line);
									if (print == 0)
									{
										compoundSet.add(line);
										print = 1;
									}

									// we need to let know CompoundHeteroObj if
									// the line was already printed
									ob3.printed = 1;
									// Was this line already checked
									if (!ob3.end_allele.equals(end_allele))
									{
										CnvCompoundHeteroObj ob4 = new CnvCompoundHeteroObj(
												tOne, tTwo, line, end_allele,
												gene[0], tFour, tFive,
												fourAffect, fiveAffect);
										ob4.printed = 1;
										temp.add(ob4);
										GeneMap.put(gene[0], temp);
									}
									datacount = datacount + 2;
									break;
								} else if (ob3.printed == 1)
								{

									// making sure we don't print the same line
									// multiple times
									if (print == 0)
									{
										compoundSet.add(line);
										print = 1;
									}

									// insert line into the hashTable
									if (!ob3.end_allele.equals(end_allele))
									{

										CnvCompoundHeteroObj ob4 = new CnvCompoundHeteroObj(
												tOne, tTwo, line, end_allele,
												gene[0], tFour, tFive,
												fourAffect, fiveAffect);
										ob4.printed = 1;
										temp.add(ob4);
										GeneMap.put(gene[0], temp);
									}
									break;
								}
							}
						}

					}
					if (print == 0)
					{
						CnvCompoundHeteroObj ob4 = new CnvCompoundHeteroObj(
								tOne, tTwo, line, end_allele, gene[0], tFour,
								tFive, fourAffect, fiveAffect);
						temp.add(ob4);
						GeneMap.put(gene[0], temp);
					}
				}

			}

		}

		return datacount;
	}

	public static void geneList()
	{

		fileChooser = new JFileChooser();
		int returnVal = fileChooser.showSaveDialog(CnvFilterFunctions.frame);
		if (returnVal == JFileChooser.CANCEL_OPTION)
			return;

		else if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			String fName = file.getName();
			if (file.exists())
			{
				BufferedReader bReader;
				try
				{
					bReader = new BufferedReader(new FileReader(file));
					String line = bReader.readLine();
					while ((line = bReader.readLine()) != null)
					{
						String[] l = line.split("\t");
						// System.out.println("Gene to be found: " + l[0]);
						geneList.add(l[0]);
					}
				} catch (IOException ex)
				{
					Logger.getLogger(CnvViewerInterface.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		}

		ArrayList<CnvReader> TempArray = new ArrayList<CnvReader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<CnvReader> TempArrayOne = CnvShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = (int) (linesLeft / 100) * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		CnvSaveProgress pr = new CnvSaveProgress();

		int counter = 0;
		int colNumber = 0;
		for (int i = 0; i < CnvShowTable.columns.length; i++)
		{
			if (CnvShowTable.columns[i].contentEquals("Gene"))
			{

				// System.out.println(CnvShowTable.columns[i]);
				colNumber = i;
			}
		}

		// counter for the variants found
		int found_counter = 0;

		for (int i = 0; i < end; i++)
		{

			// progress bar
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				progressIndicator = progressIndicator + 3;
				progressBar.setValue(progressIndicator);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			// check if this gene is in the gene list
			for (int j = 0; j < geneList.size(); j++)
			{
				String gen = geneList.get(j);
				// System.out.println(l[colNumber]);
				if (l[colNumber].contains(gen))
				{
					// System.out.println("found");
					found_counter = found_counter + 1;
					CnvShowTable.FilteredArray.add(TempArrayOne.get(i));
				}
			}
		}

		if (found_counter > 1000)
		{
			end = 1000;
			CnvShowTable.onlyPage = 0; 
		} else
		{
			end = CnvShowTable.FilteredArray.size();
			CnvShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(CnvShowTable.FilteredArray.get(i));
		}
		frame.dispose();
		CnvShowTable.frame.dispose();

		currentArray = currentArray + 1;
		filterName.add("Gene list filter");
		CnvShowTable.arrayOfArrays.add(CnvShowTable.FilteredArray);
		CnvShowTable.into2DArrayFilterData(TempArray, end);
	}

	@Override
	public void run()
	{
		pageNumber = 1;
		if (CnvShowTable.tableStatus == 3)
		{
			CodingVar();
		}
		if (CnvShowTable.tableStatus == 12 || CnvShowTable.tableStatus == 13)
		{
			CodingVarFreq();
		}
		if (CnvShowTable.tableStatus == 5)
		{
			KnownDisease();
		}
		if (CnvShowTable.tableStatus == 6)
		{
			PredClinical();
		}
		if (CnvShowTable.tableStatus == 7)
		{
			PredResearch();
		}
		if (CnvShowTable.tableStatus == 8)
		{
			CancerGenes();
		}
		if (CnvShowTable.tableStatus == 9)
		{
			Pharmacogenetic();
		}
		if (CnvShowTable.tableStatus == 10)
		{
			TruncatedVariants();
		}
		if (CnvShowTable.tableStatus == 11)
		{
			NondbSNP();
		}
		if (CnvShowTable.tableStatus == 14)
		{
			AdvanceFilterAlgorithm();
		}
		if (CnvShowTable.tableStatus == 15)
		{
			FilterFunction();
		}
		if (CnvShowTable.tableStatus == 16)
		{
			chromPosition();
		}
		if (CnvShowTable.tableStatus == 18)
		{
			AdvanceFilterAlgorithmOR();
		}
		if (CnvShowTable.tableStatus == 19)
		{
			System.out.println("Table status is: " + CnvShowTable.tableStatus);
			try
			{
				System.out.println("Going to prefilter");
				prefilter();
			} catch (IOException ex)
			{
				Logger.getLogger(CnvFilterFunctions.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
		if (CnvShowTable.tableStatus == 20)
		{
			ValidValues();
		}
		if (CnvShowTable.tableStatus == 22)
		{
			geneList();
		}

	}

}
