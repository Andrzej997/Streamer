package pl.polsl.model;

import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

/**
 * Created by Mateusz on 18.10.2016.
 */
@Entity
@Table(name = "ebook_files", schema = "public")
public class EbookFiles extends BaseEntity {

    @Id
    @Column(name = "ebook_file_id", nullable = false)
    @GeneratedValue
    private Long ebookFileId;

    @Basic
    @Column(name = "file_name", nullable = false, length = 1024)
    private String fileName;

    @Basic
    @Column(name = "file_path", nullable = false, length = -1)
    private String filePath;

    @Basic
    @Column(name = "extension", nullable = false, length = 10)
    private String extension;
    @Basic
    @Column(name = "creation_date", nullable = false)
    private Timestamp creationDate;

    @Basic
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @OneToMany(mappedBy = "ebookFilesByEbookFileId")
    private Collection<Ebook> ebooksByEbookFileId;

    public EbookFiles() {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EbookFiles that = (EbookFiles) o;

        if (!ebookFileId.equals(that.ebookFileId)) return false;
        if (!fileName.equals(that.fileName)) return false;
        if (!filePath.equals(that.filePath)) return false;
        if (!extension.equals(that.extension)) return false;
        if (!creationDate.equals(that.creationDate)) return false;
        return isPublic.equals(that.isPublic);

    }

    @Override
    public int hashCode() {
        int result = ebookFileId.hashCode();
        result = 31 * result + fileName.hashCode();
        result = 31 * result + filePath.hashCode();
        result = 31 * result + extension.hashCode();
        result = 31 * result + creationDate.hashCode();
        result = 31 * result + isPublic.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "EbookFiles{" +
                "ebookFileId=" + ebookFileId +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", extension='" + extension + '\'' +
                ", creationDate=" + creationDate +
                ", isPublic=" + isPublic +
                '}';
    }

    public Long getEbookFileId() {
        return ebookFileId;
    }

    public void setEbookFileId(Long ebookFileId) {
        this.ebookFileId = ebookFileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public Collection<Ebook> getEbooksByEbookFileId() {
        return ebooksByEbookFileId;
    }

    public void setEbooksByEbookFileId(Collection<Ebook> ebooksByEbookFileId) {
        this.ebooksByEbookFileId = ebooksByEbookFileId;
    }
}
