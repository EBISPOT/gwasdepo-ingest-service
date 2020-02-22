package uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto;

import uk.ac.ebi.spot.gwas.deposition.domain.Sample;
import uk.ac.ebi.spot.gwas.deposition.dto.SampleDto;

public class SampleDtoAssembler {

    public static SampleDto assemble(Sample sample) {
        return new SampleDto(sample.getStudyTag(),
                sample.getStage(),
                sample.getSize(),
                sample.getCases(),
                sample.getControls(),
                sample.getSampleDescription(),
                sample.getAncestryCategory(),
                sample.getAncestry(),
                sample.getAncestryDescription(),
                sample.getCountryRecruitement());
    }

    public static Sample disassemble(SampleDto sampleDto) {
        Sample sample = new Sample();
        sample.setStudyTag(sampleDto.getStudyTag());
        sample.setStage(sampleDto.getStage());
        sample.setSize(sampleDto.getSize());
        sample.setCases(sampleDto.getCases());
        sample.setControls(sampleDto.getControls());
        sample.setSampleDescription(sampleDto.getSampleDescription());
        sample.setAncestryCategory(sampleDto.getAncestryCategory());
        sample.setAncestry(sampleDto.getAncestry());
        sample.setAncestryDescription(sampleDto.getAncestryDescription());
        sample.setCountryRecruitement(sampleDto.getCountryRecruitement());
        return sample;
    }
}
