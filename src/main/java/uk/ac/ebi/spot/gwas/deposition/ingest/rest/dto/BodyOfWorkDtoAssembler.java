package uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto;

import uk.ac.ebi.spot.gwas.deposition.domain.BodyOfWork;
import uk.ac.ebi.spot.gwas.deposition.dto.AuthorDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.BodyOfWorkDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BodyOfWorkDtoAssembler {

    public static BodyOfWorkDto assemble(BodyOfWork bodyOfWork) {
        List<AuthorDto> correspondingAuthorDtoList = new ArrayList<>();
        if (bodyOfWork.getCorrespondingAuthors() != null) {
            correspondingAuthorDtoList = bodyOfWork.getCorrespondingAuthors().stream().map(AuthorDtoAssembler::assemble).collect(Collectors.toList());
        }

        return new BodyOfWorkDto(bodyOfWork.getBowId(),
                bodyOfWork.getTitle(),
                bodyOfWork.getDescription(),
                AuthorDtoAssembler.assemble(bodyOfWork.getFirstAuthor()),
                AuthorDtoAssembler.assemble(bodyOfWork.getLastAuthor()),
                bodyOfWork.getJournal(),
                bodyOfWork.getDoi(),
                bodyOfWork.getUrl(),
                correspondingAuthorDtoList,
                bodyOfWork.getPrePrintServer(),
                bodyOfWork.getPreprintServerDOI(),
                bodyOfWork.getEmbargoDate(),
                bodyOfWork.getEmbargoUntilPublished(),
                bodyOfWork.getPmids(),
                bodyOfWork.getStatus());
    }

}
