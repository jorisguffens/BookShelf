package com.gufli.bookshelf.menu;

public interface Menu<T, U> {

    T getHandle();

    MenuItem<U> getItem(int slot);

    <V extends MenuItem<U>> void setItem(int slot, V item);

    void removeItem(int slot);

}
