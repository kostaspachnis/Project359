package rest;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author kostas
 */
@javax.ws.rs.ApplicationPath("book_modification")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> set = new HashSet<>();
        set.add(new Books_ex4());
        return set;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(rest.Books_ex4.class);
    }
}
