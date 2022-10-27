package br.com.mexy.promo.util;

import java.io.IOException;

import br.com.mexy.promo.model.Estabelecimento;

public interface CustomInterface {
    public void callbackMethod(Estabelecimento estabelecimento);
    public void callbackMethodPhoto(String photo) throws IOException;
}
