package com.utilitector.backend.response;

import com.utilitector.backend.document.Report;
import lombok.Data;

import java.util.List;

@Data
public class CalebTestAllResponse {
	private List<Report> locations;
}
