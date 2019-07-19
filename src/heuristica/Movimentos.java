package heuristica;

import java.util.ArrayList;
import java.util.Random;

public final class Movimentos {
	private static Random clienteAleatorio = new Random();
	private static final int nIt = Main.instances;
	private static final boolean verbose = false;
	public static ArrayList<No> Shift(ArrayList<No> solucao){
		ArrayList<No> solucaoShift;
		ArrayList<String> Par_visitados = new ArrayList<String>();
		No k1, k2;
		int shiftK1paraRota;
		int iteracoes = 0;
		//System.out.println("Solucao.get(0).getProximo().size(shift - ANTES) -> " + solucao.get(0).getProximo().size());

		do {
			solucaoShift = Procedimentos.copiarConteudoArray(solucao);
			//System.out.println("solucaoShift.get(0).getProximo().size(shift - LOOP) -> " + solucaoShift.get(0).getProximo().size());
			int index, index2;
			
			do {
				index = clienteAleatorio.nextInt(solucaoShift.size() - 1) + 1;
				k1 = solucaoShift.get(index); //Escolhe o primeiro cliente aleatoriamente para o shift - exclui o deposito
				//System.out.println("k1: " + k1.getID() + " | p = " + k1.getProximoNo());
				shiftK1paraRota = Procedimentos.ProximaRotaValida(solucaoShift, k1.getRota());
				ArrayList<No> tempSolucao = new ArrayList<No>(); //Array dos nos sem o deposito, sem o k1 e com os nos da rota k1 + 1
				for(No n : solucaoShift) {if(n.getID() != 0 && n.getRota() == shiftK1paraRota)tempSolucao.add(n);} //*-*
				//System.out.println(shiftK1paraRota);
				//System.out.println(tempSolucao.size());
				
				try {
					index2 = clienteAleatorio.nextInt(tempSolucao.size());
					k2 = tempSolucao.get(index2); //Escolhe o segundo cliente aleatoriamente para add o shift - considera o array
				}catch(IllegalArgumentException e) {
					System.out.println("k1 = " + k1.getID() + "|R: " + k1.getRota() + " - " + "shiftPara " + shiftK1paraRota);
					e.printStackTrace();
					System.exit(-1);
				}
				index2 = clienteAleatorio.nextInt(tempSolucao.size());
				k2 = tempSolucao.get(index2); //Escolhe o segundo cliente aleatoriamente para add o shift - considera o array
				//System.out.println("k2: " + k2.getID() + " | p = " + k2.getProximoNo());
				iteracoes++;
				if(iteracoes == nIt) {
					//System.out.println("Solucao.get(0).getProximo().size(shift - IT) -> " + solucao.get(0).getProximo().size());
					if(verbose) {
						System.out.println("\nsolucaoShift\n");
						System.out.println("NAO ALTEROU");
						for(No n : solucaoShift) {
							if(n.getID() != 0) 
								System.out.printf("{k=%d|p=%d|r=%d}\n", n.getID(), n.getProximoNo(), n.getRota());
							else
								System.out.printf("\n\n{k=%d|p=%s}\n", n.getID(), n.getVetorProximos());
						}
					}
					return solucao;
				}
			}while(Par_visitados.contains(index+"/"+index2));
			
			boolean k1D = solucaoShift.get(0).getProximo().contains(k1.getID());
			
			if(k1D) {  //k1 eh um no apontado pelo deposito
				//System.out.println("k1 eh um no apontado pelo deposito");
				solucaoShift.get(0).getProximo().set(solucaoShift.get(0).getProximo().indexOf(k1.getID()),k1.getProximoNo());
			}
			
			else {																	//k1 nao eh um no apontado pelo deposito
				//System.out.println("k1 nao eh um no apontado pelo deposito");
				try {
					Procedimentos.getNo(solucaoShift, -1, k1.getID()).setProximoNodes(k1.getProximoNo()); //anteriorDeK1 aponta para proximoDeK1
				}
				catch(NullPointerException npe) {
					System.out.println("\nsolucaoShift20\n");
					for(No n : solucaoShift) {
						if(n.getID() != 0) 
							System.out.printf("{k=%d|p=%d|r=%d}\n", n.getID(), n.getProximoNo(), n.getRota());
						else
							System.out.printf("{k=%d|p=%s}\n", n.getID(), n.getVetorProximos());
					}
					//System.out.println();
					//System.out.println("K1->" + k1.getID());
					//System.out.println("K2->" + k2.getID());
					npe.printStackTrace();
					System.exit(1);
				}

				//solucao.get(solucao.indexOf(k1) - 1).AddRotaPara(k2);
			}
			
			int proximoK2 = solucaoShift.get(solucaoShift.indexOf(k2)).getProximoNo();
			solucaoShift.get(solucaoShift.indexOf(k2)).setProximoNodes(k1.getID());
			solucaoShift.get(solucaoShift.indexOf(k1)).setProximoNodes(proximoK2);
			if(solucaoShift.get(0).getProximo().indexOf(0) != -1)
				solucaoShift.get(0).getProximo().remove(solucaoShift.get(0).getProximo().indexOf(0));
			//System.out.println("rota k1 : " + k1.getRota());
			
			Par_visitados.add(index+"/"+index2);
			iteracoes = 0;
		}while(Procedimentos.ultrapassaLimite(solucaoShift));	
		k1.setRota(k2.getRota());
		Procedimentos.atualizarRotas(solucaoShift);
		//System.out.println("Solucao.get(0).getProximo().size(shift - DEPOIS) -> " + solucao.get(0).getProximo().size());
		//System.out.println("solucaoShift.get(0).getProximo().size(shift - DEPOIS) -> " + solucaoShift.get(0).getProximo().size());
		if(verbose) {
			System.out.println("\nsolucaoShift\n");
			System.out.println("K1: " + k1.getID() + " | K2: " + k2.getID());
			for(No n : solucaoShift) {
				if(n.getID() != 0) 
					System.out.printf("{k=%d|p=%d|r=%d}\n", n.getID(), n.getProximoNo(), n.getRota());
				else
					System.out.printf("\n\n{k=%d|p=%s}\n", n.getID(), n.getVetorProximos());
			}
		}
		Procedimentos.verifica(solucaoShift);
		return solucaoShift;
	}
	public static ArrayList<No> Shift(ArrayList<No> solucao, int rotaOrigem, int rotaDestino){
		ArrayList<No> solucaoShift;
		ArrayList<String> Par_visitados = new ArrayList<String>();
		No k1, k2;
		int iteracoes = 0;
		//System.out.println("Solucao.get(0).getProximo().size(shift) -> " + solucao.get(0).getProximo().size());
		do {
			solucaoShift = Procedimentos.copiarConteudoArray(solucao);
			int index, index2;
			
			do {
				ArrayList<No> tempSolucaoOrigem = new ArrayList<No>(); //Array dos nos sem o deposito, com os nos da rotaOrigem
				for(No n : solucaoShift) {if(n.getID() != 0 && n.getRota() == rotaOrigem)tempSolucaoOrigem.add(n);} //*-*
				index = clienteAleatorio.nextInt(tempSolucaoOrigem.size());
				k1 = tempSolucaoOrigem.get(index); //Escolhe o primeiro cliente aleatoriamente para o shift - exclui o deposito
				//System.out.println("k1: " + k1.getID() + " | p = " + k1.getProximoNo());
				ArrayList<No> tempSolucaoDestino = new ArrayList<No>(); //Array dos nos sem o deposito, sem o k1 e com os nos da rota k1 + 1
				for(No n : solucaoShift) {if(n.getID() != 0 && n.getRota() == rotaDestino)tempSolucaoDestino.add(n);} //*-*
				//System.out.println(shiftK1paraRota);
				//System.out.println(tempSolucao.size());
				index2 = clienteAleatorio.nextInt(tempSolucaoDestino.size());
				k2 = tempSolucaoDestino.get(index2); //Escolhe o segundo cliente aleatoriamente para add o shift - considera o array
				//System.out.println("k2: " + k2.getID() + " | p = " + k2.getProximoNo());
				iteracoes++;
				if(iteracoes == nIt) {
					if(verbose) {
						System.out.println("\nsolucaoShiftAlt\n");
						System.out.println("NAO ALTEROU");
						for(No n : solucaoShift) {
							if(n.getID() != 0) 
								System.out.printf("{k=%d|p=%d|r=%d}\n", n.getID(), n.getProximoNo(), n.getRota());
							else
								System.out.printf("\n\n{k=%d|p=%s}\n", n.getID(), n.getVetorProximos());
						}
					}
					return solucao;
				}
			}while(Par_visitados.contains(index+"/"+index2));
			
			boolean k1D = solucaoShift.get(0).getProximo().contains(k1.getID());
			
			if(k1D) {  //k1 eh um no apontado pelo deposito
				//System.out.println("k1 eh um no apontado pelo deposito");
				solucaoShift.get(0).getProximo().set(solucaoShift.get(0).getProximo().indexOf(k1.getID()),k1.getProximoNo());
			}
			
			else {																	//k1 nao eh um no apontado pelo deposito
				//System.out.println("k1 nao eh um no apontado pelo deposito");
				Procedimentos.getNo(solucaoShift, -1, k1.getID()).setProximoNodes(k1.getProximoNo()); //anteriorDeK1 aponta para proximoDeK1
				//solucao.get(solucao.indexOf(k1) - 1).AddRotaPara(k2);
			}
			
			int proximoK2 = solucaoShift.get(solucaoShift.indexOf(k2)).getProximoNo();
			solucaoShift.get(solucaoShift.indexOf(k2)).setProximoNodes(k1.getID());
			solucaoShift.get(solucaoShift.indexOf(k1)).setProximoNodes(proximoK2);
			if(solucaoShift.get(0).getProximo().indexOf(0) != -1)
				solucaoShift.get(0).getProximo().remove(solucaoShift.get(0).getProximo().indexOf(0));
			
			//System.out.println("rota k1 : " + k1.getRota());
			
			Par_visitados.add(index+"/"+index2);
			iteracoes = 0;
		}while(Procedimentos.ultrapassaLimite(solucaoShift));	
		k1.setRota(k2.getRota());
		Procedimentos.atualizarRotas(solucaoShift);
		if(verbose) {
			System.out.println("\nsolucaoShiftAlt\n");
			System.out.println("K1: " + k1.getID() + " | K2: " + k2.getID());
			for(No n : solucaoShift) {
				if(n.getID() != 0) 
					System.out.printf("{k=%d|p=%d|r=%d}\n", n.getID(), n.getProximoNo(), n.getRota());
				else
					System.out.printf("\n\n{k=%d|p=%s}\n", n.getID(), n.getVetorProximos());
			}
		}
		Procedimentos.verifica(solucaoShift);
		return solucaoShift;
	}
	public static ArrayList<No> Shift20(ArrayList<No> solucao){
		ArrayList<No> solucaoShift20;
		ArrayList<String> Par_visitados = new ArrayList<String>();
		No k1, k2, k3;
		int shiftK1paraRota;
		int iteracoes = 0;
		//System.out.println("Solucao.get(0).getProximo().size(shift20) -> " + solucao.get(0).getProximo().size());

		do {
			solucaoShift20 = Procedimentos.copiarConteudoArray(solucao);
			int index, index2;
			
			do {
				index = clienteAleatorio.nextInt(solucaoShift20.size() - 1) + 1;
				k1 = solucaoShift20.get(index); //Escolhe o primeiro cliente aleatoriamente para o shift - exclui o deposito
				//System.out.println(-1);
				while(k1.getProximoNo() == 0) {
					index = clienteAleatorio.nextInt(solucaoShift20.size() - 1) + 1;
					k1 = solucaoShift20.get(index);
				}
				//System.out.println(0);
				k2 = Procedimentos.getNo(solucaoShift20, k1.getProximoNo(), -1);
				
				shiftK1paraRota = Procedimentos.ProximaRotaValida(solucaoShift20, k1.getRota()); //define para que rota vai o n�
				ArrayList<No> tempSolucao = new ArrayList<No>(); //Array dos nos sem o deposito, sem o k1 e com os nos da rota k1 + 1
					//System.out.println("SF " + shiftK1paraRota);
				for(No n : solucaoShift20) {if(n.getID() != 0 && n.getRota() == shiftK1paraRota)tempSolucao.add(n);} //*-*
				//System.out.println(1);
				index2 = clienteAleatorio.nextInt(tempSolucao.size());
				k3 = tempSolucao.get(index2); //Escolhe o segundo cliente aleatoriamente para add o shift - considera o array
				iteracoes++;
				if(iteracoes == nIt) {
					//System.out.println(1.2);
					if(verbose) {
						System.out.println("\nsolucaoShift20\n");
						System.out.println("NAO ALTEROU");
						for(No n : solucaoShift20) {
							if(n.getID() != 0) 
								System.out.printf("{k=%d|p=%d|r=%d}\n", n.getID(), n.getProximoNo(), n.getRota());
							else
								System.out.printf("\n\n{k=%d|p=%s}\n", n.getID(), n.getVetorProximos());
						}
					}
					return solucao;
				}
			}while(Par_visitados.contains(index+"/"+index2));
			//System.out.println(2);
			//System.out.println("k1: " + k1.getID() + " - proximo: " + k1.getProximoNo());
			//System.out.println("k2: " + k2.getID() + " - proximo: " + k2.getProximoNo());
			//System.out.println("k3: " + k3.getID() + " - proximo: " + k3.getProximoNo());
			
			boolean k1D = solucaoShift20.get(0).getProximo().contains(k1.getID());
			
			if(k1D) {  //k1 eh um no apontado pelo deposito
				//System.out.println("k1 eh um no apontado pelo deposito");
				solucaoShift20.get(0).getProximo().set(solucaoShift20.get(0).getProximo().indexOf(k1.getID()),k2.getProximoNo());
			}
			
			else {																	//k1 nao eh um no apontado pelo deposito
				//System.out.println("k1 nao eh um no apontado pelo deposito");
				try {
					Procedimentos.getNo(solucaoShift20, -1, k1.getID()).setProximoNodes(k2.getProximoNo()); //anteriorDeK1 aponta para proximoDeK2
				}
				catch(NullPointerException npe) {
					System.out.println("\nsolucaoShift20\n");
					for(No n : solucaoShift20) {
						if(n.getID() != 0) 
							System.out.printf("{k=%d|p=%d|r=%d}\n", n.getID(), n.getProximoNo(), n.getRota());
						else
							System.out.printf("\n\n{k=%d|p=%s}\n", n.getID(), n.getVetorProximos());
					}
					//System.out.println();
					//System.out.println("K1->" + k1.getID());
					//System.out.println("K2->" + k2.getID());
					//System.out.println("K3->" + k3.getID());
					npe.printStackTrace();
					System.exit(1);
				}
				//solucao.get(solucao.indexOf(k1) - 1).AddRotaPara(k2);
			}
			
			int proximoK3 = solucaoShift20.get(solucaoShift20.indexOf(k3)).getProximoNo();
			solucaoShift20.get(solucaoShift20.indexOf(k3)).setProximoNodes(k1.getID());
			solucaoShift20.get(solucaoShift20.indexOf(k2)).setProximoNodes(proximoK3);
			
			if(solucaoShift20.get(0).getProximo().indexOf(0) != -1)
				solucaoShift20.get(0).getProximo().remove(solucaoShift20.get(0).getProximo().indexOf(0));
			
			Par_visitados.add(index+"/"+index2);
			//iteracoes = 0;
			//System.out.println(Procedimentos.ultrapassaLimite(solucaoShift20));
		} while(Procedimentos.ultrapassaLimite(solucaoShift20));
		k1.setRota(k3.getRota());
		k2.setRota(k3.getRota());
		Procedimentos.atualizarRotas(solucaoShift20);
		//System.out.println(3);
		if(verbose) {
			System.out.println("\nsolucaoShift20\n");
			System.out.println("K1: " + k1.getID() + " | K2: " + k2.getID() + " | K3: " + k3.getID());
	
			for(No n : solucaoShift20) {
				if(n.getID() != 0) 
					System.out.printf("{k=%d|p=%d|r=%d}\n", n.getID(), n.getProximoNo(), n.getRota());
				else
					System.out.printf("\n\n{k=%d|p=%s}\n", n.getID(), n.getVetorProximos());
			}
		}
		Procedimentos.verifica(solucaoShift20);
		return solucaoShift20;
	}
	
	public static ArrayList<No> Swap(ArrayList<No> solucao){
		ArrayList<No> solucaoSwap;
		ArrayList<String> Par_visitados = new ArrayList<String>();
		No k1, k2;
		int iteracoes = 0;
		//System.out.println("Solucao.get(0).getProximo().size(swap) -> " + solucao.get(0).getProximo().size());

		do {
			solucaoSwap = Procedimentos.copiarConteudoArray(solucao);
			int index, index2;
			
			do {
				index = clienteAleatorio.nextInt(solucaoSwap.size() - 1) + 1;
			
				k1 = solucaoSwap.get(index); //Escolhe o primeiro cliente aleatoriamente para o swap - exclui o deposito
				//System.out.println("k1: " + k1.getID());
				ArrayList<No> tempSolucao = new ArrayList<No>(); 
				//Array dos nos sem o deposito, sem o k1 e sem os nos da rota de k1
				for(No n : solucaoSwap) {if(n.getID() != 0 && n.getRota() != k1.getRota())tempSolucao.add(n);} //*-*
				index2 = clienteAleatorio.nextInt(tempSolucao.size());
				k2 = tempSolucao.get(index2); //Escolhe o segundo cliente aleatoriamente para o swap - considera o array
				iteracoes++;
				if(iteracoes == nIt) {
					if(verbose) {
						System.out.println("\nsolucaoSwap\n");
						System.out.println("NAO ALTEROU");
						for(No n : solucaoSwap) {
							if(n.getID() != 0) 
								System.out.printf("{k=%d|p=%d|r=%d}\n", n.getID(), n.getProximoNo(), n.getRota());
							else
								System.out.printf("\n\n{k=%d|p=%s}\n", n.getID(), n.getVetorProximos());
						}
					}
					return solucao;
				}
			}while(Par_visitados.contains(index+"/"+index2));
			
			//System.out.println("k2: " + k2.getID());
			
			boolean k1D = solucaoSwap.get(0).getProximo().contains(k1.getID());
			boolean k2D = solucaoSwap.get(0).getProximo().contains(k2.getID());
			No aux1 = null, aux2 = null;
			if(k1D) { //k1 eh um no apontado pelo deposito
				//System.out.println("k1 eh um no apontado pelo deposito");
				solucaoSwap.get(0).setProximoSwap(k1.getID(), k2.getID());
			}
			
			else {																	 //k1 nao eh um no apontado pelo deposito
				//System.out.println("k1 nao eh um no apontado pelo deposito");
				//Procedimentos.getNo(solucaoSwap, -1, k1.getID()).setProximoNodes(k2.getID()); //anteriorDeK1 aponta para K2
				aux1 = Procedimentos.getNo(solucaoSwap, -1, k1.getID());
				//solucao.get(solucao.indexOf(k1) - 1).AddRotaPara(k2);
			}
			if(k2D) {	//k2 eh um no apontado pelo deposito 
				//System.out.println("k2 eh um no apontado pelo deposito");
				solucaoSwap.get(0).setProximoSwap(k2.getID(), k1.getID());
			}
			else {																	//k2 nao eh um no apontado pelo deposito
				//System.out.println("k2 nao eh um no apontado pelo deposito");
				try {
					//Procedimentos.getNo(solucaoSwap, -1, k2.getID()).setProximoNodes(k1.getID()); //anteriorDeK2 aponta para K1
					aux2 = Procedimentos.getNo(solucaoSwap, -1, k2.getID());
				}
				catch(NullPointerException npe) {
					System.out.println("solucaoSwap");
					for(No n : solucaoSwap) {
						if(n.getID() != 0) 
							System.out.printf("{k=%d|p=%d|r=%d}", n.getID(), n.getProximoNo(), n.getRota());
						else
							System.out.printf("{k=%d|p=%s}", n.getID(), n.getVetorProximos());
					}
					System.out.println();
					System.out.println("K1->" + k1.getID());
					System.out.println("K1->" + k2.getProximoNo());
					npe.printStackTrace();
					System.exit(1);
				}
				//solucao.get(solucao.indexOf(k2) - 1).AddRotaPara(k1);
			}
			
			if(!k1D && !k2D) {
				aux1.setProximoNodes(k2.getID());
				aux2.setProximoNodes(k1.getID());
			}
			else if(!k1D) {
				Procedimentos.getNo(solucaoSwap, -1, k1.getID()).setProximoNodes(k2.getID()); //anteriorDeK1 aponta para K3
			}
			else if(!k2D){
				Procedimentos.getNo(solucaoSwap, -1, k2.getID()).setProximoNodes(k1.getID()); //anteriorDeK3 aponta para K1
			}
			else {
				//System.out.println(k1.getID());
				//System.out.println(k2.getID());
			}
			int k2_proximo = k2.getProximoNo();
			int k1_rota = k1.getRota();
			int k2_rota = k2.getRota();
			
			k2.setProximoNodes(k1.getProximoNo());
			k1.setProximoNodes(k2_proximo);
			
			k1.setRota(k2_rota);
			k2.setRota(k1_rota);
			
			Par_visitados.add(index+"/"+index2);
			iteracoes = 0;
		}while(Procedimentos.ultrapassaLimite(solucaoSwap));
		
		Procedimentos.atualizarRotas(solucaoSwap);
		if(verbose) {
			System.out.println("\nsolucaoSwap\n");
			System.out.println("K1: " + k1.getID() + " | K2: " + k2.getID());
	
			for(No n : solucaoSwap) {
				if(n.getID() != 0) 
					System.out.printf("{k=%d|p=%d|r=%d}\n", n.getID(), n.getProximoNo(), n.getRota());
				else
					System.out.printf("\n\n{k=%d|p=%s}\n", n.getID(), n.getVetorProximos());
			}
		}
		Procedimentos.verifica(solucaoSwap);
		return solucaoSwap;
		
	}
	public static ArrayList<No> Swap21(ArrayList<No> solucao){
		ArrayList<No> solucaoSwap21;
		ArrayList<String> Par_visitados = new ArrayList<String>();
		No k1, k2, k3;
		int iteracoes = 0;
		//System.out.println("Solucao.get(0).getProximo().size(swap21) -> " + solucao.get(0).getProximo().size());

		do {
			solucaoSwap21 = Procedimentos.copiarConteudoArray(solucao);
			int index, index2;
			
			do {
				index = clienteAleatorio.nextInt(solucaoSwap21.size() - 1) + 1;
				
				k1 = solucaoSwap21.get(index); //Escolhe o primeiro cliente aleatoriamente para o swap - exclui o deposito
				while(k1.getProximoNo() == 0) {
					index = clienteAleatorio.nextInt(solucaoSwap21.size() - 1) + 1;
					k1 = solucaoSwap21.get(index);
				}
				
				k2 = Procedimentos.getNo(solucaoSwap21, k1.getProximoNo(), -1);
				
				ArrayList<No> tempSolucao = new ArrayList<No>(); //Array dos nos sem o deposito, sem os nos da rota de k1
				for(No n : solucaoSwap21) {if(n.getID() != 0 && n.getRota() != k1.getRota())tempSolucao.add(n);} //*-*
				index2 = clienteAleatorio.nextInt(tempSolucao.size());
				k3 = tempSolucao.get(index2); //Escolhe o segundo cliente aleatoriamente para o swap - considera o array
				iteracoes++;
				if(iteracoes == nIt) {
					if(verbose) {
						System.out.println("\nsolucaoSwap21\n");
						System.out.println("NAO ALTEROU");
						for(No n : solucaoSwap21) {
							if(n.getID() != 0) 
								System.out.printf("{k=%d|p=%d|r=%d}\n", n.getID(), n.getProximoNo(), n.getRota());
							else
								System.out.printf("\n\n{k=%d|p=%s}\n", n.getID(), n.getVetorProximos());
						}
					}
					return solucao;
				}
			}while(Par_visitados.contains(index+"/"+index2));
			
			//System.out.println("k1: " + k1.getID());
			//System.out.println("k2: " + k2.getID());
			//System.out.println("k3: " + k3.getID());
			
			boolean k1D = solucaoSwap21.get(0).getProximo().contains(k1.getID());
			boolean k3D = solucaoSwap21.get(0).getProximo().contains(k3.getID());
			No aux1 = null, aux2 = null;
			if(k1D) { //k1 eh um no apontado pelo deposito
				//System.out.println("k1 eh um no apontado pelo deposito");
				solucaoSwap21.get(0).setProximoSwap(k1.getID(), k3.getID());
			}
			else {
				//System.out.println("k1 nao eh um no apontado pelo deposito");
				//System.out.println(Procedimentos.getNo(solucaoSwap22, -1, k1.getID()).getID() + " aponta para " + k3.getID());
				aux1 = Procedimentos.getNo(solucaoSwap21, -1, k1.getID()); //quem aponta pra k1
						//.setProximoNodes(k3.getID()); //anteriorDeK1 aponta para K3
			}
			if(k3D) {	//k3 eh um no apontado pelo deposito
				//System.out.println("k3 eh um no apontado pelo deposito");
				solucaoSwap21.get(0).setProximoSwap(k3.getID(), k1.getID());
			}
			else {
				//System.out.println("k3 nao eh um no apontado pelo deposito");
				//System.out.println(Procedimentos.getNo(solucaoSwap22, -1, k3.getID()).getID() + " aponta para " + k1.getID());
				aux2 = Procedimentos.getNo(solucaoSwap21, -1, k3.getID()); //quem aponta pra k3
						//.setProximoNodes(k1.getID()); //anteriorDeK3 aponta para K1
			}
			if(!k1D && !k3D) {
				aux1.setProximoNodes(k3.getID());
				aux2.setProximoNodes(k1.getID());
			}
			else if(!k1D) {
				Procedimentos.getNo(solucaoSwap21, -1, k1.getID()).setProximoNodes(k3.getID()); //anteriorDeK1 aponta para K3
			}
			else if(!k3D){
				Procedimentos.getNo(solucaoSwap21, -1, k3.getID()).setProximoNodes(k1.getID()); //anteriorDeK3 aponta para K1
			}
			else {
				//System.out.println(k1.getID());
				//System.out.println(k3.getID());
			}
			int k3_proximo = k3.getProximoNo();
			int k1_rota = k1.getRota();
			int k3_rota = k3.getRota();
			
			k3.setProximoNodes(k2.getProximoNo());
			k2.setProximoNodes(k3_proximo);
			
			k1.setRota(k3_rota);
			k2.setRota(k3_rota);
			k3.setRota(k1_rota);
			
			Par_visitados.add(index+"/"+index2);
			iteracoes = 0;
		}while(Procedimentos.ultrapassaLimite(solucaoSwap21));
		
		Procedimentos.atualizarRotas(solucaoSwap21);
		if(verbose) {
			System.out.println("\nsolucaoSwap21\n");
			System.out.println("K1: " + k1.getID() + " | K2: " + k2.getID() + " | K3: " + k3.getID());
	
			for(No n : solucaoSwap21) {
				if(n.getID() != 0) 
					System.out.printf("{k=%d|p=%d|r=%d}\n", n.getID(), n.getProximoNo(), n.getRota());
				else
					System.out.printf("\n\n{k=%d|p=%s}\n", n.getID(), n.getVetorProximos());
			}
		}
		Procedimentos.verifica(solucaoSwap21);
		return solucaoSwap21;
	}
	public static ArrayList<No> Swap22(ArrayList<No> solucao){
		ArrayList<No> solucaoSwap22;
		ArrayList<String> Par_visitados = new ArrayList<String>();
		No k1, k2, k3, k4;
		int iteracoes = 0;
		//System.out.println("Solucao.get(0).getProximo().size(swap22) -> " + solucao.get(0).getProximo().size());

		do {
			solucaoSwap22 = Procedimentos.copiarConteudoArray(solucao);
			int index, index2;
			
			do {
				index = clienteAleatorio.nextInt(solucaoSwap22.size() - 1) + 1;
				k1 = solucaoSwap22.get(index); //Escolhe o primeiro cliente aleatoriamente para o swap - exclui o deposito
				while(k1.getProximoNo() == 0) {
					index = clienteAleatorio.nextInt(solucaoSwap22.size() - 1) + 1;
					k1 = solucaoSwap22.get(index);
				}
				k2 = Procedimentos.getNo(solucaoSwap22, k1.getProximoNo(), -1);
				
				ArrayList<No> tempSolucao = new ArrayList<No>(); //Array dos nos sem o deposito, sem os nos da rota de k1
				//System.out.println("solucao swap 22");
				//for(No n : solucaoSwap22) {if(n.getID() != 0) System.out.printf("{k=%d|p=%d|r=%d}", n.getID(), n.getProximoNo(), n.getRota());}
				//System.out.println();
				for(No n : solucaoSwap22) {if(n.getID() != 0 && n.getRota() != k1.getRota() && n.getProximoNo() != 0)tempSolucao.add(n);} //*-*
				index2 = clienteAleatorio.nextInt(tempSolucao.size());
				k3 = tempSolucao.get(index2); //Escolhe o segundo cliente aleatoriamente para o swap - considera o array
				k4 = Procedimentos.getNo(solucaoSwap22, k3.getProximoNo(), -1);
				iteracoes++;
				if(iteracoes == nIt) {
					if(verbose) {
						System.out.println("\nsolucaoSwap22\n");
						System.out.println("NAO ALTEROU");
						for(No n : solucaoSwap22) {
							if(n.getID() != 0) 
								System.out.printf("{k=%d|p=%d|r=%d}\n", n.getID(), n.getProximoNo(), n.getRota());
							else
								System.out.printf("\n\n{k=%d|p=%s}\n", n.getID(), n.getVetorProximos());
						}
					}
					return solucao;
				}
			}while(Par_visitados.contains(index+"/"+index2));
			
			
			boolean k1D = solucaoSwap22.get(0).getProximo().contains(k1.getID());
			boolean k3D = solucaoSwap22.get(0).getProximo().contains(k3.getID());
			No aux1 = null, aux2 = null;
			if(k1D) { //k1 eh um no apontado pelo deposito
				//System.out.println("k1 eh um no apontado pelo deposito");
				solucaoSwap22.get(0).setProximoSwap(k1.getID(), k3.getID());
			}
			else {
				//System.out.println("k1 nao eh um no apontado pelo deposito");
				//System.out.println(Procedimentos.getNo(solucaoSwap22, -1, k1.getID()).getID() + " aponta para " + k3.getID());
				aux1 = Procedimentos.getNo(solucaoSwap22, -1, k1.getID());
						//.setProximoNodes(k3.getID()); //anteriorDeK1 aponta para K3
			}
			if(k3D) {	//k3 eh um no apontado pelo deposito
				//System.out.println("k3 eh um no apontado pelo deposito");
				solucaoSwap22.get(0).setProximoSwap(k3.getID(), k1.getID());
			}
			else {
				//System.out.println("k3 nao eh um no apontado pelo deposito");
				//System.out.println(Procedimentos.getNo(solucaoSwap22, -1, k3.getID()).getID() + " aponta para " + k1.getID());
				aux2 = Procedimentos.getNo(solucaoSwap22, -1, k3.getID());
						//.setProximoNodes(k1.getID()); //anteriorDeK3 aponta para K1
			}
			if(!k1D && !k3D) {
				aux1.setProximoNodes(k3.getID());
				aux2.setProximoNodes(k1.getID());
			}
			else if(!k1D) {
				Procedimentos.getNo(solucaoSwap22, -1, k1.getID()).setProximoNodes(k3.getID()); //anteriorDeK1 aponta para K3
			}
			else if(!k3D){
				Procedimentos.getNo(solucaoSwap22, -1, k3.getID()).setProximoNodes(k1.getID()); //anteriorDeK3 aponta para K1
			}
			else {
				//System.out.println(k1.getID());
				//System.out.println(k3.getID());
			}
			int k4_proximo = k4.getProximoNo();
			int k1_rota = k1.getRota();
			int k3_rota = k3.getRota();
			
			k4.setProximoNodes(k2.getProximoNo());
			k2.setProximoNodes(k4_proximo);
			
			k1.setRota(k3_rota);
			k2.setRota(k3_rota);
			k3.setRota(k1_rota);
			k4.setRota(k1_rota);
			
			Par_visitados.add(index+"/"+index2);
			iteracoes = 0;
		}while(Procedimentos.ultrapassaLimite(solucaoSwap22));
		
		Procedimentos.atualizarRotas(solucaoSwap22);
		if(verbose) {
			System.out.println("\nsolucaoSwap22\n");
			System.out.println("K1: " + k1.getID() + " | K2: " + k2.getID() + " | K3: " + k3.getID() + " | K4: " + k4.getID());
	
			for(No n : solucaoSwap22) {
				if(n.getID() != 0) 
					System.out.printf("{k=%d|p=%d|r=%d}\n", n.getID(), n.getProximoNo(), n.getRota());
				else
					System.out.printf("\n\n{k=%d|p=%s}\n", n.getID(), n.getVetorProximos());
			}
		}
		Procedimentos.verifica(solucaoSwap22);
		return solucaoSwap22;
	}
}