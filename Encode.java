import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import bas.util.HexaBytes;

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
		
		ArrayList<Instruct> instructions = new ArrayList<>();
		ArrayList<Byte> bytes = new ArrayList<>();
		
		try(BufferedReader in = new BufferedReader(new FileReader("D:\\Games\\Favorite\\hcb\\testout.txt"))) {
			String str = in.readLine();
			String[] tokens = str.split("=");
			
			opLength = Integer.parseInt(tokens[1]); 
			//this might actually not be the end of the instructions if the file is changed!
			//so we temporarily add 4 0 bytes and add the real values later
			bytes.add((byte) 0);
			bytes.add((byte) 0);
			bytes.add((byte) 0);
			bytes.add((byte) 0);
			
			//set the pos (check!)
			int pos = 4;
			
		    loop : while ((str = in.readLine()) != null) {
					tokens = str.split(",");
							
					byte opcode;
					
					try {
						String instruction = tokens[1];
						if (instruction.equals("initStack")) {
							opcode = 1;
						//call is like a method
						//jumps to target adress and excecutes untill a return
						} else if (instruction.equals("call")) {
							opcode = 2;
						//probably do something in the FVP system
						} else if (instruction.equals("callSys")) {
							opcode = 3;
						//ends a call statement and return
						} else if (instruction.equals("return")) {
							opcode = 4;
						} else if (instruction.equals("ret1")) {
							opcode = 5;
						//jump to target adress
						} else if (instruction.equals("jump")) {
							opcode = 6;
						//jump if the last pushed var is zero (push0)
						//pushInt 0 does not seem to work
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
						}else if (instruction.equals("unknown24")) {
							opcode = 24;
						}else if (instruction.equals("neg")) {
							opcode = 25;
						}else if (instruction.equals("add")) {
							opcode = 26;
						}else if (instruction.equals("subtract")) {
							opcode = 27;
						}else if (instruction.equals("multiply")) {
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
						//check if the last 2 vars pushed are equal
						//push a 1 in case they are equal
						//push a 0 in case they are not
						//push1 is equal to pushInt 1
						//pushInt1 is equal to puchByte 1
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
						}else if (instruction.equals("gte")) {
							opcode = 39;
						}else {
							opcode = 0;
							System.out.println("Warning: unkown opcode! " + instruction);
						}
						
						bytes.add((byte) opcode);
						pos++;
										
						switch(opcode) {
						case 1:
							pos++;
							pos++;
							bytes.add((byte) Integer.parseInt(tokens[2]));
							bytes.add((byte) Integer.parseInt(tokens[3]));
							break;
						case 12:
						case 16:
						case 18:
						case 22:
						case 24:
							//parse a byte
							pos++;
							bytes.add((byte) Integer.parseInt(tokens[2]));
							break;
						case 2:
						case 6:
						case 7:
						case 10:
							//parse an int
							pos+=4;
							byte[] tempBytes = HexaBytes.unsIntToBytes( Integer.parseInt(tokens[2]) );
							
							for(int i = 3; i >= 0; i--) {
								bytes.add(tempBytes[i]);
							}
							
							break;
							
						case 3:
						case 11:
						case 15:
						case 17:
						case 21:
						case 23:
						//parse a short
							pos+=2;
							byte[] tempBytes2 = HexaBytes.unsShortToBytes((short) Integer.parseInt(tokens[2]) );
						
							bytes.add(tempBytes2[1]);
							bytes.add(tempBytes2[0]);
							
							break;
						case 14:
							//parse a string
							try {
								byte[] tempBytes3 = tokens[2].getBytes();
							
								byte len = (byte) (tempBytes3.length + 1);
								pos++;
								bytes.add(len);
							
								for (byte b : tempBytes3) {
									pos++;
									bytes.add(b);
								}
							} catch (ArrayIndexOutOfBoundsException e) {
								pos++;
								bytes.add((byte) 1);
							}
							
							pos++;
							bytes.add((byte) 0);
							
							break;
						case 13:
							//parse a float; still to do
							pos+=4;
						
							for(int i = 3; i >= 0; i--) {
								bytes.add((byte) 0);
							}
						}						
								
					} catch(ArrayIndexOutOfBoundsException e) {
						//end of instructs reached
						opLength = pos;
						byte[] tempBytes = HexaBytes.unsIntToBytes(opLength);
						
						for(int i = 0; i < 4; i++) {
							bytes.set(i, tempBytes[3 - i]);
						}
						
						break loop;
					}
		    }
			
			//parse entry point
			str = in.readLine();
			tokens = str.split("=");
			pos+=4;
			byte[] tempBytes = HexaBytes.unsIntToBytes( Integer.parseInt(tokens[1]) );
			
			for(int i = 3; i >= 0; i--) {
				bytes.add(tempBytes[i]);
			}
			
			//parse counts and res index
			for (int i = 0; i<3; i++) {
				str = in.readLine();
				tokens = str.split("=");
				pos+=2;
				byte[] tempBytes2 = HexaBytes.unsShortToBytes((short) Integer.parseInt(tokens[1]) );
				bytes.add(tempBytes2[1]);
				bytes.add(tempBytes2[0]);
			}
			
			//parse title string
			str = in.readLine();
			tokens = str.split(",");
			
			byte[] tempBytes3 = tokens[1].getBytes();
			
			byte len = (byte) (tempBytes3.length + 1);
			pos++;
			bytes.add(len);
		
			for (byte b : tempBytes3) {
				pos++;
				bytes.add(b);
			}
			
			pos++;
			bytes.add((byte) 0);
			
			//parse import count
			pos+=2;
			str = in.readLine();
			for (int i = 1; i<3; i++) {
				tokens = str.split(":");
				bytes.add((byte) Integer.parseInt(tokens[i]));
			}
			
			//parse imports
			while ((str = in.readLine()) != null) {
				tokens = str.split(",");
				pos++;
				bytes.add((byte) Integer.parseInt(tokens[1]));
				
				tempBytes3 = tokens[3].getBytes();
				
				len = (byte) (tempBytes3.length + 1);
				pos++;
				bytes.add(len);
			
				for (byte b : tempBytes3) {
					pos++;
					bytes.add(b);
				}
				
				pos++;
				bytes.add((byte) 0);
				
			}
			
		    //convert to bytestream
		    byte[] bytesToWrite = new byte[bytes.size()];
		    for (int i=0; i < bytes.size(); i++) {
		    	bytesToWrite[i] = bytes.get(i);
		    }
		    
		    //write to hcp
		    try (FileOutputStream fos = new FileOutputStream("D:\\Games\\Favorite\\‚¢‚ë‚Æ‚è‚Ç‚è‚ÌƒZƒJƒC\\World.hcb")) {
		    	   fos.write(bytesToWrite);
		    	   fos.close();
		    }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}