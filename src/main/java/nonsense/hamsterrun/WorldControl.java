package nonsense.hamsterrun;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import nonsense.hamsterrun.env.World;

public class WorldControl {

    //idea is:
    //new panel in swing join/create (and how many rats to await) + classical urls/ports
    //then master will add rats with "NetworkController"
    //the master will send serialized world to client
    //client wills end keystrokes/mouse movement *results* as declared in RatController
    //Currently it StackOverflow in infinite recursion


    public void sendCatched(World world) {
        try {
            send(world);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void send(World world) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Class.class, new BaseConfig.ClassTypeAdapter());
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String json = gson.toJson(world, World.class);
        Files.writeString(new File("/home/jvanek/Desktop/test.json").toPath(), json);
    }
}
