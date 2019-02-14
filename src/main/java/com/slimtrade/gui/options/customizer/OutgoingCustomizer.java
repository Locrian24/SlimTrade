package main.java.com.slimtrade.gui.options.customizer;

import java.awt.Insets;
import java.util.Random;

import javax.swing.JSlider;

import main.java.com.slimtrade.core.utility.TradeOffer;
import main.java.com.slimtrade.enums.MessageType;
import main.java.com.slimtrade.gui.messaging.AbstractMessagePanel;
import main.java.com.slimtrade.gui.messaging.TradePanelA;
import main.java.com.slimtrade.gui.options.AbstractContentPanel;
import main.java.com.slimtrade.gui.panels.BufferPanel;

public class OutgoingCustomizer extends AbstractContentPanel {
	
	private static final long serialVersionUID = 1L;
	private TradePanelA exampleTradeIn;

	public OutgoingCustomizer(){
		//TODO : Colors
				Random rng = new Random();
				TradeOffer tradeOut = new TradeOffer("", "", MessageType.OUTGOING_TRADE, null, "SmashyMcFireBalls", "ITEM_NAME", 3.5, "chaos", 3.5, "STASH_TAB", rng.nextInt(12) + 1, rng.nextInt(12) + 1, "", "");

				
				AbstractMessagePanel exampleTradeOut = new TradePanelA(tradeOut, 40);
				exampleTradeOut.stopTimer();

				//OUTGOING PRESETS
				PresetMacroRow refreshOutPreset = new PresetMacroRow("Refresh", "/resources/icons/refresh1.png", true);
				refreshOutPreset.getRow("Left Mouse", "Resend trade message");
				PresetMacroRow warpOutPreset = new PresetMacroRow("Refresh", "/resources/icons/warp.png", true);
				warpOutPreset.getRow("Left Mouse", "Warp to other player's hideout");
				PresetMacroRow thankOutPreset = new PresetMacroRow("Refresh", "/resources/icons/thumb1.png", true);
				thankOutPreset.getRow("Left Mouse", "thanks", true);
				PresetMacroRow leaveOutPreset = new PresetMacroRow("Refresh", "/resources/icons/leave.png", true);
				leaveOutPreset.getRow("Left Mouse", "Kick yourself from the party");
				PresetMacroRow homeOutPreset = new PresetMacroRow("Refresh", "/resources/icons/home.png", true);
				homeOutPreset.getRow("Left Mouse", "Warp home");

				gc.insets = new Insets(2,0,0,0);
				addRow(exampleTradeOut, gc);
				addRow(refreshOutPreset, gc);
				addRow(warpOutPreset, gc);
				addRow(thankOutPreset, gc);
				addRow(leaveOutPreset, gc);
				addRow(homeOutPreset, gc);
				
				this.autoResize();
	}
	
	
}
