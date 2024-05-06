import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class BookingServiceTest {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(BookingServiceTest.class);

    @Test
    public void testBook() {
        LOGGER.info("Старт тестового метода testBook ...");

        BookingService bookingService = new BookingService();

        LOGGER.debug("Создание моков для методов класса BookingService ...");
        BookingService mockedBookingService = Mockito.spy(bookingService);
        doReturn(true).when(mockedBookingService).checkTimeInBD(any(), any());
        doReturn(true).when(mockedBookingService).createBook(any(), any(), any());

        LOGGER.debug("Проверка выполнения метода book ...");
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertTrue(mockedBookingService.book("user123", LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        });

        LOGGER.error("Cтарт теста на выброс исключения CantBookException ...");
        doReturn(false).when(mockedBookingService).checkTimeInBD(any(), any());
        Assertions.assertThrows(CantBookException.class, () -> {
            mockedBookingService.book("user123", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        });

        LOGGER.info("Завершение тестового метода testBook.");
    }
}
