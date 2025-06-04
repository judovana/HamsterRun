# HamsterRun

But in reallity you play with guinea pigs.

```
cd HamsterRun
DISPLAY=:0 mvn clean install
java -cp ./target/hamsterrun-1.0-SNAPSHOT.jar:$HOME/.m2/repository/com/google/code/gson/gson/2.13.1/gson-2.13.1.jar nonsense.hamsterrun.setup.SetupWindow
```

Without any argumetn setup window will pop up.

The cmdline launcher takes a lot of args, see: https://github.com/judovana/HamsterRun/blob/main/src/main/java/nonsense/hamsterrun/Main.java#L45-L135ji

There exists czech mutation, feel free to force it via `LANG=cs_CZ.UTF-8`
