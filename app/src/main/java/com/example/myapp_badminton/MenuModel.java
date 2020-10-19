package com.example.myapp_badminton;

import androidx.fragment.app.Fragment;

/**
 * Created by anupamchugh on 22/12/17.
 */

public class MenuModel {

    public String menuName;
    public boolean hasChildren, isGroup;
    public Fragment frag;

    /*public MenuModel(String menuName, boolean isGroup, boolean hasChildren, String url) {

        this.menuName = menuName;
        this.url = url;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
    }*/

    public MenuModel(String menuName, boolean isGroup, boolean hasChildren, Fragment frag) {
        this.menuName = menuName;
        this.frag = frag;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
    }
}
