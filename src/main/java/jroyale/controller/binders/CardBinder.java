package jroyale.controller.binders;

import java.util.EnumMap;
import java.util.Map;

import jroyale.model.cards.Card;
import jroyale.model.cards.GiantCard;
import jroyale.model.cards.MiniPekkaCard;
import jroyale.model.cards.PekkaCard;
import jroyale.model.cards.SkeletonArmyCard;
import jroyale.model.cards.SkeletonCard;
import jroyale.model.cards.ValkyrieCard;
import jroyale.utils.Enums.EntityType;

public class CardBinder {

    private static CardBinder instance = null;
    
    private final static Map<EntityType, Card> cardBinder = new EnumMap<>(EntityType.class);

    private CardBinder() {
        // troops
        cardBinder.put(EntityType.MINIPEKKA, MiniPekkaCard.getInstance());
        cardBinder.put(EntityType.GIANT, GiantCard.getInstance());
        cardBinder.put(EntityType.SKELETONS, SkeletonCard.getInstance());
        cardBinder.put(EntityType.SKELETON_ARMY, SkeletonArmyCard.getInstance());
        cardBinder.put(EntityType.PEKKA, PekkaCard.getInstance());
        cardBinder.put(EntityType.VALKYRIE, ValkyrieCard.getInstance());
    }

    public Card getCardInstance(EntityType type) {
        Card card = cardBinder.get(type);
        
        return card;
    }

    public static CardBinder getInstance() {
        if (instance == null) {
            instance = new CardBinder();
        }
        return instance;
    }
}