package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.FilenameFilter;

public class read1 {

	public static void main(String[] args) throws IOException {
		check();
	}

	public static void md5(String mMd5) throws IOException { // 判斷md5的

		String line;
		String[][] splitname = new String[1][2];// 把鄉鎮區存成二維陣列

		String[] token = mMd5.split(",");// 把鄉鎮區用,分開存入陣列
		for (int j = 0; j < token.length; j++) {
			splitname[0][j] = token[j];
		}

		InputStreamReader isr = new InputStreamReader(new FileInputStream("/Users/josephinelai/Desktop/Output/"
				+ splitname[0][0] + "/" + splitname[0][1] + "/" + splitname[0][0] + splitname[0][1] + "Md5.txt"),
				"UTF-8");
		BufferedReader read = new BufferedReader(isr);
		FileWriter md5output = new FileWriter(
				"/Users/josephinelai/Desktop/md5output/" + splitname[0][0] + splitname[0][1] + "Md5.txt", true);

		while ((line = read.readLine()) != null) {
			String[] token1 = line.split(":");// 把鄉鎮區用,分開存入陣列
			md5output.write(token1[0] + "\n");
		}
		read.close();
		md5output.close();
	}

	public static void check() throws IOException { // 判斷有無重複的

		ArrayList<String> title = cityname();
		FileWriter checkk = new FileWriter("/Users/josephinelai/Desktop/checkcity.txt", true);// 判斷現在有幾個鄉鎮市區是已經被做過的了
		InputStreamReader isr = new InputStreamReader(new FileInputStream("/Users/josephinelai/Desktop/checkcity.txt"),
				"UTF-8");
		// 讀這個文件的內容
		BufferedReader read = new BufferedReader(isr);

		String[][] address = new String[title.size()][2];// 把鄉鎮區存成二維陣列

		for (int i = 0; i < title.size(); i++) {
			String[] token = title.get(i).split(",");// 把鄉鎮區用,分開存入陣列
			for (int j = 0; j < token.length; j++) {
				address[i][j] = token[j];
			}
		}

		if (read.readLine() == null) {
			for (int i = 0; i < address.length; i++) {
				String line = address[i][0] + "," + address[i][1];
				group(line);// 做新增
				md5(line);
				checkk.write(line + "\n");
				System.out.println("我是1");
			}

		} else {
			String line1;// 讀一行行文件黨
			boolean flag = true;
			ArrayList<String> address2 = new ArrayList<String>();
			InputStreamReader isr1 = new InputStreamReader(
					new FileInputStream("/Users/josephinelai/Desktop/checkcity.txt"), "UTF-8");
			BufferedReader read2 = new BufferedReader(isr1);
			while ((line1 = read2.readLine()) != null) {
				address2.add(line1);
			}
			read2.close();
			System.out.println("我是2");
			for (int i = 0; i < address.length; i++) {
				String line = address[i][0] + "," + address[i][1];
				for (int j = 0; j < address2.size(); j++) {
					if (line.equals(address2.get(j))) {
						repeat(line);
					}
				}
				if (flag == true) {
					System.out.println("我是4");
					group(line);// 做新增
					md5(line);
					checkk.write(line + "\n");
				}
				flag = true;
			}
		}
		read.close();
		checkk.close();
	}

	public static void group(String fromcheck) throws IOException { // 產生rdf的

		// ArrayList<String> title = cityname(); //接收所有的城市鄉鎮名稱
		ArrayList<String> city = new ArrayList<String>(); // 新創一個範型

		String line;
		String[][] address = new String[1][2];// 把鄉鎮區存成二維陣列

		String[] token = fromcheck.split(",");// 把鄉鎮區用,分開存入陣列
		for (int j = 0; j < token.length; j++) {
			address[0][j] = token[j];
		}
		InputStreamReader isr = new InputStreamReader(new FileInputStream("/Users/josephinelai/Desktop/Output/"
				+ address[0][0] + "/" + address[0][1] + "/" + address[0][0] + address[0][1] + "Out.txt"), "UTF-8");
		// 讀這個文件的內容
		BufferedReader read = new BufferedReader(isr);
		read.readLine(); // 讀第一行address

		while ((line = read.readLine()) != null) {
			city.add(line + "\n"); // 把內容一行一行存入犯行陣列
		}
		city.add(" ");

		int cnt = 0;// 判斷有幾筆一樣的address key
		String[][] all = new String[city.size()][7];
		FileWriter fw = new FileWriter("/Users/josephinelai/Desktop/全縣市/" + address[0][0] + address[0][1] + "Out.txt");
		boolean flag = true; // 判斷是否為重複地址中的第一個

		/* 寫擋前面 */
		fw.write("<?xml version=\"1.0\"?>\n");
		fw.write(" <rdf:RDF\n");
		fw.write("  xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n");
		fw.write("  xmlns:feature=\"http://www.linkeddata.com/address-features#\">\n\n");

		/* 把記事本內容存入陣列 */
		for (int i = 0; i < city.size(); i++) {
			String[] token1 = city.get(i).split(",");
			for (int j = 0; j < token1.length; j++) {
				all[i][j] = token1[j];
			}
		}
		try {
			/* 跑迴圈，做rdf */
			for (int i = 0; i < city.size(); i++) {
				cnt = 0;
				for (int j = i + 1; j < city.size(); j++) {
					if (all[i][0].equals(all[j][0])) { // 跟下一個address key一樣

						if (all[i][2].equals("null") & cnt == 0) { // 目前掃到有兩個地址但第一個是null

							if (all[j][0].equals(all[j + 1][0])) { // 有兩個以上且第一個是null(做第一個而已)
								fw.write("   <rdf:Description rdf:about=\"http://www.linkeddata.com/address#"
										+ all[j][2] + "\">\n");
								fw.write("    <feature:has_next rdf:resource=\"http://www.linkeddata.com/address#"
										+ all[j + 1][2] + "\"/>\n");
								fw.write("    <feature:change_time>" + all[i][5] + "</feature:change_time>\n");
								all[i][6] = all[i][6].replaceAll("\n", "");
								fw.write("    <feature:type>" + all[i][6] + "</feature:type>\n");
								fw.write("   </rdf:Description>\n\n");
							} else { // 只有兩個且第一個是null
								fw.write("   <rdf:Description rdf:about=\"http://www.linkeddata.com/address#"
										+ all[j][2] + "\">\n");
								fw.write("    <feature:change_time>" + all[i][5] + "</feature:change_time>\n");
								all[i][6] = all[i][6].replaceAll("\n", "");
								fw.write("    <feature:type>" + all[i][6] + "</feature:type>\n");
								fw.write("   </rdf:Description>\n\n");
							}

						} else if (all[i][2].equals("null") & cnt >= 1) { // 有兩個地址以上且第一個值是null
							if (all[j][0].equals(all[j + 1][0])) { // 重複地址中的任一個(除最後一個外)
								fw.write("   <rdf:Description rdf:about=\"http://www.linkeddata.com/address#"
										+ all[j][2] + "\">\n");
								fw.write("    <feature:has_next rdf:resource=\"http://www.linkeddata.com/address#"
										+ all[j + 1][2] + "\"/>\n");
								fw.write("    <feature:has_pre rdf:resource=\"http://www.linkeddata.com/address#"
										+ all[j - 1][2] + "\"/>\n");
								fw.write("    <feature:change_time>" + all[j - 1][5] + "</feature:change_time>\n");
								all[j - 1][6] = all[j - 1][6].replaceAll("\n", "");
								fw.write("    <feature:type>" + all[j - 1][6] + "</feature:type>\n");
								fw.write("   </rdf:Description>\n\n");
							} else { // 重複地址中的最後一個
								fw.write("   <rdf:Description rdf:about=\"http://www.linkeddata.com/address#"
										+ all[j][2] + "\">\n");
								fw.write("    <feature:has_pre rdf:resource=\"http://www.linkeddata.com/address#"
										+ all[j - 1][2] + "\"/>\n");
								fw.write("    <feature:change_time>" + all[j - 1][5] + "</feature:change_time>\n");
								all[j - 1][6] = all[j - 1][6].replaceAll("\n", "");
								fw.write("    <feature:type>" + all[j - 1][6] + "</feature:type>\n");
								fw.write("   </rdf:Description>\n\n");
							}
						} else if (!all[i][2].equals("null")) { // 有兩個或以上但兩個都沒有null
							if (flag == true) { // 判斷是否為重複地址中的第一個
								fw.write("   <rdf:Description rdf:about=\"http://www.linkeddata.com/address#"
										+ all[i][2] + "\">\n");
								fw.write("    <feature:has_next rdf:resource=\"http://www.linkeddata.com/address#"
										+ all[i + 1][2] + "\"/>\n");
								fw.write("    <feature:change_time>" + all[i][5] + "</feature:change_time>\n");
								all[i][6] = all[i][6].replaceAll("\n", "");
								fw.write("    <feature:type>" + all[i][6] + "</feature:type>\n");
								fw.write("   </rdf:Description>\n\n");
								flag = false; // 防止判斷每一個都是第一個
								j--; // 讓j停留在同一點
								cnt--; // 因為讓j停留在同一點，所以不讓他也加1
							} else if (all[j][0].equals(all[j - 1][0]) & all[j][0].equals(all[j + 1][0])) { // 判斷是否為重複地址中的隨機一個(除最後一個外)
								fw.write("   <rdf:Description rdf:about=\"http://www.linkeddata.com/address#"
										+ all[j][2] + "\">\n");
								fw.write("    <feature:has_next rdf:resource=\"http://www.linkeddata.com/address#"
										+ all[j + 1][2] + "\"/>\n");
								fw.write("    <feature:has_pre rdf:resource=\"http://www.linkeddata.com/address#"
										+ all[j - 1][2] + "\"/>\n");
								fw.write("    <feature:change_time>" + all[j][5] + "</feature:change_time>\n");
								all[j][6] = all[j][6].replaceAll("\n", "");
								fw.write("    <feature:type>" + all[j][6] + "</feature:type>\n");
								fw.write("   </rdf:Description>\n\n");
							} else { // 判斷是否為重複地址中的最後一個
								fw.write("   <rdf:Description rdf:about=\"http://www.linkeddata.com/address#"
										+ all[j][2] + "\">\n");
								fw.write("    <feature:has_pre rdf:resource=\"http://www.linkeddata.com/address#"
										+ all[j - 1][2] + "\"/>\n");
								fw.write("    <feature:change_time>" + all[j][5] + "</feature:change_time>\n");
								all[j][6] = all[j][6].replaceAll("\n", "");
								fw.write("    <feature:type>" + all[j][6] + "</feature:type>\n");
								fw.write("   </rdf:Description>\n\n");
							}
						}
						cnt++;
					} else if (!all[i][0].equals(all[j][0]) & cnt == 0) { // 只有一個地址
						fw.write("   <rdf:Description rdf:about=\"http://www.linkeddata.com/address#" + all[i][2]
								+ "\">\n");
						fw.write("    <feature:change_time>" + all[i][5] + "</feature:change_time>\n");
						all[i][6] = all[i][6].replaceAll("\n", "");
						fw.write("    <feature:type>" + all[i][6] + "</feature:type>\n");
						fw.write("   </rdf:Description>\n\n");
						break;
					} else if (!all[i][0].equals(all[j][0]) & cnt > 0) { // 取出同樣的地址為止
						// System.out.println("pass");
						fw.write("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
						break;
					}
				}
				flag = true;
				i = i + cnt;
			}
			fw.write("</rdf:RDF>");
			fw.close();

			isr.close();
		} catch (Exception e) {

		}

	}

	public static void repeat(String repeat) throws IOException { // 找重複的是哪一個

		String line;
		int cnt = 0;// 判斷在哪一個位置
		String[][] splitname = new String[1][2];// 把鄉鎮區存成二維陣列
		ArrayList<String> MD5 = new ArrayList<String>();
		ArrayList<String> origMD5 = new ArrayList<String>();

		String[] token = repeat.split(",");// 把鄉鎮區用,分開存入陣列
		for (int j = 0; j < token.length; j++) {
			splitname[0][j] = token[j];
		}

		InputStreamReader isr1 = new InputStreamReader(
				new FileInputStream(
						"/Users/josephinelai/Desktop/md5output/" + splitname[0][0] + splitname[0][1] + "Md5.txt"),
				"UTF-8");
		BufferedReader read = new BufferedReader(isr1);
		InputStreamReader isr2 = new InputStreamReader(new FileInputStream("/Users/josephinelai/Desktop/Output/"
				+ splitname[0][0] + "/" + splitname[0][1] + "/" + splitname[0][0] + splitname[0][1] + "Out.txt"),
				"UTF-8");
		BufferedReader read2 = new BufferedReader(isr2);
		read2.readLine();
		while ((line = read.readLine()) != null) {
			MD5.add(line + "\n");
		}
		while ((line = read2.readLine()) != null) {
			String[] token1 = repeat.split(",");// 把鄉鎮區用,分開存入陣列
			for (int j = 0; j < token1.length; j++) {
				origMD5.add(token1[3] + "\n");
			}
		}

		for (int i = 0; i < origMD5.size(); i++) {
			for (int j = 0; j < MD5.size(); j++) {
				if (!origMD5.get(i).contentEquals(MD5.get(j))) {
					change(splitname[0][0],splitname[0][1],cnt);
				}
			}
			cnt++;
		}
		read.close();
		read2.close();
	}

	public static void change(String city,String country,int cnt) throws IOException { // 修改重複的
		// ArrayList<String> title = cityname(); //接收所有的城市鄉鎮名稱
		ArrayList<String> city1 = new ArrayList<String>(); // 新創一個範型

		String line;

		InputStreamReader isr = new InputStreamReader(new FileInputStream("/Users/josephinelai/Desktop/Output/"
				+ city + "/" + country + "/" + city + country + "Out.txt"), "UTF-8");
		// 讀這個文件的內容
		BufferedReader read = new BufferedReader(isr);
		read.readLine(); // 讀第一行address

		while ((line = read.readLine()) != null) {
			city1.add(line + "\n"); // 把內容一行一行存入犯行陣列
		}
		String[][] all = new String[city1.size()][7];
		for (int i = 0; i < city1.size(); i++) {
			String[] token1 = city1.get(i).split(",");
			for (int j = 0; j < token1.length; j++) {
				all[i][j] = token1[j];
			}
		}
		
		if(all[cnt-1][0].equals(null)){//第一筆新的地址
			
		}else if((!all[cnt-1][0].equals(null))&(!all[cnt][0].equals(all[cnt-1][0]))){//完全新的地址
			
		}else{ //單純更新地址
			
		}
		
		read.close();
	}

	public static ArrayList<String> cityname() { // 讀城市名稱
		File f = new File("/Users/josephinelai/Desktop/Output");
		ArrayList<String> cityname = new ArrayList<String>();

		File[] f1 = f.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return !name.equals(".DS_Store");
			}
		});

		for (int i = 0; i < f.listFiles().length - 1; i++) {
			cityname.add(f1[i].getName());
		}
		ArrayList<String> all = townname(cityname);
		return all;
	}

	public static ArrayList<String> townname(ArrayList<String> a) { // 讀鄉鎮名稱
		ArrayList<String> townname = new ArrayList<String>();

		for (int e = 0; e < a.size(); e++) {
			File f2 = new File("/Users/josephinelai/Desktop/Output/" + a.get(e));
			File[] f3 = f2.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return !name.equals(".DS_Store");
				}
			});

			try {
				for (int i = 0; i < f2.listFiles().length; i++) {
					townname.add(a.get(e) + "," + f3[i].getName());
				}
			} catch (Exception except) {

			}
		}
		return townname;
	}
}
