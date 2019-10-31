package com.n26.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Custom BigDecimal serializer
 */
@Component
public class BigDecimalToStringJsonSerializer extends JsonSerializer<BigDecimal> {

	@Value("${decimal}")
	private int decimal;

	@Override
	public void serialize(BigDecimal value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
		generator.writeString(value.setScale(decimal, RoundingMode.HALF_UP).toString());
	}
}
