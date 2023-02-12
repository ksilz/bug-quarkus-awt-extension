package com.betterprojectsfaster.opensource.quarkus.awtextension;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import net.coobird.thumbnailator.resizers.configurations.AlphaInterpolation;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import net.coobird.thumbnailator.resizers.configurations.Rendering;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@QuarkusMain
public class Application implements QuarkusApplication {

  @Override
  public int run(String... args) throws Exception {
    try {
      System.out.println();
      System.out.println("********************************");
      System.out.println("*   ALL THUMBS QUARKUS 1.0.1   *");
      System.out.println("********************************");
      System.out.println();
      System.out.println("This program will create thumbnails for all JPG, PNG, and GIF pictures in the current directory.");
      System.out.println();

      var inputDir = new File(".");
      var files = inputDir.listFiles();

      if (files != null && files.length > 0) {
        List<String> pictureFiles =
            Arrays.stream(files)
                .filter(
                    f ->
                        f.isFile()
                            && (f.getName().endsWith(".jpg")
                            || f.getName().endsWith(".gif")
                            || f.getName().endsWith(".png")))
                .map(File::getName)
                .toList();

        if (pictureFiles.size() > 0) {
          var currentDir = inputDir.getCanonicalPath();
          var outputDir = new File(currentDir + File.separator + "thumbnails");
          var goOn = true;

          if (outputDir.exists() == false) {
            goOn = outputDir.mkdir();

            if (goOn == false) {
              System.err.println("ERROR: Could not create 'thumbnails' directory!");
            }
          }

          if (goOn) {
            var pictureArray = pictureFiles.toArray(new String[0]);
            System.out.println(
                "Creating thumbnails for "
                    + pictureArray.length
                    + " pictures in the 'thumbnails' directory...");
            var start = System.currentTimeMillis();

            Thumbnails.of(pictureArray)
                .allowOverwrite(true)
                .size(400, 400)
                .outputFormat("jpg")
                .alphaInterpolation(AlphaInterpolation.QUALITY)
                .antialiasing(Antialiasing.ON)
                .outputQuality(1f)
                .rendering(Rendering.QUALITY)
                .scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
                .keepAspectRatio(true)
                .toFiles(outputDir, Rename.NO_CHANGE);

            var stop = System.currentTimeMillis();
            var duration = (stop - start) / 1000f;
            var formatter = new DecimalFormat("###,###.##");
            var formattedDuration = formatter.format(duration);
            System.out.println("Done creating thumbnails in " + formattedDuration + " seconds. ");
          }
        } else {
          System.err.println("ERROR: No picture files found in current directory!");
        }

      } else {
        System.err.println("ERROR: No files found in current directory");
      }

      System.out.println();
    } catch (IOException e) {
      System.err.println("ERROR: Fatal error calculating thumbnails!");
      e.printStackTrace();
      System.exit(1);
    }

    return 0;
  }
}
