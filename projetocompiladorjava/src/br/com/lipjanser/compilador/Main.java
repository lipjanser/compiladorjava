package br.com.lipjanser.compilador;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author lipjanser
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedReader arquivo = new BufferedReader(new FileReader(args[0]));
        Compilador comp = new Compilador();

        comp.simboloInicial(arquivo);

        arquivo.close();
        System.out.println("Encerrado com sucesso.");
        System.exit(0);
    }
}