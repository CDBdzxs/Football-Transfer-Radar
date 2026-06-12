package com.example.footballradar;

import java.util.ArrayList;

public class WebDataReport {
    private final String source;
    private final String generatedAt;
    private final int requiredNodes;
    private final int extractedNodes;
    private final String sortingRule;
    private final ArrayList<WebData> items;

    public WebDataReport(String source, String generatedAt, int requiredNodes,
                         int extractedNodes, String sortingRule, ArrayList<WebData> items) {
        this.source = source;
        this.generatedAt = generatedAt;
        this.requiredNodes = requiredNodes;
        this.extractedNodes = extractedNodes;
        this.sortingRule = sortingRule;
        this.items = items;
    }

    public String getSource() {
        return source;
    }

    public String getGeneratedAt() {
        return generatedAt;
    }

    public int getRequiredNodes() {
        return requiredNodes;
    }

    public int getExtractedNodes() {
        return extractedNodes;
    }

    public String getSortingRule() {
        return sortingRule;
    }

    public ArrayList<WebData> getItems() {
        return items;
    }
}
