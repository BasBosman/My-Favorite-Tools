import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.Scanner;
	
public class Decode {
	public static void main(String[] args) {
		
		Scanner s = new Scanner(System.in);
		System.out.println("Please give file name you want to parse:");
		//ie. D:\Games\Favorite\hcb\World.hcb
		//or D:\\Games\\Favorite\\いろとりどりのセカイ\\World.hcb
		File file = new File("D:\\Games\\Favorite\\いろとりどりのセカイ\\World.hcb");
		BufferedWriter writer;

		
		
		try {
			byte[] fileContent = Files.readAllBytes(file.toPath());

			//convert bytes to unsigned int
			int[] unsigned = new int[fileContent.length];
			
			for (int i = 0; i < fileContent.length; i++) {
				unsigned[i] = Byte.toUnsignedInt(fileContent[i]);
			}
			
			int pos = 0;

			try {
				writer = new BufferedWriter(new FileWriter("D:\\Games\\Favorite\\hcb\\testout.txt"));
				
				int opLength = unsigned[pos] +  (unsigned[++pos]<<8) + (unsigned[++pos]<<16) + (unsigned[++pos]<<24) ;
				
				writer.write("opLength:");
				writer.write("=" + opLength+ "\n");
					
				byte switchVal = 0;
				byte mode = 1;
				
				while (pos < fileContent.length - 3) {
					
					if (pos == opLength) {
						mode++;
					}
					
					switch(mode) {
					case 1:
						writer.write(++pos + ",");
						switchVal = fileContent[pos];
						break;
					case 2:
						writer.write("startPoint:");
						writer.write("=" + (unsigned[pos] +  (unsigned[++pos]<<8) + (unsigned[++pos]<<16) + (unsigned[++pos]<<24) ));
						writer.write("\n");
						
						writer.write("count1:");
						writer.write("=" + (unsigned[++pos] +  (unsigned[++pos]<<8)  ));
						writer.write("\n");
						
						writer.write("count2:");
						writer.write("=" + (unsigned[++pos] +  (unsigned[++pos]<<8) ));
						writer.write("\n");
						
						writer.write("resIndex:");
						writer.write("=" + (unsigned[++pos] +  (unsigned[++pos]<<8)  ) ) ;
						writer.write("\n");
						
						switchVal = 14;
						mode++;
						break;
					case 3:
						writer.write("import count=:");
						writer.write("" + unsigned[++pos]);
						writer.write(":" + unsigned[++pos]);
						writer.write("\n");
						mode++;
					case 4:						
						writer.write("import:");
						writer.write("type=:," +unsigned[++pos]+",");
						switchVal = 14;
					}

					switch(switchVal) {
					case 0 :
						writer.write("null");
						break;
					case 1:
						writer.write("initStack");
						writer.write("," + unsigned[++pos]);
						writer.write("," + unsigned[++pos]);
						break;
					case 2:
						writer.write("call");
						writer.write("," + (unsigned[++pos] +  (unsigned[++pos]<<8) + (unsigned[++pos]<<16) + (unsigned[++pos]<<24) ));
						break;
					case 3:
						writer.write("callSys");
						writer.write("," + (unsigned[++pos] +  (unsigned[++pos]<<8) ));
						break;
					case 4:
						writer.write("return");
						break;
					case 5:
						writer.write("ret1");
						break;
					case 6:
						writer.write("jump");
						writer.write("," + (unsigned[++pos] +  (unsigned[++pos]<<8) + (unsigned[++pos]<<16) + (unsigned[++pos]<<24) ));
						break;
					case 7:
						writer.write("jumpIfZero");
						writer.write("," + (unsigned[++pos] +  (unsigned[++pos]<<8) + (unsigned[++pos]<<16) + (unsigned[++pos]<<24) ));
						break;
					case 8:
						writer.write("push0");
						break;
					case 9:
						writer.write("push1");
						break;
					case 10:
						writer.write("pushInt");
						writer.write("," + (unsigned[++pos] +  (unsigned[++pos]<<8) + (unsigned[++pos]<<16) + (unsigned[++pos]<<24) ));
						break;
					case 11:
						writer.write("pushShort");
						writer.write("," + (unsigned[++pos] +  (unsigned[++pos]<<8) ));
						break;
					case 12:
						writer.write("pushByte");
						writer.write("," + unsigned[++pos]);
						break;
					case 13:
						writer.write("pushFloat");
						byte[] bytes = {fileContent[++pos], fileContent[++pos], fileContent[++pos], fileContent[++pos]}; 
						writer.write( "," +  ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat() );
						writer.write( ",bytes of" + bytes[0] + " " + bytes[1] + " " + bytes[2] + " " + bytes[3]);
						break;
					case 14:
						writer.write("pushString");
						int len = (unsigned[++pos]) - 1;
						
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
						writer.write("," + (unsigned[++pos] +  (unsigned[++pos]<<8)));
						break;
					case 16:
						writer.write("pushStack");
						writer.write("," + (unsigned[++pos]));
						break;
					case 17:
						writer.write("unknown17");
						writer.write("," + (unsigned[++pos] +  (unsigned[++pos]<<8) ));
						break;
					case 18:
						writer.write("unknown18");
						writer.write("," + (unsigned[++pos] ));
						break;
					case 19:
						writer.write("pushTop");
						break;
					case 20:
						writer.write("pushTemp");
						break;
					case 21:
						writer.write("popGlobal");
						writer.write("," + (unsigned[++pos] +  (unsigned[++pos]<<8) ));
						break;
					case 22:
						writer.write("copyStack");
						writer.write("," + (unsigned[++pos] ));
						break;
					case 23:
						writer.write("unknown23");
						writer.write("," + (unsigned[++pos] +  (unsigned[++pos]<<8)));
						break;
					case 24:
						writer.write("unknown24");
						writer.write("," + (unsigned[++pos] ));
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
				
					writer.write("\n");
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
