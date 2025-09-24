package jroyale.view;

import javafx.scene.image.Image;
import java.util.List;
import java.util.ArrayList;

public class TextureAtlas {

    private Image textureAtlas;

    // a TextureAtlas is an image with multiple textures. 
    // To extract a texture, it is necessary to define 
    // the bounds of the rectangle which contains it.

    private class SubTexture {
        private double sx; // the source rectangle's X coordinate position.
        private double sy; // the source rectangle's Y coordinate position.
        private double sw; // the source rectangle's width.
        private double sh; // the source rectangle's height.  
        
        SubTexture(double sx, double sy, double sw, double sh) {
            this.sx = sx;
            this.sy = sy;
            this.sw = sw;
            this.sh = sh;
        }
    }
    
    private List<SubTexture> textures = new ArrayList<>();

    public TextureAtlas(Image textureAtlas) {
        this.textureAtlas = textureAtlas;
    }

    public void addSubTexture(double sx, double sy, double sw, double sh) {
        textures.add(
            new SubTexture(sx, sy, sw, sh)
        );
    }

    public Image getFullImage() {
        return textureAtlas;
    }

    public double[] getSubTexture(int index) {
        SubTexture subTex = textures.get(index);
        return new double[] {subTex.sx, subTex.sy, subTex.sw, subTex.sh};
    }

    public double getWidth(int index) {
        return textures.get(index).sw;
    }

    public double getHeight(int index) {
        return textures.get(index).sh;
    }
}