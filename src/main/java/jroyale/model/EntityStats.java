package jroyale.model;

import jroyale.model.troops.Troop.MeleeRange;
import jroyale.model.troops.Troop.Speed;

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
