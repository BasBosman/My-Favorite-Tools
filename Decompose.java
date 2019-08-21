import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

class Character {
	class Alias {
		String displayName;
		String hiddenName;
	}

	int id;
	String name;
	int adress;
	ArrayList<String> alias = new ArrayList<>();
	
	Character(int adress) {
		this.adress = adress;
	}
}

class MiscData {
	int opLength;
}

class CodeSnippet {
	int adress;
	ArrayList<String> strings = new ArrayList<>();

	CodeSnippet(int adress) {
		this.adress = adress;
	}

	int length;
	
}

class Sentence {
	String content;
	int speaker;
	int voiceLine;
	
	Sentence(String content) {
		this.content = content;
	}
	
}

class Settings {
	int startPoint;
	int lineAdress1;
	int lineAdress2;
}

class NonsenseException extends RuntimeException {
	//error should be never thrown but can be used to create unreachable catch blocks for testing purposes
}

public class Decompose {
	public static void main(String[] args) {
		BufferedWriter writer;
		MiscData miscData = new MiscData();
		ArrayList<CodeSnippet> codeSnippets = new ArrayList<>();

		ArrayList<Byte> bytes = new ArrayList<>();

		File fileDir = new File("D:\\tools\\myfavtools\\out.txt");
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "SJIS"))) {

			String str = in.readLine();
			String[] tokens = str.split("=");
			miscData.opLength = Integer.parseInt(tokens[1]);
			Settings set = new Settings();
			
			loop: while ((str = in.readLine()) != null) {
				try {
					tokens = str.split(",");
					// if opcode is initStack create a new snippet with adress
					// else add the line to the last snippet
					if (tokens[1].equals("initStack")) {
						codeSnippets.add(new CodeSnippet(Integer.parseInt(tokens[0])));
					}
					codeSnippets.get(codeSnippets.size() - 1).strings.add(str);
					codeSnippets.get(codeSnippets.size() - 1).length += 1;
				} catch (ArrayIndexOutOfBoundsException e) {
					// end of instructs reached
					break loop;
				}
			}

			codeSnippets.add(new CodeSnippet(Integer.MAX_VALUE));
			while ((str = in.readLine()) != null) {
				codeSnippets.get(codeSnippets.size() - 1).strings.add(str);
			}

			//get startpoint
			str = codeSnippets.get(codeSnippets.size() - 1).strings.get(0);
			tokens = str.split("=");
			set.startPoint = Integer.parseInt(tokens[1]);
			
			//get all spoken lines
			ArrayList<Sentence> sentences = new ArrayList<>();
			for (CodeSnippet snippet : codeSnippets) {
				for (int i = 0; i < snippet.strings.size(); i++) {
					tokens = snippet.strings.get(i).split(",");
					try {
						if (tokens[1].equals("pushString") 
								&& snippet.strings.get(i+1).split(",")[1].equals("push0")
								&& snippet.strings.get(i+2).split(",")[1].equals("push0")
								&& snippet.strings.get(i+3).split(",")[1].equals("push0")) {
							Sentence newSent = new Sentence(tokens[2]);
							snippet.strings.remove(i + 1);
							snippet.strings.remove(i + 1);
							snippet.strings.remove(i + 1);

							//check if there is a speaker
							if (snippet.strings.get(i-1).split(",")[1].equals("call")) {
								newSent.speaker = Integer.parseInt(snippet.strings.get(i-1).split(",")[2]);
								snippet.strings.remove(i - 1);
								//check if there is a voiceline
								if (snippet.strings.get(i-4).split(",")[1].equals("pushInt")
										&& snippet.strings.get(i-3).split(",")[1].equals("pushByte")
										&& snippet.strings.get(i-2).split(",")[1].equals("neg")
										&& snippet.strings.get(i-1).split(",")[1].equals("push0")) {
									newSent.voiceLine = Integer.parseInt(snippet.strings.get(i-4).split(",")[2]);
									snippet.strings.remove(i - 1);
									snippet.strings.remove(i - 1);
									snippet.strings.remove(i - 1);
									snippet.strings.remove(i - 1);

								} else {
									newSent.voiceLine = 0;
								}
							} else {
								newSent.speaker = 0;
								newSent.voiceLine = 0;
							}
							snippet.strings.add(i, tokens[0] + ",speakLine," + tokens[2] + "," + newSent.speaker + "," + newSent.voiceLine);
							sentences.add(newSent);
							snippet.strings.remove(i - 1);
						}
					} catch(ArrayIndexOutOfBoundsException e) {
						//in this case the string is probably empty
					}
				}
			}
			
			//Get characters
			//first get a list of all possible characters	
			ArrayList<Character> chars = new ArrayList<>();
			for (Sentence sen : sentences) {
				if (sen.speaker != 0) {
					boolean found = false;
					for (Character x : chars) {
						if (x.adress == sen.speaker) {
							found = true;
							break;
						}
					}
					if (!found) {
						chars.add(new Character(sen.speaker));
					}
				}
			}
			
			int pos = 1;
			
			//for every character get the data
			for (int i = 0; i < chars.size();) {
				try {
					//find the right snippet for the character
					for (CodeSnippet snippet: codeSnippets) {
						if (snippet.adress == chars.get(i).adress) {
							//do something
							chars.get(i).id = Integer.parseInt(snippet.strings.get(1).split(",")[2]);
							
							//delete the snippet
							i++;
							codeSnippets.remove(snippet);
							break;
						}
					}
				} catch (ArrayIndexOutOfBoundsException | NonsenseException e) {
					//not a valid character, remove from list
					chars.remove(i);
				}
			}
			
			// write every snippet to a file
			for (CodeSnippet snippet : codeSnippets) {
						
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:\\tools\\myfavtools\\out\\" + snippet.adress + ".txt"), "UTF8"));
				
				for (String s : snippet.strings) {
					writer.write(s + "\n");
				}
				writer.flush();
				writer.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}