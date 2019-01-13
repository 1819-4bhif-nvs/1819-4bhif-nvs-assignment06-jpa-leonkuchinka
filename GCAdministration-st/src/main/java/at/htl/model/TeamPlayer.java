package at.htl.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name="TeamPlayer.findall", query = "select p from TeamPlayer p")
public class TeamPlayer extends Golfer {
    private boolean isRegularPlayer;
    private int joined;



    @ManyToOne(fetch = FetchType.EAGER)
    private Team team;

    public TeamPlayer() {
    }

    public TeamPlayer(String name, double hcp, int age, boolean isRegularPlayer, int joined) {
        super(name, hcp, age);
        this.isRegularPlayer = isRegularPlayer;
        this.joined = joined;
    }

    public void setTeam(Team t){
        if(team != null)
            team.removeMember(this);
        team = t;
    }
    public Team getTeam() {
        return team;
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
