package nonsense.hamsterrun.ratcontroll;

import nonsense.hamsterrun.env.Rat;

import java.util.List;

public interface RatsProvider {

    public List<Rat> getRats();

    public RatsController.RatControl getRatControl(Rat rat);

    void swap(Rat rat);

    void kill();
}
