package com.slimtrade.core.chatparser;

import com.slimtrade.core.References;
import com.slimtrade.core.data.IgnoreItemData;
import com.slimtrade.core.data.PlayerMessage;
import com.slimtrade.core.managers.AudioManager;
import com.slimtrade.core.managers.SaveManager;
import com.slimtrade.core.trading.LangRegex;
import com.slimtrade.core.trading.TradeOffer;
import com.slimtrade.core.trading.TradeOfferType;
import com.slimtrade.core.utility.ZUtil;
import com.slimtrade.gui.chatscanner.ChatScannerEntry;
import com.slimtrade.modules.filetailing.FileTailer;
import com.slimtrade.modules.filetailing.FileTailerListener;
import com.slimtrade.modules.updater.ZLogger;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class ChatParser implements FileTailerListener {

    public static final int tailerDelayMS = 250;
    private FileTailer tailer;

    // Listeners
    private final ArrayList<IParserInitListener> onInitListeners = new ArrayList<>();
    private final ArrayList<IParserLoadedListener> onLoadListeners = new ArrayList<>();

    private final ArrayList<ITradeListener> tradeListeners = new ArrayList<>();
    private final ArrayList<IChatScannerListener> chatScannerListeners = new ArrayList<>();
    private final ArrayList<IJoinedAreaListener> joinedAreaListeners = new ArrayList<>();
    private final ArrayList<IDndListener> dndListeners = new ArrayList<>();

    // State
    private String path;
    private boolean open;
    private int lineCount;
    private int whisperCount;
    private String currentZone = "The Twilight Strand";
    private boolean dnd = false;
    private long startTime;

    public void open(String path) {
        lineCount = 0;
        whisperCount = 0;
        if (open) close();
        if (path == null) {
            ZLogger.err("Chat parser was given a null path!");
            return;
        }
        if (!ZUtil.fileExists(path)) {
            ZLogger.err("Chat parser could not find file: " + path);
            return;
        }
        this.path = path;
        tailer = FileTailer.createTailer(path, this, tailerDelayMS, false);
        startTime = System.currentTimeMillis();
        open = true;
    }

    public void close() {
        tailer.stop();
        path = null;
        open = false;
    }

    public String getPath() {
        return path;
    }

    public void parseLine(String line) {
        if (!open) return;
        lineCount++;
        if (handleZoneChange(line)) return;
        if (handleTradeOffer(line)) return;
        if (handleDndToggle(line)) return;
        if (handleChatScanner(line)) return;
        if (handlePlayerJoinedArea(line)) return;
    }

    private boolean handleChatScanner(String line) {
        if (!SaveManager.chatScannerSaveFile.data.searching) return false;
        Matcher chatMatcher = References.chatPatten.matcher(line);
        Matcher metaMatcher = References.clientMetaPattern.matcher(line);
        String message;
        String messageType;
        String player;
        if (chatMatcher.matches()) {
            message = chatMatcher.group("message");
            messageType = chatMatcher.group("messageType");
            player = chatMatcher.group("playerName");
        } else if (metaMatcher.matches()) {
            message = metaMatcher.group("message");
            messageType = "meta";
            player = "Meta Text";
        } else {
            return false;
        }
        if (messageType == null) return false;
        message = message.toLowerCase();
        messageType = messageType.toLowerCase();
        // Iterate though all active searches and look for matching phrases
        for (ChatScannerEntry entry : SaveManager.chatScannerSaveFile.data.activeSearches) {
            if (entry.getSearchTerms() == null) continue;
            for (String term : entry.getSearchTerms()) {
                if (message.contains(term)) {
                    boolean allow = false;
                    boolean ignore = false;
                    // Check if this message should be ignored
                    if (entry.getIgnoreTerms() != null) {
                        for (String ignoreTerm : entry.getIgnoreTerms()) {
                            if (message.contains(ignoreTerm)) {
                                ignore = true;
                                break;
                            }
                        }
                    }
                    // Verify the message is allowed
                    if (entry.allowGlobalAndTradeChat && (messageType.equals("#") || messageType.equals("$")))
                        allow = true;
                    if (entry.allowWhispers) {
                        for (LangRegex lang : LangRegex.values()) {
                            if (lang.messageFrom == null) continue;
                            if (messageType.contains(lang.messageFrom)) {
                                allow = true;
                                break;
                            }
                        }
                    }
                    if (entry.allowMetaText && messageType.equals("meta")) allow = true;
                    if (ignore || !allow) continue;
                    PlayerMessage playerMessage = new PlayerMessage(player, message);
                    for (IChatScannerListener listener : chatScannerListeners)
                        listener.onScannerMessage(entry, playerMessage, tailer.isLoaded());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean handleTradeOffer(String line) {
        // Check if it is a whisper
        if (!line.contains("@")) return false;
        whisperCount++;
        // Check if it is a trade
        TradeOffer offer = TradeOffer.getTradeFromMessage(line);
        if (offer == null) return false;
        // Check if the trade should be ignored
        String itemNameLower = offer.itemName.toLowerCase();
        if (offer.offerType == TradeOfferType.INCOMING_TRADE) {
            IgnoreItemData item = SaveManager.ignoreSaveFile.data.exactMatchIgnoreMap.get(itemNameLower);
            if (item != null && !item.isExpired()) {
                handleIgnoreItem();
                return true;
            }
            for (IgnoreItemData ignoreItemData : SaveManager.ignoreSaveFile.data.containsTextIgnoreList) {
                if (itemNameLower.contains(ignoreItemData.itemNameLower()) && !ignoreItemData.isExpired()) {
                    handleIgnoreItem();
                    return true;
                }
            }
        }
        // Handle trade
        for (ITradeListener listener : tradeListeners)
            listener.handleTrade(offer, tailer.isLoaded());
        return true;
    }

    private boolean handleDndToggle(String line) {
        Matcher metaMatcher = References.clientMetaPattern.matcher(line);
        if (!metaMatcher.matches()) return false;
        String message = metaMatcher.group("message");
        if (message == null) return false;
        for (LangRegex lang : LangRegex.values()) {
            if (lang.dndOn == null || lang.dndOff == null) continue;
            if (message.contains(lang.dndOn)) {
                dnd = true;
                for (IDndListener listener : dndListeners) listener.onDndToggle(dnd, tailer.isLoaded());
                return true;
            }
            if (message.contains(lang.dndOff)) {
                dnd = false;
                for (IDndListener listener : dndListeners) listener.onDndToggle(dnd, tailer.isLoaded());
                return true;
            }
        }
        return false;
    }

    private void handleIgnoreItem() {
        if (tailer.isLoaded()) AudioManager.playSoundComponent(SaveManager.settingsSaveFile.data.itemIgnoredSound);
    }


    private boolean handlePlayerJoinedArea(String line) {
        for (LangRegex lang : LangRegex.values()) {
            if (lang.joinedArea == null) continue;
            Matcher matcher = lang.joinedAreaPattern.matcher(line);
            if (matcher.matches()) {
                String playerName = matcher.group("playerName");
                for (IJoinedAreaListener listener : joinedAreaListeners) {
                    listener.onJoinedArea(playerName);
                }
                return true;
            }
        }
        return false;
    }

    private boolean handleZoneChange(String line) {
        for (LangRegex lang : LangRegex.values()) {
            if (lang.enteredArea == null) continue;
            Matcher matcher = lang.enteredAreaPattern.matcher(line);
            if (matcher.matches()) {
                currentZone = matcher.group("zone");
                return true;
            }
        }
        return false;
    }

    public String getCurrentZone() {
        return currentZone;
    }

    public boolean isDndEnabled() {
        return dnd;
    }

    // Listeners
    public void addOnInitCallback(IParserInitListener listener) {
        onInitListeners.add(listener);
    }

    public void addOnLoadedCallback(IParserLoadedListener listener) {
        onLoadListeners.add(listener);
    }

    public void addTradeListener(ITradeListener listener) {
        tradeListeners.add(listener);
    }

    public void addChatScannerListener(IChatScannerListener listener) {
        chatScannerListeners.add(listener);
    }

    public void addJoinedAreaListener(IJoinedAreaListener listener) {
        joinedAreaListeners.add(listener);
    }

    public void addDndListener(IDndListener listener) {
        dndListeners.add(listener);
    }

    public void removeAllListeners() {
        onInitListeners.clear();
        onLoadListeners.clear();
        tradeListeners.clear();
        chatScannerListeners.clear();
        joinedAreaListeners.clear();
    }

    // File Tailing

    @Override
    public void init(FileTailer tailer) {
        for (IParserInitListener listener : onInitListeners) listener.onParserInit();
    }

    @Override
    public void fileNotFound() {

    }

    @Override
    public void fileRotated() {

    }

    @Override
    public void onLoad() {
        float endTime = (System.currentTimeMillis() - startTime) / 1000f;
        ZLogger.log("Chat parser loaded in " + endTime + " seconds. Found " + lineCount + " lines and " + whisperCount + " whispers.");
        for (IParserLoadedListener listener : onLoadListeners) listener.onParserLoaded();
    }

    @Override
    public void handle(String line) {
        parseLine(line);
    }

}
