package com.gufli.bookshelf.api.nametags;

import com.gufli.bookshelf.api.entity.ShelfPlayer;

public interface NametagManager {

    void changeNametag(ShelfPlayer player, String prefix, String suffix);

    void changePrefix(ShelfPlayer player, String prefix);

    void changeSuffix(ShelfPlayer player, String suffix);

    void removeNametag(ShelfPlayer player);

}
