package sintatico.firstFollow;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FirstFollow {
	public static void main(String[] args) {
		Reader.popularGramatica();
		Iterator<String> ite = Reader.NAOTERMINAIS.keySet().iterator();
		NaoTerminal nt = Reader.NAOTERMINAIS.get(ite.next());
		nt.Firsts();
		System.out.println("---------------------------FIRSTS---------------------------");
		for (Iterator<String> iterator = Reader.NAOTERMINAIS.keySet().iterator(); iterator.hasNext();) {
			String aux = iterator.next();
			NaoTerminal naot = Reader.NAOTERMINAIS.get(aux);
			System.out.println("First("+naot.val+"): ");
			for (Terminal tt : naot.first) {
				System.out.println(tt.getVal());
			}
		}
		nt.Follows();
		
		for (Iterator<String> iterator = Reader.NAOTERMINAIS.keySet().iterator(); iterator.hasNext();) {
			NaoTerminal naot = Reader.NAOTERMINAIS.get(iterator.next());
			for (int i = 0; i < naot.follow.size(); i++) {
				Terminal ntemp = naot.follow.get(i);
				for (int j = i+1; j < naot.follow.size(); j++) {
					Terminal ttemp = naot.follow.get(j);
					if(ntemp.getVal().trim().equals(ttemp.getVal().trim())){
						naot.follow.remove(j);
						j--;
					}
				}
			}
		}
		
		System.out.println("---------------------------FOLLOW---------------------------");
		for (Iterator<String> iterator = Reader.NAOTERMINAIS.keySet().iterator(); iterator.hasNext();) {
			String aux = iterator.next();
			NaoTerminal naot = Reader.NAOTERMINAIS.get(aux);
			System.out.println("Follow("+naot.val+"): ");
			for (Terminal tt : naot.follow) {
				System.out.println(tt.getVal());
			}
		}
		
		System.out.println("------------------------------------REGRAS (TABELA)------------------------------------");
		HashMap<String, List<Regra>> mapaRegras = new HashMap<String, List<Regra>>();
		for (Iterator<String> iterator = Reader.GRAMATICA.keySet().iterator(); iterator.hasNext();) {
			String aux = iterator.next();
			NaoTerminal naot = Reader.NAOTERMINAIS.get(aux);
			for (Terminal tt : naot.first) {
				List<Regra> lista = tt.getProducao(naot);
				if(mapaRegras.get(tt.getVal()) == null){
					mapaRegras.put(tt.getVal(), lista);
				}else{
					List<Regra> temp = mapaRegras.get(tt.getVal());
					temp.addAll(lista);
				}
			}
		}
		Iterator<Entry<String, List<Regra>>> iter = mapaRegras.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, java.util.List<Regra>> entry = (Map.Entry<String, java.util.List<Regra>>) iter.next();
			String old = "";
			for(Regra r : entry.getValue()){
				if(!r.getTerminal().getVal().equals(old)){
					System.out.println("===================== "+(r.getTerminal().getVal().equals("#")?"$":r.getTerminal().getVal())+" =======================");
					old = r.getTerminal().getVal();
				}
				System.out.println(r.getNaoTerminal().getVal()+" ::= "+r.getRegra());
			}
			
		}
	}
}
