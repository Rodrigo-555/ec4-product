package pe.idat.EC4_Product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.idat.EC4_Product.entity.Product;
import pe.idat.EC4_Product.repository.ProductRepository;

import java.util.List;

@Service
@Path("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            String json = objectMapper.writeValueAsString(products);
            return Response.ok(json, MediaType.APPLICATION_JSON).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al convertir a JSON")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductById(@PathParam("id") Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            try {
                String json = objectMapper.writeValueAsString(product);
                return Response.ok(json, MediaType.APPLICATION_JSON).build();
            } catch (JsonProcessingException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Error al convertir a JSON")
                        .build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\":\"Producto no encontrado\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProduct(String json) {
        try {
            Product newProduct = objectMapper.readValue(json, Product.class);
            productRepository.save(newProduct);
            String createdJson = objectMapper.writeValueAsString(newProduct);
            return Response.status(Response.Status.CREATED)
                    .entity(createdJson)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al convertir a JSON")
                    .build();
        }
    }

    @PUT
    @Path("/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProduct(@PathParam("name") String name, String json) {
        try {
            Product updateProduct = objectMapper.readValue(json, Product.class);
            Product existingProduct = productRepository.findByName(name);
            if (existingProduct != null) {
                existingProduct.setName(updateProduct.getName());
                existingProduct.setPrice(updateProduct.getPrice());
                existingProduct.setStock(updateProduct.getStock());
                productRepository.save(existingProduct);
                return Response.ok(existingProduct, MediaType.APPLICATION_JSON).build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Producto no encontrado\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al convertir a JSON")
                    .build();
        }
    }

    @DELETE
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProduct(@PathParam("name") String name) {
        Product product = productRepository.findByName(name);
        if (product != null) {
            productRepository.delete(product);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"message\":\"Producto no encontrado\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}