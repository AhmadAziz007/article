package com.news.article.controller.scheduler;

import com.news.article.service.rss.RSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RSSScheduler {
    private final RSSService rssService;

    @Autowired
    public RSSScheduler(RSSService rssService) {
        this.rssService = rssService;
        System.out.println("RSSService injected: " + (rssService != null));
    }

    @Scheduled(fixedRate = 10000, initialDelay = 5000)
    private void rssScheduler() {
        System.out.println("rssScheduler started");
        try {
            rssService.GetNews();
            System.out.println("rssScheduler finished successfully");
        } catch (Exception e) {
            System.out.println("Error in rssScheduler: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
