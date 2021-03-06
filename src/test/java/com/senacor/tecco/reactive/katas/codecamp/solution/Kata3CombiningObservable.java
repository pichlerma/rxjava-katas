package com.senacor.tecco.reactive.katas.codecamp.solution;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import org.junit.Test;
import rx.Observable;
import rx.observables.ConnectableObservable;

import java.util.concurrent.TimeUnit;

import static com.senacor.tecco.reactive.ReactiveUtil.print;
import static rx.Observable.zip;

/**
 * @author Andreas Keefer
 */
public class Kata3CombiningObservable {

    private final WikiService wikiService = new WikiService();
    private final RatingService ratingService = new RatingService();
    private final CountService countService = new CountService();

    @Test
    public void combiningObservable() throws Exception {
        // 1. fetch and parse Wikiarticle
        // 2. use ratingService.rateObservable() and #countWordsObervable(). Combine both information as JSON
        //    and print the JSON to the console. Example {"articleName": "Superman", "rating": 3, "wordCount": 452}

        WaitMonitor waitMonitor = new WaitMonitor();

        final String wikiArticle = "Bilbilis";
        wikiService.fetchArticleObservable(wikiArticle)
                .flatMap(wikiService::parseMediaWikiTextObservable)
                .flatMap(parsedPage -> {
                    Observable<Integer> rating = ratingService.rateObservable(parsedPage);
                    Observable<Integer> wordCount = countService.countWordsObervable(parsedPage);
                    return zip(rating, wordCount, (r, wc) -> String.format(
                            "{\"articleName\": \"%s\", \"rating\": %s, \"wordCount\": %s}",
                            wikiArticle, r, wc));
                })
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        waitMonitor::complete);

        waitMonitor.waitFor(10, TimeUnit.SECONDS);
    }

    @Test
    public void combiningObservableOption2_zipWith() throws Exception {
        // 1. fetch and parse Wikiarticle
        // 2. use ratingService.rateObservable() and #countWordsObervable(). Combine both information as JSON
        //    and print the JSON to the console. Example {"articleName": "Superman", "rating": 3, "wordCount": 452}

        WaitMonitor waitMonitor = new WaitMonitor();

        final String wikiArticle = "Bilbilis";
        wikiService.fetchArticleObservable(wikiArticle)
                .flatMap(wikiService::parseMediaWikiTextObservable)
                .flatMap(parsedPage -> {
                    Observable<Integer> rating = ratingService.rateObservable(parsedPage);
                    Observable<Integer> wordCount = countService.countWordsObervable(parsedPage);
                    return rating.zipWith(wordCount, (r, wc) -> String.format(
                            "{\"articleName\": \"%s\", \"rating\": %s, \"wordCount\": %s}",
                            wikiArticle, r, wc));
                })
                .subscribe(next -> print("next: %s", next),
                        Throwable::printStackTrace,
                        waitMonitor::complete);

        waitMonitor.waitFor(10, TimeUnit.SECONDS);
    }


    @Test
    public void combiningObservablePublish() throws Exception {
        // 1. fetch and parse Wikiarticle
        // 2. use ratingService.rateObservable() and #countWordsObervable(). Combine both information as JSON
        //    and print the JSON to the console. Example {"articleName": "Superman", "rating": 3, "wordCount": 452}

        WaitMonitor waitMonitor = new WaitMonitor();

        final String wikiArticle = "Bilbilis";
        ConnectableObservable<ParsedPage> parsedPageObservable = wikiService.fetchArticleObservable(wikiArticle)
                .flatMap(wikiService::parseMediaWikiTextObservable).publish();

        Observable<Integer> ratingObservable = parsedPageObservable.flatMap(ratingService::rateObservable);
        Observable<Integer> wordCountObservable = parsedPageObservable.flatMap(countService::countWordsObervable);
        parsedPageObservable.connect();

        zip(ratingObservable, wordCountObservable, (r, wc) -> String.format(
                            "{\"articleName\": \"%s\", \"rating\": %s, \"wordCount\": %s}",
                            wikiArticle, r, wc))
            .subscribe(next -> print("next: %s", next),
                    Throwable::printStackTrace,
                    () -> waitMonitor.complete());

        waitMonitor.waitFor(10, TimeUnit.SECONDS);
    }

}
