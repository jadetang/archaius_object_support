package github.jadetang.archauis.object.support;

import java.io.IOException;

public class DynamicObjectProperty<T> extends AbstractDynamicObjectProperty<T> {

  public DynamicObjectProperty(String propName, T defaultValue, Class<T> clazz) {
    super(propName, defaultValue, clazz);
  }

  @Override
  @SuppressWarnings("unchecked")
  T loadFromJson(String value) {
    try {
      return objectMapper.readValue(value, (Class<T>) this.clazz);
    } catch (IOException e) {
      log.error(String.format("Error parsing value to object, property %s=%s ",
          this.delegate.getName(), this.delegate.getValue()), e);
      return null;
    }
  }
}
