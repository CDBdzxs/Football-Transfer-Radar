package com.example.footballradar;

public class HighValueTransferRumor extends FootballTransferItem {
    public HighValueTransferRumor(int sourceRank, String playerName, String position,
                                  String currentClub, String transferSignal, String marketValueText,
                                  double marketValueMillions, String rawSummary, String url) {
        super(sourceRank, playerName, position, currentClub, transferSignal,
                marketValueText, marketValueMillions, rawSummary, url);
    }

    @Override
    public String getDataType() {
        return "HIGH_VALUE";
    }

    @Override
    public double calculateImpactScore() {
        return 30 + (getMarketValueMillions() * 1.35) + clubSignalBonus()
                + Math.max(0, 60 - getSourceRank()) * 0.4;
    }

    @Override
    public String toString() {
        return getDataType() + " | " + getPlayerName()
                + " | value=" + getMarketValueText()
                + " | club=" + getCurrentClub()
                + " | impact=" + String.format("%.2f", calculateImpactScore());
    }
}
