package com.ymdwiseguy.col.menu;

import java.util.List;

public class GameMenu {

    private List<Submenu> submenus;

    public GameMenu(List<Submenu> submenus) {
        this.submenus = submenus;
    }

    public List<Submenu> getSubmenus() {
        return submenus;
    }

    public void setSubmenus(List<Submenu> submenus) {
        this.submenus = submenus;
    }

}
