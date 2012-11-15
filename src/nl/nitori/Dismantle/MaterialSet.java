/*
 * Copyright (c) 2012, Justin Wilcox
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
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
