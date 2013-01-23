package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;

public class WIW_SystemOut extends PrintStream {

	private JTextArea outputArea;
	
	public WIW_SystemOut(JTextArea pText) {
		super(System.out);
		System.setOut(this);
		outputArea = pText;
	}

	@Override
	public void write(byte[] buf, int off, int len) {
		// TODO Auto-generated method stub
		super.write(buf, off, len);
		ByteArrayOutputStream lb = new ByteArrayOutputStream();
		lb.write(buf, off, len);
		outputArea.append(lb.toString());
	}

	@Override
	public void write(int b) {
		// TODO Auto-generated method stub
		super.write(b);
	}

}
