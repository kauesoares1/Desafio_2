package service;

import interfaces.Pontuavel;
import model.*;

// calcula a pontuacao de um palpite seguindo as regras do enunciado
// implementa a interface Pontuavel
public class CalculadoraPontuacao implements Pontuavel {

    @Override
    public int calcular(Palpite p) {

        int pontos = 0;

        int rc = p.getJogo().getGolsCasa(); // placar real
        int rv = p.getJogo().getGolsVisitante();

        int pc = p.getGolsCasa(); // placar que o participante chutou
        int pv = p.getGolsVisitante();

        // acertou quem ganhou (ou que ia empatar)
        if ((rc > rv && pc > pv) ||
            (rc < rv && pc < pv) ||
            (rc == rv && pc == pv)) {
            pontos += 5;
        }

        // acertou o numero de gols de cada time separado
        if (rc == pc) pontos += 3;
        if (rv == pv) pontos += 3;

        // acertou o placar inteiro, ganha bonus
        if (rc == pc && rv == pv) pontos += 10;

        // confere os jogadores que o participante chutou que iam fazer gol
        // compara com quem realmente fez gol no jogo (usa o equals do Jogador)
        // quanto mais dificil a posicao fazer gol, mais pontos vale
        for (Gol previsto : p.getGolsPrevistos()) {
            for (Gol real : p.getJogo().getGols()) {

                if (previsto.getJogador().equals(real.getJogador())) {

                    switch (previsto.getJogador().getPosicao()) {
                        case GOLEIRO: pontos += 10; break;
                        case ZAGUEIRO: pontos += 8; break;
                        case LATERAL: pontos += 6; break;
                        case MEIO_CAMPO: pontos += 4; break;
                        case ATACANTE: pontos += 2; break;
                    }
                }
            }
        }

        p.setPontos(pontos); // guarda no proprio palpite pra poder consultar depois
        return pontos;
    }
}