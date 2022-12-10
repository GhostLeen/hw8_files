package com.denal;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.denal.model.MenuFiles;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class FilesParsingTest {

    ClassLoader cl = FilesParsingTest.class.getClassLoader();

    @Test
    void zipParseTest() throws Exception {
        try (
                InputStream source = cl.getResourceAsStream("files/zip_example.zip");
                ZipInputStream zis = new ZipInputStream(source)
        ) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains(".pdf")) {
                    PDF contentPdf = new PDF(zis);
                    assertThat(contentPdf.text).contains("Some text in PDF File");
                } else if (entry.getName().contains(".csv")) {
                    CSVReader contentCsv = new CSVReader(new InputStreamReader(zis));
                    List<String[]> strCsv = contentCsv.readAll();
                    assertThat(strCsv.get(0)[0]).contains("videogame 1");
                    assertThat(strCsv.get(0)[1]).contains("It takes two");
                    assertThat(strCsv.get(1)[0]).contains("videogame 2");
                    assertThat(strCsv.get(1)[1]).contains("God of war");
                } else if (entry.getName().contains(".xlxs")) {
                    XLS contentXlxs = new XLS(zis);
                    assertThat(contentXlxs.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue()).isEqualTo("Some text in xlsx file");
                }
            }
        }
    }

    @Test
    void jsonParseTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try (
                InputStream sourse = cl.getResourceAsStream("files/json_example.json");
                InputStreamReader reader = new InputStreamReader(sourse)
        ) {
            MenuFiles menuFiles = mapper.readValue(reader, MenuFiles.class);
            assertThat(menuFiles.title).isEqualTo("menu");
            assertThat(menuFiles.menuDiv.id).isEqualTo("file");
            assertThat(menuFiles.menuDiv.value).isEqualTo("File");
            assertThat(menuFiles.menuDiv.used).isFalse();
            assertThat(menuFiles.menuDiv.popup.get(0).value).isEqualTo("New");
            assertThat(menuFiles.menuDiv.popup.get(0).clickId).isEqualTo(111);
            assertThat(menuFiles.menuDiv.popup.get(1).value).isEqualTo("Open");
            assertThat(menuFiles.menuDiv.popup.get(1).clickId).isEqualTo(666);
            assertThat(menuFiles.menuDiv.popup.get(2).value).isEqualTo("Close");
            assertThat(menuFiles.menuDiv.popup.get(2).clickId).isEqualTo(999);
        }
    }
}