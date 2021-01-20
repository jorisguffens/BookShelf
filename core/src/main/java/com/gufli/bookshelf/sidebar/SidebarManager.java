package com.gufli.bookshelf.sidebar;

import com.gufli.bookshelf.entity.ShelfPlayer;

public interface SidebarManager {

    void setSidebar(ShelfPlayer player, Sidebar sidebar);

    Sidebar getSidebar(ShelfPlayer player);

    void removeSidebar(ShelfPlayer player);

    void updateSidebar(ShelfPlayer player);

}
