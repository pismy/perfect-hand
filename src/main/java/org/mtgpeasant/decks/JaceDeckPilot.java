package org.mtgpeasant.decks;

import org.mtgpeasant.perfectdeck.common.Mana;
import org.mtgpeasant.perfectdeck.common.cards.Cards;
import org.mtgpeasant.perfectdeck.common.matchers.MulliganRules;
import org.mtgpeasant.perfectdeck.goldfish.DeckPilot;
import org.mtgpeasant.perfectdeck.goldfish.Game;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class JaceDeckPilot extends DeckPilot {

    public static final Mana U = Mana.of("U");
    public static final Mana U1 = Mana.of("U1");
    public static final Mana UU1 = Mana.of("UU1");
    public static final Mana ONE = Mana.of("1");
    public static final Mana TWO = Mana.of("2");
    public static final Mana FOUR = Mana.of("4");

    // LANDS
    public static final String ISLAND = "Island";
    public static final String DESERT = "Desert";

    // TUTORS
    public static final String TRINKET = "Trinket Mage";
    public static final String MUDDLE = "Muddle the Mixture";

    // DIG
    public static final String IMPULSE = "Impulse";
    public static final String ANTICIPATE = "Anticipate";
    public static final String PREORDAIN = "Preordain";

    // OTHERS
    public static final String TOP = "Sensei's Divining Top";
    public static final String HELM = "Helm of Awakening";
    public static final String SCULPTOR = "Etherium Sculptor";
    public static final String JACE = "Jace's Erasure";

    private static String[] MANA_PRODUCERS = new String[]{ISLAND, DESERT};
    private static String[] TUTORS = new String[]{TRINKET, MUDDLE};
    private static String[] UDIG = new String[]{PREORDAIN};
    private static String[] U1DIG = new String[]{IMPULSE, ANTICIPATE};

    private static MulliganRules rules;

    static {
        try {
            rules = MulliganRules.parse(new InputStreamReader(JaceDeckPilot.class.getResourceAsStream("/jace-rules.txt")));
        } catch (IOException e) {
            rules = null;
            System.err.println(e);
        }
    }

    public JaceDeckPilot(Game game) {
        super(game);
    }

    @Override
    public boolean keepHand(Cards hand) {
        if (game.getMulligans() >= 3) {
            return true;
        }
        return rules.firstMatch(hand).isPresent();
    }

    @Override
    public void start() {
        getRid(game.getMulligans());
    }

    @Override
    public void firstMainPhase() {

        // land
        // island, or desert if no island
        if (game.getHand().contains(ISLAND)  {
            game.land(ISLAND);
        } else if (game.getHand().contains(DESERT)) {
            game.land(DESERT);
        }



        // tutor
        for (String tutor : game.getHand().findAll(TUTORS)) {
            if (canPay(UU1)) {
                preparePool(UU1);
                game.castNonPermanent(tutor, UU1);
            }
        }

        // play top
        if (game.getHand().contains(TOP) && canPay(TWO) && (game.getBoard().count(TOP) == 0)) {
            preparePool(ONE);
            game.castPermanent(TOP, ONE);
        }

        // dig for 1
        for (String dig : game.getHand().findAll(UDIG)) {
            if (canPay(U)) {
                preparePool(U);
                game.castNonPermanent(dig, U1);
            }
        }



        // dig for 2
        for (String dig : game.getHand().findAll(U1DIG)) {
            if (canPay(U1)) {
                preparePool(U1);
                game.castNonPermanent(dig, U1);
            }
        }

    @Override
    public void secondMainPhase() {

        call firstMainPhase() // since now we maybe have a land to land

        // play top
        if (game.getHand().contains(TOP) && canPay(ONE) && (game.getBoard().count(TOP) == 0)) {
            preparePool(ONE);
            game.castPermanent(TOP, ONE);
        }
        
        // use top
        if (game.getBoard().count(TOP) > 0) {
            preparePool(ONE);
            // arrange 3 cards
        }


    }

    }

    @Override
    public void endingPhase() {
        if (game.getHand().size() > 7) {
            discard(game.getHand().size() - 7);
        }
    }

    void getRid(int number) {
        for (int i = 0; i < number; i++) {
            // discard extra lands
            if (game.getHand().count(MANA_PRODUCERS) > 3 && game.putOnBottomOfLibraryOneOf(MANA_PRODUCERS).isPresent()) {
                continue;
            }
            // discard extra counters
            if (game.getHand().count(COUNTERS) > 1 && game.putOnBottomOfLibraryOneOf(COUNTERS).isPresent()) {
                continue;
            }
            // discard extra tutors
            if (game.getHand().count(TUTORS) > 1 && game.putOnBottomOfLibraryOneOf(CREATURES).isPresent()) {
                continue;
            }
            // TODO: choose better
            game.putOnBottomOfLibrary(game.getHand().getFirst());
        }
    }

    void discard(int number) {
        for (int i = 0; i < number; i++) {    
            if (game.getHand().count(MANA_PRODUCERS) + game.getBoard().count(MANA_PRODUCERS) > 4 && game.discardOneOf(MANA_PRODUCERS).isPresent()) {
                    continue;
                }
            // discard extra counters
            if (game.putOnBottomOfLibraryOneOf(COUNTERS).isPresent()) {
                continue;
            }
            // discard extra tutors
            if ( game.putOnBottomOfLibraryOneOf(TUTORS).isPresent()) {
                continue;
            }
            game.discard(game.getHand().getFirst());
    }


    void choose(int number) {
        // chooses a card among the first "number" cards on top of the deck
        // j'ai mis ce que je voulais que ça fasse, mais je connais pas ton API, alors le boulot est encore à faire :p

        cards = get "number" card names from top of library 

        if (game.getHand().count(MANA_PRODUCERS) + game.getBoard().count(MANA_PRODUCERS) < min(game.turn_number(), 4) && cards.count(MANA_PRODUCERS) > 0) {
            return index of one MANA_PRODUCERS within the "number" cards
        }

        if (game.getHand().count(TOP) + game.getBoard().count(TOP) < 2 && cards.count(TOP) > 0) {
            return index of one TOP within the "number" cards
        }

        if (game.getHand().count(JACE) == 0 && cards.count(JACE) > 0) {
            return index of one JACE  within the "number" cards
        }

        if (game.getHand().count(SCULPTOR) == 0 && game.getHand().count(HELM) == 0 &&cards.count(HELM) > 0) {
            return index of one HELM within the "number" cards
        }

        if (game.getHand().count(SCULPTOR) == 0 && game.getHand().count(HELM) == 0 &&cards.count(SCULPTOR) > 0) {
            return index of one SCULPTOR within the "number" cards
        }
        
        if (cards.count(MANA_PRODUCERS) > 0) {
            return index of one MANA_PRODUCERS within the "number" cards
        }

        return 0
    }



    boolean canPay(Mana cost) {
        // potential mana pool is current pool + untapped lands + petals on board
        Mana potentialPool = game.getPool()
                .plus(Mana.of(0, 0, game.countUntapped(MANA_PRODUCERS), 0, 0, 0));
        return potentialPool.contains(cost);
    }

    void preparePool(Mana cost) {
        while (!game.canPay(cost)) {
            Optional<String> producer = game.findFirstUntapped(DESERT, ISLAND);
            if (producer.isPresent()) {
                if (producer.get().equals(DESERT)) {
                    game.tapLandForMana(producer.get(), 1);
                } else {
                    // a land
                    game.tapLandForMana(producer.get(), U);
                }
            } else {
                // can't preparePool !!!
                return;
            }
        }
    }

     @Override
    public String checkWin() {
        super.checkWin();
        if  (game.getBoard().count(TOP) +  game.getHand().count(TOP) > 1 && game.getHand().count(Jace) > 0 &&  game.getHand().count(HELM) + game.getHand().count(SCULPTOR) > 0 + game.canPay(FOUR)) {
            return "💀"
        }
        return null;
    }
}
