package unit.com.han.controller.converter;

import com.han.constants.OrderType;
import com.han.controller.converter.StringToOrderTypeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.converter.Converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StringToOrderTypeConverterTest {

  private Converter<String, OrderType> converter;

  @BeforeEach
  public void setUp() {
    this.converter = new StringToOrderTypeConverter();
  }

  @Test
  public void convert_Return_OrderType() {
    String source = "asc";

    OrderType type = converter.convert(source);
    assertThat(type).isEqualTo(OrderType.ASC);
  }

  @Test
  public void convert_Throw_Exception() {
    String source = "test";
    assertThrows(RuntimeException.class, () -> converter.convert(source));
  }

}
