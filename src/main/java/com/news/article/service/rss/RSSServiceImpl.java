package com.news.article.service.rss;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.news.article.model.entity.NewsArticle;
import com.news.article.repository.rss.RSSRepository;
import com.news.article.util.NodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.util.Date;
import java.util.List;

@Service
public class RSSServiceImpl implements RSSService {
    private final RSSRepository rssRepository;

    @Autowired
    public RSSServiceImpl(RSSRepository rssRepository) {
        this.rssRepository = rssRepository;
    }

    @Override
    public void GetNews() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("https://jambi.antaranews.com/rss/terkini.xml");

            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xpath = xpathfactory.newXPath();

            XPathExpression expr = xpath.compile("//item");
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            System.out.println(nodes.getLength() + " data found(s)");

            List<NewsArticle> list = NodeUtil.extractNodeList(nodes);
            System.out.println("Result: " + new ObjectMapper().writeValueAsString(list));
            System.out.println(list.size() + " data found(s)");

            int i = 0;
            for (NewsArticle data : list) {
                // check exist data
                List<NewsArticle> existList = rssRepository.findByUrl(data.getUrl());

                if (existList.isEmpty()) {
                    i++;
                    data.setInserted(new Date());
                    rssRepository.save(data);
                }
            }

            System.out.println(i + " data(s) added successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
