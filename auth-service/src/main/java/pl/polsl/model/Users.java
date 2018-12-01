package pl.polsl.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.userdetails.UserDetails;
import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Mateusz on 25.10.2016.
 */
@Entity
@Table(name = "USERS", schema = "users_schema", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_name", "email"})})
public class Users extends BaseEntity implements UserDetails {

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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Authority> authorities;

    @Basic
    @Column(name = "account_expiration_date", nullable = false)
    private Date accountExpirationDate;

    @Basic
    @Column(name = "account_locked", nullable = false)
    private Boolean accountLocked;

    @Basic
    @Column(name = "password_expiration_date", nullable = false)
    private Date passwordExpirationDate;

    @PrePersist
    public void prePersist() {
        if (this.accountLocked == null) {
            accountLocked = true;
        }
        if (this.passwordExpirationDate == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 120);
            passwordExpirationDate = calendar.getTime();
        }
        if (this.accountExpirationDate == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 120);
            accountExpirationDate = calendar.getTime();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.prePersist();
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

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        return this.accountExpirationDate.compareTo(date) > 0;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        return this.passwordExpirationDate.compareTo(date) > 0;
    }

    @Override
    public boolean isEnabled() {
        Boolean enabled = isAccountNonExpired();
        enabled &= isAccountNonLocked();
        enabled &= isCredentialsNonExpired();
        return enabled;
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
    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public Date getAccountExpirationDate() {
        return accountExpirationDate;
    }

    public void setAccountExpirationDate(Date accountExpirationDate) {
        this.accountExpirationDate = accountExpirationDate;
    }

    public Boolean getAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(Boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public Date getPasswordExpirationDate() {
        return passwordExpirationDate;
    }

    public void setPasswordExpirationDate(Date passwordExpirationDate) {
        this.passwordExpirationDate = passwordExpirationDate;
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
                ", accountExpirationDate=" + accountExpirationDate +
                ", accountLocked=" + accountLocked +
                ", passwordExpirationDate=" + passwordExpirationDate +
                '}';
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
        if (email != null ? !email.equals(users.email) : users.email != null) return false;
        if (accountExpirationDate != null ? !accountExpirationDate.equals(users.accountExpirationDate) : users.accountExpirationDate != null)
            return false;
        if (accountLocked != null ? !accountLocked.equals(users.accountLocked) : users.accountLocked != null)
            return false;
        return passwordExpirationDate != null ? passwordExpirationDate.equals(users.passwordExpirationDate) : users.passwordExpirationDate == null;

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
        result = 31 * result + (accountExpirationDate != null ? accountExpirationDate.hashCode() : 0);
        result = 31 * result + (accountLocked != null ? accountLocked.hashCode() : 0);
        result = 31 * result + (passwordExpirationDate != null ? passwordExpirationDate.hashCode() : 0);
        return result;
    }
}
