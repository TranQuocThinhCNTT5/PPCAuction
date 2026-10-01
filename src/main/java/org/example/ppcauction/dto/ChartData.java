package org.example.ppcauction.dto;

import java.util.List;

public record ChartData(List<String> labels, List<Number> values) { }
