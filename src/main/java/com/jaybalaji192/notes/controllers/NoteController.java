package com.jaybalaji192.notes.controllers;

import com.jaybalaji192.notes.models.Note;
import com.jaybalaji192.notes.requests.NoteRequest;
import com.jaybalaji192.notes.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<Note> addNote(@RequestBody NoteRequest request, Principal principal) {
        Note note = noteService.addNote(request, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(note);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(@PathVariable String id, @RequestBody NoteRequest request, Principal principal) {
        try {
            Note updatedNote = noteService.updateNote(id, request, principal);
            return ResponseEntity.ok(updatedNote);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable String id, Principal principal) {
        try {
            noteService.deleteNote(id, principal);
            return ResponseEntity.ok("Deleted Successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes(Principal principal) {
        List<Note> notes = noteService.getAllNotes(principal);
        return ResponseEntity.ok(notes);
    }
}