package com.vip.spider;

import com.alibaba.fastjson.JSON;
import com.vip.spider.bean.CrawlPointAttr;
import com.vip.spider.bean.ParseResult;
import com.vip.spider.bean.SpringContext;
import com.vip.spider.component.ListParser;
import com.vip.spider.component.loader.PageLoader;
import com.vip.spider.component.ComponentBuilder;
import com.vip.spider.component.loader.PageIndexLoader;
import com.vip.dbservice.model.AttackPage;
import com.vip.dbservice.service.AttackPageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CrawlTask implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(CrawlTask.class);
    private ListParser listParser;// 列表解析器
    private PageIndexLoader pageIndexLoader;// 分页加载器
    private PageLoader pageLoader;// 页面加载器
    private CrawlPointAttr crawlPointAttr;

    public CrawlTask(CrawlPointAttr crawlPointAttr) {
        this.crawlPointAttr = crawlPointAttr;
    }

    @Override
    public void run() {

        try {
            int pageNum = 0;
            // 创建组件
            ComponentBuilder componentBuilder = new ComponentBuilder(crawlPointAttr);
            listParser = componentBuilder.buildListParser();
            pageIndexLoader = componentBuilder.buildPageIndexLoader();
            pageLoader = componentBuilder.buildPageLoader();

            boolean isNeedSpider = true;// 是否需要爬取
            boolean isFirst = true;
            List<ParseResult> allParseResultList = new ArrayList<>();
            while (pageIndexLoader.isNext() && pageNum < crawlPointAttr.getMaxPage()) {
                try {
                    String response = pageIndexLoader.next();
                    List<ParseResult> parseResultList = listParser.parse(response);

                    if (parseResultList == null || parseResultList.size() == 0) {//如果列表为空则认为没有下一页了，此时退出，如知乎
                        break;
                    }

                    //判断该爬取源是否需要爬取 策略：前N条记录DB中是否已存在
                    if (isFirst) {
                        isFirst = false;
                        if (!isNeedCrawl(parseResultList)) {
                            break;
                        }
                    }
                    for (ParseResult parseResult : parseResultList) {
                        if (!isCrawl(parseResult)) {
                            isNeedSpider = false;
                            parseResultList.subList(parseResultList.indexOf(parseResult), parseResultList.size()).clear();
                            break;
                        }
                        // 加载详细页
                        if (crawlPointAttr.getCrawlDetail()) {
                            pageLoader.load(parseResult);
                        }
                        parseResult.setCategory(crawlPointAttr.getCategory());
                        parseResult.setBelong(crawlPointAttr.getBelong());
                        parseResult.setPointLink(crawlPointAttr.getUrl());

                        //保存到数据库
                        saveToDb(parseResult);
                        LOGGER.info(JSON.toJSONString(parseResult));
                        // 追加数据
                        //allParseResultList.add(parseResult);
                    }
                    if (!isNeedSpider) {
                        break;
                    }
                    pageNum++;

                    Thread.sleep(crawlPointAttr.getSleepTime());

                } catch (Exception e) {
                    LOGGER.error("", e);
                }
            }
            LOGGER.info("任务{}完成{},共采集点{}页{}条数据", crawlPointAttr.getTaskid(), crawlPointAttr.getCategory(),
                    pageIndexLoader.getCurCount(), allParseResultList.size());

        } catch (Exception e) {
            LOGGER.error("", e);
        }

    }

    //保存采集结果
    private void saveAll(List<ParseResult> list) {

        AttackPageService attackPageService = (AttackPageService) SpringContext.getContext().getBean("attackPageService");

        for (ParseResult parseResult : list) {

            AttackPage attackPage = new AttackPage();
            attackPage.setLink(parseResult.getLink());

            //判断数据库中是否已存在
            if (attackPageService.listByCondition(attackPage).size() > 0)
                continue;

            attackPage.setBelong(parseResult.getBelong());
            attackPage.setCategory(parseResult.getCategory());
            attackPage.setCount(0);
            attackPage.setPointLink(parseResult.getPointLink().replace("{pagenum}", "1"));
            attackPage.setTitle(parseResult.getTitle());
            attackPage.setAttr(JSON.toJSONString(parseResult.getAttr()));
            attackPage.setCreateTime(new Date());
            attackPage.setUpdateTime(new Date());

            attackPageService.save(attackPage);
        }
    }

    //保存采集结果
    private void saveToDb(ParseResult parseResult) {

        AttackPageService attackPageService = (AttackPageService) SpringContext.getContext().getBean("attackPageService");

        AttackPage attackPage = new AttackPage();
        attackPage.setLink(parseResult.getLink());

        //判断数据库中是否已存在
        if (attackPageService.listByCondition(attackPage).size() > 0)
            return;

        attackPage.setBelong(parseResult.getBelong());
        attackPage.setCategory(parseResult.getCategory());
        attackPage.setCount(0);
        attackPage.setPointLink(parseResult.getPointLink().replace("{pagenum}", "1"));
        attackPage.setTitle(parseResult.getTitle());
        attackPage.setAttr(JSON.toJSONString(parseResult.getAttr()));
        attackPage.setCreateTime(new Date());
        attackPage.setUpdateTime(new Date());

        attackPageService.save(attackPage);
    }

    /**
     * 是否爬取
     *
     * @param parseResult
     * @return
     */
    private boolean isCrawl(ParseResult parseResult) {
        List<ParseResult> list;

        return true;
    }

    /**
     * 是否需要爬取
     */
    private boolean isNeedCrawl(List<ParseResult> parseResultList) {

        return true;
    }

}
