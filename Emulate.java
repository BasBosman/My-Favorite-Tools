import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

class Global {
	String[] vars = new String[10000];
}

class Stack {
	ArrayList<String> content = new ArrayList<>();
	int address;
}

public class Emulate {
	public static int jump(String index, ArrayList<String> opcodes) {
		int pos = 0;
		while (true) {
			String[] tokens = opcodes.get(pos).split(",");
			if (tokens[0].equals(index)) {
				break;
			}
			pos++;
		}
		
		return pos;
	}
	
	public static void main(String[] args) {
		File fileDir = new File("D:\\tools\\myfavtools\\out.txt");
		
		ArrayList<String> opCodes = new ArrayList<>();
		
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "SJIS"))) {
			
			String str = in.readLine();
			String[] tokens = str.split("=");
			int opLength = Integer.parseInt(tokens[1]);
			Settings set = new Settings();
			
			loop: while ((str = in.readLine()) != null) {
				opCodes.add(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Global global = new Global();
		ArrayList<Stack> stack = new ArrayList<>();
		
		//set start address:
		int startadres = 177217;
		int pos = jump(Integer.toString(startadres), opCodes);
			
		short currentStack = 0;
		
		while (true) {
			String[] tokens = opCodes.get(pos).split(",");
			System.out.println("opCode: " + opCodes.get(pos));
			
			String instruction = tokens[1];
			
			int result = 0;
			switch(instruction) {
			case "initStack":
				//init the stack
				stack.add(new Stack());
				
				for (int j = 0; j<Integer.parseInt(tokens[2]); j++ ) {
					stack.get(stack.size()-1).content.add(tokens[3]);
				}
				
				pos++;
				break;
			 case"call":
				//call method at address
				 stack.get(stack.size()-1).address = ++pos; //address point to a pos number not an address number
				 pos = jump(tokens[2], opCodes);
				 break;
			 case"callSys":
				//call method from system
				 pos++;
				 break;
			 case"return":
				//call return to previous address (use pos in this case)
				 stack.remove(stack.size() - 1);
				 pos = stack.get(stack.size()-1).address;
				 break;
			 case"ret1":
				//??
				 pos++;
				 break;
			 case"jump":
				//go to address
				 pos = jump(tokens[2], opCodes);
				 break;
			 case"jumpIfZero":
				//go to address if last on stack is 0
				 if (stack.get(stack.size()-1).content.get(0).equals("0")) {
					 pos = jump(tokens[2], opCodes);
				 } else {
					 pos++;
				 }
				 break;
			 case"push0":
				//add a 0 (false) to the stack
				 stack.get(stack.size()-1).content.add(0,"0");
				 pos++;
				 break;
			 case"push1":
				//add a 1 (true) to the stack
				 stack.get(stack.size()-1).content.add(0,"1");
				 pos++;
				 break;
			 case"pushInt":
				//add an int to the stack
				 stack.get(stack.size()-1).content.add(0,tokens[2]);
				 pos++;
				 break;
			 case"pushShort":
				//add a short to the stack
				 stack.get(stack.size()-1).content.add(0,tokens[2]);
				 pos++;
				 break;
			 case"pushByte":
				//add a byte to the stack
				 stack.get(stack.size()-1).content.add(0,tokens[2]);
				 pos++;
				 break;
			 case"pushFloat":
				//add a float to the stack
				 stack.get(stack.size()-1).content.add(0,tokens[2]);
				 pos++;
				 break;
			 case"pushString":
				//add a string to the stack
				 try {
					 stack.get(stack.size()-1).content.add(0,tokens[2]);
				 } catch(ArrayIndexOutOfBoundsException e) {
					 stack.get(stack.size()-1).content.add(0,"");
				 }
				 pos++;
				 break;
			 case"pushGlobal":
				//add a global var to the stack
				 stack.get(stack.size()-1).content.add(0,global.vars[Integer.parseInt(tokens[2])]);
				 pos++;
				 break;
			 case"pushStack":
				//??
				 stack.get(stack.size()-1).content.add( stack.get(stack.size()-1).content.get(254 - Integer.parseInt(tokens[2])) );
				 pos++;
				 break;
			 case"unknown17":
				//??
				 pos++;
				 break;
			 case"unknown18":
				//??
				 pos++;
				 break;
			case"pushTop":
				//??
				pos++;
				break;
			 case"pushTemp":
				//??
				 pos++;
				 break;
			case"popGlobal":
				//save a global variable
				global.vars[Integer.parseInt(tokens[2])] = stack.get(stack.size()-1).content.get(0);
				pos++;
				break;
			case"copyStack":
				//??
				stack.set(stack.size()-1, new Stack());
				pos++;
				break;
			case"unknown23":
				//?? (test bitshift)
				pos++;
				break;
			case"unknown24":
				//?? (test bitshift)
				pos++;
				break;
			case"neg":
				//negate
				if (stack.get(stack.size()-1).content.get(0).equals("0")) {
					stack.get(stack.size()-1).content.add(0,"1");
				} else {
					stack.get(stack.size()-1).content.add(0,"0");
				}
				pos++;
				break;
			case"add":
				//add
				result = Integer.parseInt(stack.get(stack.size()-1).content.get(0)) + Integer.parseInt(stack.get(stack.size()-1).content.get(1));
				stack.get(stack.size()-1).content.add(0,"" + result);
				pos++;
				break;
			case"subtract":
				//subtract
				result = Integer.parseInt(stack.get(stack.size()-1).content.get(0)) - Integer.parseInt(stack.get(stack.size()-1).content.get(1));
				stack.get(stack.size()-1).content.add(0,"" + result);
				pos++;
				break;
			case"multiply":
				//multiply
				result = Integer.parseInt(stack.get(stack.size()-1).content.get(0)) * Integer.parseInt(stack.get(stack.size()-1).content.get(1));
				stack.get(stack.size()-1).content.add(0,"" + result);
				pos++;
				break;
			case"div":
				//divide
				result = Integer.parseInt(stack.get(stack.size()-1).content.get(0)) / Integer.parseInt(stack.get(stack.size()-1).content.get(1));
				stack.get(stack.size()-1).content.add(0,"" + result);
				pos++;
				break;
			case"mod":
				//modulus
				result = Integer.parseInt(stack.get(stack.size()-1).content.get(0)) % Integer.parseInt(stack.get(stack.size()-1).content.get(1));
				stack.get(stack.size()-1).content.add(0,"" + result);
				pos++;
				break;
			case"bitTest":
				//??
				pos++;
				break;
			case"and":
				//and
				pos++;
				break;
			case"or":
				//or
				pos++;
				break;
			case"equal":
				//test if equal
				if (stack.get(stack.size()-1).content.get(0).equals(stack.get(stack.size()-1).content.get(1))) {
					stack.get(stack.size()-1).content.add(0,"1");
				} else {
					stack.get(stack.size()-1).content.add(0,"0");
				}
				pos++;
				break;
			case"notEqual":
				//test if not equal
				if (stack.get(stack.size()-1).content.get(0).equals(stack.get(stack.size()-1).content.get(1))) {
					stack.get(stack.size()-1).content.add(0,"0");
				} else {
					stack.get(stack.size()-1).content.add(0,"1");
				}
				pos++;
				break;
			case"gt":
				//test if greater
				if (Integer.parseInt(stack.get(stack.size()-1).content.get(0)) > Integer.parseInt(stack.get(stack.size()-1).content.get(1))) {
					stack.get(stack.size()-1).content.add(0,"1");
				} else {
					stack.get(stack.size()-1).content.add(0,"0");
				}
				pos++;
				break;
			case"lte":
				//test if lesser or eq
				if (Integer.parseInt(stack.get(stack.size()-1).content.get(0)) <= Integer.parseInt(stack.get(stack.size()-1).content.get(1))) {
					stack.get(stack.size()-1).content.add(0,"1");
				} else {
					stack.get(stack.size()-1).content.add(0,"0");
				}
				pos++;
				break;
			case"lt":
				//test if lesser
				if (Integer.parseInt(stack.get(stack.size()-1).content.get(0)) < Integer.parseInt(stack.get(stack.size()-1).content.get(1))) {
					stack.get(stack.size()-1).content.add(0,"1");
				} else {
					stack.get(stack.size()-1).content.add(0,"0");
				}
				pos++;
				break;
			case"gte":
				//test if greater or equal
				if (Integer.parseInt(stack.get(stack.size()-1).content.get(0)) >= Integer.parseInt(stack.get(stack.size()-1).content.get(1))) {
					stack.get(stack.size()-1).content.add(0,"1");
				} else {
					stack.get(stack.size()-1).content.add(0,"0");
				}
				pos++;
				break;
			default:
				//error
				System.out.println("Unknown opcode");
				pos++;
				break;
			}
			
		}
		
	}
}
