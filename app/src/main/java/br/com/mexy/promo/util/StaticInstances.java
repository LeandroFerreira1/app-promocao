package br.com.mexy.promo.util;

import java.util.ArrayList;
import java.util.List;

import br.com.mexy.promo.model.Departamento;
import br.com.mexy.promo.model.Estabelecimento;
import br.com.mexy.promo.model.Promocao;
import br.com.mexy.promo.model.Usuario;
import br.com.mexy.promo.model.UsuarioConquista;

public class StaticInstances {

    public static List<Estabelecimento> estabelecimentos = new ArrayList<>();
    public static List<Departamento> departamentos = new ArrayList<>();
    public static Integer idUsuario;
    public static Usuario usuario =  new Usuario();
    public static List<UsuarioConquista> usuarioConquistas = new ArrayList<>();

}
