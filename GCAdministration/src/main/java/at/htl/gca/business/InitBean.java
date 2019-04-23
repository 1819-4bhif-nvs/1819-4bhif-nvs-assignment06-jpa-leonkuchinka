package at.htl.gca.business;

import at.htl.gca.model.*;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        TeamPlayer leonKuchinka = new TeamPlayer("Leon Kuchinka", -1.7, 17, true, 2015);
        em.persist(leonKuchinka);
        TeamPlayer christophBleier = new TeamPlayer("Christoph Bleier", 2.4, 17, true, 2015);
        em.persist(christophBleier);
        TeamPlayer philipPfeifenberger = new TeamPlayer("Philip Pfeifenberger", -2.7, 22, false, 2011);
        em.persist(philipPfeifenberger);
        TeamPlayer michaelFehringer = new TeamPlayer("Michael Fehringer", -0.8, 23, true, 2010);
        em.persist(michaelFehringer);
        TeamPlayer alexBinder = new TeamPlayer("Alex Binder", -1.6, 23, true, 2009);
        em.persist(alexBinder);
        HobbyPlayer julianNobis = new HobbyPlayer("Julian Nobis", -54, 18, false);
        em.persist(julianNobis);

        Team youthTeam = new Team("Youth Team");
        youthTeam.addMember(leonKuchinka);
        youthTeam.addMember(christophBleier);
        em.persist(youthTeam);

        Team mensTeam = new Team("Mens Team");
        mensTeam.addMember(philipPfeifenberger);
        mensTeam.addMember(michaelFehringer);
        mensTeam.addMember(alexBinder);
        em.persist(mensTeam);


        TeeTime firstFlight = new TeeTime(LocalDateTime.parse("2019-04-23T08:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        firstFlight.addPlayer(leonKuchinka);
        firstFlight.addPlayer(christophBleier);
        em.persist(firstFlight);

        TeeTime secondFlight = new TeeTime(LocalDateTime.parse("2019-04-23T12:30:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        secondFlight.addPlayer(leonKuchinka);
        secondFlight.addPlayer(michaelFehringer);
        secondFlight.addPlayer(philipPfeifenberger);
        secondFlight.addPlayer(julianNobis);
        em.persist(secondFlight);


    }
}
