package com.example.footballradar;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

@Service
public class WebDataService {
    private static final int REQUIRED_NODE_COUNT = 50;

    private final TransfermarktCrawler crawler = new TransfermarktCrawler();
    private final WebDataSorter sorter = new WebDataSorter();

    public WebDataReport fetchFreshReport() throws IOException {
        ArrayList<WebData> items = crawler.crawlAtLeast(REQUIRED_NODE_COUNT);
        sorter.insertionSortByImpactDescending(items);
        return new WebDataReport(
                "Transfermarkt live player market-value table",
                LocalDateTime.now().toString(),
                REQUIRED_NODE_COUNT,
                items.size(),
                "Handwritten insertion sort by player market value and transfer-watch impact score",
                items
        );
    }
}
