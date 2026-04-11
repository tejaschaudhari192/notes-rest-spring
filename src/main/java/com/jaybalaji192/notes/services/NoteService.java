package com.jaybalaji192.notes.services;

import com.jaybalaji192.notes.models.Note;
import com.jaybalaji192.notes.repositories.NoteRepository;
import com.jaybalaji192.notes.requests.NoteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note addNote(NoteRequest request){
        Note note = new Note(
                request.title(),
                request.content()
        );
        return noteRepository.save(note);
    }

    public String deleteNote(String id){
        System.out.println("deleting "+id);
        noteRepository.deleteById(id);
        return "Deleted Successfully";
    }

    public Note updateNote(String id, NoteRequest request){
        Note note = noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found with id: "+id));
        note.updateNote(note,request.title(),request.content());
        return noteRepository.save(note);
    }


    public List<Note> getAllNotes(){
        return noteRepository.findAll();
    }
}
