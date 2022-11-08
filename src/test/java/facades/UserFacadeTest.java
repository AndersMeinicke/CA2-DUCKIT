package facades;

import dtos.UserDTO;
import entities.User;
import errorhandling.API_Exception;
import javassist.NotFoundException;
import org.junit.jupiter.api.*;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade facade;
    User user1;
    User user2;
    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
        emf.close();
    }

    @BeforeEach
    void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            user1 = new User("Oscar", "test");
            user2 = new User("Mark", "test");
            em.persist(user1);
            em.persist(user2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    void tearDown() {
        //        Remove any data after each test was run
        //        emf.close();
    }

    @Test
    void getVerifiedUser() throws AuthenticationException {
        User actual = facade.getVerifiedUser("Oscar", "test");
        assertEquals(user1,actual);
    }
    @Test
    void wrongPassword() throws AuthenticationException {
        //User actual = facade.getVerifiedUser("Oscar", "test123");
        assertThrows(AuthenticationException.class, ()-> facade.getVerifiedUser("Oscar","test123"));
    }
    @Test
    void wrongUserName() throws AuthenticationException {
    }

    @Test
    void getAllUsers() throws NotFoundException {
        List<UserDTO> actual = facade.getAllUsers();
        int expected = 2;
        assertEquals(expected,actual.size());
    }
    @Test
    void createUser(){


    }

    @Test
    void getUserById() {

    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() throws API_Exception, NotFoundException {
        UserDTO userDTO = facade.deleteUser(user2.getId());
        int expected = 1;
        int actual = facade.getAllUsers().size();
        assertEquals(expected, actual);
        assertEquals(userDTO, new UserDTO(user2));
    }
}