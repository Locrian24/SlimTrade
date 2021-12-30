package com.slimtrade.gui.basic;

import javax.swing.*;

public class ColorPanel extends JPanel {

    private String key;

    public ColorPanel(String key) {
        this.key = key;
        updateUI();
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (key != null) {
            setBackground(UIManager.getColor(key));
        }
    }
}

