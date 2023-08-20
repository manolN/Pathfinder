package com.softuni.Pathfinder.service;

import com.softuni.Pathfinder.model.view.StatsView;

public interface StatsService {

    void onRequest();

    StatsView getStats();
}
