import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Cryptograph {
	
	private char[] outer = new char [36];
	private char[] middle = new char [36];
	private char[] inner = new char [36];
	
	private int rotationCounter = 0, middleCount = 0;
	
	
	public Cryptograph() {
		
		int count = 0;
		for (int a = 48; a <= 90; a ++) {
			
			char addChar = (char) a;
			if (Character.isLetterOrDigit(addChar)) {
				
				outer[count] = addChar;
				middle[count] = addChar;
				inner[count] = addChar;
				count ++;
			}
		}
		
		outer = shuffle(outer);
		middle = shuffle(middle);
		inner = shuffle(inner);
		
		storeCypher();
	}
	
	
	
	public Cryptograph(String fileName) {
		
		File inputFile = new File(fileName);
		Scanner input = null;	
		boolean foundFile = true;
		
		try {
			input = new Scanner(inputFile);
		}
		catch (FileNotFoundException ex){
			
			System.out.print("Couldn't find the file '" + fileName + "'.\n\n");
			foundFile = false;
		}
		
		if (foundFile) {
			
			if (input.hasNextLine()) {
				int count = 0;
				String line = input.nextLine();
				
				for (int n = 0; n < line.length(); n ++) {
					if (Character.isLetterOrDigit(line.charAt(n))) {
						outer[count] = line.charAt(n);
						count ++;
					}
				}
			}
			
			if (input.hasNextLine()) {
				int count = 0;
				String line = input.nextLine();
				
				for (int n = 0; n < line.length(); n ++) {
					if (Character.isLetterOrDigit(line.charAt(n))) {
						middle[count] = line.charAt(n);
						count ++;
					}
				}
				
			}
			
			if (input.hasNextLine()) {
				int count = 0;
				String line = input.nextLine();
				
				for (int n = 0; n < line.length(); n ++) {
					if (Character.isLetterOrDigit(line.charAt(n))) {
						inner[count] = line.charAt(n);
						count ++;
					}
				}	
			}
		}
	}
	
	
	
	public char getEncrypted(char encryptThis) {
		
		int index = getIndexOfChar(encryptThis, inner);
		index = getIndexOfChar(outer[index], middle);
			
		rotateCW();
			
		return outer[index];
	}

	
	
	public char getDecrypted(char decryptThis) {
		rotateCCW();
		
		int index = getIndexOfChar(decryptThis, outer);	
		index = getIndexOfChar(middle[index], outer);
		
		return inner[index];
	}
	
	
	
	public void rotateCW() {
		
		rotationCounter ++;
		inner = rotateCWHelper(inner);
			
		if ((rotationCounter > 0) && (rotationCounter % 36 == 0)) {
			middle = rotateCWHelper(middle);
			middleCount ++;
			
			if (middleCount % 36 == 0) {
				outer = rotateCWHelper(outer);
			}
		}
	}
	
	
	
	public void rotateCCW() {
		
		rotationCounter --;
		inner = rotateCCWHelper(inner);
		
		if ((rotationCounter != 0) && ((rotationCounter + 1) % 36 == 0)) {
			middle = rotateCCWHelper(middle);
			middleCount --;
			
			if ((middleCount + 1) % 36 == 0) {
				outer = rotateCCWHelper(outer);
			}
		}
	}
	
	
	
	public char[] rotateCWHelper (char[] rotateThis) {
		char storage = rotateThis[rotateThis.length - 1];
		
		for (int s = rotateThis.length - 1; s > 0; s --) {
			rotateThis[s] = rotateThis[s - 1];
		}
		
		rotateThis[0] = storage;
		return rotateThis;
	}
	
	
	
	public char[] rotateCCWHelper (char[] rotateThis) {
		char storage = rotateThis[0];
		
		for (int r = 0; r < rotateThis.length - 1; r ++) {
			rotateThis[r] = rotateThis[r + 1];
		}
		
		rotateThis[rotateThis.length - 1] = storage;	
		return rotateThis;
	}
	
	
	
	// NOTE: this is the Selection Shuffle method from Lab 12
	public char[] shuffle(char [] shuffleThis) {
		Random Generator = new Random();
			
		for (int d = shuffleThis.length - 1; d > 0; d --) {
			int randomInt = Generator.nextInt(d);
				
			char temp = shuffleThis[d];
			shuffleThis[d] = shuffleThis[randomInt];
			shuffleThis[randomInt] = temp;
		}
		
		return shuffleThis;
	}	
	
	
	
	public void storeCypher() {
		
		boolean createFile = true;
		File outputFile = new File("Encoder.txt");
		PrintWriter output = null;
		
		try {
			output = new PrintWriter(outputFile);
		}
		catch (FileNotFoundException ex) {
			System.out.println("Couldn't create file to save the Encoder.");
			createFile = false;
		}
		
		if (createFile) {
			
			for (char oneChar : outer) {
				output.print(oneChar + " ");
			}
			output.println();
			
			for (char oneChar : middle) {
				output.print(oneChar + " ");
			}
			output.println();
			
			for (char oneChar : inner) {
				output.print(oneChar + " ");
			}
			
		}
		
		output.close();
	}
	
	
	
	public int getIndexOfChar(char findThis, char[] findItIn) {
		for (int w = 0; w < findItIn.length; w ++) {
			
			if (findItIn[w] == findThis) {
				return w;
			}
		}
		
		return -1;
	}
}