package com.jaybalaji192.notes.repositories;

import com.jaybalaji192.notes.models.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {
    List<Note> findByUsername(String username);
}
