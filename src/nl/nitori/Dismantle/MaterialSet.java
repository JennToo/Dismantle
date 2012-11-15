package nl.nitori.Dismantle;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

class MaterialSet {
    private HashMap<Material, Integer> mats;
    
    public MaterialSet() {
        mats = new HashMap<Material, Integer>();
    }
    
    public void addItem(Material mat, int quantity) {
        if(mats.containsKey(mat)) {
            mats.put(mat, quantity + mats.get(mat));
        } else {
            mats.put(mat, quantity);
        }
    }
    
    public ItemStack[] getItems(float quantityFactor) {
        ItemStack[] stacks = new ItemStack[mats.size()];
        int i = 0;
        for(Material m : mats.keySet()) {
            stacks[i] = new ItemStack(m, (int)(mats.get(m) * quantityFactor));
            i++;
        }
        return stacks;
    }
}
