package pers.ontology.blaze.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pers.ontology.blaze.server.server.BlazeServer;
import pers.ontology.blaze.server.server.ServerInitializer;

/**
 * <h3></h3>
 *
 * @author ontology
 * @since 1.8
 */
@Configuration
public class BlazeServerConfiguration {


//    @Bean
//    public ServerInitializer initServerInitializer () {
//        ServerInitializer initializer = new ServerInitializer();
////        initializer.setServerHandler(serverHandler);
////        initializer.setMultiProtobufEncoder(multiProtobufEncoder);
////        initializer.setMultiProtobufDecoder(multiProtobufDecoder);
//        return initializer;
//    }

    @Bean
    public BlazeServer initBlazeServer () {
        BlazeServer blazeServer = new BlazeServer();
        blazeServer.setChildHandler(new ServerInitializer());
        return blazeServer;
    }


//    @Bean
//    @Scope("prototype")
//    public ServerHandler initServerHandler () {
//        return new ServerHandler();
//    }
//
//    @Bean
//    @Scope("prototype")
//    public MultiProtobufEncoder initMultiProtobufEncoder () {
//        return new MultiProtobufEncoder();
//    }
//
//    @Bean
//    @Scope("prototype")
//    public MultiProtobufDecoder initMultiProtobufDecoder () {
//        return new MultiProtobufDecoder();
//    }

}
