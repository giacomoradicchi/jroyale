package jroyale.view;

import jroyale.utils.Enums.Side;
import jroyale.utils.Enums.State;

public record AnimationKey(Side side, State state, int direction) {}
// record is a type of class where his attribs are immutable. ideal for map keys.
