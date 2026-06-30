package interfaces;

import model.Palpite;

// interface so com o metodo de calcular pontos
// quem implementa é a CalculadoraPontuacao
public interface Pontuavel {
    int calcular(Palpite p);
}