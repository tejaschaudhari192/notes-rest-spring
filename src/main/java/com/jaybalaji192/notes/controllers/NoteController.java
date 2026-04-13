package com.jaybalaji192.notes.controllers;

import com.jaybalaji192.notes.models.Note;
import com.jaybalaji192.notes.repositories.NoteRepository;
import com.jaybalaji192.notes.requests.NoteRequest;
import com.jaybalaji192.notes.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteRepository noteRepository;
    private final NoteService noteService;


    @Autowired
    public NoteController(NoteRepository noteRepository, NoteService noteService) {
        this.noteRepository = noteRepository;
        this.noteService = noteService;
    }

    @PostMapping
    public Note addNote(@RequestBody NoteRequest request){
        return noteService.addNote(request);
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable String id,@RequestBody NoteRequest request){
        return noteService.updateNote(id,request);
    }

    @DeleteMapping("/{id}")
    public String addNote(@PathVariable String id){
        return noteService.deleteNote(id);
    }

    @GetMapping
    public List<Note> getAllNotes(){
        return noteService.getAllNotes();
    }
}
