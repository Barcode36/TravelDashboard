package main.net.atos.uk.TravelDashboard.Dashboard.Report;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

/**
 * This class is used to generate the PDF document of the report, to the OS Desktop. 
 * 
 * @author  Zequn Yu
 * @since   2017-04-08
 * @version 1.0
*/

public class PDFGenerator {
	/**
     * Image of general information of report.
     */
	BufferedImage image1;
	/**
     * Image of time pane of report.
     */
	BufferedImage image2_1;
	/**
     * Image of bar chart in time pane of report.
     */
	BufferedImage image2_2;
	/**
     * Image of location pane of report.
     */
	BufferedImage image3_1;
	/**
     * Image of bar chart in location pane of report.
     */
	BufferedImage image3_2;
	/**
     * Image of cost element pane of report.
     */
	BufferedImage image4_1;
	/**
     * Image of bar chart in cost element pane of report.
     */
	BufferedImage image4_2;
	/**
     * Image of member pane of report.
     */
	BufferedImage image5_1;
	/**
     * Image of bar chart in member pane of report.
     */
	BufferedImage image5_2;
	
	/**
     * Image of the first page of PDF.
     */
	BufferedImage reportCombined;
	/**
     * Image of the second page of PDF.
     */
	BufferedImage chartCombined;
	
	/**
     * Image width.
     */
	int w;
	/**
     * Image height.
     */
	int h;
	
	/**
	 * The constructor takes image parameters and generate two combined PNG files.
	 * 
	 * @param total if it's true then the fifth part of report is member comparison, otherwise nothing.
	 * @param iGeneralR Image of general information of report 
	 * @param iTimeR Image of time pane of report
	 * @param iTimeT Image of bar chart in time pane of report
	 * @param iLocalR Image of location pane of report
	 * @param iLocalT Image of bar chart in location pane of report
	 * @param iElemR Image of cost element pane of report
	 * @param iElemT Image of bar chart in cost element pane of report
	 * @param iMemR Image of member pane of report
	 * @param iMemT Image of bar chart in member pane of report
	 * @param chart1 Image of table of time pane of report
	 * @param chart2 Image of table of location pane of report
	 * @param chart3 Image of table of cost element pane of report
	 * @param chart4 Image of table of member pane of report
	 */
	public PDFGenerator(boolean total, WritableImage iGeneralR,
			WritableImage iTimeR, WritableImage iTimeT,
			WritableImage iLocalR, WritableImage iLocalT,
			WritableImage iElemR, WritableImage iElemT,
			WritableImage iMemR, WritableImage iMemT,
			BufferedImage chart1, BufferedImage chart2,
			BufferedImage chart3, BufferedImage chart4) {
		
		image1 = SwingFXUtils.fromFXImage(iGeneralR, null);
		image2_1 = SwingFXUtils.fromFXImage(iTimeR, null);
		image2_2 = SwingFXUtils.fromFXImage(iTimeT, null);
		image3_1 = SwingFXUtils.fromFXImage(iLocalR, null);
		image3_2 = SwingFXUtils.fromFXImage(iLocalT, null);
		image4_1 = SwingFXUtils.fromFXImage(iElemR, null);
		image4_2 = SwingFXUtils.fromFXImage(iElemT, null);
		image5_1 = SwingFXUtils.fromFXImage(iMemR, null);
		image5_2 = SwingFXUtils.fromFXImage(iMemT, null);
		
		w = image1.getWidth()+80;
		h = image1.getHeight()*5+800;
		
		reportCombined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics g1 = reportCombined.getGraphics();
		g1.drawImage(image1, 10, 0, w, image1.getHeight(), null);
		g1.drawImage(image2_1, 10, image1.getHeight(), null);
		g1.drawImage(image2_2, 10, image1.getHeight()*2, null);
		g1.drawImage(image3_1, 10, image1.getHeight()*2+200, null);
		g1.drawImage(image3_2, 10, image1.getHeight()*3+200, null);
		g1.drawImage(image4_1, 10, image1.getHeight()*3+400, null);
		g1.drawImage(image4_2, 10, image1.getHeight()*4+400, null);
		if (total) {
			g1.drawImage(image5_1, 10, image1.getHeight()*4+600, null);
			g1.drawImage(image5_2, 10, image1.getHeight()*5+600, null);
		}
		
		chartCombined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics g2 = chartCombined.getGraphics();
		
		g2.drawImage(chart1, 50, 50, null);
		g2.drawImage(chart2, 50, 50 + chart1.getHeight(), null);
		g2.drawImage(chart3, 50, 50 + chart1.getHeight() + chart2.getHeight(), null);
		if (total) {
			g2.drawImage(chart4, 50, 50 + chart1.getHeight() + chart2.getHeight() + chart3.getHeight(), null);	
		}
		
	}
	
	/**
     * To generate the PDF, default path is OS Desktop. For more information please refer to
     * apache PDFbox reference.
     */
	public void generatePDF() {
		PDDocument doc = new PDDocument();
		
		try{
			PDPage page1 = new PDPage(new PDRectangle(reportCombined.getWidth(), reportCombined.getHeight()));
		    PDImageXObject pdImageXObject1 = LosslessFactory.createFromImage(doc, reportCombined);
		    PDPageContentStream contentStream1 = new PDPageContentStream(doc, page1);
		    contentStream1.drawImage(pdImageXObject1, 0, 0);
		    contentStream1.close();
		    doc.addPage(page1);
		
		    PDPage page2 = new PDPage(new PDRectangle(chartCombined.getWidth(), chartCombined.getHeight()));
		    PDImageXObject pdImageXObject2 = LosslessFactory.createFromImage(doc, chartCombined);
		    PDPageContentStream contentStream2 = new PDPageContentStream(doc, page2);
		    contentStream2.drawImage(pdImageXObject2, 0, 0);
		    contentStream2.close();
		    doc.addPage(page2);
		    
		    Date date = new Date();

		    String home = System.getProperty("user.home");
		    String fileName = home + File.separator + "Desktop" + File.separator + date.getTime() + ".pdf";
		    		
		    doc.save(fileName);
		    doc.close();
		} catch (Exception io){
		    System.out.println(" -- fail --" + io);
		}
	}	
}
