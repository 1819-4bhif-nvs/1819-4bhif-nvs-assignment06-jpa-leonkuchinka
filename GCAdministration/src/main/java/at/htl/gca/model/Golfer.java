package at.htl.gca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.inject.Named;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQuery(name="Golfer.findall", query="select g from Golfer g")
@DiscriminatorColumn
public abstract class Golfer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String name;
    protected double hcp;
    protected int age;
    @ManyToMany(mappedBy = "players", cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JsonIgnore
    @XmlTransient
    protected List<TeeTime> teeTimes;

    @JsonIgnore
    @XmlTransient
    @Column(name="DTYPE", insertable = false, updatable = false)
    private String dType;
    //region Constructors
    public Golfer() {
        teeTimes = new LinkedList<>();
    }

    public Golfer(String name, double hcp, int age) {
        this();
        this.name = name;
        this.hcp = hcp;
        this.age = age;
    }
    //endregion

    //region Getter and Setter
    public void addTeeTime(TeeTime teeTime){
        if(!teeTimes.contains(teeTime))
            teeTimes.add(teeTime);
        if(!teeTime.getPlayers().contains(this))
            teeTime.addPlayer(this);

    }
    public void removeTeeTime(TeeTime teeTime){
        if(teeTimes.contains(teeTime))
            teeTimes.remove(teeTime);
        if(teeTime.getPlayers().contains(this))
            teeTime.removePlayer(this);
    }
    public List<TeeTime> getTeeTimes() {
        return teeTimes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public double getHcp() {
        return hcp;
    }

    public void setHcp(double hcp) {
        this.hcp = hcp;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //endregion
}
