package pl.polsl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.polsl.dto.EbookDTO;
import pl.polsl.dto.LiteraryGenreDTO;
import pl.polsl.dto.UploadEbookMetadataDTO;
import pl.polsl.dto.WriterDTO;
import pl.polsl.mapper.EbookMapper;
import pl.polsl.model.*;
import pl.polsl.repository.*;
import pl.polsl.repository.custom.UsersRepositoryCustom;
import pl.polsl.service.EbookMetadataService;

import java.util.ArrayList;
import java.util.List;

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
        if (ebookDTO.getLiteraryGenreDTO() != null && ebookDTO.getLiteraryGenreDTO().getGenreId() == null) {
            literaryGenre = literaryGenreRepository.save(ebookMapper.toLiteraryGenre(ebookDTO.getLiteraryGenreDTO()));
        } else if (ebookDTO.getLiteraryGenreDTO() != null) {
            literaryGenre = ebookMapper.toLiteraryGenre(ebookDTO.getLiteraryGenreDTO());
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
            if (writers.getWriterId() == null) {
                writers = writersRepository.save(writers);
            }
            EbookAuthors ebookAuthors = new EbookAuthors();
            ebookAuthors.setAuthorId(writers.getWriterId());
            ebookAuthors.setWritersByAuthorId(writers);
            ebookAuthors.setEbookId(ebook.getEbookId());
            ebookAuthors.setEbookByEbookId(ebook);
            ebookAuthorsRepository.save(ebookAuthors);
        }
    }
}

