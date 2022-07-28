package me.maki325.mcmods.portablemusic.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import me.maki325.mcmods.portablemusic.PortableMusic;
import me.maki325.mcmods.portablemusic.client.ClientSoundManager;
import me.maki325.mcmods.portablemusic.common.Utils;
import me.maki325.mcmods.portablemusic.common.network.Network;
import me.maki325.mcmods.portablemusic.common.network.ToggleSoundMessage;
import me.maki325.mcmods.portablemusic.common.sound.Sound;
import me.maki325.mcmods.portablemusic.common.sound.SoundState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Random;

public class BoomboxUI extends Screen {

    public static final String PIGSTEP = "minecraft:music_disc.pigstep";

    private static final int WIDTH = 224;
    private static final int HEIGHT = 134;
    private static final int DISC_SIZE = 102;

    private int soundId;
    private Sound sound;
    private float rotation = 0;
    private Random random = new Random();

    private final ResourceLocation BACKGROUND_TEXTURE =
        new ResourceLocation(PortableMusic.MODID, "textures/gui/boombox-background.png");
    private final ResourceLocation PIGSTEP_TEXTURE =
        new ResourceLocation(PortableMusic.MODID, "textures/discs/boombox-pigstep.png");
    private final ResourceLocation UNKNOWN_SOUND_TEXTURE =
        new ResourceLocation(PortableMusic.MODID, "textures/discs/boombox-none.png");

    private final ResourceLocation ICONS_TEXTURE =
        new ResourceLocation(PortableMusic.MODID, "textures/gui/boombox-icons.png");
    private final Icon PLAY_ICON = new Icon(ICONS_TEXTURE,  0,  0,  16,  16);
    private final Icon PAUSE_ICON = new Icon(ICONS_TEXTURE,  16,  0,  16,  16);
    private final Icon STOP_ICON = new Icon(ICONS_TEXTURE,  32,  0,  16,  16);

    private final IconButton play = new IconButton(0, 0, 20, 20, PLAY_ICON,
        (b) -> syncSoundState(soundId, SoundState.PLAYING),
        (b, poseStack, x, y) ->
            this.renderTooltip(poseStack, Utils.translatable("button.%_%.play"), x, y)
    );
    private final IconButton pause = new IconButton(0, 0, 20, 20, PAUSE_ICON,
        (b) -> syncSoundState(soundId, SoundState.PAUSED),
        (b, poseStack, x, y) ->
            this.renderTooltip(poseStack, Utils.translatable("button.%_%.pause"), x, y)
    );
    private final IconButton stop = new IconButton(0, 0, 20, 20, STOP_ICON,
        (b) -> {
            syncSoundState(soundId, SoundState.STOPPED);
            rotation = 0;
        },
        (b, poseStack, x, y) ->
            this.renderTooltip(poseStack, Utils.translatable("button.%_%.stop"), x, y)
    );

    public BoomboxUI(int soundId) {
        super(Component.translatable("screen." + PortableMusic.MODID + ".boombox"));
        this.soundId = soundId;
        this.sound = ClientSoundManager.getInstance().getSound(soundId);
        if(sound != null && (sound.soundState == SoundState.PLAYING || sound.soundState == SoundState.PAUSED))
            rotation = random.nextInt(180) + random.nextInt(180) + 2;
    }

    @Override public boolean isPauseScreen() {
        return false;
    }

    @Override protected void init() {
        int posX = (this.width - WIDTH) / 2 + 20;
        int posY = (this.height - HEIGHT) / 2 + HEIGHT - 20 - 16;

        play.x = posX + 30 * 0;
        play.y = posY;
        addRenderableWidget(play);

        pause.x = posX + 30 * 1;
        pause.y = posY;
        addRenderableWidget(pause);

        stop.x = posX + 30 * 2;
        stop.y = posY;
        addRenderableWidget(stop);
    }

    @Override public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        int relX = (this.width - WIDTH) / 2;
        int relY = (this.height - HEIGHT) / 2;
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        this.blit(poseStack, relX, relY, 0, 0, WIDTH, HEIGHT);

        this.sound = ClientSoundManager.getInstance().getSound(soundId);
        if(soundId == 0 || sound == null) {
            super.render(poseStack, mouseX, mouseY, partialTicks);
            return;
        }
        if(sound.soundState == SoundState.PLAYING)
            rotation += 0.01;

        play.active = true;
        pause.active = true;
        stop.active = true;

        switch (sound.soundState) {
            case PLAYING -> play.active = false;
            case PAUSED -> pause.active = false;
        }

        RenderSystem.setShaderTexture(0, switch (sound.sound) {
            case PIGSTEP -> PIGSTEP_TEXTURE;
            default -> UNKNOWN_SOUND_TEXTURE;
        });

        poseStack.pushPose();
        poseStack.translate(relX + 164, relY + 66, 0);
        poseStack.mulPose(Vector3f.ZP.rotation(rotation));
        blit(poseStack, -51, -51, 0, 0F, 0F, DISC_SIZE, DISC_SIZE, DISC_SIZE, DISC_SIZE);

        poseStack.popPose();

        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    public void syncSoundState(int soundId, SoundState soundState) {
        Network.CHANNEL.sendToServer(new ToggleSoundMessage(soundId, soundState));
    }

    @Override
    public void onClose() {
        super.onClose();
        if(soundId != 0 && sound != null) {
            ClientSoundManager.getInstance().setSoundState(soundId, sound.soundState);
        }
    }

    public static void open(int soundId) {
        Minecraft.getInstance().setScreen(new BoomboxUI(soundId));
    }
}
