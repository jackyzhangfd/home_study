package com.autobusi.home.study.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.autobusi.home.study.dto.SubjectDto;
import com.autobusi.home.study.exception.BadRequestException;
import com.autobusi.home.study.models.Subject;
import com.autobusi.home.study.models.SubjectImage;
import com.autobusi.home.study.models.SubjectImageRepository;
import com.autobusi.home.study.models.SubjectRepository;
import com.autobusi.home.study.util.BaiduApi;
import com.autobusi.home.study.util.SpringContextHolder;
import com.autobusi.home.study.util.SubjectCategory;

@RestController
@RequestMapping("/api/v1/subject/*")
public class SubjectController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SubjectRepository repo;
	@Autowired
	private SubjectImageRepository imageRepo;
	
	@GetMapping("/all")
	public List<SubjectDto> getAllSubjects() {
		Iterable<Subject> subjects = this.repo.findAll();
		Iterator<Subject> subjectIt = subjects.iterator();
		
		List<SubjectDto> result = new ArrayList<SubjectDto>();
		while(subjectIt.hasNext()){
			Subject subDB = subjectIt.next();
			SubjectDto subDto = new SubjectDto();
			subDto.createFromSubject(subDB);
			result.add(subDto);
		}
		return result;
	}
	
	@GetMapping("/search")
	public List<SubjectDto> searchSubjectByText(@RequestParam("searchTerm") String searchText){
		if(searchText == null || searchText.isEmpty()){
			throw new BadRequestException("No search term is specified.");
		}
		
		List<SubjectDto> result = new ArrayList<SubjectDto>();
		
		List<Subject> subjects = this.repo.searchByText("%" + searchText + "%");
		Iterator<Subject> it = subjects.iterator();
		while(it.hasNext()){
			Subject sub = it.next();
			SubjectDto dto = new SubjectDto();
			dto.createFromSubject(sub);
			result.add(dto);
		}
		return result;
	}
	
	@GetMapping("/{subjectId}")
	public SubjectDto ApiV1GetHandler(@PathVariable long subjectId) {
		Subject sub = this.repo.findOne(subjectId);
		if(sub != null){
			SubjectDto dto = new SubjectDto();
			dto.createFromSubject(sub);
			
			List<SubjectImage> images = this.imageRepo.findBySubjectId(sub.getId());
			if(images != null && !images.isEmpty()){
				SubjectImage si = images.get(0);
				dto.setImageBytes(si.getImage());
			}
			return dto;
		}else{
			this.logger.warn("Don't find subject '" + subjectId + "'");
			return null;
		}
	}
	
	@PostMapping("/")
	public ResponseEntity<SubjectDto> addSubject(
			UriComponentsBuilder uriComponentsBuilder,
			@RequestParam("title") String title,
			@RequestParam("detail") String detail,
			@RequestParam("category") String category ,
			@RequestParam("imageFile") MultipartFile imageFile 
	) throws URISyntaxException{
		
		SubjectCategory sc;
		if(category == null || category.equals("null")){
			sc = SubjectCategory.ZONGHE;
		}else if(SubjectCategory.valueOf(category).equals(SubjectCategory.SHUXUE)){
			sc = SubjectCategory.SHUXUE;
		}else if(SubjectCategory.valueOf(category).equals(SubjectCategory.YUWEN)){
			sc = SubjectCategory.YUWEN;
		}else if(SubjectCategory.valueOf(category).equals(SubjectCategory.YINGYU)){
			sc = SubjectCategory.YINGYU;
		}else{
			sc = SubjectCategory.ZONGHE;
		}
		
		byte[] imageBytes = null;
		
		if(imageFile != null){
			try {
				imageBytes = imageFile.getBytes();
			} catch (IOException e) {
			
			}
		}
		
		//save the uploaded image
		Subject sub = new Subject();
		
		sub.setTitle(title);
		sub.setDetail(detail);
		sub.setCategory(sc);
		sub = this.repo.save(sub);
		
		if(imageBytes != null){
			SubjectImage si = new SubjectImage();
			si.setImage(imageBytes);
			si.setSubjectId(sub.getId());
			si = this.imageRepo.save(si);
		}
		
		SubjectDto dto = new SubjectDto();
		dto.setId(sub.getId());
		dto.setCategory(sub.getCategory());
		dto.setName(sub.getTitle());
		dto.setDetail(sub.getDetail());
		dto.setLastChangedDate(sub.getModifiedAt());
		if(imageBytes != null){
			dto.setImageBytes(imageBytes);
		}
		
		UriComponents uriComponents = uriComponentsBuilder.path("/api/v1/subject/{id}")
                .buildAndExpand(sub.getId());
        ResponseEntity re = ResponseEntity.created(new URI(uriComponents.getPath())).body(dto);
        
        return re;
	}
	
	@PostMapping("/{subjectId}")
	public ResponseEntity<SubjectDto> updateSubject(
			UriComponentsBuilder uriComponentsBuilder,
			@PathVariable String subjectId, 
			@RequestParam("title") String title,
			@RequestParam("detail") String detail,
			@RequestParam("category") String category ,
			@RequestParam("imageFile") MultipartFile imageFile 
	) throws URISyntaxException{
		
		SubjectCategory sc;
		if(category == null || category.equals("null")){
			sc = SubjectCategory.ZONGHE;
		}else if(SubjectCategory.valueOf(category).equals(SubjectCategory.SHUXUE)){
			sc = SubjectCategory.SHUXUE;
		}else if(SubjectCategory.valueOf(category).equals(SubjectCategory.YUWEN)){
			sc = SubjectCategory.YUWEN;
		}else if(SubjectCategory.valueOf(category).equals(SubjectCategory.YINGYU)){
			sc = SubjectCategory.YINGYU;
		}else{
			sc = SubjectCategory.ZONGHE;
		}
		
		byte[] imageBytes = null;
		
		if(imageFile != null){
			try {
				imageBytes = imageFile.getBytes();
			} catch (IOException e) {
			
			}
		}
		
		//save the uploaded image
		Subject sub = this.repo.findOne(Long.valueOf(subjectId));
		
		if(sub == null){
			throw new BadRequestException("No subject with ID '" + subjectId + "' is found.");
		}
		
		sub.setTitle(title);
		sub.setDetail(detail);
		sub.setCategory(sc);
		sub = this.repo.save(sub);
		
		if(imageBytes != null){
			SubjectImage si ;
			List<SubjectImage> images = this.imageRepo.findBySubjectId(sub.getId());
			if(!images.isEmpty()){
				si = images.get(0);
			}else{
				si = new SubjectImage();
			}
			
			si.setImage(imageBytes);
			si.setSubjectId(sub.getId());
			si = this.imageRepo.save(si);
		}
		
		SubjectDto dto = new SubjectDto();
		dto.setId(sub.getId());
		dto.setCategory(sub.getCategory());
		dto.setName(sub.getTitle());
		dto.setDetail(sub.getDetail());
		dto.setLastChangedDate(sub.getModifiedAt());
		if(imageBytes != null){
			dto.setImageBytes(imageBytes);
		}
		
		UriComponents uriComponents = uriComponentsBuilder.path("/api/v1/subject/{id}")
                .buildAndExpand(subjectId);
        ResponseEntity re = ResponseEntity.created(new URI(uriComponents.getPath())).body(dto);
        
        return re;
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
