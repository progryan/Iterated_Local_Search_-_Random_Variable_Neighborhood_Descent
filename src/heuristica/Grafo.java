package heuristica;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public final class Grafo {
	private ArrayList<No> nodes = new ArrayList<No>();
	private BufferedReader lerArq;
	private Random rd = new Random();
	
	public Grafo(String caminho, String entrada, String coordenadas) {
		try {
			FileReader arq = new FileReader(caminho + entrada + ".txt");
			
			lerArq = new BufferedReader(arq);
			
			String line = lerArq.readLine();
			int contador = 0;
			ArrayList<Integer> firstNodeProxs = new ArrayList<Integer>();
			firstNodeProxs.add(rd.nextInt(Main.instances - 1) + 1);
			nodes.add(new No(line + " " + vetorDistancias(contador, caminho, coordenadas), firstNodeProxs, -1));
			line = lerArq.readLine();
			while(line != null) {
				contador++;
				nodes.add(new No(line + " " + vetorDistancias(contador, caminho, coordenadas), null, 0));
				line = lerArq.readLine();
			}
		}
		catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n",
			e.getMessage());
		}
	}
	public String vetorDistancias(int contador, String caminho, String coordenadas) throws IOException {
		ArrayList<String> aux = new ArrayList<String>();
		FileReader arq = new FileReader(caminho + coordenadas + ".txt");
		BufferedReader lerArq = new BufferedReader(arq);
		String line = lerArq.readLine();
		String retorno = "";
		String XY = "";
		while(line != null) {
			aux.add(line.split(" ")[1]);
			if(line.split(" ")[0].equals(contador + "")) {
				XY = line.split(" ")[1];
			}
			line = lerArq.readLine();
		}
		for(int i = 0; i < Main.instances; i++) {
			double X2 = Double.parseDouble(aux.get(i).split(",")[0]);
			double Y2 = Double.parseDouble(aux.get(i).split(",")[1]);
			retorno += calcularDistancia(Double.parseDouble(XY.split(",")[0]), Double.parseDouble(XY.split(",")[1]), X2, Y2) + ",";
		}
		lerArq.close();
		return retorno.substring(0, retorno.length() - 1);
	}
	
	public double calcularDistancia(double X1, double Y1, double X2, double Y2) {
		return Math.sqrt(((X2 - X1)*(X2 - X1)) + ((Y2 - Y1)*(Y2 - Y1)));
	}
	public ArrayList<No> getNodes(){
		return nodes;
	}
}