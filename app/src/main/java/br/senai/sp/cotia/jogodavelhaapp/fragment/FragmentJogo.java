package br.senai.sp.cotia.jogodavelhaapp.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

import br.senai.sp.cotia.jogodavelhaapp.R;
import br.senai.sp.cotia.jogodavelhaapp.databinding.FragmentJogoBinding;
import br.senai.sp.cotia.jogodavelhaapp.util.PrefsUtil;

public class FragmentJogo extends Fragment {
    //vaiavel para acessar os elementos na view
    private FragmentJogoBinding binding;
    //vetor para agrupar os botões
    private Button[] botoes;
    //variavel que representa o tabuleiro
    private String[][] tabuleiro;
    //variavel para os simbolos
    private String simbjog1, simbjog2, simbolo;
    //variavel random para sortear quem começa
    private Random random;
    //variavel para contar o numero de jogadas
    private int numJogadas = 0;
    //variaveis para o placar
    private int placarJog1=0, placarJog2=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        binding = FragmentJogoBinding.inflate(inflater, container, false);

        //instancia o vetor
        botoes = new Button[9];
        //agrupa os botões no vetor
        botoes[0] = binding.bt00;
        botoes[1] = binding.bt01;
        botoes[2] = binding.bt02;
        botoes[3] = binding.bt10;
        botoes[4] = binding.bt11;
        botoes[5] = binding.bt12;
        botoes[6] = binding.bt20;
        botoes[7] = binding.bt21;
        botoes[8] = binding.bt22;

        //associa o listener aos botões
        for (Button bt : botoes){
            bt.setOnClickListener(listenerBotoes);
        }
        //instancia o tabuleiro
        tabuleiro = new String[3][3];

        //Preencher o tabuleiro com ""
        for(String[] vetor : tabuleiro){
            Arrays.fill(vetor, "");
        }
        //instancia o random
        random = new Random();
        //define os simbolos dos jogadores
        simbjog1 = PrefsUtil.getSimboloJog1(getContext());
        simbjog2 = PrefsUtil.getSimboloJog2(getContext());

        //altera o simbolo do jogador no placar
        binding.tvPlacar1.setText(getResources().getString(R.string.jogador1, simbjog1));
        binding.tvPlacar2.setText(getResources().getString(R.string.jogador2, simbjog2));
        //sorteia quem inicia po jogo
        sorteia();



        //Retorna a variavel Fragment
        return binding.getRoot();
    }
    private void sorteia(){
        //caso o random gere um valor verdadeiro o jogador 1 começa
        //caso contrario o jogador 2 começa
        if(random.nextBoolean()){
            simbolo = simbjog1;
        }
        else{
            simbolo = simbjog2;
        }
    }

    private void atualizaVez(){
        //verifica de quem é a vez e verifica o placar do jogador em questão
        if(simbolo.equals(simbjog1)){
            binding.lineJogador1.setBackgroundColor(getResources().getColor(R.color.blue_claro));
            binding.lineJogador2.setBackgroundColor(getResources().getColor(R.color.white));
        }else{
            binding.lineJogador1.setBackgroundColor(getResources().getColor(R.color.blue_claro));
            binding.lineJogador2.setBackgroundColor(getResources().getColor(R.color.white));
        }

    }

    private boolean venceu() {
        //verifica se venceu nas linhas
        for (int i = 0; i < 3; i++) {
            if (tabuleiro[i][0].equals(simbolo) &&
                    tabuleiro[i][1].equals(simbolo)
                    && tabuleiro[i][2].equals(simbolo)) {
                return true;

            }
        }
        //verifica se venceu na coluna
        for (int i = 0; i < 3; i++) {
            if (tabuleiro[0][i].equals(simbolo) &&
                    tabuleiro[1][i].equals(simbolo)
                    && tabuleiro[2][i].equals(simbolo)) {
                return true;

            }

        }
        //verifica se venceu nas diagonais
        if (tabuleiro[0][0].equals(simbolo) &&
                tabuleiro[1][1].equals(simbolo)
                && tabuleiro[2][2].equals(simbolo)) {
            return true;

        }
        if (tabuleiro[0][2].equals(simbolo) &&
                tabuleiro[1][1].equals(simbolo)
                && tabuleiro[2][0].equals(simbolo)) {
            return true;
        }
        return false;
    }

    private void retartJogo(){
        //zerar o tabuleiro
        for(String[] vetor : tabuleiro){
            Arrays.fill(vetor, "");

       }
        //Percorre o vetor de botões resetando-os
        for(Button botao : botoes){
            botao.setBackgroundColor(getResources().getColor(R.color.blue));
            botao.setText("");
            botao.setClickable(true);

            //zerar o numero de jogadas
            numJogadas = 0;
        }
    }

    private  void atualizaPlacar(){
        binding.placarJod1.setText(placarJog1+"");
        binding.placarJod2.setText(placarJog2+"");

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // verifica qual potão foi clacado no menu
        switch (item.getItemId()){
            //caso tenha clicado no resetar
            case R.id.menu_resetar:
                placarJog1 = 0;
                placarJog2 = 0;
                retartJogo();
                atualizaPlacar();
                break;
            //caso tenha clicadono preferencias
            case R.id.menu_prefs:
                NavHostFragment.findNavController(FragmentJogo.this).
                        navigate(R.id.action_fragmentJogo_to_fragmentPreferencias);
                break;
        }

        return true;
    }

    private View.OnClickListener listenerBotoes = btPress -> {
        //incremenata o numero de jogadas
        numJogadas++;
        //pega o nome do  botão
        String nomeBotao = getContext().getResources().getResourceName(btPress.getId());
        //extrai os 2 últimos caracteres do nomeBotao
        String posicao = nomeBotao.substring(nomeBotao.length() - 2);
        //extrai a posição linha e coluna
        int linha = Character.getNumericValue(posicao.charAt(0));
        int coluna = Character.getNumericValue(posicao.charAt(1));

        //marca no tabuleiro o simbolo que foi jogado
        tabuleiro[linha][coluna] = simbolo;
        //trocar o texto do botão que foi clicado
        Button botao = (Button) btPress;
        //troca o texto dos botoes que foi clicado
        botao.setText(simbolo);
        //desabilitar o botão
        botao.setClickable(false);
        //troca o backgroud do botão
        botao.setBackgroundColor(Color.WHITE);

        //verifica se venceu
        if (numJogadas >= 5 && venceu()) {
            //exibe um toast informando que o jogador venceu
            Toast.makeText(getContext(), R.string.venceu, Toast.LENGTH_LONG).show();
            if(simbolo.equals(simbjog1)){
                placarJog1++;

            }else{
                placarJog2++;
            }
            //atualiza o jogo
            atualizaPlacar();
            //rezetar o jogo
            retartJogo();
        } else if(numJogadas == 9){
            //exibe um toast informando que o jogador venceu
            Toast.makeText(getContext(), R.string.velha, Toast.LENGTH_LONG).show();
            //resetar tabuleiro
            retartJogo();
        }else{
            //inverter a vez, troca o simbolo sempre
            simbolo = simbolo.equals(simbjog1) ? simbjog2 : simbjog1;

            //atualiza a vez
            atualizaVez();
        }

    };
}

