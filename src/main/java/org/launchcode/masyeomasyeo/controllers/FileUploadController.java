package org.launchcode.masyeomasyeo.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.launchcode.masyeomasyeo.models.data.SongDao;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.launchcode.masyeomasyeo.models.FileUpload;
import org.launchcode.masyeomasyeo.services.SongService;
import org.launchcode.masyeomasyeo.validators.FileValidator;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileUploadController {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job job;

	@Autowired
	FileValidator fileValidator;


	//TODO: Check if we need the FileUpload functionality and associated Java model FileUpload
	@RequestMapping(value = "song/upload", method = RequestMethod.GET)
	public ModelAndView uploadPage() {
		ModelAndView model = new ModelAndView("song/upload");
		FileUpload formUpload = new FileUpload();
		model.addObject("formUpload", formUpload);

		return model;
	}

	//TODO: Check for existing song/genre/artist to prevent duplicate song/artist/genre entries
	@RequestMapping(value = "song/load", method=RequestMethod.POST)
	public String load(@RequestParam("file") MultipartFile multipartFile) throws IOException,
			JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException {

		//it's assumed you have a folder called tempuploads in the resources folder
		String path = new ClassPathResource("tempuploads/").getURL().getPath();
		File fileLocation = new File(path + multipartFile.getOriginalFilename());
		Map<String, JobParameter> maps = new HashMap<>();
		maps.put("time", new JobParameter(System.currentTimeMillis()));

		//Add file to tempuploads
		try {

			// Get the file and save it somewhere
			byte[] bytes = multipartFile.getBytes();
			Path fileToImport = Paths.get(path + multipartFile.getOriginalFilename());
			Files.write(fileToImport, bytes);

		} catch (IOException e) {
			e.printStackTrace();
		}

		//Launch the Batch Job
		JobExecution jobExecution = jobLauncher.run(job, new JobParametersBuilder()
				.addString("fullPathFileName", fileLocation.getAbsolutePath())
				.toJobParameters());

		//TODO: figure out how to add time mapping back into the job execution (currently maps is not functional)
		/*JobParameters parameters = new JobParameters(maps);
		JobExecution jobExecution = jobLauncher.run(job, parameters);*/

		System.out.println("JobExecution: " + jobExecution.getStatus());

		System.out.println("Batch is Running...");
		while (jobExecution.isRunning()) {
			System.out.println("...");
		}

		return "redirect:/success";
		//return jobExecution.getStatus();
	}

	@RequestMapping(value = "song/doUpload", method = RequestMethod.POST)
	public String doUpload(@ModelAttribute("formUpload") FileUpload fileUpload,
						   BindingResult result, RedirectAttributes redirectAttributes)
			throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
			JobRestartException, JobInstanceAlreadyCompleteException, IOException, JAXBException {

		//validate
		fileValidator.validate(fileUpload, result);

		if (result.hasErrors()) {
			return "uploadPage";
		} else {
			//doUpload
			//redirectAttributes.addFlashAttribute("fileNames", uploadAndImportDb(fileUpload));
			Map<String, JobParameter> maps = new HashMap<>();
			maps.put("time", new JobParameter(System.currentTimeMillis()));
			JobParameters parameters = new JobParameters(maps);
			JobExecution jobExecution = jobLauncher.run(job, parameters);

			System.out.println("JobExecution: " + jobExecution.getStatus());

			System.out.println("Batch is Running...");
			while (jobExecution.isRunning()) {
				System.out.println("...");
			}
			return "redirect:/success";
		}
	}

	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public ModelAndView success() {
		ModelAndView model = new ModelAndView("success");
		return model;

	}

	private List<String> uploadAndImportDb(FileUpload fileUpload) throws IOException, JAXBException {
		List<String> fileNames = new ArrayList<String>();
		List<String> paths = new ArrayList<String>();

		CommonsMultipartFile[] commonsMultipartFiles = fileUpload.getFiles();

		String filePath = null;

		for (CommonsMultipartFile multipartFile : commonsMultipartFiles) {
			filePath = "/Users/macOS/Downloads" + multipartFile.getOriginalFilename();
			File file = new File(filePath);
			// copy files
			FileCopyUtils.copy(multipartFile.getBytes(), file);
			fileNames.add(multipartFile.getOriginalFilename());

			paths.add(filePath);
		}

		//process parse and import data
		//songService.process(paths);


		return fileNames;

	}

}



