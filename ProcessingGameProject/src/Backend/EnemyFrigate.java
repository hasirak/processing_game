package Backend;

/**
 * An enemy frigate. Small and fast.
 *
 * @author Kristian Honningsvag.
 */
public class EnemyFrigate extends Actor {

    Player player;

    /**
     * Constructor.
     */
    public EnemyFrigate(double positionX, double positionY, double speedX, double speedY, Player player) {
        super(positionX, positionY, speedX, speedY);

        speedLimit = 8;
        accelerationX = 0.45f;
        accelerationY = 0.45f;
        airResistance = 0.06f;
        bounceAmplifier = 1.2f;
        hitPoints = 5;

        this.player = player;
    }

    /**
     * Call this function for each turn in the simulation.
     */
    @Override
    public void act() {
        approachPlayer();
        super.act();
    }

    /**
     * Accelerate towards the player.
     */
    private void approachPlayer() {

        double xVector = player.getPositionX() - this.positionX;
        double yVector = player.getPositionY() - this.positionY;
        double targetAngle = NumberCruncher.calculateAngle(xVector, yVector);

        if (speedT < speedLimit) {
            speedX = speedX + (accelerationX * Math.cos(targetAngle));
            speedY = speedY + (accelerationY * Math.sin(targetAngle));
        }
    }
}
