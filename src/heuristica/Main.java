package heuristica;

import java.util.ArrayList;

public final class Main {
	public static final int instances = 100;
	public static void main(String[] args) {
		Grafo g = new Grafo(".\\", "entrada5", "coord5");
		int iterMax = 1000;
		long tempoInicial = System.currentTimeMillis();
		IteratedLocalSearch(g, iterMax);
		long tempoFinal = System.currentTimeMillis();
		System.out.printf("\n---------------Tempo total: %.3f segundos---------------", (tempoFinal - tempoInicial) / 1000.0);
		System.out.println("\n\t\t\t100% SUCESSO!\t\t\t");
	}
	public static void IteratedLocalSearch(Grafo g, int iterMax) {
		System.out.println("\n---------------SOLUCAO INICIAL---------------");
		ArrayList<No> solucao = Procedimentos.CONSTRUCAOSOLUCAOINICIAL(g);
		for(No n : solucao) {
			if(n.getID() != 0) 
				System.out.printf("{k=%d|p=%d|r=%d}\n", n.getID(), n.getProximoNo(), n.getRota());
			else
				System.out.printf("\n\n{k=%d|p=%s}\n", n.getID(), n.getVetorProximos());
		}
		String custoSolInicial = ("\n---------------Custo da solucao inicial: " + Procedimentos.avaliar(solucao) + "---------------");
		System.out.println("\nExecutando...");
		solucao = Procedimentos.BUSCALOCALRVND(solucao);
		String custoSolBL = ("\n---------------Custo apos primeira busca local RVND: " + Procedimentos.avaliar(solucao) + "---------------");
		int iter = 1;
		ArrayList<No> solucaoLinha;
		while(iter < iterMax) {
			iter++;
			solucaoLinha = Procedimentos.PERTURBACAO(solucao);
			solucaoLinha = Procedimentos.BUSCALOCALRVND(solucaoLinha);
			if(Procedimentos.avaliar(solucaoLinha) < Procedimentos.avaliar(solucao)) {
				solucao = solucaoLinha;
				iter = 0;
			}
		}
		System.out.println(custoSolInicial);
		System.out.println(custoSolBL);
		System.out.println("\n---------------Custo apos o ILS completo " + Procedimentos.avaliar(solucao) + "---------------");
	}
}