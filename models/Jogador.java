package model;

// jogador de futebol, pertence a uma Selecao
public class Jogador {

    private String nome;
    private Posicao posicao;

    public Jogador(String nome, Posicao posicao) {
        this.nome = nome;
        this.posicao = posicao;
    }

    public String getNome() { return nome; }
    public Posicao getPosicao() { return posicao; }

    @Override
    public String toString() {
        return nome + " (" + posicao + ")";
    }

    // dois jogadores sao "iguais" se tiver o mesmo nome
    // precisei sobrescrever isso pra dar pra comparar o jogador que o
    // participante chutou com o jogador que realmente fez o gol
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Jogador outro = (Jogador) obj;
        return nome.equalsIgnoreCase(outro.nome);
    }

    @Override
    public int hashCode() {
        return nome.toLowerCase().hashCode();
    }
}