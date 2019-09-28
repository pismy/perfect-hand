package org.mtgpeasant.perfectdeck.goldfish;

import org.mtgpeasant.perfectdeck.common.cards.Cards;
import org.mtgpeasant.perfectdeck.handoptimizer.MulliganRules;

import java.util.Arrays;
import java.util.List;

public class ReanimatorDeckPilot extends DeckPilot {
    private static List<String> LANDS = Arrays.asList("swamp", "mountain", "crumbling vestige");
    private static List<String> REANIMATOR = Arrays.asList("Exhume", "Animate Dead", "Reanimate");
    private static List<String> MONSTERS = Arrays.asList("Hand of Emrakul", "Greater Sandwurm", "Pathrazer of Ulamog", "Ulamog's Crusher");

    private final MulliganRules rules;

    public ReanimatorDeckPilot(MulliganRules rules) {
        this.rules = rules;
    }

    @Override
    public boolean keep(Cards hand, int mulligans) {
        if(mulligans >= 2) {
            return true;
        }
        return rules.firstMatch(hand) != null;
    }

    @Override
    public void firstMainPhase(Game context) {
        // count available mana
        if(rules.findByName("turn 1 imp").matches(context.getHand(), rules) != null) {

        }
        // first land
        Cards hand = context.getHand();
        if (LANDS.stream().filter(hand::has).findFirst().isPresent()) {

        }
    }

    @Override
    public String checkWin(Game context) {
        super.checkWin(context);
        // consider I won as soon as I have a monster on the board
        Cards board = context.getBoard();
        if(MONSTERS.stream().filter(board::has).findFirst().isPresent()) {
            return "I reanimated a monster";
        }
        return null;
    }
}