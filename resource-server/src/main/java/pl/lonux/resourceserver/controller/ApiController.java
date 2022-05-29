package pl.lonux.resourceserver.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resources")
public class ApiController {

    @GetMapping(path = "/emoji")
    public final String getEmoji() {
        final String fileName = "src/main/resources/emoji.txt";
        return readEmoji(fileName);
    }

    private String readEmoji(final String fileName) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            final List<String> lines = reader.lines().toList();
            final int randomEmojiIndex = SecureRandom
                    .getInstanceStrong()
                    .nextInt(lines.size());

            final String emoji = lines.get(randomEmojiIndex);
            if(emoji.contains("$") && emoji.contains("&")) {
                final String emojiHTML_code = emoji.substring(0, emoji.indexOf("&"));
                final String emojiHTML_description = emoji.substring(emoji.indexOf("$")+2);
                return  emojiHTML_code + " | " + emojiHTML_description;
            } else {
                throw new IllegalArgumentException("Damaged file");
            }

        } catch (final IOException | IllegalArgumentException | NoSuchAlgorithmException exception) {
            return "Error " + exception.getMessage();
        }
    }
}