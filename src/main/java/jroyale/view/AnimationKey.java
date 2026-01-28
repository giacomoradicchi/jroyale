package jroyale.view;

import jroyale.shared.Enums.Side;
import jroyale.shared.Enums.State;

public record AnimationKey(Side side, State state, int direction) {}
// record is a type of class where his attribs are immutable. ideal for map keys.
