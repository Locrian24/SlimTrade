package com.slimtrade.gui.options.audio;

import com.slimtrade.core.data.PriceThresholdData;
import com.slimtrade.core.managers.SaveManager;
import com.slimtrade.core.utility.ZUtil;
import com.slimtrade.gui.components.AddRemoveContainer;
import com.slimtrade.modules.saving.ISavable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AudioThresholdPanel extends JPanel implements ISavable {

    private JButton newThresholdButton = new JButton("New Price Threshold");
    private AddRemoveContainer container = new AddRemoveContainer();

    public AudioThresholdPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        gc.anchor = GridBagConstraints.WEST;

        add(new JLabel("Incoming trades will play the sound of the highest threshold they cross."), gc);
        gc.gridy++;
        add(newThresholdButton, gc);
        gc.gridy++;
        add(container, gc);
        addListeners();
    }

    private void addListeners() {
        newThresholdButton.addActionListener(e -> container.add(new AudioThresholdRow(container)));
    }

    @Override
    public void save() {
        ArrayList<PriceThresholdData> priceThresholds = new ArrayList<>();
        for (Component c : container.getComponents()) {
            if (c instanceof AudioThresholdRow) {
                priceThresholds.add(((AudioThresholdRow) c).getData());
            }
        }
        SaveManager.settingsSaveFile.data.priceThresholds = priceThresholds;
    }

    @Override
    public void load() {
        container.removeAll();
        for (PriceThresholdData data : SaveManager.settingsSaveFile.data.priceThresholds) {
            AudioThresholdRow row = new AudioThresholdRow(container);
            row.setData(data);
            container.add(row);
        }
    }
}
