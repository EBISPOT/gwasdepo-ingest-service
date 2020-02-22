package uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto;

import uk.ac.ebi.spot.gwas.deposition.domain.SSTemplateEntry;
import uk.ac.ebi.spot.gwas.deposition.domain.SSTemplateEntryPlaceholder;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.ExtendedPublicationDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.PublicationDto;
import uk.ac.ebi.spot.gwas.deposition.dto.summarystats.SSTemplateEntryDto;

import java.util.ArrayList;
import java.util.List;

public class ExtendedPublicationDtoAssembler {

    public static ExtendedPublicationDto assemble(PublicationDto publicationDto, List<SSTemplateEntry> ssTemplateEntries) {
        List<SSTemplateEntryDto> ssTemplateEntryDtos = null;
        if (ssTemplateEntries != null) {
            ssTemplateEntryDtos = new ArrayList<>();
            for (SSTemplateEntry ssTemplateEntry : ssTemplateEntries) {
                ssTemplateEntryDtos.add(new SSTemplateEntryDto(ssTemplateEntry.getStudyAccession(),
                        ssTemplateEntry.getStudyTag(),
                        ssTemplateEntry.getTrait(),
                        ssTemplateEntry.getSampleDescription(),
                        ssTemplateEntry.getHasSummaryStats()));
            }
        }

        return new ExtendedPublicationDto(publicationDto.getPmid(),
                publicationDto.getTitle(),
                publicationDto.getJournal(),
                publicationDto.getFirstAuthor(),
                publicationDto.getPublicationDate(),
                publicationDto.getCorrespondingAuthor(),
                publicationDto.getStatus(),
                ssTemplateEntryDtos);
    }

    public static PublicationDto disassemble(ExtendedPublicationDto extendedPublicationDto) {
        return new PublicationDto(extendedPublicationDto.getPmid(),
                extendedPublicationDto.getTitle(),
                extendedPublicationDto.getJournal(),
                extendedPublicationDto.getFirstAuthor(),
                extendedPublicationDto.getPublicationDate(),
                extendedPublicationDto.getCorrespondingAuthor(),
                extendedPublicationDto.getStatus());
    }

    public static SSTemplateEntryPlaceholder disassembleTemplateEntries(ExtendedPublicationDto extendedPublicationDto) {
        if (extendedPublicationDto.getSsTemplateEntries() == null) {
            return null;
        }

        List<SSTemplateEntry> ssTemplateEntries = new ArrayList<>();
        for (SSTemplateEntryDto ssTemplateEntryDto : extendedPublicationDto.getSsTemplateEntries()) {
            ssTemplateEntries.add(new SSTemplateEntry(ssTemplateEntryDto.getStudyAccession(), ssTemplateEntryDto.getStudyTag(),
                    ssTemplateEntryDto.getTrait(), ssTemplateEntryDto.getSampleDescription(), ssTemplateEntryDto.getHasSummaryStats()));
        }

        return new SSTemplateEntryPlaceholder(extendedPublicationDto.getPmid(), ssTemplateEntries);
    }
}
