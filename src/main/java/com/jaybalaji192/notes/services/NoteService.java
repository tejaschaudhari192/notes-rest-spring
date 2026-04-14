package com.jaybalaji192.notes.services;

import com.jaybalaji192.notes.models.Note;
import com.jaybalaji192.notes.repositories.NoteRepository;
import com.jaybalaji192.notes.requests.NoteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note addNote(NoteRequest request, Principal principal) {
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
            } else {
                throw new IllegalStateException("Not Your Note");
            }
        } else {
            throw new IllegalArgumentException("Note not Found");
        }
    }

    public Note updateNote(String id, NoteRequest request, Principal principal) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Note not found with id: " + id));
        if (!note.getUsername().equals(principal.getName())) {
            throw new IllegalStateException("Not Your Note");
        }
        note.updateNote(request.title(), request.content());
        return noteRepository.save(note);
    }


    public List<Note> getAllNotes(Principal principal) {
        String username = principal.getName();
        return noteRepository.findByUsername(username);
    }
}