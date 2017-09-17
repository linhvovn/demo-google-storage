package com.legalfab.util;

import java.io.OutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFGenerator {

	public static void generatePDF(String text, String number,OutputStream out){
		Document document = new Document();
		try{
			PdfWriter.getInstance(document, out);
			document.open();
			document.add(new Paragraph("Alphabetical : " + text));
			document.add(new Paragraph("Number : "+ number));
			document.addAuthor("Linh Vo");
			document.addCreationDate();
			document.addCreator("iText library");
			document.addTitle("Legalfab Test");
		}catch(Exception e){
			//error
		}
		document.close();
	}
}
