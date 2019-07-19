import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MenuClass {

	public static void main(String[] args) {
		
		System.out.println("Welcome to my Cypher!!!\n\n");
		Scanner userInput = new Scanner(System.in);
		
		Cryptograph cypher = null;
		int cypherChoice = 0;
		
		do {
			
			System.out.print("Do you want load the cypher in 'Encoder.txt'? (Yes = 1, No = 0): ");
			cypherChoice = userInput.nextInt();
	
			if (cypherChoice == 0) {
				cypher = new Cryptograph();
			}
			else if (cypherChoice == 1) {
				cypher = new Cryptograph("Encoder.txt");
			}
			else {
				System.out.print("Invalid input, try again. ");
			}
			
		} while ((cypherChoice < 0) || (cypherChoice > 1));
		
		int userChoice = 4;
		
		do {
		
			do {
				if ((userChoice < 1) || (userChoice > 4)) {
					System.out.print("\n\nInvalid Input. ");
				}
			
				System.out.println("Choose what you want to do:"
						+ "\n    1 - Encrypt a Text File"
						+ "\n    2 - Decrypt a Text File"
						+ "\n    3 - Encrypt and Decrypt an input"
						+ "\n    4 - Exit the Program");
				
				userChoice = userInput.nextInt();
				userInput.nextLine();
			
			} while ((userChoice < 1) || (userChoice > 4));
	
			
			switch(userChoice) {
			
			case 1:
			case 2:
				
				boolean foundFile = true, createFile = true;;
				File outputFile = new File("Save_File.txt");
				PrintWriter output = null;
				
				try {
					output = new PrintWriter(outputFile);
				}
				catch (FileNotFoundException ex) {
					System.out.println("Couldn't create file to save your message.\n\n");
					createFile = false;
				}
				
				System.out.print("Name of Text File: ");
				String fileName = userInput.nextLine();
				
				File inputFile = new File(fileName);
				Scanner input = null;	
				
				try {
					input = new Scanner(inputFile);
				}
				catch (FileNotFoundException ex){
					
					System.out.print("Couldn't find the file '" + fileName + "'.\n\n");
					foundFile = false;
				}
				
				if (foundFile) {
				
					if (userChoice == 1) {
						
						System.out.print("\nThe encoded text file:\n\n");
						
						while (input.hasNextLine()) {
							
							String encoded = encodeLine(input.nextLine(), cypher);
							
							System.out.println(encoded);
							
							if (createFile) {
								output.println(encoded);
							}
						}
					}
					
					else {
						
						System.out.print("\nThe decoded text file:\n\n");
						String wholeFile = "";
						int lineNum = 0;
						
						while (input.hasNextLine()) {
							wholeFile = input.nextLine() + "\n" + wholeFile;
							lineNum ++;
						}
						
						String oneLine = "", answer = "";
						
						for (int f = 0; f < lineNum; f ++) {
							
							int endPos = wholeFile.indexOf("\n");
							
							if (endPos >= 0) {
								
								oneLine = wholeFile.substring(0, endPos);
								wholeFile = wholeFile.substring(endPos + 1);
								
							}
							
							else {
								oneLine = wholeFile;
								wholeFile = "";
							}
							
							answer = decodeLine(oneLine, cypher) + "\n" + answer;
						}
						
						System.out.print(answer);
						
						if (createFile) {
							output.println(answer);
						}
					}
				}	
				
				output.close();
				break;
				
			case 3:
				
				System.out.print("What do you want to encrypt: ");
				String line = userInput.nextLine();
				
				line = encodeLine(line, cypher);
				System.out.println("   The Encoded line: " + line);
				
				line = decodeLine(line, cypher);
				System.out.print("   The Decoded line: " + line + "\n\n\n");
				break;
				
			}
			
		} while (userChoice != 4);	
		
		System.out.print("\n\nThanks for using the program.");
		userInput.close();
	}
	
	public static String encodeLine(String line, Cryptograph cypher) {
		line  = line.toUpperCase();
		String lineEncoded = "";
		
		for (int q = 0; q < line.length(); q ++) {
			char thisChar = line.charAt(q);
			
			if (Character.isLetterOrDigit(thisChar)) {
				thisChar = cypher.getEncrypted(thisChar);
				cypher.rotateCW();
			}
			
			lineEncoded += thisChar;
		}
		
		return lineEncoded;
	}
	
	public static String decodeLine(String line, Cryptograph cypher) {
		line = line.toUpperCase();
		String lineDecoded = "";
		
		for (int t = line.length() - 1; t >= 0; t --) {
			char thisChar = line.charAt(t);
			
			if (Character.isLetterOrDigit(thisChar)) {
				thisChar = cypher.getDecrypted(thisChar);
			}
			
			lineDecoded = thisChar + lineDecoded;
		}
		
		return lineDecoded;
	}
}
