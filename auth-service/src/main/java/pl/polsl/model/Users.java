package pl.polsl.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Mateusz on 25.10.2016.
 */
@Entity
@Table(name = "USERS", schema = "users_schema", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_name", "email"})})
public class Users extends BaseEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    @GenericGenerator(
            name = "generator",
            strategy = "sequence-identity",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "sequence",
                            value = "DEFAULTDBSEQ"
                    )

            })
    @GeneratedValue(generator = "generator")
    private Long userId;

    @Basic
    @Column(name = "user_name", nullable = false)
    @NotNull
    private String userName;

    @JsonIgnore
    @Basic
    @Column(name = "password", nullable = false)
    @NotNull
    private String password;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "surname")
    private String surname;

    @Basic
    @Column(name = "nationality")
    private String nationality;

    @Basic
    @Column(name = "email", nullable = false, length = 1024)
    @NotNull
    private String email;

    public Users() {

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Users users = (Users) o;

        if (userId != null ? !userId.equals(users.userId) : users.userId != null) return false;
        if (userName != null ? !userName.equals(users.userName) : users.userName != null) return false;
        if (password != null ? !password.equals(users.password) : users.password != null) return false;
        if (name != null ? !name.equals(users.name) : users.name != null) return false;
        if (surname != null ? !surname.equals(users.surname) : users.surname != null) return false;
        if (nationality != null ? !nationality.equals(users.nationality) : users.nationality != null) return false;
        return email != null ? email.equals(users.email) : users.email == null;

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (nationality != null ? nationality.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
