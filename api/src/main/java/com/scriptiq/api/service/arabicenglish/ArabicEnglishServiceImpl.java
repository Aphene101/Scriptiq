package com.scriptiq.api.service.arabicenglish;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ArabicEnglishServiceImpl implements ArabicEnglishService {

    @Override
    public String convert(
            String text
    ) {
        throw new ResponseStatusException(
                HttpStatus.NOT_IMPLEMENTED,
                "Arabic-English model is not available yet"
        );
    }
}
