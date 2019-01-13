package at.htl.gca.rest;

import at.htl.gca.model.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("golfer")
@Stateless //um sich nicht um Transaktionen kümmern zu müssen
public class GolferEndpoint {

    @PersistenceContext
    EntityManager em;

    @Path("findall/tp")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllTeamPlayer(){
        List<TeamPlayer> list = em.createNamedQuery("TeamPlayer.findall", TeamPlayer.class).getResultList();
        return Response.ok(list).build();
    }

    @Path("findall/hp")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllHobbyPlayer(){
        List<HobbyPlayer> list = em.createNamedQuery("HobbyPlayer.findall", HobbyPlayer.class).getResultList();
        return Response.ok(list).build();
    }

    @Path("findall")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(){
        List<Golfer> list = em.createNamedQuery("Golfer.findall", Golfer.class).getResultList();
        return Response.ok(list).build();
    }

    @Path("find/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") long id){
        Golfer g = em.find(Golfer.class, id);
        if(g != null){
            return Response.ok(g).build();
        }
        else
        {
            return Response.noContent().build();
        }
    }

    @Path("delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") long id){
        try{
            Golfer g = em.find(Golfer.class, id);
            if(g != null){
                g.getTeeTimes().forEach(t -> {
                    t.removePlayer(g);
                });
                em.merge(g);
                if(g instanceof TeamPlayer){
                    TeamPlayer t = (TeamPlayer)g;
                    t.getTeam().removeMember(t);
                }
                em.remove(g);
            }
        }
        catch (Exception e){
            return Response.status(404).build();
        }
        return Response.ok().build();
    }

    @Path("new/tp")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(TeamPlayer p){
        em.persist(p);
        em.flush();
        return Response.created(URI.create("http://localhost:8085/gca/api/golfer/find/" + p.getId())).build();
    }

    @Path("new/hp")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(HobbyPlayer p){
        em.persist(p);
        em.flush();
        return Response.created(URI.create("http://localhost:8085/gca/api/golfer/find/" + p.getId())).build();
    }

    @Path("update/tp/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, TeamPlayer p){
        TeamPlayer player = em.find(TeamPlayer.class, id);
        player.setName(p.getName());
        player.setAge(p.getAge());
        player.setHcp(p.getHcp());
        player.setJoined(p.getJoined());
        player.setRegularPlayer(p.isRegularPlayer());
        em.merge(player);
        return Response.ok().build();
    }


}
