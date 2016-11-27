package pl.polsl.model;

import org.hibernate.annotations.GenericGenerator;
import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "music_files", schema = "public")
public class MusicFiles extends BaseEntity {

    @Id
    @Column(name = "music_file_id", nullable = false)
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
    private Long musicFileId;

    @Basic
    @Column(name = "file_name", nullable = false, length = 1024)
    private String fileName;

    @Basic
    @Column(name = "file_size", nullable = false)
    private Integer fileSize;

    @Basic
    @Column(name = "extension", nullable = false, length = 10)
    private String extension;

    @Basic
    @Column(name = "creation_date", nullable = false)
    private Timestamp creationDate;

    @Basic
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Lob
    @Column(name = "file", nullable = false)
    private Blob file;

    @OneToMany(mappedBy = "musicFilesByFileId", cascade = CascadeType.ALL)
    private Collection<Songs> songsesByMusicFileId;

    public MusicFiles() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicFiles that = (MusicFiles) o;

        if (!musicFileId.equals(that.musicFileId)) return false;
        if (!fileName.equals(that.fileName)) return false;
        if (!fileSize.equals(that.fileSize)) return false;
        if (!extension.equals(that.extension)) return false;
        if (!creationDate.equals(that.creationDate)) return false;
        return isPublic.equals(that.isPublic);

    }

    @Override
    public int hashCode() {
        int result = musicFileId.hashCode();
        result = 31 * result + fileName.hashCode();
        result = 31 * result + fileSize.hashCode();
        result = 31 * result + extension.hashCode();
        result = 31 * result + creationDate.hashCode();
        result = 31 * result + isPublic.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MusicFiles{" +
                "musicFileId=" + musicFileId +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", extension='" + extension + '\'' +
                ", creationDate=" + creationDate +
                ", isPublic=" + isPublic +
                '}';
    }

    public Long getMusicFileId() {
        return musicFileId;
    }

    public void setMusicFileId(Long musicFileId) {
        this.musicFileId = musicFileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Blob getFile() {
        return file;
    }

    public void setFile(Blob file) {
        this.file = file;
    }

    public Collection<Songs> getSongsesByMusicFileId() {
        return songsesByMusicFileId;
    }

    public void setSongsesByMusicFileId(Collection<Songs> songsesByMusicFileId) {
        this.songsesByMusicFileId = songsesByMusicFileId;
    }
}
