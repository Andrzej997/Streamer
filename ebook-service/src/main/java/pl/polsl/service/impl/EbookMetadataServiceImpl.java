package pl.polsl.service.impl;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.polsl.dto.*;
import pl.polsl.mapper.EbookMapper;
import pl.polsl.model.*;
import pl.polsl.repository.*;
import pl.polsl.repository.custom.UsersRepositoryCustom;
import pl.polsl.service.EbookMetadataService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Mateusz on 28.11.2016.
 */
@Service
@Transactional
public class EbookMetadataServiceImpl implements EbookMetadataService {

    @Autowired
    private EbookMapper ebookMapper;

    @Autowired
    private UsersRepositoryCustom usersRepository;

    @Autowired
    private WritersRepository writersRepository;

    @Autowired
    private LiteraryGenreRepository literaryGenreRepository;

    @Autowired
    private EbookAuthorsRepository ebookAuthorsRepository;

    @Autowired
    private EbookFilesRepository ebookFilesRepository;

    @Autowired
    private EbookRepository ebookRepository;

    @Override
    public List<WriterDTO> getWritersByPrediction(String name, String name2, String surname) {
        List<WriterDTO> result;
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        name = "%" + name + "%";
        List<Writers> writersList = writersRepository.findByNameLike(name);
        if (writersList == null) {
            writersList = new ArrayList<>();
        }
        writersList.addAll(writersRepository.findByName2Like(name));
        writersList.addAll(writersRepository.findBySurnameLike(name));
        if (!StringUtils.isEmpty(surname) && !surname.equals("undefined")) {
            surname = "%" + surname + "%";
            writersList = writersRepository.findByNameLikeAndSurnameLike(name, surname);
            if (writersList == null) {
                writersList = new ArrayList<>();
            }
            writersList.addAll(writersRepository.findByNameLikeAndName2Like(name, surname));
        }
        if (!StringUtils.isEmpty(name2) && !name2.equals("undefined")) {
            name2 = "%" + name2 + "%";
            writersList = writersRepository.findByNameLikeAndName2Like(name, name2);
            if (writersList == null) {
                writersList = new ArrayList<>();
            }
            writersList.addAll(writersRepository.findByNameLikeAndSurnameLike(name, name2));
        }
        result = ebookMapper.toWriterDTOList(writersList);
        return result;
    }

    @Override
    public List<LiteraryGenreDTO> getLiteraryGenresByPrediction(String name) {
        List<LiteraryGenreDTO> result;
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        name = "%" + name + "%";
        List<LiteraryGenre> literaryGenreList = literaryGenreRepository.findByNameLike(name);
        return ebookMapper.toLiteraryGenreDTOList(literaryGenreList);
    }

    @Override
    public UploadEbookMetadataDTO saveMetadata(UploadEbookMetadataDTO uploadEbookMetadataDTO) {
        UsersView user = usersRepository.findUsersByUserName(uploadEbookMetadataDTO.getUsername());
        if (user == null) {
            return null;
        }
        EbookDTO ebookDTO = uploadEbookMetadataDTO.getEbookDTO();
        if (ebookDTO == null) {
            return null;
        }
        Ebook ebook = ebookMapper.toEbook(ebookDTO);
        List<Writers> writersList = ebookMapper.toWritersList(ebookDTO.getWriterDTOList());
        saveLiteraryGenreForEbook(ebookDTO, ebook);
        saveEbookFileMetadataForEbook(ebookDTO, ebook);
        ebook = ebookRepository.save(ebook);

        saveWritersForEbook(ebook, writersList);
        List<EbookAuthors> imageAuthorsList = ebookAuthorsRepository.findByEbookId(ebook.getEbookId());
        ebook.setEbookAuthorsesByEbookId(imageAuthorsList);

        ebook = ebookRepository.save(ebook);

        EbookDTO toEbookDTO = ebookMapper.toEbookDTO(ebook);
        UploadEbookMetadataDTO result = new UploadEbookMetadataDTO();
        result.setEbookDTO(ebookDTO);
        result.setUsername(user.getUserName());
        return result;
    }

    private void saveLiteraryGenreForEbook(EbookDTO ebookDTO, Ebook ebook) {
        LiteraryGenre literaryGenre = null;
        if (ebookDTO.getLiteraryGenreDTO() != null && !StringUtils.isEmpty(ebookDTO.getLiteraryGenreDTO().getName())) {
            literaryGenre = literaryGenreRepository.save(ebookMapper.toLiteraryGenre(ebookDTO.getLiteraryGenreDTO()));
        }
        if (literaryGenre == null) {
            return;
        }
        ebook.setLiteraryGenreByGenreId(literaryGenre);
        ebook.setGenreId(literaryGenre.getGenreId());
    }

    private void saveEbookFileMetadataForEbook(EbookDTO ebookDTO, Ebook ebook) {
        EbookFiles ebookFiles = null;
        if (ebookDTO.getEbookFileMetadataDTO() != null) {
            ebookFiles = ebookFilesRepository.save(ebookMapper.toEbookFiles(ebookDTO.getEbookFileMetadataDTO()));
        }
        if (ebookFiles == null) {
            return;
        }
        ebook.setEbookFilesByEbookFileId(ebookFiles);
        ebook.setEbookFileId(ebookFiles.getEbookFileId());
    }

    private void saveWritersForEbook(Ebook ebook, List<Writers> writersList) {
        if (writersList == null) {
            return;
        }
        for (Writers writers : writersList) {
            writers = writersRepository.save(writers);

            EbookAuthors ebookAuthors = new EbookAuthors();
            ebookAuthors.setAuthorId(writers.getWriterId());
            ebookAuthors.setWritersByAuthorId(writers);
            ebookAuthors.setEbookId(ebook.getEbookId());
            ebookAuthors.setEbookByEbookId(ebook);
            ebookAuthorsRepository.save(ebookAuthors);
        }
    }

    @Override
    public List<EbookDTO> getTop10Ebooks(String username, String title) {
        List<Ebook> ebookList = null;
        UsersView user = null;
        if (!StringUtils.isEmpty(username)) {
            user = usersRepository.findUsersByUserName(username);
        }
        if (user == null) {
            if (StringUtils.isEmpty(title)) {
                ebookList = ebookRepository.findTop10ByIsPublicOrderByRatingDesc(true);
            } else {
                title = "%" + title + "%";
                ebookList = ebookRepository.findTop10ByIsPublicAndTitleLikeOrderByRatingDesc(true, title);
            }
        } else {
            if (StringUtils.isEmpty(title)) {
                ebookList = ebookRepository.findTop10ByIsPublicAndOwnerIdOrderByRatingDesc(false, user.getUserId());
            } else {
                title = "%" + title + "%";
                ebookList = ebookRepository.findTop10ByIsPublicAndOwnerIdAndTitleLikeOrderByRatingDesc(false, user.getUserId(), title);
            }
        }
        if (ebookList == null) {
            return null;
        }
        List<EbookDTO> ebookDTOList = ebookMapper.toEbookDTOList(ebookList);
        if (ebookDTOList == null || ebookDTOList.isEmpty()) {
            return null;
        } else {
            return ebookDTOList.subList(0, ebookDTOList.size() > 9 ? 9 : ebookDTOList.size());
        }
    }

    @Override
    public List<EbookDTO> getAllUserImages(String username) {
        UsersView user = null;
        user = usersRepository.findUsersByUserName(username);
        if (user == null) {
            return null;
        }
        List<Ebook> ebookList = ebookRepository.findByOwnerId(user.getUserId());
        return ebookMapper.toEbookDTOList(ebookList);
    }

    @Override
    public List<EbookDTO> searchEbooksByCriteria(SearchEbookCriteriaDTO searchEbookCriteriaDTO) {
        if (searchEbookCriteriaDTO == null || StringUtils.isEmpty(searchEbookCriteriaDTO.getCriteria())) {
            return null;
        }
        String criteria = searchEbookCriteriaDTO.getCriteria();
        String textSearched = searchEbookCriteriaDTO.getTextSearched();
        switch (criteria) {
            case "T":
                return getEbooksByTitle(textSearched);
            case "A":
                return getEbooksByAuthor(textSearched);
            case "G":
                return getEbooksByLiteraryGenreName(textSearched);
            case "Y":
                return getEbooksByYear(textSearched);
            case "ALL":
                return getEbooksByAllCriteria(textSearched);
        }
        return null;
    }

    private List<EbookDTO> getEbooksByTitle(String title) {
        if (title == null || "undefined".equals(title)) {
            return null;
        }
        List<Ebook> ebookList = ebookRepository.findByTitleOrderByRating(title);
        if (ebookList == null) {
            ebookList = new ArrayList<>();
        }
        title += "%";
        ebookList.addAll(ebookRepository.findByTitleLikeOrderByRating(title));
        ebookList = ebookList.stream().filter(ebook ->
                (ebook.getEbookFilesByEbookFileId() != null && ebook.getEbookFilesByEbookFileId().getPublic()))
                .collect(Collectors.toList());
        return ebookMapper.toEbookDTOList(ebookList);
    }

    private List<EbookDTO> getEbooksByAuthor(String authorData) {
        if (authorData == null || "undefined".equals(authorData)) {
            return null;
        }
        String[] data = authorData.split(" ");
        if (data == null || data.length <= 0) {
            return null;
        }
        String name = data[0];
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        List<Ebook> result = null;
        switch (data.length) {
            case 1:
                result = ebookRepository.findByWriterNameLikeOrderByRating(name);
                break;
            case 2:
                result = ebookRepository.findByWriterNameLikeAndSurnameLikeOrderByRating(name, data[1]);
                break;
            case 3:
                result = ebookRepository.findByWriterNameLikeAndSurnameLikeAndName2LikeOrderByRating(name, data[1], data[2]);
                break;
            default:
                break;
        }
        result = result.stream().filter(ebook ->
                (ebook.getEbookFilesByEbookFileId() != null && ebook.getEbookFilesByEbookFileId().getPublic()))
                .collect(Collectors.toList());
        return ebookMapper.toEbookDTOList(result);
    }

    private List<EbookDTO> getEbooksByLiteraryGenreName(String name) {
        if (name == null || "undefined".equals(name)) {
            return null;
        }
        List<Ebook> ebookList = ebookRepository.findByTypeNameOrderByRating(name);
        if (ebookList == null) {
            ebookList = new ArrayList<>();
        }
        name += "%";
        ebookList.addAll(ebookRepository.findByTypeNameLikeOrderByRating(name));
        ebookList = ebookList.stream().filter(ebook ->
                (ebook.getEbookFilesByEbookFileId() != null && ebook.getEbookFilesByEbookFileId().getPublic()))
                .collect(Collectors.toList());
        return ebookMapper.toEbookDTOList(ebookList);
    }

    private List<EbookDTO> getEbooksByYear(String year) {
        if (year == null || "undefined".equals(year) || !NumberUtils.isNumber(year)) {
            return null;
        }
        Short yearNumber = Short.parseShort(year);
        List<Ebook> ebookList = ebookRepository.findByYearOrderByRating(yearNumber);
        ebookList = ebookList.stream().filter(ebook ->
                (ebook.getEbookFilesByEbookFileId() != null && ebook.getEbookFilesByEbookFileId().getPublic()))
                .collect(Collectors.toList());
        return ebookMapper.toEbookDTOList(ebookList);
    }

    private List<EbookDTO> getEbooksByAllCriteria(String searchText) {
        if (StringUtils.isEmpty(searchText)) {
            return null;
        }
        List<EbookDTO> result = new ArrayList<>();
        List<EbookDTO> ebooksByTitle = getEbooksByTitle(searchText);
        List<EbookDTO> ebooksByYear = getEbooksByYear(searchText);
        List<EbookDTO> ebooksByAuthor = getEbooksByAuthor(searchText);
        List<EbookDTO> ebooksByLiteraryGenreName = getEbooksByLiteraryGenreName(searchText);
        if (ebooksByTitle != null && !ebooksByTitle.isEmpty()) {
            result.addAll(ebooksByTitle);
        }
        if (ebooksByYear != null && !ebooksByYear.isEmpty()) {
            result.addAll(ebooksByYear);
        }
        if (ebooksByAuthor != null && !ebooksByAuthor.isEmpty()) {
            result.addAll(ebooksByAuthor);
        }
        if (ebooksByLiteraryGenreName != null && !ebooksByLiteraryGenreName.isEmpty()) {
            result.addAll(ebooksByLiteraryGenreName);
        }
        return result;
    }

    @Override
    public EbookDTO updateEbookMetadata(EbookDTO ebookDTO) {

        Ebook ebook = ebookMapper.toEbook(ebookDTO);
        List<Writers> writersList = ebookMapper.toWritersList(ebookDTO.getWriterDTOList());
        saveLiteraryGenreForEbook(ebookDTO, ebook);
        saveEbookFileMetadataForEbook(ebookDTO, ebook);
        ebook = ebookRepository.save(ebook);

        saveWritersForEbook(ebook, writersList);
        List<EbookAuthors> imageAuthorsList = ebookAuthorsRepository.findByEbookId(ebook.getEbookId());
        ebook.setEbookAuthorsesByEbookId(imageAuthorsList);

        ebook = ebookRepository.save(ebook);

        EbookDTO result = ebookMapper.toEbookDTO(ebook);
        return result;
    }

    @Override
    public List<EbookDTO> getEbooksTop50() {
        Iterable<Ebook> all = ebookRepository.findAllByOrderByRatingDesc();
        if (all == null) {
            return null;
        }
        List<Ebook> allEbooks = new ArrayList<>();
        all.forEach(ebook -> {
            if (ebook.getEbookFilesByEbookFileId() != null && ebook.getEbookFilesByEbookFileId().getPublic()) {
                allEbooks.add(ebook);
            }
        });
        allEbooks.sort((o1, o2) -> compareFloat(o1.getRating(), o2.getRating()));
        List<EbookDTO> result = ebookMapper.toEbookDTOList(allEbooks);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.subList(0, result.size() > 49 ? 49 : result.size());
        }
    }

    private int compareFloat(Float f1, Float f2) {
        if (f1 == null && f2 == null) {
            return 0;
        }
        if (f1 == null) {
            return 1;
        }
        if (f2 == null) {
            return -1;
        }
        return f2.compareTo(f1);
    }

}

