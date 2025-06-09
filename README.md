# Guinea Pig run through maze

But in reallity you play with guinea pigs.

```
cd HamsterRun
DISPLAY=:0 mvn clean install
java -cp ./target/hamsterrun-1.0-SNAPSHOT.jar:$HOME/.m2/repository/com/google/code/gson/gson/2.13.1/gson-2.13.1.jar nonsense.hamsterrun.setup.SetupWindow
```

Without any argumetn setup window will pop up.

The cmdline launcher takes a lot of args, see: https://github.com/judovana/HamsterRun/blob/main/src/main/java/nonsense/hamsterrun/Main.java#L45-L135ji

There exists czech mutation, feel free to force it via `LANG=cs_CZ.UTF-8`


The goal of the game is to get as much escaped Guine Pigs back to the golden cage (https://github.com/judovana/HamsterRun/blob/main/src/main/resources/nonsense/hamsterrun/sprites/gate.png) after you get fat by eating a lot of vegetables, and collecting several golden keys (https://github.com/judovana/HamsterRun/blob/main/src/main/resources/nonsense/hamsterrun/sprites/aliens/key.gif), and survivced all the traps.

Note that it is collaborative game (AI is not exactly understanding this), and you all win, only after ALL guinea pigs are back in cage. The keys are comons shared proeprty, but score from vegetable is individual.
The game is pretty hard on defaults - wi recomend to disable most of the obstacles, and enemies for first tries. Also reduce the necessesary amount of keys and score.

The only way how to lost, is that all enemies will be repalced by immortal ghosts. In such case no new key will appear, and if you do not have necessary amount, you can not win.

## Movement
Do not hold the keys. It will immediately swap you to turbo mode. Instead, one press on key, will change the direction and the rat is then going in that direction, untill it wishes. You can not stop it, it stops on it own. If you press again in the same dirrection, your speed rises. After several presses (or longer hold) it reaches super speed (there is sound for that border), and in that speed you can miss also obstacless/enemies, however some traps are hurting much *much* **much** more. So carefully. Of course in turbo speed you ca not eat. It is more probable you will stop on vegetable then elsewhere. Haveyou ever had guinea pigs? they mve exactly like this...
