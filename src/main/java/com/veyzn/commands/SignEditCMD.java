package com.veyzn.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.veyzn.main.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;


public class SignEditCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        Player player = (Player) sender;

        openSignEditor(player, "Hello World");

        return false;
    }

    private void openSignEditor(Player player, String line1Text) {
        Location tempLocation = player.getLocation().clone().add(0, 1, 0);
        Block block = tempLocation.getBlock();
        block.setType(Material.OAK_SIGN);

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PacketContainer openSignPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
                    BlockPosition signPosition = new BlockPosition(tempLocation.getBlockX(), tempLocation.getBlockY(), tempLocation.getBlockZ());
                    openSignPacket.getBlockPositionModifier().write(0, signPosition);
                    block.setType(Material.AIR);
                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, openSignPacket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskLater(Main.instance, 1L);
    }
}
