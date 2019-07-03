import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.Scanner;
	
public class Parse {
	public static void main(String[] args) {
		
		Scanner s = new Scanner(System.in);
		System.out.println("Please give file name you want to parse:");
		//ie. D:\Games\Favorite\hcb\World.hcb
		File file = new File(s.nextLine());
		BufferedWriter writer;

		try {
			byte[] fileContent = Files.readAllBytes(file.toPath());
			//System.out.println(DatatypeConverter.printHexBinary(new byte[]{-104, 106, 64, 0}));

			int pos = 0;

			try {
				writer = new BufferedWriter(new FileWriter("D:\\Games\\Favorite\\hcb\\testout.txt"));
				
				int oplength = fileContent[pos] +  (fileContent[++pos]<<8) + (fileContent[++pos]<<16) + (fileContent[++pos]<<24) + 256 ;
				
				writer.write("opLength:");
				writer.write("=" + oplength+ "\n");
					
				byte switchVal;
				
				while (pos < fileContent.length - 5) {
					
					
					if (pos == oplength) {
						writer.write("startPoint:");
						writer.write("=" + (fileContent[pos] +  (fileContent[++pos]<<8) + (fileContent[++pos]<<16) + (fileContent[++pos]<<24) ));
						writer.write("\n");
						
						writer.write("count1:");
						writer.write("=" + (fileContent[++pos] +  (fileContent[++pos]<<8)  ));
						writer.write("\n");
						
						writer.write("count2:");
						writer.write("=" + (fileContent[++pos] +  (fileContent[++pos]<<8) + 256  ));
						writer.write("\n");
						
						writer.write("resIndex:");
						writer.write("=" + (fileContent[++pos] +  (fileContent[++pos]<<8)  ) ) ;
						writer.write("\n");
						
						switchVal = 14;
					} else if(pos >= oplength) {
						
						writer.write("import:");
						writer.write("type=:" +fileContent[++pos]+",");
						switchVal = 14;
					}
					else {
						writer.write(++pos + ",");
						switchVal = fileContent[pos];
					}

					switch(switchVal) {
					case 0 :
						writer.write("null");
						break;
					case 1:
						writer.write("initStack");
						writer.write("," + fileContent[++pos]);
						writer.write("," + fileContent[++pos]);
						break;
					case 2:
						writer.write("call");
						writer.write("," + (fileContent[++pos] +  (fileContent[++pos]<<8) + (fileContent[++pos]<<16) + (fileContent[++pos]<<24) ));
						break;
					case 3:
						writer.write("callSys");
						writer.write("," + (fileContent[++pos] +  (fileContent[++pos]<<8) ));
						break;
					case 4:
						writer.write("return");
						break;
					case 5:
						writer.write("ret1");
						break;
					case 6:
						writer.write("jump");
						writer.write("," + (fileContent[++pos] +  (fileContent[++pos]<<8) + (fileContent[++pos]<<16) + (fileContent[++pos]<<24) ));
						break;
					case 7:
						writer.write("jumpIfZero");
						writer.write("," + (fileContent[++pos] +  (fileContent[++pos]<<8) + (fileContent[++pos]<<16) + (fileContent[++pos]<<24) ));
						break;
					case 8:
						writer.write("push0");
						break;
					case 9:
						writer.write("push1");
						break;
					case 10:
						writer.write("pushInt");
						writer.write("," + (fileContent[++pos] +  (fileContent[++pos]<<8) + (fileContent[++pos]<<16) + (fileContent[++pos]<<24) ));
						break;
					case 11:
						writer.write("pushShort");
						writer.write("," + (fileContent[++pos] +  (fileContent[++pos]<<8) ));
						break;
					case 12:
						writer.write("pushByte");
						writer.write("," + fileContent[++pos]);
						break;
					case 13:
						writer.write("pushFloat");
						byte[] bytes = {fileContent[++pos], fileContent[++pos], fileContent[++pos], fileContent[++pos]}; 
						writer.write( "," +  ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat() );
						writer.write( ",bytes of" + bytes[0] + " " + bytes[1] + " " + bytes[2] + " " + bytes[3]);
						break;
					case 14:
						writer.write("pushString");
						//Length is unsigned (the 0xff converts)
						int len = (fileContent[++pos] & 0xff) - 1;
						
						if (len >= 0 ) {
							byte[] bytes2 = new byte[len];
							for(int i = 0; i < len; i++) {
							    bytes2[i] = fileContent[++pos];
							}
							writer.write("," + new String(bytes2));
							++pos;
						} else {
							writer.write(",Watch out! null string!");
						}

						break;
					case 15:
						writer.write("pushGlobal");
						writer.write("," + (fileContent[++pos] +  (fileContent[++pos]<<8)));
						break;
					case 16:
						writer.write("pushStack");
						writer.write("," + (fileContent[++pos]));
						break;
					case 17:
						writer.write("unknown17");
						writer.write("," + (fileContent[++pos] +  (fileContent[++pos]<<8) ));
						break;
					case 18:
						writer.write("unknown18");
						writer.write("," + (fileContent[++pos] ));
						break;
					case 19:
						writer.write("pushTop");
						break;
					case 20:
						writer.write("pushTemp");
						break;
					case 21:
						writer.write("popGlobal");
						writer.write("," + (fileContent[++pos] +  (fileContent[++pos]<<8) ));
						break;
					case 22:
						writer.write("copyStack");
						writer.write("," + (fileContent[++pos] ));
						break;
					case 23:
						writer.write("unknown23");
						writer.write("," + (fileContent[++pos] +  (fileContent[++pos]<<8)));
						break;
					case 24:
						writer.write("unknown24");
						writer.write("," + (fileContent[++pos] ));
						break;
					case 25:
						writer.write("neg");
						break;
					case 26:
						writer.write("add");
						break;
					case 27:
						writer.write("subtract");
						break;
					case 28:
						writer.write("multiply");
						break;
					case 29:
						writer.write("div");
						break;
					case 30:
						writer.write("mod");
						break;
					case 31:
						writer.write("bitTest");
						break;
					case 32:
						writer.write("and");
						break;
					case 33:
						writer.write("or");
						break;
					case 34:
						writer.write("equal");
						break;
					case 35:
						writer.write("notEqual");
						break;
					case 36:
						writer.write("gt");
						break;
					case 37:
						writer.write("lte");
						break;
					case 38:
						writer.write("lt");
						break;
					case 39:
						writer.write("gte");
						break;
					}
					
					
						
					writer.write("\t\n");
				}
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
