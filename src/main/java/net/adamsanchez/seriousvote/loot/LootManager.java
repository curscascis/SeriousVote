package net.adamsanchez.seriousvote.loot;

import net.adamsanchez.seriousvote.utils.CC;
import net.adamsanchez.seriousvote.utils.CM;
import net.adamsanchez.seriousvote.utils.U;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.List;
import java.util.stream.Collectors;

public class LootManager {

    String[][] mainRewardTables;
    private int chanceTotal, chanceMax, chanceMin = 0;
    private int[] chanceMap;


    public void updateLoot(ConfigurationNode node) {
        List<String> rewardStrings = CM.getRandomCommands();
        if (!CM.getAreLootTablesAvailable()) {
            U.warn(CC.LINE);
            U.warn("There are no random tables to load, or they are formatted incorrectly. If you are not using random rewards you can ignore this message.");
            U.warn(CC.LINE);
            return;
        }
        String[] inputLootSource = rewardStrings.stream().toArray(String[]::new);
        //Create a new Array of the proper size x*2 to hold the tables for choosing later
        String[][] table = new String[2][inputLootSource.length / 2];
        chanceMap = new int[inputLootSource.length / 2];
        U.info(CC.PURPLE + inputLootSource.length / 2 + CC.YELLOW + " Tables Imported for Rewards");

        for (int ix = 0; ix < inputLootSource.length; ix += 2) {
            table[0][ix / 2] = inputLootSource[ix];
            table[1][ix / 2] = inputLootSource[ix + 1];
            //Initialize chanceMap
            chanceMap[ix / 2] = Integer.parseInt(table[0][ix / 2]);
            if (ix != 0) {
                chanceMap[ix / 2] += chanceMap[(ix / 2) - 1];

            }
        }
        mainRewardTables = table;
        chanceTotal = chanceMap.length - 1;
        chanceMin = chanceMap[0];
        chanceMax = chanceMap[chanceTotal];


    }
}
