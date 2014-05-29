/*
 * MergeSort algorithm
 */
package org.scripps.utils;

import java.awt.BorderLayout;
import java.awt.Container;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

/**
 * 
 * @author gerikson
 */
public class CnvMergeSort extends JFrame implements Runnable
{

	public static String selection;
	public static double[][] ArrayforSort;
	public static Object[][] SortedData;
	public static ArrayList<CnvReader> arrayOfLines;
	public static int dataLines;
	public static int datacount = 0;
	public static String threadName;
	// progress bar variable
	public static JFrame frame;
	public static Container content;
	public static JProgressBar progressBar;
	public static Border border;
	public static double perc2 = 0;
	public static int last = 0;
	public static int begin = 0;
	public static int nextPage = 1;
	// variable to see if we printed the last page already
	public static boolean tr = false;

	public CnvMergeSort(ArrayList<CnvReader> al, int d, String sel)
	{
		arrayOfLines = al;
		dataLines = d;
		selection = sel;
		// ArrayforSort = null;
		ArrayforSort = new double[dataLines][2];

		frame = new JFrame("Sorting");
		content = frame.getContentPane();
		progressBar = new JProgressBar();
		border = BorderFactory.createTitledBorder("Sorting by column " + sel
				+ " ...");
		progressBar.setBorder(border);
		content.add(progressBar, BorderLayout.NORTH);
		frame.setSize(300, 100);
		frame.setVisible(true);
	}

	public static void inputData(ArrayList<CnvReader> arrayOfLines, int dataL,
			String sel)
	{

		// The number of the column to be sorted
		int columnNumber = 0;
		// int CG_69 = 0;
		// ArrayList<String> frequency = new ArrayList<String>();
		// Set<String> CG_frequency;

		System.out.println("Thread active count: " + Thread.activeCount());
		System.out.println("current thread is: " + Thread.currentThread());

		boolean match;
		nextPage = 1;
		begin = 0;
		last = 0;
		// Counter for parser
		int readCount = 0;

		double y = dataLines;
		double ing = 100000.0;
		double x = ing / dataLines;
		double perc = x * 100;

		// Extracting the row number to sort by
		for (int s = 0; s < CnvShowTable.columns.length; s++)
		{
			if (selection == CnvShowTable.columns[s])
			{
				columnNumber = s;
			}
		}

		for (int j = 0; j < dataLines; j++)
		{
			readCount++;

			if ((readCount % 100000) == 0)
			{

				// Loading.frame.dispose();
				System.out.print("Preproces " + readCount + "\t");
				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				System.out.println(dateFormat.format(cal.getTime()));
				perc2 = perc2 + perc;
				int b = (int) perc2 - 2;
				progressBar.setValue(b);
				progressBar.setStringPainted(true);
			}

			// Get the column number
			double c;
			c = Double.parseDouble(Integer.toString(j));
			// undefined values need to be last
			double b = 999999999;

			String lines = arrayOfLines.get(j).fileRow;
			String[] datavalue = lines.split("\t");
			String col;

			if (columnNumber > datavalue.length - 1)
			{

				col = "999999999";

			} else
			{

				col = datavalue[columnNumber];
			}

			// check var type column first
			if (sel.equalsIgnoreCase("VarType"))
			{
				if ("del".equals(col))
				{
					b = 1;
				} else if ("delins".equals(col))
				{
					b = 2;
				} else if ("ins".equals(col))
				{
					b = 3;
				} else if ("snp".equals(col))
				{
					b = 4;
				}
			}
			// parse Chromosome
			else if (sel.equalsIgnoreCase("Chromosome"))
			{
				col = col.replaceAll("chr", "");
				if (col.equalsIgnoreCase("X"))
				{
					b = 100;
				} else if (col.equalsIgnoreCase("Y"))
				{
					b = 200;
				} else
				{
					b = Integer.parseInt(col);
				}
			}

			// parse all other columns
			else
			{
				String[] ADVISER = col.split("///");
				String ac1 = ADVISER[0];

				String ac;
				if (match = ac1.contains("~"))
				{
					String[] ac2 = ac1.split("~");
					ac = ac2[0];
				} else
				{
					ac = ac1.substring(0, Math.min(ac1.length(), 9));
				}

				// if ac is a number this should work
				try
				{
					b = Double.parseDouble(ac);

				}
				// else catch exception and transform ac
				catch (Exception ex)
				{
					ac = ac.replaceAll("\\s", "");

					// converting string to ASCII
					match = ac.contains("N/A");
					if (ac != "-" && ac != null && ac != "" && match == false)
					{

						// take only first 2 characters
						ac = ac.substring(0, Math.min(ac.length(), 3));
						String n = "";
						for (int i = 0; i < ac.length(); i++)
						{
							char ch = ac.charAt(i);
							// here is the trick from char to ASCII
							int jh = ch;
							String n1 = Integer.toString(jh);
							n = n + n1;
						}
						b = Integer.parseInt(n);
					}

					if (ac.equals("-"))
					{
						b = 999999999;
					}

				}

			}
			ArrayforSort[j][0] = c;
			ArrayforSort[j][1] = b;
		}

		datacount = 0;

		insertArray(ArrayforSort);
		String lin;
		int leng;

		int Rows = arrayOfLines.size();
		if (Rows > 1000)
		{
			leng = 1000;

		} else
		{
		
			leng = Rows;
			CnvShowTable.onlyPage = 1; //JUST CHANGED
			

		}

		begin = 0;
		last = 1000;

		SortedData = new Object[leng][CnvShowTable.columns.length];
		for (int i = 0; i < leng; i++)
		{
			double b;
			b = ArrayforSort[i][0];
			int value;
			value = (int) (b);
			lin = arrayOfLines.get(value).fileRow;
			String[] r = lin.split("\t");
			int size = r.length;
			for (int in = 0; in < size; in++)
			{
				// row.add(r[in]);
				SortedData[i][in] = r[in];

			}
		}

		CnvShowTable.frame.dispose();
		CnvShowTable.createContentTable(SortedData, CnvShowTable.columns,
				"Sorted File " + CnvReadFile.fileName + " by column: "
						+ selection);
		CnvShowTable.tableStatus = 23;
		//System.out.println("Before CnvShowTable.onlyPage: " + CnvShowTable.onlyPage );
		CnvShowTable.onlyPage = 0;
		//System.out.println("After CnvShowTable.onlyPage: " + CnvShowTable.onlyPage );
		frame.dispose();

	}

	// next page sort
	public static void SortNext() throws InterruptedException
	{
		int leng;
		int siz = arrayOfLines.size();
		begin = begin + 1000;
		last = last + 1000;
		if (last > siz)
		{
			leng = siz - begin;
			last = siz;
			CnvShowTable.onlyPage = 2; 
		} else
		{
			leng = 1000;
			CnvShowTable.onlyPage = 4; 
			System.out.println("Sort next: " + CnvShowTable.onlyPage);

		}

		SortedData = new Object[leng][CnvShowTable.columns.length];
		
		for (int i = begin; i < last; i++)
		{
			double b;
			b = ArrayforSort[i][0];
			int value;
			value = (int) (b);
			String lin = arrayOfLines.get(value).fileRow;
			String[] r = lin.split("\t");
			int size = r.length;
			for (int in = 0; in < size; in++)
			{
				SortedData[i - begin][in] = r[in];

			}
		}
	
		CnvShowTable.frame.dispose();
		CnvShowTable.createContentTable(SortedData, CnvShowTable.columns,
				"Sorted File " + CnvReadFile.fileName + " by column: "
						+ selection);
		CnvShowTable.tableStatus = 23;

	}

	// Previous page
	public static void previousPage()
	{
		int leng;
		int siz = arrayOfLines.size();
		begin = begin - 1000;
		last = last - 1000;
		if (begin <= 0)
		{
			begin = 0;
			last = 1000;
			CnvShowTable.onlyPage = 3;
			CnvFilterFunctions.pageNumber = 1;

		}

		SortedData = new Object[1000][CnvShowTable.columns.length];
		for (int i = begin; i < last; i++)
		{
			// Vector<Object> row = new Vector<Object>();
			double b;
			b = ArrayforSort[i][0];
			int value;
			value = (int) (b);
			String lin = arrayOfLines.get(value).fileRow;
			String[] r = lin.split("\t");
			int size = r.length;
			for (int in = 0; in < size; in++)
			{
				SortedData[i - begin][in] = r[in];

			}
		}

		CnvShowTable.frame.dispose();
		CnvShowTable.createContentTable(SortedData, CnvShowTable.columns,
				"Sorted File " + CnvReadFile.fileName + " by column: "
						+ selection);
		CnvShowTable.tableStatus = 23;
		CnvShowTable.onlyPage = 4;
	}

	public static void insertArray(double[][] data)
	{

		final String BLANKS = "  "; // A String of two blanks

		int i;
		int j;

		mergesort(data, 0, data.length);

	}

	/**
	 * Sort an array of integers from smallest to largest, using a merge sort
	 * algorithm.
	 * */
	public static void mergesort(double[][] data, int first, int n)
	{

		int n1; // Size of the first half of the array
		int n2; // Size of the second half of the array
		if (n > 1)
		{
			datacount = datacount + 1;

			if (datacount % 100000 == 0)
			{

				// Loading.frame.dispose();
				System.out.print(datacount + "\t");
				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				System.out.println(dateFormat.format(cal.getTime()));
			}
			// Compute sizes of the two halves
			n1 = n / 2;
			n2 = n - n1;
			mergesort(data, first, n1); // Sort data[first] through
										// data[first+n1-1]
			mergesort(data, first + n1, n2); // Sort data[first+n1] to the end
			merge(data, first, n1, n2);
		}
	}

	private static void merge(double[][] data, int first, int n1, int n2)
	// Precondition: data has at least n1 + n2 components starting at
	// data[first]. The first
	// n1 elements (from data[first] to data[first + n1 ñ 1] are sorted from
	// smallest
	// to largest, and the last n2 (from data[first + n1] to data[first + n1 +
	// n2 - 1]) are also
	// sorted from smallest to largest.
	// Postcondition: Starting at data[first], n1 + n2 elements of data
	// have been rearranged to be sorted from smallest to largest.
	// Note: An OutOfMemoryError can be thrown if there is insufficient

	// memory for an array of n1+n2 ints.

	{
		double[][] temp = new double[n1 + n2][2]; // Allocate the temporary
													// array
		int copied = 0; // Number of elements copied from data to temp
		int copied1 = 0; // Number copied from the first half of data
		int copied2 = 0; // Number copied from the second half of data
		int i; // Array index to copy from temp back into data

		// Merge elements, copying from two halves of data to the temporary
		// array.
		while ((copied1 < n1) && (copied2 < n2))
		{
			if (data[first + copied1][1] < data[first + n1 + copied2][1])
			{
				// copied++;
				// copied1++;
				temp[copied][0] = data[first + (copied1)][0];
				// System.out.println(temp[copied][0] + "\t");
				temp[copied][1] = data[first + (copied1)][1];
				// System.out.println(temp[copied][1] + "\t");
				copied++;
				copied1++;
			} else
			{
				temp[copied][0] = data[first + n1 + (copied2)][0];
				temp[copied][1] = data[first + n1 + (copied2)][1];
				copied++;
				copied2++;
			}
		}

		// Copy any remaining entries in the left and right subarrays.
		while (copied1 < n1)
		{
			temp[copied][0] = data[first + (copied1)][0];
			temp[copied][1] = data[first + (copied1)][1];
			copied++;
			copied1++;
		}

		while (copied2 < n2)
		{
			temp[copied][0] = data[first + n1 + (copied2)][0];
			temp[copied][1] = data[first + n1 + (copied2)][1];
			copied++;
			copied2++;
		}

		// Copy from temp back to the data array.
		for (i = 0; i < n1 + n2; i++)
		{
			data[first + i][0] = temp[i][0];
			data[first + i][1] = temp[i][1];
		}
	}

	@Override
	public void run()
	{
		// inputData(arrayOfLines, dataLines, selection, headers);

		final String orgName = Thread.currentThread().getName();
		threadName = Thread.currentThread().getName();
		Thread.currentThread().setName(orgName + "firstThread");
		try
		{
			perc2 = 0;
			inputData(arrayOfLines, dataLines, selection);
			//System.out.println("After calling CnvShowTable.onlyPage: " + CnvShowTable.onlyPage );
		} finally
		{
			Thread.currentThread().setName(orgName);
		}
	}

}
