import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
public class ManageStudents {

	public static void main(String[] args) {
		if (args.length != 3 && args.length != 2 && args.length!= 7) {
			System.out.println("USAGE: java ReadStudents   입력파일명   [학생이름]");
			System.exit(0);
		}
		String name = null;
		String kk = null;
		//키 값 u시에 사용될 변수들
		String number =null;
		String gender =null;
		String phone_no=null;
		String address=null;
		int sum = 0;
		int pcnt = 1;// 학생들 수를  count할 변수
		int errcat = 0;// 어떤 종류의 에런지 확인하는 변수
		if (args.length == 3) {
			name = args[2];
			kk = args[1];
		}
		else kk=args[1];
		String input_file = args[0];

		File in_f = new File(input_file);
		if (!in_f.exists()) {
			System.out.println(input_file + " does not exist");
			System.exit(0);
		}

		try {
			RandomAccessFile din = new RandomAccessFile(in_f, "rw");
			Student s = new Student();
			Boolean found = false;
			int size = 0;
			LinkedList avail = new LinkedList(); //delete된 정보들 저장

			System.out.println("File Size : " + din.length());
			while (true) {
				if ((size = s.readStudent(din)) < 0)
					break; // if EOF
				
				//번호
				else if (kk.equals("p")) {
					if (Integer.parseInt(name) == 0) {
						found = true;

						if(s.number.contains("*"))System.out.println("deleted data");
						else System.out.println("[" + size + " bytes] " + s);
						continue;
					} else if (pcnt == Integer.parseInt(name)) {
						found = true;
						if(s.number.contains("*"))System.out.println("deleted data");
						else System.out.println("[" + size + " bytes] " + s);
						break;
					} else {
						pcnt++;
						sum += size + 4;
						if (sum == din.length()) {
							errcat = 1;
							break;
						}
						continue;
					}
					// s.printStudent();{
				} 
				
				
				//학번
				else if (kk.equals("s")) {
					if (s.number.equals(name)) {
						found = true;
						if(s.number.contains("*"))System.out.println("deleted data");
						else System.out.println("[" + size + " bytes] " + s);
						break;
					} else
						continue;
				}
				
				//이름
				else if (kk.equals("n")) {
					if (s.name.equals(name)) {
						found = true;
						if(s.number.contains("*"))System.out.println("deleted data");
						else System.out.println("[" + size + " bytes] " + s);
					} else
						continue;
				}
				
				//주소
				else if (kk.equals("a")) {
					if (s.address.contains(name)) {
						found = true;
						if(s.number.contains("*"))System.out.println("deleted data");
						else System.out.println("[" + size + " bytes] " + s);
					} else
						continue;
				}
				
				//삭제
				else if(kk.equals("d")) {
					if(Integer.parseInt(name)==0) {
						if(s.number.contains("*")) 
							System.out.println("["+s.count+"]"+" 삭제된 공간 "+size+" 바이트");
						found=true;
					}
					else {

						if(s.number.equals(name)) {
							found=true;
							System.out.println("[" + size + " bytes] " + s + "delete");
							
							System.out.println();
							din.seek(din.getFilePointer()-size);
							din.writeBytes("*");
							avail.add(din.getFilePointer()-size);
							break;
						}
						pcnt++;
					}
				}
				
				//업데이트
				else if(kk.equals("u")) {
					number=args[2];
					if(s.number.equals(number)) {
						name=args[3].equals("-")? s.name:args[3];
						gender=args[4].equals("-")? Character.toString(s.gender):args[4];
						phone_no=args[5].equals("-")? s.phone_no:args[5];
						address=args[6].equals("-")? s.address:args[6];
						
						RandomAccessFile dout = new RandomAccessFile(in_f, "rw");
						found=true;
					int sizeu=number.getBytes().length+name.getBytes().length+gender.getBytes().length+phone_no.getBytes().length+address.getBytes().length;
					
					if(sizeu<=size-5) {
						dout.seek(din.getFilePointer()-size);
						s.name=name;
						s.gender=gender.charAt(0);
						s.phone_no=phone_no;
						s.address=address;
						s.storeOneStudent(dout);
					}
					else {
						s.name=name;
						s.gender=gender.charAt(0);
						s.phone_no=phone_no;
						s.address=address;
						dout.seek(din.getFilePointer()-size);
						dout.writeBytes("*");
						avail.add(dout.getFilePointer());
						System.out.println(avail);
						dout.seek(din.length());
						s.storeOneStudent(dout);
					}

					System.out.println("[" + size + " bytes] " + s + "is update");
					dout.close();
					break;
					}
				}
				
				//그외
				else {
					found = true;
					System.out.println("error..");
					break;
				}
			}
			if (name != null && !found) {
				if (errcat == 1)
					System.out.println("error");
				else
					System.out.println("No " + name + "'s student exists");
			}
			din.close();
		} catch (IOException err) {
			System.out.println("file I/O error..");
		}
	}

}