package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class mod_WIW_InfomationWindow extends BaseMod {

	class cwindow extends JFrame implements ChangeListener {
		public JTextArea ftextarea1 = new JTextArea();
		public JTextArea ftextarea2 = new JTextArea();
		public JTextArea ftextarea3 = new JTextArea();
		public JTextArea ftextarea4 = new JTextArea();
	
		public cwindow() {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			JScrollPane lscl1 = new JScrollPane(ftextarea1);
			lscl1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			JScrollPane lscl2 = new JScrollPane(ftextarea2);
			lscl2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			JScrollPane lscl3 = new JScrollPane(ftextarea3);
			lscl3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			JScrollPane lscl4 = new JScrollPane(ftextarea4);
			lscl4.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			
			JTabbedPane ltab = new JTabbedPane();
			ltab.add("EntityID", lscl1);
			ltab.add("ItemID", lscl2);
			ltab.add("BlockID", lscl3);
			ltab.add("Chat", lscl4);
			getContentPane().add(ltab);
			
			setTitle("InfomationWindow");
			setSize(700, 400);
			setVisible(true);
		}
	
		@Override
		public void stateChanged(ChangeEvent e) {
		}
	
	}

	private cwindow fwindow;


	@Override
	public String getName() {
		return "InfomationWindow";
	}

	@Override
	public String getVersion() {
		return "1.4.6-1";
	}

	@Override
	public String getPriorities() {
		return "after:*";
	}

	@Override
	public void load() {
	}

	@Override
	public void modsLoaded() {
		// WindowçÏê¨
		fwindow = new cwindow();
		fwindow.setLocation(10, 600);
		
		// EntityIDList
		fwindow.ftextarea1.append("EntityID(Integer), EntityID(Byte), EntityName, ClassName\r\n");
		List<Integer> lentityIds = new ArrayList<Integer>();
		try {
			Map<Integer, Class> lmap = (Map)ModLoader.getPrivateValue(EntityList.class, null, 2);
			lentityIds.addAll(lmap.keySet());
		} catch (Exception e) {
		}
		Collections.sort(lentityIds);
		for (Integer li : lentityIds) {
			fwindow.ftextarea1.append(String.format("% 6d, % 6d, %s, %S\r\n", li, li & 0x00ff, EntityList.getStringFromID(li), EntityList.getClassFromID(li).getName()));
		}
		
		// ItemIDList
		fwindow.ftextarea2.append("ItemID, ItemName, ClassName\r\n");
		for (Item litem : Item.itemsList) {
			if (litem != null) {
				String ls = litem.getItemName();
				if (ls == null) ls = "NULL";
				fwindow.ftextarea2.append(String.format("% 6d, %s, %S\r\n", litem.itemID, ls, litem.getClass().getName()));
			}
		}
		
		// BlockIDList
		fwindow.ftextarea3.append("BlockID, BlockName, ClassName\r\n");
		for (Block lblock : Block.blocksList) {
			if (lblock != null) {
				String ls = lblock.getBlockName();
				if (ls == null) ls = "NULL";
				fwindow.ftextarea3.append(String.format("% 6d, %s, %S\r\n", lblock.blockID, ls, lblock.getClass().getName()));
			}
		}
		
	}

	@Override
	public void serverChat(NetServerHandler var1, String var2) {
		fwindow.ftextarea4.append(var2 + "\r\n");
	}

	@Override
	public void clientChat(String var1) {
		fwindow.ftextarea4.append(var1 + "\r\n");
	}

}
