package at.htl.model;

import javax.persistence.Entity;

@Entity
public class TeamPlayer extends Golfer {
    private boolean isRegularPlayer;
    private int joined;

    public TeamPlayer() {
    }

    public TeamPlayer(String name, double hcp, int age, boolean isRegularPlayer, int joined) {
        super(name, hcp, age);
        this.isRegularPlayer = isRegularPlayer;
        this.joined = joined;
    }

    public boolean isRegularPlayer() {
        return isRegularPlayer;
    }

    public void setRegularPlayer(boolean regularPlayer) {
        isRegularPlayer = regularPlayer;
    }

    public int getJoined() {
        return joined;
    }

    public void setJoined(int joined) {
        this.joined = joined;
    }
}
