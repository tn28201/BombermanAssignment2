package uet.oop.bomberman.entities.enemy.ai;

public class AILow extends AI {

    @Override
    public int calculateDirection() {
        return random.nextInt();
    }
}
