package com.example.com.anish.screen;

import java.awt.event.KeyEvent;

import com.example.asciiPanel.AsciiPanel;

public interface Screen {

    public void displayOutput(AsciiPanel terminal);

    public Screen respondToUserInput(KeyEvent key);
}
