package ca.bcit.comp2522.gameproject.barkingBombs;

import java.util.ArrayList;
import java.util.List;

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

    private void validateName(final String name)
    {
        if(name == null || name.isBlank())
        {
            throw new IllegalArgumentException("Name cannot be null or blank.");
        }
    }

    private List<Card> newHandOfCards()
    {
        final List<Card> handOfCards;

        handOfCards = new ArrayList<>();
        handOfCards.add(new DefusalCard());

        for(int i = 0; i < DEFAULT_NUM_CARDS; i++)
        {
            
        }

        return handOfCards;
    }


}
