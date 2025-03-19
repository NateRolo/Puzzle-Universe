package ca.bcit.comp2522.gameproject.barkingBombs;

import java.util.ArrayList;
import java.util.List;


/*
Computer opponents:
1️⃣ Always draw a card at the start of its turn.
2️⃣ Play a Defusal Card if an Exploding Kitten is drawn.
3️⃣ Randomly play action cards (Skip, Attack, Favor, Shuffle) based on probability.
4️⃣ If AI has a Favor card, randomly pick a player to demand a card from.
5️⃣ If AI has a Nope card, use it 50% of the time against the player’s action card.
6️⃣ If AI has See the Future, it will randomly decide to use it (20-40% probability).
7️⃣ AI does NOT need to "bluff" or make advanced strategic moves—it just follows simple logic.
 */

class Player
{
    private static final int DEFAULT_NUM_CARDS = 6;

    final String name;
    final List<Card> cardsInHand;

    boolean isAlive;

    Player(final String name)
    {
        validateName(name);

        this.name = name;
        this.isAlive = true;
        cardsInHand = newHandOfCards();
    }

    private static void drawCard(final Player player,
                                 final int numCards)
    {
        // "pickup" card from deck
    }

    private static void validateName(final String name)
    {
        if(name == null || name.isBlank())
        {
            throw new IllegalArgumentException("Name cannot be null or blank.");
        }
    }

    private static List<Card> newHandOfCards()
    {
        final List<Card> handOfCards;

        handOfCards = new ArrayList<>();
        handOfCards.add(new DefusalCard()); // add defuse card

        for(int i = 0; i < DEFAULT_NUM_CARDS; i++)
        {
            // add random set of 6 other cards
        }

        return handOfCards;
    }


}
