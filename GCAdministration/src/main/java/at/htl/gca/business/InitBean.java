package at.htl.gca.business;

import at.htl.gca.model.Golfer;
import at.htl.gca.model.HobbyPlayer;
import at.htl.gca.model.TeamPlayer;
import at.htl.gca.model.TeeTime;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Startup
@Singleton
public class InitBean {

    @PersistenceContext
    EntityManager em;
    public InitBean() {
        
    }

    @PostConstruct
    private void init(){
        System.err.println("------init------");

        Golfer leonKuchinka = new TeamPlayer("Leon Kuchinka", -1.7, 17, true, 2015);
        em.persist(leonKuchinka);
        Golfer christophBleier = new TeamPlayer("Christoph Bleier", 2.7, 17, true, 2015);
        em.persist(christophBleier);
        Golfer philipPfeifenberger = new TeamPlayer("Philip Pfeifenberger", -2.7, 22, false, 2011);
        em.persist(philipPfeifenberger);
        Golfer michaelFehringer = new TeamPlayer("Michael Fehringer", 0.8, 23, true, 2010);
        em.persist(michaelFehringer);
        Golfer alexBinder = new TeamPlayer("Alex Binder", 1.6, 23, true, 2009);
        em.persist(alexBinder);
        Golfer test = new HobbyPlayer("Test", 1, 99, false);
        em.persist(test);
        List<Golfer> firstFlight = new LinkedList<>();
        firstFlight.add(leonKuchinka);
        firstFlight.add(christophBleier);
        em.persist(new TeeTime(LocalDate.of(2018, 11, 26), firstFlight));
        List<Golfer> secondFlight = new LinkedList<>();
        secondFlight.add(philipPfeifenberger);
        secondFlight.add(michaelFehringer);
        secondFlight.add(alexBinder);
        secondFlight.add(leonKuchinka);
        em.persist(new TeeTime(LocalDate.of(2018, 11, 27), secondFlight));
    }
}
