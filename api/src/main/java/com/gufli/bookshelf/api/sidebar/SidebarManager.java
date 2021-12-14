package com.gufli.bookshelf.api.sidebar;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public interface SidebarManager {

    void setSidebar(ShelfPlayer player, SidebarTemplate sidebar);

    void removeSidebar(ShelfPlayer player);

    void updateSidebar(ShelfPlayer player);

    void refresh(ShelfPlayer player);

    void refresh();

}
