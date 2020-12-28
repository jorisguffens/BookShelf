package com.gufli.bookshelf.nametags;

import com.gufli.bookshelf.entity.PlatformPlayer;

public interface NametagHandler {

    void setNametag(PlatformPlayer player, String prefix, String suffix);

    void setPrefix(PlatformPlayer player, String prefix);

    void setSuffix(PlatformPlayer player, String suffix);

    void clear(PlatformPlayer player);

}
