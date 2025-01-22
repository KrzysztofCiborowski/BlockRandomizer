package net.historynoob.blockrandomizer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Mod(modid = BlockRandomizer.MODID, name = BlockRandomizer.NAME, version = BlockRandomizer.VERSION)
public class BlockRandomizer {

    public static final String MODID = "blockrandomizer";
    public static final String NAME = "Block Randomizer";
    public static final String VERSION = "1.0.0";

    private static KeyBinding toggleKey;
    private static boolean randomizerEnabled = false;
    private static final Random random = new Random();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Register this class for event handling
        MinecraftForge.EVENT_BUS.register(this);
        System.out.println("BlockRandomizer: Pre-initialization complete.");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Initialize key binding for client-side only
        if (event.getSide().isClient()) {
            toggleKey = new KeyBinding("Toggle Block Randomizer", Keyboard.KEY_R, "Block Randomizer");
            ClientRegistry.registerKeyBinding(toggleKey);
            System.out.println("BlockRandomizer: Keybinding registered.");
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getMinecraft().world != null) {
            if (toggleKey != null && toggleKey.isPressed()) {
                randomizerEnabled = !randomizerEnabled;
                EntityPlayer player = Minecraft.getMinecraft().player;
                if (player != null) {
                    player.sendMessage(new TextComponentString(
                            "Block Randomizer: " + (randomizerEnabled ? "Enabled" : "Disabled")
                    ));
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockPlace(PlayerInteractEvent.RightClickBlock event) {
        if (!randomizerEnabled) return;

        EntityPlayer player = event.getEntityPlayer();
        if (player == null || player.world.isRemote) return; // Ensure this runs server-side

        List<Integer> placeableSlots = getPlaceableSlots(player);
        if (!placeableSlots.isEmpty()) {
            int randomSlot = placeableSlots.get(random.nextInt(placeableSlots.size()));
            player.inventory.currentItem = randomSlot;
        }
    }

    private List<Integer> getPlaceableSlots(EntityPlayer player) {
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock) {
                ItemBlock itemBlock = (ItemBlock) stack.getItem();
                if (itemBlock.getBlock() != Blocks.AIR) {
                    slots.add(i);
                }
            }
        }
        return slots;
    }
}
