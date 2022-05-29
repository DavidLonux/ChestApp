package pl.lonux.clientapplication.controller;

import lombok.RequiredArgsConstructor;
import pl.lonux.clientapplication.controller.utils.ApiConstants;
import pl.lonux.clientapplication.model.Code;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static pl.lonux.clientapplication.controller.utils.ApiConstants.*;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private boolean chestIsOpened = false;
    private final ModelAndView model = new ModelAndView(VIEW_NAME);

    @GetMapping
    public final ModelAndView chestPage(final Authentication authentication) {
        model.addObject(LOGGED_USER_WORD, authentication.getName());
        model.addObject(CHEST_IS_OPEN, chestIsOpened);
        model.addObject(CODE_OBJECT, new Code());
        return model;
    }

    @PostMapping
    public final ModelAndView openChestIfCorrect(@ModelAttribute("code") Code code) {
        if (checkCode(code.getCodeNumber())) {
            chestIsOpened = !chestIsOpened;
            model.addObject(CHEST_IS_OPEN, chestIsOpened);
            model.addObject(EMOJI_ICON, getEmoji());
        } else {
            chestIsOpened = false;
        } return model;
    }

    private String getEmoji() throws NullPointerException {
        final RestTemplate restTemplate = new RestTemplate();

        final List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        interceptors.add(new BasicAuthenticationInterceptor("David", "user"));

        final String serverURL = "http://localhost:8081/resources/emoji";
        final String response = restTemplate.getForObject(serverURL, String.class);

        if (response == null)
            throw new NullPointerException();

        return prepareEmoji(response);
    }

    private String prepareEmoji(final String emoji) {
        final String emojiDescription = emoji.substring(emoji.indexOf("|")+2);
        model.addObject(ApiConstants.EMOJI_DESCRIPTION, emojiDescription);
        
        return emoji.substring(0, emoji.indexOf("|"));
    }

    private boolean checkCode(final String codeNumber) {
        return Integer.parseInt(codeNumber) == 123;
    }
}
