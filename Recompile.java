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
		
		ArrayList<String> opCodes = new ArrayList<>();
		ArrayList<Integer> oldAdres = new ArrayList<>();
		ArrayList<Integer> newAdres = new ArrayList<>();
		
		
		//the opLength is recalculated during encoding so we can just add any value here
		opCodes.add("opLength:=0");
		int pos = 4;
		
		//get a list of all files in a folder
		String path = "D:\\tools\\myfavtools\\out\\";
		try (Stream<Path> walk = Files.walk(Paths.get(path))) {

			List<String> result = walk.filter(Files::isRegularFile)
					.map(x -> x.toString()).collect(Collectors.toList());

			//result.forEach(System.out::println);

			//sort files numerically
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
				//for each line in each file, recalculate the new address and add to the new file
				File fileDir = new File(files);
				try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "UTF8"))) {
					String str;
					//combin
					loop: while ((str = in.readLine()) != null) {
						String[] tokens = str.split(",");
						try {
							oldAdres.add(Integer.parseInt(tokens[0]));

							newAdres.add(pos);

							tokens[0] = "" + pos;
							opCodes.add(String.join(",", tokens));
							
							switch (tokens[1]) {
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
								pos += 1;
								break;
							case "pushByte":
							case "pushStack":
							case "unknown18":
							case "copyStack":
							case "unknown24":
								pos += 2;
								break;
							case "initStack":
							case "callSys":
							case "pushShort":
							case "pushGlobal":
							case "unknown17":
							case "popGlobal":
							case "unknown23":
								pos += 3;
								break;
							case "call":
							case "jump":
							case "jumpIfZero":
							case "pushInt":
							case "pushFloat":
								pos += 5;
								break;
							case "pushString":
								// get string length
								try {
									byte[] b1 = tokens[2].getBytes();
									pos += b1.length + 3;
								} catch (ArrayIndexOutOfBoundsException e) {
									pos += 2;
								}
								
								break;
							default:
								System.out.println("Warning: unknown opCode" + tokens[1]);
							}

						} catch (NumberFormatException e) {
							while ((str = in.readLine()) != null) {
								opCodes.add(str);
							}
							break loop;
						}
					}

				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:\\tools\\myfavtools\\test.txt"),"SJIS"));
					
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
