package facades;

import dtos.UserDTO;
import entities.Role;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
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
                em.getTransaction().begin();
                em.persist(user);
                em.getTransaction().commit();
        } finally {
            em.close();
        }
        return user;
    }

    public User getVerifiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            TypedQuery<User> query = em.createQuery("select u from User u where u.userName= :username", User.class);
            query.setParameter("username", username);
            user = query.getSingleResult();
            if (!user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid username or password");
            }
        }catch (NoResultException e) {
            throw new AuthenticationException("Invalid user name or password");
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

    public UserDTO getUserById(Long id) throws API_Exception{
        EntityManager em = getEntityManager();

            User user = em.find(User.class, id);
            if(user == null)
                throw new API_Exception("There's no user with that id",404);
            em.close();
            return new UserDTO(user);



    }

    public UserDTO deleteUser(Long id) throws API_Exception {
        EntityManager em = getEntityManager();
        User user;
        try {
            user = em.find(User.class, id);
            if(user == null) {
                throw new API_Exception("Can't find a user with the username: "+id);
            }
            em.getTransaction().begin();
            em.remove(user);
            em.getTransaction().commit();
            return new UserDTO(user);
        } finally {
            em.close();
        }
    }
    public UserDTO updateUser(UserDTO userDTO) throws API_Exception {
        EntityManager em = getEntityManager();
        User fromDB = em.find(User.class,userDTO.getId());
        if(fromDB == null) {
            throw new API_Exception("No such user with id: " + userDTO.getId());
        }
        User userEntity = new User(userDTO.getId(), userDTO.getUserName(), userDTO.getUserPass(),userDTO.toUser().getRoleList());
        try {
            em.getTransaction().begin();
            em.merge(userEntity);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new UserDTO(userEntity);
    }

}
