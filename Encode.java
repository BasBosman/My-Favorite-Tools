import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

abstract class Instruct{
	byte type;
	int num1;
	int num2;
	String str;
	byte[] floatbytes;
	
	public Instruct(byte type) {
		this.type = type;
	}
	
}

class Opcode extends Instruct{
	public Opcode(byte type) {
		super(type);
	}
}

public class Encode {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.println("Please give file name you want to parse:");
		//ie. D:\Games\Favorite\hcb\testout.txt
		BufferedWriter writer;
		
		int opLength;
		int mode = 1;
		
		ArrayList<Instruct> instructions = new ArrayList<>();
		ArrayList<Byte> bytes = new ArrayList<>();
		
		
		
		try(BufferedReader in = new BufferedReader(new FileReader(s.nextLine()))) {
			String str = in.readLine();
			String[] tokens = str.split("=");
			
			opLength = Integer.parseInt(tokens[1]); //this might actually not be the end of the instructions if the file is changed!
			
		    while ((str = in.readLine()) != null) {
				switch(mode) {
				case 1:
					tokens = str.split(",");
					
					if (Integer.parseInt(tokens[0]) == opLength) {
						mode++;
					}
					
					byte opcode;
					
					try {
						String instruction = tokens[1];
						if (instruction.equals("initStack")) {
							opcode = 1;
						} else if (instruction.equals("call")) {
							opcode = 2;
						} else if (instruction.equals("callSys")) {
							opcode = 3;
						} else if (instruction.equals("return")) {
							opcode = 3;
						} else if (instruction.equals("ret1")) {
							opcode = 4;
						} else if (instruction.equals("jump")) {
							opcode = 6;
						} else if (instruction.equals("jumpIfZero")) {
							opcode = 7;
						} else if (instruction.equals("push0")) {
							opcode = 8;
						} else if (instruction.equals("push1")) {
							opcode = 9;
						} else if (instruction.equals("pushInt")) {
							opcode = 10;
						} else if (instruction.equals("pushShort")) {
							opcode = 11;
						} else if (instruction.equals("pushByte")) {
							opcode = 12;
						} else if (instruction.equals("pushFloat")) {
							opcode = 13;
						} else if (instruction.equals("pushString")) {
							opcode = 14;
						} else if (instruction.equals("pushGlobal")) {
							opcode = 15;
						} else if (instruction.equals("pushStack")) {
							opcode = 16;
						} else if (instruction.equals("unknown17")) {
							opcode = 17;
						} else if (instruction.equals("unknown18")) {
							opcode = 18;
						} else if (instruction.equals("pushTop")) {
							opcode = 19;
						} else if (instruction.equals("pushTemp")) {
							opcode = 20;
						}else if (instruction.equals("popGlobal")) {
							opcode = 21;
						}else if (instruction.equals("copyStack")) {
							opcode = 22;
						}else if (instruction.equals("unknown23")) {
							opcode = 23;
						}else if (instruction.equals("unkown24")) {
							opcode = 24;
						}else if (instruction.equals("neg")) {
							opcode = 25;
						}else if (instruction.equals("add")) {
							opcode = 26;
						}else if (instruction.equals("subtract")) {
							opcode = 27;
						}else if (instruction.equals("multipy")) {
							opcode = 28;
						}else if (instruction.equals("div")) {
							opcode = 29;
						}else if (instruction.equals("mod")) {
							opcode = 30;
						}else if (instruction.equals("bitTest")) {
							opcode = 31;
						}else if (instruction.equals("and")) {
							opcode = 32;
						}else if (instruction.equals("or")) {
							opcode = 33;
						}else if (instruction.equals("equal")) {
							opcode = 34;
						}else if (instruction.equals("notEqual")) {
							opcode = 35;
						}else if (instruction.equals("gt")) {
							opcode = 36;
						}else if (instruction.equals("lte")) {
							opcode = 37;
						}else if (instruction.equals("lt")) {
							opcode = 38;
						}else if (instruction.equals("lte")) {
							opcode = 39;
						}else {
							opcode = 0;
							System.out.println("Warning: unkown opcode!");
						}
						
						Instruct instruct = new Opcode(opcode);
						
						switch(opcode) {
						case 1:
							instruct.num2 = Integer.parseInt(tokens[3]);
						case 2:
						case 3:
						case 6:
						case 7:
						case 10:
						case 11:
						case 12:
						case 15:
						case 16:
						case 17:
						case 21:
						case 22:
						case 23:
						case 24:
							instruct.num1 = Integer.parseInt(tokens[2]);
							break;
						case 14:
							instruct.str = tokens[2];
							break;
						case 13:
							//writing float not yet supported;
							instruct.floatbytes = new byte[]{0, 0, 0, 0};
						}
						
						instructions.add(instruct);
						
								
					} catch(ArrayIndexOutOfBoundsException e) {
						//end of instructs reached
						opLength = Integer.parseInt(tokens[0]);
						mode++;
					}
					
					break;
				}
		    }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
