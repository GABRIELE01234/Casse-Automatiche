package com.casseautomatiche.gdo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.function.*;

/**
 * TUTTE LE FUNZIONALITA' SOTTOSTANTI
 * SONO ESEMPI GENERICI DI COME LE
 * LAMBDAS EXPRESSION E LE FUNCTIONAL INTERFACE
 * POSSONO ESSERE UTILIZZATE ED APPLICATE
 * IN DIVERSI CONTESTI CREANDO DI FATTI
 * RIUSABILITA' E ASTRAZIONE DEI METODI STESSI
 **/
@SpringBootApplication
public class GdoApplication {
	public static Random random = new Random();

	public static void main(String[] args) {
		SpringApplication.run(GdoApplication.class, args);




		// FIRST METHOD --> METODO CHE STAMPA UN ARRAY[] DI STRINGHE
		// SPLITTANDOLI PER " " (SPAZIO)
		Consumer<String> printWordsLambda = sentence -> {
			String[] parts = sentence.split(" ");
			Arrays.asList(parts).forEach(System.out::println);
		};


		System.out.println("-------------");
		System.out.println("*FIRST METHOD*");
		System.out.println("-------------");


		printWordsLambda.accept("QUESTA STRINGA SARA' INSERITA E SPLITTATA LADDOVE E' PRESENTE LO SPAZIO TRA DUE STRINGHE");

		// SECOND METHOD --> METODO COINCISO CHE STAMPA UN ARRAY[] DI STRINGHE
		// SPLITTANDOLI PER " " (SPAZIO)
		Consumer<String> printWordsLambdaCoincise = sentence -> {
			Arrays.asList(sentence.split(" ")).forEach(System.out::println);
		};
		System.out.println("-------------");
		System.out.println("*SECOND METHOD*");
		System.out.println("-------------");
		printWordsLambdaCoincise.accept("QUESTA STRINGA SARA INSERITA E SPLITTATA LADDOVE E' PRESENTE LO SPAZIO TRA DUE STRINGHE");

		// THIRD METHOD --> UTILIZZO DELL INTERFACIA FUNZIONALE
		// 'UnaryOperator<T>', CHE DATO IN INPUT UNA STRINGA DI CARATTERI
		// LA SCORRE E OGNI DUE POSIZIONI STAMPA IL CARATTERE RELTIVO
		System.out.println("-------------");
		System.out.println("*THIRD METHOD*");
		System.out.println("-------------");
		UnaryOperator <String> everySecondChar = source -> {
			StringBuilder returnVal = new StringBuilder();
			for (int i = 0; i < source.length(); i++) {
				if (i % 2 == 1){
					returnVal.append(source.charAt(i));
				}
			}
			return returnVal.toString();
		};
		System.out.println(everySecondChar.apply("1234567890"));

		// FOURTH METHOD --> UTILIZZO DELL INTERFACIA FUNZIONALE
		// 'UnaryOperator<T>', E DELL' INTERFACCIA 'Function<T,T>' CHE DATO IN INPUT UNA STRINGA DI CARATTERI
		// LA SCORRE E OGNI DUE POSIZIONI STAMPA IL CARATTERE RELTIVO
		System.out.println("-------------");
		System.out.println("*FOURTH METHOD*");
		System.out.println("-------------");
		System.out.println(esempioDiMetodoFunzionale("1234567890",everySecondChar));

		// FIFTH METHOD --> UTILIZZO DELLA LAMBDA EXPRESSION
		// PER ASSEGNARE UNA STRINGA AL SUPPLIER<>
		System.out.println("-------------");
		System.out.println("*FIFTH METHOD*");
		System.out.println("-------------");
		Supplier<String> iLoveJava = () -> "I LOVE JAVA PROGRAMMING";
		String supplierRes = iLoveJava.get();
		System.out.println(supplierRes);

		// SIXTH METHOD --> SETTA TUTTI I VALORI
		// PRECEDENTEMENTE ISTANZIATI ALL'INTRNO DELL'ARRAY
		// E LI STAMPA UTILIZZANDO UNA LAMBDA EXPRESSION (Es: 'array.forEach(System.out::println)' )
		System.out.println("-------------");
		System.out.println("--> TRANSFORM TO UPPERCASE");
		System.out.println("-------------");
		String[] arrayStringNames = {"Anna","Bob","Carole","David","Eduardo","Fred","Gary"};
		Arrays.setAll(arrayStringNames, i -> arrayStringNames[i].toUpperCase());
		Arrays.asList(arrayStringNames).forEach(System.out::println);


		List<String> backedByArray = Arrays.asList(arrayStringNames);
		backedByArray.replaceAll(s -> s += " " + getRandomChar('A','D')+ ".");
		System.out.println("-------------");
		System.out.println("--> ADD RANDOM MIDDLE INITIAL");
		System.out.println("-------------");
		backedByArray.forEach(System.out::println);

		backedByArray.replaceAll(s -> s += " " + getNamedReverse(s.split(" ")[0])+ ".");
		System.out.println("-------------");
		System.out.println("--> ADD REVERSED NAME AS LAST NAME");
		System.out.println("-------------");
		Arrays.asList(arrayStringNames).forEach(
				s -> System.out.println(functionForTransformer(s,Boolean.FALSE))
		);

		List<String> newList = new ArrayList<>(List.of(arrayStringNames));
		newList.removeIf(s -> s.substring(0, s.indexOf(" ")).equals(
				s.substring(s.lastIndexOf(" ") + 1)
		));
		System.out.println("-------------");
		System.out.println("--> REMOVE NAMES WHERE FIRST = LAST");
		System.out.println("-------------");
		newList.forEach(System.out::println);




		String name = "GABRIELE";
		Function<String,String> uCase = String::toUpperCase;
		System.out.println(uCase.apply(name));

		Function<String,String> lastname = s -> s.concat(" GALLIFUOCO");
		Function<String,String> uCaseLastName = uCase.andThen(lastname);
		System.out.println(uCaseLastName.apply(name));

		uCaseLastName = uCase.compose(lastname);
		System.out.println(uCaseLastName.apply(name));

		Function<String,String[]> f0 = uCase
				.andThen(s -> s.concat(" GALLIFUOCO"))
				.andThen(s -> s.split(" "));
		System.out.println(Arrays.toString(f0.apply(name)));

		Function<String,String> f1 = uCase
				.andThen(s -> s.concat(" GALLIFUOCO"))
				.andThen(s -> s.split(" "))
				.andThen(s -> s[1].toUpperCase() + ", "+ s[0]);
		System.out.println(f1.apply(name));

		Function<String,Integer> f2 = uCase
				.andThen(s -> s.concat(" GALLIFUOCO"))
				.andThen(s -> s.split(" "))
				.andThen(s -> String.join(",",s))
				.andThen(String::length);
		System.out.println(f2.apply(name));

		Predicate<String> p1 = s -> s.equals("GABRIELE");
		Predicate<String> p2 = s -> s.equalsIgnoreCase("Gabriele");
		Predicate<String> p3 = s -> s.startsWith("G");
		Predicate<String> p4 = s -> s.endsWith("E");

		Predicate<String> combined1 = p1.or(p2);
		System.out.println("1- COMBINED: "+combined1.test(name));

		Predicate<String> combined2 = p3.and(p4);
		System.out.println("2- COMBINED: "+combined2.test(name));

		Predicate<String> combined3 = p3.and(p4).negate();
		System.out.println("2- COMBINED: "+combined3.test(name));


	}
	public static String esempioDiMetodoFunzionale(String o, UnaryOperator<String> operator){
		return operator.apply(o);
	}
	public static char getRandomChar(char startChar,char endChar){
		return (char) random.nextInt((int) startChar, (int) endChar);
	}
	public static String getNamedReverse(String firstname){
		return new StringBuilder(firstname).reverse().toString();
	}
	public static Object functionForTransformer(String stringa,Object tipoDaTrasformare){
		Function <String,Object> functionForTransformer = String::isEmpty;
		tipoDaTrasformare = stringa.transform(functionForTransformer);
		return tipoDaTrasformare;
	}
}
