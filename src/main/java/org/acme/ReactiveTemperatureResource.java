package org.acme;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import org.acme.entity.NamespaceEntity;
import org.eclipse.microprofile.reactive.messaging.*;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/temperature")
public class ReactiveTemperatureResource {

    @Inject
    @Channel("temperature-emitter")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER)
    Emitter<String> emitter;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Void> emmitTemperature() {
        return Panache
                .withTransaction(() -> {
                    NamespaceEntity namespaceEntity = new NamespaceEntity();
                    namespaceEntity.id = UUID.randomUUID().toString();
                    namespaceEntity.name = "any name";
                    return namespaceEntity.<NamespaceEntity>persist();
                })
                .chain(namespaceEntity -> Uni.createFrom()
                        .completionStage(emitter.send(namespaceEntity.id))
                );
    }

    @Incoming("temperature-incoming")
    @Acknowledgment(Acknowledgment.Strategy.MANUAL)
    protected Uni<Void> verifyTicket(Message<String> inMessage) {
        return Uni.createFrom().completionStage(inMessage.ack());
    }

}
