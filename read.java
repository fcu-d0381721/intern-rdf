package test;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.FileWriter;

public class read {

	public static void main(String[] args) throws IOException {

		ArrayList<String> CityNameList = new ArrayList<String>();
		int cnt = 0;//判斷有幾筆一樣的address key
		CityNameList.addAll(group());
		String[][] all = new String[CityNameList.size()][7];
		FileWriter fw = new FileWriter("/Users/josephinelai/Desktop/testoutput.txt");
		boolean flag=true; //判斷是否為重複地址中的第一個
		
		/*寫擋前面*/
		fw.write("<?xml version=\"1.0\"?>\n");
		fw.write(" <rdf:RDF\n");
		fw.write("  xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n");
		fw.write("  xmlns:feature=\"http://www.linkeddata.com/address-features#\">\n\n");
		System.out.println(CityNameList.size());
		
		/*把記事本內容存入陣列*/
		for (int i = 0; i < CityNameList.size(); i++) {
			String[] token = CityNameList.get(i).split(",");
			for (int j = 0; j < token.length; j++) {
				all[i][j] = token[j];
			}
		}
		
		/*跑迴圈，做rdf*/
		for (int i = 0; i < CityNameList.size(); i++) {
			cnt = 0;
			for (int j = i + 1; j < CityNameList.size(); j++) {
				if (all[i][0].equals(all[j][0])) { //跟下一個address key一樣
					
					if (all[i][2].equals("null")&cnt==0) { //目前掃到有兩個地址但第一個是null
						
						if(all[j][0].equals(all[j+1][0])){ //有兩個以上且第一個是null(做第一個而已)
							fw.write("   <rdf:Description rdf:about=\"http://www.linkeddata.com/address#"+all[j][2]+"\">\n");
							fw.write("    <feature:has_next rdf:resource=\"http://www.linkeddata.com/address#"+all[j+1][2]+"\"/>\n");
							fw.write("    <feature:change_time>"+all[i][5]+"</feature:change_time>\n");
							all[i][6] = all[i][6].replaceAll("\n", "");
							fw.write("    <feature:type>"+all[i][6]+"</feature:type>\n");
							fw.write("   </rdf:Description>\n\n");
						}else{ //只有兩個且第一個是null
							fw.write("   <rdf:Description rdf:about=\"http://www.linkeddata.com/address#"+all[j][2]+"\">\n");
							fw.write("    <feature:change_time>"+all[i][5]+"</feature:change_time>\n");
							all[i][6] = all[i][6].replaceAll("\n", "");
							fw.write("    <feature:type>"+all[i][6]+"</feature:type>\n");
							fw.write("   </rdf:Description>\n\n");
						}
						
					}else if(all[i][2].equals("null")&cnt>=1){ //有兩個地址以上且第一個值是null
						if(all[j][0].equals(all[j+1][0])){ //重複地址中的任一個(除最後一個外)
							fw.write("   <rdf:Description rdf:about=\"http://www.linkeddata.com/address#"+all[j][2]+"\">\n");
							fw.write("    <feature:has_next rdf:resource=\"http://www.linkeddata.com/address#"+all[j+1][2]+"\"/>\n");
							fw.write("    <feature:has_pre rdf:resource=\"http://www.linkeddata.com/address#"+all[j-1][2]+"\"/>\n");
							fw.write("    <feature:change_time>"+all[j-1][5]+"</feature:change_time>\n");
							all[j-1][6] = all[j-1][6].replaceAll("\n", "");
							fw.write("    <feature:type>"+all[j-1][6]+"</feature:type>\n");
							fw.write("   </rdf:Description>\n\n");
						}else{ //重複地址中的最後一個
							fw.write("   <rdf:Description rdf:about=\"http://www.linkeddata.com/address#"+all[j][2]+"\">\n");
							fw.write("    <feature:has_pre rdf:resource=\"http://www.linkeddata.com/address#"+all[j-1][2]+"\"/>\n");
							fw.write("    <feature:change_time>"+all[j-1][5]+"</feature:change_time>\n");
							all[j-1][6] = all[j-1][6].replaceAll("\n", "");
							fw.write("    <feature:type>"+all[j-1][6]+"</feature:type>\n");
							fw.write("   </rdf:Description>\n\n");
						}
					}else if(!all[i][2].equals("null")){ //有兩個或以上但兩個都沒有null
						if(!all[i][0].equals(all[i-1][0])&flag==true){ //判斷是否為重複地址中的第一個
							fw.write("   <rdf:Description rdf:about=\"http://www.linkeddata.com/address#"+all[i][2]+"\">\n");
							fw.write("    <feature:has_next rdf:resource=\"http://www.linkeddata.com/address#"+all[i+1][2]+"\"/>\n");
							fw.write("    <feature:change_time>"+all[i][5]+"</feature:change_time>\n");
							all[i][6] = all[i][6].replaceAll("\n", "");
							fw.write("    <feature:type>"+all[i][6]+"</feature:type>\n");
							fw.write("   </rdf:Description>\n\n");
							flag=false; //防止判斷每一個都是第一個
							j--; //讓j停留在同一點
							cnt--; //因為讓j停留在同一點，所以不讓他也加1
						}else if(all[j][0].equals(all[j-1][0])&all[j][0].equals(all[j+1][0])){ //判斷是否為重複地址中的隨機一個(除最後一個外)
							fw.write("   <rdf:Description rdf:about=\"http://www.linkeddata.com/address#"+all[j][2]+"\">\n");
							fw.write("    <feature:has_next rdf:resource=\"http://www.linkeddata.com/address#"+all[j+1][2]+"\"/>\n");
							fw.write("    <feature:has_pre rdf:resource=\"http://www.linkeddata.com/address#"+all[j-1][2]+"\"/>\n");
							fw.write("    <feature:change_time>"+all[j][5]+"</feature:change_time>\n");
							all[j][6] = all[j][6].replaceAll("\n", "");
							fw.write("    <feature:type>"+all[j][6]+"</feature:type>\n");
							fw.write("   </rdf:Description>\n\n");
						}else{ //判斷是否為重複地址中的最後一個
							fw.write("   <rdf:Description rdf:about=\"http://www.linkeddata.com/address#"+all[j][2]+"\">\n");
							fw.write("    <feature:has_pre rdf:resource=\"http://www.linkeddata.com/address#"+all[j-1][2]+"\"/>\n");
							fw.write("    <feature:change_time>"+all[j][5]+"</feature:change_time>\n");
							all[j][6] = all[j][6].replaceAll("\n", "");
							fw.write("    <feature:type>"+all[j][6]+"</feature:type>\n");
							fw.write("   </rdf:Description>\n\n");
						}
					}
					cnt++;
				} else if (!all[i][0].equals(all[j][0]) & cnt == 0) { //只有一個地址
					fw.write("   <rdf:Description rdf:about=\"http://www.linkeddata.com/address#"+all[i][2]+"\">\n");
					fw.write("    <feature:change_time>"+all[i][5]+"</feature:change_time>\n");
					all[i][6] = all[i][6].replaceAll("\n", "");
					fw.write("    <feature:type>"+all[i][6]+"</feature:type>\n");
					fw.write("   </rdf:Description>\n\n");
					break;
				} else if (!all[i][0].equals(all[j][0]) & cnt > 0) { //取出同樣的地址為止
					//System.out.println("pass");
					fw.write("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
					break;
				}
			}
			flag=true;
			i = i + cnt;
		}
		fw.write("</rdf:RDF>");
		fw.close();
	}

	public static ArrayList<String> group() throws IOException {
		String[] allcity = { "testdata" };
		ArrayList<String> city = new ArrayList<String>();
		String line;
		for (int i = 0; i < allcity.length; i++) {
			InputStreamReader isr = new InputStreamReader(
					new FileInputStream("/Users/josephinelai/Desktop/" + allcity[i] + ".txt"), "UTF-8");
			BufferedReader read = new BufferedReader(isr);
			read.readLine();//讀第一行address

			while ((line = read.readLine()) != null) {
				city.add(line + "\n");
			}
			isr.close();
		}
		return city;
	}
}
