package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileService {

	public static Stream<String> readBookingsFile() throws IOException {
		Stream<String> records = Files.lines(Paths.get("C:\\tmp\\bookings.txt"));
		return records;
	}

}
