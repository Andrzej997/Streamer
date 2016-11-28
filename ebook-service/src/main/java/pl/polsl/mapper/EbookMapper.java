package pl.polsl.mapper;

import pl.polsl.dto.EbookDTO;
import pl.polsl.dto.EbookFileMetadataDTO;
import pl.polsl.dto.LiteraryGenreDTO;
import pl.polsl.dto.WriterDTO;
import pl.polsl.model.Ebook;
import pl.polsl.model.EbookFiles;
import pl.polsl.model.LiteraryGenre;
import pl.polsl.model.Writers;

import java.util.List;

/**
 * Created by Mateusz on 28.11.2016.
 */
public interface EbookMapper {
    EbookFileMetadataDTO toEbookFileMetadataDTO(EbookFiles ebookFiles);

    List<EbookFileMetadataDTO> toEbookFileMetadataDTOList(List<EbookFiles> ebookFilesList);

    EbookFiles toEbookFiles(EbookFileMetadataDTO ebookFileMetadataDTO);

    List<EbookFiles> toEbookFilesList(List<EbookFileMetadataDTO> ebookFileMetadataDTOList);

    LiteraryGenreDTO toLiteraryGenreDTO(LiteraryGenre literaryGenre);

    List<LiteraryGenreDTO> toLiteraryGenreDTOList(List<LiteraryGenre> literaryGenreList);

    LiteraryGenre toLiteraryGenre(LiteraryGenreDTO literaryGenreDTO);

    List<LiteraryGenre> toLiteraryGenreList(List<LiteraryGenreDTO> literaryGenreDTOList);

    WriterDTO toWriterDTO(Writers writers);

    List<WriterDTO> toWriterDTOList(List<Writers> writersList);

    Writers toWriters(WriterDTO writerDTO);

    List<Writers> toWritersList(List<WriterDTO> writerDTOList);

    EbookDTO toEbookDTO(Ebook ebook);

    List<EbookDTO> toEbookDTOList(List<Ebook> ebookList);

    Ebook toEbook(EbookDTO ebookDTO);

    List<Ebook> toEbookList(List<EbookDTO> ebookDTOList);
}
