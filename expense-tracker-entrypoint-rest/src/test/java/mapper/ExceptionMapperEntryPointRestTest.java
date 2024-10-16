package mapper;

import com.portfolio.expensetracker.exception.BusinessException;
import com.portfolio.expensetracker.exception.ExceptionCode;
import com.portfolio.expensetracker.exception.ExceptionDto;
import com.portfolio.expensetracker.mapper.ExceptionMapperEntryPointRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class ExceptionMapperEntryPointRestTest {

    private ExceptionMapperEntryPointRest mapper;

    @BeforeEach
    void setup() {
        mapper = Mappers.getMapper(ExceptionMapperEntryPointRest.class);
    }

    @Test
    void testToExceptionDto() {
        // Arrange
        BusinessException exception;
        String message = "test-message";
        exception = new BusinessException(ExceptionCode.RESOURCE_NOT_FOUND, message);

        // Act
        ExceptionDto exceptionDto = mapper.toExceptionDto(exception);

        // Assert
        assertNotNull(exceptionDto);
        assertEquals(ExceptionCode.RESOURCE_NOT_FOUND.getCode(), exceptionDto.getCode());
        assertEquals(ExceptionCode.RESOURCE_NOT_FOUND.getHttpStatusCode(), exceptionDto.getHttpStatusCode());
        assertEquals(ExceptionCode.RESOURCE_NOT_FOUND.getReason(), exceptionDto.getReason());
        assertEquals(message, exceptionDto.getMessage());
    }
}
