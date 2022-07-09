package me.maki325.mcmods.portablemusic.common.capabilities.boombox;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BoomboxProvider implements ICapabilityProvider {

    public static final Capability<IBoomboxCapability> BOOMBOX_CAPABILITY =
            CapabilityManager.get(new CapabilityToken<>() {});

    private final LazyOptional<IBoomboxCapability> holder = LazyOptional.of(BoomboxCapability::new);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == BOOMBOX_CAPABILITY ? holder.cast() : LazyOptional.empty();
    }

}
