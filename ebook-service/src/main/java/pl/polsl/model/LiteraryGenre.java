package pl.polsl.model;

import org.hibernate.annotations.GenericGenerator;
import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "literary_genre", schema = "public")
public class LiteraryGenre extends BaseEntity {

    @Id
    @Column(name = "genre_id", nullable = false)
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
    private Long genreId;

    @Basic
    @Column(name = "name", nullable = false, length = -1)
    private String name;

    @Basic
    @Column(name = "comments", length = -1)
    private String comments;

    @OneToMany(mappedBy = "literaryGenreByGenreId")
    private Collection<Ebook> ebooksByGenreId;

    public LiteraryGenre() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LiteraryGenre that = (LiteraryGenre) o;

        if (!genreId.equals(that.genreId)) return false;
        if (!name.equals(that.name)) return false;
        return comments != null ? comments.equals(that.comments) : that.comments == null;

    }

    @Override
    public int hashCode() {
        int result = genreId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LiteraryGenre{" +
                "genreId=" + genreId +
                ", name='" + name + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
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

    public Collection<Ebook> getEbooksByGenreId() {
        return ebooksByGenreId;
    }

    public void setEbooksByGenreId(Collection<Ebook> ebooksByGenreId) {
        this.ebooksByGenreId = ebooksByGenreId;
    }
}
