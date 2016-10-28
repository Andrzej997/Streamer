package pl.polsl.model;

import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "artists", schema = "public")
public class Artists extends BaseEntity {

    @Id
    @Column(name = "artist_id", nullable = false)
    private Long artistId;

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
    @Column(name = "birth_year")
    private Timestamp birthYear;

    @Basic
    @Column(name = "death_year")
    private Timestamp deathYear;

    @Basic
    @Column(name = "comments", length = -1)
    private String comments;

    @Basic
    @Column(name = "ratings", precision = 0)
    private Float ratings;

    @OneToMany(mappedBy = "artistsByAuthorId")
    private Collection<ImageAuthors> imageAuthorsesByArtistId;

    public Artists() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Artists artists = (Artists) o;

        if (!artistId.equals(artists.artistId)) return false;
        if (name != null ? !name.equals(artists.name) : artists.name != null) return false;
        if (name2 != null ? !name2.equals(artists.name2) : artists.name2 != null) return false;
        if (surname != null ? !surname.equals(artists.surname) : artists.surname != null) return false;
        if (birthYear != null ? !birthYear.equals(artists.birthYear) : artists.birthYear != null) return false;
        if (deathYear != null ? !deathYear.equals(artists.deathYear) : artists.deathYear != null) return false;
        if (comments != null ? !comments.equals(artists.comments) : artists.comments != null) return false;
        return ratings != null ? ratings.equals(artists.ratings) : artists.ratings == null;

    }

    @Override
    public int hashCode() {
        int result = artistId.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (name2 != null ? name2.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (birthYear != null ? birthYear.hashCode() : 0);
        result = 31 * result + (deathYear != null ? deathYear.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (ratings != null ? ratings.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Artists{" +
                "artistId=" + artistId +
                ", name='" + name + '\'' +
                ", name2='" + name2 + '\'' +
                ", surname='" + surname + '\'' +
                ", birthYear=" + birthYear +
                ", deathYear=" + deathYear +
                ", comments='" + comments + '\'' +
                ", ratings=" + ratings +
                '}';
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
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

    public Timestamp getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Timestamp birthYear) {
        this.birthYear = birthYear;
    }

    public Timestamp getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Timestamp deathYear) {
        this.deathYear = deathYear;
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

    public Collection<ImageAuthors> getImageAuthorsesByArtistId() {
        return imageAuthorsesByArtistId;
    }

    public void setImageAuthorsesByArtistId(Collection<ImageAuthors> imageAuthorsesByArtistId) {
        this.imageAuthorsesByArtistId = imageAuthorsesByArtistId;
    }
}
