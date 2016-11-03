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
@Table(name = "image_files", schema = "public")
public class ImageFiles extends BaseEntity {

    @Id
    @Column(name = "image_file_id", nullable = false)
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
    private Long imageFileId;

    @Basic
    @Column(name = "file_name", nullable = false, length = 1024)
    private String fileName;

    @Basic
    @Column(name = "file_path", nullable = false, length = -1)
    private String filePath;

    @Basic
    @Column(name = "file_extension", nullable = false, length = 10)
    private String fileExtension;

    @Basic
    @Column(name = "creation_date", nullable = false)
    private Timestamp creationDate;

    @Basic
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Lob
    @Column(name = "file", nullable = false)
    private Blob file;

    @OneToMany(mappedBy = "imageFilesByImageFileId")
    private Collection<Images> imagesByImageFileId;

    public ImageFiles() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageFiles that = (ImageFiles) o;

        if (!imageFileId.equals(that.imageFileId)) return false;
        if (!fileName.equals(that.fileName)) return false;
        if (!filePath.equals(that.filePath)) return false;
        if (!fileExtension.equals(that.fileExtension)) return false;
        if (!creationDate.equals(that.creationDate)) return false;
        return isPublic.equals(that.isPublic);

    }

    @Override
    public int hashCode() {
        int result = imageFileId.hashCode();
        result = 31 * result + fileName.hashCode();
        result = 31 * result + filePath.hashCode();
        result = 31 * result + fileExtension.hashCode();
        result = 31 * result + creationDate.hashCode();
        result = 31 * result + isPublic.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ImageFiles{" +
                "imageFileId=" + imageFileId +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                ", creationDate=" + creationDate +
                ", isPublic=" + isPublic +
                '}';
    }

    public Long getImageFileId() {
        return imageFileId;
    }

    public void setImageFileId(Long imageFileId) {
        this.imageFileId = imageFileId;
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

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
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

    public Collection<Images> getImagesByImageFileId() {
        return imagesByImageFileId;
    }

    public void setImagesByImageFileId(Collection<Images> imagesByImageFileId) {
        this.imagesByImageFileId = imagesByImageFileId;
    }
}
