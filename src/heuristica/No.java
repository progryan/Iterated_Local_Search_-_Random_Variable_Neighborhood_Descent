package heuristica;

import java.util.ArrayList;

public final class No {
	private int coleta;
	private int entrega;
	private int id;
	private ArrayList<Double> vetorDistancias = new ArrayList<Double>();
	private int [] X = new int [Main.instances];
	private ArrayList<Integer> proximos;
	private int p;
	private int rota;
	
	public No() {
		proximos = new ArrayList<Integer>();
	}
	public No(String node, ArrayList<Integer> proximos, int p) {
		this.id = Integer.parseInt(node.split(" ")[0]);
		this.entrega = Integer.parseInt(node.split(" ")[1]);
		this.coleta = Integer.parseInt(node.split(" ")[2]);
		this.proximos = proximos;
		this.p = p;
		for(int i = 0; i < Main.instances; i++) {
			X[i] = 0;
			this.vetorDistancias.add(Double.parseDouble(node.split(" ")[3].split(",")[i]));
		}
	}
	
	public int getColeta() {
		return coleta;
	}
	public void setColeta(int coleta) {
		this.coleta = coleta;
	}
	public int getEntrega() {
		return entrega;
	}
	public void setEntrega(int entrega) {
		this.entrega = entrega;
	}
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}
	public int getRota() {
		return rota;
	}
	public void setRota(int rota) {
		this.rota = rota;
	}
	public ArrayList<Integer> getProximo() {
		return proximos;
	}
	public String getVetorProximos() {
		String temp = "[";
		for (int d : proximos) {
			temp += d + ","; 
		}
		return temp.substring(0, temp.length() - 1) + "]";
	}
	public int getProximoNo() {
		return p;
	}
	public void setProximoNodes(int proximo) {
		this.p = proximo;
	}
	public void setProximoDeposito(int proximo) {
		this.proximos.add(proximo);
	}
	public void setProximoSwap(int index, int proximo) {
		this.proximos.set(this.proximos.indexOf(index), proximo);
	}
	public String getVetorDistancias() {
		String temp = " [";
		for (Double d : vetorDistancias) {
			temp += d + ", "; 
		}
		return temp.substring(0, temp.length()) + "]";
	}
	public ArrayList<Double> getVetorDistanciasClass() {
		return vetorDistancias;
	}
	public void setVetorProximosClass(ArrayList<Integer> vp) {
		//System.out.println("Antes proximos.size() = " + proximos.size());
		this.proximos.addAll(vp);
		//System.out.println("Depois proximos.size() = " + proximos.size());
	}
	public void setVetorDistanciasClass(ArrayList<Double> vd) {
		this.vetorDistancias.addAll(vd);
	}
	public String getVetorRotas() {
		String temp = " [";
		for (int d : X) {
			temp += d + ", "; 
		}
		return temp.substring(0, temp.length() - 2) + "]";
	}
	public Double distanciaAte(No node) {
		return vetorDistancias.get(node.getID());
	}
	
	public int rotaPara(No node) {
		return X[node.getID()];
	}
	public void AddRotaPara(int node) {
		this.X[node] = 1;
	}
	public void cleanRotas() {
		for(int i = 0; i < X.length; i++) {
			X[i] = 0;
		}
	}
}