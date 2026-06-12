package com.example.footballradar;

public class TransferRumor extends FootballTransferItem {
    public TransferRumor(int sourceRank, String playerName, String position,
                         String currentClub, String transferSignal, String marketValueText,
                         double marketValueMillions, String rawSummary, String url) {
        super(sourceRank, playerName, position, currentClub, transferSignal,
                marketValueText, marketValueMillions, rawSummary, url);
    }

    @Override
    public String getDataType() {
        return "WATCHLIST";
    }

    @Override
    public double calculateImpactScore() {
        return getMarketValueMillions() + clubSignalBonus() + Math.max(0, 60 - getSourceRank()) * 0.25;
    }

    @Override
    public String toString() {
        return getDataType() + " | " + getPlayerName()
                + " | value=" + getMarketValueText()
                + " | club=" + getCurrentClub()
                + " | impact=" + String.format("%.2f", calculateImpactScore());
    }
}
