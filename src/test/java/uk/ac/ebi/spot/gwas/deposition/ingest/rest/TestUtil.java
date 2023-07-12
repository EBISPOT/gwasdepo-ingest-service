package uk.ac.ebi.spot.gwas.deposition.ingest.rest;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import uk.ac.ebi.spot.gwas.deposition.constants.PublicationStatus;
import uk.ac.ebi.spot.gwas.deposition.domain.CorrespondingAuthor;
import uk.ac.ebi.spot.gwas.deposition.domain.Publication;
import uk.ac.ebi.spot.gwas.deposition.domain.User;

public class TestUtil {

    public static User user() {
        return new User(RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10));
    }

    public static Publication publication() {

        Publication publication = new Publication();
        publication.setJournal(RandomStringUtils.randomAlphanumeric(10));
        publication.setTitle(RandomStringUtils.randomAlphanumeric(10));
        publication.setFirstAuthor(RandomStringUtils.randomAlphanumeric(10));
        publication.setPublicationDate(LocalDate.now());
        publication.setCorrespondingAuthor(new CorrespondingAuthor(RandomStringUtils.randomAlphanumeric(10),
                RandomStringUtils.randomAlphanumeric(10)));
        publication.setStatus(PublicationStatus.ELIGIBLE.name());

        return publication;
    }

}
