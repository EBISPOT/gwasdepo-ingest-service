package uk.ac.ebi.spot.gwas.deposition.ingest.rest.dto;


import uk.ac.ebi.spot.gwas.deposition.domain.Note;
import uk.ac.ebi.spot.gwas.deposition.dto.NoteDto;

public class NoteDtoAssembler {

    public static NoteDto assemble(Note note) {
        return new NoteDto(note.getStudyTag(),
                note.getNote(),
                note.getNoteSubject(),
                note.getStatus());
    }

    public static Note disassemble(NoteDto noteDto) {
        return new Note(noteDto.getStudyTag(),
                noteDto.getNote(),
                noteDto.getNoteSubject(),
                noteDto.getStatus());
    }
}
