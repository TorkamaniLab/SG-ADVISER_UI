package org.scripps.utils;

public class CnvReader
{
	public String fileRow;
	public String Comments;
	public static int lineNumber;
	public static int VCFcompare;

	// Initialize the class constructor
	public CnvReader(String row)
	{
		this.fileRow = row;
	}

	public void InsertComment(String s)
	{
		this.Comments = s;
	}

}
