package lexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFileChooser;

import model.Tag;
import model.Token;

public class AnalisadorLexico {

	public static ArrayList<Token> listaTokens = new ArrayList<Token>();
	public static HashMap<String, Boolean> RESERVADO = new HashMap<String, Boolean>();
	public static HashMap<String, Boolean> OPERADORES = new HashMap<String, Boolean>();

	static String Identifier = "[A-Za-z]{1}(_*[0-9]*[A-Za-z]*)*";
	static String NumberLiteral = "[0-9]+";

	public static int line = 1;

	 public AnalisadorLexico() {
		 inicializarHashMaps();
	 }

	public static void inicializarHashMaps() {
		// PALAVRAS RESERVADAS
		RESERVADO.put("if", true);
		RESERVADO.put("else", true);
		RESERVADO.put("while", true);
		RESERVADO.put("this", true);
		RESERVADO.put("class", true);
		RESERVADO.put("public", true);
		RESERVADO.put("static", true);
		RESERVADO.put("void", true);
		RESERVADO.put("main", true);
		RESERVADO.put("String", true);
		RESERVADO.put("new", true);
		RESERVADO.put("int", true);
		RESERVADO.put("boolean", true);
		RESERVADO.put("true", true);
		RESERVADO.put("false", true);
		RESERVADO.put("System.out.println", true);
		RESERVADO.put("length", true);
		RESERVADO.put("input", true);

		// OPERADORES
		OPERADORES.put("!", true);
		OPERADORES.put("+", true);
		OPERADORES.put("-", true);
		OPERADORES.put("/", true);
		OPERADORES.put("*", true);
		OPERADORES.put("<", true);
		OPERADORES.put(">", true);
		OPERADORES.put("=", true);
		OPERADORES.put("&", true);
		OPERADORES.put("|", true);
		OPERADORES.put(".", true);
		OPERADORES.put(",", true);
		OPERADORES.put("{", true);
		OPERADORES.put("}", true);
		OPERADORES.put("(", true);
		OPERADORES.put(")", true);
		OPERADORES.put("[", true);
		OPERADORES.put("]", true);
		OPERADORES.put(":", true);
		OPERADORES.put(";", true);
	}

//	public static void main(String[] args) {
//
//		imprimirTokens(gerarListaTokens());
//
//	}

	public static ArrayList<Token> gerarListaTokens() {

		inicializarHashMaps(); // Inicialização de palavras reservadas e
								// operadores.

		File file = carregarArquivo(); // Carregamento do arquivo.

		try {
			String entrada = "";
			BufferedReader br = null;
			if (file != null) {
				br = new BufferedReader(new FileReader(file));
			} else {
				throw new FileNotFoundException("Arquivo invalido.");
			}
			while ((entrada = br.readLine()) != null) {
				int entradaPos = 0;
				String buffer = "";
				int estado = 1;
				boolean done = false;
				char peek = ' ';

				while (!done) {
					if (entrada != null && entradaPos < entrada.length()) {
						peek = entrada.charAt(entradaPos);
					} else {
						done = true;
						estado = -1;
					}
					switch (estado) {
					case 1:
						if (peek == '/') {
							if (entrada.charAt(entradaPos + 1) == '/' || entrada.charAt(entradaPos + 1) == '*') {
								estado = 5;
								break;
							}
						} else if (OPERADORES.containsKey(String.valueOf(peek))) {
							listaTokens.add(new Token(Tag.OPER, String.valueOf(peek), String.valueOf(line)));
							entradaPos++;
						} else if (Character.isLetter(peek)) {
							estado = 2;
						} else if (Character.isDigit(peek)) {
							estado = 3;
						} else if (peek == '"') {
							estado = 4;
							listaTokens.add(new Token(Tag.OPER, String.valueOf(peek), String.valueOf(line)));
							entradaPos++;
						} else if (Character.isWhitespace(peek)) {
							estado = 1;
							entradaPos++;
						} else {

							listaTokens.add(new Token(Tag.INV, "" + peek, String.valueOf(line)));
							entradaPos++;
						}
						break;
					case 2:
						while ((Character.isLetter(peek) || Character.isDigit(peek) || peek == '_')) {
							buffer = buffer.concat(String.valueOf(peek));
							entradaPos++;
							if (entradaPos < entrada.length()) {
								peek = entrada.charAt(entradaPos);
							} else {
								peek = ' ';
							}
						}

						if (buffer.equalsIgnoreCase("System") && entrada.charAt(entradaPos) == '.') {
							while ((Character.isLetter(peek) || peek == '.')) {
								buffer = buffer.concat(String.valueOf(peek));
								entradaPos++;
								if (entradaPos < entrada.length()) {
									peek = entrada.charAt(entradaPos);
								} else {
									peek = ' ';
								}
							}
						}

						if (RESERVADO.containsKey(buffer)) {
							listaTokens.add(new Token(Tag.PALRES, buffer, String.valueOf(line)));
						} else if (buffer.matches(Identifier)) {
							listaTokens.add(new Token(Tag.ID, buffer, String.valueOf(line)));
						} else {
							listaTokens.add(new Token(Tag.INV, buffer, String.valueOf(line)));
						}
						buffer = "";
						estado = 1;
						break;
					case 3:
						while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_') {
							buffer = buffer.concat(String.valueOf(peek));
							entradaPos++;
							peek = entrada.charAt(entradaPos);
						}
						if (buffer.matches(NumberLiteral)) {
							listaTokens.add(new Token(Tag.NUMLIT, buffer, String.valueOf(line)));
						} else {
							listaTokens.add(new Token(Tag.INV, buffer, String.valueOf(line)));
						}
						buffer = "";
						estado = 1;
						break;
					case 4:
						boolean notFinished = false;
						while (peek != '"') {
							if (peek != '"') {
								buffer = buffer.concat(String.valueOf(peek));
							}
							entradaPos++;
							if (entradaPos < entrada.length()) {
								peek = entrada.charAt(entradaPos);
							} else {
								peek = '"';
								notFinished = true;
							}
						}

						listaTokens.add(new Token(Tag.STRLIT, buffer, String.valueOf(line)));
						if (!notFinished) {
							listaTokens.add(new Token(Tag.OPER, peek + "", String.valueOf(line)));
						}
						buffer = "";
						estado = 1;
						entradaPos++;
						break;
					case 5:
						if (entrada.charAt(entradaPos + 1) == '/') {
							while (entradaPos < entrada.length()) {
								peek = entrada.charAt(entradaPos);
								buffer = buffer.concat(String.valueOf(peek));
								entradaPos++;
							}
							listaTokens.add(new Token(Tag.COMENT, buffer, String.valueOf(line)));
						} else if (entrada.charAt(entradaPos + 1) == '*') {
							boolean endComment = false;
							while (!endComment) {
								while (entradaPos < entrada.length()) {
									peek = entrada.charAt(entradaPos);
									buffer = buffer.concat(String.valueOf(peek));
									if (peek == '*' && entradaPos < entrada.length()) {
										if (entrada.charAt(entradaPos + 1) == '/') {
											endComment = true;
											buffer = buffer.concat(String.valueOf('/'));
											listaTokens.add(new Token(Tag.COMENT, buffer, String.valueOf(line)));
											estado = 0;
											break;
										}
									}
									entradaPos++;
								}
								entrada = br.readLine();
								entradaPos = 0;
								if (entrada == null) {
									endComment = true;
									listaTokens.add(new Token(Tag.INV, buffer, String.valueOf(line)));
									estado = 0;
									break;
								}
							}
						}
					default:
						break;
					}
				}

				line = line + 1;
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listaTokens;
	}

	public static File carregarArquivo() {
//		JFileChooser fileChooser = new JFileChooser();
		JFileChooser fileChooser = new JFileChooser(
				"D:\\LUCAS\\ESTUDOS\\trabalho-compiladores-2015-2\\compiladores-2015-2\\src\\static");
		fileChooser.showOpenDialog(null);
		return fileChooser.getSelectedFile();
	}

	public static void imprimirTokens(ArrayList<Token> lista) {
		for (Token tok : lista) {
			System.out.println(tok.tag + " - " + tok.valor + " - " + tok.linha);
		}
	}

}
