package com.slimtrade.core.utility;

import com.slimtrade.App;
import com.slimtrade.core.trading.TradeOffer;

import java.util.ArrayList;

public class SlimUtil {

    /**
     * Returns a printable version of an enum name.
     *
     * @param input
     * @return
     */
    public static String enumToString(String input) {
        input = input.replaceAll("_", " ");
        input = input.toLowerCase();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (i == 0 || input.charAt(i - 1) == ' ') {
                builder.append(Character.toUpperCase(input.charAt(i)));
            } else {
                builder.append(input.charAt(i));
            }
        }
        return builder.toString();
    }

    public static ArrayList<String> getCommandList(String input, TradeOffer tradeOffer) {
        ArrayList<String> commands = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c == '/' || c == '@') {
                if (builder.length() > 0)
                    commands.add(builder.toString().trim());
                builder.setLength(0);
            }
            builder.append(c);
        }
        if (builder.length() > 0)
            commands.add(builder.toString().trim());
        for (int i = 0; i < commands.size(); i++) {
            String clean = commands.get(i);
            clean = clean.replaceAll("\\{self}", App.saveManager.settingsSaveFile.characterName);
            clean = clean.replaceAll("\\{player}", tradeOffer.playerName);
            clean = clean.replaceAll("\\{item}", tradeOffer.playerName);
            clean = clean.replaceAll("\\{price}", tradeOffer.playerName);
            clean = clean.replaceAll("\\{message}", tradeOffer.playerName);
            clean = clean.replaceAll("\\{zone}", tradeOffer.playerName);
            commands.set(i, clean);
        }
        System.out.println("COMMANDS :: " + commands);
        return commands;
    }

    public static <T> int safeEnumIndex(Class<T> enumClass, int index) {
        System.out.println("enum len" + enumClass.getFields().length);
        return 0;
    }

    public static <T> void test(Class<T> e, int index) {
        System.out.println("WOW");


    }

}
