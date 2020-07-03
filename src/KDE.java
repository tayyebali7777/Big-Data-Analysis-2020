import java.awt.KeyEventDispatcher;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeMap;

import org.jfree.util.ArrayUtilities;
import java.sql.Timestamp;
import bits.kde.*;
import smile.math.MathEx;
import smile.stat.Hypothesis.t;

/*
 * public KernelDensity(double[] x) {
        this.x = x;
        this.mean = Math.mean(x);
        this.var = Math.var(x);
        this.sd = Math.sqrt(var);

        Arrays.sort(x);

        int n = x.length;
        double iqr = x[n*3/4] - x[n/4];
        h = 1.06 * Math.min(sd, iqr/1.34) / Math.pow(x.length, 0.2);
        gaussian = new GaussianDistribution(0, h);
    }
 * */

public class KDE {
	ArrayList<Double> longitude_k_zero;
	ArrayList<Double> latitude_k_zero;
	ArrayList<Double> timestamp_k_zero;
	
	ArrayList<Double> characterLongitudeArrayList;
	ArrayList<Double> characterLatitudeArrayList;
	ArrayList<Double> characterTimestampArrayList;
	
	double h;
	public KDE() {
		
	}
	//need to decide which norm method we use
	public double[] KernelDensityFunctionDecent(ArrayList<Double> data) {
		int n = timestamp_k_zero.size();
		Double [] longi=longitude_k_zero.toArray(new Double[1]);
		Double [] latiDoubles=latitude_k_zero.toArray(new Double[1]);
		Double [] timesDoubles=timestamp_k_zero.toArray(new Double[1]);
		Arrays.parallelSort(longi);
		Arrays.parallelSort(latiDoubles);
		Arrays.parallelSort(timesDoubles);
        double iqr = (longi[n*3/4] - longi[n/4]+latiDoubles[n*3/4] - latiDoubles[n/4]+timesDoubles[n*3/4] - timesDoubles[n/4])/3;
        double sd=(MathEx.sqr(MathEx.var(todoubleArray(longi)))+MathEx.sqr(MathEx.var(todoubleArray(latiDoubles)))+
        		MathEx.sqr(MathEx.var(todoubleArray(timesDoubles))))/2;
        
        //h = 1.06 * Math.min(sd, iqr/1.34) / Math.pow(longi.length, 0.2);
        h=1;
        double [] de=new double[3];
		if(data.size()==3) {
			de[0]=0;
			de[1]=0;
			de[2]=0;
			for(int i=0;i<n;i++) {
				double sum=0;
				ArrayList<Double> tmArrayList=null;
				for(int j=0;j<data.size();j++) {	
					switch (j) {
					case 0:
						tmArrayList=longitude_k_zero;
						break;
					case 1:
						tmArrayList=latitude_k_zero;
						break;
					case 2:
						tmArrayList=timestamp_k_zero;
						break;
					default:
						break;
					}
					sum+=Math.pow(tmArrayList.get(i)-data.get(j), 2);
					//System.out.println("sum:"+sum);
				}
				double pa=-sum/(2*h*h);
				//System.out.println("pa:"+pa);
				for(int j=0;j<data.size();j++) {
					switch (j) {
					case 0:
						tmArrayList=longitude_k_zero;
						break;
					case 1:
						tmArrayList=latitude_k_zero;
						break;
					case 2:
						tmArrayList=timestamp_k_zero;
						break;
					default:
						break;
					}
					de[j]+=Math.exp(pa)*(-(data.get(j)-tmArrayList.get(i))/(h*h));
					//System.out.println("Math.exp(pa):"+Math.exp(pa));
				}
			}
		}
		return de;
	}
	
	public int [] characterPointsCount(double rad) {
		int [] res=new int[longitude_k_zero.size()];
		for(int i=0;i<res.length;i++) {
			res[i]=0;
		}
		for(int i=0;i<latitude_k_zero.size();i++) {
			if(i%1000==0)
				System.out.println("i is:"+i+" size:"+latitude_k_zero.size());
			for(int j=0;j<latitude_k_zero.size();j++) {
				if(distance(longitude_k_zero.get(i), longitude_k_zero.get(j), latitude_k_zero.get(i), latitude_k_zero.get(j),
						timestamp_k_zero.get(i), timestamp_k_zero.get(j))<rad) {
					res[i]++;
				}
			}
		}
		return res;
	}
	public double distance(double x1,double x2,double y1,double y2,double z1,double z2) {
		return Math.sqrt(Math.pow(x1-x2, 2)+Math.pow(y1-y2, 2)+Math.pow(z1-z2, 2));
	}
	public void getCharacterPonits(int th)throws IOException {
		BufferedReader bReader=new BufferedReader(new FileReader(new File("data/characterCounts.csv")));
		String line=bReader.readLine();
		BufferedWriter bWriter=new BufferedWriter(new FileWriter(new File("data/CharacterPoints.csv")));
		while(line != null) {
			String [] srStrings=line.replace("\n", "").split(",");
			if(Integer.parseInt(srStrings[srStrings.length-1])>th) {
				bWriter.write(srStrings[0]+","+srStrings[1]+","+srStrings[2]+"\n");
			}
			line=bReader.readLine();
		}
		bWriter.close();
	}
	public double KernelDensityFunction(ArrayList<Double> data) {
		double sum0=0;
		for(int i=0;i<longitude_k_zero.size();i++) {
			double sum=0;
			ArrayList<Double> tmArrayList=null;
			for(int j=0;j<data.size();j++) {
				switch (j) {
				case 0:
					tmArrayList=longitude_k_zero;
					break;
				case 1:
					tmArrayList=latitude_k_zero;
					break;
				case 2:
					tmArrayList=timestamp_k_zero;
					break;
				default:
					break;
				}
				sum+=Math.pow(tmArrayList.get(i)-data.get(j), 2);
			}
			sum0+=sum;
		}
		return sum0;
	}
	double [] todoubleArray(Double [] da) {
		double [] d=new double[da.length];
		for(int i=0;i<da.length;i++) {
			d[i]=da[i];
		}
		return d;
	}
	public void gredientDecent(String fileString,double learningrate)throws IOException {
		ArrayList<Double> w=new ArrayList<Double>();
		double [] lo=todoubleArray(longitude_k_zero.toArray(new Double[1]));
		double [] la=todoubleArray(latitude_k_zero.toArray(new Double[1]));
		double [] ti=todoubleArray(timestamp_k_zero.toArray(new Double[1]));
		w.add(MathEx.random()%(MathEx.max(lo)-MathEx.min(lo))+MathEx.min(lo));
		w.add(MathEx.random()%(MathEx.max(la)-MathEx.min(la))+MathEx.min(la));
		w.add(MathEx.random()%(MathEx.max(ti)-MathEx.min(ti))+MathEx.min(ti));
		//double learningrate=-0.00001;
		BufferedWriter bWriter=new BufferedWriter(new FileWriter(new File(fileString)));
		for(int i =0;i<100;i++) {
			double []de=KernelDensityFunctionDecent(w);
//			for(int j=0;j<3;j++) {
//				System.out.print(de[j]+" ");
//			}
//			System.out.println("");
			w.set(0, w.get(0)+learningrate*de[0]);
			w.set(1, w.get(1)+learningrate*de[1]);
			w.set(2, w.get(2)+learningrate*de[2]);
			if(i%10==0) {
				System.out.println(i+":"+10000+" "+KernelDensityFunction(w));
			}
			bWriter.write(KernelDensityFunction(w)+","+w.get(0)+","+w.get(1)+","+w.get(2)+"\n");
		}
		bWriter.close();
	}
	public void readFile(String fileString) throws IOException {
		BufferedReader bWriter=new BufferedReader(new FileReader(new File(fileString)));
		String lineString=bWriter.readLine();
		longitude_k_zero=new ArrayList<Double>();
		latitude_k_zero=new ArrayList<Double>();
		timestamp_k_zero=new ArrayList<Double>();
		while(lineString!=null) {
			lineString=lineString.replace("\n", "");
			String [] ar=lineString.split(",");
			longitude_k_zero.add(Double.parseDouble(ar[0]));
			latitude_k_zero.add(Double.parseDouble(ar[1]));
			timestamp_k_zero.add(Double.parseDouble(ar[2]));
			lineString=bWriter.readLine();
		}
	}
	public void printOutCandidates(String [] filename)throws IOException{
		Data data=new Data("data/trip_data_1.csv");
		data.readFile();
		data.prepareData(3);
		TreeMap<Double, ArrayList<Double>> dataMap=new TreeMap<Double, ArrayList<Double>>();
		TreeMap<Double, ArrayList<Double>> dataMap1=new TreeMap<Double, ArrayList<Double>>();
		BufferedReader bReader=new BufferedReader(new FileReader(new File("data/CharacterPoints.csv")));
		String lineString=bReader.readLine();
		while(lineString!=null) {
			String [] srStrings=lineString.replace("\n", "").split(",");
			ArrayList<Double> tmpDoubles=new ArrayList<Double>();
			tmpDoubles.add(Double.parseDouble(srStrings[0]));
			tmpDoubles.add(Double.parseDouble(srStrings[1]));
			tmpDoubles.add(Double.parseDouble(srStrings[2]));
			dataMap.put(KernelDensityFunction(tmpDoubles), tmpDoubles);
			lineString=bReader.readLine();
		}
		BufferedWriter bwBufferedWriter=new BufferedWriter(new FileWriter(new File("data/Result.txt")));
		bwBufferedWriter.write("Top 10 candidates\n");
		for(int i=0;i<10;i++) {
			System.out.println(dataMap.firstKey()+" "+dataMap.lastKey());
			bwBufferedWriter.write(dataMap.lastKey()+",[");
			bwBufferedWriter.write(""+(dataMap.get(dataMap.lastKey()).get(0)*data.longitudeStdev+data.longitudeMean)+",");
			bwBufferedWriter.write(""+(dataMap.get(dataMap.lastKey()).get(1)*data.latitudeStdev+data.latitudeMean)+",");
			Timestamp timestamp=new Timestamp((long)(dataMap.get(dataMap.lastKey()).get(2)*data.timestampStdev+data.timestampMean));
			bwBufferedWriter.write(""+timestamp+"]\n");
			dataMap1.put(dataMap.lastKey(), dataMap.get(dataMap.lastKey()));
			dataMap.remove(dataMap.lastKey());
		}
		bwBufferedWriter.flush();
		bwBufferedWriter.write("7 points by optimizing\n");
		for(int i=0;i<filename.length;i++) {
			bReader.close();
			bReader=new BufferedReader(new FileReader(new File(filename[i])));
			String lineString2=bReader.readLine();
			ArrayList<Double> tmpArrayList=new ArrayList<Double>();
			double max=Double.MIN_VALUE;
			while(lineString2!=null) {
				String [] srStrings=lineString2.replace("\n","").split(",");
				if(max<Double.parseDouble(srStrings[0])) {
					max=Double.parseDouble(srStrings[0]);
					tmpArrayList.clear();
					tmpArrayList.add(Double.parseDouble(srStrings[1]));
					tmpArrayList.add(Double.parseDouble(srStrings[2]));
					tmpArrayList.add(Double.parseDouble(srStrings[3]));
				}
				lineString2=bReader.readLine();
			}
			dataMap1.put(max, tmpArrayList);
			bwBufferedWriter.write(max+",[");
			bwBufferedWriter.write((tmpArrayList.get(0)*data.longitudeStdev+data.longitudeMean)+",");
			bwBufferedWriter.write((tmpArrayList.get(1)*data.latitudeStdev+data.latitudeMean)+",");
			Timestamp timestamp=new Timestamp((long)(tmpArrayList.get(2)*data.timestampStdev+data.timestampMean));
			bwBufferedWriter.write((timestamp)+"]\n");
		}
		bwBufferedWriter.write("Top10 optimal Points\n");
		for(int i=0;i<10;i++) {
			bwBufferedWriter.write(dataMap1.lastKey()+",[");
			Timestamp timestamp=new Timestamp((long)(dataMap1.get(dataMap1.lastKey()).get(2)*data.timestampStdev+data.timestampMean));
			bwBufferedWriter.write(""+(dataMap1.get(dataMap1.lastKey()).get(0)*data.longitudeStdev+data.longitudeMean)+",");
			bwBufferedWriter.write(""+(dataMap1.get(dataMap1.lastKey()).get(1)*data.latitudeStdev+data.latitudeMean)+",");
			bwBufferedWriter.write(""+timestamp+"]\n");
			dataMap1.remove(dataMap1.lastKey());
		}
		bwBufferedWriter.close();
	}
	public static void main(String [] args) throws FileNotFoundException,IOException {
		KDE kde=new KDE();
		//kde.getCharacterPonits(44000);
		kde.readFile("data/CharacterPoints.csv");
//		kde.gredientDecent("data/l0.0000000000001.csv",-0.0000000000001);
//		kde.gredientDecent("data/l0.000000000001.csv",-0.000000000001);
//		kde.gredientDecent("data/l0.00000000001.csv",-0.00000000001);
//		kde.gredientDecent("data/l0.0000000001.csv",-0.0000000001);
//		kde.gredientDecent("data/l0.000000001.csv",-0.000000001);
//		kde.gredientDecent("data/l0.00000001.csv",-0.00000001);
//		kde.gredientDecent("data/l0.0000001.csv",-0.0000001);
//		String [] fileStrings= {"data/l0.00001.csv","data/l0.00002.csv","data/l0.00005.csv","data/l00.0001.csv",
//				"data/l00.0002.csv","data/l00.0005.csv","data/l000.001.csv"};
//		kde.printOutCandidates(fileStrings);
		String [] fileStrings= {"data/l0.0000000000001.csv","data/l0.000000000001.csv","data/l0.00000000001.csv","data/l0.0000000001.csv",
				"data/l0.000000001.csv","data/l0.00000001.csv","data/l0.0000001.csv"};
		kde.printOutCandidates(fileStrings);
//		int [] ir=kde.characterPointsCount(0.0001);
//		BufferedWriter bWriter=new BufferedWriter(new FileWriter(new File("data/characterCounts.csv")));
//		for(int i=0;i<ir.length;i++) {
//			bWriter.write(kde.longitude_k_zero.get(i)+","+kde.latitude_k_zero.get(i)+","+kde.timestamp_k_zero.get(i)+","+ir[i]+"\n");
//		}
//		bWriter.close();
	}
}
