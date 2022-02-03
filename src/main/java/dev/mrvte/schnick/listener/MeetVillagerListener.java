package dev.mrvte.schnick.listener;

import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MeetVillagerListener implements Listener {

    List<ItemStack> playerItems = new ArrayList<>(9);

    List<Material> gameItems = Arrays.asList(Material.SHEARS, Material.STONE, Material.PAPER);
    List<String> gameItemsNames = Arrays.asList("Schere", "Stein", "Papier");

    Boolean gameStarted = false;
    Boolean timeout = false;
    Timer timeoutTimer = new Timer("GameTimeout");

    TimerTask timeoutTask = new TimerTask() {
        @Override
        public void run() {
            resetTimeout();
        }
    };

    private synchronized void resetTimeout() {
        ConsoleCommandSender ccs = Bukkit.getConsoleSender();
        ccs.sendMessage("resetTimeout");
        this.timeout = false;
        this.timeoutTimer.cancel();
    }


    @EventHandler
    public void meetVillager(PlayerInteractEntityEvent e) {

        if (e.getRightClicked() instanceof Villager) {
            e.setCancelled(true);

            Villager v = (Villager) e.getRightClicked();
            Player p = e.getPlayer();

            ItemStack handItem = p.getInventory().getItem(EquipmentSlot.HAND);
            if (this.gameItems.contains(handItem.getType()) && this.gameStarted) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1);
                if (handItem.getType() == this.gameItems.get(randomNum)) {
                    p.sendMessage(ChatColor.RED + this.gameItemsNames.get(randomNum) + "! Das habe ich auch :P");
                    e.setCancelled(false);
                } else {
                    if ((handItem.getType() == Material.SHEARS && this.gameItems.get(randomNum) == Material.STONE) || (handItem.getType() == Material.PAPER && this.gameItems.get(randomNum) == Material.SHEARS) || (handItem.getType() == Material.STONE && this.gameItems.get(randomNum) == Material.PAPER)) {
                        p.sendMessage(ChatColor.RED + this.gameItemsNames.get(randomNum) + "! Ich hab gewonnen! :P");
                        p.getWorld().dropItemNaturally(v.getLocation(), new ItemStack(Material.ROTTEN_FLESH, 64));

                        Firework firework = p.getWorld().spawn(v.getLocation(), Firework.class);
                        FireworkMeta fireworkMeta = firework.getFireworkMeta();
                        fireworkMeta.addEffect(FireworkEffect.builder().flicker(false).with(FireworkEffect.Type.BALL).trail(false).withColor(Color.RED).build());
                        firework.setFireworkMeta(fireworkMeta);

                    } else {
                        p.sendMessage(ChatColor.RED + this.gameItemsNames.get(randomNum) + "! Du hast gewonnen!");
                        p.getWorld().dropItemNaturally(v.getLocation(), new ItemStack(Material.EMERALD, 64));
                        v.playEffect(EntityEffect.VILLAGER_ANGRY);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 30, 1));
                        e.setCancelled(false);
                    }

                }
                gameStarted = false;
                if (this.playerItems.size() > 0) {
                    for (int i = 0; i < 9; i++) {
                        if (this.playerItems.get(i) != null) {
                            p.getInventory().setItem(i, this.playerItems.get(i));
                        }
                    }
                }

                this.timeout = true;
            } else if (!this.timeout && !this.gameStarted) {

                p.sendMessage(ChatColor.RED + "Lass uns Schnick Schnack Schnuck spielen!");
                this.gameStarted = true;

                this.timeoutTimer.schedule(this.timeoutTask, 30 * 1000);
                this.timeout = true;
                for (int i = 0; i < 9; i++) {
                    if (p.getInventory().getItem(i) != null) {
                        this.playerItems.add(i, p.getInventory().getItem(i));
                        p.getInventory().clear(i);
                    }
                }


                for (int i = 0; i < this.gameItems.size(); i++) {
                    p.getInventory().setItem(i, new ItemStack(this.gameItems.get(i), 1));
                }


            } else {
                p.sendMessage(ChatColor.RED + "30 Sekunden Cooldown!");
            }
        }
    }
}
