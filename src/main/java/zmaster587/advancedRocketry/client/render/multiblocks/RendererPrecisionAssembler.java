package zmaster587.advancedRocketry.client.render.multiblocks;

import java.util.List;

import org.lwjgl.opengl.GL11;

import zmaster587.advancedRocketry.backwardCompat.ModelFormatException;
import zmaster587.advancedRocketry.backwardCompat.WavefrontObject;
import zmaster587.libVulpes.block.RotatableBlock;
import zmaster587.libVulpes.render.RenderHelper;
import zmaster587.libVulpes.tile.multiblock.TileMultiblockMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class RendererPrecisionAssembler extends TileEntitySpecialRenderer {
	WavefrontObject model;

	ResourceLocation texture = new ResourceLocation("advancedrocketry:textures/models/precAssembler.png");

	//private final RenderItem dummyItem = Minecraft.getMinecraft().getRenderItem();
	
	//Model Names:
	// Tray
	// Hull
	// ProcessA
	// ProcessB
	// ProcessC
	
	public RendererPrecisionAssembler() {
		try {
			model = new WavefrontObject(new ResourceLocation("advancedrocketry:models/precAssembler.obj"));
		} catch (ModelFormatException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x,
			double y, double z, float f, int damage) {

		TileMultiblockMachine multiBlockTile = (TileMultiblockMachine)tile;

		if(!multiBlockTile.canRender())
			return;
		
		GL11.glPushMatrix();

		//Rotate and move the model into position
		GL11.glTranslated(x+.5f, y, z + .5f);
		EnumFacing front = RotatableBlock.getFront(tile.getWorld().getBlockState(tile.getPos())); //tile.getWorldObj().getBlockMetadata(tile.xCoord, tile.yCoord, tile.zCoord));
		GL11.glRotatef((front.getFrontOffsetX() == 1 ? 180 : 0) + front.getFrontOffsetZ()*90f, 0, 1, 0);
		GL11.glTranslated(-.5f, 0, -.5f);
		
		if(multiBlockTile.isRunning()) {

			float progress = multiBlockTile.getProgress(0)/(float)multiBlockTile.getTotalProgress(0);
			float process,tray;
			tray = process = 3*progress;



			List<ItemStack> outputList = multiBlockTile.getOutputs();
			if(outputList != null && !outputList.isEmpty()) {
				ItemStack stack = outputList.get(0);
				EntityItem entity = new EntityItem(tile.getWorld());
				
				entity.setEntityItemStack(stack);
				entity.hoverStart = 0;
				
				GL11.glPushMatrix();
				GL11.glRotatef(90, 1, 0, 0);
				GL11.glTranslated(1, tray + .5, -1.2f);
				RenderHelper.renderItem(multiBlockTile, entity,  Minecraft.getMinecraft().getRenderItem());
				GL11.glPopMatrix();
			}
			
			bindTexture(texture);
			model.renderPart("Hull");
			
			GL11.glTranslated(0, 0, tray);
			model.renderPart("Tray"); // 0-> 3
			GL11.glTranslated(0, 0, -tray);
			
			process *= 6;
			if(process > 2 && process < 4){
				if(process < 3) {
					process-=2;
					GL11.glTranslatef(0, -.25f*process, 0); // 0 -> -.25
					model.renderPart("ProcessA");
					GL11.glTranslatef(0, .25f*process, 0);
				}
				else if(process < 4) {
					process = -process + 4;
					GL11.glTranslatef(0, -.25f*process, 0); // 0 -> -.25
					model.renderPart("ProcessA");
					GL11.glTranslatef(0, .25f*process, 0);
				}
			}
			else
				model.renderPart("ProcessA");
			
			
			
			process -= 6;
			if(process > 2 && process < 4){
				if(process < 3) {
					process-=2;
					GL11.glTranslatef(0, -.25f*process, 0); // 0 -> -.25
					model.renderPart("ProcessB");
					GL11.glTranslatef(0, .25f*process, 0);
				}
				else if(process < 4) {
					process = -process + 4;
					GL11.glTranslatef(0, -.25f*process, 0); // 0 -> -.25
					model.renderPart("ProcessB");
					GL11.glTranslatef(0, .25f*process, 0);
				}
			}
			else
				model.renderPart("ProcessB");
			
			process -= 6;
			if(process > 1 && process < 3){
				if(process < 2) {
					process-=1;
					
					GL11.glTranslated(1.55, 1.47, 0);
					GL11.glRotatef(90*process, 0, 0, 1);
					GL11.glTranslated(-1.55, -1.47, 0);
					
					model.renderPart("ProcessC");
				}
				else if(process < 3) {
					process = -process + 3;
					
					GL11.glTranslated(1.55, 1.47, 0);
					GL11.glRotatef(90*process, 0, 0, 1);
					GL11.glTranslated(-1.55, -1.47, 0);
					
					model.renderPart("ProcessC");
				}
			}
			else
				model.renderPart("ProcessC");



			
		}
		else {
			bindTexture(texture);
			model.renderAll();
		}
		
		
		GL11.glPopMatrix();

	}

}
