package com.ymdwiseguy.col.menu;

import java.util.List;

public class Submenu {


    private String entryName;
    private List<MenuEntry> menuEntries;

    public Submenu(String entryName, List<MenuEntry> menuEntries) {
        this.entryName = entryName;
        this.menuEntries = menuEntries;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public List<MenuEntry> getMenuEntries() {
        return menuEntries;
    }

    public void setMenuEntries(List<MenuEntry> menuEntries) {
        this.menuEntries = menuEntries;
    }
}
