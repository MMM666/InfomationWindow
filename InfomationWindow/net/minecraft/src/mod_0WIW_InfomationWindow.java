package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.server.MinecraftServer;

public class mod_0WIW_InfomationWindow extends BaseMod {

	@MLProp
	public static int posX = 10;
	@MLProp
	public static int posY = 580;
	@MLProp
	public static int sizeX = 700;
	@MLProp
	public static int sizeY = 500;

	public static Entity targetSV;
	public static Entity targetCL;
	public static boolean isHold = false;



	private static WIW_FrameWindow fwindow;
	static {
		// Window作成
		fwindow = new WIW_FrameWindow();
		new WIW_SystemOut(fwindow.ftextarea5);
	}

	@Override
	public String getName() {
		return "InfomationWindow";
	}

	@Override
	public String getPriorities() {
		return "after:*";
	}

	@Override
	public String getVersion() {
		return "1.6.2-1";
	}

	@Override
	public void load() {
		// MMMLibのRevisionチェック
		MMM_Helper.checkRevision("1");
		
		// GUI を開くキーの登録と名称変換テーブルの登録
		String s = "key.Lockon";
		ModLoader.registerKey(this, new KeyBinding(s, 157), false);
		ModLoader.addLocalization(
				(new StringBuilder()).append(s).toString(),
				(new StringBuilder()).append("Lock ON").toString()
				);
		ModLoader.addLocalization(
				(new StringBuilder()).append(s).toString(),
				"ja_JP",
				(new StringBuilder()).append("ロックオン").toString()
				);
		ModLoader.setInGameHook(this, true, false);
		
		fwindow.setSize(sizeX, sizeY);
	}

	@Override
	public void modsLoaded() {
		// Window作成
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
	public void keyboardEvent(KeyBinding keybinding) {
		// LevelUp
		Minecraft mcGame = Minecraft.getMinecraft();
		if (mcGame.theWorld != null && mcGame.currentScreen == null) {
			if (keybinding.keyDescription.endsWith(".Lockon")) {
				isHold = !isHold;
				fwindow.ftab.setSelectedIndex(5);
			}
		}
	}

	@Override
	public boolean onTickInGame(float f, Minecraft minecraft) {
		if (minecraft.isIntegratedServerRunning() && (minecraft.currentScreen == null || minecraft.currentScreen instanceof GuiChat)) {
			FontRenderer lfont = minecraft.fontRenderer;
			List<String> llines = new ArrayList<String>();
			if (targetSV == null || targetSV.isDead) {
				isHold = false;
				targetSV = null;
				targetCL = null;
			}
			if (!isHold) {
				if (minecraft.objectMouseOver != null 
						&& minecraft.objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY
						&& minecraft.objectMouseOver.entityHit != null) {
					Entity lentity = minecraft.objectMouseOver.entityHit;
					targetSV = MinecraftServer.getServer().worldServers[0].getEntityByID(lentity.entityId);
					targetCL = lentity;
				} else {
					targetSV = null;
					targetCL = null;
				}
			}
			// メッセージの作成
			if (targetSV != null) {
				String ls;
				Class lclass;
				llines.add(String.format("%s (%d)%s", targetSV.getClass().getSimpleName(), targetSV.entityId, isHold ? " : Locked" : ""));
				llines.add(String.format("%s", targetSV.worldObj.getClass().getSimpleName()));
				llines.add(String.format("Range=%6f(%f)", minecraft.thePlayer.getDistanceToEntity(targetSV), targetSV.width));
				llines.add(String.format("Pos SV: %f, %f, %f / CL: %f, %f, %f", targetSV.posX, targetSV.posY, targetSV.posZ, targetCL.posX, targetCL.posY, targetCL.posZ));
				llines.add(String.format("lastPos SV: %f, %f, %f / CL: %f, %f, %f", targetSV.lastTickPosX, targetSV.lastTickPosY, targetSV.lastTickPosZ, targetCL.lastTickPosX, targetCL.lastTickPosY, targetCL.lastTickPosZ));
				llines.add(String.format("prevPos SV: %f, %f, %f / CL: %f, %f, %f", targetSV.prevPosX, targetSV.prevPosY, targetSV.prevPosZ, targetCL.prevPosX, targetCL.prevPosY, targetCL.prevPosZ));
				llines.add(String.format("Motion SV: %f, %f, %f / CL: %f, %f, %f", targetSV.motionX, targetSV.motionY, targetSV.motionZ, targetCL.motionX, targetCL.motionY, targetCL.motionZ));
				llines.add(String.format("Size w-h SV: %f-%f / CL: %f - %f", targetSV.width, targetSV.height, targetCL.width, targetCL.height));
				if (targetSV instanceof EntityLivingBase) {
					EntityLivingBase lentitys = (EntityLivingBase)targetSV;
					EntityLivingBase lentityc = (EntityLivingBase)targetSV;
					llines.add(String.format("Health=%f / %f", lentitys.func_110143_aJ(), lentitys.func_110138_aP()));
					llines.add(String.format("Age SV: %d / CL: %d", lentitys.entityAge, lentityc.entityAge));
					AttributeInstance latts = lentitys.func_110148_a(SharedMonsterAttributes.field_111263_d);
					AttributeInstance lattc = lentityc.func_110148_a(SharedMonsterAttributes.field_111263_d);
					llines.add(String.format("Speed SV: %f / CL: %f",
							latts == null ? 0F : latts.func_111126_e(),
							lattc == null ? 0F : lattc.func_111126_e()
							));
					latts = lentitys.func_110148_a(SharedMonsterAttributes.field_111264_e);
					lattc = lentityc.func_110148_a(SharedMonsterAttributes.field_111264_e);
					llines.add(String.format("Attack SV: %f / CL: %f",
							latts == null ? 0F : latts.func_111126_e(),
							lattc == null ? 0F : lattc.func_111126_e()
							));
					
				}
				if (targetSV instanceof EntityAgeable) {
					llines.add(String.format("GrowingAge=%d", ((EntityAgeable)targetSV).getGrowingAge()));
				}
				llines.add(String.format("onGround:%b, InWater:%b(%d)", targetSV.onGround, targetSV.inWater, targetSV.getAir()));
				ls = "EIT_EntityChicken";
				if (targetSV.getClass().getSimpleName().equals(ls)) {
					lclass = MMM_Helper.getNameOfClass(ls);
					try {
						llines.add(String.format("Frontal=%b/%b", (Boolean)lclass.getMethod("isFullFrontal", new Class[] {}).invoke(targetSV, new Object[] {}), (Boolean)lclass.getField("fFullFrontal").get(targetSV)));
						llines.add(String.format("HPMax=%b", (Integer)lclass.getMethod("isHPMax", new Class[] {}).invoke(targetSV, new Object[] {})));
					} catch (Exception e) {
					}
				}
				if (targetSV instanceof EntityChicken) {
					llines.add(String.format("NextEgg=%d", ((EntityChicken)targetSV).timeUntilNextEgg));
				}
				if (targetSV instanceof EntityLiving) {
					Entity lentity;
					EntityLiving lliving = (EntityLiving)targetSV;
					lentity = lliving.getAttackTarget();
					if (lentity != null) {
						llines.add(String.format("AttackTarget=%S(Alive:%b/Dead:%b)", lentity.getClass().getSimpleName(), lentity.isEntityAlive(), lentity.isDead));
					} else {
						llines.add("AttackTarget=NoTarget");
					}
					lentity = lliving.getAITarget();
					if (lentity != null) {
						llines.add(String.format("AITarget=%S(Alive:%b/Dead:%b)", lentity.getClass().getSimpleName(), lentity.isEntityAlive(), lentity.isDead));
					} else {
						llines.add("AITarget=NoTarget");
					}
					llines.add(String.format("DirSV YawBody  =%f/ %f/ %f", lliving.rotationYaw, lliving.prevRotationYaw, lliving.newRotationYaw));
					llines.add(String.format("DirSV YawOffset=%f/ %f", lliving.renderYawOffset, lliving.prevRenderYawOffset));
					llines.add(String.format("DirSV YawHead  =%f/ %f", lliving.rotationYawHead, lliving.prevRotationYawHead));
					lliving = (EntityLiving)targetCL;
					llines.add(String.format("DirCL YawBody  =%f/ %f/ %f", lliving.rotationYaw, lliving.prevRotationYaw, lliving.newRotationYaw));
					llines.add(String.format("DirCL YawOffset=%f/ %f", lliving.renderYawOffset, lliving.prevRenderYawOffset));
					llines.add(String.format("DirCL YawHead  =%f/ %f", lliving.rotationYawHead, lliving.prevRotationYawHead));
				}
				if (targetSV instanceof EntityCreature) {
					Entity lentity;
					lentity = ((EntityCreature)targetSV).getEntityToAttack();
					if (lentity != null) {
						llines.add(String.format("entityToAttack=%S(Alive:%b/Dead:%b)", lentity.getClass().getSimpleName(), lentity.isEntityAlive(), lentity.isDead));
					} else {
						llines.add("entityToAttack=NoTarget");
					}
				}
				ls = "LMM_EntityLittleMaid";
				if (targetSV.getClass().getSimpleName().equals(ls)) {
					lclass = MMM_Helper.getNameOfClass(ls);
					try {
						llines.add(String.format("C-Limit=%d(%f)", (Integer)lclass.getDeclaredField("maidContractLimit").get(targetSV), (Float)lclass.getMethod("getContractLimitDays").invoke(targetSV)));
						MMM_TextureData ltexds = (MMM_TextureData)lclass.getDeclaredField("textureData").get(targetSV);
						MMM_TextureData ltexdc = (MMM_TextureData)lclass.getDeclaredField("textureData").get(targetCL);
						llines.add(String.format("TextureSV=%s(%x), %s(%x), %x",
								ltexds.textureBox[0].textureName, ltexds.textureIndex[0],
								ltexds.textureBox[1].textureName, ltexds.textureIndex[1],
								ltexds.color
								));
						llines.add(String.format("TextureCL=%s(%x), %s(%x), %x",
								ltexdc.textureBox[0].textureName, ltexdc.textureIndex[0],
								ltexdc.textureBox[1].textureName, ltexdc.textureIndex[1],
								ltexdc.color
								));
						Object lo;
						lo = (Object)lclass.getDeclaredField("maidOverDriveTime").get(targetSV);
						int locs = (Integer)lo.getClass().getDeclaredField("fCounter").get(lo);
						lo = (Object)lclass.getDeclaredField("maidOverDriveTime").get(targetCL);
						int locc = (Integer)lo.getClass().getDeclaredField("fCounter").get(lo);
						llines.add(String.format("TNT-D=sv:%d / cl:%d", locs, locc));
						llines.add(String.format("OpenInventory= sv:%b / cl:%b",
								(Boolean)lclass.getDeclaredField("mstatOpenInventory").get(targetSV),
								(Boolean)lclass.getDeclaredField("mstatOpenInventory").get(targetCL)
								));
						llines.add(String.format("ModeSV: %04x / PlayRole:%04x",
								(Integer)lclass.getDeclaredField("maidMode").get(targetSV),
								(Integer)lclass.getDeclaredField("mstatPlayingRole").get(targetSV)));
						int lssda = (Integer)lclass.getDeclaredField("maidDominantArm").get(targetSV);
						Object lssd = ((Object[])lclass.getDeclaredField("mstatSwingStatus").get(targetSV))[lssda];
						Object lcsd = ((Object[])lclass.getDeclaredField("mstatSwingStatus").get(targetCL))[lssda];
						Class lclass2 = MMM_Helper.getNameOfClass("LMM_SwingStatus");
						llines.add(String.format("DominantArm: %d, ItemsSV: %b-%08d / ItemsCL: %b-%08d",
								lssda,
								lclass2.getDeclaredField("itemInUse").get(lssd) != null,
								(Integer)lclass2.getDeclaredField("itemInUseCount").get(lssd),
								lclass2.getDeclaredField("itemInUse").get(lcsd) != null,
								(Integer)lclass2.getDeclaredField("itemInUseCount").get(lcsd)));
					} catch (Exception e) {
					}
				}
				ls = "VZN_EntityZabuton";
				if (targetSV.getClass().getSimpleName().equals(ls)) {
					lclass = MMM_Helper.getNameOfClass(ls);
					try {
						llines.add(String.format("PosZabuton: %f, %f, %f, %f, %f - %d",
								(Double)lclass.getDeclaredField("zabutonX").get(targetSV),
								(Double)lclass.getDeclaredField("zabutonY").get(targetSV),
								(Double)lclass.getDeclaredField("zabutonZ").get(targetSV),
								(Double)lclass.getDeclaredField("zabutonYaw").get(targetSV),
								(Double)lclass.getDeclaredField("zabutonPitch").get(targetSV),
								(Integer)lclass.getDeclaredField("boatPosRotationIncrements").get(targetSV)
							));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				
				if (targetSV.riddenByEntity != null || targetSV.ridingEntity != null) {
					llines.add(String.format("RideSV %s(%d : %f) / %s(%d : %f)", 
							targetSV.ridingEntity == null ? "NULL" : targetSV.ridingEntity.getClass().getSimpleName(),
							targetSV.ridingEntity == null ? 0 : targetSV.ridingEntity.entityId,
							targetSV.ridingEntity == null ? 0 : targetSV.ridingEntity.yOffset,
							targetSV.riddenByEntity == null ? "NULL" : targetSV.riddenByEntity.getClass().getSimpleName(),
							targetSV.riddenByEntity == null ? 0 : targetSV.riddenByEntity.entityId,
							targetSV.riddenByEntity == null ? 0 : targetSV.riddenByEntity.yOffset
					));
				}
				if (targetCL.riddenByEntity != null || targetCL.ridingEntity != null) {
					llines.add(String.format("RideCL %s(%d : %f) / %s(%d : %f)", 
							targetCL.ridingEntity == null ? "NULL" : targetCL.ridingEntity.getClass().getSimpleName(),
							targetCL.ridingEntity == null ? 0 : targetCL.ridingEntity.entityId,
							targetCL.ridingEntity == null ? 0 : targetCL.ridingEntity.yOffset,
							targetCL.riddenByEntity == null ? "NULL" : targetCL.riddenByEntity.getClass().getSimpleName(),
							targetCL.riddenByEntity == null ? 0 : targetCL.riddenByEntity.entityId,
							targetCL.riddenByEntity == null ? 0 : targetCL.riddenByEntity.yOffset
					));
				}
				if (targetSV instanceof EntityLiving) {
					EntityLiving lel = (EntityLiving)targetSV;
					if (lel.isAIEnabled()) {
						List llist = getEcecutingTasks(lel.tasks);
						if (llist != null) {
							llines.add("Tasks:" + llist.size());
							for (Object lo : llist) {
								EntityAITaskEntry lte = (EntityAITaskEntry)lo;
								llines.add(String.format("%4d : %s", lte.priority, lte.action.getClass().getSimpleName()));
							}
						}
						llist = getEcecutingTasks(lel.targetTasks);
						if (llist != null) {
							llines.add("targetTasks:" + llist.size());
							for (Object lo : llist) {
								EntityAITaskEntry lte = (EntityAITaskEntry)lo;
								llines.add(String.format("%4d : %s", lte.priority, lte.action.getClass().getSimpleName()));
							}
						}
					}
				}
				
			} else {
				llines.add("NOTARGET");
			}
			// Draw
			StringBuilder lsb = new StringBuilder();
			for (String ls : llines) {
				lsb.append(ls);
				lsb.append("\r\n");
			}
			fwindow.ftextarea6.setText(lsb.toString());
			llines.clear();
		}

		return true;
	}

	private List getEcecutingTasks(EntityAITasks ptasks) {
		List llist = null;
		try {
			llist = new ArrayList((List)ModLoader.getPrivateValue(EntityAITasks.class, ptasks, 1));
		} catch (Exception e) {
		}
		return llist;
	}

	@Override
	public void serverChat(NetServerHandler var1, String var2) {
		fwindow.ftextarea4.append(var2 + "\r\n");
	}

	@Override
	public void clientChat(String var1) {
		fwindow.ftextarea4.append(var1 + "\r\n");
	}

	@Override
	public void clientDisconnect(NetClientHandler var1) {
		targetSV = null;
		targetCL = null;
	}

	@Override
	public void clientConnect(NetClientHandler var1) {
		targetSV = null;
		targetCL = null;
	}



}
