package uet.oop.bomberman.entities;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import uet.oop.bomberman.input.Keyboard;

import java.util.List;

public class Bomber extends Entity {

    protected Keyboard input;
    public Bomber(double x, double y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {

    }

    
}
