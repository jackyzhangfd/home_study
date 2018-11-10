package com.autobusi.home.study.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.autobusi.home.study.exception.BadRequestException;
import com.autobusi.home.study.models.Subject;
import com.autobusi.home.study.models.SubjectImage;
import com.autobusi.home.study.models.SubjectImageRepository;
import com.autobusi.home.study.models.SubjectRepository;
import com.autobusi.home.study.util.BaiduApi;
import com.autobusi.home.study.util.SpringContextHolder;

@RestController
@RequestMapping("/api/v1/subject/*")
public class SubjectController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SubjectRepository repo;
	@Autowired
	private SubjectImageRepository imageRepo;
	
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
		
		Subject savedSubject = repo.save(subject);

        UriComponents uriComponents = uriComponentsBuilder.path("/api/v1/subject/{id}")
                .buildAndExpand(savedSubject.getId());
        return ResponseEntity.created(new URI(uriComponents.getPath())).body(savedSubject);
	}
	
	@PostMapping("/uploadSubjectImage")
	public ResponseEntity<Subject> addSubjectViaImage(@RequestParam("fileName") String fileName, @RequestParam("imageFile") MultipartFile imageFile, 
			HttpServletResponse response, UriComponentsBuilder uriComponentsBuilder)throws URISyntaxException{
		
		if(imageFile == null || imageFile.isEmpty()){
			throw new BadRequestException("No image is detected");
		}
		
		byte[] imageBytes;
		try {
			imageBytes = imageFile.getBytes();
		} catch (IOException e) {
			throw new BadRequestException("Parse image content fail.");
		}
		
		//extract text from picture
		BaiduApi baiduApi = SpringContextHolder.getApplicationContext().getBean(BaiduApi.class);
		JSONObject imageTextJson = baiduApi.extractTextFromPicture(imageBytes);
		String imageText = imageTextJson.toString(0);
		
		//save the uploaded image
		Subject sub = new Subject();
		sub.setTitle(new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss").format(new Date()));
		sub.setDetail(imageText);
		sub = this.repo.save(sub);
		
		SubjectImage si = new SubjectImage();
		si.setImage(imageBytes);
		si.setSubjectId(sub.getId());
		si = this.imageRepo.save(si);
		
		UriComponents uriComponents = uriComponentsBuilder.path("/api/v1/subject/{id}")
                .buildAndExpand(sub.getId());
        ResponseEntity re = ResponseEntity.created(new URI(uriComponents.getPath())).body(sub);
        
        return re;
	}
	
	@PostMapping("/extractTextFromImage")
	public String extractTextFromImage(@RequestParam("imageFile") MultipartFile imageFile, 
			HttpServletResponse response, UriComponentsBuilder uriComponentsBuilder)throws URISyntaxException{
		
		if(imageFile == null || imageFile.isEmpty()){
			throw new BadRequestException("No image is detected");
		}
		
		byte[] imageBytes;
		try {
			imageBytes = imageFile.getBytes();
		} catch (IOException e) {
			throw new BadRequestException("Parse image content fail.");
		}
		
		//extract text from picture
		BaiduApi baiduApi = SpringContextHolder.getApplicationContext().getBean(BaiduApi.class);
		JSONObject imageTextJson = baiduApi.extractTextFromPicture(imageBytes);
		
		String result = "";
		if(imageTextJson != null && !imageTextJson.isNull("words_result")){
			JSONArray wordsArray = imageTextJson.getJSONArray("words_result");
			for( int i = 0; i < wordsArray.length(); i++){
				JSONObject word = wordsArray.getJSONObject(i);
				if(word != null && !word.isNull("words")){
					result = result + " " + word.getString("words");
				}
				
			}
		}
		
		return result;
	}
	
}
