package no.sonat.fruktsalat.apps;

import no.sonat.fruktsalat.FruitJavaApp;
import no.sonat.fruktsalat.JavaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class AsyncAppJava extends FruitJavaApp {
    final Logger log = LoggerFactory.getLogger(getClass());
    public static void main(String[] args) {
        new AsyncAppJava().run();
    }

    void run() {
        JavaClient javaClient = getJavaClient();
        List<String> stuffWeNeed = getSuffWeNeed();

        List<Future<String>> futureIngredients = stuffWeNeed.stream()
                .map(whatWeNeed -> {
                    log.info("Starting to fetch " + whatWeNeed);
                    Future<String> futureToResult = javaClient.getFruit(whatWeNeed);
                    log.info("Now we're waiting for " + whatWeNeed);
                    return futureToResult;
                }).collect(Collectors.toList());

        log.info("Now we're going to wait for all our futures - when all are done, we have our result");

        List<String> ingredients = futureIngredients.stream()
                .map(f -> {
                    try {
                        String oneIngredient = f.get();
                        log.info("Got one ingredient: " + oneIngredient);
                        return oneIngredient;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        log.info("Our list is complete: " + toString(ingredients));


    }
}
