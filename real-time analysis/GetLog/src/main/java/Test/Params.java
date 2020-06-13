package Test;

import com.csvreader.CsvReader;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;
public class Params {
	static String[][] songs=new String[1000][2];
	static ArrayList<String> chars=new ArrayList<String>();
	public Params(){
		setSongs();
		getRandomString();

	}

	public  String getParams(int n){
		if(n==1){
			return getParams1();
		}else if(n==2){
			return getParams2();
		}else if(n==3){
			return getParams3();
		}else if(n==4){
			return getParams4();
		}else if(n==5){
			return getParams5();
		}else if(n==6){
			return getParams6();
		}else if(n==7){
			return getParams7();
		}else if(n==8){
			return getParams8();
		}else if(n==9){
			return getParams9();
		}else if(n==10){
			return getParams10();
		}else if(n==11){
			return getParams11();
		}else{
			return getParams12();
		}
	}
	public  String getParams1(){
		return "\"action_num\":1,\"action_type\":\"main\"";
	}
	
	public  String getParams2(){
		String re="\"action_num\":2,\"action_type\":\"search\",\"keyword\":\"";
        String keyword=getRandomString(1,5);
        Random random=new Random();
        int result_num=(int) (Math.sqrt(49)*random.nextGaussian()+10);
        result_num=result_num>=0?result_num:0;
        re+=keyword+"\",\"result_num\":"+result_num+",\"result_content\":[";
        String content="";
        for(int i=0;i<result_num;i++){
            content+=getSong()+",";
        }
        if(result_num!=0){
            content=content.substring(0,content.length()-1);
        }
        re+=content+"]";
		return re;
	}

	public  String getParams3(){
	    return "\"action_num\":3,\"action_type\":\"back\"";
    }
    public  String getParams4(){
	    String re="\"action_num\":4,\"action_type\":\"suggestion\",\"match\":[";
        Random random=new Random();
        int result_num=(int) (Math.sqrt(9)*random.nextGaussian()+6);
        result_num=result_num>=0?result_num:0;
        String content="";
        for(int i=0;i<result_num;i++){
            content+=getSong()+",";
        }
        if(result_num!=0){
            content=content.substring(0,content.length()-1);
        }
        re+=content+"],\"rematch\":[";

        if(result_num==0){
            int reMatch_num=(int) (Math.sqrt(4)*random.nextGaussian()+5);
            reMatch_num=reMatch_num>=0?reMatch_num:0;
            String reContent="";
            for(int i=0;i<reMatch_num;i++){
                reContent+=getSong()+",";
            }
            if(reMatch_num!=0){
                reContent=reContent.substring(0,reContent.length()-1);
            }
            re+=reContent;
        }
        re+="]";
        return re;
    }

    public  String getParams5(){
		Random random=new Random();
		int comment_id=random.nextInt(10000)+1;
		String content=getRandomString(1,20);
		String re="\"action_num\":5,\"action_type\":\"comment\",\"comment_id\"" +
				":"+comment_id+",\"content\":\""+content+"\"";
		return re;
	}

	public  String getParams6(){
		Random random=new Random();
		int comment_id=random.nextInt(10000)+1;
		String content=getRandomString(1,20);
		String re="\"action_num\":6,\"action_type\":\"comment_rm\",\"comment_id\":"+comment_id+
				",\"content\":\""+content+"\"";
		return re;
	}
	public  String getParams7(){
		Random random=new Random();
		int comment_id=random.nextInt(10000)+1;
		String content=getRandomString(1,20);
		int likes= random.nextInt(1000);
		String re="\"action_num\":7,\"action_type\":\"comment_like\",\"comment_id\":"+comment_id+
		",\"content\":\""+content+"\",\"likes\":"+likes;
		return re;
	}
	public  String getParams8(){
		String url="https://abc/music/";
		Random random=new Random();
		int seed=random.nextInt(7);
		if(seed==0){
			url+="main";
		}else if(seed==1){
			url+="search";
		}else if(seed==2){
			url+="list";
		}else if(seed==3){
			url+="play";
		}else if(seed==4){
			url+="suggestion";
		}else if(seed==5){
			url+="favorite";
		}else{
			url+="share?id="+random.nextInt(999)+1;
		}
		String re="\"action_num\":8,\"action_type\":\"click\",\"protocol\":\"https\",\"url\":\""+url+"\"";
		return re;
	}

	public  String getParams9(){
		String song=getSong();
		song=song.substring(1,song.length()-1);
		String re="\"action_num\":9,\"action_type\":\"favorite\","+song;
		return re;
	}

	public  String getParams10(){
		String song=getSong();
		song=song.substring(1,song.length()-1);
		String re="\"action_num\":10,\"action_type\":\"favorite_rm\","+song;
		return re;
	}

	public  String getParams11(){
		String re="\"action_num\":11,\"action_type\":\"play\",\"content\":";
		re+=getSong();
		return re;
	}

	public  String getParams12(){
		String song=getSong();
		String id=song.split(",")[0].split(":")[1];
		String url="https://abc/music/share?id="+id;
		String re="\"action_num\":12,\"action_type\":\"share\",\"url\":\""+url+"\",\"content\":"+song;
		return re;
	}
	public  String getRandomString(int least,int most){
        int numOfKeyword=0;
        StringBuilder re=new StringBuilder();
        Random random=new Random();
        numOfKeyword=random.nextInt(most)+least;
        for(int i=0;i<numOfKeyword;i++){
            re.append(chars.get(random.nextInt(chars.size())));
        }
        return re.toString();
    }

	private  void getRandomString() {
		File file=null;
		FileReader reader=null;
		BufferedReader br=null;
		try {
			file = new File("C:\\Users\\dell\\Desktop\\char.txt");
			reader = new FileReader(file);
			br = new BufferedReader(reader);
			String s = "";
			while ((s = br.readLine()) != null) {
				chars.add(s);
			}
			br.close();
			reader.close();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	public  void setSongs(){
		CsvReader reader = null;
		try {
			//如果生产文件乱码，windows下用gbk，linux用UTF-8
			reader = new CsvReader("C:\\Users\\dell\\Desktop\\1.csv", ',', Charset.forName("GBK"));
			// 读取标题
			reader.readHeaders();
			int i=0;
			int index=0;
			// 逐条读取记录，直至读完
			while (reader.readRecord()) {
				//读取第一例
				if(i>=80000&&i<81000){
					songs[index][0]=reader.get(1);
					songs[index][1]=reader.get(2);
					index++;
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				reader.close();
			}
		}
	}
	public String getSong(){
		Random r=new Random();
		int id=r.nextInt(999)+1;
		int dur=(int) (Math.sqrt(625)*r.nextGaussian()+240);
		dur=dur>=1?dur:1;
		int min=dur/60;
		int second=dur%60;
		String min1=String.format("%2d",min).replace(" ","0");
		String second1=String.format("%2d",second).replace(" ","0");

		String duration=min1+"m"+second1+"s";
		String song=songs[id][0];
		String singer=songs[id][1];
		String album=getRandomString(1,3);
		String re="{\"id\":"+id+",\"title\":\""+song+"\",\"singer\":\""+singer+"\",\"album\"" +
				":\""+album+"\",\"duration\":\""+duration+"\"}";
		return re;
	}
}
