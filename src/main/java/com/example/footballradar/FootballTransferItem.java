package com.example.footballradar;

public abstract class FootballTransferItem extends WebData {
    private final String playerName;
    private final String position;
    private final String currentClub;
    private final String transferSignal;
    private final String marketValueText;
    private final double marketValueMillions;
    private final String rawSummary;

    public FootballTransferItem(int sourceRank, String playerName, String position,
                                String currentClub, String transferSignal, String marketValueText,
                                double marketValueMillions, String rawSummary, String url) {
        super(sourceRank, clean(playerName, "Unknown player") + " transfer radar", url, "Transfermarkt");
        this.playerName = clean(playerName, "Unknown player");
        this.position = clean(position, "Unknown");
        this.currentClub = clean(currentClub, "Unknown club");
        this.transferSignal = clean(transferSignal, "Transfer watchlist");
        this.marketValueText = clean(marketValueText, "Unknown");
        this.marketValueMillions = Math.max(0, marketValueMillions);
        this.rawSummary = clean(rawSummary, this.playerName + " " + this.marketValueText);
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPosition() {
        return position;
    }

    public String getCurrentClub() {
        return currentClub;
    }

    public String getTransferSignal() {
        return transferSignal;
    }

    public String getTargetClub() {
        return transferSignal;
    }

    public String getMarketValueText() {
        return marketValueText;
    }

    public double getMarketValueMillions() {
        return marketValueMillions;
    }

    public String getRawSummary() {
        return rawSummary;
    }

    protected double clubSignalBonus() {
        return "Unknown club".equals(currentClub) ? 0 : 12;
    }

    private static String clean(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value.trim().replaceAll("\\s+", " ");
    }
}
