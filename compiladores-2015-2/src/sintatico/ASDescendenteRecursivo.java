package sintatico;

import model.*;

import java.util.ArrayList;
import java.util.List;

import lexico.AnalisadorLexico;;


public class ASDescendenteRecursivo {
	
	private static List<Token> listaTokens = null;
	private static List<String> OPERADORES = null;
	
	
	public static void MainClass() throws Exception{
		
		Token reg = listaTokens.remove(0);
		if(!reg.valor.equalsIgnoreCase("class")){
			throw new Exception("palavra reservada CLASS esperada");
		}
		reg = listaTokens.remove(0);
		if(!reg.tag.equalsIgnoreCase("Identificador")){
			throw new Exception("CLASS esperado");
		}
		reg = listaTokens.remove(0);
		if(!reg.valor.equalsIgnoreCase("{")){
			throw new Exception("{ esperado");
		}
		reg = listaTokens.remove(0);
		
		if(reg.valor.equalsIgnoreCase("public")){
			reg = listaTokens.remove(0);
			if(reg.valor.equalsIgnoreCase("static")){
				reg = listaTokens.remove(0);
				if(!reg.valor.equalsIgnoreCase("void")){
					throw new Exception("Definicao do metodo Main invalido: [public] [static] void main (String [] )");
				}
			}
		}else if(reg.valor.equalsIgnoreCase("static")){
			reg = listaTokens.remove(0);
			if(!reg.valor.equalsIgnoreCase("void")){
				throw new Exception("Definicao do metodo Main invalido: [public] [static] void main (String [] )");
			}
		}else if(!reg.valor.equalsIgnoreCase("void")){
			throw new Exception("Definicao do metodo Main invalido: [public] [static] void main (String [] )");
		}
		reg = listaTokens.remove(0);
		if(!reg.valor.equalsIgnoreCase("main")){
			throw new Exception("Definicao do metodo Main invalido: [public] [static] void main (String [] )");
		}
		reg = listaTokens.remove(0);
		if(!reg.valor.equalsIgnoreCase("(")){
			throw new Exception("( esperado");
		}
		reg = listaTokens.remove(0);
		if(!reg.valor.equalsIgnoreCase("string")){
			throw new Exception("tipo String esperado");
		}
		reg = listaTokens.remove(0);
		if(!reg.valor.equalsIgnoreCase("[")){
			throw new Exception("[ esperado");
		}
		reg = listaTokens.remove(0);
		if(!reg.valor.equalsIgnoreCase("]")){
			throw new Exception("] esperado");
		}
		reg = listaTokens.remove(0);
		if(!reg.tag.equalsIgnoreCase("Identificador")){
			throw new Exception("Parameter esperado");
		}
		reg = listaTokens.remove(0);
		if(!reg.valor.equalsIgnoreCase(")")){
			throw new Exception(") esperado");
		}
		reg = listaTokens.remove(0);
		if(!reg.valor.equalsIgnoreCase("{")){
			throw new Exception("{ esperado");
		}
		//Main Class
		
		Statement();
		reg = listaTokens.remove(0);
		if(!reg.valor.equalsIgnoreCase("}")){
			throw new Exception("} esperado");
		}
		reg = listaTokens.remove(0);
		if(!reg.valor.equalsIgnoreCase("}")){
			throw new Exception("} esperado");
		}
	}
	
	public static void Statement() throws Exception {
		Token reg = listaTokens.remove(0);
		if(reg.tag.equals("Comentarios")){
			
		}else if(reg.valor.equalsIgnoreCase("{")){
			boolean statement = true;
			while(statement){
				Statement();
				if(listaTokens.get(0).valor.equals("}")){
					statement = false;
				}
			}
			reg = listaTokens.remove(0);
			if(!reg.valor.equalsIgnoreCase("}")){
				throw new Exception("} esperado");
			}
		}else if(reg.valor.equalsIgnoreCase("IF")){
			reg = listaTokens.remove(0);
			if(reg.valor.equals("(")){
				Expression();
				reg = listaTokens.remove(0);
				if(reg.valor.equals(")")){
					Statement();
					if(listaTokens.get(0).valor.equalsIgnoreCase("ELSE")){
						Statement();
					}
				}else{
					throw new Exception(") esperado");
				}
			}else{
				throw new Exception("( esperado");
			}
		}else if(reg.valor.equalsIgnoreCase("WHILE")){
			reg = listaTokens.remove(0);
			if(reg.valor.equals("(")){
				Expression();
				reg = listaTokens.remove(0);
				if(reg.valor.equals(")")){
					Statement();
				}else{
					throw new Exception(") esperado");
				}
			}else{
				throw new Exception("( esperado");
			}
		}else if(reg.valor.equalsIgnoreCase("System.out.println")){
			reg = listaTokens.remove(0);
			if(reg.valor.equalsIgnoreCase("(")){
				Factor();
				reg = listaTokens.remove(0);
				if(reg.valor.equalsIgnoreCase(")")){
					reg = listaTokens.remove(0);
					if(!reg.valor.equals(";")){
						throw new Exception("; esperado");
					}
				}else{
					throw new Exception(") esperado");
				}
			}else{
				throw new Exception("( esperado");
			}
		}else if(reg.tag.equalsIgnoreCase("Identificador")){
			reg = listaTokens.remove(0);
			if(reg.valor.equals("=")){
				Factor();
				reg = listaTokens.remove(0);
				if(!reg.valor.equals(";")){
					throw new Exception("; esperado");
				}
			}else if(reg.valor.equals("[")){
				Factor();
				reg = listaTokens.remove(0);
				if(reg.valor.equals("]")){
					reg = listaTokens.remove(0);
					if(reg.valor.equals("=")){
						Factor();
						reg = listaTokens.remove(0);
						if(!reg.valor.equals(";")){
							throw new Exception("; esperado");
						}
					}else{
						throw new Exception("= esperado");
					}
				}else{
					throw new Exception("] esperado");
				}
			}else{
				throw new Exception("= or [ esperado");
			}
		}else if(reg.valor.equalsIgnoreCase("int") || reg.valor.equalsIgnoreCase("boolean") || reg.valor.equalsIgnoreCase("String")){
			VarDeclaration(reg);
		}else{
			throw new Exception("Sintaxe incorreta, Statement esperado");
		}
	}

	private static void VarDeclaration(Token reg) throws Exception {
		Type(reg);
		reg = listaTokens.remove(0);
		if(!reg.tag.equalsIgnoreCase("Identificador")){
			throw new Exception("variable esperada");
		}
		reg = listaTokens.remove(0);
		if(reg.valor.equals("=")){
			Factor();
			reg = listaTokens.remove(0);
			if(!reg.valor.equals(";")){
				throw new Exception("; esperado");
			}
		}else if(reg.valor.equals(";")){
			
		}else if(!reg.valor.equals(";")){
			throw new Exception("; esperado");
		}else{
			throw new Exception("Sintaxe incorreta, Var Declaration esperado");
		}
	}

	private static void Type(Token reg) throws Exception {
		if(reg.valor.equalsIgnoreCase("int")){
			reg = listaTokens.get(0);
			if(reg.valor.equals("[")){
				reg = listaTokens.remove(0);
				reg = listaTokens.remove(0);
				if(!reg.valor.equals("]")){
					throw new Exception("] esperado");
				}
			}else if(reg.tag.equals("Identificador")){
				
			}
		}else if(reg.valor.equalsIgnoreCase("boolean") || reg.valor.equalsIgnoreCase("String")){
			
		}else if(!reg.valor.equalsIgnoreCase("boolean") && !reg.valor.equalsIgnoreCase("String")){
			throw new Exception("tipo variable esperado");
		}
	}

	private static void Expression() throws Exception {
		Factor();
		Token reg = listaTokens.remove(0);
		if(OPERADORES.contains(reg.valor)){
			Factor();
		}else if(reg.valor.equals("[")){
			Factor();
			reg = listaTokens.remove(0);
			if(!reg.valor.equals("]")){
				throw new Exception("] esperado");
			}
		}else if(reg.valor.equals(".")){
			reg = listaTokens.remove(0);
			if(!reg.valor.equals("length")){
				throw new Exception("palavra reservada length esperada");
			}
		}
		
	}
	
	private static boolean Factor() throws Exception {
		Token reg = listaTokens.remove(0);
		if(reg.tag.equalsIgnoreCase("Identificador")){
			return true;
		}else if(reg.valor.equalsIgnoreCase("true")){
			return true;
		}else if(reg.valor.equalsIgnoreCase("false")){
			return true;
		}else if(reg.valor.equalsIgnoreCase("new")){
			reg = listaTokens.remove(0);
			if(reg.valor.equals("int")){
				reg = listaTokens.remove(0);
				if(reg.valor.equals("[")){
					Factor();
					reg = listaTokens.remove(0);
					if(!reg.valor.equals("]")){
						throw new Exception("] esperado");
					}else{
						return true;
					}
				}else{
					throw new Exception("[ esperado");
				}
			}else{
				throw new Exception("tipo int esperado");
			}
		}else if(reg.valor.equals("!")){
			Factor();
			return true;
		}else if(reg.valor.equals("(")){
			Expression();
			reg = listaTokens.remove(0);
			if(!reg.valor.equals(")")){
				throw new Exception(") esperado");
			}else{
				return true;
			}
		}else if(reg.tag.equalsIgnoreCase("Numero Literal")){
			return true;
		}else if(reg.valor.equalsIgnoreCase('"'+"")){
			reg = listaTokens.remove(0);
			if(reg.tag.equalsIgnoreCase("String Literal")){
				reg = listaTokens.remove(0);
				if(!reg.valor.equals('"'+"")){
					throw new Exception('"'+" esperado");
				}else{
					return true;
				}
			}
		}else{
			throw new Exception("Factor esperado perto de "+reg.valor);
			
		}
		return false;
	}

	public static void main(String[] args) {
		AnalisadorLexico aLexico = new AnalisadorLexico();
		
		listaTokens = aLexico.gerarListaTokens();
		inicializarOperadores();
		
		try {
			for (Token tok : listaTokens) {
				if(tok.tag.equalsIgnoreCase("Nao pertence a gramatica")){
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

	private static void inicializarOperadores() {
		OPERADORES = new ArrayList<String>();
		OPERADORES.add("+");
		OPERADORES.add("-");
		OPERADORES.add("/");
		OPERADORES.add("*");
		OPERADORES.add("<");
		OPERADORES.add(">");
		OPERADORES.add("&");
	}

}
