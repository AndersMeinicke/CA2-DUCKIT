package facades;

import dtos.UserDTO;
import entities.Role;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import errorhandling.API_Exception;
import javassist.NotFoundException;
import security.errorhandling.AuthenticationException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public User createUser(User user) {
        EntityManager em = getEntityManager();
        Role defaultRole = new Role("user");
        user.addRole(defaultRole);
        try {
            if (em.find(User.class, user.getUserName()) == null) {
                em.getTransaction().begin();
                em.persist(user);
                em.getTransaction().commit();
            } else throw new Exception("A user with that username already exists");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return user;
    }

    public User getVerifiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public List<UserDTO> getAllUsers() throws NotFoundException{
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
            if(query == null){
                throw new NotFoundException("Can't find any users");
            }
            List<User> users = query.getResultList();
            return UserDTO.getUserDTOs(users);
        } finally {
            em.close();
        }
    }

    public UserDTO getUserByUsername(String userName) throws NotFoundException{
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.userName = :userName", User.class);
            if(query == null) {
                throw new NotFoundException("Can't find a user with the username: "+userName);
            }
            query.setParameter("userName", userName);

            User user = query.getSingleResult();
            return new UserDTO(user);

        } finally {
            em.close();
        }
    }

    public UserDTO deleteUser(String userName) throws API_Exception {
        EntityManager em = getEntityManager();
        User user;

        try {
            user = em.find(User.class, userName);
            if(user == null) {
                throw new API_Exception("Can't find a user with the username: "+userName);
            }
            em.getTransaction().begin();
            em.remove(user);
            em.getTransaction().commit();
            return new UserDTO(user);
        } finally {
            em.close();
        }
    }

}
