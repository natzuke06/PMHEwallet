package com.scb.doc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DownloadController {

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void download(HttpServletResponse response, @RequestParam String docName) {
		try {

			byte[] data = null;
			String fileName = docName;
			System.out.println("Downloading file..." + docName);

			InputStream inputStream = DownloadController.class.getClassLoader().getResourceAsStream("static/" + fileName);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data2 = new byte[16384];

			//migrate InputStream to ByteArrayOutputStream
			while ((nRead = inputStream.read(data2, 0, data2.length)) != -1) {
				buffer.write(data2, 0, nRead);
			}

			buffer.flush();
			data = buffer.toByteArray();

			// ClassLoader classLoader =
			// DocumentController.class.getClassLoader().getSystemClassLoader();
			// File file = new File(classLoader.getResource("static/"+fileName).getFile());
			// data = Files.readAllBytes(file.toPath());

			if (docName.trim().endsWith(".pdf")) {
				// response info...
				response.setContentType("application/pdf");
				response.setContentLength(data.length);

			} else if (docName.trim().endsWith(".ppt") || docName.trim().endsWith(".pptx")) {
				response.setContentType("application/vnd.ms-powerpoint");
				response.setHeader("Content-Disposition", "attachment;filename=\""+fileName+".ppt\"");
			}

			// Streaming...
			response.getOutputStream().write(data);
			response.getOutputStream().flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
