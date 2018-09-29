package com.autobusi.home.study.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.autobusi.home.study.exception.BadRequestException;
import com.autobusi.home.study.models.Subject;
import com.autobusi.home.study.models.SubjectRepository;

@RestController
@RequestMapping("/api/v1/subject/*")
public class SubjectController {
	
	@Autowired
	private SubjectRepository repo;
	
	@GetMapping("/all")
	public String getAllSubjects() {
		return "Server Ok";
	}
	
	@GetMapping("/{subjectId}")
	public String ApiV1GetHandler(@PathVariable long subjectId) {
		return "Server Ok";
	}
	
	@PostMapping("/")
	public ResponseEntity<Subject> addSubject(@RequestBody Subject subject, 
			UriComponentsBuilder uriComponentsBuilder) throws URISyntaxException{
		
		throwIfIdNotNull(subject.getId());

		Subject savedAdvertisement = repo.save(subject);

        UriComponents uriComponents = uriComponentsBuilder.path("/api/v1/subject/{id}")
                .buildAndExpand(savedAdvertisement.getId());
        return ResponseEntity.created(new URI(uriComponents.getPath())).body(savedAdvertisement);
	}
	
	private static void throwIfIdNotNull(final Long id) {
        if (id != null && id.intValue() != 0) {
            String message = String
                    .format("Remove 'id' property from request or use PUT method to update resource with id = %d", id);
            throw new BadRequestException(message);
        }
    }
}
