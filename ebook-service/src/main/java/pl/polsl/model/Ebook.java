package pl.polsl.model;

import org.hibernate.annotations.GenericGenerator;
import pl.polsl.model.base.BaseEntity;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Mateusz on 28.10.2016.
 */
@Entity
@Table(name = "EBOOK", schema = "public")
public class Ebook extends BaseEntity {

    @Id
    @Column(name = "ebook_id", nullable = false)
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
    private Long ebookId;

    @Basic
    @Column(name = "title", nullable = false, length = -1)
    private String title;

    @Basic
    @Column(name = "num_of_pages", nullable = false)
    private Integer numOfPages;

    @Basic
    @Column(name = "year", nullable = false)
    private Short year;

    @Basic
    @Column(name = "rating")
    private Float rating;

    @Basic
    @Column(name = "genre_id")
    private Long genreId;

    @Basic
    @Column(name = "comments", length = -1)
    private String comments;

    @Basic
    @Column(name = "rating_times")
    private Long ratingTimes;

    @Basic
    @Column(name = "owner_id")
    private Long ownerId;

    @Basic
    @Column(name = "ebook_file_id", nullable = false)
    private Long ebookFileId;

    @ManyToOne
    @JoinColumn(name = "genre_id", referencedColumnName = "genre_id", insertable = false, updatable = false)
    private LiteraryGenre literaryGenreByGenreId;

    @Transient
    private UsersView usersViewByOwnerId;

    @ManyToOne
    @JoinColumn(name = "ebook_file_id", referencedColumnName = "ebook_file_id", nullable = false,
            updatable = false, insertable = false)
    private EbookFiles ebookFilesByEbookFileId;

    @OneToMany(mappedBy = "ebookByEbookId", cascade = CascadeType.ALL)
    private Collection<EbookAuthors> ebookAuthorsesByEbookId;

    @OneToMany(mappedBy = "ebookByEbookId", cascade = CascadeType.ALL)
    private Collection<LibrariesEbooks> librariesEbooksesByEbookId;

    public Ebook() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ebook ebook = (Ebook) o;

        if (!ebookId.equals(ebook.ebookId)) return false;
        if (!title.equals(ebook.title)) return false;
        if (!numOfPages.equals(ebook.numOfPages)) return false;
        if (!year.equals(ebook.year)) return false;
        if (rating != null ? !rating.equals(ebook.rating) : ebook.rating != null) return false;
        if (genreId != null ? !genreId.equals(ebook.genreId) : ebook.genreId != null) return false;
        if (comments != null ? !comments.equals(ebook.comments) : ebook.comments != null) return false;
        if (ratingTimes != null ? !ratingTimes.equals(ebook.ratingTimes) : ebook.ratingTimes != null) return false;
        if (ownerId != null ? !ownerId.equals(ebook.ownerId) : ebook.ownerId != null) return false;
        return ebookFileId.equals(ebook.ebookFileId);

    }

    @Override
    public int hashCode() {
        int result = ebookId.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + numOfPages.hashCode();
        result = 31 * result + year.hashCode();
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        result = 31 * result + (genreId != null ? genreId.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (ratingTimes != null ? ratingTimes.hashCode() : 0);
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        result = 31 * result + ebookFileId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Ebook{" +
                "ebookId=" + ebookId +
                ", title='" + title + '\'' +
                ", numOfPages=" + numOfPages +
                ", year=" + year +
                ", rating=" + rating +
                ", genreId=" + genreId +
                ", comments='" + comments + '\'' +
                ", ratingTimes=" + ratingTimes +
                ", ownerId=" + ownerId +
                ", ebookFileId=" + ebookFileId +
                '}';
    }

    public Long getEbookId() {
        return ebookId;
    }

    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNumOfPages() {
        return numOfPages;
    }

    public void setNumOfPages(Integer numOfPages) {
        this.numOfPages = numOfPages;
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getRatingTimes() {
        return ratingTimes;
    }

    public void setRatingTimes(Long ratingTimes) {
        this.ratingTimes = ratingTimes;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getEbookFileId() {
        return ebookFileId;
    }

    public void setEbookFileId(Long ebookFileId) {
        this.ebookFileId = ebookFileId;
    }

    public LiteraryGenre getLiteraryGenreByGenreId() {
        return literaryGenreByGenreId;
    }

    public void setLiteraryGenreByGenreId(LiteraryGenre literaryGenreByGenreId) {
        this.literaryGenreByGenreId = literaryGenreByGenreId;
    }

    public UsersView getUsersViewByOwnerId() {
        return usersViewByOwnerId;
    }

    public void setUsersViewByOwnerId(UsersView usersViewByOwnerId) {
        this.usersViewByOwnerId = usersViewByOwnerId;
    }

    public EbookFiles getEbookFilesByEbookFileId() {
        return ebookFilesByEbookFileId;
    }

    public void setEbookFilesByEbookFileId(EbookFiles ebookFilesByEbookFileId) {
        this.ebookFilesByEbookFileId = ebookFilesByEbookFileId;
    }

    public Collection<EbookAuthors> getEbookAuthorsesByEbookId() {
        return ebookAuthorsesByEbookId;
    }

    public void setEbookAuthorsesByEbookId(Collection<EbookAuthors> ebookAuthorsesByEbookId) {
        this.ebookAuthorsesByEbookId = ebookAuthorsesByEbookId;
    }

    public Collection<LibrariesEbooks> getLibrariesEbooksesByEbookId() {
        return librariesEbooksesByEbookId;
    }

    public void setLibrariesEbooksesByEbookId(Collection<LibrariesEbooks> librariesEbooksesByEbookId) {
        this.librariesEbooksesByEbookId = librariesEbooksesByEbookId;
    }
}
