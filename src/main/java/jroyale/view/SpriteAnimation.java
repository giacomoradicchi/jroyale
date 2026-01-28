package jroyale.view;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public class SpriteAnimation {
    
    private final List<Image> frames;

    public SpriteAnimation() {
        frames = new ArrayList<>();
    }

    public void addSprite(Image e) {
        frames.add(e);
    }

    public Image getFrame(int index) {
        return frames.get(index);
    }

    public int size() {
        return frames.size();
    }

}
