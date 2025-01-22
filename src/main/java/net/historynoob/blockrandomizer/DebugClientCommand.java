package net.historynoob.blockrandomizer;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DebugClientCommand implements ICommand {
    private final List<String> aliases;

    public DebugClientCommand() {
        aliases = new ArrayList<>();
        aliases.add("blockrandomizerdebug");
        aliases.add("brd");
    }

    @Override
    public String getName() {
        return "blockrandomizerdebug";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "/blockrandomizerdebug";
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings) throws CommandException {
        BlockRandomizer blockRandomizer = new BlockRandomizer();
        blockRandomizer.changeDebugState();

    }

    @Override
    public boolean checkPermission(MinecraftServer minecraftServer, ICommandSender iCommandSender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer minecraftServer, ICommandSender iCommandSender, String[] strings, @Nullable BlockPos blockPos) {
        return new ArrayList<>();
    }

    @Override
    public boolean isUsernameIndex(String[] strings, int i) {
        return false;
    }


    @Override
    public int compareTo(ICommand o) {
        return this.getName().compareTo(o.getName());
    }
}
