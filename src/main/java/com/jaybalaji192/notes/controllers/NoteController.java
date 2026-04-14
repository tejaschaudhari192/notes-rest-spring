package com.jaybalaji192.notes.controllers;

import com.jaybalaji192.notes.models.Note;
import com.jaybalaji192.notes.requests.NoteRequest;
import com.jaybalaji192.notes.services.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);
    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<Note> addNote(@RequestBody NoteRequest request, Principal principal) {
        Note note = noteService.addNote(request, principal);
        logger.debug("Note added successfully with title: {}", note.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(note);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(@PathVariable String id, @RequestBody NoteRequest request, Principal principal) {
        try {
            Note updatedNote = noteService.updateNote(id, request, principal);
            logger.debug("Note with id: {} updated successfully", id);
            return ResponseEntity.ok(updatedNote);
        } catch (IllegalArgumentException e) {
            logger.warn("Update failed: Note not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            logger.warn("Update failed: User '{}' does not own note with id: {}", principal.getName(), id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable String id, Principal principal) {
        try {
            noteService.deleteNote(id, principal);
            logger.debug("Note with id: {} deleted successfully", id);
            return ResponseEntity.ok("Deleted Successfully");
        } catch (IllegalArgumentException e) {
            logger.warn("Delete failed: Note not found with id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            logger.warn("Delete failed: User '{}' does not own note with id: {}", principal.getName(), id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes(Principal principal) {
        List<Note> notes = noteService.getAllNotes(principal);
        logger.debug("Retrieved {} notes for user '{}'", notes.size(), principal.getName());
        return ResponseEntity.ok(notes);
    }
}