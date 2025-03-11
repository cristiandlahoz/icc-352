package org.example.util;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.lang.reflect.Method;
import java.util.List;
import org.example.util.annotations.Controller;
import org.example.util.annotations.Delete;
import org.example.util.annotations.Get;
import org.example.util.annotations.Post;
import org.example.util.annotations.Put;

public class Router {

  public static void registerRoutes(Javalin app) {
    List<Class<?>> controllers = ReflectionPaths.getClasses("org.example.controller");

    for (Class<?> clazz : controllers) {
      if (clazz.isAnnotationPresent(Controller.class)) {
        Controller controller = clazz.getAnnotation(Controller.class);

        try {
          // Obtener la instancia del controlador a través del contenedor de DI
          Object controllerInstance = DIContainer.get(clazz);

          for (Method method : clazz.getDeclaredMethods()) {
            String path = controller.path();

            if (method.isAnnotationPresent(Get.class)) {
              Get get = method.getAnnotation(Get.class);
              path += get.path();
              app.get(path, ctx -> invokeMethod(method, controllerInstance, ctx));
            } else if (method.isAnnotationPresent(Post.class)) {
              Post post = method.getAnnotation(Post.class);
              path += post.path();
              app.post(path, ctx -> invokeMethod(method, controllerInstance, ctx));
            } else if (method.isAnnotationPresent(Put.class)) {
              Put put = method.getAnnotation(Put.class);
              path += put.path();
              app.put(path, ctx -> invokeMethod(method, controllerInstance, ctx));
            } else if (method.isAnnotationPresent(Delete.class)) {
              Delete delete = method.getAnnotation(Delete.class);
              path += delete.path();
              app.delete(path, ctx -> invokeMethod(method, controllerInstance, ctx));
            }
          }
        } catch (Exception e) {
          System.err.println("❌ Error initializing controller: " + clazz.getName());
          e.printStackTrace();
        }
      } else {
        System.out.println("⏩ Skipping non-controller class: " + clazz.getName());
      }
    }
  }

  private static void invokeMethod(Method method, Object instance, Context ctx) {
    try {
      if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == Context.class) {
        method.invoke(instance, ctx);
      } else {
        System.err.println(
            "❌ Invalid method signature: " + method.getName() + " must accept only Context.");
      }
    } catch (Exception e) {
      System.err.println("❌ Error invoking method: " + method.getName());
      e.printStackTrace();
    }
  }
}
