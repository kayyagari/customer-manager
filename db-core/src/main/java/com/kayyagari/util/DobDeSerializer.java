package com.kayyagari.util;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class DobDeSerializer extends StdDeserializer<LocalDate> {

	private static final String DOB_FORMAT = "yyyy-MM-dd";

	public DobDeSerializer() {
		super(LocalDate.class);
	}

	@Override
	public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
		try {
			String strDob = p.readValueAs(String.class);
			LocalDate dob = LocalDate.parse(strDob);
			return dob;
		}
		catch(Exception e) {
			throw new InvalidFormatException(p, "invalid date format, should be " + DOB_FORMAT, ctxt, _valueClass);
		}
	}

}
