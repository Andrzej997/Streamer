package pl.polsl.model;

import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "music_genres", schema = "public")
public class MusicGenres extends BaseEntity {

    @Id
    @Column(name = "music_genre_id", nullable = false)
    @GeneratedValue
    private Long musicGenreId;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "comments", length = -1)
    private String comments;

    @OneToMany(mappedBy = "musicGenresByMusicGenreId")
    private Collection<Songs> songsesByMusicGenreId;

    public MusicGenres() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicGenres that = (MusicGenres) o;

        if (!musicGenreId.equals(that.musicGenreId)) return false;
        if (!name.equals(that.name)) return false;
        return comments != null ? comments.equals(that.comments) : that.comments == null;

    }

    @Override
    public int hashCode() {
        int result = musicGenreId.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MusicGenres{" +
                "musicGenreId=" + musicGenreId +
                ", name='" + name + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }

    public Long getMusicGenreId() {
        return musicGenreId;
    }

    public void setMusicGenreId(Long musicGenreId) {
        this.musicGenreId = musicGenreId;
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

    public Collection<Songs> getSongsesByMusicGenreId() {
        return songsesByMusicGenreId;
    }

    public void setSongsesByMusicGenreId(Collection<Songs> songsesByMusicGenreId) {
        this.songsesByMusicGenreId = songsesByMusicGenreId;
    }
}
