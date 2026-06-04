package com.scriptiq.api.service.shared;

import org.springframework.stereotype.Service;

@Service
public class InputTypeDetector {

    public boolean isArabic(String text) {

        for (char c : text.toCharArray()) {

            if (Character.UnicodeBlock.of(c)
                    == Character.UnicodeBlock.ARABIC) {
                return true;
            }
        }

        return false;
    }
}
