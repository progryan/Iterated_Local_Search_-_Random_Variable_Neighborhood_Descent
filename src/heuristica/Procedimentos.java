package heuristica;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class Procedimentos {
	private static final boolean verbose = false;
	public static ArrayList<No> CONSTRUCAOSOLUCAOINICIAL(Grafo g) {
		int rota = 0;
		ArrayList<No> solucao = new ArrayList<No>();
		ArrayList<No> grafo = copiarConteudoArray(g.getNodes());
		solucao.add(grafo.get(0)); //add deposito
		No primeiroNo = grafo.get(grafo.get(0).getProximo().get(0));
		solucao.add(primeiroNo); //add o primeiro n� na solu��o
		primeiroNo.setRota(rota); //definindo a rota do primeiro n� com zendo ZERO
		//solucao.get(0).AddRotaDepositoPara(primeiroNo.getID()); //add um arco do deposito para o primeiro n�
		No current = primeiroNo;
		int Q = 150; //carga do caminhao
		int taxa = 100; //total de produtos que ser� separado pra entrega
		int entrega = taxa, coleta = Q - taxa;
		entrega -= primeiroNo.getEntrega();
		coleta +=  primeiroNo.getEntrega() - primeiroNo.getColeta();
		//System.out.println(entrega + " | " + coleta);
		while(solucao.size() != grafo.size()) { //enquanto a solucao n�o contem todos os n�s
			double d = 99999.0;
			for(int i = 1; i < grafo.size(); i++) { //para cada n� presente no conjunto de n�s
				if(!solucao.contains(grafo.get(i))) { //se o n� n�o pertence a solucao
					No k = grafo.get(i);
					double custoInsercao = custoInsercao(solucao, k, rota);//distancia = g.getNodes().get(i).distanciaAte(solucao.get(solucao.size() - 1));
					//System.out.println("Ultimo n�: " + solucao.get(solucao.size() - 1).getID() + " | K = " + k.getID() + "/" + custoInsercao + "||gama: " + gama(solucao.get(0), k));
					if(custoInsercao < d) {
						d = custoInsercao;
						current = k;
					}
				}
			}
			
			entrega -= current.getEntrega();
			coleta +=  current.getEntrega() - current.getColeta();
			//System.out.println(entrega + " | " + coleta);
			if(entrega > 0 && coleta > 0) { //se a restri��o de coleta e entrega n�o foram violadas
				solucao.get(solucao.size() - 1).setProximoNodes(current.getID()); //defino que o n� atual � o pr�ximo do �ltimo n� na solu��o
				//System.out.println("rota adicionada de " + (solucao.get(solucao.size() - 1).getID()) + " para " + current.getID());
				//solucao.get(solucao.size() - 1).AddRotaPara(current.getID()); //add um arco para esse n�
				current.setRota(rota); //defino a rota dele como sendo a rota atual
				
				solucao.add(current); //add o n� na solu��o
			}
			else //se ultrapassou o limite do caminh�o
			{
				solucao.get(solucao.size() - 1).setProximoNodes(0); //defino que o �ltimo n� da solu��o deve voltar para o dep�sito
				//System.out.println("rota adicionada de " + solucao.get(solucao.size() - 1).getID() + " para " + solucao.get(0).getID());
				//solucao.get(solucao.size() - 1).AddRotaPara(solucao.get(0).getID()); //add um arco para o dep�sito
				double d2 = 99999.0;
				for(int i = 1; i < grafo.size(); i++) { //procuro o melhor n� para criar uma nova rota a partir do dep�sito
					if(!solucao.contains(grafo.get(i))) {
						No k = grafo.get(i);
						double custoInsercao = grafo.get(i).distanciaAte(grafo.get(0));
						//System.out.println("Ultimo n�: " + solucao.get(solucao.size() - 1).getID() + " | K = " + k.getID() + "/" + custoInsercao + "||gama: " + gama(solucao.get(0), k));
						if(custoInsercao < d2) {
							d2 = custoInsercao;
							current = k;
						}
					}
				}
				solucao.get(0).setProximoDeposito(current.getID());
				//System.out.println("rota adicionada de " + solucao.get(0).getID() + " para " + current.getID());
				//solucao.get(0).AddRotaDepositoPara(current.getID()); //add um arco do dep�sito para esse novo melhor n�
				solucao.add(current); //add esse n� na solu��o
				rota++; //incremento a rota
				current.setRota(rota); //defino a nova rota para esse novo n�
				entrega = taxa; //reseto os atributos
				coleta = Q - taxa; //
				entrega -= current.getEntrega(); //
				coleta +=  current.getEntrega() - current.getColeta(); //
				//System.out.println(entrega + " | " + coleta);
			}
		}
		//System.out.println("rota adicionada de " + current.getID() + " para " + solucao.get(0).getID());
		//current.AddRotaPara(solucao.get(0).getID()); //defino que o �ltimo n� na solu��o deve voltar para o dep�sito
		//System.out.println(rota);
		atualizarRotas(solucao);
		return solucao;
	}
	public static ArrayList<No> BUSCALOCALRVND(ArrayList<No> solucao){
		Random rd = new Random();
		
		ArrayList<No> SolucaoAtual = solucao;
		ArrayList<No> Slinha = null;
		ArrayList<Integer> sorteados = new ArrayList<Integer>();
		
		int k = rd.nextInt(5) + 1;
		int contadorK = 1;
		
		while(contadorK <= 5) {
			switch(k) {
				case 1:
					if(verbose)
						System.out.println("**************SHIFT**************");
					Slinha = Movimentos.Shift(SolucaoAtual);
					if(!sorteados.contains(1))
						sorteados.add(1);
					break;
				case 2:
					if(verbose)
						System.out.println("*************SHIFT20*************");
					Slinha = Movimentos.Shift20(SolucaoAtual);
					if(!sorteados.contains(2))
						sorteados.add(2);
					//System.out.println(2);
					break;
				case 3:
					if(verbose)
						System.out.println("**************SWAP**************");
					Slinha = Movimentos.Swap(SolucaoAtual);
					if(!sorteados.contains(3))
						sorteados.add(3);
					//System.out.println(3);
					break;
				case 4:
					if(verbose)
						System.out.println("*************SWAP21*************");
					Slinha = Movimentos.Swap21(SolucaoAtual);
					if(!sorteados.contains(4))
						sorteados.add(4);
					//System.out.println(4);
					break;
				case 5:
					if(verbose)
						System.out.println("*************SWAP22*************");
					Slinha = Movimentos.Swap22(SolucaoAtual);
					if(!sorteados.contains(5))
						sorteados.add(5);
					//System.out.println(5);
					break;
			}
			if(avaliar(Slinha) < avaliar(SolucaoAtual)) {
				SolucaoAtual = Slinha;
			}
			else
			{
				if(sorteados.size() < 5) {
					do {
						k = rd.nextInt(5) + 1;
					}while(sorteados.contains(k));
					//System.out.println("K: " + k);
				}
				contadorK++;
			}
		}
		return SolucaoAtual;
	}
	
	public static ArrayList<No> PERTURBACAO(ArrayList<No> solucao){
		Random rd = new Random();
		ArrayList<No> SolucaoPerturbada = solucao;
		int k = rd.nextInt(3) + 1;
		
		switch(k) {
			case 1: //Multiplos Shifts
				if(verbose)
					System.out.println("---------Multiplos Shifts---------");
				int v = rd.nextInt(3) + 1;
				for(int i = 0; i < v; i++) {
					/*System.out.println("\nsolucaoShift20\n");
					for(No n : SolucaoPerturbada) {
						if(n.getID() != 0) 
							System.out.printf("{k=%d|p=%d|r=%d}\n", n.getID(), n.getProximoNo(), n.getRota());
						else
							System.out.printf("{k=%d|p=%s}\n", n.getID(), n.getVetorProximos());
					}*/
					SolucaoPerturbada = Movimentos.Shift(SolucaoPerturbada);
				}
				break;
			case 2: //Multiplos Swaps
				if(verbose)
					System.out.println("---------Multiplos Swaps---------");
				int n = rd.nextInt(3) + 1;
				for(int i = 0; i < n; i++) {
					SolucaoPerturbada = Movimentos.Swap(SolucaoPerturbada);
				}
				break;
			case 3: //Ejection Chain
				if(verbose)
					System.out.println("---------Ejection Chain---------");
				ArrayList<Integer> rotas = new ArrayList<Integer>();
				for(No n1 : solucao) {
					if(!rotas.contains(n1.getRota()) && n1.getID() != 0)
						rotas.add(n1.getRota());
				}
				ordenar(rotas);
				for(int r : rotas) {
					Movimentos.Shift(SolucaoPerturbada, r, ProximaRotaValida(SolucaoPerturbada, r));
				}
				
				break;
		}
		return SolucaoPerturbada;
	}
	public static int avaliar(ArrayList<No> solucao) { //fun��o de avalia��o para a solu��o
		int custo = 0;
		for(int i = 0; i < solucao.size(); i++) {
			for (int j = 0; j < solucao.size(); j++) {
				custo += solucao.get(i).distanciaAte(solucao.get(j)) * solucao.get(i).rotaPara(solucao.get(j));
			}
		}
		return custo;
	}
	
	public static int custoInsercao(ArrayList<No> solucao, No k, int rota) { //fun��o de custo para a insercao de um n�
		solucao.get(0).setRota(rota);
		int custo = 0;
		for(int i = 1; i < solucao.size(); i++) {
			for (int j = 1; j < solucao.size(); j++) {
				if(solucao.get(i).getRota() == rota && solucao.get(j).getRota() == rota) { //comparo o custo relativo aos n�s pertencentes a rota do n� que estou avaliando
				custo += (solucao.get(i).distanciaAte(k) + k.distanciaAte(solucao.get(j)) - solucao.get(i).distanciaAte(solucao.get(j))) 
						- gama(solucao.get(0), k)*(solucao.get(0).distanciaAte(k) + k.distanciaAte(solucao.get(0)));
				}
			}
		}
		return custo;
	}
	
	public static double gama(No deposito, No k) { //vari�vel de penaliza��o de dist�ncia do dep�sito, (0 <= gama <= 1) 
		double gama = 0.0;
		gama = k.distanciaAte(deposito)/10.0 > 1 ? 1 : (k.distanciaAte(deposito)/10.0 < 0.1 ? 0 : (k.distanciaAte(deposito)/10.0));
		return gama;
	}
	
	public static void atualizarRotas(ArrayList<No> solucao){
		for (No no : solucao) {
			no.cleanRotas();
		}
		
		for (int i : solucao.get(0).getProximo()) {
			solucao.get(0).AddRotaPara(i);
		}
		
		for(int i = 1; i < solucao.size(); i++) {
			solucao.get(i).AddRotaPara(solucao.get(i).getProximoNo());
		}
	}
	
	public static No getNo(ArrayList<No> solucao, int id, int proximo) {
		if(id != -1) {
			for (No no : solucao) {
				if(no.getID() == id)
					return no;
			}
		}
		for (No no : solucao) {
			if(no.getProximoNo() == proximo)
				return no;
		}
		return null;
	}
	public static boolean ultrapassaLimite(ArrayList<No> solucao) {
		int Q = 150; //carga do caminhao
		int taxa = 100; //total de produtos que ser� separado pra entrega
		int entrega = taxa, coleta = Q - taxa; //entrega = 100 | coleta = 50
		
		for(int i = 0; i < solucao.get(0).getProximo().size(); i++) {
			for(No no : solucao) {
				if(no.getRota() == i) {
					entrega -= no.getEntrega();
					coleta +=  no.getEntrega() - no.getColeta();
					if(entrega <= 0 || coleta <= 0) {
						return true;
					}
				}
			}
			entrega = taxa; coleta = Q - taxa;
		}
		return false;
	}
	public static ArrayList<No> copiarConteudoArray(ArrayList<No> array){
		//System.out.println("array: " + array.get(0).getProximo().size());
		ArrayList<No> novo = new ArrayList<No>();
		for(No current : array) {
			No n = new No();
			if(current.getID() == 0) {
				n.setVetorProximosClass(current.getProximo());
			}
			n.setRota(current.getRota());
			n.setID(current.getID());
			n.setColeta(current.getColeta());
			n.setEntrega(current.getEntrega());
			n.setProximoNodes(current.getProximoNo());
			n.setVetorDistanciasClass(current.getVetorDistanciasClass());
			novo.add(n);
		}
		//System.out.println("copy: " + novo.get(0).getProximo().size());
		return novo;
	}
	public static int ProximaRotaValida(ArrayList<No> solucao, int rota) {
		ArrayList<Integer> rotas = new ArrayList<Integer>();
		for(No n : solucao) {
			if(!rotas.contains(n.getRota()) && n.getID() != 0)
				rotas.add(n.getRota());
		}
		ordenar(rotas);
		int indexRotaAtual = rotas.get(rotas.indexOf(rota));
		//if(rota == 2) {
		//	System.out.println("index: " + indexRotaAtual + " array: {");
		//	for(Integer n : rotas) {
		//		System.out.printf("%d", n);
		//	}
		//System.out.printf("}\n");
		//}
		return indexRotaAtual + 1 < rotas.size() ? rotas.get(indexRotaAtual + 1) : rotas.get(0);
	}
	public static void ordenar(ArrayList<Integer> array) {
		Collections.sort (array, (o1, o2) -> {
		    Integer p1 = (Integer) o1;
		    Integer p2 = (Integer) o2;
		    return p1 < p2 ? -1 : (p1 > p2 ? +1 : 0);
		});
	}
	
	public static void verifica(ArrayList<No> sol) {
		for(No n : sol) {
			if(n.getID() != 0 && n.getProximoNo() != 0) {
				if(getNo(sol, n.getProximoNo(), -1).getRota() != n.getRota()) {
					System.out.println("ID: " + getNo(sol, n.getProximoNo(), -1).getID() + " ROTA: " + getNo(sol, n.getProximoNo(), -1).getRota());
					System.out.println("ID: " + n.getID() + " ROTA: " + n.getRota());
					System.out.println("ERRO!");
					System.exit(-1);
				}
			}
		}
	}
}