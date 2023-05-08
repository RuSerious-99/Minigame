package com.ruserious99.minigame.instance.game.deadspace.deadUtils.videoUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FileDownloader {
    public static void playVideoMain(String fileUrl, String fileName ) throws IOException, InterruptedException {
        //String fileUrl = "https://example.com/shoot_the_limbs.mp4";
       // String fileName = "shoot_the_limbs.mp4";
        downloadFile(fileUrl, fileName);
        playFile(fileName);
    }

    public static void playFile(String fileName) throws IOException {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            // Windows
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "start", "\"\"", fileName);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
        } else if (osName.contains("mac")) {
            // MacOS
            ProcessBuilder processBuilder = new ProcessBuilder("open", fileName);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            // Linux, Unix, or AIX
            ProcessBuilder processBuilder = new ProcessBuilder("xdg-open", fileName);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
        } else {
            // Unsupported operating system
            throw new UnsupportedOperationException("Unsupported operating system");
        }
    }

    public static void downloadFile(String fileUrl, String fileName) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(fileUrl))
                .build();
        HttpResponse<InputStream> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());

        InputStream inputStream = httpResponse.body();
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        byte[] dataBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(dataBuffer)) != -1) {
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        }

        fileOutputStream.close();
        inputStream.close();
    }
}





