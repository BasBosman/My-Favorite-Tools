import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class Character {
	class Alias {
		String displayName;
		String hiddenName;
	}

	int id;
	String name;
	ArrayList<String> alias = new ArrayList<>();
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

}

public class Decompose {
	public static void main(String[] args) {
		BufferedWriter writer;
		MiscData miscData = new MiscData();
		ArrayList<CodeSnippet> codeSnippets = new ArrayList<>();

		ArrayList<Byte> bytes = new ArrayList<>();

		try (BufferedReader in = new BufferedReader(new FileReader("D:\\tools\\myfavtools\\out.txt"))) {
			String str = in.readLine();
			String[] tokens = str.split("=");
			miscData.opLength = Integer.parseInt(tokens[1]);

			int pos;

			loop: while ((str = in.readLine()) != null) {
				try {
					tokens = str.split(",");
					// if opcode is initStack create a new snippet with adress
					// else add the line to the last snippet
					if (tokens[1].equals("initStack")) {
						codeSnippets.add(new CodeSnippet(Integer.parseInt(tokens[0])));
					}
					codeSnippets.get(codeSnippets.size() - 1).strings.add(str);
				} catch (ArrayIndexOutOfBoundsException e) {
					// end of instructs reached
					break loop;
				}
			}

			codeSnippets.add(new CodeSnippet(Integer.MAX_VALUE));
			while ((str = in.readLine()) != null) {
				codeSnippets.get(codeSnippets.size() - 1).strings.add(str);
			}

			// write every snippet to a file
			for (CodeSnippet snippet : codeSnippets) {
				writer = new BufferedWriter(new FileWriter("D:\\tools\\myfavtools\\out\\" + snippet.adress + ".txt"));
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