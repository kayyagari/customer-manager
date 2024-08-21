package com.kayyagari.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DobSerializer extends StdSerializer<LocalDate> {
	public DobSerializer() {
		super(LocalDate.class);
	}

	@Override
	public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE));
	}
}
