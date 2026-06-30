package model;

// classe pai de Administrador e Participante (herança)
// aqui fica tudo que os dois tipos de usuario tem em comum
public abstract class Usuario {

    private String nome;
    private String login;
    private String senha;

    public Usuario(String nome, String login, String senha) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    public String getNome() { return nome; }
    public String getLogin() { return login; }
    public String getSenha() { return senha; }

    // confere se o login/senha digitado bate com o do usuario
    // usado no login do sistema
    public boolean autenticar(String login, String senha) {
        return this.login.equals(login) && this.senha.equals(senha);
    }

    // metodo abstrato -> cada subclasse (Administrador / Participante) decide
    // o que retorna aqui.(polimorfismo)
    public abstract String getTipo();

    @Override
    public String toString() {
        return getTipo() + ": " + nome;
    }
}