package at.htl.gca.model;

import at.htl.gca.converter.XMLAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
@Entity
@NamedQueries({
        @NamedQuery(name = "TeeTime.findAll", query = "select distinct t from TeeTime t join fetch t.players")
})
@XmlAccessorType(XmlAccessType.FIELD)
public class TeeTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @XmlJavaTypeAdapter(XMLAdapter.class)
    private LocalDateTime time;
    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(
            name="GOLFER_TEETIME",
            joinColumns = @JoinColumn(name = "TEETIMEID"),
            inverseJoinColumns = @JoinColumn(name = "GOLFERID")
    )
    private List<Golfer> players;

    //region Constructors
    public TeeTime(LocalDateTime time) {
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

    /*public LocalDate getTime() {
        return time;
    }*/

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    //endregion
}
