package com.example.eunboard.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.eunboard.domain.dto.request.MemberRequestDTO;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KakaoAPI {

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String getAccessToken(String code) {
        String access_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            /** TODO: 카카오 키 하드코딩, 환경파일로 관리가 필요함 */
            // 파라미터 세팅
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("grant_type", "authorization_code");
            map.add("client_id", "235fc02960c0239e43b70d9e3fd2c9e6");
            map.add("client_secret", "QQP9e4kuegEA1ZQLUSDFINBknLcDoL8R");
            map.add("code", code);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,
                    headers);

            ResponseEntity<String> res = new RestTemplate().postForEntity(reqURL, request,
                    String.class);

            // Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            if (res.getStatusCodeValue() != 200) {
                JsonElement element = JsonParser.parseString(res.getBody());
                throw new RuntimeException("Kakao login Fail. " + element.getAsJsonObject().get("error").getAsString());
            }
            JsonElement element = JsonParser.parseString(res.getBody());

            access_Token = element.getAsJsonObject().get("access_token").getAsString();

        } catch (Exception e) {
            log.warn("Kakao login Fail. {}", e.getMessage());
            throw new RuntimeException("Kakao login Fail.");
        }

        return access_Token;
    }

    public MemberRequestDTO getUserInfo(String accessToken) {

        MemberRequestDTO memberDTO = new MemberRequestDTO();

        long kakaoId = 0;
        String kakaoNickName = "";
        String kakaoEmail = "";

        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBearerAuth(accessToken);

            // 파라미터 세팅
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,
                    headers);

            ResponseEntity<String> res = new RestTemplate().postForEntity(reqURL, request,
                    String.class);

            // Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            if (res.getStatusCodeValue() != 200) {
                JsonElement element = JsonParser.parseString(res.getBody());
                throw new RuntimeException(
                        "Kakao get account Fail. " + element.getAsJsonObject().get("error").getAsString());
            }
            JsonElement element = JsonParser.parseString(res.getBody());

            kakaoId = element.getAsJsonObject().get("id").getAsLong();
            kakaoNickName = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
            if (!element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean()) {
                throw new RuntimeException("Kakao get email Fail.");
            }
            kakaoEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();

            memberDTO.setEmail(kakaoEmail);
            memberDTO.setMemberName(kakaoNickName);
            memberDTO.setPassword(passwordEncoder.encode(Long.toString(kakaoId)));
            memberDTO.setMember(false);
        } catch (Exception e) {
            log.warn("Kakao get account Fail. {}", e.getMessage());
            throw new RuntimeException("Kakao get account Fail.");
        }

        return memberDTO;
    }

    public void kakaoLogout(String accessToken) {
        String reqURL = "http://kapi.kakao.com/v1/user/logout";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}