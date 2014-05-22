/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scripps.utils;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

/**
 * 
 * @author gerikson
 */
public class CnvSaveProgress implements Runnable
{
	public static JFrame frame;
	public static Container content;
	public static JProgressBar progressBar;
	public static Border border;
	public static int b;

	public CnvSaveProgress()
	{
		frame = new JFrame("Rest of file filtering and saving");
		content = frame.getContentPane();
		progressBar = new JProgressBar();
		border = BorderFactory
				.createTitledBorder("Entire file filtering by column: "
						+ " ...");
		progressBar.setBorder(border);
		content.add(progressBar, BorderLayout.NORTH);
		frame.setSize(300, 100);
		b = 0;
		// frame.setVisible(true);
	}

	public static void progBar()
	{
		frame.setVisible(true);
		b = b + 3;
		progressBar.setValue(b);
		progressBar.setStringPainted(true);
	}

	@Override
	public void run()
	{
		progBar();
	}

}
