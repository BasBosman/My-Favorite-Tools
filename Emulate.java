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
	ArrayList<Integer> address = new ArrayList<>();
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
		Stack stack = new Stack();
		
		int pos = jump("177217", opCodes);
	
		while (true) {
			String[] tokens = opCodes.get(pos).split(",");
			
			System.out.println("opCode: " + opCodes.get(pos));
			
			String instruction = tokens[1];
			
			int result = 0;
			switch(instruction) {
			case "initStack":
				//init the stack
				pos++;
				break;
			 case"call":
				//call method at address
				 stack.address.add(0,++pos); //address point to a pos number not an address number
				 pos = jump(tokens[2], opCodes);
				 break;
			 case"callSys":
				//call method from system
				 pos++;
				 break;
			 case"return":
				//call return to previous address (use pos in this case)
				 pos = stack.address.get(0);
				 stack.address.remove(0);
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
				 if (stack.content.get(0).equals("0")) {
					 pos = jump(tokens[2], opCodes);
				 } else {
					 pos++;
				 }
				 break;
			 case"push0":
				//add a 0 (false) to the stack
				 stack.content.add(0,"0");
				 pos++;
				 break;
			 case"push1":
				//add a 1 (true) to the stack
				 stack.content.add(0,"1");
				 pos++;
				 break;
			 case"pushInt":
				//add an int to the stack
				 stack.content.add(0,tokens[2]);
				 pos++;
				 break;
			 case"pushShort":
				//add a short to the stack
				 stack.content.add(0,tokens[2]);
				 pos++;
				 break;
			 case"pushByte":
				//add a byte to the stack
				 stack.content.add(0,tokens[2]);
				 pos++;
				 break;
			 case"pushFloat":
				//add a float to the stack
				 stack.content.add(0,tokens[2]);
				 pos++;
				 break;
			 case"pushString":
				//add a string to the stack
				 try {
					 stack.content.add(0,tokens[2]);
				 } catch(ArrayIndexOutOfBoundsException e) {
					 stack.content.add(0,"");
				 }
				 pos++;
				 break;
			 case"pushGlobal":
				//add a global var to the stack
				 stack.content.add(0,global.vars[Integer.parseInt(tokens[2])]);
				 pos++;
				 break;
			 case"pushStack":
				//??
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
				global.vars[Integer.parseInt(tokens[2])] = stack.content.get(0);
				pos++;
				break;
			case"copyStack":
				//??
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
				if (stack.content.get(0).equals("0")) {
					stack.content.add(0,"1");
				} else {
					stack.content.add(0,"0");
				}
				pos++;
				break;
			case"add":
				//add
				result = Integer.parseInt(stack.content.get(0)) + Integer.parseInt(stack.content.get(1));
				stack.content.add(0,"" + result);
				pos++;
				break;
			case"subtract":
				//subtract
				result = Integer.parseInt(stack.content.get(0)) - Integer.parseInt(stack.content.get(1));
				stack.content.add(0,"" + result);
				pos++;
				break;
			case"multiply":
				//multiply
				result = Integer.parseInt(stack.content.get(0)) * Integer.parseInt(stack.content.get(1));
				stack.content.add(0,"" + result);
				pos++;
				break;
			case"div":
				//divide
				result = Integer.parseInt(stack.content.get(0)) / Integer.parseInt(stack.content.get(1));
				stack.content.add(0,"" + result);
				pos++;
				break;
			case"mod":
				//modulus
				result = Integer.parseInt(stack.content.get(0)) % Integer.parseInt(stack.content.get(1));
				stack.content.add(0,"" + result);
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
				if (stack.content.get(0).equals(stack.content.get(1))) {
					stack.content.add(0,"1");
				} else {
					stack.content.add(0,"0");
				}
				pos++;
				break;
			case"notEqual":
				//test if not equal
				if (stack.content.get(0).equals(stack.content.get(1))) {
					stack.content.add(0,"0");
				} else {
					stack.content.add(0,"1");
				}
				pos++;
				break;
			case"gt":
				//test if greater
				if (Integer.parseInt(stack.content.get(0)) > Integer.parseInt(stack.content.get(1))) {
					stack.content.add(0,"1");
				} else {
					stack.content.add(0,"0");
				}
				pos++;
				break;
			case"lte":
				//test if lesser or eq
				if (Integer.parseInt(stack.content.get(0)) <= Integer.parseInt(stack.content.get(1))) {
					stack.content.add(0,"1");
				} else {
					stack.content.add(0,"0");
				}
				pos++;
				break;
			case"lt":
				//test if lesser
				if (Integer.parseInt(stack.content.get(0)) < Integer.parseInt(stack.content.get(1))) {
					stack.content.add(0,"1");
				} else {
					stack.content.add(0,"0");
				}
				pos++;
				break;
			case"gte":
				//test if greater or equal
				if (Integer.parseInt(stack.content.get(0)) >= Integer.parseInt(stack.content.get(1))) {
					stack.content.add(0,"1");
				} else {
					stack.content.add(0,"0");
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
