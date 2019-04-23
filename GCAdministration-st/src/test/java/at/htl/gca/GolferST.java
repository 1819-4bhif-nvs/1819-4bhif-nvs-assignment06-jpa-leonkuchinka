package at.htl.gca;

import at.htl.model.Golfer;
import at.htl.model.TeeTime;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GolferST {
        private static Client client;
        private static WebTarget target;

        private static int createdPlayerId = 0;
        private static int createdTeamId = 0;

        @BeforeClass
        public static void init(){
            client = ClientBuilder.newClient();
            target = client.target("http://localhost:8085/gca/api");
        }

        @Test
        public void test01_GetRequestFindAll(){
            Response response = this.target.path("/golfer").request(MediaType.APPLICATION_JSON).get();
            assertThat(response.getStatus(), is(200));
            JsonArray jsonArray = response.readEntity(JsonArray.class);
            assertThat(jsonArray.getValuesAs(JsonObject.class).size(), greaterThan(1));
        }


        @Test
        public void test02_GetGolfer(){
            JsonObject response = target.path("/golfer/1").request(MediaType.APPLICATION_JSON).get(JsonObject.class);
            assertThat(response.getInt("age"), is(17));
            assertThat(response.getString("name"), is("Leon Kuchinka"));
        }

        @Test
        public void test03_CreateGolfer(){
            JsonObjectBuilder builder = Json.createObjectBuilder();
            JsonObject newPupil = builder.add("age", 45).add("name", "Max Mustermann").add("hcp", "-45").add("premiumMember", "true").build();
            Response response = this.target.path("golfer/hobbyplayer").request().post(Entity.json(newPupil));
            assertThat(response.getStatus(), is(201));
            JsonObject pupil = this.client.target(response.getLocation()).request(MediaType.APPLICATION_JSON).get().readEntity(JsonObject.class);
            assertThat(pupil.getInt("age"), is(45));
            assertThat(pupil.getString("name"), containsString("Mustermann"));
            createdPlayerId = pupil.getInt("id");
        }

        @Test
        public void test04_UpdateExistingGolfer(){
            JsonObject pupil = Json.createObjectBuilder()
                    .add("age", 18)
                    .add("name", "Leon Kuchinka")
                    .add("hcp", "1.7")
                    .add("joined", 2015)
                    .add("regularPlayer", true)
                    .build();
            Response res = this.target.path("/golfer/teamplayer/1").request().put(Entity.json(pupil));
            assertThat(res.getStatus(), is(200));

            JsonObject response = this.target.path("/golfer/1").request(MediaType.APPLICATION_JSON).get().readEntity(JsonObject.class);
            assertThat(response.getInt("age"), is(18));
            assertThat(response.getString("name"), is("Leon Kuchinka"));

            //change to what it was
            pupil = Json.createObjectBuilder()
                    .add("age", 17)
                    .add("name", "Leon Kuchinka")
                    .add("hcp", "1.7")
                    .add("joined", 2015)
                    .add("regularPlayer", true)
                    .build();
            this.target.path("/golfer/teamplayer/1").request().put(Entity.json(pupil));
        }

        @Test
        public void test05_DeleteGolferWhoHasTeeTimes(){
            Response response = this.target.path("/golfer/6").request().delete();
            assertThat(response.getStatus(), is(200));
        }


        @Test
        public void test06_DeleteGolfer(){
            Response response = this.target.path("/golfer/" + createdPlayerId).request().delete();
            assertThat(response.getStatus(), is(200));
            response = this.target.path("/golfer/" + createdPlayerId).request().delete();
            assertThat(response.getStatus(), is(200));
        }


        @Test
        public void test07_GetFirstTeetime(){
            JsonObject response = target.path("teetime/1").request(MediaType.APPLICATION_JSON).get(JsonObject.class);
            JsonArray players = response.getJsonArray("players");
            JsonObject firstPlayer = players.getJsonObject(0);
            assertThat(firstPlayer.getInt("age"), is(17));
            assertThat(firstPlayer.getString("name"), is("Leon Kuchinka"));
        }

        @Test
        public void test08_DeleteTeeTime(){
            Response response = target.path("teetime/1").request().delete();
            assertThat(response.getStatus(), is(200));
            response = target.path("teetime/1").request().delete();
            assertThat(response.getStatus(), is(200));
            response = target.path("teetime/1000").request().delete();
            assertThat(response.getStatus(), is(200));
        }
        @Test
        public void test09_GetTeam(){
            Response response = target.path("team/1").request().get();
            assertThat(response.getStatus(), is(200));
            JsonObject team = response.readEntity(JsonObject.class);
            assertThat(team.getString("teamName"), is("Youth Team"));
        }
        @Test
        public void test10_CreateTeam(){
            JsonObject team = Json.createObjectBuilder().add("teamName", "Senior Team").build();
            Response response = target.path("team").request().post(Entity.json(team));
            assertThat(response.getStatus(), is(201));
            JsonObject createdTeam = this.client.target(response.getLocation()).request(MediaType.APPLICATION_JSON).get().readEntity(JsonObject.class);
            assertThat(createdTeam.getString("teamName"), is("Senior Team"));
            createdTeamId = createdTeam.getInt("id");
        }
        @Test
        public void test11_UpdateTeam(){
            JsonObject team = Json.createObjectBuilder().add("teamName", "MidAm Team").build();
            Response response = target.path("team/" + createdTeamId).request().put(Entity.json(team));
            assertThat(response.getStatus(), is(200));
            response = target.path("team/" + createdTeamId).request().get();
            assertThat(response.getStatus(), is(200));
            JsonObject updatedTeam = response.readEntity(JsonObject.class);
            assertThat(updatedTeam.getString("teamName"), is("MidAm Team"));
        }
        @Test
        public void test12_DeleteTeam(){
            Response response = target.path("team/1").request().delete();
            assertThat(response.getStatus(), is(200));
            response = target.path("team/1").request().delete();
            assertThat(response.getStatus(), is(200));
            response = target.path("team/" + createdTeamId).request().delete();
            assertThat(response.getStatus(), is(200));
            response = target.path("team/1000").request().delete();
            assertThat(response.getStatus(), is(200));
        }




    }
