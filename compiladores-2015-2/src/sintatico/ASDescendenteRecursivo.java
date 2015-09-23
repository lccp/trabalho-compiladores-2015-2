package sintatico;

import model.*;

import java.util.ArrayList;
import java.util.List;

import lexico.AnalisadorLexico;;


public class ASDescendenteRecursivo {
	private static List<Token> lista = null;
	private static List<String> OPERADORES_LOGICOS_MATEMATICOS = null;
	public static void MainClass() throws Exception{
		Token reg = lista.remove(0);
		if(!reg.valor.equalsIgnoreCase("class")){
			throw new Exception("reserved word CLASS expected");
		}
		reg = lista.remove(0);
		if(!reg.tag.equalsIgnoreCase("Identificador")){
			throw new Exception("CLASS name expected");
		}
		reg = lista.remove(0);
		if(!reg.valor.equalsIgnoreCase("{")){
			throw new Exception("{ expected");
		}
		reg = lista.remove(0);
		
		if(reg.valor.equalsIgnoreCase("public")){
			reg = lista.remove(0);
			if(reg.valor.equalsIgnoreCase("static")){
				reg = lista.remove(0);
				if(!reg.valor.equalsIgnoreCase("void")){
					throw new Exception("Main method definition invalid: [public] [static] void main (String [] )");
				}
			}
		}else if(reg.valor.equalsIgnoreCase("static")){
			reg = lista.remove(0);
			if(!reg.valor.equalsIgnoreCase("void")){
				throw new Exception("Main method definition invalid: [public] [static] void main (String [] )");
			}
		}else if(!reg.valor.equalsIgnoreCase("void")){
			throw new Exception("Main method definition invalid: [public] [static] void main (String [] )");
		}
		reg = lista.remove(0);
		if(!reg.valor.equalsIgnoreCase("main")){
			throw new Exception("Main method definition invalid: [public] [static] void main (String [] )");
		}
		reg = lista.remove(0);
		if(!reg.valor.equalsIgnoreCase("(")){
			throw new Exception("( expected");
		}
		reg = lista.remove(0);
		if(!reg.valor.equalsIgnoreCase("string")){
			throw new Exception("String type expected");
		}
		reg = lista.remove(0);
		if(!reg.valor.equalsIgnoreCase("[")){
			throw new Exception("[ expected");
		}
		reg = lista.remove(0);
		if(!reg.valor.equalsIgnoreCase("]")){
			throw new Exception("] expected");
		}
		reg = lista.remove(0);
		if(!reg.tag.equalsIgnoreCase("Identificador")){
			throw new Exception("Parameter name expected");
		}
		reg = lista.remove(0);
		if(!reg.valor.equalsIgnoreCase(")")){
			throw new Exception(") expected");
		}
		reg = lista.remove(0);
		if(!reg.valor.equalsIgnoreCase("{")){
			throw new Exception("{ expected");
		}
		//Main Class definition
		Statement();
		reg = lista.remove(0);
		if(!reg.valor.equalsIgnoreCase("}")){
			throw new Exception("} expected");
		}
		reg = lista.remove(0);
		if(!reg.valor.equalsIgnoreCase("}")){
			throw new Exception("} expected");
		}
	}
	
	public static void Statement() throws Exception {
		Token reg = lista.remove(0);
		if(reg.tag.equals("Comentarios")){
			
		}else if(reg.valor.equalsIgnoreCase("{")){
			boolean statement = true;
			while(statement){
				Statement();
				if(lista.get(0).valor.equals("}")){
					statement = false;
				}
			}
			reg = lista.remove(0);
			if(!reg.valor.equalsIgnoreCase("}")){
				throw new Exception("} expected");
			}
		}else if(reg.valor.equalsIgnoreCase("IF")){
			reg = lista.remove(0);
			if(reg.valor.equals("(")){
				Expression();
				reg = lista.remove(0);
				if(reg.valor.equals(")")){
					Statement();
					if(lista.get(0).valor.equalsIgnoreCase("ELSE")){
						Statement();
					}
				}else{
					throw new Exception(") expected");
				}
			}else{
				throw new Exception("( expected");
			}
		}else if(reg.valor.equalsIgnoreCase("WHILE")){
			reg = lista.remove(0);
			if(reg.valor.equals("(")){
				Expression();
				reg = lista.remove(0);
				if(reg.valor.equals(")")){
					Statement();
				}else{
					throw new Exception(") expected");
				}
			}else{
				throw new Exception("( expected");
			}
		}else if(reg.valor.equalsIgnoreCase("System.out.println")){
			reg = lista.remove(0);
			if(reg.valor.equalsIgnoreCase("(")){
				Factor();
				reg = lista.remove(0);
				if(reg.valor.equalsIgnoreCase(")")){
					reg = lista.remove(0);
					if(!reg.valor.equals(";")){
						throw new Exception("; expected");
					}
				}else{
					throw new Exception(") expected");
				}
			}else{
				throw new Exception("( expected");
			}
		}else if(reg.tag.equalsIgnoreCase("Identificador")){
			reg = lista.remove(0);
			if(reg.valor.equals("=")){
				Factor();
				reg = lista.remove(0);
				if(!reg.valor.equals(";")){
					throw new Exception("; expected");
				}
			}else if(reg.valor.equals("[")){
				Factor();
				reg = lista.remove(0);
				if(reg.valor.equals("]")){
					reg = lista.remove(0);
					if(reg.valor.equals("=")){
						Factor();
						reg = lista.remove(0);
						if(!reg.valor.equals(";")){
							throw new Exception("; expected");
						}
					}else{
						throw new Exception("= expected");
					}
				}else{
					throw new Exception("] expected");
				}
			}else{
				throw new Exception("= or [ expected");
			}
		}else if(reg.valor.equalsIgnoreCase("int") || reg.valor.equalsIgnoreCase("boolean") || reg.valor.equalsIgnoreCase("String")){
			VarDeclaration(reg);
		}else{
			throw new Exception("Sintaxe incorreta, Statement expected");
		}
	}

	private static void VarDeclaration(Token reg) throws Exception {
		Type(reg);
		reg = lista.remove(0);
		if(!reg.tag.equalsIgnoreCase("Identificador")){
			throw new Exception("variable name expected");
		}
		reg = lista.remove(0);
		if(reg.valor.equals("=")){
			Factor();
			reg = lista.remove(0);
			if(!reg.valor.equals(";")){
				throw new Exception("; expected");
			}
		}else if(reg.valor.equals(";")){
			
		}else if(!reg.valor.equals(";")){
			throw new Exception("; expected");
		}else{
			throw new Exception("Sintaxe incorreta, Var Declaration expected");
		}
	}

	private static void Type(Token reg) throws Exception {
		if(reg.valor.equalsIgnoreCase("int")){
			reg = lista.get(0);
			if(reg.valor.equals("[")){
				reg = lista.remove(0);
				reg = lista.remove(0);
				if(!reg.valor.equals("]")){
					throw new Exception("] expected");
				}
			}else if(reg.tag.equals("Identificador")){
				
			}
		}else if(reg.valor.equalsIgnoreCase("boolean") || reg.valor.equalsIgnoreCase("String")){
			
		}else if(!reg.valor.equalsIgnoreCase("boolean") && !reg.valor.equalsIgnoreCase("String")){
			throw new Exception("variable type expected");
		}
	}

	private static void Expression() throws Exception {
		Factor();
		Token reg = lista.remove(0);
		if(OPERADORES_LOGICOS_MATEMATICOS.contains(reg.valor)){
			Factor();
		}else if(reg.valor.equals("[")){
			Factor();
			reg = lista.remove(0);
			if(!reg.valor.equals("]")){
				throw new Exception("] expected");
			}
		}else if(reg.valor.equals(".")){
			reg = lista.remove(0);
			if(!reg.valor.equals("length")){
				throw new Exception("reserved word length expected");
			}
		}
		
	}
	
	private static boolean Factor() throws Exception {
		Token reg = lista.remove(0);
		if(reg.tag.equalsIgnoreCase("Identificador")){
			return true;
		}else if(reg.valor.equalsIgnoreCase("true")){
			return true;
		}else if(reg.valor.equalsIgnoreCase("false")){
			return true;
		}else if(reg.valor.equalsIgnoreCase("new")){
			reg = lista.remove(0);
			if(reg.valor.equals("int")){
				reg = lista.remove(0);
				if(reg.valor.equals("[")){
					Factor();
					reg = lista.remove(0);
					if(!reg.valor.equals("]")){
						throw new Exception("] expected");
					}else{
						return true;
					}
				}else{
					throw new Exception("[ expected");
				}
			}else{
				throw new Exception("int type expected");
			}
		}else if(reg.valor.equals("!")){
			Factor();
			return true;
		}else if(reg.valor.equals("(")){
			Expression();
			reg = lista.remove(0);
			if(!reg.valor.equals(")")){
				throw new Exception(") expected");
			}else{
				return true;
			}
		}else if(reg.tag.equalsIgnoreCase("Numero Literal")){
			return true;
		}else if(reg.valor.equalsIgnoreCase('"'+"")){
			reg = lista.remove(0);
			if(reg.tag.equalsIgnoreCase("String Literal")){
				reg = lista.remove(0);
				if(!reg.valor.equals('"'+"")){
					throw new Exception('"'+" expected");
				}else{
					return true;
				}
			}
		}else{
			throw new Exception("Factor expected near "+reg.valor);
			
		}
		return false;
	}

	public static void main(String[] args) {
		AnalisadorLexico lex = new AnalisadorLexico();
		
		lista = lex.gerarListaTokens();
		initialize();
//		Transdutor.imprimirResultados();
		try {
			for (Token reg : lista) {
				if(reg.tag.equalsIgnoreCase("Nao pertence a gramatica")){
					throw new Exception("Codigo contem elementos que nao pertencem a gramatica");
				}
			}
			MainClass();
			System.out.println("Programa com sintaxe correta");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Sintaxe incorreta");
		}
	}

	private static void initialize() {
		OPERADORES_LOGICOS_MATEMATICOS = new ArrayList<String>();
		OPERADORES_LOGICOS_MATEMATICOS.add("+");
		OPERADORES_LOGICOS_MATEMATICOS.add("-");
		OPERADORES_LOGICOS_MATEMATICOS.add("/");
		OPERADORES_LOGICOS_MATEMATICOS.add("*");
		OPERADORES_LOGICOS_MATEMATICOS.add("<");
		OPERADORES_LOGICOS_MATEMATICOS.add(">");
		OPERADORES_LOGICOS_MATEMATICOS.add("&");
	}

}
