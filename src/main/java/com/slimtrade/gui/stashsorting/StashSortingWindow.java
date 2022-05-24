package com.slimtrade.gui.stashsorting;

import com.slimtrade.core.enums.StashTabColor;
import com.slimtrade.core.managers.SaveManager;
import com.slimtrade.core.utility.ZUtil;
import com.slimtrade.core.utility.searchInStash;
import com.slimtrade.gui.messaging.NotificationButton;
import com.slimtrade.gui.windows.CustomDialog;

import javax.swing.*;
import java.awt.*;

public class StashSortingWindow extends CustomDialog {

    private final GridBagConstraints gc = ZUtil.getGC();

    public StashSortingWindow() {
        super("Sorting", true);
        setFocusable(false);
        setFocusableWindowState(false);
        contentPanel.setLayout(new GridBagLayout());
        refreshButtons();
    }

    public void refreshButtons() {
        contentPanel.removeAll();
        gc.gridy = 0;
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        for (StashSortData data : SaveManager.settingsSaveFile.data.stashSortData) {
            JButton button = new NotificationButton(data.TAG);
            contentPanel.add(button, gc);
            if (data.COLOR_INDEX > 0) {
                button.setBackground(StashTabColor.values()[data.COLOR_INDEX].getBackground());
                button.setForeground(StashTabColor.values()[data.COLOR_INDEX].getForeground());
                button.addActionListener(e -> searchInStash.findInStash(data.SEARCH));
            }
            gc.gridy++;
        }
        revalidate();
        repaint();
        pack();
    }

}
