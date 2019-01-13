package at.htl.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
@Entity
@NamedQueries({
        @NamedQuery(name = "TeeTime.findAll", query = "select t from TeeTime t")
})
public class TeeTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    private LocalDate time;
    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Golfer> players;

    //region Constructors
    public TeeTime(LocalDate time) {
        this();
        this.time = time;
    }

    public TeeTime() {
        players = new LinkedList<>();
    }
    //endregion

    //region Getter and Setter

    public Long getId() {
        return id;
    }

    public List<Golfer> getPlayers() {
        return players;
    }

    public void addPlayer(Golfer g){
        if(!players.contains(g))
            players.add(g);
        if(!g.getTeeTimes().contains(this))
            g.addTeeTime(this);
    }

    public void removePlayer(Golfer g){
        if(players.contains(g))
            players.remove(g);
        if(g.getTeeTimes().contains(this))
            g.removeTeeTime(this);
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }
    //endregion
}
