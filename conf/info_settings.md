# Settings Configuration Guide

This document explains the technical parameters used in the settings JSON files. Modifying these values will directly affect the behavior and balance of the units in the game.

## Field Descriptions

### `difficulty`
- **Type**: String
- **Values**: `BASIC`, `STANDARD`, `EXPERT`, `MASTER`
- **Default**: `STANDARD`
- **Description**: Defines how smart the AI will be.

### `maxTimeSec`
- **Type**: int 
- **Example**: `180` (3 minutes)
- **Description**: The duration in seconds of the match.

---

## Example Snippet
```json
{
    "difficulty": "STANDARD",
    "maxTimeSec": 180
}