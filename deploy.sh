DISPLAY=:0 mvn clean install 
scp $HOME/.m2/repository/com/google/code/gson/gson/2.13.1/gson-2.13.1.jar  kino.local:/$HOME/gson-2.13.1.jar
scp $HOME/git/HamsterRun/target/hamsterrun-1.0-SNAPSHOT.jar  kino.local:/$HOME/hamsterrun-1.0-SNAPSHOT.jar

