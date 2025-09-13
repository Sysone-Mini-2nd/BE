package com.sys.stm.domains.meeting.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sys.stm.global.exception.BadRequestException;
import com.sys.stm.global.exception.ExceptionMessage;
import com.sys.stm.global.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class NaverApiService {

    // clientId
    @Value("${cloud.naver.clova.client}")
    private String clientId;

    // clientSecret
    @Value("${cloud.naver.clova.secret}")
    private String clientSecret;


    public String processAudio(MultipartFile audioFile) {

        // 파일이 비어있는지 확인
        if (audioFile.isEmpty()) {
            throw new NotFoundException(ExceptionMessage.VOICE_NOT_FOUND);
        }

        try {
//            File voiceFile = audioFile.getResource().getFile();

            File voiceFile = File.createTempFile("audio_", ".wav");
            audioFile.transferTo(voiceFile);

            String language = "Kor";        // 언어 코드 ( Kor, Jpn, Eng, Chn )
            String apiURL = "https://naveropenapi.apigw.ntruss.com/recog/v1/stt?lang=" + language;
            URL url = new URL(apiURL);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

            OutputStream outputStream = conn.getOutputStream();
            FileInputStream inputStream = new FileInputStream(voiceFile);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();
            BufferedReader br = null;
            int responseCode = conn.getResponseCode();
            if(responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {  // 오류 발생
                System.out.println("데이터 반환값 오류 error!!!!!!! responseCode= " + responseCode);
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();

            if(br != null) {
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
            } else {
                System.out.println("반환된 데이터 출력 과정 중 오류 발생!!!");
            }


            return extractTextFromJson(response.toString());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BadRequestException(ExceptionMessage.API_COMMUNICATION_ERROR);
        }
    }


    // JSON에서 텍스트 추출 메서드 추가
    private String extractTextFromJson(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonResponse);

            // "text" 필드에서 값 추출
            if (jsonNode.has("text")) {
                return jsonNode.get("text").asText();
            }

            // "text" 필드가 없으면 전체 응답 반환
            return jsonResponse;

        } catch (Exception e) {
            // JSON 파싱 실패 시 원본 반환
            return jsonResponse;
        }
    }
}
