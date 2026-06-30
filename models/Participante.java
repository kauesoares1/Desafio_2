package model;

// participante do bolao, quem da os palpites e ganha pontos
public class Participante extends Usuario {

    private int pontuacao; // total de pontos acumulados em todos os jogos

    public Participante(String nome, String login, String senha) {
        super(nome, login, senha);
        this.pontuacao = 0;
    }

    @Override
    public String getTipo() {
        return "Participante";
    }

    public int getPontuacao() {
        return pontuacao;
    }

    // soma pontos depois que a CalculadoraPontuacao calcula o palpite
    public void adicionarPontos(int pontos) {
        this.pontuacao += pontos;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }
}