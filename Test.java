import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.util.Scanner;
	
public class Test {
	public static void main(String[] args) {
		
		File file = new File("D:\\Games\\Favorite\\hcb\\World.hcb");
		BufferedWriter writer;

		try {
			byte[] fileContent = Files.readAllBytes(file.toPath());
			//System.out.println(DatatypeConverter.printHexBinary(new byte[]{-104, 106, 64, 0}));

			int pos = 3;

			try {
				writer = new BufferedWriter(new FileWriter("D:\\Games\\Favorite\\hcb\\testout.txt"));
					
				writer.write("bytes:");
				for (int i = 0; i<25; i++) {
					writer.write(Integer.toString(fileContent[i], 16)+"," );
				}
				
				try (FileOutputStream fileOuputStream = new FileOutputStream("D:\\Games\\Favorite\\hcb\\testout.hcb")){
				    fileOuputStream.write(fileContent);
				 } 
				
				while (pos < 25) {
					writer.write(++pos + ",");
					switch(fileContent[pos]) {
					case 15:
						writer.write("pushGlobal");
						writer.write("," + (fileContent[++pos] +  (fileContent[++pos]<<8)));
						break;

					case 21:
						writer.write("popGlobal");
						writer.write("," + (fileContent[++pos] +  (fileContent[++pos]<<8) ));
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
