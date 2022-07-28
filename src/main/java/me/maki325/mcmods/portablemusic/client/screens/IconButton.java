package me.maki325.mcmods.portablemusic.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class IconButton extends Button {
    private Icon icon;

    public IconButton(int x, int y, int width, int height, Icon icon, OnPress onPress) {
        this(x, y, width, height, icon, onPress, NO_TOOLTIP);
    }

    public IconButton(int x, int y, int width, int height, Icon icon, OnPress onPress, OnTooltip onTooltip) {
        super(x, y, width, height, Component.literal(""), onPress, onTooltip);
        this.icon = icon;
    }

    @Override public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.render(poseStack, mouseX, mouseY, partialTicks);
        if(!this.visible) return;
        int x = this.x + this.width / 2 - this.icon.width() / 2;
        int y = this.y + this.height / 2 - this.icon.height() / 2;


        RenderSystem.setShaderTexture(0, this.icon.texture());
        blit(poseStack, x, y, this.icon.x(), this.icon.y(), this.icon.width(), this.icon.height());
    }

}
