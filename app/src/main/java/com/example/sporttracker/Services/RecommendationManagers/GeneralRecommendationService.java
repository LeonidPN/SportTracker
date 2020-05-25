package com.example.sporttracker.Services.RecommendationManagers;

import com.example.sporttracker.R;

import java.util.ArrayList;

public class GeneralRecommendationService {

    public static ArrayList<Integer> getRecommendationStringsId(int groupId) {
        ArrayList<Integer> recommendation = new ArrayList<>();
        recommendation.add(R.string.wos_groups_description);
        switch (groupId) {
            case R.integer.age_group_1:
                recommendation.add(R.string.wos_group_1_recommendation);
                break;
            case R.integer.age_group_2:
                recommendation.add(R.string.wos_group_2_recommendation);
                break;
            case R.integer.age_group_3:
                recommendation.add(R.string.wos_group_3_recommendation);
                break;
        }
        recommendation.add(R.string.wos_groups_description_end);
        return recommendation;
    }

}
