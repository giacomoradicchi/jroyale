# Troop Statistics Configuration Guide

This document explains the technical parameters used in the troop JSON files. Modifying these values will directly affect the behavior and balance of the units in the game.

## Field Descriptions

### `speed`
- **Type**: String (Enum)
- **Values**: `SLOW`, `MEDIUM`, `FAST`, `VERY_FAST`
- **Description**: Defines how quickly the unit moves across the battlefield.

### `meleeRange`
- **Type**: String (Enum)
- **Values**: `SHORT`, `MEDIUM`, `LONG`
- **Description**: Determines the maximum distance (in tiles) from which the troop can initiate an attack. 

### `loadTime`
- **Type**: Float (Seconds)
- **Example**: `1.67`
- **Description**: Also known as "Hit Speed." It represents the interval (in seconds) between two consecutive attacks. 
    - *Lower is faster.* A value of `1.0` means the troop attacks every second.

### `hitPoints`
- **Type**: Integer
- **Description**: The total health of the unit. When this reaches 0, the unit is destroyed.

### `damage`
- **Type**: Integer
- **Description**: The amount of health points deducted from the target with a single hit. 
    - **Note**: To calculate **DPS** (Damage Per Second), use the formula: `damage / loadTime`.

### `elixirCost`
- **Type**: Byte 
- **Description**: The elixir cost of the troop (from 0 to 10). 

---

## Example Snippet
```json
{
    "speed": "FAST",
    "meleeRange": "MEDIUM",
    "loadTime": 1.67,
    "hitPoints": 670,
    "damage": 67,
    "elixirCost": 6
}