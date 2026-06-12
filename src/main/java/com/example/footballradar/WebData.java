package com.example.footballradar;

public abstract class WebData {
    private final int sourceRank;
    private final String title;
    private final String url;
    private final String sourceName;

    public WebData(int sourceRank, String title, String url, String sourceName) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be blank.");
        }

        this.sourceRank = sourceRank;
        this.title = title.trim();
        this.url = url == null ? "" : url.trim();
        this.sourceName = sourceName == null ? "Unknown" : sourceName.trim();
    }

    public int getSourceRank() {
        return sourceRank;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getSourceName() {
        return sourceName;
    }

    public abstract String getDataType();

    public abstract double calculateImpactScore();

    public double getImpactScore() {
        return calculateImpactScore();
    }

    @Override
    public abstract String toString();
}
