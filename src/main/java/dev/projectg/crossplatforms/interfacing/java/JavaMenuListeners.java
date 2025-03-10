package dev.projectg.crossplatforms.interfacing.java;

import dev.projectg.crossplatforms.interfacing.InterfaceManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class JavaMenuListeners implements Listener {

    private final InterfaceManager interfaceManager;
    private final JavaMenuRegistry javaMenuRegistry;

    public JavaMenuListeners(InterfaceManager interfaceManager) {
        this.interfaceManager = interfaceManager;
        this.javaMenuRegistry = interfaceManager.getJavaRegistry();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // This is used for processing inventory clicks WITHIN the java menu GUI

        if (javaMenuRegistry.isEnabled()) {
            if (event.getWhoClicked() instanceof Player player) {
                ItemStack item = event.getCurrentItem();

                if (item != null) {
                    JavaMenu menu = javaMenuRegistry.getMenu(item);
                    if (menu != null) {
                        event.setCancelled(true);
                        menu.process(event.getSlot(), event.isRightClick(), player, interfaceManager);
                    }
                }
            }
        }
    }
}