package com.veyzn.main;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.veyzn.commands.SignEditCMD;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public static Plugin instance;
    @Override
    public void onEnable() {
        instance = this;
        getCommand("signedit").setExecutor(new SignEditCMD());

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(this, PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                BlockPosition pos = event.getPacket().getBlockPositionModifier().read(0);

                String[] lines = event.getPacket().getStringArrays().read(0);

                String numberInput = lines[1];
                try {
                    int number = Integer.parseInt(numberInput);
                    Bukkit.broadcastMessage(player.getName() + " entered the number: " + number);
                } catch (NumberFormatException e) {
                    player.sendMessage("Please enter a valid number on the sign.");
                }

                // Remove the sign block after capturing input
                Location signLocation = new Location(player.getWorld(), pos.getX(), pos.getY(), pos.getZ());
                Block block = signLocation.getBlock();
                if (block.getType() == Material.OAK_SIGN || block.getType() == Material.OAK_WALL_SIGN) {
                    block.setType(Material.AIR);
                }
            }
        });

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
