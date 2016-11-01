package pl.polsl.model;

import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Mateusz on 28.10.2016.
 */
@Entity
@Table(name = "users", schema = "public")
public class Users extends BaseEntity {

    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue
    private Long userId;

    @Basic
    @Column(name = "user_name", nullable = false)
    private String userName;

    @Basic
    @Column(name = "password", nullable = false)
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
    private String email;

    @OneToMany(mappedBy = "usersByUserId")
    private Collection<VideoPlaylists> videoPlaylistsesByUserId;

    @OneToMany(mappedBy = "usersByOwnerId")
    private Collection<Videos> videosesByUserId;

    public Users() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Users users = (Users) o;

        if (!userId.equals(users.userId)) return false;
        if (!userName.equals(users.userName)) return false;
        if (!password.equals(users.password)) return false;
        if (name != null ? !name.equals(users.name) : users.name != null) return false;
        if (surname != null ? !surname.equals(users.surname) : users.surname != null) return false;
        if (nationality != null ? !nationality.equals(users.nationality) : users.nationality != null) return false;
        return email.equals(users.email);

    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + userName.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (nationality != null ? nationality.hashCode() : 0);
        result = 31 * result + email.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
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

    public Collection<VideoPlaylists> getVideoPlaylistsesByUserId() {
        return videoPlaylistsesByUserId;
    }

    public void setVideoPlaylistsesByUserId(Collection<VideoPlaylists> videoPlaylistsesByUserId) {
        this.videoPlaylistsesByUserId = videoPlaylistsesByUserId;
    }

    public Collection<Videos> getVideosesByUserId() {
        return videosesByUserId;
    }

    public void setVideosesByUserId(Collection<Videos> videosesByUserId) {
        this.videosesByUserId = videosesByUserId;
    }
}
