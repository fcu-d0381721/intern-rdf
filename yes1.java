package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.FilenameFilter;

public class yes {

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
		
		 InputStreamReader isr = new InputStreamReader(new
		 FileInputStream("/Users/josephinelai/Desktop/Output1/"
		 + splitname[0][0] + "/" + splitname[0][1] + "/" + splitname[0][0] +"_"+
		 splitname[0][1] + "Md5.txt"),
		 "UTF-8");
		 BufferedReader read = new BufferedReader(isr);
		 FileWriter md5output = new FileWriter(
		 "/Users/josephinelai/Desktop/md5output/" + splitname[0][0] +
		 splitname[0][1] + "Md5.txt", true);
		
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
						repeat(line); // 去找重複的
						System.out.println("我是7");
						flag = false;
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
				+ address[0][0] + "/" + address[0][1] + "/" + address[0][0] + "_" + address[0][1] + "_OutAddXY.csv"),
				"UTF-8");
		// 讀這個文件的內容
		BufferedReader read = new BufferedReader(isr);
		read.readLine(); // 讀第一行address

		while ((line = read.readLine()) != null) {
			city.add(line + "\n"); // 把內容一行一行存入犯行陣列
		}
		city.add(" ");

		int cnt = 0;// 判斷有幾筆一樣的address key
		String[][] all = new String[city.size()][9];
		FileWriter fw = new FileWriter(
				"/Users/josephinelai/Desktop/全縣市/" + address[0][0] + "_" + address[0][1] + "_OutAddXY.rdf");
		boolean flag = true; // 判斷是否為重複地址中的第一個

		/* 寫擋前面 */
		fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		fw.write(" <rdf:RDF\n");
		fw.write("  xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n");
		fw.write("  xmlns:addr=\"http://addr.sgis.tw/ontology/\">\n");

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
				int k = i;
				for (int j = i + 1; j < city.size(); j++) {
					if (all[k][4].equals(all[j][1]) & !all[k][1].equals(all[k][4])) { // 跟下一個address
																						// key一樣

						if (all[i][2].equals("null") & cnt == 0 & !all[j][6].equals("0")) { // 目前掃到有兩個地址但第一個是null

							if (all[j][4].equals(all[j + 1][1])) { // 有兩個以上且第一個是null(做第一個而已)
								fw.write("   <rdf:Description rdf:about=\"http://addr.sgis.tw/address/"
										+ all[j][2] + "\">\n");
								fw.write("    <addr:has_next rdf:resource=\"http://addr.sgis.tw/address/"
										+ all[j + 1][2] + "\"/>\n");
								fw.write("    <addr:has_STime>" + all[i][5] + "</addr:has_STime>\n");
								fw.write("    <addr:has_ETime>" + all[j][5] + "</addr:has_ETime>\n");
								all[j][8] = all[j][8].replaceAll("\n", "");
								fw.write("    <addr:type>" + all[i][6] + "</addr:type>\n");
								fw.write("    <addr:lat>" + all[j][7] + "</addr:lat>\n");
								fw.write("    <addr:lng>" + all[j][8] + "</addr:lng>\n");
								fw.write("   </rdf:Description>\n\n");
							} else { // 只有兩個且第一個是null
								fw.write("   <rdf:Description rdf:about=\"http://addr.sgis.tw/address/"
										+ all[j][2] + "\">\n");
								fw.write("    <addr:has_STime>" + all[i][5] + "</addr:has_STime>\n");
								// fw.write(" <addr:has_ETime>" + "unknown" +
								// "</addr:has_ETime>\n");
								all[j][8] = all[j][8].replaceAll("\n", "");
								fw.write("    <addr:type>" + all[i][6] + "</addr:type>\n");
								fw.write("    <addr:lat>" + all[j][7] + "</addr:lat>\n");
								fw.write("    <addr:lng>" + all[j][8] + "</addr:lng>\n");
								fw.write("   </rdf:Description>\n\n");
							}

						} else if (all[i][2].equals("null") & cnt >= 1) { // 有兩個地址以上且第一個值是null
							if (all[j][4].equals(all[j + 1][1])) { // 重複地址中的任一個(除最後一個外)
								fw.write("   <rdf:Description rdf:about=\"http://addr.sgis.tw/address/"
										+ all[j][2] + "\">\n");
								fw.write("    <addr:has_next rdf:resource=\"http://addr.sgis.tw/address/"
										+ all[j + 1][2] + "\"/>\n");
								fw.write("    <addr:has_pre rdf:resource=\"http://addr.sgis.tw/address/"
										+ all[j - 1][2] + "\"/>\n");
								fw.write("    <addr:has_STime>" + all[j - 1][5] + "</addr:has_STime>\n");
								fw.write("    <addr:has_ETime>" + all[j][5] + "</addr:has_ETime>\n");
								all[j][8] = all[j][8].replaceAll("\n", "");
								fw.write("    <addr:type>" + all[j - 1][6] + "</addr:type>\n");
								fw.write("    <addr:lat>" + all[j][7] + "</addr:lat>\n");
								fw.write("    <addr:lng>" + all[j][8] + "</addr:lng>\n");
								fw.write("   </rdf:Description>\n\n");
							} else if (all[j][6].equals("5")) { // 重複地址中的最後一個且type=5(刪除)
								fw.write("   <rdf:Description rdf:about=\"http://addr.sgis.tw/address/"
										+ all[j][2] + "\">\n");
								fw.write("    <addr:has_pre rdf:resource=\"http://addr.sgis.tw/address/"
										+ all[j - 1][2] + "\"/>\n");
								fw.write("    <addr:has_STime>" + all[j - 1][5] + "</addr:has_STime>\n");
								fw.write("    <addr:has_ETime>" + all[j][5] + "</addr:has_ETime>\n");
								all[j][8] = all[j][8].replaceAll("\n", "");
								fw.write("    <addr:type>" + all[j - 1][6] + "</addr:type>\n");
								fw.write("    <addr:lat>" + all[j][7] + "</addr:lat>\n");
								fw.write("    <addr:lng>" + all[j][8] + "</addr:lng>\n");
								fw.write("   </rdf:Description>\n\n");
							} else { // 重複地址中的最後一個
								fw.write("   <rdf:Description rdf:about=\"http://addr.sgis.tw/address/"
										+ all[j][2] + "\">\n");
								fw.write("    <addr:has_pre rdf:resource=\"http://addr.sgis.tw/address/"
										+ all[j - 1][2] + "\"/>\n");
								fw.write("    <addr:has_STime>" + all[j - 1][5] + "</addr:has_STime>\n");
								all[j][8] = all[j][8].replaceAll("\n", "");
								fw.write("    <addr:type>" + all[j - 1][6] + "</addr:type>\n");
								fw.write("    <addr:lat>" + all[j][7] + "</addr:lat>\n");
								fw.write("    <addr:lng>" + all[j][8] + "</addr:lng>\n");
								fw.write("   </rdf:Description>\n\n");
							}
						} else if (!all[i][2].equals("null")) { // 有兩個或以上但兩個都沒有null
							if (flag == true) { // 判斷是否為重複地址中的第一個
								fw.write("   <rdf:Description rdf:about=\"http://addr.sgis.tw/address/"
										+ all[i][2] + "\">\n");
								fw.write("    <addr:has_next rdf:resource=\"http://addr.sgis.tw/address/"
										+ all[i + 1][2] + "\"/>\n");
								fw.write("    <addr:has_STime>" + "unknown" + "</addr:has_STime>\n");
								fw.write("    <addr:has_ETime>" + all[i][5] + "</addr:has_ETime>\n");
								all[i][8] = all[i][8].replaceAll("\n", "");
								fw.write("    <addr:type>" + "unknown" + "</addr:type>\n");
								fw.write("    <addr:lat>" + all[i][7] + "</addr:lat>\n");
								fw.write("    <addr:lng>" + all[i][8] + "</addr:lng>\n");
								fw.write("   </rdf:Description>\n\n");
								flag = false; // 防止判斷每一個都是第一個
								j--; // 讓j停留在同一點
								cnt--; // 因為讓j停留在同一點，所以不讓他也加1
								k--;
							} else if (all[j][1].equals(all[j - 1][4]) & all[j][4].equals(all[j + 1][1])) { // 判斷是否為重複地址中的隨機一個(除最後一個外)
								fw.write("   <rdf:Description rdf:about=\"http://addr.sgis.tw/address/"
										+ all[j][2] + "\">\n");
								fw.write("    <addr:has_next rdf:resource=\"http://addr.sgis.tw/address/"
										+ all[j + 1][2] + "\"/>\n");
								fw.write("    <addr:has_pre rdf:resource=\"http://addr.sgis.tw/address/"
										+ all[j - 1][2] + "\"/>\n");
								fw.write("    <addr:has_STime>" + all[j - 1][5] + "</addr:has_STime>\n");
								fw.write("    <addr:has_ETime>" + all[j][5] + "</addr:has_ETime>\n");
								all[j][8] = all[j][8].replaceAll("\n", "");
								fw.write("    <addr:type>" + all[j - 1][6] + "</addr:type>\n");
								fw.write("    <addr:lat>" + all[j][7] + "</addr:lat>\n");
								fw.write("    <addr:lng>" + all[j][8] + "</addr:lng>\n");
								fw.write("   </rdf:Description>\n\n");
							} else if (all[j][6].equals("5")) { // 判斷是否為重複地址中的最後一個且type=5
								fw.write("   <rdf:Description rdf:about=\"http://addr.sgis.tw/address/"
										+ all[j][2] + "\">\n");
								fw.write("    <addr:has_pre rdf:resource=\"http://addr.sgis.tw/address/"
										+ all[j - 1][2] + "\"/>\n");
								fw.write("    <addr:has_STime>" + all[j - 1][5] + "</addr:has_STime>\n");
								fw.write("    <addr:has_ETime>" + all[j][5] + "</addr:has_ETime>\n");
								all[j][8] = all[j][8].replaceAll("\n", "");
								fw.write("    <addr:type>" + all[j - 1][6] + "</addr:type>\n");
								fw.write("    <addr:lat>" + all[j][7] + "</addr:lat>\n");
								fw.write("    <addr:lng>" + all[j][8] + "</addr:lng>\n");
								fw.write("   </rdf:Description>\n\n");
							} else { // 判斷是否為重複地址中的最後一個
								fw.write("   <rdf:Description rdf:about=\"http://addr.sgis.tw/address/"
										+ all[j][2] + "\">\n");
								fw.write("    <addr:has_pre rdf:resource=\"http://addr.sgis.tw/address/"
										+ all[j - 1][2] + "\"/>\n");
								fw.write("    <addr:has_Sime>" + all[j - 1][5] + "</addr:has_STime>\n");
								all[j][8] = all[j][8].replaceAll("\n", "");
								fw.write("    <addr:type>" + all[j - 1][6] + "</addr:type>\n");
								fw.write("    <addr:lat>" + all[j][7] + "</addr:lat>\n");
								fw.write("    <addr:lng>" + all[j][8] + "</addr:lng>\n");
								fw.write("   </rdf:Description>\n\n");
							}
						}
						cnt++;
						k++;
					} else if (!all[i][4].equals(all[j][1]) & cnt == 0 & all[i][6].equals("5")) { // 只有一個地址
						fw.write("   <rdf:Description rdf:about=\"http://addr.sgis.tw/address/" + all[i][2]
								+ "\">\n");
						fw.write("    <addr:has_STime>" + "unknown" + "</addr:has_STime>\n");
						fw.write("    <addr:has_ETime>" + all[i][5] + "</addr:has_ETime>\n");
						all[i][8] = all[i][8].replaceAll("\n", "");
						fw.write("    <addr:type>" + "unknown" + "</addr:type>\n");
						fw.write("    <addr:lat>" + all[i][7] + "</addr:lat>\n");
						fw.write("    <addr:lng>" + all[i][8] + "</addr:lng>\n");
						fw.write("   </rdf:Description>\n\n");
						break;
					} else if (all[i][1].equals(all[i][4])) {
						break;
					} else if (!all[i][0].equals(all[j][0]) & cnt > 0) { // 取出同樣的地址為止
						// System.out.println("pass");
						// fw.write("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
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
		String line2;
		boolean ch = true;
		String[][] splitname = new String[1][2];// 把鄉鎮區存成二維陣列
		ArrayList<String> MD5 = new ArrayList<String>();
		ArrayList<String> origMD5 = new ArrayList<String>();

		String[] token = repeat.split(",");// 把鄉鎮區用,分開存入陣列
		for (int j = 0; j < token.length; j++) {
			splitname[0][j] = token[j];
		}
		System.out.println("我是8");

		InputStreamReader isr1 = new InputStreamReader(
				new FileInputStream(
						"/Users/josephinelai/Desktop/md5output/" + splitname[0][0] + splitname[0][1] + "Md5.txt"),
				"UTF-8");
		BufferedReader read = new BufferedReader(isr1); // 讀創造的md5

		InputStreamReader isr2 = new InputStreamReader(new FileInputStream("/Users/josephinelai/Desktop/Output1/"
				+ splitname[0][0] + "/" + splitname[0][1] + "/" + splitname[0][0] +"_"+ splitname[0][1] + "Md5.txt"),
				"UTF-8");
		BufferedReader read2 = new BufferedReader(isr2); // 讀原始的md5

		while ((line = read.readLine()) != null) {
			MD5.add(line + "\n");

		}
		while ((line2 = read2.readLine()) != null) {
			String[] token1 = line2.split(":");// 把鄉鎮區用,分開存入陣列
			origMD5.add(token1[0] + "\n");
		}

		for (int i = 0; i < origMD5.size(); i++) {
			for (int j = 0; j < MD5.size(); j++) {

				if (origMD5.get(i).contentEquals(MD5.get(j))) {
					ch = false; // 假如有找到一樣的md5就不做
				}
			}
			if (ch == true) {
				System.out.println("我是9");
				String md5 = origMD5.get(i);
				change(splitname[0][0], splitname[0][1], md5);
			}
			ch = true;
		}

		read.close();
		read2.close();
	}

	public static void change(String city, String country, String md5) throws IOException { // 修改重複的

		System.out.println("我是10");
		ArrayList<String> ori = new ArrayList<String>();
		String line;
		InputStreamReader isr = new InputStreamReader(new FileInputStream(
				"/Users/josephinelai/Desktop/Output/" + city + "/" + country + "/" + city +"_"+ country + "_OutAddXY.csv"),
				"UTF-8");
		BufferedReader read = new BufferedReader(isr);
		read.readLine();

		while ((line = read.readLine()) != null) {
			ori.add(line + "\n"); // 把內容一行一行存入犯行陣列
		}

		String[][] orisp = new String[ori.size()][7];
		for (int i = 0; i < ori.size(); i++) {
			String[] token1 = ori.get(i).split(",");
			for (int j = 0; j < token1.length; j++) {
				orisp[i][j] = token1[j];// 存入一般陣列
			}
		}
		System.out.println("我是11");
		read.close();

		InputStreamReader isr1 = new InputStreamReader(
				new FileInputStream("/Users/josephinelai/Desktop/全縣市/" + city +"_"+ country + "_OutAddXY.rdf"), "UTF-8");
		BufferedReader read1 = new BufferedReader(isr1);

		ArrayList<String> list = new ArrayList<String>();

		for (int k = 0; k < orisp.length; k++) { // 找出原始文件中，一樣md5的地方
			String lin;
			int cct = 0;
			boolean f = true; // 判斷是否有生產出了
			md5 = md5.replace("\n", "");
			if (orisp[k][3].equals(md5)) { // 我找到位置了
				System.out.println("我是13");
				// System.out.println(md5);
				while ((lin = read1.readLine()) != null) { // 讀取寫好的黨
					if (lin.contentEquals("   <rdf:Description rdf:about=\"http://addr.sgis.tw/address/"
							+ orisp[k - 1][2] + "\">") & orisp[k][0].equals(orisp[k - 1][0])
							& !orisp[k - 2][2].equals("null")) { // 找出一樣的md5前一段，如編號一樣，兩個以上的地址
						System.out.println("我是14");
						String accept = half(orisp[k - 1][2], orisp[k - 2][2], orisp[k][2], orisp[k - 2][5],
								orisp[k - 2][6], orisp[k][2], orisp[k - 1][2], orisp[k - 1][5], orisp[k - 1][6]);
						list.add(accept);
						cct = 4;
						f = false;
						continue;
					} else if (lin
							.contentEquals("   <rdf:Description rdf:about=\"http://addr.sgis.tw/address/"
									+ orisp[k - 1][2] + "\">")
							& orisp[k][0].equals(orisp[k - 1][0]) & orisp[k - 2][2].equals("null")) { // 找出一樣的md5前一段，如編號一樣，只有兩個而已
						System.out.println("我是16");
						String accept2 = part(orisp[k - 1][2], orisp[k][2], orisp[k - 2][5], orisp[k - 2][6],
								orisp[k - 1][5], orisp[k - 1][6]);
						list.add(accept2);
						cct = 3;
						f = false;
						continue;
					} else if (lin.contentEquals("</rdf:RDF>")) { // 找出最後一段
						continue;
					}
					if (cct == 0) {
						list.add(lin); // 除了以上三個需特別抓出來做，其餘直接丟進去
					} else {
						cct--;
					}
				}
				if (f == true) { // 產生全新的地址
					String accept1 = all(orisp[k][2], orisp[k - 1][5], orisp[k - 1][6]);
					list.add(accept1);
				}
			}
		}
		read1.close();

		FileWriter fw = new FileWriter("/Users/josephinelai/Desktop/全縣市/" + city +"_"+ country + "_OutAddXY.rdf");// 改rdf文檔內容

		System.out.println("我是15");
		for (int m = 0; m < list.size(); m++) {
			fw.write(list.get(m) + "\n");
		}
		fw.write("</rdf:RDF>");
		fw.close();

		 FileWriter fmd5 = new
		 FileWriter("/Users/josephinelai/Desktop/md5output/" + city + country
		 + "Md5.txt", true);// 新增md5
		 fmd5.write(md5 + "\n");
		 fmd5.close();

	}

	public static String half(String s, String spre, String snext, String schatime, String stype, String nows,
			String pres, String chanow, String nowtype) { // 製出新的rdf

		stype = stype.replaceAll("\n", "");
		nowtype = nowtype.replace("\n", "");
		String line = "   <rdf:Description rdf:about=\"http://addr.sgis.tw/address/" + s + "\">\n"
				+ "    <addr:has_pre rdf:resource=\"http://addr.sgis.tw/address/" + spre + "\"/>\n"
				+ "    <addr:has_next rdf:resource=\"http://addr.sgis.tw/address/" + snext + "\"/>\n"
				+ "    <addr:change_time>" + schatime + "</addr:change_time>\n" + "    <addr:type>" + stype
				+ "</addr:type>\n</rdf:Description>\n\n"
				+ "   <rdf:Description rdf:about=\"http://addr.sgis.tw/address/" + nows + "\">\n"
				+ "    <addr:has_pre rdf:resource=\"http://addr.sgis.tw/address/" + pres + "\"/>\n"
				+ "    <addr:change_time>" + chanow + "</addr:change_time>\n" + "    <addr:type>" + nowtype
				+ "</addr:type>\n</rdf:Description>\n\n";
		return line;
	}

	public static String all(String s, String time, String type) {
		type = type.replaceAll("\n", "");
		String line = "   <rdf:Description rdf:about=\"http://addr.sgis.tw/address/" + s + "\">\n"
				+ "    <addr:change_time>" + time + "</addr:change_time>\n" + "    <addr:type>" + type
				+ "</addr:type>\n</rdf:Description>\n\n";
		return line;
	}

	public static String part(String s, String nexts, String pretime, String pretype, String time, String type) {
		type = type.replaceAll("\n", "");
		pretype = pretype.replaceAll("\n", "");
		String line = "   <rdf:Description rdf:about=\"http://addr.sgis.tw/address/" + s + "\">\n"
				+ "    <addr:has_next rdf:resource=\"http://addr.sgis.tw/address/" + nexts + "\"/>\n"
				+ "    <addr:change_time>" + pretime + "</addr:change_time>\n" + "    <addr:type>" + pretype
				+ "</addr:type>\n</rdf:Description>\n\n"
				+ "   <rdf:Description rdf:about=\"http://addr.sgis.tw/address/" + nexts + "\">\n"
				+ "    <addr:has_pre rdf:resource=\"http://addr.sgis.tw/address/" + s + "\"/>\n"
				+ "    <addr:change_time>" + time + "</addr:change_time>\n" + "    <addr:type>" + type
				+ "</addr:type>\n</rdf:Description>\n\n";
		return line;
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
