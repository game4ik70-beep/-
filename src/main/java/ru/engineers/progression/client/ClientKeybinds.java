package ru.engineers.progression.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;
import ru.engineers.progression.EngineersProgression;

@EventBusSubscriber(modid = EngineersProgression.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ClientKeybinds {
    public static final KeyMapping OPEN_ARCHIVE = new KeyMapping(
            "key.engineers_progression.open_archive",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            "key.categories.engineers_progression"
    );

    private ClientKeybinds() {}

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(OPEN_ARCHIVE);
    }

    @EventBusSubscriber(modid = EngineersProgression.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
    public static final class Ticks {
        private Ticks() {}

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            Minecraft minecraft = Minecraft.getInstance();
            while (OPEN_ARCHIVE.consumeClick()) {
                if (minecraft.player != null && minecraft.screen == null) {
                    minecraft.setScreen(new EngineeringArchiveScreen());
                }
            }
        }
    }
}
