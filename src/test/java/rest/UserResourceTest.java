package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.UserDTO;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ResponseOptions;
import io.restassured.response.ValidatableResponse;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import entities.*;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeAll;

public class UserResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static User u1, u2;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        u1 = new User("Henrik","henrik123");
        u2 = new User("Betty","betty123");


        try {
            em.getTransaction().begin();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.persist(u1);
            em.persist(u2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/info").then().statusCode(200);
    }

    @Test
    public void testLogRequest() {
        System.out.println("Testing logging request details");
        given().log().all()
                .when().get("/info")
                .then().statusCode(200);
    }

    @Test
    public void testLogResponse() {
        System.out.println("Testing logging response details");
        given()
                .when().get("/info")
                .then().log().body().statusCode(200);
    }

    @Test
    public void testGetById()  {
        given()
                .contentType(ContentType.JSON)
                .get("/info/user/{id}",u1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(u1.getId()))
                .body("userName", equalTo(u1.getUserName()));
    }

   /* @Test
    public void testError() {
        given()
                .contentType(ContentType.JSON)
               // .pathParam("id", u1.getId()).when()
                .get("/info/user/"+20)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("code", equalTo(404))
                .body("message", equalTo("Can't find a user with that id"));
    }

    */

    @Test
    public void testPrintResponse(){
        Response response = given().when().get("/info/user/"+u1.getId());
        ResponseBody body = response.getBody();
        System.out.println(body.prettyPrint());

        response
                .then()
                .assertThat()
                .body("userName",equalTo("Henrik"));
    }

    @Test
    public void exampleJsonPathTest() {
        Response res = given().get("/info/user/"+u1.getId());
        assertEquals(200, res.getStatusCode());
        String json = res.asString();
        JsonPath jsonPath = new JsonPath(json);
        assertEquals("Henrik", jsonPath.get("userName"));
    }

    @Test
    void deleteUser() {
        given()
                .contentType("application/json")
                .pathParam("id", u2.getId())
                .delete("/info/user/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(u2.getId()));
    }



/*
    @Test
    public void getAllUsers() throws Exception {
        List<UserDTO> userDTOs;

        userDTOs = given()
                .contentType("application/json")
                .when()
                .get("/info/user/all")
                .then()
                .extract().body().jsonPath().getList("", UserDTO.class);

        UserDTO u1DTO = new UserDTO(u1);
        UserDTO u2DTO = new UserDTO(u1);
        assertThat(userDTOs, containsInAnyOrder(u1DTO, u2DTO));

    }

 */



    /*@Test
    public void postTest() {
        User u = new User("Helge","helge123");
        UserDTO udto = new UserDTO(u);
        String requestBody = GSON.toJson(udto);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/info")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("userName", equalTo("Helge"));

    }

     */

    /*
    @Test
    public void updateTest() {
        p2.addChild(c2);
        p2.setAge(23);
        ParentDTO pdto = new ParentDTO(p2);
        String requestBody = GSON.toJson(pdto);

        given()
                .header("Content-type", ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/parent/"+p2.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(p2.getId()))
                .body("name", equalTo("Betty"))
                .body("age", equalTo(23))
                .body("children", hasItems(hasEntry("name","Alberta")));
    }

    @Test
    public void testDeleteParent() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", p2.getId())
                .delete("/parent/{id}")
                .then()
                .statusCode(200)
                .body("id",equalTo(p2.getId()));
    }

    // More test tools from: https://www.baeldung.com/java-junit-hamcrest-guide
    @Test
    public void testListSize() {
        System.out.println("Check size of list");
        List<String> hamcrestMatchers = Arrays.asList(
                "collections", "beans", "text", "number");
        assertThat(hamcrestMatchers, hasSize(4));
    }
    @Test
    public void testPropAndValue() {
        System.out.println("Check for property and value on an entity instance");
        Parent person = new Parent("Benjamin", 33);
        assertThat(person, hasProperty("name", equalTo("Benjamin")));
    }
    @Test
    public void testCompareObjects() {
        System.out.println("Check if 2 instances has same property values (EG. use compare properties rather than objects");
        Parent person1 = new Parent("Betty", 45);
        Parent person2 = new Parent("Betty", 45);
        assertThat(person1, samePropertyValuesAs(person2));
    }
    @Test
    public void testToString(){
        System.out.println("Check if obj.toString() creates the right output");
        Parent person=new Parent("Billy", 89);
        String str=person.toString();
        assertThat(person,hasToString(str));
    }

    @Test
    public void testMapContains() {
        List<Parent> parents = Arrays.asList(
                new Parent("Henrik",67),
                new Parent("Henriette",57)
        );
        assertThat(parents.toArray(), arrayContainingInAnyOrder(parents.get(0),parents.get(1)));
    }
    @Test
    public void testNumeric() {
        System.out.println("Test numeric values");
        assertThat(1.2, closeTo(1, 0.5));
        assertThat(5, greaterThanOrEqualTo(5));

        List<Integer> list = Arrays.asList(1, 2, 3);
        int baseCase = 0;
        assertThat(list, everyItem(greaterThan(baseCase)));
    }
    @Test
    public void testMoreReadable() {
        System.out.println("Use the IS, NOT etc keywords for readability");
        String str1 = "text";
        String str2 = "texts";
        String str3 = "texts";
        String str4 = "These are several texts in one sentence";
        assertThat(str1, not(str2));
        assertThat(str2, is(str3));
        assertThat(str4, containsString(str2));

    }

 */

}
