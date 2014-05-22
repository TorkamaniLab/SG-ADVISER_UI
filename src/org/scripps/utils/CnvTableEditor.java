package org.scripps.utils;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class CnvTableEditor extends AbstractCellEditor implements
		TableCellEditor
{
	// This is the component that will handle the editing of the cell value
	JComponent component = new JTextField();
	// String t;
	public int rowInd;
	public int colInd;
	public Object FilterID;

	// public Object entireArrayID;

	// This method is called when a cell value is edited by the user.
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int rowIndex, int vColIndex)
	{
		// 'value' is value contained in the cell located at (rowIndex,
		// vColIndex)

		if (isSelected)
		{
			// cell (and perhaps other cells) are selected
		}

		// Configure the component with the specified value
		((JTextField) component).setText((String) value);
		rowInd = rowIndex;
		colInd = vColIndex;
		FilterID = table.getValueAt(rowInd, getHeaderIndex("Index"));
		System.out.println("Filter ID is supposed to be: " + FilterID);
		return component;
	}

	public int getHeaderIndex(String var)
	{
		int index = 0;
		for (int i = 0; i < CnvShowTable.columns.length; i++)
		{
			if (CnvShowTable.columns[i].contains(var))
			{
				index = i;
				break;
			}
		}

		return index;
	}

	// This method is called when editing is completed.
	// It must return the new value to be stored in the cell.
	public Object getCellEditorValue()
	{

		String t = (String) ((JTextField) component).getText();
		System.out.println(t);

		String i = (String) FilterID;
		int in = Integer.parseInt(i);
		String tr2 = CnvReadFile.arrayOfLines.get(in - 1).fileRow;
		// tr2 = tr2.replace("\n", "").replace("\r", "");
		String finalLine = "";
		/*
		 * Checking if a comment was there already in the last column
		 */
		String[] trSplit = tr2.split("\t");
		if (trSplit.length >= CnvShowTable.columns.length)
		{
			trSplit[CnvShowTable.columns.length - 1] = t;
			for (int som = 0; som < CnvShowTable.columns.length; som++)
			{
				finalLine = finalLine.concat("\t").concat(trSplit[som]);
			}

		} else
		{
			finalLine = tr2.concat("\t" + t);
		}
		// System.out.println("This is supposed to be added: " + tr2);
		CnvReadFile.arrayOfLines.get(in - 1).fileRow = finalLine;
		/*
		 * System.out.println("Row is: " + rowInd);
		 * System.out.println("Column is: " + colInd);
		 * System.out.println("Haplotype ID is: " + FilterID);
		 * System.out.println("The entire line is: " +
		 * ReadFile.arrayOfLines.get(in - 1).fileRow);
		 */
		return ((JTextField) component).getText();
	}

}
