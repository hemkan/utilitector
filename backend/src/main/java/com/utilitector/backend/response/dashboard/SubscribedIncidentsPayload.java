package com.utilitector.backend.response.dashboard;

import com.utilitector.backend.data.Incident;

import java.util.Collection;

public record SubscribedIncidentsPayload(Collection<Incident> incidents) {}
