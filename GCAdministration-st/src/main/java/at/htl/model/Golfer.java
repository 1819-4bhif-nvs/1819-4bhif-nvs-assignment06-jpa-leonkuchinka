package at.htl.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NamedQueries({
        @NamedQuery(name = "Golfer.findAll", query = "select g from Golfer g"),
        @NamedQuery(name = "Golfer.findByName", query = "select g from Golfer g where g.name like ?1"),
        @NamedQuery(name = "Golfer.findById", query = "select g from Golfer g where g.id = ?1")
})
public class Golfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String name;
    protected double hcp;
    protected int age;

    public Golfer() {
    }

    public Golfer(String name, double hcp, int age) {
        this.name = name;
        this.hcp = hcp;
        this.age = age;
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
}
