package com.jaybalaji192.notes.requests;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


public record NoteRequest(String title, String content) {

}
