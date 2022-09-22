package uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto;


import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import uk.ac.ebi.spot.gwas.deposition.domain.Association;
import uk.ac.ebi.spot.gwas.deposition.domain.Study;
import uk.ac.ebi.spot.gwas.deposition.dto.AssociationDto;
import uk.ac.ebi.spot.gwas.deposition.dto.StudyDto;

@Component
public class AssociationAssembler implements ResourceAssembler<Association, Resource<AssociationDto>> {

    @Override
    public Resource<AssociationDto> toResource(Association association) {

        AssociationDto associationDto = new AssociationDto(association.getStudyTag(),
                association.getHaplotypeId(),
                association.getVariantId(),
                association.getPvalue(),
                association.getPvalueText(),
                association.getProxyVariant(),
                association.getEffectAllele(),
                association.getOtherAllele(),
                association.getEffectAlleleFrequency(),
                association.getOddsRatio(),
                association.getBeta(),
                association.getBetaUnit(),
                association.getCiLower(),
                association.getCiUpper(),
                association.getStandardError());

        return new Resource<>(associationDto);
    }

    public static Association disassemble(AssociationDto associationDto) {
        Association association = new Association();

        association.setStudyTag(associationDto.getStudyTag());
        association.setHaplotypeId(associationDto.getHaplotypeId());
        association.setVariantId(associationDto.getVariantId());
        association.setPvalue(associationDto.getPvalue());
        association.setPvalueText(associationDto.getPvalueText());
        association.setProxyVariant(associationDto.getProxyVariant());
        association.setEffectAllele(associationDto.getEffectAllele());
        association.setOtherAllele(associationDto.getOtherAllele());
        association.setEffectAlleleFrequency(associationDto.getEffectAlleleFrequency());
        association.setOddsRatio(associationDto.getOddsRatio());
        association.setBeta(associationDto.getBeta());
        association.setBetaUnit(associationDto.getBetaUnit());
        association.setCiLower(associationDto.getCiLower());
        association.setCiUpper(associationDto.getCiUpper());
        association.setStandardError(associationDto.getStandardError());

        return association;
    }

}
