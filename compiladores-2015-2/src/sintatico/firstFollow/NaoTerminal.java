package sintatico.firstFollow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class NaoTerminal{
	public String val;
	public ArrayList<Terminal> first = new ArrayList<Terminal>();
	public ArrayList<Terminal> follow = new ArrayList<Terminal>();
	public HashMap<String, String> firstMap = new HashMap<String, String>();
	public HashMap<String, String> followMap = new HashMap<String, String>();
	
	public NaoTerminal(String val){
		this();
		this.val = val;
	}
	
	public NaoTerminal(){
		super();
	}
	
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public ArrayList<Terminal> getFirst() {
		return first;
	}
	public void setFirst(ArrayList<Terminal> first) {
		this.first = first;
	}
	public ArrayList<Terminal> getFollow() {
		return follow;
	}
	public void setFollow(ArrayList<Terminal> follow) {
		this.follow = follow;
	}
	
	public List<Terminal> First(String valor){
		List<Terminal> resultado = new ArrayList<Terminal>();
		String aux [] = valor.split(",");
		if(Reader.TERMINAIS.containsKey(aux[0].trim())){
			resultado.add(new Terminal(aux[0]));
		}else if(aux[0].indexOf("|") < 0){
			NaoTerminal temp = Reader.NAOTERMINAIS.get(aux[0].trim());
			String prodTemp = Reader.GRAMATICA.get(temp.val);
			return temp.First(prodTemp);
		}
		return resultado;
		
	}
	
	public List<Terminal> FirstL(String valor){
		List<Terminal> resultado = new ArrayList<Terminal>();
		String aux [] = valor.split(",");
		if(Reader.TERMINAIS.containsKey(aux[0].trim())){
			resultado.add(new Terminal(aux[0]));
			return resultado;
		}else{
			NaoTerminal temp = Reader.NAOTERMINAIS.get(aux[0].trim());
			return temp.first;
		}
	}

	public void Firsts() {
		for (int j = 0; j < Reader.NAOTERMINAIS.values().size(); j++) {
			for(NaoTerminal nt : Reader.NAOTERMINAIS.values()){
				String aux [] = Reader.GRAMATICA.get(nt.val).split("\\|");
				for (int i = 0; i < aux.length; i++) {
					if(j == 0){
						List<Terminal> ltt = nt.First(aux[i]);
						for (Terminal tt : ltt) {
							if(!nt.firstMap.containsKey(tt.getVal())){
								nt.getFirst().add(tt);
								nt.firstMap.put(tt.getVal(), tt.getVal());
							}
						}
					}else{
						List<Terminal> ltt = nt.FirstL(aux[i]);
						for (Terminal tt : ltt) {
							if(!nt.firstMap.containsKey(tt.getVal())){
								nt.getFirst().add(tt);
								nt.firstMap.put(tt.getVal(), tt.getVal());
							}
						}
					}
				}
			}
		}
		
	}
	
	public void Follows(){
		Regra1();
		Regra2();
		Regra3();
	}

	public void Regra1(){
		Iterator<String> ite = Reader.NAOTERMINAIS.keySet().iterator();
		while(ite.hasNext()){
			NaoTerminal nt = Reader.NAOTERMINAIS.get(ite.next());
			if(nt.val.equals(Reader.SIMBOLO_INICIAL)){
				nt.follow.add(new Terminal("$"));
				nt.followMap.put("$", "$");
				break;
			}
		}
	}
	
	public void Regra2() {
		Iterator<String> iteGra = Reader.GRAMATICA.keySet().iterator();
		while(iteGra.hasNext()){
			String [] prods = Reader.GRAMATICA.get(iteGra.next()).split("\\|");
			for (int i = 0; i < prods.length; i++) {
				String [] prod = prods[i].split(",");
				for (int j = 0; j < prod.length; j++) {
					while(j < prod.length && Reader.TERMINAIS.containsKey(prod[j].trim())){
						j++;
					}
					if(j < prod.length){
						NaoTerminal ntAtual = Reader.NAOTERMINAIS.get(prod[j]);;
						for (int k = j+1; k < prod.length; k++) {
							if(Reader.TERMINAIS.containsKey(prod[k].trim()) && !prod[k].trim().equals("#")){
								if(!ntAtual.jaTiver(prod[k].trim())){
									ntAtual.follow.add(new Terminal(prod[k].trim()));
									ntAtual.followMap.put(prod[k].trim(), prod[k].trim());
								}
							}else{
								NaoTerminal ntProx = Reader.NAOTERMINAIS.get(prod[k].trim());
								addAllNaoRepitidos(ntAtual.follow, ntProx.first, ntAtual.followMap);
								if(!contemVazio(ntProx)){
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void Regra3(){
		Iterator<String> iteGra = Reader.GRAMATICA.keySet().iterator();
		while(iteGra.hasNext()){
			String prodAtual = iteGra.next();
			String [] prods = Reader.GRAMATICA.get(prodAtual.trim()).split("\\|");
			NaoTerminal ntAtual = Reader.NAOTERMINAIS.get(prodAtual.trim());
			for (int i = 0; i < prods.length; i++) {
				String [] prod = prods[i].split(",");
				for (int k = prod.length-1; k > -1; k--) {
					if(!Reader.TERMINAIS.containsKey(prod[k].trim()) && !prod[k].trim().equals("#")){
						NaoTerminal ntProx = Reader.NAOTERMINAIS.get(prod[k].trim());
						if(!prodAtual.equals(prod[k].trim())){
							this.addAllNaoRepitidos(ntProx.follow, ntAtual.follow, ntProx.followMap);
						}
						if(!contemVazio(ntProx)){
							break;
						}
					}else{
						break;
					}
				}
			}
		}
	}

	private void addAllNaoRepitidos(ArrayList<Terminal> adicionado, ArrayList<Terminal> adicionando, HashMap<String, String> followMap) {
		for (Terminal inexistente : adicionando) {
			if(!followMap.containsKey(inexistente.getVal().trim()) && !inexistente.getVal().trim().equals("#")){
				adicionado.add(inexistente);
				followMap.put(inexistente.getVal().trim(), inexistente.getVal().trim());
			}
		}
	}

	public boolean contemVazio(NaoTerminal nt) {
		boolean isFound = false;
		for(Terminal atual : nt.first){
			if(atual.getVal().equals("#")){
				isFound = true;
				break;
			}
		}
		return isFound;
	}
	
	public boolean jaTiver(String val) {
		boolean isFound = false;
		for(Terminal atual : this.follow){
			if(atual.getVal().trim().equals(val.trim())){
				isFound = true;
			}
		}
		return isFound;
	}
}
