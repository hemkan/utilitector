package com.utilitector.backend.response;

import com.utilitector.backend.data.Cluster;

import java.util.List;

public record GetAllClustersResponse(List<Cluster> clusters) {
}
