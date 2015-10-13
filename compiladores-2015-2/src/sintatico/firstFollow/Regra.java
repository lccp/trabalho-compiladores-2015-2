package sintatico.firstFollow;


public class Regra {
	NaoTerminal naoTerminal;
	Terminal terminal;
	String regra;
	
	public Regra(String regra){
		super();
		this.regra = regra;
	}
	
	public Regra(String regra, Terminal terminal){
		this(regra);
		this.terminal = terminal;
	}
	
	public Regra(String regra, Terminal terminal, NaoTerminal naoTerminal) {
		this(regra, terminal);
		this.naoTerminal = naoTerminal;
	}

	public NaoTerminal getNaoTerminal() {
		return naoTerminal;
	}

	public void setNaoTerminal(NaoTerminal naoTerminal) {
		this.naoTerminal = naoTerminal;
	}

	public Terminal getTerminal() {
		return terminal;
	}
	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}
	public String getRegra() {
		return regra;
	}
	public void setRegra(String regra) {
		this.regra = regra;
	}
	
}
