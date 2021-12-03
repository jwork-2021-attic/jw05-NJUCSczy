package com.example.com.anish.calabash;

import com.example.asciiPanel.AsciiPanel;
import java.awt.Color;

public class Target extends Thing {

    Target(World world) {
        super(new Color(255,0,0), (char) 33, world);
    }

}
