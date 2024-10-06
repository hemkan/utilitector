package com.utilitector.backend.response;

import com.utilitector.backend.document.Report;

import java.util.List;

public record SubscribedEventsResponse(List<Report> reports) {}
