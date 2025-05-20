package nonsense.hamsterrun.env.aliens;


import java.awt.Point;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.RatActions;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.env.traps.AnimationCounrer;
import nonsense.hamsterrun.sprites.SpritesProvider;

//going up+down xor left and down, removing energy from all it meets
//must be faster then rats... maybe different thread then repl? Maybe special % in repl?
public  class Boulder extends MovingOne {

    private final int maxspeed;
    private Point lastCoord = new Point(-1,-1);
    private int lastCoordCounter = -1;

    public Boulder() {
        this.maxspeed = seed.nextInt(2)+4;
        this.anim = new AnimationCounrer(1000);
        anim.reset(seed.nextInt(SpritesProvider.getAlienSize(getSkin())));
    }

    @Override
    protected boolean returnOnSalat(World world) {
        return false;
    }

    @Override
    public void selfAct(World world) {
        this.speed=maxspeed;
        this.action = RatActions.WALK;
        move(world);
        anim.addLimited();
        if (lastCoord.equals(getUniversalCoords())) {
            lastCoordCounter++;
        } else {
            lastCoord = getUniversalCoords();
            lastCoordCounter=0;
        }
        if (lastCoordCounter>relativeSizes*2+1){
            this.direction=this.direction.opposite();
        }
    }



    @Override
    protected void adjustSpeedBeforeActionDirection() {

    }


    @Override
    protected String getSkin() {
        return "boulder";
    }

    @Override
    public void interact(Rat rat) {
        rat.getSounds().addToHarmQueue(SoundsBuffer.piskFire);
        rat.adjustScore(-500);
    }
}
