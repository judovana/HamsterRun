package nonsense.hamsterrun.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Objects;

public class HelloCmd implements Serializable {
    public static final int MAX_TTL = 5;
    private static final GsonBuilder builder = new GsonBuilder();
    private static Gson gson = builder.create();

    private final String id;
    private final int port;
    private final String rat;
    private final String name;
    private transient int ttl = MAX_TTL;

    public HelloCmd(String id, int port, String rat, String name) {
        this.id = id;
        this.port = port;
        this.rat = rat;
        this.name = name;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public void ttl() {
        this.ttl = ttl -1;
    }

    public String getName() {
        return name;
    }

    public String getRat() {
        return rat;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id + ":" + port +
                " " + rat +
                " " + name + ":" + ttl;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HelloCmd helloCmd = (HelloCmd) o;
        return port == helloCmd.port && Objects.equals(id, helloCmd.id) && Objects.equals(rat, helloCmd.rat) && Objects.equals(name, helloCmd.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, port, rat, name);
    }

    public void send(PrintWriter ps) {
        String json = gson.toJson(this);
        ps.println(Commands.HANDSHAKE + json);
    }

    public static HelloCmd read(String s) {
        HelloCmd cmd = gson.fromJson(s, HelloCmd.class);
        return cmd;
    }
}
