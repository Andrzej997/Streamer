package pl.polsl.model;

import org.hibernate.annotations.Immutable;
import pl.polsl.model.base.BaseEntity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Mateusz on 28.10.2016.
 */

@Entity
@Immutable
public class UsersView extends BaseEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Basic
    @Column(name = "user_name", nullable = false)
    private String userName;

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
    private String email;

    public UsersView() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsersView usersView = (UsersView) o;

        if (!userId.equals(usersView.userId)) return false;
        if (!userName.equals(usersView.userName)) return false;
        if (name != null ? !name.equals(usersView.name) : usersView.name != null) return false;
        if (surname != null ? !surname.equals(usersView.surname) : usersView.surname != null) return false;
        if (nationality != null ? !nationality.equals(usersView.nationality) : usersView.nationality != null)
            return false;
        return email.equals(usersView.email);

    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + userName.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (nationality != null ? nationality.hashCode() : 0);
        result = 31 * result + email.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UsersView{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", nationality='" + nationality + '\'' +
                ", email='" + email + '\'' +
                '}';
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

}
