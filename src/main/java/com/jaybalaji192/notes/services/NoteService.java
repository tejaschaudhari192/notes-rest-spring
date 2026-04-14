package com.jaybalaji192.notes.services;

import com.jaybalaji192.notes.models.Note;
import com.jaybalaji192.notes.repositories.NoteRepository;
import com.jaybalaji192.notes.requests.NoteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private static final Logger logger = LoggerFactory.getLogger(NoteService.class);
    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note addNote(NoteRequest request, Principal principal) {
        logger.debug("Saving new note for user '{}'", principal.getName());
        Note note = new Note(
                principal.getName(),
                request.title(),
                request.content()
        );
        return noteRepository.save(note);
    }

    public void deleteNote(String id, Principal principal) {
        Optional<Note> optionalNote = noteRepository.findById(id);
        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();
            if (note.getUsername().equals(principal.getName())) {
                noteRepository.deleteById(id);
                logger.debug("Note '{}' deleted successfully from repository", id);
            } else {
                logger.error("Unauthorized delete attempt on note '{}' by user '{}'", id, principal.getName());
                throw new IllegalStateException("Not Your Note");
            }
        } else {
            logger.warn("Note '{}' not found for deletion", id);
            throw new IllegalArgumentException("Note not Found");
        }
    }

    public Note updateNote(String id, NoteRequest request, Principal principal) {
        Note note = noteRepository.findById(id).orElseThrow(() -> {
            logger.warn("Note '{}' not found for update", id);
            return new IllegalArgumentException("Note not found with id: " + id);
        });
        
        if (!note.getUsername().equals(principal.getName())) {
            logger.error("Unauthorized update attempt on note '{}' by user '{}'", id, principal.getName());
            throw new IllegalStateException("Not Your Note");
        }
        
        note.updateNote(request.title(), request.content());
        Note savedNote = noteRepository.save(note);
        logger.debug("Note '{}' updated successfully in repository", id);
        return savedNote;
    }

    public List<Note> getAllNotes(Principal principal) {
        String username = principal.getName();
        logger.debug("Retrieving all notes from repository for user '{}'", username);
        return noteRepository.findByUsername(username);
    }
}