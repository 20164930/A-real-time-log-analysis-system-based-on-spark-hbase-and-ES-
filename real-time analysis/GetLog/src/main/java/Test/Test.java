package Test;

import Spark.KafkaTools;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Test {
    static Params params;
    static long session_id=0;
    private static final String TOPIC="test0";

	/*
	public static void main(String[] args){
		params=new Test.Params();
		FileWriter fw=null;
		BufferedWriter bw=null;
		FileWriter fw1=null;
		BufferedWriter bw1=null;
		FileReader fr=null;
		BufferedReader br=null;
		try {
			fw = new FileWriter(new File("C:\\Users\\dell\\Desktop\\log.log"), true);
			bw = new BufferedWriter(fw);
			fr=new FileReader(new File("C:\\Users\\dell\\Desktop\\log.index"));
			br=new BufferedReader(fr);
			String s=br.readLine();
			int index=Integer.parseInt(s);
			session_id=index;
			br.close();
			fr.close();

			int idx_now=index+3;
			fw1 = new FileWriter(new File("C:\\Users\\dell\\Desktop\\log.index"));
			bw1 = new BufferedWriter(fw1);
			bw1.write(String.valueOf(idx_now));
			bw1.flush();
			bw1.close();
			fw1.close();
		}catch (IOException e){
			e.printStackTrace();
		}
		for(int k=0;k<3;k++) {
			Random random=new Random();
			int num=(int) (Math.sqrt(25)*random.nextGaussian()+20);//ÿ��session���ж�������¼
			num=num>=1?num:1;
			String imei = getImei();
			String uid = getUid();
			String cookie = getCookie();
			String ip = getIp();
			String local_list = getLocal_list();
			String data = getDate();
			long session_id = getSession_id();
			for (int i = 0; i < num; i++) {
				String re = getDatas(imei, uid, cookie, ip, local_list, data, session_id);
				re="{"+re+"}";
				try {
					System.out.println(re);
					bw.write(re);
					bw.write("\r\n");
				}catch (IOException e){
					e.printStackTrace();
				}
			}
		}
		try {
			bw.close();
			fw.close();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
*/


    public static void main(String[] args){
        params=new Params();
        FileWriter fw=null;
        BufferedWriter bw=null;
        FileReader fr=null;
        BufferedReader br=null;
        try {
            fr = new FileReader(new File("C:\\Users\\dell\\Desktop\\log.index"));
            br = new BufferedReader(fr);
            String s = br.readLine();
            int index = Integer.parseInt(s);
            session_id = index;
            br.close();
            fr.close();

            int idx_now=index+1;
            fw = new FileWriter(new File("C:\\Users\\dell\\Desktop\\log.index"));
            bw = new BufferedWriter(fw);
            bw.write(String.valueOf(idx_now));
            bw.flush();
            bw.close();
            fw.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        Producer producer= KafkaTools.getProducer();

        long all=0;
        for(int k=0;k<10000;k++) {
            Random random=new Random();
            int num=(int) (Math.sqrt(25)*random.nextGaussian()+15);
            num=num>=1?num:1;
            String imei = getImei();
            String uid = getUid();
            String cookie = getCookie();
            String ip = getIp();
            String local_list = getLocal_list();
            String data = getDate();
            long session_id = getSession_id();
            for (int i = 0; i < num; i++) {
                String re = getDatas(imei, uid, cookie, ip, local_list, data, session_id,i+1);
                re="{"+re+"}";
                producer.send(new ProducerRecord(TOPIC,re));
                all++;
                System.out.println(all);
            }
        }
        producer.close();

    }

    public static String getDatas(String imei,String uid,String cookie,String ip,
                                  String local_list,String date,long session_id,int inner_id){
        String str_session=String.format("%8d", session_id).replace(" ", "0");
        String str_inner_id=String.format("%4d",inner_id).replace(" ","0");
        String re="\"imei\":\""+imei+"\",\"uid\":\""+uid+"\",\"cookie\":\""+cookie+"\",\"ip\":\""
                +ip+"\",\"local_list\":\""+local_list+"\",\"date\":\""
                +date+"\",\"timestamp\":"+getTimestamp()+",\"session_id\":\""
                +str_session+"\",\"inner_id\":\""+str_inner_id+"\",\"referrer\":\""
                +getReferrer()+"\",\"params\":{";
        Random random=new Random();
        int seed=random.nextInt(12)+1;
        String param=params.getParams(seed);
        re+=param+"}";
        return re;
    }
    public static String getImei(){
        double seed=Math.random();
        long num=(long) (seed*1000000000000000L);
        String re=String.format("%15d", num).replace(" ", "0");
        return re;
    }

    public static String getUid(){
        double seed=Math.random();
        long num=(long)(seed*1000);
        String re=String.format("%10d", num).replace(" ","0");
        return re;
    }

    public static String getCookie(){
        long len=(new Random()).nextInt(50)+50;
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<len;i++){
            char ch=(char)((new Random()).nextInt(52)+'A');
            if(ch>='A'&&ch<='Z' ||ch>='a'&&ch<='z'){
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public static String getIp(){
        Random random=new Random();
        int ip1=random.nextInt(1000);
        int ip2=random.nextInt(1000);
        int ip3=random.nextInt(1000);
        int ip4=random.nextInt(1000);
        String i1,i2,i3,i4;
        if(ip1==0){
            i1="0";
        }else{
            i1=String.valueOf(ip1);
        }

        if(ip2==0){
            i2="0";
        }else{
            i2=String.valueOf(ip2);
        }
        if(ip3==0){
            i3="0";
        }else{
            i3=String.valueOf(ip3);
        }
        if(ip4==0){
            i4="0";
        }else{
            i4=String.valueOf(ip4);
        }
        String ip=i1+"."+i2+"."+i3+"."+i4;
        return ip;
    }


    public static String getLocal_list(){
        int len=(new Random()).nextInt(4)+2;
        Random random=new Random();
        String re="";
        int i=1;
        while(i<=len){
            if(i==1){
                re+=String.valueOf(random.nextInt(34)+1)+"_";
            }else if(i==2){
                re+=String.valueOf(random.nextInt(166)+35)+"_";
            }else if(i==3){
                re+=String.valueOf(random.nextInt(600)+201)+"_";
            }else if(i==4){
                re+=String.valueOf(random.nextInt(700)+801)+"_";
            }else if(i==5){
                re+=String.valueOf(random.nextInt(1500)+1501)+"_";
            }
            i++;
        }
        return re.substring(0,re.length()-1);
    }

    public static String getDate(){
        String re="";
        Calendar calendar = Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int date=calendar.get(Calendar.DATE);
        re+=String.valueOf(year);
        re+=String.format("%2d", month).replace(" ", "0");
        re+=String.format("%2d", date).replace(" ", "0");
        return re;
    }
    public static String getDate(int n){
        String re="";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - n);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int date=calendar.get(Calendar.DATE);
        re+=String.valueOf(year);
        re+=String.format("%2d", month).replace(" ", "0");
        re+=String.format("%2d", date).replace(" ", "0");
        return re;
    }

    public static long getTimestamp(){
        return  System.currentTimeMillis();
    }

    public static long getSession_id(){
        session_id++;
        return session_id;
    }

    public static String getReferrer(){
        //main,search,suggestion,share,other
        Random random=new Random();
        int seed=random.nextInt(7);
        String re="";
        if(seed==0){
            re="main";
        }else if(seed==1){
            re="suggestion";
        }else if(seed==2){
            re="share";
        }else if(seed==3||seed==4){
            re="other";
        }else{
            re="search";
        }
        return re;
    }


}