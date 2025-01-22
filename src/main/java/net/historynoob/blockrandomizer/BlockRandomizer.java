package net.historynoob.blockrandomizer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.util.text.TextComponentString;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Mod(modid = BlockRandomizer.MODID, name = BlockRandomizer.NAME, version = BlockRandomizer.VERSION)
@SideOnly(Side.CLIENT)
public class BlockRandomizer {

    public static final String MODID = "blockrandomizer";
    public static final String NAME = "Block Randomizer";
    public static final String VERSION = "0.0.6";

    private static KeyBinding toggleKey;
    private static boolean randomizerEnabled = false;
    private static final Random random = new Random();
    private static Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Register this class for event handling
        MinecraftForge.EVENT_BUS.register(this);
        System.out.println("BlockRandomizer: Pre-initialization complete.");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
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
/*
    @SubscribeEvent
    public void onBlockPlace(PlayerInteractEvent.RightClickBlock event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player == null) return;

        // Send a debug message to the player
        player.sendMessage(new TextComponentString("onBlockPlace event triggered"));

        // Check if running on the client side
        if (!event.getWorld().isRemote) {
            player.sendMessage(new TextComponentString("onBlockPlace is running on the server side"));
            return;
        }
        player.sendMessage(new TextComponentString("onBlockPlace is running on the client side"));

        // Retrieve placeable slots
        List<Integer> placeableSlots = getPlaceableSlots(player);
        if (!placeableSlots.isEmpty()) {
            int randomSlot = placeableSlots.get(random.nextInt(placeableSlots.size()));
            player.inventory.currentItem = randomSlot;

            // Notify the player about the selected random slot
            player.sendMessage(new TextComponentString("Random slot selected: " + randomSlot));

            // Simulate keypress for the random slot
            simulateKeyPress(randomSlot);
        } else {
            player.sendMessage(new TextComponentString("No placeable slots found."));
        }
    }
*/
    @SubscribeEvent
    public void onBlockPlace(MouseEvent event) {

        Minecraft mc = Minecraft.getMinecraft();
        if (randomizerEnabled && mc.player != null && event.getButton() == 1 && !event.isButtonstate()) {
            EntityPlayer player = mc.player;
            RayTraceResult result = mc.objectMouseOver;

            if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                List<Integer> placeableSlots = getPlaceableSlots(player);
                if (!placeableSlots.isEmpty()) {
                    int randomSlot = placeableSlots.get(random.nextInt(placeableSlots.size()));
                    player.inventory.currentItem = randomSlot;

                    // Notify the player about the selected random slot
                    player.sendMessage(new TextComponentString("Random slot selected: " + randomSlot));

                    // Simulate keypress for the random slot
                    simulateKeyPress(randomSlot);
            }
        }
    }}

    public List<Integer> getPlaceableSlots(EntityPlayer player) {
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            System.out.println(stack);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock) {
                ItemBlock itemBlock = (ItemBlock) stack.getItem();
                if (itemBlock.getBlock() != Blocks.AIR) {
                    slots.add(i);
                }
            }
        }
        EntityPlayer playerdebug = Minecraft.getMinecraft().player;
        if (playerdebug != null) {
            playerdebug.sendMessage(new TextComponentString(
                    "Block Randomizer DEBUG: " + slots
            ));
        }

        return slots;
    }

    private void simulateKeyPress(int slot) {
        int keyCode = slot + 1;
        int hexKeyCode = 0x30 + keyCode;

        try {
            robot.keyPress(hexKeyCode);
            robot.keyRelease(hexKeyCode);
            EntityPlayer playerdebug = Minecraft.getMinecraft().player;
            if (playerdebug != null) {
                playerdebug.sendMessage(new TextComponentString(
                        "Block Randomizer DEBUG: " + hexKeyCode + "pressed"
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

