package com.example.eunboard.ticket.application.port.in;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.junit.MockitoTestRule;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TicketCreateRequestDtoTest {

    @Test
    void timeRegexTest() {
        String example ="[0-2][0-9][0-5][0-9]";
        List<String> times = new ArrayList<>();
        times.add("2959");
        times.add("2400");
        times.add("2401");
        Pattern pattern = Pattern.compile(example);
        for (String time : times) {
            Matcher matcher = pattern.matcher(time);
            Assertions.assertThat(matcher.matches()).isTrue();
        }
        String newExample = "([0-1]?[0-9]|2[0-3])[0-5][0-9]";
        Pattern pattern2 = Pattern.compile(newExample);
        for (String time : times) {
            Matcher matcher = pattern2.matcher(time);
            Assertions.assertThat(matcher.matches()).isFalse();
        }

        List<String> correct = new ArrayList<>();
        correct.add("0959");
        correct.add("0000");
        correct.add("1959");
        correct.add("2059");
        correct.add("2359");
        for (String s : correct) {
            Matcher matcher = pattern2.matcher(s);
            Assertions.assertThat(matcher.matches()).isTrue();
        }
    }
}