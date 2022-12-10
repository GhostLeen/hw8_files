package com.denal.model;

import java.util.List;

public class MenuFiles {
    public String title;
    public MenuDiv menuDiv;

    public static class MenuDiv {
        public String id;
        public String value;
        public boolean used;
        public List<Popup> popup;
    }

    public static class Popup {
        public String value;
        public int clickId;
    }
}