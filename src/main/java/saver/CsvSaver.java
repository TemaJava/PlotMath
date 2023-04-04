package saver;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class CsvSaver {
    File directory;
    CSVWriter writer;
    FileWriter outputfile;

    public CsvSaver(Path path) throws IOException {
        this.directory = new File(String.valueOf(path));

        outputfile = new FileWriter(directory + "/outputData.csv");
        writer = new CSVWriter(outputfile);
        //заполнение заголовков файла
        String[] headers = { "File Path", "Ra", "Rz", "RMax" };
        writer.writeNext(headers);
        writer.flush();
    }

    public void addData(String string) throws IOException {
        String[] data = string.split(",");
        writer.writeNext(data);
        writer.flush();
    }
}
