package com.gufli.bookshelf.api.nametags;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public interface NametagManager {

    void setNametag(ShelfPlayer player, String prefix, String suffix);

    void setPrefix(ShelfPlayer player, String prefix);

    void setSuffix(ShelfPlayer player, String suffix);

    void removeNametag(ShelfPlayer player);

}
