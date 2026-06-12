package com.example.footballradar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TransfermarktCrawler {
    private static final String BASE_URL = "https://www.transfermarkt.com/spieler-statistik/wertvollstespieler/marktwertetop";
    private static final String USER_AGENT = "Mozilla/5.0 FootballTransferRadar/1.0";

    public ArrayList<WebData> crawlAtLeast(int minimumCount) throws IOException {
        ArrayList<WebData> players = new ArrayList<>();

        for (int page = 1; players.size() < minimumCount && page <= 5; page++) {
            Document document = Jsoup.connect(BASE_URL + "?page=" + page)
                    .userAgent(USER_AGENT)
                    .referrer("https://www.google.com/")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .timeout(15000)
                    .get();

            Elements rows = document.select("table.items tbody tr.odd, table.items tbody tr.even");
            if (rows.isEmpty()) {
                rows = document.select("table.items tbody tr");
            }

            for (Element row : rows) {
                WebData item = parsePlayerRow(row, players.size() + 1);
                if (item != null) {
                    players.add(item);
                }
            }
        }

        if (players.size() < minimumCount) {
            throw new IOException("Crawler extracted only " + players.size()
                    + " football player value nodes; at least " + minimumCount + " are required.");
        }

        return players;
    }

    private WebData parsePlayerRow(Element row, int fallbackRank) {
        Element playerLink = findPlayerLink(row);
        if (playerLink == null) {
            return null;
        }

        String playerName = playerLink.text();
        String url = playerLink.absUrl("href");
        String rowText = row.text();
        String marketValueText = findMarketValueText(row);
        double marketValueMillions = parseMarketValueMillions(marketValueText);
        String position = findPosition(row);
        String currentClub = findCurrentClub(row);
        String transferSignal = marketValueMillions >= 50 ? "Elite transfer target" : "Transfer watchlist";

        if (marketValueMillions >= 25) {
            return new HighValueTransferRumor(fallbackRank, playerName, position, currentClub,
                    transferSignal, marketValueText, marketValueMillions, rowText, url);
        }

        return new TransferRumor(fallbackRank, playerName, position, currentClub,
                transferSignal, marketValueText, marketValueMillions, rowText, url);
    }

    private String findMarketValueText(Element row) {
        Elements candidates = row.select(".rechts.hauptlink, td.rechts, .marktwert, .mw");
        for (Element candidate : candidates) {
            String text = candidate.text();
            if (text.contains("€")) {
                return text;
            }
        }

        String[] tokens = row.text().split(" ");
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].contains("€")) {
                StringBuilder value = new StringBuilder(tokens[i]);
                if (i + 1 < tokens.length) {
                    value.append(" ").append(tokens[i + 1]);
                }
                return value.toString();
            }
        }

        return "Unknown";
    }

    private double parseMarketValueMillions(String valueText) {
        if (valueText == null) {
            return 0;
        }

        String normalized = valueText.replace("€", "")
                .replace("m", " m")
                .replace("k", " k")
                .replace(",", ".")
                .replaceAll("[^0-9. mk]", "")
                .trim()
                .toLowerCase();

        if (normalized.isEmpty()) {
            return 0;
        }

        String[] parts = normalized.split("\\s+");
        double number = parseDouble(parts[0]);
        if (normalized.contains(" k")) {
            return number / 1000.0;
        }
        return number;
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    private String findPosition(Element row) {
        Elements inlineRows = row.select("table.inline-table tr");
        String playerName = findPlayerName(row);
        for (Element inlineRow : inlineRows) {
            String text = inlineRow.text();
            if (!text.equals("") && !text.equals(playerName) && !text.contains("€")) {
                return text;
            }
        }

        Elements centeredCells = row.select("td.zentriert");
        for (Element cell : centeredCells) {
            String text = cell.text().trim();
            if (!text.contains("€") && !text.matches("\\d+") && text.length() > 1 && text.length() <= 24) {
                return text;
            }
        }
        return "Unknown";
    }

    private String findPlayerName(Element row) {
        Element playerLink = findPlayerLink(row);
        return playerLink == null ? "" : playerLink.text().trim();
    }

    private Element findPlayerLink(Element row) {
        Elements playerLinks = row.select("a[href*=/profil/spieler/], a[href*=/spieler/]");
        for (Element link : playerLinks) {
            if (!link.text().trim().isEmpty()) {
                return link;
            }
        }
        return null;
    }

    private String findCurrentClub(Element row) {
        LinkedHashSet<String> clubs = new LinkedHashSet<>();
        Elements clubLinks = row.select("a.vereinprofil_tooltip, a[href*=/verein/]");

        for (Element link : clubLinks) {
            String name = link.attr("title");
            if (name == null || name.trim().isEmpty()) {
                name = link.text();
            }
            name = name == null ? "" : name.trim();
            if (!name.isEmpty()) {
                clubs.add(name);
            }
        }

        ArrayList<String> names = new ArrayList<>(clubs);
        return names.size() >= 1 ? names.get(0) : "Unknown club";
    }
}
