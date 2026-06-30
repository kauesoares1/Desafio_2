package model;

// administrador do sistema, o unico que pode cadastrar selecao, jogador, jogo e participante
// nao tem atributo a mais, só serve pra diferenciar do Participante
// (uso o instanceof la no SistemaBolao pra checar permissao)
public class Administrador extends Usuario {

    public Administrador(String nome, String login, String senha) {
        super(nome, login, senha);
    }

    @Override
    public String getTipo() {
        return "Administrador";
    }
}