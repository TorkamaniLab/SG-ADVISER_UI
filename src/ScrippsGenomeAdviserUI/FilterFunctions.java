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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

import ScrippsGenomeAdviserUI.ShowTable;

/**
 * 
 * @author gerikson
 */
public class FilterFunctions implements Runnable
{
	public static javax.swing.JFileChooser fileChooser;
	public static JFrame frame;
	public static Container content;
	public static JProgressBar progressBar;
	public static Border border;
	public static int b;
	public static int pageNumber = 1;

	public static int currentArray = 0;
	// for ability to print current filter table
	public static ArrayList<String> filterName = new ArrayList<String>();
	public static ArrayList<String> geneList = new ArrayList<String>();
	public static String ValidValColumn = "";
	// public Map<String, CompoundHeteroObj> GeneMap = new HashMap<String,
	// CompoundHeteroObj>();

	/*
	 * creating a set for the compound heterozygous filter
	 */
	// public static SortedSet<String> compoundSet = new TreeSet<String>();
	public static Set<String> compoundSet = new HashSet<String>();

	public Map<String, ArrayList<CompoundHeteroObj>> GeneMap = new HashMap<String, ArrayList<CompoundHeteroObj>>();

	public FilterFunctions(String s)
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
		b = 0;
	}

	public static void CodingVar()
	{

		int colForSort = 0;
		int counterCod = 0;

		// create new temporary array to store just the view for this page
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArrayOne = ShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// find which is the column named Coding_Impact
		for (int i = 0; i < ShowTable.columnNames.length; i++)
		{

			if (ShowTable.columnNames[i].contains("Coding_Impact"))
			{
				// System.out.println("We will be selecting by coulmn: ");
				// System.out.println(ShowTable.columnNames[i]);
				colForSort = i;
			}
		}

		int end = TempArrayOne.size();
		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = linesLeft / 100 * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		SaveProgress pr = new SaveProgress();

		for (int i = 0; i < end; i++)
		{

			lineCount++;
			// check the percentage, if true increment the progress bar
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				b = b + 3;
				progressBar.setValue(b);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;

			String[] l = line.split("\t");
			String s = l[colForSort];
			if (s.toLowerCase().contains("nonsy")
					|| s.toLowerCase().contains("frame")
					|| s.toLowerCase().contains("Nonsense"))
			{

				counterCod++;
				ShowTable.FilteredArray.add(TempArrayOne.get(i));
			}

		}

		if (counterCod > 1000)
		{
			ShowTable.onlyPage = 0;
			end = 1000;
		} else
		{
			end = ShowTable.FilteredArray.size();
			ShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(ShowTable.FilteredArray.get(i));
		}
		frame.dispose();
		ShowTable.frame.dispose();

		currentArray = currentArray + 1;
		filterName.add("Coding Impact");
		ShowTable.arrayOfArrays.add(ShowTable.FilteredArray);
		ShowTable.into2DArrayFilterData(TempArray, end);

	}

	public static void SpliceVar()
	{

		int colForSort = 0;
		int spliceCol = 0;
		int counter = 0;
		int thousandGen = 0;
		int GC69 = 0;
		int GCWELL = 0;
		int dbSNP = 0;

		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArrayOne = ShowTable.arrayOfArrays
				.get(currentArray);
		// System.out.println("The size of the main array is: " +
		// TempArrayOne.size());
		int end = TempArrayOne.size();

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = linesLeft / 100 * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		SaveProgress pr = new SaveProgress();

		// find which is the column named Coding_Impact
		for (int i = 0; i < ShowTable.columnNames.length; i++)
		{
			if (ShowTable.columnNames[i].contains("Coding_Impact"))
			{
				// System.out.println("We will be selecting by coulmn: ");
				// System.out.println(ShowTable.columnNames[i]);
				colForSort = i;
			}

			// find the Splice column number
			else if (ShowTable.columnNames[i].contains("Splice_Site"))
			{
				// System.out.println("We will be selecting by coulmn: ");
				// System.out.println(ShowTable.columnNames[i]);
				spliceCol = i;
			} else if (ShowTable.columnNames[i].contains("1000GENOMES"))
			{
				thousandGen = i;
				// System.out.println("1000GENOMES column number is: " +
				// thousandGen);
			}

			else if (ShowTable.columnNames[i].contains("69"))
			{
				GC69 = i;
				// System.out.println("GC_69 frequency column is: " + GC69);
			}

			else if (ShowTable.columnNames[i].contains("WELLDERLY"))
			{
				GCWELL = i;
				// System.out.println("WELLDELY frequency column is: " +
				// GCWELL);
			} else if (ShowTable.columnNames[i].contains("dbSNP_ID"))
			{
				dbSNP = i;
				// System.out.println("dbSNP column number is: " + dbSNP);
			}
		}

		for (int i = 0; i < end; i++)
		{
			lineCount++;
			// check the percentage, if true increment the progress bar
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				b = b + 3;
				progressBar.setValue(b);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String s = l[colForSort];
			// if it's from custom filter 11
			if (ShowTable.tableStatus == 13)
			{
				String t = l[dbSNP];
				if (!t.equals("-"))
				{
					continue;
				}
			}

			if (s.toLowerCase().contains("nonsy")
					|| s.toLowerCase().contains("frame")
					|| s.toLowerCase().contains("Nonsense"))
			{

				// check to see if this is custom filter 3
				if (ShowTable.frequencySelection == -1
						&& ShowTable.frequencySelection2 == -1
						&& ShowTable.frequencySelection3 == -1)
				{

					counter++;

					ShowTable.FilteredArray.add(TempArrayOne.get(i));

				} else
				{

					Double b = -1.0;
					Double c = -1.0;
					Double d = -1.0;
					// If "N/A" found in the frequency columns transform "N/A"
					// to "0" since we are just looking for smaller the
					// frequency and alhighes frequency is "1"
					if (ShowTable.frequencySelection != -1.0
							&& thousandGen != 0)
					{
						if (l[thousandGen].contains("N/A"))
						{
							b = 0.0;
						} else
						{
							try
							{
								b = Double.valueOf(l[thousandGen]);
							} catch (Exception ex)
							{
								continue;
							}
						}
					}
					if (ShowTable.frequencySelection2 != -1.0 && GC69 != 0)
					{
						if (l[GC69].contains("N/A"))
						{
							c = 0.0;
						} else
						{
							try
							{
								c = Double.valueOf(l[GC69]);
							} catch (Exception ex)
							{
								continue;
							}
						}
					}
					if (ShowTable.frequencySelection3 != -1.0 && GCWELL != 0)
					{
						if (l[GCWELL].contains("N/A"))
						{
							d = 0.0;
						} else
						{
							try
							{
								d = Double.valueOf(l[GCWELL]);
							} catch (Exception ex)
							{
								continue;
							}
						}
					}

					if (b <= ShowTable.frequencySelection
							&& c <= ShowTable.frequencySelection2
							&& d <= ShowTable.frequencySelection3)
					{

						counter++;
						ShowTable.FilteredArray.add(TempArrayOne.get(i));

					}

				}
			} else
			{
				// find the splice site column
				String splice = l[spliceCol];
				if (splice.contains("Splice"))
				{
					// check to see if this is custom filter 3
					if (ShowTable.frequencySelection == -1
							&& ShowTable.frequencySelection2 == -1
							&& ShowTable.frequencySelection3 == -1)
					{

						counter++;
						ShowTable.FilteredArray.add(TempArrayOne.get(i));
					} else
					{
						Double b = -1.0;
						Double c = -1.0;
						Double d = -1.0;
						// If "N/A" found in the frequency columns transform
						// "N/A" to "0" since we are just looking for smaller
						// the frequency and alhighes frequency is "1"
						if (ShowTable.frequencySelection != -1.0
								&& thousandGen != 0)
						{
							if (l[thousandGen].contains("N/A"))
							{
								b = 0.0;
							} else
							{
								b = Double.valueOf(l[thousandGen]);
							}
						}
						if (ShowTable.frequencySelection2 != -1.0 && GC69 != 0)
						{
							if (l[GC69].contains("N/A"))
							{
								c = 0.0;
							} else
							{
								c = Double.valueOf(l[GC69]);
							}
						}
						if (ShowTable.frequencySelection3 != -1.0
								&& GCWELL != 0)
						{
							if (l[GCWELL].contains("N/A"))
							{
								d = 0.0;
							} else
							{
								d = Double.valueOf(l[GCWELL]);
							}
						}

						if (b <= ShowTable.frequencySelection
								&& c <= ShowTable.frequencySelection2
								&& d <= ShowTable.frequencySelection3)
						{

							counter++;
							ShowTable.FilteredArray.add(TempArrayOne.get(i));
						}
					}
				}
			}

		}

		if (counter > 1000)
		{
			ShowTable.onlyPage = 0;
			end = 1000;
		} else
		{
			end = ShowTable.FilteredArray.size();
			ShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(ShowTable.FilteredArray.get(i));
		}
		frame.dispose();
		ShowTable.frame.dispose();

		if (ShowTable.tableStatus == 4)
		{
			filterName.add("Coding & Splice Var");
		}
		if (ShowTable.tableStatus == 12)
		{
			filterName.add("Coding and Splice Variants plus frequency");
		}
		if (ShowTable.tableStatus == 13)
		{
			filterName
					.add("Non dbSNP, Coding and Splice Variants with frequency");
		}
		currentArray = currentArray + 1;
		ShowTable.arrayOfArrays.add(ShowTable.FilteredArray);
		ShowTable.into2DArrayFilterData(TempArray, end);

	}

	public static void KnownDisease()
	{
		int colForSort = 0;
		int counter = 0;

		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArrayOne = ShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		int end = TempArrayOne.size();
		// find which is the column named ACMG_Clinical...
		for (int i = 0; i < ShowTable.columnNames.length; i++)
		{
			if (ShowTable.columnNames[i].contains("ACMG_Score"))
			{
				// System.out.println("We will be selecting by coulmn: ");
				// System.out.println(columnNames[i]);
				colForSort = i;
			}
		}

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = linesLeft / 100 * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		SaveProgress pr = new SaveProgress();

		for (int i = 0; i < end; i++)
		{
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				b = b + 3;
				progressBar.setValue(b);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String s = l[colForSort];
			String[] g = s.split("~");
			if (g[0].equals("1"))
			{
				counter++;
				ShowTable.FilteredArray.add(TempArrayOne.get(i));

			}

		}

		if (counter > 1000)
		{
			ShowTable.onlyPage = 0;
			end = 1000;
		} else
		{
			end = ShowTable.FilteredArray.size();
			ShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(ShowTable.FilteredArray.get(i));
		}
		frame.dispose();
		ShowTable.frame.dispose();

		filterName.add("Known Disease Variants");
		currentArray = currentArray + 1;
		ShowTable.arrayOfArrays.add(ShowTable.FilteredArray);
		ShowTable.into2DArrayFilterData(TempArray, end);
	}

	public static void PredClinical()
	{
		int colForSort = 0;
		int counterClinnical = 0;

		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArrayOne = ShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		int end = TempArrayOne.size();

		// progress bar calculation
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = linesLeft / 100 * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		SaveProgress pr = new SaveProgress();

		// find which is the column named ACMG_Clinical...
		for (int i = 0; i < ShowTable.columnNames.length; i++)
		{
			if (ShowTable.columnNames[i].contains("ADVISER_Score_Clinical"))
			{
				System.out.println("We will be selecting by coulmn: ");
				System.out.println(ShowTable.columnNames[i]);
				colForSort = i;
			}
		}

		for (int i = 0; i < end; i++)
		{

			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				b = b + 3;
				progressBar.setValue(b);
				progressBar.setStringPainted(true);
			}
			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String s = l[colForSort];
			String[] letter = s.split("~");
			String letterOne = letter[0];

			if (letterOne.contains("1") || letterOne.contains("2"))
			{
				counterClinnical++;
				ShowTable.FilteredArray.add(TempArrayOne.get(i));

			}

		}
		// verify if we have more then 1000 lines in the filtered array to print
		if (counterClinnical > 1000)
		{
			ShowTable.onlyPage = 0;
			end = 1000;
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = ShowTable.FilteredArray.size();
			ShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(ShowTable.FilteredArray.get(i));
		}
		// dispose the progress bar
		frame.dispose();

		ShowTable.frame.dispose();

		// Insert newly filtered array

		filterName.add("Predicted Clinical Variants");
		// change the count of current array
		currentArray = currentArray + 1;
		ShowTable.arrayOfArrays.add(ShowTable.FilteredArray);
		ShowTable.into2DArrayFilterData(TempArray, end);
	}

	public static void PredResearch()
	{
		int colForSort = 0;

		int counterResearch = 0;

		// this array is for representing first 1000 lines
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArrayOne = ShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();

		// progress bar calculation
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = linesLeft / 100 * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		SaveProgress pr = new SaveProgress();

		// find which is the column named ACMG_Clinical...
		for (int i = 0; i < ShowTable.columnNames.length; i++)
		{
			if (ShowTable.columnNames[i].contains("ADVISER_Score_Research"))
			{
				System.out.println("We will be selecting by column: ");
				System.out.println(ShowTable.columnNames[i]);
				colForSort = i;
			}
		}

		for (int i = 0; i < end; i++)
		{
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				b = b + 3;
				progressBar.setValue(b);
				progressBar.setStringPainted(true);
			}
			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String s = l[colForSort];

			String[] letter = s.split("~");
			String letterOne = letter[0];

			if (letterOne.contains("1") || letterOne.contains("2"))
			{
				counterResearch++;
				// insert the reference to the found element into the filtered
				// array
				ShowTable.FilteredArray.add(TempArrayOne.get(i));
			}

		}

		// verify if we have more then 1000 lines in the filtered array to print
		if (counterResearch > 1000)
		{
			ShowTable.onlyPage = 0;
			end = 1000;
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = ShowTable.FilteredArray.size();
			ShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(ShowTable.FilteredArray.get(i));
		}
		// dispose the progress bar
		frame.dispose();

		ShowTable.frame.dispose();

		filterName.add("Predicted Research Variants");
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		ShowTable.arrayOfArrays.add(ShowTable.FilteredArray);
		ShowTable.into2DArrayFilterData(TempArray, end);

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
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArrayOne = ShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();

		// find which is the column named ACMG_Clinical...
		for (int i = 0; i < ShowTable.columnNames.length; i++)
		{

			if (ShowTable.columnNames[i].contains("MSKCC_CancerGenes"))
			{
				// System.out.println("We will be selecting by coulmn: ");
				// System.out.println(columnNames[i]);
				colForSort = i;
			}

			if (ShowTable.columnNames[i].contains("Atlas_Oncology"))
			{
				// System.out.println("We will be selecting by coulmn: ");
				// System.out.println(columnNames[i]);
				colForSort2 = i;
			}

			if (ShowTable.columnNames[i].contains("Sanger_CancerGenes"))
			{
				// System.out.println("We will be selecting by coulmn: ");
				// System.out.println(columnNames[i]);
				colForSort3 = i;
			}

		}

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = linesLeft / 100 * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		SaveProgress pr = new SaveProgress();

		for (int i = 0; i < end; i++)
		{
			// progress bar
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				b = b + 3;
				progressBar.setValue(b);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String s = l[colForSort];
			if (Character.isUpperCase(s.charAt(0))
					|| Character.isLetterOrDigit(s.charAt(0)))
			{
				counterResearch++;
				// insert the reference to the found element into the filtered
				// array
				ShowTable.FilteredArray.add(TempArrayOne.get(i));

			} else if (Character.isUpperCase(l[colForSort2].charAt(0))
					|| Character.isLetterOrDigit(l[colForSort2].charAt(0)))
			{
				counterResearch++;
				// insert the reference to the found element into the filtered
				// array
				ShowTable.FilteredArray.add(TempArrayOne.get(i));

			} else if (Character.isUpperCase(l[colForSort3].charAt(0))
					|| Character.isLetterOrDigit(l[colForSort3].charAt(0)))
			{
				counterResearch++;
				// insert the reference to the found element into the filtered
				// array
				ShowTable.FilteredArray.add(TempArrayOne.get(i));

			}

		}

		// verify if we have more then 1000 lines in the filtered array to print
		if (counterResearch > 1000)
		{
			ShowTable.onlyPage = 0;
			end = 1000;
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = ShowTable.FilteredArray.size();
			ShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(ShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();
		ShowTable.frame.dispose();
		filterName.add("Cancer Genes");
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		ShowTable.arrayOfArrays.add(ShowTable.FilteredArray);
		ShowTable.into2DArrayFilterData(TempArray, end);

	}

	public static void Pharmacogenetic()
	{
		int colForSort = 0;
		int counterPharm = 0;

		// this array is for representing first 1000 lines
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArrayOne = ShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();
		// find which is the column named ACMG_Clinical...
		for (int i = 0; i < ShowTable.columnNames.length; i++)
		{
			if (ShowTable.columnNames[i].contains("PharmGKB"))
			{
				// System.out.println("We will be selecting by coulmn: ");
				// System.out.println(columnNames[i]);
				colForSort = i;
			}
		}
		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = linesLeft / 100 * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		SaveProgress pr = new SaveProgress();

		for (int i = 0; i < end; i++)
		{
			// progress bar
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				b = b + 3;
				progressBar.setValue(b);
				progressBar.setStringPainted(true);
			}
			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String s = l[colForSort];
			if (Character.isUpperCase(s.charAt(0))
					|| Character.isLetterOrDigit(s.charAt(0)))
			{
				counterPharm++;
				// insert the reference to the found element into the filtered
				// array
				ShowTable.FilteredArray.add(TempArrayOne.get(i));
			}
		}
		// verify if we have more then 1000 lines in the filtered array to print
		if (counterPharm > 1000)
		{
			ShowTable.onlyPage = 0;
			end = 1000;
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = ShowTable.FilteredArray.size();
			ShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(ShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();
		ShowTable.frame.dispose();

		filterName.add("Pharmacogenetic");
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		ShowTable.arrayOfArrays.add(ShowTable.FilteredArray);
		ShowTable.into2DArrayFilterData(TempArray, end);

	}

	public static void TruncatedVariants()
	{
		int colForSort = 0;
		int counterPharm = 0;

		// this array is for representing first 1000 lines
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArrayOne = ShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();
		// find which is the column named ACMG_Clinical...
		for (int i = 0; i < ShowTable.columnNames.length; i++)
		{
			if (ShowTable.columnNames[i].contains("PharmGKB"))
			{
				// System.out.println("We will be selecting by coulmn: ");
				// System.out.println(columnNames[i]);
				colForSort = i;
			}
		}

		// find which is the column named ACMG_Clinical...
		for (int i = 0; i < ShowTable.columnNames.length; i++)
		{
			if (ShowTable.columnNames[i].contains("Coding_Impact"))
			{
				// System.out.println("We will be selecting by coulmn: ");
				// System.out.println(columnNames[i]);
				colForSort = i;
			}
		}

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = linesLeft / 100 * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		SaveProgress pr = new SaveProgress();

		for (int i = 0; i < end; i++)
		{
			// progress bar
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				b = b + 3;
				progressBar.setValue(b);
				progressBar.setStringPainted(true);
			}
			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String s = l[colForSort];
			if (s.contains("Frameshift") || s.contains("Nonsense"))
			{
				// insert the reference to the found element into the filtered
				// array
				ShowTable.FilteredArray.add(TempArrayOne.get(i));
			}
		}
		// verify if we have more then 1000 lines in the filtered array to print
		if (counterPharm > 1000)
		{
			ShowTable.onlyPage = 0;
			end = 1000;
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = ShowTable.FilteredArray.size();
			ShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(ShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();
		ShowTable.frame.dispose();

		filterName.add("Truncated Variants");
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		ShowTable.arrayOfArrays.add(ShowTable.FilteredArray);
		ShowTable.into2DArrayFilterData(TempArray, end);
	}

	public static void NondbSNP()
	{
		int colForSort = 0;
		int counterPharm = 0;

		// this array is for representing first 1000 lines
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArrayOne = ShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();

		// find which is the column named ACMG_Clinical...
		for (int i = 0; i < ShowTable.columnNames.length; i++)
		{
			if (ShowTable.columnNames[i].contains("dbSNP_ID"))
			{
				// System.out.println("We will be selecting by coulmn: ");
				// System.out.println(columnNames[i]);
				colForSort = i;
			}
		}

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = linesLeft / 100 * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		SaveProgress pr = new SaveProgress();

		for (int i = 0; i < end; i++)
		{

			// progress bar
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				b = b + 3;
				progressBar.setValue(b);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String s = l[colForSort];
			if (s.equals("-"))
			{
				// insert the reference to the found element into the filtered
				// array
				ShowTable.FilteredArray.add(TempArrayOne.get(i));
			}

		}
		// verify if we have more then 1000 lines in the filtered array to print
		if (counterPharm > 1000)
		{
			ShowTable.onlyPage = 0;
			end = 1000;
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = ShowTable.FilteredArray.size();
			ShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(ShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();
		//ShowTable.frame.dispose(); // JUST CHANGED

		filterName.add("Non dbSNP");
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		ShowTable.arrayOfArrays.add(ShowTable.FilteredArray);
		ShowTable.into2DArrayFilterData(TempArray, end);

	}

	public static void AdvanceFilterAlgorithmOR()
	{
		int colForSort = 0;
		int colForSort2 = 0;
		int colForSort3 = 0;

		int counterResearch = 0;

		// this array is for representing first 1000 lines
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArrayOne = ShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();

		// Find the number of the first selection
		for (int i = 0; i < ShowTable.columns.length; i++)
		{
			if (ShowTable.columns[i] == ShowTable.sort1)
			{
				// System.out.println("Yay found the first column!");
				// System.out.println(columns[i]);
				colForSort = i;
			}
		}

		// Find the number of the second selection
		for (int i = 0; i < ShowTable.columns.length; i++)
		{
			if (ShowTable.columns[i] == null ? ShowTable.sort2 == null
					: ShowTable.columns[i].equals(ShowTable.sort2))
			{
				// System.out.println(ShowTable.columns[i]);
				colForSort2 = i;
			}
		}

		// Find the number of the third selection
		for (int i = 0; i < ShowTable.columns.length; i++)
		{
			if (ShowTable.columns[i] == null ? ShowTable.sort3 == null
					: ShowTable.columns[i].equals(ShowTable.sort3))
			{
				// System.out.println("Yay, found a third column!");
				// System.out.println(columns[i]);
				colForSort3 = i;
			}
		}

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = linesLeft / 100 * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		SaveProgress pr = new SaveProgress();

		for (int i = 0; i < end; i++)
		{
			// progress bar
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				b = b + 3;
				progressBar.setValue(b);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String t = l[colForSort];
			// boolean match;
			t = t.replace("\\^\\w+", "");
			ShowTable.text1 = ShowTable.text1.replace("\\s+?", "");

			String t2 = l[colForSort2];
			t2 = t2.replace("\\^\\w+", "");
			ShowTable.text2 = ShowTable.text2.replace("\\s+?", "");

			String t3 = l[colForSort3];
			t3 = t3.replace("\\^\\w+", "");
			ShowTable.text3 = ShowTable.text3.replace("\\s+?", "");

			if (t.contains(ShowTable.text1))
			{
				counterResearch++;
				// insert the reference to the found element into the filtered
				// array
				ShowTable.FilteredArray.add(TempArrayOne.get(i));

			} else if (t2.contains(ShowTable.text2))
			{
				counterResearch++;
				// insert the reference to the found element into the filtered
				// array
				ShowTable.FilteredArray.add(TempArrayOne.get(i));

			} else if (t3.contains(ShowTable.text3))
			{
				counterResearch++;
				// insert the reference to the found element into the filtered
				// array
				ShowTable.FilteredArray.add(TempArrayOne.get(i));

			}

		}

		// verify if we have more then 1000 lines in the filtered array to print
		if (counterResearch > 1000)
		{
			ShowTable.onlyPage = 0;
			end = 1000;
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = ShowTable.FilteredArray.size();
			ShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(ShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();
		ShowTable.frame.dispose();
		filterName.add("Advanced filter OR logical operator");
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		ShowTable.arrayOfArrays.add(ShowTable.FilteredArray);
		ShowTable.into2DArrayFilterData(TempArray, end);
	}

	public static void AdvanceFilterAlgorithm()
	{

		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArrayOne = ShowTable.arrayOfArrays
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
		int threePerc = linesLeft / 100 * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		SaveProgress pr = new SaveProgress();

		// Find the number of the first selection
		for (int i = 0; i < ShowTable.columns.length; i++)
		{
			if (ShowTable.columns[i] == ShowTable.sort1)
			{
				// System.out.println("Yay found the first column!");
				// System.out.println(columns[i]);
				colNumber1 = i;
			}
		}

		// Find the number of the second selection
		for (int i = 0; i < ShowTable.columns.length; i++)
		{
			if (ShowTable.columns[i] == null ? ShowTable.sort2 == null
					: ShowTable.columns[i].equals(ShowTable.sort2))
			{
				// System.out.println(ShowTable.columns[i]);
				colNumber2 = i;
			}
		}

		// Find the number of the third selection
		for (int i = 0; i < ShowTable.columns.length; i++)
		{
			if (ShowTable.columns[i] == null ? ShowTable.sort3 == null
					: ShowTable.columns[i].equals(ShowTable.sort3))
			{
				// System.out.println("Yay, found a third column!");
				// System.out.println(columns[i]);
				colNumber3 = i;
			}
		}

		String st2 = "Please enter the 2nd filter criteria!";
		if (ShowTable.text2.equals(st2))
		{
			ShowTable.text2 = "";
		}

		String st3 = "Please enter the 3rd filter criteria!";
		if (ShowTable.text3.equals(st3))
		{
			ShowTable.text3 = "";
		}

		for (int i = 0; i < end; i++)
		{

			// progress bar
			lineCount++;
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				b = b + 3;
				progressBar.setValue(b);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String t = l[colNumber1];
			boolean match;
			t = t.replace("\\^\\w+", "");
			ShowTable.text1 = ShowTable.text1.replace("\\s+?", "");

			if (match = t.toLowerCase().contains(ShowTable.text1.toLowerCase()))
			{

				if (ShowTable.sort2 == null)
				{
					// insert the reference to the found element into the
					// filtered array
					ShowTable.FilteredArray.add(TempArrayOne.get(i));
					counter++;
					// else verify the 3nd criteria
				} else
				{
					String t2 = l[colNumber2];
					boolean match2;
					t2 = t2.replace("\\^\\w+", "");
					ShowTable.text2 = ShowTable.text2.replace("\\s+?", "");

					if (match2 = t2.toLowerCase().contains(
							ShowTable.text2.toLowerCase()))
					{
						// System.out.println("Yay, found a second match!");
						// if there is no 3rd criteria print
						if (ShowTable.text3 == null)
						{
							// insert the reference to the found element into
							// the filtered array
							ShowTable.FilteredArray.add(TempArrayOne.get(i));
							counter++;
						} else
						{
							String t3 = l[colNumber3];
							boolean match3;
							t3 = t3.replace("\\^\\w+", "");
							ShowTable.text3 = ShowTable.text3.replace("\\s+?",
									"");

							if (match3 = t3.toLowerCase().contains(
									ShowTable.text3.toLowerCase()))
							{
								// insert the reference to the found element
								// into the filtered array
								ShowTable.FilteredArray
										.add(TempArrayOne.get(i));
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
			ShowTable.onlyPage = 0;
			end = 1000;
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = ShowTable.FilteredArray.size();
			ShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(ShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();
		ShowTable.frame.dispose();

		filterName.add("Advance Filter (AND logical operator)");
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		ShowTable.arrayOfArrays.add(ShowTable.FilteredArray);
		ShowTable.into2DArrayFilterData(TempArray, end);

	}

	public static void FilterFunction()
	{

		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArrayOne = ShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = linesLeft / 100 * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		SaveProgress pr = new SaveProgress();

		int counter = 0;
		int colNumber = 0;
		for (int i = 0; i < ShowTable.columns.length; i++)
		{
			if (ShowTable.columns[i] == ShowTable.sortSelection)
			{
				// System.out.println(ShowTable.columns[i]);
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
				b = b + 3;
				progressBar.setValue(b);
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
			ShowTable.text = ShowTable.text.replace("\\s+?", "");

			if (match = t.toLowerCase().contains(ShowTable.text.toLowerCase()))
			{
				ShowTable.FilteredArray.add(TempArrayOne.get(i));
				counter++;
			}

		}

		// verify if we have more then 1000 lines in the filtered array to print
		if (counter > 1000)
		{
			ShowTable.onlyPage = 0;
			end = 1000;
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = ShowTable.FilteredArray.size();
			ShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(ShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();
		ShowTable.frame.dispose();

		filterName.add("by column " + ShowTable.sortSelection);
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		ShowTable.arrayOfArrays.add(ShowTable.FilteredArray);
		ShowTable.into2DArrayFilterData(TempArray, end);

	}

	/*
	 * Filter out all of the values that are '-' or 'N/A'
	 */
	public static void ValidValues()
	{

		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArrayOne = ShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = linesLeft / 100 * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		SaveProgress pr = new SaveProgress();

		int counter = 0;
		int colNumber = 0;
		for (int i = 0; i < ShowTable.columns.length; i++)
		{
			if (ShowTable.columns[i] == ValidValColumn)
			{
				// System.out.println(ShowTable.columns[i]);
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
				b = b + 3;
				progressBar.setValue(b);
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
					ShowTable.FilteredArray.add(TempArrayOne.get(i));
					counter++;
					break;
				} else if (Character.isLetter(c))
				{
					ShowTable.FilteredArray.add(TempArrayOne.get(i));
					counter++;
					break;
				}
			}
			continue;

		}

		// verify if we have more then 1000 lines in the filtered array to print
		if (counter > 1000)
		{
			ShowTable.onlyPage = 0;
			end = 1000;
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = ShowTable.FilteredArray.size();
			ShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(ShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();
		ShowTable.frame.dispose();

		filterName.add("by column " + ShowTable.sortSelection);
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		ShowTable.arrayOfArrays.add(ShowTable.FilteredArray);
		ShowTable.into2DArrayFilterData(TempArray, end);

	}

	public static void chromPosition()
	{
		// dispose ChromPosFilter option page
		ChromPosFilter.fr.dispose();
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArrayOne = ShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = linesLeft / 100 * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		SaveProgress pr = new SaveProgress();

		int counter = 0;
		int chromosomeColumn = 0;
		int beginColumn = 0;
		int endColumn = 0;

		// Find the number of the Chromosome column
		for (int i = 0; i < ShowTable.columns.length; i++)
		{
			if ("Chromosome".equals(ShowTable.columns[i]))
			{
				chromosomeColumn = i;
			}
		}

		// Find the number of the Begin column
		for (int i = 0; i < ShowTable.columns.length; i++)
		{
			if ("Begin".equals(ShowTable.columns[i]))
			{
				beginColumn = i;
			}
		}

		// Find the number of the End column
		for (int i = 0; i < ShowTable.columns.length; i++)
		{
			if ("End".equals(ShowTable.columns[i]))
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
				b = b + 3;
				progressBar.setValue(b);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");
			String t = l[chromosomeColumn];
			boolean match;
			t = t.replace("\\^\\w+", "");
			ChromPosFilter.chrom = ChromPosFilter.chrom.replace("\\s+?", "");
			/*
			 * In case we have numbers without the 'chr' upfront
			 */
			String chrNochr = ChromPosFilter.chrom.substring(3);
			if (t.equals(ChromPosFilter.chrom) || t.equals(chrNochr))
			{
				// parse in the number of Begin position
				double t2 = Double.parseDouble(l[beginColumn]);

				if (t2 >= ChromPosFilter.startPosition)
				{
					// parse in the number of End position
					double tEnd = Double.parseDouble(l[endColumn]);
					if (tEnd <= ChromPosFilter.endPosition)
					{
						ShowTable.FilteredArray.add(TempArrayOne.get(i));
						counter++;
					}
				}
			}
		}

		// verify if we have more then 1000 lines in the filtered array to print
		if (counter > 1000)
		{
			ShowTable.onlyPage = 0;
			end = 1000;
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = ShowTable.FilteredArray.size();
			ShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(ShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();

		ShowTable.frame.dispose();

		filterName.add("Chromosome Position");
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		ShowTable.arrayOfArrays.add(ShowTable.FilteredArray);
		ShowTable.into2DArrayFilterData(TempArray, end);

	}

	public void prefilter() throws IOException
	{

		int colForSort = 0;
		int spliceCol = 0;

		int lineCount = 0;
		// counter of only the good variants
		int datacount = 0;
		int end = 0;

		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
		ShowTable.FilteredArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();

		// find which is the column named Coding_Impact
		for (int i = 0; i < ShowTable.columnNames.length; i++)
		{
			if (ShowTable.columnNames[i].contains("Coding_Impact"))
			{
				// System.out.println("We will be selecting by coulmn: ");
				// System.out.println(ShowTable.columnNames[i]);
				colForSort = i;
			}

			// find the Splice column number
			else if (ShowTable.columnNames[i].contains("Splice_Site"))
			{
				// System.out.println("We will be selecting by coulmn: ");
				// System.out.println(ShowTable.columnNames[i]);
				spliceCol = i;
			}

		}

		String line = "";
		String[] head;

		boolean match;
		BufferedReader bReader = null;
		try
		{
			bReader = new BufferedReader(new FileReader(Interface.file));
		} catch (IOException ex)
		{
			Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		try
		{
			line = bReader.readLine();
		} catch (IOException ex)
		{
			Logger.getLogger(ReadFile.class.getName()).log(Level.SEVERE, null,
					ex);
		}

		for (int i = 0; i < 1; i++)
		{
			head = line.split("\t");
		}
		long fileInBytes = Interface.file.length();
		System.out.println("Filelenght is: " + fileInBytes);
		// calculating how many lines on average 3 percent would be,
		// 1358 is the size of each line in bytes
		int threePerc = (int) ((fileInBytes / 1358) / 100) * 3;

		while ((line = bReader.readLine()) != null)
		{
			lineCount++;
			// check the percentage, if true increment the progress bar
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				b = b + 3;
				progressBar.setValue(b);
				progressBar.setStringPainted(true);
			}
			/*
			 * if ((lineCount % 10000) == 0) {
			 * System.out.println("Line count is: " + lineCount); }
			 */

			String[] l = line.split("\t");
			String s = l[colForSort];

			if (s.toLowerCase().contains("nonsy")
					|| s.toLowerCase().contains("frame"))
			{
				datacount++;
				String nt;
				// adding an index plus first element needs to be an empty space
				// for future adition of imported genotypes
				nt = "N/A" + "\t" + datacount + "\t" + line;
				ScrippsGenomeAdviserUI.Reader ob1 = new ScrippsGenomeAdviserUI.Reader(
						nt);
				ReadFile.arrayOfLines.add(ob1);
				ShowTable.FilteredArray.add(ob1);

			} else
			{
				// find the splice site column
				String splice = l[spliceCol];
				if (splice.contains("Splice"))
				{
					datacount++;
					String nt;
					// adding an index plus first element needs to be an empty
					// space for future adition of imported genotypes
					nt = "N/A" + "\t" + datacount + "\t" + line;
					ScrippsGenomeAdviserUI.Reader ob1 = new ScrippsGenomeAdviserUI.Reader(
							nt);
					ReadFile.arrayOfLines.add(ob1);
					ShowTable.FilteredArray.add(ob1);
				}

			}
		}

		System.out.println("Datacount is: " + datacount);
		/*
		 * variables needed to build that darn table; get rid of some of them
		 */
		String newHead = "Imported Genotypes" + "\t" + "Data Counter" + "\t"
				+ Interface.tempHeadForPrefilter + "\t" + "Comments";
		ShowTable.columnNames = newHead.split("\t");
		ShowTable.columns = newHead.split("\t");

		if (datacount > 1000)
		{
			ShowTable.onlyPage = 0;
			end = 1000;
		} else
		{
			end = ReadFile.arrayOfLines.size();
			ShowTable.onlyPage = 1;
		}

		for (int j = 0; j < end; j++)
		{
			TempArray.add(ReadFile.arrayOfLines.get(j));
		}
		frame.dispose();
		// Interface.frame.dispose();

		filterName.add("Coding & Splice Variants");
		ShowTable.arrayOfArrays.add(ReadFile.arrayOfLines);
		ShowTable.into2DArrayFilterData(TempArray, end);
	}

	public void IDIOMprefilter()
	{

		IDIOMmultipleChildren.fr.dispose();
		int affected = 0;
		int unaffectedOne = 0;
		int unaffectedTwo = 0;
		int genoFour = 0;
		int genoFive = 0;

		int passCol = 0;
		int colForSort = 0;
		int spliceCol = 0;
		int GeneCol = 0;
		int colForEnd = 0;
		int AlleleCol = 0;
		int thousandGen = 0;
		int GC69 = 0;
		int GCWELL = 0;

		int lineCount = 0;
		// counter of only the good variants
		int datacount = 0;

		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArrayOne = ShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		int end = TempArrayOne.size();

		int linesLeft = end;
		int threePerc = linesLeft / 100 * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}

		// pick the genotypes and find which is the column named Coding_Impact
		for (int i = 0; i < ShowTable.columnNames.length; i++)
		{
			if (ShowTable.columnNames[i]
					.contains(IDIOMmultipleChildren.affectedGeno))
			{
				affected = i;
				System.out.println("Affected genotype column: " + i);
			}

			else if (ShowTable.columnNames[i]
					.contains(IDIOMmultipleChildren.unaffectedGenoOne))
			{
				unaffectedOne = i;
				System.out.println("Unaffected genotype column: " + i);
			}

			else if (ShowTable.columnNames[i]
					.contains(IDIOMmultipleChildren.unaffectedGenoTwo))
			{
				unaffectedTwo = i;
				System.out.println("Unaffected genotype column: " + i);
			}

			else if (ShowTable.columnNames[i].contains("QUAL~FILTER"))
			{
				passCol = i;
			}

			else if (ShowTable.columnNames[i].equals("Gene"))
			{
				GeneCol = i;
			}

			else if (ShowTable.columnNames[i].contains("Coding_Impact"))
			{
				// System.out.println("We will be selecting by coulmn: ");
				// System.out.println(ShowTable.columnNames[i]);
				colForSort = i;
			}

			// find the Splice column number
			else if (ShowTable.columnNames[i].contains("Splice_Site"))
			{
				// System.out.println("We will be selecting by coulmn: ");
				// System.out.println(ShowTable.columnNames[i]);
				spliceCol = i;
			}

			else if (ShowTable.columnNames[i].contains("End"))
			{
				// System.out.println("We will be selecting by coulmn: ");
				// System.out.println(ShowTable.columnNames[i]);
				colForEnd = i;
			}

			// find the Splice column number
			else if (ShowTable.columnNames[i].contains("Allele"))
			{
				// System.out.println("We will be selecting by coulmn: ");
				// System.out.println(ShowTable.columnNames[i]);
				AlleleCol = i;
			}

			else if (ShowTable.columnNames[i].contains("1000GENOMES"))
			{
				thousandGen = i;
				// System.out.println("1000GENOMES column number is: " +
				// thousandGen);
			}

			else if (ShowTable.columnNames[i].contains("69"))
			{
				GC69 = i;
				// System.out.println("GC_69 frequency column is: " + GC69);
			}

			else if (ShowTable.columnNames[i].contains("WELLDERLY"))
			{
				GCWELL = i;
				// System.out.println("WELLDELY frequency column is: " +
				// GCWELL);
			} else if (!IDIOMmultipleChildren.genoFour.equals("N/A"))
			{
				if (ShowTable.columnNames[i]
						.contains(IDIOMmultipleChildren.genoFour))
				{
					genoFour = i;
				}
			} else if (!IDIOMmultipleChildren.genoFive.equals("N/A"))
			{
				if (ShowTable.columnNames[i]
						.contains(IDIOMmultipleChildren.genoFive))
				{
					genoFive = i;
				}
			}

		}

		for (int i = 0; i < end; i++)
		{

			lineCount++;
			// check the percentage, if true increment the progress bar
			if ((lineCount % threePerc) == 0)
			{
				frame.setVisible(true);
				b = b + 3;
				progressBar.setValue(b);
				progressBar.setStringPainted(true);
			}

			String line = TempArrayOne.get(i).fileRow;
			String[] l = line.split("\t");

			/*
			 * Extract the PASS call, see if this the client wants to check PASS
			 * or not
			 */

			if (IDIOMmultipleChildren.pass.equals("YES"))
			{
				// System.out.println("Pass is: " + IDIOMmultipleChildren.pass);
				String p = l[passCol];
				String[] pTest = p.split("~");
				if (pTest.length > 1)
				{
					if (!pTest[1].equals("PASS"))
					{
						continue;
					}
				} else
				{
					// System.out.println(p);
					continue;
				}
			}

			String[] g = l[GeneCol].split("///");
			String s = l[colForSort];

			// Extracting the frequencies
			String ThousandFreq = l[thousandGen];
			String GC = l[GC69];
			String WELL = l[GCWELL];

			double thousand = 0.0;
			double gc = 0.0;
			double wel = 0.0;

			// Parse the frequencies
			try
			{
				thousand = Double.parseDouble(ThousandFreq);
			} catch (Exception ex)
			{
				thousand = 0.0;
			}

			try
			{
				gc = Double.parseDouble(GC);
			} catch (Exception ex)
			{
				gc = 0.0;
			}

			try
			{
				wel = Double.parseDouble(WELL);
			} catch (Exception ex)
			{
				wel = 0.0;
			}

			// Make sure this is a rare variant
			if (thousand < 0.01 && gc < 0.01 && wel < 0.01)
			{

				if (s.toLowerCase().contains("nonsy")
						|| s.toLowerCase().contains("frame")
						|| s.toLowerCase().contains("nonsense"))
				{
					String aff = l[affected];
					String unaffOne = l[unaffectedOne];
					String unaffTwo = l[unaffectedTwo];

					String gFour = "N/A";
					String gFive = "N/A";
					String fourAffect = IDIOMmultipleChildren.genoFourAffect;
					String fiveAffect = IDIOMmultipleChildren.genoFiveAffect;

					// Extract the genotype four and five if present
					if (genoFour != 0)
					{
						gFour = l[genoFour];
					}

					if (genoFive != 0)
					{
						gFive = l[genoFive];
					}

					// check the genotypes
					if (IDIOM(aff, unaffOne, unaffTwo, gFour, gFive,
							fourAffect, fiveAffect))
					{
						datacount++;
						ShowTable.FilteredArray.add(TempArrayOne.get(i));
					} else
					{
						String e = l[colForEnd];
						String all = l[AlleleCol];
						String end_allele = e + "_" + all;
						if (fourAffect.equals("N/A") || gFour.equals("N/A"))
						{
							fourAffect = "Unaffected";
							// TwoNA = "N/A";
						}
						if (fiveAffect.equals("N/A") || gFive.equals("N/A"))
						{
							fiveAffect = "Unaffected";
							// ThreeNA = "N/A";
						}
						/*
						 * Check if it fits the compound heterozygous criteris
						 */
						int linesAdded = compoundHetero(aff, unaffOne,
								unaffTwo, line, g, end_allele, gFour,
								fourAffect, gFive, fiveAffect);
						datacount = datacount + linesAdded;

					}

				} else
				{
					// find the splice site column
					String splice = l[spliceCol];

					if (splice.contains("Splice"))
					{

						String aff = l[affected];
						String unaffOne = l[unaffectedOne];
						String unaffTwo = l[unaffectedTwo];
						String gFour = "N/A";
						String gFive = "N/A";
						String fourAffect = IDIOMmultipleChildren.genoFourAffect;
						String fiveAffect = IDIOMmultipleChildren.genoFiveAffect;

						// Extract the genotype four and five if present
						if (genoFour != 0)
						{
							gFour = l[genoFour];
						}

						if (genoFive != 0)
						{
							gFive = l[genoFive];
						}

						// check the genotypes
						if (IDIOM(aff, unaffOne, unaffTwo, gFour, gFive,
								fourAffect, fiveAffect))
						{
							datacount++;
							ShowTable.FilteredArray.add(TempArrayOne.get(i));
						} else
						{
							String e = l[colForEnd];
							String all = l[AlleleCol];
							String end_allele = e + "_" + all;
							String TwoNA = "";
							String ThreeNA = "";
							/*
							 * Check if it fits the compound heterozygous
							 * criteris
							 */
							// some parsing for the 2 more children
							if (fourAffect.equals("N/A") || gFour.equals("N/A"))
							{
								fourAffect = "Unaffected";
								TwoNA = "N/A";
							}
							if (fiveAffect.equals("N/A") || gFive.equals("N/A"))
							{
								fiveAffect = "Unaffected";
								ThreeNA = "N/A";
							}
							int linesAdded = compoundHetero(aff, unaffOne,
									unaffTwo, line, g, end_allele, gFour,
									fourAffect, gFive, fiveAffect); // , TwoNA,
																	// ThreeNA);
							datacount = datacount + linesAdded;
						}

					}

				}
			}
		}

		/*
		 * Add all of the variants from the compoundSet to the filtered array
		 */
		Iterator it = compoundSet.iterator();
		int setData = 0;

		while (it.hasNext())
		{
			setData++;
			Object li = it.next();
			String l = li.toString();
			Reader ob2 = new Reader(l);
			ShowTable.FilteredArray.add(ob2);
		}
		compoundSet.clear();

		int finalDatacount = ShowTable.FilteredArray.size();
		// System.out.println("Datacount is: " + datacount);
		// System.out.println("Set Datacount = " + setData);
		System.out.println("Datacount = " + finalDatacount);

		// verify if we have more then 1000 lines in the filtered array to print
		if (datacount > 1000)
		{
			ShowTable.onlyPage = 0;
			end = 1000;
		}
		// if we have less then 1000 lines print all the lens, set the page as
		// the only page
		else
		{
			end = ShowTable.FilteredArray.size();
			ShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(ShowTable.FilteredArray.get(i));
		}

		// dispose the progresses bar
		frame.dispose();
		ShowTable.frame.dispose();

		filterName.add("IDIOM filter");
		// change the count of current array
		currentArray = currentArray + 1;
		// Insert newly filtered array
		ShowTable.arrayOfArrays.add(ShowTable.FilteredArray);
		ShowTable.into2DArrayFilterData(TempArray, end);
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
					CompoundHeteroObj ob1 = new CompoundHeteroObj(tOne, tTwo,
							line, end_allele, gene[0], tFour, tFive,
							fourAffect, fiveAffect);
					ArrayList<CompoundHeteroObj> temp = new ArrayList<CompoundHeteroObj>();
					temp.add(ob1);
					GeneMap.put(gene[0], temp);

				} else if (GeneMap.containsKey(gene[0]))
				{
					/*
					 * Gene was already previously found, we need to print both
					 * variants if it fits our criteria
					 */
					// first check if this gene was not already printed
					ArrayList<CompoundHeteroObj> temp = new ArrayList<CompoundHeteroObj>();
					temp = GeneMap.get(gene[0]);
					// iterate through each element of the arrayList
					for (int i = 0; i < temp.size(); i++)
					{
						CompoundHeteroObj ob3 = temp.get(i);
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
										CompoundHeteroObj ob4 = new CompoundHeteroObj(
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

										CompoundHeteroObj ob4 = new CompoundHeteroObj(
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
						CompoundHeteroObj ob4 = new CompoundHeteroObj(tOne,
								tTwo, line, end_allele, gene[0], tFour, tFive,
								fourAffect, fiveAffect);
						temp.add(ob4);
						GeneMap.put(gene[0], temp);
					}
				}

			}

		}

		return datacount;
	}

	public boolean IDIOM(String aff, String unaffOne, String unaffTwo,
			String gFour, String gFive, String fourAffect, String fiveAffect)
	{
		/*
		 * De-novo variants: variants present in the affected child only and
		 * neither of the parents
		 */
		/*
		 * if((aff.contains("0/1") || aff.contains("1/1") || aff.contains("1/0")
		 * || aff.contains("0|1") || aff.contains("1|1") || aff.contains("1|0"))
		 * && !unaffOne.contains("1") && !unaffTwo.contains("1")){
		 * 
		 * 
		 * 
		 * //check the other 2 children if (!gFour.equals("N/A")) { if
		 * (fourAffect.equals("Affected") && gFour.contains("1") &&
		 * (gFive.equals("N/A") || (gFive.contains("1") &&
		 * fiveAffect.equals("Affected")))) {
		 * 
		 * return true; } else if (fourAffect.equals("Unaffected") &&
		 * !gFour.contains("1")) {
		 * 
		 * if (gFive.equals("N/A") || (fiveAffect.equals("Unaffected") &&
		 * !gFive.contains("1"))) { return true; } else if (gFive.contains("1")
		 * && fiveAffect.equals("Affected")) { return true; } else { return
		 * false; }
		 * 
		 * } } else { return true; } return true; }
		 */
		if (!unaffOne.contains("1") && !unaffTwo.contains("1"))
		{
			if (aff.contains("0/1") || aff.contains("1/1")
					|| aff.contains("1/0") || aff.contains("0|1")
					|| aff.contains("1|1") || aff.contains("1|0"))
			{

				if (gFour.equals("N/A") && gFive.equals("N/A"))
				{
					return true;
				} else if (fourAffect.equals("Unaffected")
						&& !gFour.contains("1") && gFive.equals("N/A"))
				{
					return true;
				} else if (fourAffect.equals("Affected") && gFour.contains("1")
						&& gFive.equals("N/A"))
				{
					return true;
				} else if (fourAffect.equals("Affected") && gFour.contains("1")
						&& fiveAffect.equals("Unaffected")
						&& !gFive.contains("1"))
				{
					return true;
				} else if (fourAffect.equals("Affected") && gFour.contains("1")
						&& fiveAffect.equals("Affected") && gFive.contains("1"))
				{
					return true;
				} else
				{
					return false;
				}

			}
		}

		/*
		 * Homozygous in child only: Should be heterozygous in both parents and
		 * homozygous in the child (Recessive inheritance)
		 */
		else if ((aff.contains("1|1") || aff.contains("1/1"))
				&& (unaffOne.contains("0/1") || unaffOne.contains("1/0")
						|| unaffOne.contains("0|1") || unaffOne.contains("1|0"))
				&& (unaffTwo.contains("0|1") || unaffTwo.contains("1|0")
						|| unaffTwo.contains("1/0") || unaffTwo.contains("0/1")))
		{

			if (gFour.equals("N/A") && gFive.equals("N/A"))
			{
				return true;
			} else if (fourAffect.equals("Unaffected")
					&& !(gFour.contains("1|1") || gFour.contains("1/1"))
					&& gFive.equals("N/A"))
			{
				return true;
			} else if (fourAffect.equals("Affected")
					&& (gFour.contains("1|1") || gFour.contains("1/1"))
					&& gFive.equals("N/A"))
			{
				return true;
			} else if (fourAffect.equals("Affected")
					&& (gFour.contains("1|1") || gFour.contains("1/1"))
					&& fiveAffect.equals("Unaffected") && !gFive.contains("1"))
			{
				return true;
			} else if (fourAffect.equals("Affected")
					&& (gFour.contains("1|1") || gFour.contains("1/1"))
					&& fiveAffect.equals("Affected") && gFive.contains("1"))
			{
				return true;
			} else
			{
				return false;
			}

			// check the other 2 children
			/*
			 * if (!gFour.equals("N/A")) { if (fourAffect.equals("Affected") &&
			 * (gFour.contains("1|1") || gFour.contains("1/1"))){ if
			 * (gFive.equals("N/A")) { return true; } else if
			 * (gFive.equals("Affected") && (gFive.contains("1|1") ||
			 * gFive.contains("1/1"))) { return true; } else if
			 * (gFive.equals("Unaffected") && (gFive.contains("0|1") ||
			 * gFive.contains("1|0") || gFive.contains("1/0") ||
			 * gFive.contains("0/1"))) { return true; } else { return false; } }
			 * else if (fourAffect.equals("Unaffected") &&
			 * (gFour.contains("0|1") || gFour.contains("1|0") ||
			 * gFour.contains("1/0") || gFour.contains("0/1"))) { if
			 * (gFive.equals("N/A")) { return true; } else if
			 * (gFive.equals("Affected") && (gFive.contains("1|1") ||
			 * gFive.contains("1/1"))) { return true; } else if
			 * (gFive.equals("Unaffected") && (gFive.contains("0|1") ||
			 * gFive.contains("1|0") || gFive.contains("1/0") ||
			 * gFive.contains("0/1"))) { return true; } else { return false; } }
			 * }
			 * 
			 * return true;
			 */
		}

		return false;
	}

	public static void geneList()
	{

		fileChooser = new JFileChooser();
		int returnVal = fileChooser.showSaveDialog(FilterFunctions.frame);
		if (returnVal == JFileChooser.APPROVE_OPTION)
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
					Logger.getLogger(Interface.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		}

		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArray = new ArrayList<ScrippsGenomeAdviserUI.Reader>();
		// extracting the last filtered array from the arrayOfArrays
		ArrayList<ScrippsGenomeAdviserUI.Reader> TempArrayOne = ShowTable.arrayOfArrays
				.get(currentArray);
		System.out.println("The size of the main array is: "
				+ TempArrayOne.size());
		// the end of for loop iteration
		int end = TempArrayOne.size();

		// progress bar calculations
		int lineCount = 0;
		int linesLeft = end;
		int threePerc = linesLeft / 100 * 3;
		if (threePerc < 1)
		{
			threePerc = 1;
		}
		SaveProgress pr = new SaveProgress();

		int counter = 0;
		int colNumber = 0;
		for (int i = 0; i < ShowTable.columns.length; i++)
		{
			if (ShowTable.columns[i].contentEquals("Gene"))
			{

				// System.out.println(ShowTable.columns[i]);
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
				b = b + 3;
				progressBar.setValue(b);
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
					ShowTable.FilteredArray.add(TempArrayOne.get(i));
				}
			}
		}

		if (found_counter > 1000)
		{
			ShowTable.onlyPage = 0;
			end = 1000;
		} else
		{
			end = ShowTable.FilteredArray.size();
			ShowTable.onlyPage = 1;
		}

		for (int i = 0; i < end; i++)
		{
			TempArray.add(ShowTable.FilteredArray.get(i));
		}
		frame.dispose();
		ShowTable.frame.dispose();

		currentArray = currentArray + 1;
		filterName.add("Gene list filter");
		ShowTable.arrayOfArrays.add(ShowTable.FilteredArray);
		ShowTable.into2DArrayFilterData(TempArray, end);
	}

	@Override
	public void run()
	{
		pageNumber = 1;
		if (ShowTable.tableStatus == 3)
		{
			CodingVar();
		}
		if (ShowTable.tableStatus == 4 || ShowTable.tableStatus == 12
				|| ShowTable.tableStatus == 13)
		{
			SpliceVar();
		}
		if (ShowTable.tableStatus == 5)
		{
			KnownDisease();
		}
		if (ShowTable.tableStatus == 6)
		{
			PredClinical();
		}
		if (ShowTable.tableStatus == 7)
		{
			PredResearch();
		}
		if (ShowTable.tableStatus == 8)
		{
			CancerGenes();
		}
		if (ShowTable.tableStatus == 9)
		{
			Pharmacogenetic();
		}
		if (ShowTable.tableStatus == 10)
		{
			TruncatedVariants();
		}
		if (ShowTable.tableStatus == 11)
		{
			NondbSNP();
		}
		if (ShowTable.tableStatus == 14)
		{
			AdvanceFilterAlgorithm();
		}
		if (ShowTable.tableStatus == 15)
		{
			FilterFunction();
		}
		if (ShowTable.tableStatus == 16)
		{
			chromPosition();
		}
		if (ShowTable.tableStatus == 18)
		{
			AdvanceFilterAlgorithmOR();
		}
		if (ShowTable.tableStatus == 19)
		{
			try
			{
				prefilter();
			} catch (IOException ex)
			{
				Logger.getLogger(FilterFunctions.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
		if (ShowTable.tableStatus == 20)
		{
			ValidValues();
		}
		if (ShowTable.tableStatus == 21)
		{
			IDIOMprefilter();
		}
		if (ShowTable.tableStatus == 22)
		{
			geneList();
		}

	}

}
