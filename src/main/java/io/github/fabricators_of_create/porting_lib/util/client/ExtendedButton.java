package io.github.fabricators_of_create.porting_lib.util.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import net.minecraft.client.gui.components.Button.OnPress;

/**
 * This class provides a button that fixes several bugs present in the vanilla GuiButton drawing code.
 * The gist of it is that it allows buttons of any size without gaps in the graphics and with the
 * borders drawn properly. It also prevents button text from extending out of the sides of the button by
 * trimming the end of the string and adding an ellipsis.<br/><br/>
 *
 * The code that handles drawing the button is in GuiUtils.
 *
 * @author bspkrs
 */
public class ExtendedButton extends Button {
	public ExtendedButton(int xPos, int yPos, int width, int height, Component displayString, OnPress handler) {
		super(xPos, yPos, width, height, displayString, handler);
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		Minecraft mc = Minecraft.getInstance();
		int k = this.getYImage(this.isHovered);
		GuiUtils.drawContinuousTexturedBox(poseStack, WIDGETS_LOCATION, this.x, this.y, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.getBlitOffset());
		this.renderBg(poseStack, mc, mouseX, mouseY);

		Component buttonText = this.getMessage();
		int strWidth = mc.font.width(buttonText);
		int ellipsisWidth = mc.font.width("...");

		if (strWidth > width - 6 && strWidth > ellipsisWidth)
			//TODO, srg names make it hard to figure out how to append to an ITextProperties from this trim operation, wraping this in StringTextComponent is kinda dirty.
			buttonText = new TextComponent(mc.font.substrByWidth(buttonText, width - 6 - ellipsisWidth).getString() + "...");

		drawCenteredString(poseStack, mc.font, buttonText, this.x + this.width / 2, this.y + (this.height - 8) / 2, this.active ? 16777215 : 10526880);
	}
}
