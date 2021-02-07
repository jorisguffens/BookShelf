package com.gufli.bookshelf.nametags;

import com.gufli.bookshelf.entity.ShelfPlayer;

public interface NametagManager {

    void setNametag(ShelfPlayer player, String prefix, String suffix);

    void setPrefix(ShelfPlayer player, String prefix);

    void setSuffix(ShelfPlayer player, String suffix);

    void removeNametag(ShelfPlayer player);

}
