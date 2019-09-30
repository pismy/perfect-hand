package org.mtgpeasant.perfectdeck.goldfish;

import lombok.Getter;
import org.mtgpeasant.perfectdeck.common.cards.Cards;

@Getter
public class Game {
    public enum Area {
        hand(false), library_top(true), library_bottom(false), board(false), exile(false), graveyard(false);

        private boolean top;

        Area(boolean top) {
            this.top = top;
        }

        void put(Game game, String card) {
            if (top) {
                cards(game).addFirst(card);
            } else {
                cards(game).addLast(card);
            }
        }

        Cards cards(Game game) {
            switch (this) {
                case hand:
                    return game.hand;
                case library_top:
                case library_bottom:
                    return game.library;
                case board:
                    return game.board;
                case exile:
                    return game.exile;
                case graveyard:
                    return game.graveyard;
                default:
                    return null;
            }
        }
    }

    private int currentTurn = 1;
    private int opponentLife = 20;
    private int opponentPoisonCounters = 0;
    private boolean landed = false;

    private Cards library;
    private Cards hand;
    private Cards board = Cards.none();
    private Cards exile = Cards.none();
    private Cards graveyard = Cards.none();

    Game(Cards library, Cards hand) {
        this.library = library;
        this.hand = hand;
    }

    private Cards tapped = Cards.none();
    private Mana pool = Mana.zero();

    Game startNextTurn() {
        currentTurn++;
        landed = false;
        emptyPool();
        return this;
    }

    Game emptyPool() {
        pool = Mana.zero();
        return this;
    }

    public Game pay(Mana mana) {
        if (!has(mana)) {
            throw new IllegalMoveException("Can't pay " + mana + ": not enough mana in pool (" + pool + ")");
        }
        pool = pool.minus(mana);
        return this;
    }

    public Game add(Mana mana) {
        pool = pool.plus(mana);
        return this;
    }

    public boolean has(Mana mana) {
        return pool.contains(mana);
    }

    public Game tap(String cardName) {
        int countOnBoard = board.count(cardName);
        if (countOnBoard == 0) {
            throw new IllegalMoveException("Can't tap [" + cardName + "]: not on board");
        }
        int countTapped = tapped.count(cardName);
        if (countTapped >= countOnBoard) {
            throw new IllegalMoveException("Can't tap [" + cardName + "]: already tapped");
        }
        tapped.add(cardName);
        return this;
    }

    public Game untap(String cardName) {
        if (!board.contains(cardName)) {
            throw new IllegalMoveException("Can't untap [" + cardName + "]: not on board");
        }
        tapped.remove(cardName);
        return this;
    }

    public Game untapAll() {
        tapped.clear();
        return this;
    }

    public Game land(String cardName) {
        if (!hand.contains(cardName)) {
            throw new IllegalMoveException("Can't land [" + cardName + "]: not in hand");
        }
        if (landed) {
            throw new IllegalMoveException("Can't land [" + cardName + "]: can't land twice the same turn");
        }
        hand.remove(cardName);
        board.add(cardName);
        landed = true;
        return this;
    }

    public Game damageOpponent(int damage) {
        opponentLife -= damage;
        return this;
    }

    public Game poisonOpponent(int counters) {
        opponentPoisonCounters += counters;
        return this;
    }

    public Game shuffleLibrary() {
        library = library.shuffle();
        return this;
    }

    public Game draw(int cards) {
        if (library.size() < cards) {
            throw new GameLostException("Can't draw [" + cards + "]: not enough cards");
        }
        for (int i = 0; i < cards; i++) {
            hand.add(library.draw());
        }
        return this;
    }

    public Game move(String cardName, Area from, Area to) {
        if (!from.cards(this).contains(cardName)) {
            throw new IllegalMoveException("Can't move [" + cardName + "]: not in " + from);
        }
        from.cards(this).remove(cardName);
        to.put(this, cardName);
        return this;
    }

    public Game cast(String cardName, Area from, Area to, Mana mana) {
        if (!has(mana)) {
            throw new IllegalMoveException("Can't cast [" + cardName + "]: not enough mana");
        }
        pay(mana);
        return move(cardName, from, to);
    }

    public Game castPermanent(String cardName, Mana mana) {
        return cast(cardName, Area.hand, Area.board, mana);
    }

    public Game castNonPermanent(String cardName, Mana mana) {
        return cast(cardName, Area.hand, Area.graveyard, mana);
    }

    public Game discard(String cardName) {
        if (!hand.contains(cardName)) {
            throw new IllegalMoveException("Can't discard [" + cardName + "]: not in hand");
        }
        hand.remove(cardName);
        graveyard.add(cardName);
        return this;
    }

    public Game sacrifice(String cardName) {
        if (!board.contains(cardName)) {
            throw new IllegalMoveException("Can't sacrifice [" + cardName + "]: not on board");
        }
        board.remove(cardName);
        graveyard.add(cardName);
        return this;
    }

    public Game destroy(String cardName) {
        if (!board.contains(cardName)) {
            throw new IllegalMoveException("Can't destroy [" + cardName + "]: not on board");
        }
        board.remove(cardName);
        graveyard.add(cardName);
        return this;
    }
}
