package entities;

import java.io.Serializable;
import java.util.*;
import javax.enterprise.inject.Default;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import dtos.UserDTO;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@NamedQuery(name = "User.deleteAllRows", query = "DELETE from User")
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_name", length = 25)
    private String userName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "user_pass")
    private String userPass;
    @JoinTable(name = "user_roles", joinColumns = {
            @JoinColumn(name = "user_name", referencedColumnName = "user_name")}, inverseJoinColumns = {
            @JoinColumn(name = "role_name", referencedColumnName = "role_name")})
    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Role> roleList = new ArrayList<>();

    public User() {
    }

    public User(String userName, String userPass) {
        this.userName = userName;
        this.userPass = BCrypt.hashpw(userPass, BCrypt.gensalt());
    }

    public User(String userName, String userPass, List<Role> roleList) {
        this.userName = userName;
        this.userPass = userPass;
        this.roleList = roleList;
    }

    public User(UserDTO userDTO){
        this.id= userDTO.getId();
        this.userName = userDTO.getUserName();
        this.userPass = userDTO.getUserPass();
        List<Role> roleList = new ArrayList<>();
        for (String role : userDTO.getRoles()) {
            roleList.add(new Role(role));

        }
        this.roleList = roleList;
    }

    public List<String> getRolesAsStrings() {
        if (roleList.isEmpty()) {
            return null;
        }
        List<String> rolesAsStrings = new ArrayList<>();
        roleList.forEach((role) -> {
            rolesAsStrings.add(role.getRoleName());
        });
        return rolesAsStrings;
    }

    public boolean verifyPassword(String pw) {
        return (BCrypt.checkpw(pw, userPass));
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return this.userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public void addRole(Role userRole) {
        roleList.add(userRole);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUserName().equals(user.getUserName()) && getUserPass().equals(user.getUserPass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName(), getUserPass());
    }
}
