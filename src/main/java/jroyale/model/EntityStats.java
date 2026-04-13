package jroyale.model;

import jroyale.model.troops.Troop.MeleeRange;
import jroyale.model.troops.Troop.Speed;

/* 
    private static final String NAME = "Giant";
    private static final Speed SPEED = Speed.SLOW;
    private static final MeleeRange MELEE = MeleeRange.LONG;
    private static Map<State, Integer> totalAnimationSteps;
    private static final double COLLISION_RADIUS = 0.75;
    private static final int FPS_ANIMATION = 10;
    private static final long LOAD_TIME = (long) (1.5 * 1_000_000_000);
    private static final int HIT_FRAME = 7;
    private static final int HITPOINTS = 1600;
    private static final int DAMAGE = 300; 
    
*/

public record EntityStats (
    String name, 
    Speed speed,
    MeleeRange melee,
    double collisionRadius,
    long loadTime,
    int hitPoints,
    int damage
) {}

// record is a type of class where his attribs are immutable. ideal for map keys.
