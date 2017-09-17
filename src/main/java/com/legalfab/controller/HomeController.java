package com.legalfab.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Calendar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.model.ObjectAccessControl;
import com.google.api.services.storage.model.StorageObject;
import com.legalfab.util.Const;
import com.legalfab.util.PDFGenerator;
import com.legalfab.util.StorageFactory;

/**
 * @author Jerry Conde, webmaster@javapointers.com
 * @since 8/9/2016
 */
@Controller
public class HomeController {

	@RequestMapping("/")
	public String viewHome() {
		return "index";
	}

	@RequestMapping("/create")
	public String createFile(@RequestParam(value = "text", required = true) String text,
			@RequestParam(value = "number", required = true) String number, Model model) {
		ByteArrayOutputStream outpdf = new ByteArrayOutputStream();
		PDFGenerator.generatePDF(text, number, outpdf);
		InputStream inpdf = new ByteArrayInputStream(outpdf.toByteArray());
		
		String outFileName = generateFileName(text,number);
		
		try {
			String fileUrl = uploadFile(outFileName,"application/pdf",inpdf,Const.BUCKET_NAME);
			
			model.addAttribute("url",fileUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "create";
	}

	/**
	 * Upload file to google storage
	 * @param name
	 * @param contentType
	 * @param input
	 * @param bucketName
	 * @return
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	private String uploadFile(String name, String contentType, InputStream input, String bucketName)
			throws IOException, GeneralSecurityException {

		InputStreamContent contentStream = new InputStreamContent(contentType, input);


		StorageObject objectMetadata = new StorageObject()
				// Set the destination object name
				.setName(name)
				// Set the access control list to publicly read-only
				.setAcl(Arrays.asList(new ObjectAccessControl().setEntity("allUsers").setRole("READER")));

		// Do the insert
		Storage client = StorageFactory.getService();

		Storage.Objects.Insert insertRequest = client.objects().insert(bucketName, objectMetadata, contentStream);

		insertRequest.execute();

		return Const.GOOGLE_STORAGE_URL+name;
	}
	
	/**
	 * Generate file name base on content
	 * @param text
	 * @param number
	 * @return
	 */
	private String generateFileName(String text,String number){
		return "pdf-"+Calendar.getInstance().getTimeInMillis();
	}
	
}
