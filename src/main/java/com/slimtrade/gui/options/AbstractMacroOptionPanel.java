package com.slimtrade.gui.options;

import com.slimtrade.core.enums.MessageType;
import com.slimtrade.core.trading.TradeOffer;
import com.slimtrade.core.utility.MacroButton;
import com.slimtrade.gui.messaging.TradeMessagePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AbstractMacroOptionPanel extends AbstractOptionPanel {

    private MessageType messageType = MessageType.INCOMING_TRADE;
    protected final JPanel macroContainer;


    private final JPanel exampleTradeContainer = new JPanel(new GridBagLayout());
    private GridBagConstraints gc = new GridBagConstraints();


    public AbstractMacroOptionPanel(MessageType messageType) {
        this.messageType = messageType;
        gc.gridx = 0;
        gc.gridy = 0;
        gc.insets = new Insets(0, 0, 2, 0);

        macroContainer = new JPanel();
        macroContainer.setLayout(new GridBagLayout());

        JPanel buttonPanel = new JPanel();
        JButton addMacroButton = new JButton("Add New Macro");
        addMacroButton.addActionListener(e -> {
            macroContainer.add(new MacroCustomizerPanel(macroContainer), gc);
            gc.gridy++;
            revalidate();
            repaint();
        });

        addHeader("Macro Preview");
        addPanel(exampleTradeContainer);
        addHeader("Inbuilt Macros");
        addHeader("Custom Macros");
        addPanel(addMacroButton);
        addPanel(macroContainer);
    }


    public void reloadExampleTrade() {
        exampleTradeContainer.removeAll();
        switch (messageType) {
            case INCOMING_TRADE:
                exampleTradeContainer.add(new TradeMessagePanel(TradeOffer.getExampleTrade(TradeOffer.TradeOfferType.INCOMING), false));
//                addComponent(new TradeMessagePanel(TradeOffer.getExampleTrade(TradeOffer.TradeOfferType.INCOMING)));
                break;
            case OUTGOING_TRADE:
//                addComponent(new TradeMessagePanel(TradeOffer.getExampleTrade(TradeOffer.TradeOfferType.OUTGOING)));
                exampleTradeContainer.add(new TradeMessagePanel(TradeOffer.getExampleTrade(TradeOffer.TradeOfferType.OUTGOING), false));
                break;
        }
    }

    public void clearMacros() {
        macroContainer.removeAll();
        gc.gridy = 0;
    }

    public void addMacro(MacroButton macro) {
        MacroCustomizerPanel macroPanel = new MacroCustomizerPanel(macroContainer);
        macroPanel.setMacro(macro);
        macroContainer.add(macroPanel, gc);
        gc.gridy++;
    }

    public ArrayList<MacroButton> getMacros() {
        ArrayList<MacroButton> macros = new ArrayList<>(macroContainer.getComponentCount());
        for (Component c : macroContainer.getComponents()) {
            if (c instanceof MacroCustomizerPanel) {
                MacroCustomizerPanel panel = (MacroCustomizerPanel) c;
                MacroButton macro = panel.getMacroButton();
                macros.add(macro);
            } else {
                // ERROR
                System.err.println("Macro Option Panel contains non customizer component!");
            }
        }
        return macros;
    }

}
