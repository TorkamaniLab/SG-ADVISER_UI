/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scripps.utils;

/**
 * 
 * @author gerikson
 */
public class CnvCompoundHeteroObj
{

	public int parentOne = 0;
	public int parentTwo = 0;
	public String line = "";
	public int printed = 0;
	public String end_allele = "";
	public String gene = "";
	public int childTwo = 0;
	public String childTwoAffect = "";
	public int childThree = 0;
	public String childThreeAffect = "";

	public CnvCompoundHeteroObj(int pOne, int pTwo, String lin, String end_all,
			String g, int gFour, int gFive, String fourAffect, String fiveAffect)
	{
		parentOne = pOne;
		parentTwo = pTwo;
		line = lin;
		printed = 0;
		end_allele = end_all;
		gene = g;
		childTwo = gFour;
		childThree = gFive;
		childTwoAffect = fourAffect;
		childThreeAffect = fiveAffect;
	}

	public int getOne()
	{
		return parentOne;
	}

	public int getTwo()
	{
		return parentTwo;
	}

	public String getLine()
	{
		return line;
	}

	public void linePreviouslyPrinted()
	{
		printed = 1;
	}
}
