package at.htl.gca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String teamName;
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    @JsonIgnore
    @XmlTransient
    private List<TeamPlayer> teamMembers;

    //region Constructor
    public Team(String name){
        teamName = name;
    }
    public Team(){
    }
    //endregion

    //region Getter and Setter
    public void addMember(TeamPlayer p){
        if(teamMembers == null)
            teamMembers = new LinkedList<>();
        p.setTeam(this);
        teamMembers.add(p);
    }
    public void removeMember(TeamPlayer p){
        if(teamMembers != null && teamMembers.contains(p)){
            teamMembers.remove(p);
            p.setTeam(null);
        }
    }
    public String getTeamName() {
        return teamName;
    }

    public List<TeamPlayer> getTeamMembers() {
        return teamMembers;
    }
    //endregion
}
