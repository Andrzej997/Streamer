package pl.polsl.model;

import org.hibernate.annotations.GenericGenerator;
import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "film_genres", schema = "public")
public class FilmGenres extends BaseEntity {

    @Id
    @Column(name = "film_genre_id", nullable = false)
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
    private Long filmGenreId;

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;

    @Basic
    @Column(name = "comments", length = -1)
    private String comments;

    @OneToMany(mappedBy = "filmGenresByFilmGenreId", cascade = CascadeType.ALL)
    private Collection<Videos> videosesByFilmGenreId;

    public FilmGenres() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilmGenres that = (FilmGenres) o;

        if (!filmGenreId.equals(that.filmGenreId)) return false;
        if (!name.equals(that.name)) return false;
        return comments != null ? comments.equals(that.comments) : that.comments == null;

    }

    @Override
    public int hashCode() {
        int result = filmGenreId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FilmGenres{" +
                "filmGenreId=" + filmGenreId +
                ", name='" + name + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }

    public Long getFilmGenreId() {
        return filmGenreId;
    }

    public void setFilmGenreId(Long filmGenreId) {
        this.filmGenreId = filmGenreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Collection<Videos> getVideosesByFilmGenreId() {
        return videosesByFilmGenreId;
    }

    public void setVideosesByFilmGenreId(Collection<Videos> videosesByFilmGenreId) {
        this.videosesByFilmGenreId = videosesByFilmGenreId;
    }
}
