package uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import uk.ac.ebi.spot.gwas.deposition.domain.Sample;
import uk.ac.ebi.spot.gwas.deposition.domain.Submission;
import uk.ac.ebi.spot.gwas.deposition.dto.SampleDto;
import uk.ac.ebi.spot.gwas.deposition.dto.ingest.SubmissionDto;

@Component
public class SampleAssembler implements ResourceAssembler<Sample, Resource<SampleDto>> {

    @Override
    public Resource<SampleDto> toResource(Sample sample) {

        SampleDto sampleDto = new SampleDto(sample.getStudyTag(),
                sample.getStage(),
                sample.getSize(),
                sample.getCases(),
                sample.getControls(),
                sample.getSampleDescription(),
                sample.getAncestryCategory(),
                sample.getAncestry(),
                sample.getAncestryDescription(),
                sample.getCountryRecruitement());

        return new Resource<>(sampleDto);
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
