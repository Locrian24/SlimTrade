package com.slimtrade.gui.messaging;

import com.slimtrade.core.data.SaleItem;
import com.slimtrade.core.enums.StashTabColor;
import com.slimtrade.core.managers.SaveManager;
import com.slimtrade.core.trading.TradeOffer;
import com.slimtrade.core.utility.AdvancedMouseListener;
import com.slimtrade.gui.managers.FrameManager;
import com.slimtrade.gui.stash.StashHelperPanel;

import java.awt.*;
import java.awt.event.MouseEvent;

public class TradeMessagePanel extends NotificationPanel {

    private StashHelperPanel stashHelperPanel;

    public TradeMessagePanel(TradeOffer offer) {
        this(offer, true);
    }

    public TradeMessagePanel(TradeOffer trade, boolean createListeners) {
        super(createListeners);
        tradeOffer = trade;
        if (FrameManager.stashHelperContainer != null)
            stashHelperPanel = FrameManager.stashHelperContainer.addHelper(trade);
        playerNameButton.setText(trade.playerName);
        itemButton.setItems(trade.getItems());
        pricePanel.setItem(new SaleItem(trade.priceTypeString, trade.priceQuantity));

        // Message type specific stuff
        switch (trade.offerType) {
            case INCOMING:
                topMacros = SaveManager.settingsSaveFile.data.incomingTopMacros;
                bottomMacros = SaveManager.settingsSaveFile.data.incomingBottomMacros;
                StashTabColor stashTabColor = trade.getStashTabColor();
                if (!SaveManager.settingsSaveFile.data.applyStashColorToMessage || stashTabColor == StashTabColor.ZERO) {
                    messageColor = new Color(105, 201, 97);
                } else {
                    messageColor = trade.getStashTabColor().getBackground();
                }
                break;
            case OUTGOING:
                topMacros = SaveManager.settingsSaveFile.data.outgoingTopMacros;
                bottomMacros = SaveManager.settingsSaveFile.data.outgoingBottomMacros;
                messageColor = new Color(180, 72, 72);
                break;
        }
        setup();
        if (createListeners) addListeners();
    }

    private void addListeners() {
        switch (tradeOffer.offerType) {
            case INCOMING:
                itemButton.addMouseListener(new AdvancedMouseListener() {
                    @Override
                    public void click(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            stashHelperPanel.setVisible(true);
                            FrameManager.stashHelperContainer.refresh();
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            FrameManager.itemIgnoreWindow.setItemName(tradeOffer.itemName);
                        }
                    }
                });
                break;
            case OUTGOING:
                break;
        }

    }

    @Override
    public void cleanup() {
        super.cleanup();
        FrameManager.stashHelperContainer.remove(stashHelperPanel);
        FrameManager.stashHelperContainer.refresh();
    }

}
