package dev.mrvte.schnick.schnick;

import org.bukkit.*;
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

public class SchnickListener implements Listener {

    List<Material> gameItems = Arrays.asList(Material.SHEARS, Material.STONE, Material.PAPER);
    List<String> gameItemsNames = Arrays.asList("Schere", "Stein", "Papier");
    HashMap<UUID,Player> playerHashMap = new HashMap<>();
    HashMap<UUID, ItemStack> playerItemsMap = new HashMap<>();
    HashMap<UUID,Villager> villagerHashMap = new HashMap<>();

    Boolean gameStarted = false;
    Boolean timeout = false;
    @EventHandler
    public void rightClickOnVillager(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Villager) {
            clickOnVillager(event);
        }
    }

    public void clickOnVillager(PlayerInteractEntityEvent event){
        Villager villager = (Villager) event.getRightClicked();
        villagerHashMap.put(villager.getUniqueId(),villager);
        Player player = event.getPlayer();
        playerHashMap.put(player.getUniqueId(),player);

        if (!this.timeout && !this.gameStarted) {
            player.sendMessage(ChatColor.DARK_BLUE + "Lass uns Schnick Schnakc Schnuck spielen!");
            gameStarted = true;

            for (int i = 0; i < 9; i++) {
                if (player.getInventory().getItem(i) != null) {
                    playerItemsMap.put(player.getUniqueId(), player.getInventory().getItem(i));
                    player.getInventory().clear(i);
                }
            }
            for (int i = 0; i < this.gameItems.size(); i++) {
                player.getInventory().setItem(i, new ItemStack(this.gameItems.get(i), 1));
            }
            startGame(event, player, villager);

        }else{
            player.sendMessage(ChatColor.RED + "30 Sekunden Cooldown!");
        }


    }

    private void startGame(PlayerInteractEntityEvent event, Player player, Villager villager) {
        int randomNumb = (int) (Math.random() * 3 + 1);
        ItemStack handItem = player.getInventory().getItem(EquipmentSlot.HAND);

        if (handItem.getType() == this.gameItems.get(randomNumb)) {
            player.sendMessage(ChatColor.RED + this.gameItemsNames.get(randomNumb) + "! Das habe ich auch :P");
            event.setCancelled(false);
        } else {
            if ((handItem.getType() == Material.SHEARS && this.gameItems.get(randomNumb) == Material.STONE) || (handItem.getType() == Material.PAPER && this.gameItems.get(randomNumb) == Material.SHEARS) || (handItem.getType() == Material.STONE && this.gameItems.get(randomNumb) == Material.PAPER)) {
                player.sendMessage(ChatColor.RED + this.gameItemsNames.get(randomNumb) + "! Ich hab gewonnen! :P");
                player.getWorld().dropItemNaturally(villager.getLocation(), new ItemStack(Material.ROTTEN_FLESH, 64));

                Firework firework = player.getWorld().spawn(villager.getLocation(), Firework.class);
                FireworkMeta fireworkMeta = firework.getFireworkMeta();
                fireworkMeta.addEffect(FireworkEffect.builder().flicker(false).with(FireworkEffect.Type.BALL).trail(false).withColor(Color.RED).build());
                firework.setFireworkMeta(fireworkMeta);

            } else {
                player.sendMessage(ChatColor.RED + this.gameItemsNames.get(randomNumb) + "! Du hast gewonnen!");
                player.getWorld().dropItemNaturally(villager.getLocation(), new ItemStack(Material.EMERALD, 64));
                villager.playEffect(EntityEffect.VILLAGER_ANGRY);
                player.addPotionEffect(new PotionEffect(PotionEffectType.HERO_OF_THE_VILLAGE, 30, 1));
                event.setCancelled(false);
            }

        }
        gameStarted = false;
        if (this.playerItemsMap.size() > 0) {
            for (int i = 0; i < 9; i++) {
                if (this.playerHashMap.get(i) != null) {
                    playerItemsMap.put(player.getUniqueId(), player.getInventory().getItem(i));
                    player.getInventory().setItem(i,playerItemsMap.get(player.getUniqueId()));
                    //player.getInventory().setItem(i, this.playerItems.get(i));
                }
            }
        }

        this.timeout = true;
    }


}
