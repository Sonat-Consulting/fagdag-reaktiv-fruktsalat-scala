package no.sonat.fruktsalat.apps.javaactor;

import akka.actor.UntypedActor;
import akka.dispatch.OnSuccess;
import no.sonat.fruktsalat.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FruitSalatActor extends UntypedActor {

    final Logger log = LoggerFactory.getLogger(getClass());
    final Client client; // Not using java-client to get proper Futures

    boolean isMaking = false;
    List<String> whatWeNeed = new ArrayList<>();
    List<String> whatWeHave = new ArrayList<>();


    public FruitSalatActor(Client client) {
        this.client = client;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof MakeFruitSalat) {
            if (isMaking) {
                log.warn("Cannot make another fruitSalat now - still working on the previous one..");
            } else {
                List<String> ingredients = ((MakeFruitSalat) message).ingredients;

                log.info("We're going to make a fruitSalat of: "+toString(ingredients)+". Getting what we need..");
                whatWeNeed.addAll(ingredients);
                isMaking = true;

                // For all ingredients, fetch it, and send the result back to us when it is ready
                ingredients.stream()
                        .forEach(name -> {
                            client.getFruit(name).onSuccess(new OnSuccess<String>() {
                                @Override
                                public void onSuccess(String result) throws Throwable {
                                    // Send it back to the actor
                                    self().tell(new GotOneIngredient(result), self());
                                }
                            }, context().dispatcher());
                        });
            }
        } else if (message instanceof GotOneIngredient) {
            String name = ((GotOneIngredient) message).name;
            log.info("Got one ingredient: " + name);
            whatWeHave.add(name);

            log.info("We now have {} of {} ingredients", whatWeHave.size(), whatWeNeed.size());

            if (whatWeHave.size() >= whatWeNeed.size()) {
                log.info("** We now have all we need!! Making the fruitSalat!!!");
                // since we're done, we can clean up and be ready to make another one
                isMaking = false;
                whatWeHave.clear();
                whatWeNeed.clear();
            }
        }
    }

    private String toString(List<String> list) {
        return list.stream().collect(Collectors.joining(","));
    }
}
