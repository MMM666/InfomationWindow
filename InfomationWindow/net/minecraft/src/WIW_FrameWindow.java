package net.minecraft.src;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class WIW_FrameWindow extends JFrame implements ChangeListener {
	public JTextArea ftextarea1 = new JTextArea();
	public JTextArea ftextarea2 = new JTextArea();
	public JTextArea ftextarea3 = new JTextArea();
	public JTextArea ftextarea4 = new JTextArea();
	public JTextArea ftextarea5 = new JTextArea();
	public JTextArea ftextarea6 = new JTextArea();
	public JTabbedPane ftab = new JTabbedPane();



	public WIW_FrameWindow() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JScrollPane lscl1 = new JScrollPane(ftextarea1);
		lscl1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JScrollPane lscl2 = new JScrollPane(ftextarea2);
		lscl2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JScrollPane lscl3 = new JScrollPane(ftextarea3);
		lscl3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JScrollPane lscl4 = new JScrollPane(ftextarea4);
		lscl4.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JScrollPane lscl5 = new JScrollPane(ftextarea5);
		lscl5.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JScrollPane lscl6 = new JScrollPane(ftextarea6);
		lscl6.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		ftab.add("EntityID", lscl1);
		ftab.add("ItemID", lscl2);
		ftab.add("BlockID", lscl3);
		ftab.add("Chat", lscl4);
		ftab.add("STDOUT", lscl5);
		ftab.add("Entity", lscl6);
		getContentPane().add(ftab);
		
		setTitle("InfomationWindow");
		setSize(700, 400);
		setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
	}

}

