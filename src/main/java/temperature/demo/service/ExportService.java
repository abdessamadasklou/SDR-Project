package temperature.demo.service;

import com.opencsv.CSVWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import temperature.demo.model.Mesure;
import temperature.demo.repository.MesureRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportService {

    private final MesureRepository mesureRepository;

    public ExportService(MesureRepository mesureRepository) {
        this.mesureRepository = mesureRepository;
    }

    public List<Mesure> getAllMesures() {
        return mesureRepository.findAll();
    }

    public ByteArrayInputStream exportToCSV() throws Exception {
        List<Mesure> mesures = getAllMesures();
        if (mesures.isEmpty()) {
            throw new Exception("No data available to export.");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        CSVWriter writer = new CSVWriter(osw);

        // Write header
        String[] header = {"ID", "Temperature", "Humidite", "Capteur Name", "Insertion Time"};
        writer.writeNext(header);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Write data rows
        for (Mesure m : mesures) {
            String[] data = {
                    m.getId().toString(),
                    String.valueOf(m.getTemperature()),
                    String.valueOf(m.getHumidite()),
                    m.getCapteurName(),
                    m.getInsertionTime().format(formatter)
            };
            writer.writeNext(data);
        }

        writer.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream exportToExcel() throws Exception {
        List<Mesure> mesures = getAllMesures();
        if (mesures.isEmpty()) {
            throw new Exception("No data available to export.");
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Mesures");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Temperature", "Humidite", "Capteur Name", "Insertion Time"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Fill data rows
            int rowIdx = 1;
            for (Mesure m : mesures) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(m.getId());
                row.createCell(1).setCellValue(m.getTemperature());
                row.createCell(2).setCellValue(m.getHumidite());
                row.createCell(3).setCellValue(m.getCapteurName());
                row.createCell(4).setCellValue(m.getInsertionTime().format(formatter));
            }

            // Autosize columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
