# archaius_object_support
extend the Netflix [Archaius](https://github.com/Netflix/archaius) to have dynamic object support

Archaius is a very powerful library for configuration management, however, it only support primitive type.
This small project is coming from a real project in my work. It provides Dynamic object support within Archaius.

### an example
```java
    String key = "user";
    String youngUser = "{\"age\":21,\"name\":\"jadetang\"}";
    
    ConfigurationManager.getConfigInstance().setProperty(key, youngUser);
    DynamicObjectProperty<User> user = new DynamicObjectProperty<>(key, null, User.class);
    
    System.out.println(user.get());
    
    System.out.println("Updating the property.");
    String oldUser = "{\"age\":99,\"name\":\"jadetang\"}";
    ConfigurationManager.getConfigInstance().setProperty(key, oldUser);
    
    System.out.println(user.get());
    
```
The output is:
```bash
User{age=21, name='jadetang'}
Updating the property.
User{age=99, name='jadetang'}
```

In production, we store all the configuration in Dynamodb and pull the latest value periodically, 
so the Java object will always reflect the latest value.