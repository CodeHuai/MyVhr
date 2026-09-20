package com.myhr.config;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        // 只允许 post 请求方式进行登录
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        // 从 session 获取对应验证码数据
        String verifyCode = (String) request.getSession().getAttribute("verify_code");
        if (request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE) || request.getContentType().contains(MediaType.APPLICATION_JSON_UTF8_VALUE)) {
            HashMap<String, String> loginData = new HashMap<>();

            try {
                loginData = new ObjectMapper().readValue(request.getInputStream(), loginData.getClass());
            } catch (DatabindException e) {
                throw new RuntimeException(e);
            } catch (StreamReadException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                String code = loginData.get("code");
                // 检查验证码是否正确
                this.checkCode(code, verifyCode);
                String username = loginData.get("username");
                String password = loginData.get("password");

                if (username == null || "".equals(username)) {
                    username = "";
                }
                if (password == null || "".equals(password)) {
                    password = "";
                }
                username = username.trim();

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
                setDetails(request, authenticationToken);
                return this.getAuthenticationManager().authenticate(authenticationToken);
            }
        } else {
            checkCode(request.getParameter("code"), verifyCode);
            return super.attemptAuthentication(request, response);
        }
    }

    public void checkCode(String code, String verifyCode) {
        if (code == null || "".equals(code) || verifyCode == null || "".equals(verifyCode) || !code.toLowerCase().equals(verifyCode.toLowerCase())) {
            //验证码不正确
            throw new AuthenticationServiceException("无效验证码！");
        }
    }
}
