package nonsense.hamsterrun.env.aliens;

//flying over screen, targeting rats, walls do not stop
public abstract class Hawk extends MovingOne {

    @Override
    public boolean mustBeInCorridor() {
        return false;
    }
}
