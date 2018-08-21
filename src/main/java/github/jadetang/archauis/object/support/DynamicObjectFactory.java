package github.jadetang.archauis.object.support;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DynamicObjectFactory {

  private static final Logger log = LoggerFactory.getLogger(DynamicObjectFactory.class);
  private static Cache<String, AbstractDynamicObjectProperty> cache = CacheBuilder.newBuilder()
      .maximumSize(1000).expireAfterAccess(10, TimeUnit.MINUTES).build();

  private DynamicObjectFactory() {

  }

  @SuppressWarnings("unchecked")
  public static <T> DynamicObjectProperty<T> getDynamicObjectProperty(String propName,
      T defaultValue, Class<T> clazz) {
    try {
      return (DynamicObjectProperty<T>) cache
          .get(propName, () -> new DynamicObjectProperty<>(propName, defaultValue, clazz));
    } catch (ExecutionException e) {
      log.error(String.format("Loading configuration error, propName :%s", propName), e);
      return null;
    }
  }

}
