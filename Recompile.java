import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Recompile {
	public static void main(String[] args) {
		Recompile.run();
	}
	
	private static synchronized void run() {

		ArrayList<String> opCodes = new ArrayList<>();
		// ArrayList<Integer> oldAdres = new ArrayList<>();
		// ArrayList<Integer> newAdres = new ArrayList<>();

		int[] newAdres = new int[9999999];

		// the opLength is recalculated during encoding so we can just add any value
		// here
		opCodes.add("opLength:=0");
		int pos = 4;

		// get a list of all files in a folder
		String path = "D:\\Games\\Favorite\\hcb\\out\\";
		try (Stream<Path> walk = Files.walk(Paths.get(path))) {

			List<String> result = walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());

			// result.forEach(System.out::println);

			// sort files numerically
			{
				ArrayList<Integer> filesnames = new ArrayList<>();
				for (String file : result) {
					String[] tokens1 = file.split("\\.");
					String[] tokens2 = tokens1[0].split("\\\\");
					filesnames.add(Integer.parseInt(tokens2[tokens2.length - 1]));

					Collections.sort(filesnames);

				}

				for (int i = 0; i < result.size(); i++) {
					result.set(i, path + filesnames.get(i) + ".txt");
				}

			}

			for (String files : result) {
				System.out.println(files);
				// for each line in each file, recalculate the new address and add to the new
				// file
				File fileDir = new File(files);
				try (BufferedReader in = new BufferedReader(
						new InputStreamReader(new FileInputStream(fileDir), "UTF8"))) {
					String str;
					// combin
					loop: while ((str = in.readLine()) != null) {
						String[] tokens = str.split(",");
						try {
							// oldAdres.add(Integer.parseInt(tokens[0]));

							// newAdres.add(pos);
							newAdres[Integer.parseInt(tokens[0])] = pos;

							tokens[0] = "" + pos;

							switch (tokens[1]) {

							// Recompile compiled functions
							case "speakLine":

								// check if there is a voiceline
								if (!tokens[4].equals("0")) {
									// does not seem to work so far
								}
								// check if theres a speaker
								if (!tokens[3].equals("0")) {
									opCodes.add(pos + ",call," + tokens[3]);
									pos += 5;
								}

								// parse the string
								opCodes.add(pos + ",pushString," + tokens[2]);
								byte[] b2 = tokens[2].getBytes();
								pos += b2.length + 3;

								// add 3x push0
								opCodes.add(pos + ",push0");
								pos += 1;
								opCodes.add(pos + ",push0");
								pos += 1;
								opCodes.add(pos + ",push0");
								pos += 1;

								break;

							// Recalc the new address for basic functions
							case "null":
							case "return":
							case "ret1":
							case "push0":
							case "push1":
							case "pushTop":
							case "pushTemp":
							case "neg":
							case "add":
							case "subtract":
							case "multiply":
							case "div":
							case "mod":
							case "bitTest":
							case "and":
							case "or":
							case "equal":
							case "notEqual":
							case "gt":
							case "lte":
							case "lt":
							case "gte":
								opCodes.add(String.join(",", tokens));
								pos += 1;
								break;
							case "pushByte":
							case "pushStack":
							case "unknown18":
							case "copyStack":
							case "unknown24":
								opCodes.add(String.join(",", tokens));
								pos += 2;
								break;
							case "initStack":
							case "callSys":
							case "pushShort":
							case "pushGlobal":
							case "unknown17":
							case "popGlobal":
							case "unknown23":
								opCodes.add(String.join(",", tokens));
								pos += 3;
								break;
							case "call":
							case "jump":
							case "jumpIfZero":
							case "pushInt":
							case "pushFloat":
								opCodes.add(String.join(",", tokens));
								pos += 5;
								break;
							case "pushString":
								// get string length
								opCodes.add(String.join(",", tokens));
								try {

									byte[] b1 = tokens[2].getBytes();
									pos += b1.length + 3;

								} catch (ArrayIndexOutOfBoundsException e) {
									pos += 3;
								}

								break;
							default:
								System.out.println("Warning: unknown opCode" + tokens[1]);
							}

						} catch (NumberFormatException e) {
							opCodes.add("" + pos + ",");
							opCodes.add(str);
							while ((str = in.readLine()) != null) {
								opCodes.add(str);
							}
							break loop;
						}
					}

					// recalc new address:
					loop: for (int i = 1; i < opCodes.size(); i++) {
						try {
							String[] tokens = opCodes.get(i).split(",");
							switch (tokens[1]) {
							case "call":
							case "jump":
							case "jumpIfZero":
								tokens[2] = "" + newAdres[Integer.parseInt(tokens[2])];
								opCodes.set(i, String.join(",", tokens));
								break;
							}

						} catch (NumberFormatException e) {
							String[] tokens = opCodes.get(i).split("=");
							tokens[1] = "" + newAdres[Integer.parseInt(tokens[1])];
							opCodes.set(i, String.join("=", tokens));
							break loop;
						} catch (ArrayIndexOutOfBoundsException e) {
							i++;
						}
					}

				
					
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
							new FileOutputStream("D:\\Games\\Favorite\\hcb\\recompiled.txt"), "SJIS"));

					for (String s : opCodes) {
						writer.write(s + "\n");
					}
					writer.flush();
					writer.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

