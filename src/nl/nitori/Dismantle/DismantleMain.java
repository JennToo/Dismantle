package nl.nitori.Dismantle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;

public class DismantleMain extends JavaPlugin {
    HashMap<Material, MaterialSet> breakDowns;
    HashMap<Material, Short> maxDurability;

    public void onEnable() {
        populateBreakDowns("plugins/dismantle.cfg");
    }

    public void onDisable() {
    }

    private void populateBreakDowns(String fileName) {
        breakDowns = new HashMap<Material, MaterialSet>();
        try {
            FileReader reader = new FileReader(fileName);
            BufferedReader buf = new BufferedReader(reader);

            String line = buf.readLine();
            int lineCount = 1;

            MaterialSet set = null;
            Material base = null;
            while (line != null) {
                line = line.trim();

                if (line.equals("")) {
                    if (set != null) {
                        breakDowns.put(base, set);
                    }
                    set = null;
                    base = null;
                } else if (line.charAt(0) == '#') { // A comment line
                } else if (set == null) {
                    base = Material.valueOf(line);
                    set = new MaterialSet();
                } else {
                    String[] parts = line.split(" ");
                    if (parts.length != 2) {
                        getLogger().severe(
                                "Malformed dismantle data file on line "
                                        + lineCount);
                    } else {
                        try {
                            set.addItem(Material.valueOf(parts[0]),
                                    Integer.parseInt(parts[1]));
                        } catch (NumberFormatException e) {
                            getLogger().severe(
                                    "Malformed dismantle data file on line "
                                            + lineCount
                                            + ". Was expecting a number. Got: "
                                            + parts[1]);
                        } catch (IllegalArgumentException e) {
                            getLogger()
                                    .severe("Malformed dismantle data file on line "
                                            + lineCount
                                            + ". Was expecting a Material. Got: "
                                            + parts[0]);
                        }
                    }
                }
                lineCount++;
                line = buf.readLine();
            }
            if (set != null) {
                breakDowns.put(base, set);
            }
            getLogger().info(
                    "Loaded " + breakDowns.size() + " dismantle recipies");
            buf.close();
        } catch (FileNotFoundException e1) {
            getLogger().severe(
                    "Could not find dismantle data file: " + fileName);
        } catch (IOException e1) {
            getLogger().severe("Error reading dismantle data file");
        }

    }

    public boolean onCommand(CommandSender sender, Command cmd, String label,
            String[] args) {
        if (cmd.getName().equalsIgnoreCase("dismantle")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Error: Dismantle must be run by a player");
            }
            Player player = (Player) sender;
            breakDownHand(player);
            return true;
        }
        return false;
    }

    private void breakDownHand(Player player) {
        ItemStack hand = player.getItemInHand();
        if (!breakDowns.containsKey(hand.getType())) {
            player.sendMessage("That item can't be dismantled");
            return;
        }

        /*
         * if (hand.getDurability() != 0) {
         * player.sendMessage("The item must be fully repaired before dismantling"
         * ); return; }
         */
        float factor = 1.0f;
        if (hand.getDurability() != 0) {
            short max = hand.getType().getMaxDurability();
            factor = (float) (max - hand.getDurability()) / max;
        }
        ItemStack[] broken = breakDowns.get(hand.getType()).getItems(factor);
        player.getInventory().clear(player.getInventory().getHeldItemSlot());

        for (ItemStack s : broken) {
            HashMap<Integer, ItemStack> ret = player.getInventory().addItem(s);
            for (Integer i : ret.keySet()) {
                player.getLocation().getWorld()
                        .dropItem(player.getLocation(), ret.get(i));
            }
        }
    }
}
