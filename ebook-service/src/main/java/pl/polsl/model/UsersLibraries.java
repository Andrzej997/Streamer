package pl.polsl.model;

import org.hibernate.annotations.GenericGenerator;
import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "users_libraries", schema = "public")
public class UsersLibraries extends BaseEntity {

    @Id
    @Column(name = "library_id", nullable = false)
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
    private Long libraryId;

    @Basic
    @Column(name = "user_id")
    private Long userId;

    @Basic
    @Column(name = "title", nullable = false, length = -1)
    private String title;

    @Basic
    @Column(name = "creation_date")
    private Timestamp creationDate;

    @OneToMany(mappedBy = "usersLibrariesByLibraryId", cascade = CascadeType.ALL)
    private Collection<LibrariesEbooks> librariesEbooksesByLibraryId;

    @Transient
    private UsersView usersViewByUserId;

    public UsersLibraries() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsersLibraries that = (UsersLibraries) o;

        if (!libraryId.equals(that.libraryId)) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (!title.equals(that.title)) return false;
        return creationDate != null ? creationDate.equals(that.creationDate) : that.creationDate == null;

    }

    @Override
    public int hashCode() {
        int result = libraryId.hashCode();
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + title.hashCode();
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UsersLibraries{" +
                "libraryId=" + libraryId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }

    public Long getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Long libraryId) {
        this.libraryId = libraryId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Collection<LibrariesEbooks> getLibrariesEbooksesByLibraryId() {
        return librariesEbooksesByLibraryId;
    }

    public void setLibrariesEbooksesByLibraryId(Collection<LibrariesEbooks> librariesEbooksesByLibraryId) {
        this.librariesEbooksesByLibraryId = librariesEbooksesByLibraryId;
    }

    public UsersView getUsersViewByUserId() {
        return usersViewByUserId;
    }

    public void setUsersViewByUserId(UsersView usersViewByUserId) {
        this.usersViewByUserId = usersViewByUserId;
    }
}
