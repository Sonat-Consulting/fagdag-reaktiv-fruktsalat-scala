package no.sonat.fruktsalat.apps;

import no.sonat.fruktsalat.FruitJavaApp;
import no.sonat.fruktsalat.JavaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BlockingInARowJava7 extends FruitJavaApp {
    final Logger log = LoggerFactory.getLogger(getClass());
    public static void main(String[] args) {
        new BlockingInARowJava7().run();
    }

    void run() {
        JavaClient javaClient = getJavaClient();
        List<String> stuffWeNeed = getSuffWeNeed();

        List<String> ingredients = new ArrayList<>();

        for( String whatWeNeed : stuffWeNeed) {
            log.info("Starting to fetch " + whatWeNeed);
            String gotIt = javaClient.getFruitNow(whatWeNeed);
            log.info("Now we have " + gotIt);
            ingredients.add(gotIt);
        }

        log.info("Our list is complete: " + toString(ingredients));
    }


}
