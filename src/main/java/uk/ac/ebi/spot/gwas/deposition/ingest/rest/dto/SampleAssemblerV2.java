package uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import uk.ac.ebi.spot.gwas.deposition.domain.Sample;
import uk.ac.ebi.spot.gwas.deposition.dto.SampleDto;

@Component
public class SampleAssemblerV2 implements ResourceAssembler<Sample, Resource<SampleDto>> {

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
                sample.getCountryRecruitement(),
                sample.getCaseControlStudy(),
                sample.getAncestryMethod());

        return new Resource<>(sampleDto);
    }

}
