package pl.polsl.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import pl.polsl.dto.EbookDTO;
import pl.polsl.dto.EbookFileMetadataDTO;
import pl.polsl.dto.LiteraryGenreDTO;
import pl.polsl.dto.WriterDTO;
import pl.polsl.mapper.EbookMapper;
import pl.polsl.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Mateusz on 28.11.2016.
 */
@Component
public class EbookMapperImpl implements EbookMapper {

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public EbookFileMetadataDTO toEbookFileMetadataDTO(EbookFiles ebookFiles) {
        if (ebookFiles == null) {
            return null;
        }
        return modelMapper.map(ebookFiles, EbookFileMetadataDTO.class);
    }

    @Override
    public List<EbookFileMetadataDTO> toEbookFileMetadataDTOList(List<EbookFiles> ebookFilesList) {
        if (ebookFilesList == null) {
            return null;
        }
        List<EbookFileMetadataDTO> result = new ArrayList<>();
        ebookFilesList.forEach(ebookFiles -> result.add(toEbookFileMetadataDTO(ebookFiles)));
        return result;
    }

    @Override
    public EbookFiles toEbookFiles(EbookFileMetadataDTO ebookFileMetadataDTO) {
        if (ebookFileMetadataDTO == null) {
            return null;
        }
        return modelMapper.map(ebookFileMetadataDTO, EbookFiles.class);
    }

    @Override
    public List<EbookFiles> toEbookFilesList(List<EbookFileMetadataDTO> ebookFileMetadataDTOList) {
        if (ebookFileMetadataDTOList == null) {
            return null;
        }
        List<EbookFiles> result = new ArrayList<>();
        ebookFileMetadataDTOList.forEach(ebookFileMetadataDTO -> result.add(toEbookFiles(ebookFileMetadataDTO)));
        return result;
    }

    @Override
    public LiteraryGenreDTO toLiteraryGenreDTO(LiteraryGenre literaryGenre) {
        if (literaryGenre == null) {
            return null;
        }
        return modelMapper.map(literaryGenre, LiteraryGenreDTO.class);
    }

    @Override
    public List<LiteraryGenreDTO> toLiteraryGenreDTOList(List<LiteraryGenre> literaryGenreList) {
        if (literaryGenreList == null) {
            return null;
        }
        List<LiteraryGenreDTO> result = new ArrayList<>();
        literaryGenreList.forEach(literaryGenre -> result.add(toLiteraryGenreDTO(literaryGenre)));
        return result;
    }

    @Override
    public LiteraryGenre toLiteraryGenre(LiteraryGenreDTO literaryGenreDTO) {
        if (literaryGenreDTO == null) {
            return null;
        }
        return modelMapper.map(literaryGenreDTO, LiteraryGenre.class);
    }

    @Override
    public List<LiteraryGenre> toLiteraryGenreList(List<LiteraryGenreDTO> literaryGenreDTOList) {
        if (literaryGenreDTOList == null) {
            return null;
        }
        List<LiteraryGenre> result = new ArrayList<>();
        literaryGenreDTOList.forEach(literaryGenreDTO -> result.add(toLiteraryGenre(literaryGenreDTO)));
        return result;
    }

    @Override
    public WriterDTO toWriterDTO(Writers writers) {
        if (writers == null) {
            return null;
        }
        return modelMapper.map(writers, WriterDTO.class);
    }

    @Override
    public List<WriterDTO> toWriterDTOList(List<Writers> writersList) {
        if (writersList == null) {
            return null;
        }
        List<WriterDTO> result = new ArrayList<>();
        writersList.forEach(writers -> result.add(toWriterDTO(writers)));
        return result;
    }

    @Override
    public Writers toWriters(WriterDTO writerDTO) {
        if (writerDTO == null) {
            return null;
        }
        return modelMapper.map(writerDTO, Writers.class);
    }

    @Override
    public List<Writers> toWritersList(List<WriterDTO> writerDTOList) {
        if (writerDTOList == null) {
            return null;
        }
        List<Writers> result = new ArrayList<>();
        writerDTOList.forEach(writerDTO -> result.add(toWriters(writerDTO)));
        return result;
    }

    @Override
    public EbookDTO toEbookDTO(Ebook ebook) {
        if (ebook == null) {
            return null;
        }
        EbookFiles ebookFiles = ebook.getEbookFilesByEbookFileId();
        LiteraryGenre literaryGenre = ebook.getLiteraryGenreByGenreId();
        Collection<EbookAuthors> ebookAuthorses = ebook.getEbookAuthorsesByEbookId();
        EbookDTO ebookDTO = new EbookDTO();
        ebookDTO.setEbookId(ebook.getEbookId());
        ebookDTO.setTitle(ebook.getTitle());
        ebookDTO.setNumOfPages(ebook.getNumOfPages());
        ebookDTO.setYear(ebook.getYear());
        ebookDTO.setRating(ebook.getRating());
        ebookDTO.setGenreId(ebook.getGenreId());
        ebookDTO.setComments(ebook.getComments());
        ebookDTO.setAuthorId(ebook.getAuthorId());
        ebookDTO.setOwnerId(ebook.getOwnerId());
        ebookDTO.setEbookFileId(ebook.getEbookFileId());
        ebookDTO.setEbookFileMetadataDTO(toEbookFileMetadataDTO(ebookFiles));
        ebookDTO.setLiteraryGenreDTO(toLiteraryGenreDTO(literaryGenre));
        if (ebookAuthorses != null) {
            List<WriterDTO> writerDTOList = new ArrayList<>();
            for (EbookAuthors ebookAuthors : ebookAuthorses) {
                writerDTOList.add(toWriterDTO(ebookAuthors.getWritersByAuthorId()));
            }
            ebookDTO.setWriterDTOList(writerDTOList);
        }
        return ebookDTO;
    }

    @Override
    public List<EbookDTO> toEbookDTOList(List<Ebook> ebookList) {
        if (ebookList == null) {
            return null;
        }
        List<EbookDTO> result = new ArrayList<>();
        ebookList.forEach(ebook -> result.add(toEbookDTO(ebook)));
        return result;
    }

    @Override
    public Ebook toEbook(EbookDTO ebookDTO) {
        if (ebookDTO == null) {
            return null;
        }
        EbookFileMetadataDTO ebookFileMetadataDTO = ebookDTO.getEbookFileMetadataDTO();
        LiteraryGenreDTO literaryGenreDTO = ebookDTO.getLiteraryGenreDTO();
        Ebook ebook = new Ebook();
        ebook.setEbookId(ebookDTO.getEbookId());
        ebook.setTitle(ebookDTO.getTitle());
        ebook.setNumOfPages(ebookDTO.getNumOfPages());
        ebook.setYear(ebookDTO.getYear());
        ebook.setRating(ebookDTO.getRating());
        ebook.setGenreId(ebookDTO.getGenreId());
        ebook.setComments(ebookDTO.getComments());
        ebook.setAuthorId(ebookDTO.getAuthorId());
        ebook.setOwnerId(ebookDTO.getOwnerId());
        ebook.setEbookFileId(ebookDTO.getEbookFileId());
        ebook.setEbookFilesByEbookFileId(toEbookFiles(ebookFileMetadataDTO));
        ebook.setLiteraryGenreByGenreId(toLiteraryGenre(literaryGenreDTO));
        return ebook;
    }

    @Override
    public List<Ebook> toEbookList(List<EbookDTO> ebookDTOList) {
        if (ebookDTOList == null) {
            return null;
        }
        List<Ebook> result = new ArrayList<>();
        ebookDTOList.forEach(ebookDTO -> result.add(toEbook(ebookDTO)));
        return result;
    }
}
