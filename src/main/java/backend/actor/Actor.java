package backend.actor;

import backend.GameEngine;
import backend.NumberCruncher;
import userinterface.Drawable;
import userinterface.GUIHandler;

/**
 * Super class for all actors.
 *
 * @author Kristian Honningsvag.
 */
public abstract class Actor implements Drawable {

    // Position.
    protected double positionX;     // pixels
    protected double positionY;     // pixels

    // Direction.
    protected double heading;       // radians
    protected double course;        // radians    (derived value)

    // Speed.
    protected double speedX;        // pixels/ms
    protected double speedY;        // pixels/ms
    protected double speedT;        // pixels/ms  (derived value)
    protected double speedLimit;    // pixels/ms

    // Acceleration.
    protected double accelerationX;   // pixels/ms^2
    protected double accelerationY;   // pixels/ms^2
    protected double drag;            // pixels/ms^2

    // Attributes.
    protected double hitBoxRadius;    // pixels
    protected double bounceModifier;
    protected double hitPoints;

    protected GameEngine gameEngine;
    protected GUIHandler guiHandler;

    /**
     * Constructor.
     *
     * @param positionX Actors X-position in pixels.
     * @param positionY Actors Y-position in pixels.
     * @param gameEngine
     * @param guiHandler
     */
    protected Actor(double positionX, double positionY, GameEngine gameEngine, GUIHandler guiHandler) {

        this.positionX = positionX;
        this.positionY = positionY;
        this.gameEngine = gameEngine;
        this.guiHandler = guiHandler;
    }

    @Override
    public abstract void draw();

    /**
     * Updates the actors state. Should be called once each cycle of the
     * simulation.
     *
     * @param timePassed Time passed since last simulation cycle in
     * milliseconds. Used in calculations.
     */
    public void act(double timePassed) {
        addFriction(timePassed);
        updatePosition(timePassed);
        updateVectors();
    }

    /**
     * Updates the actors total speed and direction of the current movement.
     */
    protected void updateVectors() {
        speedT = Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));
        course = NumberCruncher.calculateAngle(speedX, speedY);
    }
    
    /**
     * Quick Hack Added to be able to create the explosions for the fireball
     * 
     * @param isObject true=enemy false=wall
     */
    public void collide(boolean isObject){
        // Do nothing 
    }

    /**
     * Updates the actors position.
     */
    private void updatePosition(double timePassed) {
        positionX = positionX + speedX * timePassed;   // s = s0 + v*dt
        positionY = positionY + speedY * timePassed;
    }

    /**
     * Makes the actor gradually come to a halt if no acceleration is applied.
     */
    private void addFriction(double timePassed) {
        
        if (speedX > 0) {
            if (Math.abs(speedX) < drag * Math.cos(course) * timePassed) {
                speedX = 0;
            } else {
                speedX = speedX - drag * Math.cos(course) * timePassed;
            }
        }
        
        if (speedX < 0) {
            if (Math.abs(speedX) < drag * Math.cos(course) * timePassed) {
                speedX = 0;
            } else {
                speedX = speedX - drag * Math.cos(course) * timePassed;
            }
        }

        if (speedY > 0) {
            if (Math.abs(speedY) < drag * Math.sin(course) * timePassed) {
                speedY = 0;
            } else {
                speedY = speedY - drag * Math.sin(course) * timePassed;
            }
        }

        if (speedY < 0) {
            if (Math.abs(speedY) < drag * Math.sin(course) * timePassed) {
                speedY = 0;
            } else {
                speedY = speedY - drag * Math.sin(course) * timePassed;
            }
        }
    }

    /**
     * Accelerates the actor in the given direction.
     *
     * @param direction The direction of the acceleration.
     */
    public void accelerate(String direction, double timePassed) {

        // Accelerate upwards.
        if (direction.equalsIgnoreCase("up")) {
            if (speedY > (-speedLimit)) {
                speedY = speedY - accelerationY * timePassed;
            }
        }
        // Accelerate downwards.
        if (direction.equalsIgnoreCase("down")) {
            if (speedY < (speedLimit)) {
                speedY = speedY + accelerationY * timePassed;
            }
        }
        // Accelerate left.
        if (direction.equalsIgnoreCase("left")) {
            if (speedX > (-speedLimit)) {
                speedX = speedX - accelerationX * timePassed;
            }
        }
        // Accelerate right.
        if (direction.equalsIgnoreCase("right")) {
            if (speedX < (speedLimit)) {
                speedX = speedX + accelerationX * timePassed;
            }
        }
    }

    /**
     * Changes actor speed and direction upon collision with the outer walls.
     *
     * @param wall The wall that was hit.
     */
    public void wallBounce(String wall, double timePassed) {

        switch (wall) {
            // Right wall was hit.
            case "right":
                if (speedX > 0) {
                    speedX = speedX * (-bounceModifier);
//                    speedY = speedY * (bounceAmplifier);
                    act(timePassed);
                    break;
                }
            // Lower wall was hit.
            case "lower":
                if (speedY > 0) {
//                    speedX = speedX * (bounceAmplifier);
                    speedY = speedY * (-bounceModifier);
                    act(timePassed);
                    break;
                }
            // Left wall was hit.
            case "left":
                if (speedX < 0) {
                    speedX = speedX * (-bounceModifier);
//                    speedY = speedY * (bounceAmplifier);
                    act(timePassed);
                    break;
                }
            // Upper wall was hit.
            case "upper":
                if (speedY < 0) {
//                    speedX = speedX * (bounceAmplifier);
                    speedY = speedY * (-bounceModifier);
                    act(timePassed);
                    break;
                }
        }
        updateVectors();
    }

    // Getters.
    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getHeading() {
        return heading;
    }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public double getSpeedT() {
        return speedT;
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public double getCourse() {
        return course;
    }

    public double getAccelerationX() {
        return accelerationX;
    }

    public double getAccelerationY() {
        return accelerationY;
    }

    public double getHitBoxRadius() {
        return hitBoxRadius;
    }

    public double getDrag() {
        return drag;
    }

    public double getBounceModifier() {
        return bounceModifier;
    }

    public double getHitPoints() {
        return hitPoints;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    // Setters.
    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }

    public void setAccelerationX(double accelerationX) {
        this.accelerationX = accelerationX;
    }

    public void setAccelerationY(double accelerationY) {
        this.accelerationY = accelerationY;
    }

    public void setHitBoxRadius(double hitBoxRadius) {
        this.hitBoxRadius = hitBoxRadius;
    }

    public void setDrag(double drag) {
        this.drag = drag;
    }

    public void setBounceModifier(double bounceModifier) {
        this.bounceModifier = bounceModifier;
    }

    public void setHitPoints(double hitPoints) {
        this.hitPoints = hitPoints;
    }

}