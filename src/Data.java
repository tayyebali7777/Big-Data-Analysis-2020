import java.io.*;
import java.util.*;

import smile.math.MathEx;
import javax.sound.sampled.LineListener;

import java.sql.Timestamp;
public class Data {
	BufferedReader br;
	public String filename;
	ArrayList<String> dateArrayList;
	ArrayList<Double> longitudeArrayList;
	ArrayList<Double> latitudeArrayList;
	ArrayList<Timestamp> timestamps;
	
	int columnsNum;
	int rowsNum;
	int counts[];
	
	double longitudeMean;
	double latitudeMean;
	
	double longitudeStdev;
	double latitudeStdev;
	
	double timestampMean;
	double timestampStdev;
	
	double longitudeMax;
	double longitudeMin;
	
	double latitudeMax;
	double latitudeMin;
	
	double timestampMax;
	double timestampMin;

	List<Double> latitude_k_zero;
	List<Double> longitude_k_zero;
	List<Double> timestamp_k_zero;
	
	List<Double> longitude_min_max_normArrayList;
	List<Double> latitude_min_max_normArrayList;
	List<Double> timestamp_min_max_normArrayList;
	
	List<Double> characterLongitudeDoubles;
	List<Double> characterLatitudeDoubles;
	List<Double> characterTimestampDoubles;
	
	public int getRowsNum() {
		return this.rowsNum;
	}
	
	public int getColumnsNum() {
		return this.columnsNum;
	}
	
	public List<Double> getRawDataByIndex(int index) {
		if(index==0) return this.longitudeArrayList;
		else if(index==1) return this.latitudeArrayList;
		else if(index == 2) {
			ArrayList<Double> tmpArrayList=new ArrayList<Double>();
			for(Timestamp t:timestamps) {
				tmpArrayList.add((double)t.getTime());
			}
			return tmpArrayList;
		} else
		return null;
		
	}
	
	public List<Double> getKScoreDataByIndex(int index){
		if(index==0) return longitude_k_zero;
		else if(index==1) return latitude_k_zero;
		else if(index==2) return timestamp_k_zero;
		else return null;
	}
	
	public List<Double> getMinMaxDataByIndex(int index){
		if(index==0) return longitude_min_max_normArrayList;
		else if(index == 1) return latitude_min_max_normArrayList;
		else if(index == 2) return timestamp_min_max_normArrayList;
		else return null;
	}
	
	public void readFile() throws IOException{
		this.br=new BufferedReader(new FileReader(new File(filename)));
		this.br.readLine();
		String line=this.br.readLine();
		while(line!=null) {
			String [] arrsStrings=line.replace("\n", "").split(",");
			if(!line.contains(",,")) {
				if(dateArrayList.contains(arrsStrings[5].split(" ")[0])) {
					this.timestamps.add(Timestamp.valueOf(arrsStrings[5]));
					this.longitudeArrayList.add(Double.parseDouble(arrsStrings[10]));
					this.latitudeArrayList.add(Double.parseDouble(arrsStrings[11]));
				}
				if(dateArrayList.contains(arrsStrings[6].split(" ")[0])) {
					this.timestamps.add(Timestamp.valueOf(arrsStrings[6]));
					this.longitudeArrayList.add(Double.parseDouble(arrsStrings[12]));
					this.latitudeArrayList.add(Double.parseDouble(arrsStrings[13]));
				}
			}
			line=this.br.readLine();
		}
		this.br.close();
		this.columnsNum=3;
		this.rowsNum=this.longitudeArrayList.size();
	}
	public Data(String filename) {
		this.filename=filename;
		this.dateArrayList=new ArrayList<String>();
		this.dateArrayList.add(new String("2013-01-01"));
//		this.dateArrayList.add(new String("2013-01-02"));
//		this.dateArrayList.add(new String("2013-01-03"));
		this.longitudeArrayList=new ArrayList<Double>();
		this.latitudeArrayList=new ArrayList<Double>();
		this.timestamps=new ArrayList<Timestamp>();
	}
	public void printData() {
		if(this.longitudeArrayList.size()==this.latitudeArrayList.size() & this.longitudeArrayList.size()==this.timestamps.size()) {
			for(int i=0;i<this.longitudeArrayList.size();i++) {
				System.out.println("("+this.longitudeArrayList.get(i)+","+this.latitudeArrayList.get(i)+")->"+this.timestamps.get(i));
			}
		}else {
			System.out.println("Data Error");
		}
	}
	public void findLongitudeMean() {
		double sum=0;
		for(double d:this.longitudeArrayList) {
			sum+=d;
		}
		longitudeMean=sum/this.longitudeArrayList.size();
	}
	public void findLatotudeMean() {
		double sum=0;
		for(double d:this.latitudeArrayList) {
			sum+=d;
		}
		latitudeMean=sum/this.latitudeArrayList.size();
	}
	public void findTimestampMean() {
		double sum=0;
		for(Timestamp p:timestamps) {
			sum+=(double)p.getTime();
		}
		this.timestampMean=sum/timestamps.size();
	}
	public void findLongitudeMax() {
		double max=Double.MIN_VALUE;
		for(double d:longitudeArrayList) {
			if(max<d) max=d;
		}
		this.longitudeMax=max;
	}
	public void findLongitudeMin() {
		double min=Double.MAX_VALUE;
		for(double d:longitudeArrayList) {
			if(min>d) min=d;
		}
		this.longitudeMin=min;
	}
	public void findLatitudeMax() {
		double max=Double.MIN_VALUE;
		for(double d:latitudeArrayList) {
			if(max<d) max=d;
		}
		this.latitudeMax=max;
	}
	public void findLatitudeMin() {
		double min=Double.MAX_VALUE;
		for(double d:latitudeArrayList) {
			if(min>d)min=d;
		}
		this.latitudeMin=min;
	}
	public void findTimestampMax() {
		double max=Double.MIN_VALUE;
		for(Timestamp t:timestamps) {
			if(max<t.getTime()) max=t.getTime();
		}
		this.timestampMax=max;
	}
	public void findTimestampMin() {
		double min=Double.MAX_VALUE;
		for(Timestamp t:timestamps) {
			if(min>t.getTime()) min=t.getTime();
		}
		this.timestampMin=min;
	}
	
	public void findLongitudeStdev() {
		this.findLongitudeMean();
		double sum=0;
		for(double d:longitudeArrayList) {
			sum+=Math.pow(d-this.longitudeMean, 2);
		}
		this.longitudeStdev=Math.sqrt(sum);
	}
	
	public void findLatitudeStdev() {
		this.findLatotudeMean();
		double sum=0;
		for(double d:latitudeArrayList) {
			sum+=Math.pow(d-this.latitudeMean, 2);
		}
		this.latitudeStdev=Math.sqrt(sum);
	}
	
	public void findTimestampStdev() {
		this.findTimestampMean();
		double sum=0;
		for(Timestamp d:timestamps) {
			sum+=Math.pow(d.getTime()-this.timestampMean, 2);
		}
		this.timestampStdev=Math.sqrt(sum);
	}
	
	public void findMaxValue() {
		this.findLongitudeMax();
		this.findLatitudeMax();
		this.findTimestampMax();
	}
	public void findMinValue() {
		this.findLongitudeMin();
		this.findLatitudeMin();
		this.findTimestampMin();
	}
	
	public void findMeans() {
		this.findLongitudeMean();
		this.findLatotudeMean();
		this.findTimestampMean();
	}
	
	public void findStdev() {
		this.findLongitudeStdev();
		this.findLatitudeStdev();
		this.findTimestampStdev();
	}
	
	public void filterData(double sigma) {
		this.findMeans();
		this.findStdev();
		int index=0;
		double longmin,longmax,latmin,latmax;
		longmin=longitudeMean-sigma*longitudeStdev;
		longmax=longitudeMean+sigma*longitudeStdev;
		latmin=latitudeMean-sigma*latitudeStdev;
		latmax=latitudeMean+sigma*latitudeStdev;
		while(index<this.latitudeArrayList.size()) {
			if(longitudeArrayList.get(index)<longmin | longitudeArrayList.get(index)>longmax |
					latitudeArrayList.get(index)<latmin | latitudeArrayList.get(index) > latmax) {
				longitudeArrayList.remove(index);
				latitudeArrayList.remove(index);
				timestamps.remove(index);
			}else index++;
		}
	}
	public void saveData(String filename) throws IOException {
		BufferedWriter bWriter=new BufferedWriter(new FileWriter(new File(filename)));
		for(int i=0;i<latitudeArrayList.size();i++) {
			bWriter.write(longitudeArrayList.get(i)+",");
			bWriter.write(latitudeArrayList.get(i)+",");
			bWriter.write(timestamps.get(i)+"\n");
		}
		bWriter.close();
	}
	public void longitudeKZero() {
		longitude_k_zero=new LinkedList<Double>();
		for(double d:longitudeArrayList) {
			longitude_k_zero.add((d-longitudeMean)/longitudeStdev);
		}
	}
	public void latitudeKZero() {
		latitude_k_zero=new LinkedList<Double>();
		for(double d:latitudeArrayList) {
			latitude_k_zero.add((d-latitudeMean)/latitudeStdev);
		}
	}
	public void timestampKZero() {
		timestamp_k_zero=new LinkedList<Double>();
		for(Timestamp t:timestamps) {
			timestamp_k_zero.add((t.getTime()-timestampMean)/timestampStdev);
		}
	}
	
	public void longitudeMinMaxNorm() {
		longitude_min_max_normArrayList=new LinkedList<Double>();
		double cd=longitudeMax-longitudeMin;
		for(double d:longitudeArrayList) {
			longitude_min_max_normArrayList.add((d-longitudeMin)/cd);
		}
	}
	public void latitudeMinMaxNorm() {
		latitude_min_max_normArrayList=new LinkedList<Double>();
		double cd=latitudeMax-latitudeMin;
		for(double d:latitudeArrayList) {
			latitude_min_max_normArrayList.add((d-latitudeMin)/cd);
		}
	}
	public void timestampMinMaxNorm() {
		timestamp_min_max_normArrayList=new LinkedList<Double>();
		double cd=timestampMax-timestampMin;
		for(Timestamp t:timestamps) {
			timestamp_min_max_normArrayList.add((t.getTime()-timestampMin)/cd);
		}
	}
	public void k_zero() {
		longitudeKZero();
		latitudeKZero();
		timestampKZero();
	}
	public void minMaxNorm() {
		longitudeMinMaxNorm();
		latitudeMinMaxNorm();
		timestampMinMaxNorm();
	}
	public void prepareData(double sigma) throws IOException {
		readFile();
        filterData(sigma);
        findMaxValue();
        findMeans();
        findStdev();
        findMinValue();
        minMaxNorm();
        k_zero();
	}
	public void refreshRawData(double sigma) {
		double longmean,longstdev,latmean,latstdev,timean,tistdev;
		//longitude
		Double[] d=longitudeArrayList.toArray(new Double[1]);
		double [] da=new double[d.length];
		for(int i=0;i<d.length;i++) {
			da[i]=d[i];
		}
		longmean=MathEx.mean(da);
		longstdev=MathEx.sqr(MathEx.var(da));
		System.out.println("In refresh data step1.1:"+sigma);
		//latitude
		d=latitudeArrayList.toArray(new Double[1]);
		da=new double[d.length];
		for(int i=0;i<d.length;i++) {
			da[i]=d[i];
		}
		latmean=MathEx.mean(da);
		latstdev=MathEx.sqr(MathEx.var(da));
		System.out.println("In refresh data step1.2:"+sigma);
		//timestamp
		da=new double[timestamps.size()];
		for(int i=0;i<timestamps.size();i++) {
			da[i]=timestamps.get(i).getTime();
		}
		timean=MathEx.mean(da);
		tistdev=MathEx.sqr(MathEx.var(da));
		System.out.println("In refresh data step1.3:"+sigma);
		int index=0;
		while(index<longitudeArrayList.size()) {
			if(longitudeArrayList.get(index)<longmean-sigma*longstdev | longitudeArrayList.get(index)>longmean + sigma*longstdev
					| latitudeArrayList.get(index)<latmean-sigma*latstdev|latitudeArrayList.get(index)>latmean+sigma*latstdev|
					timestamps.get(index).getTime()<timean-sigma*tistdev|timestamps.get(index).getTime()>timean+sigma*tistdev) {
				timestamps.remove(index);
				longitudeArrayList.remove(index);
				latitudeArrayList.remove(index);
			}else index++;
		}
	}
	public void refreshKZeroData(double sigma) {
		System.out.println("In refresh data step2.1:"+sigma);
		List<Double> tmp1,tmp2,tmp3;
		tmp1=longitude_k_zero;
		tmp2=latitude_k_zero;
		tmp3=timestamp_k_zero;
		double longmean,longstdev,latmean,latstdev,timean,tistdev;
		//longitude
		Double[] d=tmp1.toArray(new Double[1]);
		double [] da=new double[d.length];
		for(int i=0;i<d.length;i++) {
			da[i]=d[i];
		}
		longmean=MathEx.mean(da);
		longstdev=MathEx.sqr(MathEx.var(da));
		System.out.println("In refresh data step2.2:"+sigma);
		//latitude
		d=tmp2.toArray(new Double[1]);
		da=new double[d.length];
		for(int i=0;i<d.length;i++) {
			da[i]=d[i];
		}
		latmean=MathEx.mean(da);
		latstdev=MathEx.sqr(MathEx.var(da));
		System.out.println("In refresh data step2.3:"+sigma);
		//timestamp
		d=tmp3.toArray(new Double[1]);
		da=new double[d.length];
		for(int i=0;i<d.length;i++) {
			da[i]=d[i];
		}
		timean=MathEx.mean(da);
		tistdev=MathEx.sqr(MathEx.var(da));
		System.out.println("In refresh data step2.4:"+sigma);
		int index=0;
		while(index<tmp1.size()) {
			if(index % 10000==0)
				System.out.println(index+":"+tmp1.size());
			if(tmp1.get(index)<longmean-sigma*longstdev | tmp1.get(index)>longmean + sigma*longstdev
					| tmp2.get(index)<latmean-sigma*latstdev|tmp2.get(index)>latmean+sigma*latstdev|
					tmp3.get(index)<timean-sigma*tistdev|tmp3.get(index)>timean+sigma*tistdev) {
				tmp1.remove(index);
				tmp2.remove(index);
				tmp3.remove(index);
			}else index++;
		}
		System.out.println("In refresh data step2.5:"+sigma);
	}
	public void refreshMinMaxNormData(double sigma) {
		List<Double> tmp1,tmp2,tmp3;
		tmp1=longitude_min_max_normArrayList;
		tmp2=latitude_min_max_normArrayList;
		tmp3=timestamp_min_max_normArrayList;
		double longmean,longstdev,latmean,latstdev,timean,tistdev;
		//longitude
		
		System.out.println("In refresh data step3.1:"+sigma);
		
		Double[] d=tmp1.toArray(new Double[1]);
		double [] da=new double[d.length];
		for(int i=0;i<d.length;i++) {
			da[i]=d[i];
		}
		longmean=MathEx.mean(da);
		longstdev=MathEx.sqr(MathEx.var(da));
		
		System.out.println("In refresh data step3.2:"+sigma);
		
		//latitude
		d=tmp2.toArray(new Double[1]);
		da=new double[d.length];
		for(int i=0;i<d.length;i++) {
			da[i]=d[i];
		}
		latmean=MathEx.mean(da);
		latstdev=MathEx.sqr(MathEx.var(da));
		
		System.out.println("In refresh data step3.3:"+sigma);
		
		//timestamp
		d=tmp3.toArray(new Double[1]);
		da=new double[d.length];
		for(int i=0;i<d.length;i++) {
			da[i]=d[i];
		}
		timean=MathEx.mean(da);
		tistdev=MathEx.sqr(MathEx.var(da));
		
		System.out.println("In refresh data step3.4:"+sigma);
		
		for(int i=0;i<tmp1.size();i++) {
			if(tmp1.get(i)<longmean-sigma*longstdev | tmp1.get(i)>longmean + sigma*longstdev
					| tmp2.get(i)<latmean-sigma*latstdev|tmp2.get(i)>latmean+sigma*latstdev|
					tmp3.get(i)<timean-sigma*tistdev|tmp3.get(i)>timean+sigma*tistdev) {
				tmp1.remove(i);
				tmp2.remove(i);
				tmp3.remove(i);
			}
		}
	}
	public void refreshData(double sigma) {
		System.out.println("In refresh data step1:"+sigma);
        refreshRawData(sigma);
        System.out.println("In refresh data step2:"+sigma);
        refreshKZeroData(sigma);
        System.out.println("In refresh data step3:"+sigma);
        refreshMinMaxNormData(sigma);
        System.out.println("In refresh data step4:"+sigma);
	}
	public void characterPoints(double rad,int thr) {
		characterLongitudeDoubles=new ArrayList<Double>();
		characterLatitudeDoubles=new ArrayList<Double>();
		characterTimestampDoubles=new ArrayList<Double>();
		
		
	}
	public void countNumber(double rad) {
		counts=new int[longitude_k_zero.size()];
		for(int i=0;i<counts.length;i++) {
			counts[i]=0;
		}
		for(int i=0;i<longitude_k_zero.size();i++) {
			for(int j=0;j<longitude_k_zero.size();j++) {
				if(distance(longitude_k_zero.get(i), longitude_k_zero.get(j), latitude_k_zero.get(i), latitude_k_zero.get(j)
						, timestamp_k_zero.get(i), timestamp_k_zero.get(j))<rad) {
					counts[i]++;
				}
			}
		}
	}
	public double distance(double x1,double y1,double z1,double x2,double y2,double z2) {
		return Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2)+Math.pow(z1-z2, 2));
	}
	public void writeKZeroToFile(String filename) throws IOException {
		BufferedWriter bWriter=new BufferedWriter(new FileWriter(new File(filename)));
		for(int i=0;i<longitude_k_zero.size();i++) {
			bWriter.write(longitude_k_zero.get(i)+","+latitude_k_zero.get(i)+","+timestamp_k_zero.get(i)+"\n");
		}
		bWriter.close();
	}
	public static void main(String [] args)throws IOException {
		Data data=new Data("data/trip_data_1.csv");
		data.readFile();
		data.prepareData(3);
		data.refreshData(3*1e8);
		//data.writeKZeroToFile("data/KZero.csv");
//		System.out.println(data.longitudeArrayList.size());
//		data.saveData("data/data1.csv");
//		data.filterData();
//		data.saveData("data/data2.csv");
//		System.out.println(data.longitudeArrayList.size());
		
//		ArrayList<Integer> integers=new ArrayList<Integer>();
//		for(int i=0;i<10;i++) {
//			integers.add(i);
//		}
//		for(int i=0;i<integers.size();i++) {
//			integers.remove(i);
//			i--;
//		}
//		System.out.println(integers);
//		System.out.println(integers.size());
		/*
		 * Test two ways errors
		 * 
		double mean=0;
		double sum=0;
		for(double d:data.longitudeArrayList) {
			sum+=d;
			mean+=d/data.longitudeArrayList.size();
		}
		System.out.println(sum/data.longitudeArrayList.size()+","+mean);
		mean=0;
		sum=0;
		for(double d:data.latitudeArrayList) {
			sum+=d;
			mean+=d/data.longitudeArrayList.size();
		}
		System.out.println(sum/data.longitudeArrayList.size()+","+mean);
		*/
	}
}
