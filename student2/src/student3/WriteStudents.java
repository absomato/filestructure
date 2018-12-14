package student3;

import java.io.*;
import java.util.Scanner;

public class WriteStudents {
	public static void main(String[] args) {
		if(args.length != 1 && args.length !=3) {
			System.out.println("USAGE: java WriteStudents 입력파일명");
			System.exit(0);
		}
		String	input_file=null;
		if(args.length==1) {
		input_file = args[0];
		}
		else {
			input_file=args[2];
		}
		try {
			File in_f = new File(input_file);
			if(!in_f.exists()) {
				System.out.println(input_file+" does not exist");
				System.exit(0);
			}
			
			FileReader fr = new FileReader(in_f);
			BufferedReader br = new BufferedReader(fr);
			Scanner in = new Scanner(br);

			RandomAccessFile out_f = new RandomAccessFile(input_file+"_dat", "rw");
			out_f.setLength(0L);		// clear output  file
			Student  s = new Student();
			
			while(in.hasNext()) {
				s.getStudent(in);
				if(s.number.length()==8)s.number=s.number.substring(1, s.number.length());
				s.writeStudent(out_f);
			}

			if(args.length==1)
			System.out.println("File Size : "+out_f.length());
			br.close();
			out_f.close();
		} catch (IOException e) {
			System.out.println("file error ...");
		} 
	}
}