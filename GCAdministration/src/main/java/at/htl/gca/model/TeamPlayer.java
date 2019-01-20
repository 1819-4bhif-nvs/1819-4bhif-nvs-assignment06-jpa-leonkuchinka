package at.htl.gca.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@NamedQuery(name="TeamPlayer.findall", query = "select p from TeamPlayer p")
public class TeamPlayer extends Golfer {
    private Boolean isRegularPlayer;
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
