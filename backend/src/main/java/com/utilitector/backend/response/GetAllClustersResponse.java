package com.utilitector.backend.response;

import com.utilitector.backend.data.DCluster;

import java.util.List;

public record GetAllClustersResponse(List<DCluster> clusters) {
}
