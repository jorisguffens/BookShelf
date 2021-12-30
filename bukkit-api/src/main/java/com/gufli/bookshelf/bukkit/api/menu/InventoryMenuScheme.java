package com.gufli.bookshelf.bukkit.api.menu;

import java.util.ArrayList;
import java.util.List;

public class InventoryMenuScheme {

    private final int[] mask;

    private InventoryMenuScheme(int... rows) {
        mask = rows;
    }

    public static InventoryMenuScheme of(int... rows) {
        return new InventoryMenuScheme(rows);
    }

    public static InventoryMenuScheme of(String... rows) {
        int[] result = new int[rows.length];
        for (int i = 0; i < rows.length; i++) {
            result[i] = 0;
            for (int pos = 0; pos < rows[i].length(); pos++) {
                if (rows[i].charAt(pos) == '1') {
                    result[i] ^= 1 << pos;
                }
            }
        }
        return new InventoryMenuScheme(result);
    }


    public int getRows() {
        return mask.length;
    }

    public boolean isMasked(int slot) {
        int row = slot / 9;
        int pos = slot % 9;

        if (row >= mask.length) {
            return false;
        }

        return ((mask[row] >> pos) & 1) == 1;
    }

    public List<Integer> getSlots() {
        List<Integer> slots = new ArrayList<>();
        for (int slot = 0; slot < mask.length * 9; slot++) {
            if (isMasked(slot)) {
                slots.add(slot);
            }
        }
        return slots;
    }
}