package com.kayyagari.db.entities;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kayyagari.util.DobDeSerializer;
import com.kayyagari.util.DobSerializer;
import com.kayyagari.util.EnumGender;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

@Entity(name = "customer")
public class Customer extends AuditableEntity {
	@Column
	@Size(min = 2, max = 100, message = "customer's name should be between 2 to 100 characters in length")
	private String name;
	
	@Column
	// derived from DoB, input value is ignored if provided
	private Integer age;
	
	@Column
	@NotNull
	@Past(message = "customer date of birth should be a date from past")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonDeserialize(using = DobDeSerializer.class)
	@JsonSerialize(using = DobSerializer.class)
	private LocalDate dob;

	@Column
	@Size(min = 10, max = 200, message = "customer's address should be between 10 to 200 characters in length")
	private String address;
	
	@Column
	@Enumerated(EnumType.STRING)
	@EnumGender(anyOf = {Gender.F, Gender.M, Gender.UNKNOWN})
	private Gender gender = Gender.UNKNOWN; // equivalent to null, the default value

	// not using lombok, just to avoid another dependency for the sake of a single entity
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	protected void setAge(int age) {
		this.age = age;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
		long age = ChronoUnit.YEARS.between(dob, LocalDate.now(ZoneId.of("UTC")));
		// ignoring the loss of precision, assuming caller takes care of DoB's range
		this.age = (int)age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "Customer [id=" + getId() + ", name=" + name + ", age=" + age + ", dob=" + dob + ", address=" + address
				+ ", gender=" + gender + "]";
	}
}
