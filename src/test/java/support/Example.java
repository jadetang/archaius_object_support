package support;

import com.netflix.config.ConfigurationManager;
import github.jadetang.archauis.object.support.DynamicObjectProperty;

public class Example {


  public static void main(String[] args) {

    String key = "user";
    String youngUser = "{\"age\":21,\"name\":\"jadetang\"}";

    ConfigurationManager.getConfigInstance().setProperty(key, youngUser);
    DynamicObjectProperty<User> user = new DynamicObjectProperty<>(key, null, User.class);
    System.out.println(user.get());
    String oldUser = "{\"age\":99,\"name\":\"jadetang\"}";
    System.out.println("Updating the property.");
    ConfigurationManager.getConfigInstance().setProperty(key, oldUser);
    System.out.println(user.get());

  }

  public static class User {

    private int age;

    private String name;

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return "User{" +
          "age=" + age +
          ", name='" + name + '\'' +
          '}';
    }
  }

}
