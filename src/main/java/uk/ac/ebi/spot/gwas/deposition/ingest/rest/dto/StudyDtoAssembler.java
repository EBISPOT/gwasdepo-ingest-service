package uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto;

import uk.ac.ebi.spot.gwas.deposition.domain.Study;
import uk.ac.ebi.spot.gwas.deposition.dto.AssociationDto;
import uk.ac.ebi.spot.gwas.deposition.dto.NoteDto;
import uk.ac.ebi.spot.gwas.deposition.dto.SampleDto;
import uk.ac.ebi.spot.gwas.deposition.dto.StudyDto;

import java.util.List;

public class StudyDtoAssembler {

    public static StudyDto assemble(Study study) {
        return new StudyDto(study.getStudyTag(),
                study.getAccession(),
                study.getGenotypingTechnology(),
                study.getArrayManufacturer(),
                study.getArrayInformation(),
                study.getImputation(),
                study.getVariantCount(),
                study.getSampleDescription(),
                study.getStatisticalModel(),
                study.getStudyDescription(),
                study.getTrait(),
                study.getEfoTrait(),
                study.getBackgroundTrait(),
                study.getBackgroundEfoTrait(),
                study.getSummaryStatisticsFile(),
                study.getRawFilePath(),
                study.getChecksum(),
                study.getSummaryStatisticsAssembly(),
                study.getReadmeFile(),
                study.getCohort(),
                study.getCohortId(),
                null,
                null,
                null,
                study.isAgreedToCc0());
    }

    public static StudyDto assemble(Study study, List<AssociationDto> associationDtos,
                                    List<SampleDto> sampleDtos, List<NoteDto> noteDtos) {
        return new StudyDto(study.getStudyTag(),
                study.getAccession(),
                study.getGenotypingTechnology(),
                study.getArrayManufacturer(),
                study.getArrayInformation(),
                study.getImputation(),
                study.getVariantCount(),
                study.getSampleDescription(),
                study.getStatisticalModel(),
                study.getStudyDescription(),
                study.getTrait(),
                study.getEfoTrait(),
                study.getBackgroundTrait(),
                study.getBackgroundEfoTrait(),
                study.getSummaryStatisticsFile(),
                study.getRawFilePath(),
                study.getChecksum(),
                study.getSummaryStatisticsAssembly(),
                study.getReadmeFile(),
                study.getCohort(),
                study.getCohortId(),
                associationDtos,
                sampleDtos,
                noteDtos,
                study.isAgreedToCc0());
    }

    public static Study disassemble(StudyDto studyDto) {
        Study study = new Study();

        study.setStudyTag(studyDto.getStudyTag());
        study.setAccession(studyDto.getAccession());
        study.setGenotypingTechnology(studyDto.getGenotypingTechnology());
        study.setArrayManufacturer(studyDto.getArrayManufacturer());
        study.setArrayInformation(studyDto.getArrayInformation());
        study.setImputation(studyDto.getImputation());
        study.setVariantCount(studyDto.getVariantCount());
        study.setSampleDescription(studyDto.getSampleDescription());
        study.setStatisticalModel(studyDto.getStatisticalModel());
        study.setStudyDescription(studyDto.getStudyDescription());
        study.setTrait(studyDto.getTrait());
        study.setEfoTrait(studyDto.getEfoTrait());
        study.setBackgroundTrait(studyDto.getBackgroundTrait());
        study.setBackgroundEfoTrait(studyDto.getBackgroundEfoTrait());
        study.setSummaryStatisticsFile(studyDto.getSummaryStatisticsFile());
        study.setChecksum(studyDto.getChecksum());
        study.setSummaryStatisticsAssembly(studyDto.getSummaryStatisticsAssembly());
        study.setReadmeFile(studyDto.getReadmeFile());
        study.setCohort(studyDto.getCohort());
        study.setCohortId(studyDto.getCohortId());

        return study;
    }
}
