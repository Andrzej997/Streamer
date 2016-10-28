package pl.polsl.model;

import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "music_authors", schema = "public")
@IdClass(MusicAuthorsPK.class)
public class MusicAuthors extends BaseEntity {

    @Id
    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Id
    @Column(name = "song_id", nullable = false)
    private Long songId;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "author_id", nullable = false)
    private MusicArtists musicArtistsByAuthorId;

    @ManyToOne
    @JoinColumn(name = "song_id", referencedColumnName = "song_id", nullable = false)
    private Songs songsBySongId;

    public MusicAuthors() {
    }

    public MusicAuthors(Long authorId, Long songId) {
        this.authorId = authorId;
        this.songId = songId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicAuthors that = (MusicAuthors) o;

        if (!authorId.equals(that.authorId)) return false;
        return songId.equals(that.songId);

    }

    @Override
    public int hashCode() {
        int result = authorId.hashCode();
        result = 31 * result + songId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MusicAuthors{" +
                "authorId=" + authorId +
                ", songId=" + songId +
                '}';
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    public MusicArtists getMusicArtistsByAuthorId() {
        return musicArtistsByAuthorId;
    }

    public void setMusicArtistsByAuthorId(MusicArtists musicArtistsByAuthorId) {
        this.musicArtistsByAuthorId = musicArtistsByAuthorId;
    }

    public Songs getSongsBySongId() {
        return songsBySongId;
    }

    public void setSongsBySongId(Songs songsBySongId) {
        this.songsBySongId = songsBySongId;
    }
}
