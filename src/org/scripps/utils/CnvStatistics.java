/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scripps.utils;

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

import org.scripps.cnvViewer.CnvViewerInterface;
import org.scripps.utils.CnvReader;

/**
 * 
 * @author gerikson
 */
public class CnvStatistics implements Runnable
{
	private int gain_count = 0;
	private int loss_count = 0;
	private int whole_deletion = 0;
	private int whole_amplification = 0;
	private int c_terminal = 0;
	private int n_terminal = 0;
	private int cat_one_var = 0;
	private int cat_oneStar_var = 0;
	private int cat_two_var = 0;
	private int cat_twoStar_var = 0;
	private int cat_three_var = 0;
	private int cat_fourStar_var = 0;
	private int cat_four_var = 0;
	private int cat_five_var = 0;
	private int cat_six_var = 0;
	private int wellderly_overlap = 0;
	private int ClinVar_overlap = 0;

	
	private int datacount = 0;
	private static String[] header;
	private JFrame frame;
	private Container content;
	private JProgressBar progressBar;
	private Border border;
	private static int progBarCalc = 0;
	private ArrayList<String> ADVISER1_genotype = new ArrayList<String>();

	public CnvStatistics(String head, String s)
	{

		datacount = 0;
		header = head.split("\t");
		gain_count = 0;
		loss_count = 0;
		whole_deletion = 0;
		whole_amplification = 0;
		c_terminal = 0;
		n_terminal = 0;
		cat_one_var = 0;
		cat_oneStar_var = 0;
		cat_two_var = 0;
		cat_twoStar_var = 0;
		cat_three_var = 0;
	    cat_fourStar_var = 0;
		cat_four_var = 0;
		cat_five_var = 0;
		cat_six_var = 0;
		wellderly_overlap = 0;
		ClinVar_overlap = 0;
		
		frame = new JFrame("Statistics");
		content = frame.getContentPane();
		progressBar = new JProgressBar();
		border = BorderFactory.createTitledBorder(s);
		progressBar.setBorder(border);
		content.add(progressBar, BorderLayout.NORTH);
		frame.setSize(300, 100);
		progBarCalc = 0;
	}

	private int getHeaderIndex(String var)
	{
		int index = 0;
		for (int i = 0; i < header.length; i++)
		{
			// if (header[i].contains(var)) {
			if (header[i].equals(var))
			{
				index = i;
			}
		}

		return index;
	}

	// get the info in some column
	private String getColum(String var)
	{
		String value = "";
		for (int i = 0; i < header.length; i++)
		{
			if (header[i].contains(var))
			{
				value = header[i];
				break;
			}
		}
		return value;
	}


	/*
	 * This method is for when the user tries to calculate the statistics before
	 * loading in a file in the UI
	 */
	private void calculateStatsFile(File file) throws IOException
	{
		String line = "";
		String[] head;

		boolean match;
		BufferedReader bReader = null;
		try
		{
			bReader = new BufferedReader(new FileReader(file));
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
			head = line.split("\t");
		}
		long fileInBytes = file.length();
		System.out.println("Filelength is: " + fileInBytes);
		// calculating how many lines on average 3 percent would be,
		// 1358 is the size of each line in bytes
		int threePerc = (int) ((fileInBytes / 1358) / 100) * 3;

		while ((line = bReader.readLine()) != null)
		{

			datacount++;

			if (datacount % threePerc == 0)
			{
				frame.setVisible(true);
				progBarCalc = progBarCalc + 3;
				progressBar.setValue(progBarCalc);
				progressBar.setStringPainted(true);
				System.out.print(datacount + "\t");
				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				System.out.println(dateFormat.format(cal.getTime()));
			}

			String[] spLine = line.split("\t");
			
			calculateStatistics(spLine);

		}

		writeToFile();
		frame.dispose();

		System.out.println("Number of lines is: " + datacount);
		System.out.println("Each line contains raughly: " + fileInBytes
				/ datacount + " bytes");
	}

	/*
	 * This method is for when the file is loaded in already
	 */

	private void calculateStatsArray(ArrayList<CnvReader> TempArray)
			throws IOException
	{

		/*
		 * int varType = getHeaderIndex("VarType"); int codingImpact =
		 * getHeaderIndex("Coding_Impact");
		 */
		String line;

		for (int i = 0; i < TempArray.size(); i++)
		{

			int end = TempArray.size();
			// progress bar calculations
			int lineCount = 0;
			int linesLeft = end;
			int threePerc = linesLeft / 100 * 3;
			if (threePerc < 1)
			{
				threePerc = 1;
			}
			CnvSaveProgress pr = new CnvSaveProgress();

			datacount++;
			// check the percentage, if true increment the progress bar
			if (datacount % threePerc == 0)
			{
				frame.setVisible(true);
				progBarCalc = progBarCalc + 3;
				progressBar.setValue(progBarCalc);
				progressBar.setStringPainted(true);
				System.out.print(i + "\t");
				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				System.out.println(dateFormat.format(cal.getTime()));
			}

			line = TempArray.get(i).fileRow;

			String[] spLine = line.split("\t");

			calculateStatistics(spLine);
		}

		writeToFile();
		frame.dispose();
	}

	

	private void calculateStatistics(String[] spLine) {
		datacount++;
		if (spLine[getHeaderIndex("VarType")].contains("gain"))
		{
			gain_count++;
		} else if (spLine[getHeaderIndex("VarType")].contains("loss"))  {
			loss_count++;
		}
		
		if (spLine[getHeaderIndex("Coding_Impact")].contains("Deleted"))
		{
			whole_deletion++;
		} else if (spLine[getHeaderIndex("Coding_Impact")].contains("Amplified")) {
			whole_amplification++;
		} 
		
		if (spLine[getHeaderIndex("Location")].contains("C_Terminal"))
		{
			c_terminal++;
		} else if (spLine[getHeaderIndex("Location")].contains("N_Terminal")) {
			n_terminal++;
		} 
		

		
		if (spLine[getHeaderIndex("ADVISER_Score~Disease_Entry")].contains("1~")) {
			cat_one_var++;
		}
		else if (spLine[getHeaderIndex("ADVISER_Score~Disease_Entry")].contains("1*~")) {
			cat_oneStar_var++;
		}
		else if (spLine[getHeaderIndex("ADVISER_Score~Disease_Entry")].contains("2~")) {
			cat_two_var++;
		}
		else if (spLine[getHeaderIndex("ADVISER_Score~Disease_Entry")].contains("2*~")) {
			cat_twoStar_var++;
		}
		else if (spLine[getHeaderIndex("ADVISER_Score~Disease_Entry")].contains("3~")) {
			cat_three_var++;
		}
		else if (spLine[getHeaderIndex("ADVISER_Score~Disease_Entry")].contains("4~")) {
			cat_four_var++;
		}
		else if (spLine[getHeaderIndex("ADVISER_Score~Disease_Entry")].contains("4*~")) {
			cat_fourStar_var++;
		}
		else if (spLine[getHeaderIndex("ADVISER_Score~Disease_Entry")].contains("5~")) {
			cat_five_var++;
		}
		else if (spLine[getHeaderIndex("ADVISER_Score~Disease_Entry")].contains("6~")) {
			cat_five_var++;
		}
		
		if (spLine[getHeaderIndex("Wellderly_Loss_Overlap")].contains("well")) {
			wellderly_overlap++;
		} else if  (spLine[getHeaderIndex("Wellderly_Gain_Overlap")].contains("well")) {
			wellderly_overlap++;
		}
		
		if (!spLine[getHeaderIndex("ClinVar_Loss")].contains("-~-~-~-")) {
			ClinVar_overlap++;
		} else if  (!spLine[getHeaderIndex("ClinVar_Gain")].contains("-~-~-~-")) {
			ClinVar_overlap++;
		}
	}
	
	private void writeToFile() throws IOException
	{
		BufferedWriter outfile = new BufferedWriter(new FileWriter(
				"Statistics.txt", true));
		if (CnvFilterFunctions.filterName.isEmpty())
		{
			String fName = CnvViewerInterface.cnvFile.getName();
			outfile.write("\t" + "\t".concat("STATISTICS ").concat(fName)
					+ '\n');
		} else
		{
			outfile.write("\t"
					+ "\t".concat("STATISTICS ").concat(
							CnvFilterFunctions.filterName
									.get(CnvFilterFunctions.currentArray))
					+ '\n');
		}
        outfile.write("Total number of CNVs:\t" + "\t"+ Integer.toString(datacount) + "\n");
        outfile.write("Total gains:\t" + "\t" + Integer.toString(gain_count) + "\n");
        outfile.write("Total loss:\t" + "\t" + Integer.toString(loss_count) + "\n");
        outfile.write("Whole Deletions:\t" + "\t" + Integer.toString(whole_deletion) + "\n");
        outfile.write("Whole Amplification:\t" + "\t" + Integer.toString(whole_amplification) + "\n");
        outfile.write("C-terminal affected:\t" + "\t" + Integer.toString(c_terminal) + "\n");
        outfile.write("N-terminal affected:\t" + "\t" + Integer.toString(n_terminal) + "\n");
        outfile.write("Overlap with ClinVar CNVs:\t" + "\t" + Integer.toString(ClinVar_overlap) + "\n");
        outfile.write("Overlap with Wellderly CNVs:\t" + "\t" + Integer.toString(wellderly_overlap) + "\n");
        outfile.write("Category 1 CNVs:\t" + "\t" + Integer.toString(cat_one_var) + "\n");
        outfile.write("Category 1* CNVs:\t" + "\t" + Integer.toString(cat_oneStar_var) + "\n");
        outfile.write("Category 2 CNVs:\t" + "\t" + Integer.toString(cat_two_var) + "\n");
        outfile.write("Category 2* CNVs:\t" + "\t" + Integer.toString(cat_twoStar_var) + "\n");
        outfile.write("Category 3 CNVs:\t" + "\t" + Integer.toString(cat_three_var) + "\n");
        outfile.write("Category 4 CNVs:\t" + "\t" + Integer.toString(cat_four_var) + "\n");
        outfile.write("Category 4* CNVs:\t" + "\t" + Integer.toString(cat_fourStar_var) + "\n");
        outfile.write("Category 5 CNVs:\t" + "\t" + Integer.toString(cat_five_var) + "\n");
        outfile.write("Category 6 CNVs:\t" + "\t" + Integer.toString(cat_six_var) + "\n");
        
		outfile.close();
		System.out.println("Everything written to file!");
	}


	

	@Override
	public void run()
	{
		if (CnvViewerInterface.statistics == 1)
		{
			try
			{
				calculateStatsFile(CnvViewerInterface.cnvFile);
			} catch (IOException ex)
			{
				Logger.getLogger(CnvStatistics.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		} else if (CnvViewerInterface.statistics == 2)
		{
			// int last = ShowTable.arrayOfArrays.size();
			try
			{
				calculateStatsArray(CnvShowTable.arrayOfArrays
						.get(CnvFilterFunctions.currentArray));
			} catch (IOException ex)
			{
				Logger.getLogger(CnvStatistics.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}

	}
}
