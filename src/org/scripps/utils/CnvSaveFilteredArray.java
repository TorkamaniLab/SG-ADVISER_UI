/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scripps.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

public class CnvSaveFilteredArray implements Runnable
{

	public static JFileChooser fc;
	public static ArrayList<CnvReader> tempArray = new ArrayList<CnvReader>();

	public CnvSaveFilteredArray()
	{

	}

	public static void saveFunct() throws IOException
	{

		fc = new JFileChooser();
		int returnVal = fc.showSaveDialog(CnvFilterFunctions.frame);
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fc.getSelectedFile();
			String fName = file.getName();
			/*
			 * DateFormat dateFormat = new
			 * SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); Calendar cal =
			 * Calendar.getInstance();
			 * System.out.println(dateFormat.format(cal.getTime()));
			 */
			if (!file.exists())
			{
				file.createNewFile();
				FileWriter fileWriter = new FileWriter(file);
				String headerName = "";
				for (int i = 0; i < CnvShowTable.columns.length; i++)
				{
					headerName = headerName.concat(CnvShowTable.columns[i])
							.concat("\t");
				}
				fileWriter.write(headerName);
				fileWriter.write("\n");

				// int i = ShowTable.arrayOfArrays.size();
				// tempArray = ShowTable.arrayOfArrays.get(i - 1);
				// print current array

				// Make sure there are any filtered arrays, if there are none,
				// just print the main array
				// if (FilterFunctions.currentArray) {

				// } else {
				tempArray = CnvShowTable.arrayOfArrays
						.get(CnvFilterFunctions.currentArray);
				// }
				for (int t = 0; t < tempArray.size(); t++)
				{
					String line;
					// Make sure there is something in FilteredArray
					/*
					 * if (ShowTable.FilteredArray.isEmpty()) { line =
					 * ShowTable.FilteredArray.get(t).fileRow; } else { line =
					 * ShowTable.FilteredArray.get(t).fileRow; }
					 */

					line = tempArray.get(t).fileRow;

					fileWriter.write(line);
					fileWriter.write("\n");

				}

				fileWriter.close();
				System.out.println("Print to file finished!");
			}

		}
		CnvShowTable.threadStat = false;
	}

	@Override
	public void run()
	{
		try
		{
			saveFunct();
		} catch (IOException ex)
		{
			Logger.getLogger(CnvSaveFilteredArray.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}
}
