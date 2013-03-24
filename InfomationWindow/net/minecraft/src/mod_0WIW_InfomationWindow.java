package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class mod_0WIW_InfomationWindow extends BaseMod {

	@MLProp
	public static int posX = 10;
	@MLProp
	public static int posY = 600;

	private static WIW_FrameWindow fwindow;
	static {
		// WindowçÏê¨
		fwindow = new WIW_FrameWindow();
		new WIW_SystemOut(fwindow.ftextarea5);
	}

	@Override
	public String getName() {
		return "InfomationWindow";
	}

	@Override
	public String getVersion() {
		return "1.5.1-1";
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
		fwindow.setLocation(posX, posY);
		
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
				String ls = litem.getUnlocalizedName();
				if (ls == null) ls = "NULL";
				fwindow.ftextarea2.append(String.format("% 6d, %s, %S\r\n", litem.itemID, ls, litem.getClass().getName()));
			}
		}
		
		// BlockIDList
		fwindow.ftextarea3.append("BlockID, BlockName, ClassName\r\n");
		for (Block lblock : Block.blocksList) {
			if (lblock != null) {
				String ls = lblock.getUnlocalizedName();
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
