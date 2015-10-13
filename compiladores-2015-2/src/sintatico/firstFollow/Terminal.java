package sintatico.firstFollow;

import java.util.ArrayList;
import java.util.List;

public class Terminal {
	private String val;

	public Terminal(String val) {
		super();
		this.val = val;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public List<Regra> getProducao(NaoTerminal nt) {
		List<Regra> list = new ArrayList<Regra>();
		String rules [] = Reader.GRAMATICA.get(nt.getVal()).split("\\|");
		for (int i = 0; i < rules.length; i++) {
			String rulesAux [] = rules[i].split(",");
			if(Reader.NAOTERMINAIS.containsKey(rulesAux[0])){
				NaoTerminal ntAux = Reader.NAOTERMINAIS.get(rulesAux[0]);
				for (Terminal tt : ntAux.first) {
					if(tt.getVal().trim().equals(this.val)){
						list.add(new Regra(rules[i], tt, Reader.NAOTERMINAIS.get(nt.getVal())));
					}
				}
			}else if(Reader.TERMINAIS.containsKey(rulesAux[0])){
				if(rulesAux[0].trim().equals(this.val)){
					list.add(new Regra(rules[i], this, Reader.NAOTERMINAIS.get(nt.getVal())));
				}
			}
		}
		return list;
	}
	
}
