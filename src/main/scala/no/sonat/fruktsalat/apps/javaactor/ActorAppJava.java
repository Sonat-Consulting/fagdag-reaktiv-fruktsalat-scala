package no.sonat.fruktsalat.apps.javaactor;

import akka.actor.ActorRef;
import akka.actor.Props;
import no.sonat.fruktsalat.FruitJavaApp;
import no.sonat.fruktsalat.JavaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActorAppJava extends FruitJavaApp {
    final Logger log = LoggerFactory.getLogger(getClass());
    public static void main(String[] args) {
        new ActorAppJava().run();
    }


    void run() {
        JavaClient javaClient = getJavaClient();
        log.info("Start our fruitSalatActor");

        ActorRef fruitSalatActor = system().actorOf(Props.create(FruitSalatActor.class, client()), "fruitSalatActor");

        log.info("Tell it to make our salat");

        fruitSalatActor.tell(new MakeFruitSalat(getSuffWeNeed()), ActorRef.noSender());

        log.info("Done with this work...");

    }


}


