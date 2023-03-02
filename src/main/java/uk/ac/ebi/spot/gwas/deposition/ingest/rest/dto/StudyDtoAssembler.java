package uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.ebi.spot.gwas.deposition.domain.Study;
import uk.ac.ebi.spot.gwas.deposition.dto.AssociationDto;
import uk.ac.ebi.spot.gwas.deposition.dto.NoteDto;
import uk.ac.ebi.spot.gwas.deposition.dto.SampleDto;
import uk.ac.ebi.spot.gwas.deposition.dto.StudyDto;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.DiseaseTraitRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.repository.EfoTraitRepository;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.DiseaseTraitAssemblyService;
import uk.ac.ebi.spot.gwas.deposition.ingest.service.EFOTraitAssemblyService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class StudyDtoAssembler {

    private static final Logger log = LoggerFactory.getLogger(StudyDtoAssembler.class);

    @Autowired
    DiseaseTraitAssemblyService diseaseTraitAssemblyService;

    @Autowired
    EFOTraitAssemblyService efoTraitAssemblyService;

    @Autowired
    DiseaseTraitRepository diseaseTraitRepository;

    @Autowired
    EfoTraitRepository efoTraitRepository;

    public  StudyDto assemble(Study study) {

        log.info("Study Accession Id {}",study.getAccession());
        //DiseaseTrait diseaseTrait = diseaseTraitOptional.get();


        return new StudyDto(
                study.getStudyTag(),
                study.getId(),
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
                study.isAgreedToCc0(),
                Optional.ofNullable(study.getDiseaseTrait())
                        .map(diseaseTraitRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(diseaseTraitAssemblyService::assembleDTO)
                        .orElse(null) ,
                study.getEfoTraits() != null ? study.getEfoTraits().stream().map(efoTraitRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(efoTraitAssemblyService::assembleDTO)
                        .collect(Collectors.toList()) : null,
                study.getBackgroundEfoTraits() != null ? study.getBackgroundEfoTraits().stream().map(efoTraitRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(efoTraitAssemblyService::assembleDTO)
                        .collect(Collectors.toList()) : null,
                study.getInitialSampleDescription(),
                study.getReplicateSampleDescription(),
                study.getSumstatsFlag(),
                study.getPooledFlag(),
                study.getGxeFlag(),
                study.getSubmissionId(),
                study.getImputationPanel(),
                study.getImputationSoftware(),
                study.getAdjustedCovariates(),
                study.getNeg_log_p_value(),
                study.getEffect_allele_frequency_lower_limit()
                );
    }

    public static StudyDto assemble(Study study, List<AssociationDto> associationDtos,
                                    List<SampleDto> sampleDtos, List<NoteDto> noteDtos) {
        return new StudyDto(study.getStudyTag(),
                study.getId(),
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
                study.isAgreedToCc0()
                ,null,
                null,
                null,
                null,
                null,
                study.getSumstatsFlag(),
                study.getPooledFlag(),
                study.getGxeFlag(),
                study.getSubmissionId(),
                study.getImputationPanel(),
                study.getImputationSoftware(),
                study.getAdjustedCovariates(),
                study.getNeg_log_p_value(),
                study.getEffect_allele_frequency_lower_limit()
                );
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
