package github.jadetang.archauis.object.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.netflix.config.Property;
import com.rits.cloning.Cloner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDynamicObjectProperty<T> implements Property<T> {

  static final Logger log = LoggerFactory.getLogger(AbstractDynamicObjectProperty.class);

  static ObjectMapper objectMapper = new ObjectMapper();
  DynamicStringProperty delegate;
  Class<?> clazz;
  private volatile T value;
  private T defaultValue;

  /**
   * create a dynamic object with default value set to Null the object should be
   * serialized/deserialized to/from a json string
   *
   * @param propName the name of the property
   * @param defaultValue the default value if the property is not set.
   * @param clazz the class of your object
   */
  public AbstractDynamicObjectProperty(String propName, T defaultValue, Class<?> clazz) {
    setUp(propName, defaultValue, clazz);
  }

  private void setUp(String propName, T defaultValue, Class<?> clazz) {
    // Make a defensive copy of the default value.
    this.clazz = clazz;
    this.defaultValue = cloneDefaultValue(defaultValue);
    this.delegate = DynamicPropertyFactory.getInstance().getStringProperty(propName, null);
    load();
    Runnable callback = this::propertyChangedInternal;
    delegate.addCallback(callback);
  }

  private void propertyChangedInternal() {
    load();
  }

  private void load() {
    if (delegate.get() == null) {
      this.value = this.defaultValue;
    } else {
      T tempValue = loadFromJson(this.delegate.get());
      if (tempValue == null) {
        //if something wrong with the new value or original value, try to use the default value
        this.value = this.value == null ? this.defaultValue : this.value;
        log.error("Error updating object {} with new property {}:{}. Keep the old object",
            this.getClass().getSimpleName(), this.delegate, this.getValue());
      } else {
        this.value = tempValue;
      }
    }
  }

  abstract T loadFromJson(String value);

  private T cloneDefaultValue(T defaultValue) {
    Cloner cloner = new Cloner();
    return cloner.deepClone(defaultValue);
  }

  @Override
  public T getValue() {
    return value;
  }

  public T get() {
    return getValue();
  }

  @Override
  public T getDefaultValue() {
    return defaultValue;
  }

  @Override
  public String getName() {
    return this.delegate.getName();
  }

  @Override
  public long getChangedTimestamp() {
    return this.delegate.getChangedTimestamp();
  }

  @Override
  public void addCallback(Runnable callback) {
    this.delegate.addCallback(callback);
  }

  @Override
  public void removeAllCallbacks() {
    this.delegate.removeAllCallbacks();
  }

}
