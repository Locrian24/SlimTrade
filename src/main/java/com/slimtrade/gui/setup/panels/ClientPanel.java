package com.slimtrade.gui.setup.panels;

import com.slimtrade.App;
import com.slimtrade.gui.FrameManager;
import com.slimtrade.gui.basic.CustomTextField;
import com.slimtrade.gui.buttons.BasicButton;
import com.slimtrade.gui.setup.SetupWindow;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class ClientPanel extends AbstractSetupPanel implements ISetupValidator{

    private JPanel browsePanel = new JPanel(FrameManager.gridBag);
    private JPanel multiPathPanel = new JPanel(FrameManager.gridBag);

    private JLabel pathLabel = new JLabel("Client Path");
    public JTextField clientText = new CustomTextField();
    public JButton editButton = new BasicButton("Browse");

    private JLabel info1 = new JLabel("Enter the location of Path of Exile's Client.txt file.");
    private JLabel info2a = new JLabel("Multiple client files detected. Selected the one being used, or enter a new one.");
    private JLabel info2b = new JLabel("If this file was recently deleted, launch POE before running SlimTrade.");

//    private JLabel info2 = new JLabel("If this file was recently deleted, launch POE to recreate it.");

    private JFrame parent;
    private JFileChooser fileChooser;
    private ArrayList<JCheckBox> checkboxList = new ArrayList<>();

    public ClientPanel(JFrame parent) {
        this.parent = parent;
        clientText.setEditable(false);

        // Path Panel
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.insets.right = 5;
        browsePanel.add(clientText, gc);
        gc.insets.right = 0;
        gc.weightx = 0;
        gc.fill = GridBagConstraints.NONE;
        gc.gridx++;
        browsePanel.add(editButton, gc);
        gc.gridx = 0;

        // Multi Path Panel
        System.out.println("LENGGGG" + App.saveManager.clientPaths.size());
        if(App.saveManager.clientPaths.size() > 1) {
            for(String s : App.saveManager.clientPaths) {
                gc.insets.bottom = 5;
                gc.insets.right = 5;
                multiPathPanel.add(new JLabel(s), gc);
                gc.insets.right = 0;
                gc.gridx++;
                JButton b = new BasicButton("Select");
//                JCheckBox b = new CustomCheckbox();
//                checkboxList.add(b);
                multiPathPanel.add(b, gc);
                gc.gridx = 0;
                gc.gridy++;
                b.addActionListener(e -> {
                    clientText.setText(s);
                    FrameManager.setupWindow.refreshButtons();
                });
            }
        }
        gc.insets.bottom = 0;
        gc.gridy = 0;


        // This
        gc.insets.bottom = 5;
        container.add(info1, gc);
        gc.gridy++;
//        if(App.saveManager.clientPaths.size() > 1) {
//            gc.insets.bottom = 10;
//            container.add(info2a, gc);
//        } else {
//            container.add(info2b, gc);
//        }
        gc.insets.bottom = 0;
        gc.gridy++;
        gc.fill = GridBagConstraints.HORIZONTAL;
        container.add(multiPathPanel, gc);
        gc.gridy++;
        container.add(browsePanel, gc);
        gc.gridy++;
        container.add(Box.createHorizontalStrut(450), gc);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            fileChooser = new JFileChooser();
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            fileChooser = new JFileChooser();
        }

        editButton.addActionListener(e -> {
            int action = fileChooser.showOpenDialog(parent);
            if (action == JFileChooser.APPROVE_OPTION) {
                File clientFile = fileChooser.getSelectedFile();
                String path = clientFile.getPath();
                clientText.setText(path);
                FrameManager.setupWindow.refreshButtons();
            }
        });

        container.setBackground(SetupWindow.BACKGROUND_COLOR);
        multiPathPanel.setBackground(SetupWindow.BACKGROUND_COLOR);
        browsePanel.setBackground(SetupWindow.BACKGROUND_COLOR);
//        clientText.setBackground(SetupWindow.BACKGROUND_COLOR);
    }

    @Override
    public boolean isValidInput() {
        File file = new File(clientText.getText());
        if(file.exists() && file.isFile()) {
            return true;
        }
        return false;
    }

    @Override
    public void save() {
        App.saveManager.saveFile.clientPath = clientText.getText();
//        File file = new File(clientText.getText());
//        App.saveManager.saveFile.clientDirectory = file.getParent();
//        App.saveManager.saveFile.validClientPath = true;
    }

}
