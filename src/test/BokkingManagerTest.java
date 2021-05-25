package test;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import service.BookingManager;

class BokkingManagerTest {

	
	
	@Test
	void testReadBookingsFile() throws IOException {
		assertNotNull(BookingManager.readBookingsFile());
	}

	
}
