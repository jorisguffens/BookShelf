package com.gufli.bookshelf.api.sidebar;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public interface SidebarManager {

    void changeSidebar(ShelfPlayer player, Sidebar sidebar);

    void removeSidebar(ShelfPlayer player);

    void updateSidebar(ShelfPlayer player);

    void refresh(ShelfPlayer player);

    void refresh();

}
