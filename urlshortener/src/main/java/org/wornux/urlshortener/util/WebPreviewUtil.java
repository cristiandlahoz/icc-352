package org.wornux.urlshortener.util;

import java.util.Base64;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebPreviewUtil {

  public static String captureBase64Preview(String websiteUrl) {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless"); // Modo sin ventana
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-gpu");

    WebDriver driver = new ChromeDriver(options);
    try {
      driver.get(websiteUrl);
      Thread.sleep(2000); // Esperar que cargue

      byte[] screenshot =
          ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

      return "data:image/png;base64," + Base64.getEncoder().encodeToString(screenshot);
    } catch (Exception e) {
      throw new RuntimeException("Error al capturar la vista previa", e);
    } finally {
      driver.quit();
    }
  }
}
