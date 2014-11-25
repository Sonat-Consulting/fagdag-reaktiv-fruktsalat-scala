package no.sonat.fruktsalat.apps;

import no.sonat.fruktsalat.FruitJavaApp;
import no.sonat.fruktsalat.JavaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class BlockingInARowJava8 extends FruitJavaApp {
    final Logger log = LoggerFactory.getLogger(getClass());
    public static void main(String[] args) {
        new BlockingInARowJava8().run();
    }

    void run() {
        JavaClient javaClient = getJavaClient();
        List<String> stuffWeNeed = getSuffWeNeed();

        List<String> ingredients = stuffWeNeed.stream()
                .map( whatWeNeed -> {
                    log.info("Starting to fetch " + whatWeNeed);
                    String gotIt = javaClient.getFruitNow(whatWeNeed);
                    log.info("Now we have " + gotIt);
                    return gotIt;
                }).collect(Collectors.toList());

        log.info("Our list is complete: " + toString(ingredients));
    }


}
