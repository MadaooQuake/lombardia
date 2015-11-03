package lombardia2014.generators;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.Phrase;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Chunk;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jarek_000
 */
public class PDFCreator {

    int rows_per_page = 20; // max rows in page
    int rotate = 0;
    String output_file_name = null;
    String title = null;
    Document document = null;

    public PDFCreator(String file_name, String report_title) {
        output_file_name = file_name;
        title = report_title;
    }

    public void SetRowsPerPage(int value) {
        rows_per_page = value;
    }

    public void SetLandscapeView() {
        rotate = 1;
    }

    public void SetVerticalView() {
        rotate = 0;
    }

    public void CreatePDF(DefaultTableModel data,
            String[] headers,
            float[] headers_widths
    ) throws DocumentException, IOException {
        String[][][] convertedData = ConvertData(data, rows_per_page);
        if (rotate == 1) {
            document = new Document(PageSize.LETTER.rotate());
        } else {
            document = new Document(PageSize.LETTER);
        }
        PdfWriter.getInstance(document, new FileOutputStream(output_file_name));
        document.open();
        for (int row = 0; row < convertedData.length; row++) {
            BaseFont bf = BaseFont.createFont("Lombardia2014/core/fonts/arialuni.ttf", BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED);
            com.itextpdf.text.Font myFont = new com.itextpdf.text.Font(bf, 12);
            com.itextpdf.text.Font smallFont = new com.itextpdf.text.Font(bf, 6);
            
            Paragraph p = new Paragraph(title + " strona " + (row + 1), myFont);

            document.add(p);
            document.add(Chunk.NEWLINE);
            PdfPTable table = createPDFTable(convertedData[row],
                    smallFont,
                    headers,
                    headers_widths
            );
            document.add(table);
            document.newPage();
        }
        document.close();
    }

    private PdfPTable createPDFTable(String[][] inputData,
            com.itextpdf.text.Font myFont,
            String[] headers,
            float[] headers_widths
    )
            throws DocumentException {

        //String[] headers = getHeaders();
        PdfPTable table = new PdfPTable(headers.length);
        table.setWidthPercentage(90);
        // set relative columns width

        table.setWidths(headers_widths);

        //generate headers
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, myFont));
            table.addCell(cell);
        }
        //generate content

        for (String[] inputData1 : inputData) {
            //hack to not present empty rows on the last page
            if (inputData1.length > 0) {
                if (inputData1[0] == null) {
                    break;
                }
            }
            for (String inputData11 : inputData1) {
                PdfPCell cell = new PdfPCell(new Phrase(inputData11, myFont));
                table.addCell(cell);
            }
        }
        return table;
    }

    private String[][][] ConvertData(DefaultTableModel model, int obj_per_page) {
        int row_count = model.getRowCount();
        int column_count = model.getColumnCount();
        int page_count = (row_count / obj_per_page) + 1;

        String[][][] convertedData = new String[page_count][obj_per_page][column_count];
        int new_row = 0;
        int page = 1;
        for (int row = 0; row < row_count; row++) {
            new_row = (row % obj_per_page);
            page = (row / obj_per_page) + 1;

            for (int col = 0; col < column_count; col++) {
                if (model.getValueAt(row, col) != null) {
                    convertedData[page - 1][new_row][col] = model.getValueAt(row, col).toString();
                } else {
                    convertedData[page - 1][new_row][col] = "";
                }
            }
        }

        return convertedData;
    }

}
