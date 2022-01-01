package com.gufli.bookshelf.api.location.region;

import com.gufli.bookshelf.api.location.ShelfLocation;

public interface Region {

    boolean contains(ShelfLocation loc);

}
