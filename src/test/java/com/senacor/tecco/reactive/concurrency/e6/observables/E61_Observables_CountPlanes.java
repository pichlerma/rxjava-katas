package com.senacor.tecco.reactive.concurrency.e6.observables;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.concurrency.PlaneArticleBaseTest;
import com.senacor.tecco.reactive.concurrency.Summary;
import com.senacor.tecco.reactive.concurrency.model.Article;
import com.senacor.tecco.reactive.concurrency.model.PlaneInfo;
import org.junit.Test;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class E61_Observables_CountPlanes extends PlaneArticleBaseTest {

    @Test
    public void thatPlaneInfoIsSummedUpCombinedWithObservables() throws Exception {
        WaitMonitor monitor = new WaitMonitor();

        // error handler function
        Action1<Throwable> exceptionConsumer = (e)->{e.printStackTrace();monitor.complete();};

        String[] planeTypes = {"Boeing 777", "Boeing 747", "Boeing 737", "Airbus A330"};

        Observable.from(planeTypes)
            .flatMap(this::fetchArticle)
            .map(this::parsePlaneInfo)
            .subscribe((planeInfo) -> {
                Summary.printCounter(planeInfo.typeName, planeInfo.numberBuild);
            },
            exceptionConsumer,
            monitor::complete);

        monitor.waitFor(10000,TimeUnit.MILLISECONDS);
    }

    // fetches an article from the wikipedia
    Observable<Article> fetchArticle(String articleName) {
        return wikiService.fetchArticleObservable(articleName)
                .map((article) -> new Article(articleName, article));
    }

    // Extracts plane-related information from an wikipedia article
    PlaneInfo parsePlaneInfo(Article article){
        return new PlaneInfo(article.name, parseBuildCountInt(article.content));
    }

}
