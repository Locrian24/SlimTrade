package com.slimtrade.gui.options;

import com.slimtrade.core.enums.MessageType;
import com.slimtrade.core.trading.TradeOffer;
import com.slimtrade.core.utility.GUIReferences;
import com.slimtrade.core.utility.MacroButton;
import com.slimtrade.core.utility.ZUtil;
import com.slimtrade.gui.components.AddRemoveContainer;
import com.slimtrade.gui.components.PlainLabel;
import com.slimtrade.gui.messaging.TradeMessagePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AbstractMacroOptionPanel extends AbstractOptionPanel {

    private MessageType messageType = MessageType.INCOMING_TRADE;
    protected final AddRemoveContainer macroContainer;


    private final JPanel exampleTradeContainer = new JPanel(new GridBagLayout());
    private GridBagConstraints gc = new GridBagConstraints();

    // Examples
    private JButton exampleButton = new JButton("Show Examples");
    private final Component exampleHeaderPanel;
    private Component examplePanel;
    private Component exampleSeparator;
    private boolean showExamples;

    public AbstractMacroOptionPanel(MessageType messageType) {
        this.messageType = messageType;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(0, 0, 2, 0);

        macroContainer = new AddRemoveContainer();
        macroContainer.setSpacing(4);

//        macroContainer.setLayout(new GridBagLayout());


        JButton addMacroButton = new JButton("Add New Macro");
        addMacroButton.addActionListener(e -> {
            macroContainer.add(new MacroCustomizerPanel(macroContainer));
            gc.gridy++;
            revalidate();
            repaint();
        });

        addHeader("Macro Preview");
        addPanel(exampleTradeContainer);
        addHeader("Inbuilt Macros");
        addPanel(inbuiltMacroPanel("Player Name", "/whois {player}", "Open empty whisper message"));
        addSmallVerticalStrut();
        addPanel(inbuiltMacroPanel("Item Name", "Open Stash Helper", "Ignore Item"));
        addVerticalStrut();
        addHeader("Custom Macro Info");
        addPanel(new PlainLabel("Run one or more commands using {player}, {self}, {item}, {price}, {zone}, and {message}."));
        addPanel(new PlainLabel("Commands that don't start with @ or / will have '@{player}' added automatically."));
        addPanel(new PlainLabel("Hotkeys use the left click of the oldest trade. Use escape to clear a hotkey."));
        addSmallVerticalStrut();
        addPanel(exampleButton);

        addVerticalStrut();
        exampleHeaderPanel = addHeader("Examples");
        examplePanel = addPanel(createExamplePanel());
        exampleSeparator = addVerticalStrut();
        addHeader("Custom Macros");

//        JPanel buttonPanel = new JPanel();
        addPanel(addMacroButton);
        addPanel(macroContainer);
        hideExamples();
        addListeners();
    }

    private void addListeners() {
        exampleButton.addActionListener(e -> {
            showExamples = !showExamples;
            if (showExamples) showExamples();
            else hideExamples();
        });
    }

    private JPanel inbuiltMacroPanel(String text, String lmb, String rmb) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel(text), gc);
        gc.gridx++;
//        gc.gridy++;
        gc.insets.left = 20;
        panel.add(new JLabel("Left Mouse"), gc);
        gc.gridy++;
        panel.add(new JLabel("Right Mouse"), gc);
        gc.gridx++;
        gc.gridy--;
        panel.add(new PlainLabel(lmb), gc);
        gc.gridy++;
        panel.add(new PlainLabel(rmb), gc);
        return panel;
    }

    private JPanel createExamplePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = ZUtil.getGC();
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
//        panel.add(new JLabel("Message"), gc);
//        gc.gridx++;
//        gc.insets.left = GUIReferences.INSET;
//        panel.add(new JLabel("Result"), gc);
//        gc.insets.left = 0;
//        gc.gridx = 0;
//        gc.gridy++;

        panel.add(new JLabel("thanks"), gc);
        gc.gridx++;
        gc.insets.left = GUIReferences.INSET;
        panel.add(new PlainLabel("Whisper thanks"), gc);
        gc.insets.left = 0;
        gc.gridx = 0;
        gc.gridy++;

        panel.add(new JLabel("thanks /kick {player}"), gc);
        gc.gridx++;
        gc.insets.left = GUIReferences.INSET;
        panel.add(new PlainLabel("Whisper thanks, then kick the other player."), gc);
        gc.insets.left = 0;
        gc.gridx = 0;
        gc.gridy++;

        panel.add(new JLabel("thanks /kick {self} /hideout"), gc);
        gc.gridx++;
        gc.insets.left = GUIReferences.INSET;
        panel.add(new PlainLabel("Whisper thanks, leave the party, then warp to your own hideout."), gc);
        gc.insets.left = 0;
        gc.gridx = 0;
        gc.gridy++;

        panel.add(new JLabel("hold on, in {zone}"), gc);
        gc.gridx++;
        gc.insets.left = GUIReferences.INSET;
        panel.add(new PlainLabel("Let a player know what zone you are in."), gc);
        gc.insets.left = 0;
        gc.gridx = 0;
        gc.gridy++;

        panel.add(new JLabel("{message}"), gc);
        gc.gridx++;
        gc.insets.left = GUIReferences.INSET;
        panel.add(new PlainLabel("Resend an outgoing trade message."), gc);
        gc.insets.left = 0;
        gc.gridx = 0;
        gc.gridy++;

        return panel;
    }

    private void showExamples() {
        exampleButton.setText("Hide Examples");
        exampleHeaderPanel.setVisible(true);
        examplePanel.setVisible(true);
        exampleSeparator.setVisible(true);
    }

    private void hideExamples() {
        exampleButton.setText("Show Examples");
        exampleHeaderPanel.setVisible(false);
        examplePanel.setVisible(false);
        exampleSeparator.setVisible(false);
    }

    public void reloadExampleTrade() {
        exampleTradeContainer.removeAll();
        TradeMessagePanel panel = null;
        switch (messageType) {
            case INCOMING_TRADE:
                panel = new TradeMessagePanel(TradeOffer.getExampleTrade(TradeOffer.TradeOfferType.INCOMING), false);
                break;
            case OUTGOING_TRADE:
                panel = new TradeMessagePanel(TradeOffer.getExampleTrade(TradeOffer.TradeOfferType.OUTGOING), false);
                break;
        }
        assert panel != null;
        exampleTradeContainer.add(panel);
        exampleTradeContainer.revalidate();
        exampleTradeContainer.repaint();
    }

    public void clearMacros() {
        macroContainer.removeAll();
        gc.gridy = 0;
    }

    public void addMacro(MacroButton macro) {
        MacroCustomizerPanel macroPanel = new MacroCustomizerPanel(macroContainer);
        macroPanel.setMacro(macro);
        macroContainer.add(macroPanel);
        gc.gridy++;
    }

    public ArrayList<MacroButton> getMacros() {
        ArrayList<MacroButton> macros = new ArrayList<>(macroContainer.getComponentCount());
        for (Component c : macroContainer.getComponents()) {
            if (c instanceof MacroCustomizerPanel) {
                MacroCustomizerPanel panel = (MacroCustomizerPanel) c;
                MacroButton macro = panel.getMacroButton();
                macros.add(macro);
            }
        }
        return macros;
    }

}
