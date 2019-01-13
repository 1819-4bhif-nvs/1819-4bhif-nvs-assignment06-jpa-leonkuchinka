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


        @BeforeClass
        public static void init(){
            client = ClientBuilder.newClient();
            target = client.target("http://localhost:8085/gca/api");
        }

        @Test
        public void test01_GetRequestFindAll(){
            Response response = this.target.path("/golfer/findall").request(MediaType.APPLICATION_JSON).get();
            assertThat(response.getStatus(), is(200));
            JsonArray jsonArray = response.readEntity(JsonArray.class);
            assertThat(jsonArray.getValuesAs(JsonObject.class).size(), greaterThan(1));
        }


        @Test
        public void test02_GetGolfer(){
            JsonObject response = target.path("/golfer/find/1").request(MediaType.APPLICATION_JSON).get(JsonObject.class);
            assertThat(response.getInt("age"), is(17));
            assertThat(response.getString("name"), is("Leon Kuchinka"));
        }

        @Test
        public void test03_CreateGolfer(){
            JsonObjectBuilder builder = Json.createObjectBuilder();
            JsonObject newPupil = builder.add("age", 45).add("name", "Max Mustermann").add("hcp", "-45").add("premiumMember", "true").build();
            Response response = this.target.path("golfer/new/hp").request().post(Entity.json(newPupil));
            assertThat(response.getStatus(), is(201));
            JsonObject pupil = this.client.target(response.getLocation()).request(MediaType.APPLICATION_JSON).get().readEntity(JsonObject.class);
            assertThat(pupil.getInt("age"), is(45));
            assertThat(pupil.getString("name"), containsString("Mustermann"));
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
            Response res = this.target.path("/golfer/update/tp/1").request().put(Entity.json(pupil));
            assertThat(res.getStatus(), is(200));

            JsonObject response = this.target.path("/golfer/find/1").request(MediaType.APPLICATION_JSON).get().readEntity(JsonObject.class);
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
            this.target.path("/golfer/update/tp/1").request().put(Entity.json(pupil));
        }
/*
        @Test
        public void test05_DeleteGolferWhoHasTeeTimes(){
            Response response = this.target.path("/golfer/delete/6").request().delete();
            assertThat(response.getStatus(), is(404));
        }*/


        @Test
        public void test06_DeleteGolfer(){
            Response response = this.target.path("/golfer/delete/5").request().delete();
            assertThat(response.getStatus(), is(200));
            response = this.target.path("/golfer/delete/5").request().delete();
            assertThat(response.getStatus(), is(200));
        }


        @Test
        public void test07_GetFirstTeetime(){
            JsonObject response = target.path("teetime/find/1").request(MediaType.APPLICATION_JSON).get(JsonObject.class);
            JsonArray players = response.getJsonArray("players");
            JsonObject firstPlayer = players.getJsonObject(0);
            assertThat(firstPlayer.getInt("age"), is(17));
            assertThat(firstPlayer.getString("name"), is("Leon Kuchinka"));
        }


    }
