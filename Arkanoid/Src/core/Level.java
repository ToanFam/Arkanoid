package Arkanoid.Src.core;

import com.fasterxml.jackson.databind.ObjectMapper;

import Arkanoid.Src.entities.Bricks.*;
import java.io.File;
import java.util.ArrayList;

public class Level {
    private ArrayList<Brick> bricks = new ArrayList<>();
    private double ballSpeed;
    private double paddleWidth;

    public Level(String jsonPath) {
        loadFromJSON(jsonPath);
    }

    private void loadFromJSON(String filename) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            LevelData data = mapper.readValue(new File(filename), LevelData.class);

            this.ballSpeed = data.ballSpeed;
            this.paddleWidth = data.paddleWidth;

            int brickWidth = 43;
            int brickHeight = 21;
            int startX = 18 + 15, startY = 170;

            for (int row = 0; row < data.rows; row++) {
                String line = data.layout[row];
                for (int col = 0; col < data.cols; col++) {
                    char c = line.charAt(col);

                    switch (c) {
                        case '0':
                            bricks.add(new UnbreakableBrick(startX + col * brickWidth, startY + row * brickHeight, brickWidth, brickHeight));
                            break;
                        case '1':
                            bricks.add(new NormalBrick(startX + col * brickWidth, startY + row * brickHeight, brickWidth, brickHeight));
                            break;
                        case '2':
                            bricks.add(new StrongBrick(startX + col * brickWidth, startY + row * brickHeight, brickWidth, brickHeight));
                            break;
                        case '3':
                            bricks.add(new ExplosiveBrick(startX + col * brickWidth, startY + row * brickHeight, brickWidth, brickHeight));
                            break;
                        case '4':
                            bricks.add(new PowerBrick(startX + col * brickWidth, startY + row * brickHeight, brickWidth, brickHeight));
                            break;

                        default:
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Brick> getBricks() { return bricks; }
    public double getBallSpeed() { return ballSpeed; }
    public double getPaddleWidth() { return paddleWidth; }
}
