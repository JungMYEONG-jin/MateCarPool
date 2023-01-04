package com.example.eunboard.regex;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class RegexTest {

    @Test
    @DisplayName(value = "날짜에 대한 문자열 테스트 코드")
    public void startDayMonthRegexTest() {
        String regex = "(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])";

        assertThat("0900".matches(regex)).isEqualTo(false);
        assertThat("0724".matches(regex)).isEqualTo(true);
        assertThat("2400".matches(regex)).isEqualTo(false);
        assertThat("2359".matches(regex)).isEqualTo(false);
    }

    @Test
    @DisplayName(value = "시간에 대한 문자열 테스트 코드")
    public void startStartTimeRegexTest() {
        String regex = "([0-1][0-9]|2[0-3])[0-5][0-9]";

        assertThat("0900".matches(regex)).isEqualTo(true);
        assertThat("0724".matches(regex)).isEqualTo(true);
        assertThat("2400".matches(regex)).isEqualTo(false);
        assertThat("2359".matches(regex)).isEqualTo(true);
    }
}
