package com.slimtrade.gui.stashsorting;

import com.slimtrade.core.utility.ZUtil;
import com.slimtrade.gui.buttons.IconButton;
import com.slimtrade.gui.components.AddRemoveContainer;
import com.slimtrade.gui.components.AddRemovePanel;

import javax.swing.*;
import java.awt.*;

public class StashSortRow extends AddRemovePanel {

    private StashSortData data;
    private JButton removeButton = new IconButton("/icons/default/closex64.png");
    private JButton shiftUpButton = new IconButton("/icons/default/arrow-upx48.png");
    private JButton shiftDownButton = new IconButton("/icons/default/arrow-downx48.png");
    private JLabel tagLabel = new JLabel();
    private JLabel searchLabel = new JLabel();

    public StashSortRow(AddRemoveContainer parent, StashSortData data) {
        super(parent);
        this.data = data;
        tagLabel.setText(data.TAG);
        searchLabel.setText(data.SEARCH);

        setLayout(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        add(removeButton, gc);
        gc.gridx++;
        add(shiftDownButton, gc);
        gc.gridx++;
        add(shiftUpButton, gc);
        gc.gridx++;
        gc.insets.left = 10;
        add(new StashColorPanel(data.COLOR_INDEX), gc);
        gc.gridx++;
        add(tagLabel, gc);
        gc.gridx++;
        add(searchLabel, gc);
        gc.gridx++;
        updateUI();
        addListeners();
    }

    private void addListeners() {
        removeButton.addActionListener(e -> removeFromParent());
        shiftUpButton.addActionListener(e -> shiftUp(shiftUpButton));
        shiftDownButton.addActionListener(e -> shiftDown(shiftDownButton));
    }

    public StashSortData getData() {
        return data;
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (searchLabel == null) return;
        Font font = searchLabel.getFont();
        searchLabel.setFont(font.deriveFont(Font.PLAIN, font.getSize()));
    }
}
