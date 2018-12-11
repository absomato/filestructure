package student3;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;

public class ManageStudents {

	public static void main(String[] args) {
		if (args.length != 3 && args.length != 2 && args.length!= 7) {
			System.out.println("USAGE: java ReadStudents   입력파일명   [학생이름]");
			System.exit(0);
		}
		String number=null;
		String name = null;
		String gender=null;
		String phone_no=null;
		String address=null;
		String kk = null;
		int errcat=0;
		int sw=0;//마지막 출력 할지 안할지 스위치
		
		if(args.length==3) {
		name = args[2];
		}
		else if(args.length==7){
			number=args[2];
		}
		kk = args[1];
		String input_file = args[0];
		

		File in_f = new File(input_file);
		if (!in_f.exists()) {
			System.out.println(input_file + " does not exist");
			System.exit(0);
		}
		ArrayList datag = new ArrayList(); //학생들 정보를 저장
		ArrayList<String> datas; //학생 정보를 저장
		LinkedList<Integer> avail = new LinkedList(); //delete된 정보들 저장
		ArrayList<String> datan=new ArrayList();//존재하는 학번들을 저장
		
		try {
			RandomAccessFile din = new RandomAccessFile(in_f, "rw");
			Student s = new Student();
			Boolean found = false;
			int size = 0;
			
			
			if(!args[1].equals("i"))
			System.out.println("File Size : " + din.length());
			while (true) {
				if ((size = s.readStudent(din)) < 0)
					break; // if EOF
				
					datas=new ArrayList<String>();
					
					datas.add(s.number);
					if(s.number.contains("*"))avail.add(datag.size()-1);
					datan.add(s.number);
					datas.add(s.name);
					//char
					datas.add(String.valueOf(s.gender));
					datas.add(s.phone_no);
					datas.add(s.address);
					datas.add(String.valueOf(size));
					
					//p 번호 입력시 출력
					if(kk.equals("p")) {
						if(Integer.parseInt(name)==0) {
							found=true;
							if(datas.get(0).contains("*"))System.out.println("deleted data");
							else System.out.println(s);
						}
						else if(Integer.parseInt(name)==datag.size()) {
							found=true;
							if(datas.get(0).contains("*"))System.out.println("deleted data");
							else System.out.println(s);
							break;
						}
						errcat=1;
					}
					
					//s 학번 입력시 출력
					else if(kk.equals("s")) {
						if(name.equals(datas.get(0))) {
							found=true;
							if(datas.get(0).contains("*"))System.out.println("deleted data");
							else System.out.println(s);
							break;
						}
						errcat=1;
					}
					
					
					
					//n 이름 입력시 출력
					else if(kk.equals("n")) {
						if(name.equals(datas.get(1))) {
							found=true;
							if(datas.get(0).contains("*"))System.out.println("deleted data");
							else System.out.println(s);
							break;
						}
						errcat=1;
					}
					
					//d 삭제 기능
					else if(kk.equals("d")) {
						if(name.equals("0"))found=true;
						if(name.equals(datas.get(0))) {
							found=true;
							datas.set(0,"*"+datas.get(0).substring(1,7));
							avail.add(datag.size()-1);
							datan.set(datag.size()-1, datas.get(0));
							System.out.println(s+"is delete");
						}
						errcat=1;
					}
					
					//u 업데이트 기증
					else if(kk.equals("u")) {
						if(number.equals(datas.get(0))) {
							found=true;
							name=args[3].equals("-")?datas.get(1):args[3];
							gender=args[4].equals("-")?datas.get(2):args[4];
							phone_no=args[5].equals("-")?datas.get(3):args[5];
							address=args[6].equals("-")?datas.get(4):args[6];
							int sizeu=name.getBytes().length+gender.getBytes().length+number.getBytes().length+phone_no.getBytes().length+address.getBytes().length;
							//u의 크기가 클 경우
							if(sizeu>size) {
								datas.set(0,"*"+datas.get(0).substring(1,7));
								avail.add(datag.size()-1);
							}
							//u의 크키가 작거나 같을 경우
							else {
								datas.set(1, name);
								datas.set(2, gender);
								datas.set(3, phone_no);
								datas.set(4, address);
								sw=1;
							}
							System.out.println(s+"is change");
						}
						errcat=1;
					}
					
					//i 삽입 기능
					else if(kk.equals("i")) {
						found=true;
					}
						
				datag.add(datas);
			}
				
			if (name != null && !found) {
				if(errcat==1)System.out.println("error");
				else System.out.println("No " + name + "'s student exists");
			}
			din.close();
		} catch (IOException err) {
			System.out.println("file I/O error..");
		}
		//삭제 0입력 시 삭제된 데이터 출력
		if(kk.equals("d") && sw==0) {
			sw=1;
			if(Integer.parseInt(name)==0) {
				for(int i=0;i<avail.size();i++) {
					datas=(ArrayList<String>) datag.get(avail.get(i));
					System.out.println("["+avail.get(i)+"]삭제된 공간 "+datas.get(5)+" 바이트");
		}}}
		
		//업데이트 가장 마지막에 입력

		if(kk.equals("u") && sw==0) {
			sw=2;
			datas=new ArrayList();
			datas.add(number);
			datas.add(name);
			datas.add(gender);
			datas.add(phone_no);
			datas.add(address);
			datag.add(datas);
		}
		if(kk.equals("i")) {
			input_file=args[2]+"_dat";
			File in_f2 = new File(input_file);
			WriteStudents.main(args);
			try {
				RandomAccessFile din = new RandomAccessFile(in_f2, "rw");
				Student s = new Student();
				int size = 0;
				int cnt=0;

				while (true) {
					if ((size = s.readStudent(din)) < 0)
						break; // if EOF

					//i입력시 데이터가 이미 존재하여 수정
					if((cnt=datan.indexOf(s.number))>=0) {

						datas=(ArrayList<String>) datag.get(cnt);

						//수정할 데이터가 작을 때
						if(Integer.parseInt(datas.get(5))>=size) {
							datas.set(1, s.name);
							datas.set(2, String.valueOf(s.gender));
							datas.set(3, s.phone_no);
							datas.set(4, s.address);
							continue;
						}
						//수정할 데이터가 클 때
						else {
							datas.set(0,"*"+datas.get(0).substring(1,7));
							avail.add(cnt);
						}
					}
					//없을경우
					else {
						cnt=0;
						while(avail.size()>cnt) {
							datas=(ArrayList<String>) datag.get(avail.get(cnt));
							if(size<Integer.parseInt(datas.get(5))) {
								datas.set(0, s.number);
								datas.set(1, s.name);
								datas.set(2, String.valueOf(s.gender));
								datas.set(3, s.phone_no);
								datas.set(4, s.address);
								avail.remove(cnt);
								break;
							}
							cnt++;
						}
						}
					datas=new ArrayList<String>();
					datas.add(s.number);
					datas.add(s.name);
					//char
					datas.add(String.valueOf(s.gender));
					datas.add(s.phone_no);
					datas.add(s.address);
					datag.add(datas);

				}
		din.close();
		System.out.println("insert complete...");
		}catch (IOException err) {
			System.out.println("file I/O error..");
		}
		}
		
		
		try {
			RandomAccessFile din = new RandomAccessFile(in_f, "rw");
			RandomAccessFile dout = new RandomAccessFile(in_f, "rw");
			Student s = new Student();
			int size = 0;
			int cnt=0;
			//메모장 복사 시작
			while (cnt<=datag.size()) {
				if ((size = s.readStudent(din)) < 0)
					break; // if EOF
				
				
				datas=(ArrayList) datag.get(cnt++);
				s.number=(String) datas.get(0);
				s.name=(String) datas.get(1);
				//char
				s.gender=datas.get(2).charAt(0);
				s.phone_no=(String) datas.get(3);
				s.address=(String) datas.get(4);
				dout.seek(din.getFilePointer()-size);
				s.storeOneStudent(dout);
			}
			if(kk.equals("u") && sw==2) {
				datas=(ArrayList) datag.get(cnt);
				s.number=(String) datas.get(0);
				s.name=(String) datas.get(1);
				//char
				s.gender=datas.get(2).charAt(0);
				s.phone_no=(String) datas.get(3);
				s.address=(String) datas.get(4);
				dout.seek(din.getFilePointer());
				s.storeOneStudent(dout);
			}
			else if(kk.equals("i")) {
				while(cnt<=datag.size()) {
					datas=(ArrayList) datag.get(cnt++);
					s.number=(String) datas.get(0);
					s.name=(String) datas.get(1);
					//char
					s.gender=datas.get(2).charAt(0);
					s.phone_no=(String) datas.get(3);
					s.address=(String) datas.get(4);
					s.storeOneStudent(dout);
					dout.seek(dout.length());
				}
			}
			din.close();
			dout.close();
			} catch (IOException err) {
				System.out.println("file I/O error..");
			}
	}

	private static Object substring(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

}