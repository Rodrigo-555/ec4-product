package pe.idat.EC4_Product.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import pe.idat.EC4_Product.controller.ProductController;
import pe.idat.EC4_Product.security.BasicAuthFilter;

@Configuration
public class ServerConfig extends ResourceConfig {
    public ServerConfig() {
        register(ProductController.class);
        register(BasicAuthFilter.class);
    }
}
