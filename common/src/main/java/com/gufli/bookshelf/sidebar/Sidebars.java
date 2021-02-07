package com.gufli.bookshelf.sidebar;

import com.gufli.bookshelf.entity.ShelfPlayer;

public class Sidebars {

    private static SidebarManager sidebarManager;

    public static void register(SidebarManager manager) {
        if (sidebarManager != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton SidebarManager.");
        }

        sidebarManager = manager;
    }

    public static void setSidebar(ShelfPlayer player, Sidebar sidebar) {
        sidebarManager.setSidebar(player, sidebar);
    }

    public static Sidebar getSidebar(ShelfPlayer player) {
        return sidebarManager.getSidebar(player);
    }

    public static void removeSidebar(ShelfPlayer player) {
        sidebarManager.removeSidebar(player);
    }

    public static void updateSidebar(ShelfPlayer player) {
        sidebarManager.updateSidebar(player);
    }

}
