package me.xTDKx.DP;

import net.minecraft.server.v1_7_R3.EntityPlayer;
import net.minecraft.server.v1_7_R3.PlayerInteractManager;
import net.minecraft.server.v1_7_R3.PlayerList;
import net.minecraft.server.v1_7_R3.WorldServer;
import net.minecraft.util.com.google.common.base.Charsets;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import org.bukkit.craftbukkit.v1_7_R3.CraftServer;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;


public class DummyPlayers extends JavaPlugin implements Listener {
    private TPSCheck tpsCheck = new TPSCheck();
    public Set<String> dummies = new HashSet<String>();

    @Override
    public void onEnable() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, tpsCheck, 20, 20);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        String prefix = (ChatColor.DARK_GRAY + "["+ChatColor.YELLOW+"DummyPlayers"+ChatColor.DARK_GRAY + "] " + ChatColor.GRAY);
        if(commandLabel.equalsIgnoreCase("dp")){
            if(args.length == 0){
                sender.sendMessage(ChatColor.DARK_GRAY + "["+ChatColor.YELLOW+"DummyPlayers"+ChatColor.DARK_GRAY + "]" + ChatColor.GRAY+":");
                sender.sendMessage(ChatColor.YELLOW+"/dp spawn [name] [amount] [location] "+ChatColor.GRAY+"- Spawn dummy players.");
                sender.sendMessage(ChatColor.YELLOW+"/dp kill <all|name> "+ChatColor.GRAY+"- Kill dummy players");
                sender.sendMessage(ChatColor.YELLOW+"/dp setSkin <all|name> <skin> "+ChatColor.GRAY+"- Set the skin of dummy players");
                sender.sendMessage(ChatColor.YELLOW+"/dp command <all|name> <command> "+ChatColor.GRAY+"- Execute a command as a dummy player.");
                sender.sendMessage(ChatColor.YELLOW+"/dp chat <all|name> <chat> "+ChatColor.GRAY+"- Chat as a dummy player");
            }
            else if (args.length == 1){
                if(args[0].equalsIgnoreCase("spawn")) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        Random random = new Random();
                        String name = ChatColor.YELLOW + "Dummy" + random.nextInt(1000) + 1;
                        WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
                        PlayerList playerList = ((CraftServer) Bukkit.getServer()).getHandle();
                        UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8));
                        GameProfile gameProfile = new GameProfile(UUID.fromString(uuid.toString().replaceAll("-", "")), name);

                        EntityPlayer entityPlayer = new EntityPlayer(playerList.getServer(), world, gameProfile, new PlayerInteractManager(world));
                        new DummyConnection(playerList.getServer(), new DummyNetwork(), entityPlayer);

                        entityPlayer.spawnIn(world);
                        entityPlayer.playerInteractManager.a((WorldServer) entityPlayer.world);
                        entityPlayer.playerInteractManager.b(world.getWorldData().getGameType());

                        entityPlayer.setPosition(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
                        playerList.players.add(entityPlayer);
                        world.addEntity(entityPlayer);
                        playerList.a(entityPlayer, null);
                        dummies.add(name);

                        p.sendMessage(prefix+"Dummy player "+name+ChatColor.GRAY+" spawned at your location.");
                    }else{
                        sender.sendMessage(prefix+"Only players can use this command.");
                    }
                }
            }
        }

        /*if (command.getName().equalsIgnoreCase("spawnbot")) {
            int range = 2000;
            int num = 1;
            if (args.length > 0) {
                num = Integer.parseInt(args[0]);
            }
            if (args.length > 1) {
                range = Integer.parseInt(args[1]);
            }

            for (int i = 0; i < num; i++) {
                Random random = new Random();
                String name = ChatColor.BLUE + "Bot" + random.nextInt(1000) + i;
                WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
                PlayerList playerList = ((CraftServer) Bukkit.getServer()).getHandle();
                UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8));
                GameProfile gameProfile = new GameProfile(uuid, name);

                EntityPlayer entityplayer = new EntityPlayer(playerList.getServer(), world, gameProfile, new PlayerInteractManager(world));
                new DummyConnection(playerList.getServer(), new DummyNetwork(), entityplayer);

                entityplayer.spawnIn(world);
                entityplayer.playerInteractManager.a((WorldServer) entityplayer.world);
                entityplayer.playerInteractManager.b(world.getWorldData().getGameType());

                entityplayer.setPosition(random.nextInt(range * 2) - range, 100, random.nextInt(range * 2) - range);

                playerList.players.add(entityplayer);
                world.addEntity(entityplayer);
                playerList.a(entityplayer, null);

                sender.sendMessage("Added player " + entityplayer.getName() + ChatColor.RESET + " at " + entityplayer.locX + ", " + entityplayer.locY + ", " + entityplayer.locZ + ".");
            }

            return true;
        }

        if (command.getName().equalsIgnoreCase("killbots")) {
            PlayerList playerList = ((CraftServer) Bukkit.getServer()).getHandle();
            for (EntityPlayer entityplayer : (CopyOnWriteArrayList<EntityPlayer>) playerList.players) {
                if (entityplayer.getName().startsWith(ChatColor.BLUE + "Bot")) {
                    entityplayer.playerConnection.disconnect("");
                    sender.sendMessage("Disconnected " + entityplayer.getName());
                }
            }
        }

        if (command.getName().equalsIgnoreCase("debug")) {
            toggle = !toggle;
            float tps = 0;
            for (Long l : tpsCheck.history) {
                if (l != null)
                    tps += 20 / (l / 1000);
            }
            tps = tps / tpsCheck.history.size();

            sender.sendMessage("TPS: " + tps + " Loaded chunks: " + Bukkit.getWorlds().get(0).getLoadedChunks().length + " Entities: " + Bukkit.getWorlds().get(0).getEntities().size());
        }*/
        return false;
    }
}
