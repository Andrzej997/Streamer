package pl.polsl.model;

import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "music_artists", schema = "public")
public class MusicArtists extends BaseEntity {

    @Id
    @Column(name = "author_id", nullable = false)
    private Long authorId;

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
    private Short birthYear;

    @Basic
    @Column(name = "death_year")
    private Short deathYear;

    @Basic
    @Column(name = "comments", length = -1)
    private String comments;

    @Basic
    @Column(name = "ratings")
    private Float ratings;

    @OneToMany(mappedBy = "musicArtistsByAuthorId")
    private Collection<MusicAuthors> musicAuthors;

    public MusicArtists() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicArtists that = (MusicArtists) o;

        if (!authorId.equals(that.authorId)) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (name2 != null ? !name2.equals(that.name2) : that.name2 != null) return false;
        if (surname != null ? !surname.equals(that.surname) : that.surname != null) return false;
        if (birthYear != null ? !birthYear.equals(that.birthYear) : that.birthYear != null) return false;
        if (deathYear != null ? !deathYear.equals(that.deathYear) : that.deathYear != null) return false;
        if (comments != null ? !comments.equals(that.comments) : that.comments != null) return false;
        return ratings != null ? ratings.equals(that.ratings) : that.ratings == null;

    }

    @Override
    public int hashCode() {
        int result = authorId.hashCode();
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
        return "MusicArtists{" +
                "authorId=" + authorId +
                ", name='" + name + '\'' +
                ", name2='" + name2 + '\'' +
                ", surname='" + surname + '\'' +
                ", birthYear=" + birthYear +
                ", deathYear=" + deathYear +
                ", comments='" + comments + '\'' +
                ", ratings=" + ratings +
                '}';
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
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

    public Short getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Short birthYear) {
        this.birthYear = birthYear;
    }

    public Short getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Short deathYear) {
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

    public Collection<MusicAuthors> getMusicAuthors() {
        return musicAuthors;
    }

    public void setMusicAuthors(Collection<MusicAuthors> musicAuthors) {
        this.musicAuthors = musicAuthors;
    }
}
