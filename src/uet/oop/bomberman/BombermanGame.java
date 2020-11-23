package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.enemy.Balloon;
import uet.oop.bomberman.entities.enemy.Oneal;
import uet.oop.bomberman.entities.Item.Brick;
import uet.oop.bomberman.entities.Item.Grass;
import uet.oop.bomberman.entities.Item.Portal;
import uet.oop.bomberman.entities.Item.Wall;
import uet.oop.bomberman.graphics.Sprite;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BombermanGame extends Application  {
    public boolean w, s, d, a;
    public int WIDTH;
    public int HEIGHT;
    public static int Level;
    public final String fileName = "res/levels/Level1.txt";
    public List<String> map = new ArrayList<>();
    Entity bomberman = new Bomber(1, 2, Sprite.player_left.getFxImage());
    
    private GraphicsContext gc;
    private Canvas canvas;
    private final List<Entity> entities = new ArrayList<>();
    private final List<Entity> stillObjects = new ArrayList<>();
    private double SPEED = 0.1;

    //*********
    protected int _direction = 1;
    protected boolean _moving = true;

    protected int _animate = 0;
    protected final int MAX_ANIMATE = 7500;

    protected void animate() {
        if(_animate < MAX_ANIMATE) _animate++; else _animate = 0;
    }



    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        readLevelFromFile();
        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        Group root = new Group();
        root.getChildren().add(canvas);
        // Tao scene
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(keyPressed);
        scene.setOnKeyReleased(keyReleased);

        // Them scene vao stage
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Bomberman Game!");
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (w) {
                    _direction = 0;
                    animate();
                    bomberman.setY(bomberman.getY() - SPEED);
                    if (bomberman.getY() < 1) bomberman.setY(1);
                }
                if (a) {
                    _direction = 3;
                    animate();
                    bomberman.setX(bomberman.getX() - SPEED);
                    if (bomberman.getX() < 1) bomberman.setX(1);
                }
                if (s) {
                    animate();
                    _direction = 2;
                    bomberman.setY(bomberman.getY() + SPEED);
                    if (bomberman.getY() >= HEIGHT - 2) bomberman.setY(HEIGHT - 2);

                }
                if (d) {
                    animate();
                    _direction = 1;
                    bomberman.setX(bomberman.getX() + SPEED);
                    if (bomberman.getX() >= WIDTH - 2) bomberman.setX(WIDTH - 2);
                }
                render();
                update();
            }
        };
        timer.start();

        createMap();

        entities.add(bomberman);
    }

    public void readLevelFromFile() {
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fr);
            String currLine = bufferedReader.readLine();

            Level = Integer.parseInt(currLine.substring(0, 1));
            HEIGHT = Integer.parseInt(currLine.substring(2, 4));
            WIDTH = Integer.parseInt(currLine.substring(5, 7));

            while((currLine = bufferedReader.readLine()) != null) {
                map.add(currLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createMap() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Entity object;
                switch (map.get(j).charAt(i)) {
                    case '#':
                        object = new Wall(i, j, Sprite.wall.getFxImage());
                        break;
                    case 'x':
                        object = new Portal(i, j, Sprite.portal.getFxImage());
                        break;
                    case '1':
                        object = new Balloon(i, j, Sprite.balloom_left1.getFxImage());
                        break;
                    case '*':
                        object = new Brick(i, j, Sprite.brick.getFxImage());
                        break;
                    case '2':
                        object = new Oneal(i, j, Sprite.oneal_dead.getFxImage());
                        break;
                    default:
                        object = new Grass(i, j, Sprite.grass.getFxImage());
                        break;
                }
                stillObjects.add(object);
            }
        }
    }

    public void update() {
        entities.forEach(Entity::update);
    }

    public void render() {
        chooseSprite();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
    }

    private final EventHandler<KeyEvent> keyReleased = new EventHandler<KeyEvent>() {

        @Override
        public void handle(KeyEvent event) {
            switch (event.getCode()) {
                case W:
                    w = false;
                    break;
                case A:
                    a = false;
                    break;
                case S:
                    s = false;
                    break;
                case D:
                    d = false;
                    break;
            }
        }

    };

    private final EventHandler<KeyEvent> keyPressed = new EventHandler<KeyEvent>() {

        @Override
        public void handle(KeyEvent event) {
            switch (event.getCode()) {
                case W:
                    w = true;
                    break;
                case S:
                    s = true;
                    break;
                case A:
                    a = true;
                    break;
                case D:
                    d = true;
                    break;
            }
        }
    };


    private void chooseSprite() {
        switch (_direction) {
            case 0:
                bomberman.img = Sprite.player_up.getFxImage();
                if (_moving) {
                    bomberman.img = Sprite.movingSprite(Sprite.player_up_1.getFxImage(), Sprite.player_up_2.getFxImage(), _animate, 20);
                }
                break;
            case 2:
                bomberman.img = Sprite.player_down.getFxImage();
                if (_moving) {
                    bomberman.img = Sprite.movingSprite(Sprite.player_down_1.getFxImage(), Sprite.player_down_2.getFxImage(), _animate, 20);
                }
                break;
            case 3:
                bomberman.img = Sprite.player_left.getFxImage();
                if (_moving) {
                    bomberman.img = Sprite.movingSprite(Sprite.player_left_1.getFxImage(), Sprite.player_left_2.getFxImage(), _animate, 20);
                }
                break;
            default:
                bomberman.img = Sprite.player_right.getFxImage();
                if (_moving) {
                    bomberman.img = Sprite.movingSprite(Sprite.player_right_1.getFxImage(), Sprite.player_right_2.getFxImage(), _animate, 20);
                }
                break;
        }
    }




}
