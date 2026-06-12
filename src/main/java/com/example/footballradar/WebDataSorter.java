package com.example.footballradar;

import java.util.ArrayList;

public class WebDataSorter {
    public void insertionSortByImpactDescending(ArrayList<WebData> data) {
        for (int i = 1; i < data.size(); i++) {
            WebData current = data.get(i);
            int j = i - 1;

            while (j >= 0 && data.get(j).calculateImpactScore() < current.calculateImpactScore()) {
                data.set(j + 1, data.get(j));
                j--;
            }

            data.set(j + 1, current);
        }
    }
}
