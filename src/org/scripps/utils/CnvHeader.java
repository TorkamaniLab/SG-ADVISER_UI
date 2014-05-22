package org.scripps.utils;

import java.util.Vector;

public class CnvHeader
{
	public Vector<String> headers; // = new Vector<String>();

	public CnvHeader(String lines)
	{
		this.headers = InitHeader(lines);
		// this.headers.add(h);
	}

	public Vector<String> InitHeader(String lines)
	{
		String[] r = lines.split("\t");
		int size = r.length;
		Vector<String> row = new Vector<String>();
		for (int i = 0; i < size; i++)
		{
			row.add(r[i]);
		}

		return row;
	}

	public Vector<String> getheaders()
	{
		return headers;
	}
}
