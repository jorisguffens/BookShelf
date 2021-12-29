package com.gufli.bookshelf.api.sidebar;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public class Sidebars {

    private Sidebars() {}

    private static SidebarManager sidebarManager;

    public static void register(SidebarManager manager) {
        if (sidebarManager != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton SidebarManager.");
        }

        sidebarManager = manager;
    }

    public static void setSidebar(ShelfPlayer player, SidebarTemplate sidebar) {
        sidebarManager.setSidebar(player, sidebar);
    }

    public static void removeSidebar(ShelfPlayer player) {
        sidebarManager.removeSidebar(player);
    }

    public static void updateSidebar(ShelfPlayer player) {
        sidebarManager.updateSidebar(player);
    }

    public static void refresh() {
        sidebarManager.refresh();
    }

    public static void refresh(ShelfPlayer player) {
        sidebarManager.refresh(player);
    }

}
