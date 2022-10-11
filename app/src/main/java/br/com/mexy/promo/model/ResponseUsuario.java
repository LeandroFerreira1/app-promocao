package br.com.mexy.promo.model;

public class ResponseUsuario {

    private Integer status_code;
    private String token_type;
    private String access_token;

    public Integer getStatusCode() {
        return status_code;
    }

    public void setStatusCode(Integer status_code) {
        this.status_code = status_code;
    }

    public String getTokenType() {
        return token_type;
    }

    public void setTokenType(String token_type) {
        this.token_type = token_type;
    }

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }
}
