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

        // Open the fake sign with the first line pre-filled with "Hello World"
        openSignEditor(player, "Hello World");

        return false;
    }

    private void openSignEditor(Player player, String line1Text) {
        // Place a real sign at a nearby location
        Location tempLocation = player.getLocation().clone().add(0, 1, 0);
        Block block = tempLocation.getBlock();
        block.setType(Material.OAK_SIGN);

        // Open the sign editor for the player after placing the sign
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PacketContainer openSignPacket = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
                    BlockPosition signPosition = new BlockPosition(tempLocation.getBlockX(), tempLocation.getBlockY(), tempLocation.getBlockZ());
                    openSignPacket.getBlockPositionModifier().write(0, signPosition);
                    block.setType(Material.AIR);
                    // Send the packet to open the sign editor
                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, openSignPacket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskLater(Main.instance, 1L); // Small delay to ensure block entity registration
    }
}
