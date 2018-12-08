package at.htl.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
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

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private List<Golfer> players;

    public List<Golfer> getPlayers() {
        return players;
    }

    public void setPlayers(List<Golfer> players) {
        this.players = players;
    }

    public TeeTime(LocalDate time, List<Golfer> players) {
        this.time = time;
        this.players = players;
    }

    public TeeTime() {
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }
}
