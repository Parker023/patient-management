package com.parker.patientservice.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "patient")
public class PatientResponseDTO implements BaseResponseDto, Serializable {

    @JacksonXmlProperty(localName = "id")
    @CsvBindByName(column = "id")
    private String id;

    @CsvBindByName(column = "name")
    @JacksonXmlProperty(localName = "name")
    private String name;

    @CsvBindByName(column = "email")
    @JacksonXmlProperty(localName = "email")
    private String email;

    @CsvBindByName(column = "gender")
    @JacksonXmlProperty(localName = "gender")
    private String gender;

    @CsvBindByName(column = "address")
    @JacksonXmlProperty(localName = "address")
    private String address;

    @CsvBindByName(column = "dob")
    @JacksonXmlProperty(localName = "date-of-birth")
    private String dateOfBirth;
}