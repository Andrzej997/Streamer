package pl.polsl.model;

import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "directors", schema = "public")
public class Directors extends BaseEntity {

    @Id
    @Column(name = "director_id", nullable = false)
    private Long directorId;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "name_2")
    private String name2;

    @Basic
    @Column(name = "surname")
    private String surname;

    @Basic
    @Column(name = "birth_date")
    private Timestamp birthDate;

    @Basic
    @Column(name = "death_date")
    private Timestamp deathDate;

    @Basic
    @Column(name = "comments", length = -1)
    private String comments;

    @Basic
    @Column(name = "ratings")
    private Float ratings;

    @OneToMany(mappedBy = "directorsByAuthorId")
    private Collection<VideosAuthors> videosAuthorsesByDirectorId;

    public Directors() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Directors directors = (Directors) o;

        if (!directorId.equals(directors.directorId)) return false;
        if (name != null ? !name.equals(directors.name) : directors.name != null) return false;
        if (name2 != null ? !name2.equals(directors.name2) : directors.name2 != null) return false;
        if (surname != null ? !surname.equals(directors.surname) : directors.surname != null) return false;
        if (birthDate != null ? !birthDate.equals(directors.birthDate) : directors.birthDate != null) return false;
        if (deathDate != null ? !deathDate.equals(directors.deathDate) : directors.deathDate != null) return false;
        if (comments != null ? !comments.equals(directors.comments) : directors.comments != null) return false;
        return ratings != null ? ratings.equals(directors.ratings) : directors.ratings == null;

    }

    @Override
    public int hashCode() {
        int result = directorId.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (name2 != null ? name2.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        result = 31 * result + (deathDate != null ? deathDate.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (ratings != null ? ratings.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Directors{" +
                "directorId=" + directorId +
                ", name='" + name + '\'' +
                ", name2='" + name2 + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDate=" + birthDate +
                ", deathDate=" + deathDate +
                ", comments='" + comments + '\'' +
                ", ratings=" + ratings +
                '}';
    }

    public Long getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Long directorId) {
        this.directorId = directorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Timestamp getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Timestamp birthDate) {
        this.birthDate = birthDate;
    }

    public Timestamp getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Timestamp deathDate) {
        this.deathDate = deathDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Float getRatings() {
        return ratings;
    }

    public void setRatings(Float ratings) {
        this.ratings = ratings;
    }

    public Collection<VideosAuthors> getVideosAuthorsesByDirectorId() {
        return videosAuthorsesByDirectorId;
    }

    public void setVideosAuthorsesByDirectorId(Collection<VideosAuthors> videosAuthorsesByDirectorId) {
        this.videosAuthorsesByDirectorId = videosAuthorsesByDirectorId;
    }
}
