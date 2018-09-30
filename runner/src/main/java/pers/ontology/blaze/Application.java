package pers.ontology.blaze;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pers.ontology.blaze.server.server.BlazeServer;
import pers.ontology.blaze.utils.tool.SpringUtils;

/**
 * <h3></h3>
 *
 * @since 1.8
 */
@SpringBootApplication
public class Application {

    /**
     * 开始
     *
     * @param args
     */
    public static void main (String[] args) throws InterruptedException {
        SpringApplication.run(Application.class, args);
        BlazeServer blazeServer = SpringUtils.getBean(BlazeServer.class);
        blazeServer.start();
    }

}
